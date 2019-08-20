package DayBreak.AbilityWar.Ability.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
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
import DayBreak.AbilityWar.Utils.Library.ParticleLib;
import DayBreak.AbilityWar.Utils.Library.ParticleLib.RGB;
import DayBreak.AbilityWar.Utils.Thread.TimerBase;

@AbilityManifest(Name = "������ ���Ŀ", Rank = Rank.A, Species = Species.HUMAN)
public class EnergyBlocker extends AbilityBase {

	public static SettingObject<Integer> CooldownConfig = new SettingObject<Integer>(EnergyBlocker.class, "Cooldown", 3, 
			"# ��Ÿ��") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 0;
		}
		
	};
	
	private boolean Default = true;
	
	public EnergyBlocker(Participant participant) {
		super(participant,
				ChatColor.translateAlternateColorCodes('&', "&f���Ÿ� ���� ���ظ� 1/3��, �ٰŸ� ���� ���ظ� �� ��� �ްų�"),
				ChatColor.translateAlternateColorCodes('&', "&f���Ÿ� ���� ���ظ� �� ���, �ٰŸ� ���� ���ظ� 1/3�� ���� �� �ֽ��ϴ�."),
				ChatColor.translateAlternateColorCodes('&', "&fö���� ��Ŭ���ϸ� ������ ���� ������ �ڹٲߴϴ�. " + Messager.formatCooldown(CooldownConfig.getValue())),
				ChatColor.translateAlternateColorCodes('&', "&fö���� ��Ŭ���ϸ� ���� ���¸� Ȯ���� �� �ֽ��ϴ�."));
	}
	
	private CooldownTimer Cool = new CooldownTimer(this, CooldownConfig.getValue());
	
	@Override
	public boolean ActiveSkill(MaterialType mt, ClickType ct) {
		if(mt.equals(MaterialType.Iron_Ingot)) {
			if(ct.equals(ClickType.RightClick)) {
				if(!Cool.isCooldown()) {
					Default = !Default;
					Player p = getPlayer();
					if(Default) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b���Ÿ� &f1/3&7, &a�ٰŸ� &f�� ��� ����Ǿ����ϴ�."));
					} else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b���Ÿ� &f�� ��&7, &a�ٰŸ� &f1/3�� ����Ǿ����ϴ�."));
					}
					
					Cool.StartTimer();
				}
			} else if(ct.equals(ClickType.LeftClick)) {
				if(Default) {
					Messager.sendMessage(getPlayer(), ChatColor.translateAlternateColorCodes('&', "&6���� ����&f: &b���Ÿ� &f1/3&7, &a�ٰŸ� &f�� ��"));
				} else {
					Messager.sendMessage(getPlayer(), ChatColor.translateAlternateColorCodes('&', "&6���� ����&f: &b���Ÿ� &f�� ��&7, &a�ٰŸ� &f1/3"));
				}
			}
		}
		
		return false;
	}

	private TimerBase Particle = new TimerBase() {

		@Override
		public void onStart() {}
		
		@Override
		public void TimerProcess(Integer Seconds) {
			if(Default) {
				ParticleLib.REDSTONE.spawnParticle(getPlayer().getLocation().add(0, 2.2, 0), new RGB(116, 237, 167), 0);
			} else {
				ParticleLib.REDSTONE.spawnParticle(getPlayer().getLocation().add(0, 2.2, 0), new RGB(85, 237, 242), 0);
			}
		}
		
		@Override
		public void onEnd() {}
		
	}.setPeriod(1);
	
	@SubscribeEvent
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
		if(e.getEntity().equals(getPlayer())) {
			DamageCause dc = e.getCause();
			if(dc != null) {
				if(dc.equals(DamageCause.PROJECTILE)) {
					if(Default) {
						e.setDamage(e.getDamage() / 3);
					} else {
						e.setDamage(e.getDamage() * 2);
					}
				} else if(dc.equals(DamageCause.ENTITY_ATTACK)) {
					if(Default) {
						e.setDamage(e.getDamage() * 2);
					} else {
						e.setDamage(e.getDamage() / 3);
					}
				}
			}
		}
	}
	
	@Override
	public void onRestrictClear() {
		Particle.StartTimer();
	}

	@Override
	public void TargetSkill(MaterialType mt, LivingEntity entity) {}
	
}
