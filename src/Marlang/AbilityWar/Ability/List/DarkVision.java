package Marlang.AbilityWar.Ability.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Ability.AbilityManifest;
import Marlang.AbilityWar.Ability.AbilityManifest.Rank;
import Marlang.AbilityWar.Config.AbilitySettings.SettingObject;
import Marlang.AbilityWar.Game.Games.AbstractGame.Participant;
import Marlang.AbilityWar.Utils.Library.EffectLib;
import Marlang.AbilityWar.Utils.Math.LocationUtil;
import Marlang.AbilityWar.Utils.Thread.TimerBase;

@AbilityManifest(Name = "�ɾ�", Rank = Rank.D)
public class DarkVision extends AbilityBase {
	
	public static SettingObject<Integer> DistanceConfig = new SettingObject<Integer>(DarkVision.class, "Distance", 30,
			"# �Ÿ� ����") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 1;
		}
		
	};
	
	public DarkVision(Participant participant) {
		super(participant,
				ChatColor.translateAlternateColorCodes('&', "&f���� ������ �ʴ� ���, �÷��̾��� " + DistanceConfig.getValue() + "ĭ �ȿ� �ִ� �÷��̾����"),
				ChatColor.translateAlternateColorCodes('&', "&f�߱� ȿ���� ����˴ϴ�."));
	}

	TimerBase Dark = new TimerBase() {
		
		@Override
		public void onStart() {}
		
		@Override
		public void TimerProcess(Integer Seconds) {
			EffectLib.BLINDNESS.addPotionEffect(getPlayer(), 40, 0, true);
			EffectLib.SPEED.addPotionEffect(getPlayer(), 40, 3, true);
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
				EffectLib.GLOWING.addPotionEffect(p, 10, 0, true);
			}
		}
		
		@Override
		public void onEnd() {}
		
	}.setPeriod(2);
	
	@Override
	public boolean ActiveSkill(MaterialType mt, ClickType ct) {
		return false;
	}

	@Override
	public void PassiveSkill(Event event) {}

	@Override
	public void onRestrictClear() {
		Dark.StartTimer();
		Vision.StartTimer();
	}

	@Override
	public void TargetSkill(MaterialType mt, Entity entity) {}

}
