package DayBreak.AbilityWar.Ability.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

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
import DayBreak.AbilityWar.Utils.Library.SoundLib;
import DayBreak.AbilityWar.Utils.Thread.TimerBase;

@AbilityManifest(Name = "베르투스", Rank = Rank.A, Species = Species.HUMAN)
public class Virtus extends AbilityBase {

	public static SettingObject<Integer> DurationConfig = new SettingObject<Integer>(Virtus.class, "Duration", 5,
			"# 능력 지속시간") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 1;
		}
		
	};

	public static SettingObject<Integer> CooldownConfig = new SettingObject<Integer>(Virtus.class, "Cooldown", 70,
			"# 쿨타임") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 0;
		}
		
	};
	
	public Virtus(Participant participant) {
		super(participant,
				ChatColor.translateAlternateColorCodes('&', "&f철괴를 우클릭하면 다음 " + DurationConfig.getValue() + "&f초간 받는 데미지가 75% 감소합니다. " + Messager.formatCooldown(CooldownConfig.getValue())));
	}

	private CooldownTimer Cool = new CooldownTimer(this, CooldownConfig.getValue());
	
	private boolean Activated = false;
	
	private TimerBase Activate = new TimerBase(DurationConfig.getValue()) {
		
		@Override
		public void onStart() {
			Activated = true;
		}
		
		@Override
		public void TimerProcess(Integer Seconds) {
			SoundLib.BLOCK_ANVIL_LAND.playSound(getPlayer());
			ParticleLib.LAVA.spawnParticle(getPlayer().getLocation(), 3, 3, 3, 10);
		}
		
		@Override
		public void onEnd() {
			Activated = false;
		}
		
	};
	
	@Override
	public boolean ActiveSkill(MaterialType mt, ClickType ct) {
		if(mt.equals(MaterialType.Iron_Ingot)) {
			if(ct.equals(ClickType.RightClick)) {
				if(!Cool.isCooldown()) {
					Activate.StartTimer();
					
					Cool.StartTimer();
					
					return true;
				}
			}
		}
		
		return false;
	}

	@SubscribeEvent
	public void onEntityDamage(EntityDamageEvent e) {
		if(e.getEntity().equals(getPlayer()) && Activated) {
			e.setDamage(e.getDamage() / 4);
		}
	}

	@SubscribeEvent
	public void onEntityDamage(EntityDamageByEntityEvent e) {
		if(e.getEntity().equals(getPlayer()) && Activated) {
			e.setDamage(e.getDamage() / 4);
		}
	}

	@SubscribeEvent
	public void onEntityDamage(EntityDamageByBlockEvent e) {
		if(e.getEntity().equals(getPlayer()) && Activated) {
			e.setDamage(e.getDamage() / 4);
		}
	}

	@Override
	public void onRestrictClear() {}

	@Override
	public void TargetSkill(MaterialType mt, LivingEntity entity) {}
	
}
