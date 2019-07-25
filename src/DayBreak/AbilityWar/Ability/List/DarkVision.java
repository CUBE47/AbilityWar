package DayBreak.AbilityWar.Ability.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import DayBreak.AbilityWar.Ability.AbilityBase;
import DayBreak.AbilityWar.Ability.AbilityManifest;
import DayBreak.AbilityWar.Ability.AbilityManifest.Rank;
import DayBreak.AbilityWar.Ability.AbilityManifest.Species;
import DayBreak.AbilityWar.Config.AbilitySettings.SettingObject;
import DayBreak.AbilityWar.Game.Games.Mode.AbstractGame.Participant;
import DayBreak.AbilityWar.Utils.Library.EffectLib;
import DayBreak.AbilityWar.Utils.Math.LocationUtil;
import DayBreak.AbilityWar.Utils.Thread.TimerBase;

@AbilityManifest(Name = "�ɾ�", Rank = Rank.C, Species = Species.HUMAN)
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
				ChatColor.translateAlternateColorCodes('&', "&f�߱� ȿ���� ����˴ϴ�. ����, ������ �޸��� ���� ������ �� �ֽ��ϴ�."));
	}

	TimerBase Dark = new TimerBase() {
		
		@Override
		public void onStart() {}
		
		@Override
		public void TimerProcess(Integer Seconds) {
			EffectLib.BLINDNESS.addPotionEffect(getPlayer(), 40, 0, true);
			EffectLib.SPEED.addPotionEffect(getPlayer(), 40, 5, true);
			EffectLib.JUMP.addPotionEffect(getPlayer(), 40, 1, true);
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
