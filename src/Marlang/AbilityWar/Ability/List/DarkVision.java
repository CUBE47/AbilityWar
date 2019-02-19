package Marlang.AbilityWar.Ability.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Config.AbilitySettings.SettingObject;
import Marlang.AbilityWar.Utils.Math.LocationUtil;
import Marlang.AbilityWar.Utils.Thread.TimerBase;
import Marlang.AbilityWar.Utils.VersionCompat.PlayerCompat;
import Marlang.AbilityWar.Utils.VersionCompat.PotionEffectType;

public class DarkVision extends AbilityBase {
	
	public static SettingObject<Integer> DistanceConfig = new SettingObject<Integer>("�ɾ�", "Distance", 30,
			"# �Ÿ� ����") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 1;
		}
		
	};
	
	public DarkVision(Player player) {
		super(player, "�ɾ�", Rank.C,
				ChatColor.translateAlternateColorCodes('&', "&f���� ������ �ʴ� ���, �÷��̾��� " + DistanceConfig.getValue() + "ĭ �ȿ� �ִ� �÷��̾����"),
				ChatColor.translateAlternateColorCodes('&', "&f�߱� ȿ���� ����˴ϴ�."));
	}

	TimerBase Dark = new TimerBase() {
		
		@Override
		public void onStart() {}
		
		@Override
		public void TimerProcess(Integer Seconds) {
			PlayerCompat.addPotionEffect(getPlayer(), PotionEffectType.BLINDNESS, 40, 0, true);
			PlayerCompat.addPotionEffect(getPlayer(), PotionEffectType.SPEED, 40, 3, true);
		}
		
		@Override
		public void onEnd() {}
		
	}.setPeriod(2);
	
	TimerBase Vision = new TimerBase() {
		
		Integer Distance = DistanceConfig.getValue();
		
		@Override
		public void onStart() {}
		
		@Override
		public void TimerProcess(Integer Seconds) {
			for(Player p : LocationUtil.getNearbyPlayers(getPlayer(), Distance, Distance)) {
				PlayerCompat.addPotionEffect(p, PotionEffectType.GLOWING, 10, 0, true);
			}
		}
		
		@Override
		public void onEnd() {}
		
	}.setPeriod(2);
	
	@Override
	public boolean ActiveSkill(ActiveMaterialType mt, ActiveClickType ct) {
		return false;
	}

	@Override
	public void PassiveSkill(Event event) {}

	@Override
	public void onRestrictClear() {
		Dark.StartTimer();
		Vision.StartTimer();
	}

}
