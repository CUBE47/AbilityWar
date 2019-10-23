package daybreak.abilitywar.ability.list;

import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import daybreak.abilitywar.ability.AbilityBase;
import daybreak.abilitywar.ability.AbilityManifest;
import daybreak.abilitywar.ability.SubscribeEvent;
import daybreak.abilitywar.ability.AbilityManifest.Rank;
import daybreak.abilitywar.ability.AbilityManifest.Species;
import daybreak.abilitywar.ability.timer.CooldownTimer;
import daybreak.abilitywar.config.AbilitySettings.SettingObject;
import daybreak.abilitywar.game.games.mode.AbstractGame.Participant;
import daybreak.abilitywar.utils.Messager;
import daybreak.abilitywar.utils.library.ParticleLib;
import daybreak.abilitywar.utils.math.LocationUtil;
import daybreak.abilitywar.utils.thread.TimerBase;

@AbilityManifest(Name = "보이드", Rank = Rank.A, Species = Species.OTHERS)
public class Void extends AbilityBase {

	public static SettingObject<Integer> CooldownConfig = new SettingObject<Integer>(Void.class, "Cooldown", 80,
			"# 쿨타임") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 0;
		}
		
	};

	public Void(Participant participant) {
		super(participant,
				ChatColor.translateAlternateColorCodes('&', "&f공허의 존재 보이드. 철괴를 우클릭하면 보이드가 공허를 통하여"),
				ChatColor.translateAlternateColorCodes('&', "제일 가까이 있는 플레이어에게 텔레포트합니다. " + Messager.formatCooldown(CooldownConfig.getValue())),
				ChatColor.translateAlternateColorCodes('&', "&f텔레포트를 하고 난 후 5초간 데미지를 입지 않습니다."));
	}

	private boolean Inv = false;
	
	private CooldownTimer Cool = new CooldownTimer(this, CooldownConfig.getValue());
	
	private TimerBase Invincibility = new TimerBase(5) {
		
		@Override
		public void onStart() {
			Inv = true;
		}
		
		@Override
		public void onProcess(int Seconds) {}
		
		@Override
		public void onEnd() {
			Inv = false;
		}
		
	};
	
	@Override
	public boolean ActiveSkill(MaterialType mt, ClickType ct) {
		if(mt.equals(MaterialType.Iron_Ingot)) {
			if(ct.equals(ClickType.RightClick)) {
				if(!Cool.isCooldown()) {
					Player target = LocationUtil.getNearestPlayer(getPlayer());

					if(target != null) {
						getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&e" + target.getName() + "&f님에게 텔레포트합니다."));
						getPlayer().teleport(target);
						ParticleLib.DRAGON_BREATH.spawnParticle(getPlayer().getLocation(), 1, 1, 1, 20);
						
						Invincibility.StartTimer();
						
						Cool.StartTimer();
					} else {
						getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&a가장 가까운 플레이어&f가 존재하지 않습니다."));
					}
				}
			}
		}
		
		return false;
	}

	@SubscribeEvent
	public void onEntityDamage(EntityDamageEvent e) {
		if(e.getEntity().equals(getPlayer())) {
			if(this.Inv) {
				e.setCancelled(true);
				ParticleLib.DRAGON_BREATH.spawnParticle(getPlayer().getLocation(), 1, 1, 1, 20);
			}
		}
	}

	@SubscribeEvent
	public void onEntityDamage(EntityDamageByEntityEvent e) {
		if(e.getEntity().equals(getPlayer())) {
			if(this.Inv) {
				e.setCancelled(true);
				ParticleLib.DRAGON_BREATH.spawnParticle(getPlayer().getLocation(), 1, 1, 1, 20);
			}
		}
	}
	
	@SubscribeEvent
	public void onEntityDamage(EntityDamageByBlockEvent e) {
		if(e.getEntity().equals(getPlayer())) {
			if(this.Inv) {
				e.setCancelled(true);
				ParticleLib.DRAGON_BREATH.spawnParticle(getPlayer().getLocation(), 1, 1, 1, 20);
			}
		}
	}
	
	@Override
	public void onRestrictClear() {}

	@Override
	public void TargetSkill(MaterialType mt, LivingEntity entity) {}
	
}
