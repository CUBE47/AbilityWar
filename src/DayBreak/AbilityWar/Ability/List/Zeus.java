package DayBreak.AbilityWar.Ability.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import DayBreak.AbilityWar.Ability.AbilityBase;
import DayBreak.AbilityWar.Ability.AbilityManifest;
import DayBreak.AbilityWar.Ability.AbilityManifest.Rank;
import DayBreak.AbilityWar.Ability.AbilityManifest.Species;
import DayBreak.AbilityWar.Ability.SubscribeEvent;
import DayBreak.AbilityWar.Ability.Timer.CooldownTimer;
import DayBreak.AbilityWar.Config.AbilitySettings.SettingObject;
import DayBreak.AbilityWar.Game.Games.Mode.AbstractGame.Participant;
import DayBreak.AbilityWar.Utils.Messager;
import DayBreak.AbilityWar.Utils.Math.LocationUtil;
import DayBreak.AbilityWar.Utils.Math.Geometry.Circle;
import DayBreak.AbilityWar.Utils.Thread.TimerBase;

@AbilityManifest(Name = "���콺", Rank = Rank.S, Species = Species.GOD)
public class Zeus extends AbilityBase {
	
	public static SettingObject<Integer> CooldownConfig = new SettingObject<Integer>(Zeus.class, "Cooldown", 180,
			"# ��Ÿ��") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 0;
		}
		
	};
	
	public Zeus(Participant participant) {
		super(participant,
				ChatColor.translateAlternateColorCodes('&', "&f������ �� ���콺."),
				ChatColor.translateAlternateColorCodes('&', "&fö���� ��Ŭ���ϸ� �ֺ��� ������ ����߸��� ������ ����ŵ�ϴ�. " + Messager.formatCooldown(CooldownConfig.getValue())),
				ChatColor.translateAlternateColorCodes('&', "&f������ ���� �÷��̾�� 3�ʰ� ���ӵ˴ϴ�."),
				ChatColor.translateAlternateColorCodes('&', "&f���� �������� ���� �������� ���� �ʽ��ϴ�."));
	}
	
	private CooldownTimer Cool = new CooldownTimer(this, CooldownConfig.getValue());
	
	private TimerBase Skill = new TimerBase(5) {

		Location center;
		
		@Override
		public void onStart() {
			center = getPlayer().getLocation();
		}
		
		@Override
		public void TimerProcess(Integer Seconds) {
			Circle circle = new Circle(center, 3 + (2 * (5 - getCount()))).setAmount(7).setHighestLocation(true);
			for(Location l : circle.getLocations()) {
				l.getWorld().strikeLightningEffect(l);
				for(Damageable d : LocationUtil.getNearbyDamageableEntities(l, 2, 2)) {
					if(!d.equals(getPlayer())) {
						d.damage(d.getHealth() / 5, getPlayer());
						if(d instanceof Player) {
							Zeus.this.getGame().getEffectManager().Stun((Player) d, 60);
						}
					}
				}
				l.getWorld().createExplosion(l, 3);
			}
		}
		
		@Override
		public void onEnd() {}
		
	}.setPeriod(4);
	
	@Override
	public boolean ActiveSkill(MaterialType mt, ClickType ct) {
		if(mt.equals(MaterialType.Iron_Ingot)) {
			if(ct.equals(ClickType.RightClick)) {
				if(!Cool.isCooldown()) {
					Skill.StartTimer();
					
					Cool.StartTimer();
					
					return true;
				}
			}
		}
		
		return false;
	}

	@SubscribeEvent
	public void onEntityDamage(EntityDamageEvent e) {
		if(e.getEntity().equals(getPlayer())) {
			if(e.getCause().equals(DamageCause.LIGHTNING) || e.getCause().equals(DamageCause.BLOCK_EXPLOSION) || e.getCause().equals(DamageCause.ENTITY_EXPLOSION)) {
				e.setCancelled(true);
			}
		}
	}

	@SubscribeEvent
	public void onEntityDamage(EntityDamageByEntityEvent e) {
		if(e.getEntity().equals(getPlayer())) {
			if(e.getCause().equals(DamageCause.LIGHTNING) || e.getCause().equals(DamageCause.BLOCK_EXPLOSION) || e.getCause().equals(DamageCause.ENTITY_EXPLOSION)) {
				e.setCancelled(true);
			}
		}
	}

	@SubscribeEvent
	public void onEntityDamage(EntityDamageByBlockEvent e) {
		if(e.getEntity().equals(getPlayer())) {
			if(e.getCause().equals(DamageCause.LIGHTNING) || e.getCause().equals(DamageCause.BLOCK_EXPLOSION) || e.getCause().equals(DamageCause.ENTITY_EXPLOSION)) {
				e.setCancelled(true);
			}
		}
	}
	
	@Override
	public void onRestrictClear() {}
	
	@Override
	public void TargetSkill(MaterialType mt, Entity entity) {}
	
}
