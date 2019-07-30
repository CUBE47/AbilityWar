package DayBreak.AbilityWar.Ability.List;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;

import DayBreak.AbilityWar.Ability.AbilityBase;
import DayBreak.AbilityWar.Ability.AbilityManifest;
import DayBreak.AbilityWar.Ability.AbilityManifest.Rank;
import DayBreak.AbilityWar.Ability.AbilityManifest.Species;
import DayBreak.AbilityWar.Ability.Timer.CooldownTimer;
import DayBreak.AbilityWar.Config.AbilitySettings.SettingObject;
import DayBreak.AbilityWar.Game.Games.Mode.AbstractGame.Participant;
import DayBreak.AbilityWar.Utils.Messager;
import DayBreak.AbilityWar.Utils.Library.SoundLib;
import DayBreak.AbilityWar.Utils.Math.LocationUtil;
import DayBreak.AbilityWar.Utils.Thread.TimerBase;

@AbilityManifest(Name = "�ϻ���", Rank = Rank.A, Species = Species.HUMAN)
public class Assassin extends AbilityBase {

	public static SettingObject<Integer> DistanceConfig = new SettingObject<Integer>(Assassin.class, "Distance", 6, 
			"# ��ų ������") {
		
		@Override
		public boolean Condition(Integer value) {
			return value > 0;
		}
		
	};
	
	public static SettingObject<Integer> DamageConfig = new SettingObject<Integer>(Assassin.class, "Damage", 12, 
			"# ��ų ������") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 0;
		}
		
	};
	
	public static SettingObject<Integer> CooldownConfig = new SettingObject<Integer>(Assassin.class, "Cooldown", 25,
			"# ��Ÿ��") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 0;
		}
		
	};
	
	public static SettingObject<Integer> TeleportCountConfig = new SettingObject<Integer>(Assassin.class, "TeleportCount", 4,
			"# �ɷ� ��� �� �ڷ���Ʈ Ƚ��") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 1;
		}
		
	};
	
	public Assassin(Participant participant) {
		super(participant,
				ChatColor.translateAlternateColorCodes('&', "&fö���� ��Ŭ���ϸ� 6ĭ �̳��� �ִ� �� " + TeleportCountConfig.getValue() + "���� �̵��ϸ�"),
				ChatColor.translateAlternateColorCodes('&', "&f�������� �ݴϴ�. " + Messager.formatCooldown(CooldownConfig.getValue())));
	}
	
	private CooldownTimer Cool = new CooldownTimer(this, CooldownConfig.getValue());

	private List<Damageable> Entities = null;
	
	private final int Distance = DistanceConfig.getValue();
	
	private TimerBase Duration = new TimerBase(TeleportCountConfig.getValue()) {
		
		Integer Damage = DamageConfig.getValue();
		
		@Override
		public void onStart() {}
		
		@Override
		public void TimerProcess(Integer Seconds) {
			if(Entities != null) {
				if(Entities.size() >= 1) {
					Damageable e = Entities.get(0);
					Entities.remove(e);
					getPlayer().teleport(e);
					e.damage(Damage, getPlayer());
					SoundLib.ENTITY_PLAYER_ATTACK_SWEEP.playSound(getPlayer());
					SoundLib.ENTITY_EXPERIENCE_ORB_PICKUP.playSound(getPlayer());
				} else {
					this.StopTimer(false);
				}
			}
		}
		
		@Override
		public void onEnd() {}
		
	}.setPeriod(3);
	
	@Override
	public boolean ActiveSkill(MaterialType mt, ClickType ct) {
		if(mt.equals(MaterialType.Iron_Ingot)) {
			if(ct.equals(ClickType.RightClick)) {
				if(!Cool.isCooldown()) {
					this.Entities = LocationUtil.getNearbyDamageableEntities(getPlayer(), Distance, 5);
					if(Entities.size() > 0) {
						Duration.StartTimer();
						Cool.StartTimer();
						return true;
					} else {
						Messager.sendMessage(getPlayer(), ChatColor.translateAlternateColorCodes('&', "&f" + Distance + "ĭ �̳��� &a��ƼƼ&f�� �������� �ʽ��ϴ�."));
					}
				}
			}
		}
		
		return false;
	}
	
	@Override
	public void onRestrictClear() {}

	@Override
	public void TargetSkill(MaterialType mt, Entity entity) {}
	
}
