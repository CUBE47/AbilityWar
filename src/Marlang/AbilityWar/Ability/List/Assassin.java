package Marlang.AbilityWar.Ability.List;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Ability.Timer.CooldownTimer;
import Marlang.AbilityWar.Config.AbilitySettings.SettingObject;
import Marlang.AbilityWar.Utils.Messager;
import Marlang.AbilityWar.Utils.Library.SoundLib;
import Marlang.AbilityWar.Utils.Math.LocationUtil;
import Marlang.AbilityWar.Utils.Thread.TimerBase;

public class Assassin extends AbilityBase {
	
	public static SettingObject<Integer> DamageConfig = new SettingObject<Integer>("�ϻ���", "Damage", 10, 
			"# ��ų ������") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 0;
		}
		
	};
	
	public static SettingObject<Integer> CooldownConfig = new SettingObject<Integer>("�ϻ���", "Cooldown", 60, 
			"# ��Ÿ��") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 0;
		}
		
	};
	
	public static SettingObject<Integer> TeleportCountConfig = new SettingObject<Integer>("�ϻ���", "TeleportCount", 4,
			"# �ɷ� ��� �� �ڷ���Ʈ Ƚ��") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 1;
		}
		
	};
	
	public Assassin(Player player) {
		super(player, "�ϻ���", Rank.A,
				ChatColor.translateAlternateColorCodes('&', "&fö���� ��Ŭ���ϸ� �ֺ��� �ִ� �� " + TeleportCountConfig.getValue() + "���� �ڷ���Ʈ�ϸ�"),
				ChatColor.translateAlternateColorCodes('&', "&f�������� �ݴϴ�. " + Messager.formatCooldown(CooldownConfig.getValue())));
	}
	
	CooldownTimer Cool = new CooldownTimer(this, CooldownConfig.getValue());
	
	TimerBase Duration = new TimerBase(TeleportCountConfig.getValue()) {
		
		ArrayList<Damageable> Entities = new ArrayList<Damageable>();
		
		Integer Damage = DamageConfig.getValue();
		
		@Override
		public void onStart() {
			Entities.addAll(LocationUtil.getNearbyDamageableEntities(getPlayer(), 6, 3));
		}
		
		@Override
		public void TimerProcess(Integer Seconds) {
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
		
		@Override
		public void onEnd() {}
		
	}.setPeriod(5);
	
	@Override
	public boolean ActiveSkill(ActiveMaterialType mt, ActiveClickType ct) {
		if(mt.equals(ActiveMaterialType.Iron_Ingot)) {
			if(ct.equals(ActiveClickType.RightClick)) {
				if(!Cool.isCooldown()) {
					Duration.StartTimer();
					
					Cool.StartTimer();
					
					return true;
				}
			}
		}
		
		return false;
	}
	
	@Override
	public void PassiveSkill(Event event) {}

	@Override
	public void onRestrictClear() {}
	
}
