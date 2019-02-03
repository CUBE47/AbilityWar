package Marlang.AbilityWar.Ability.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Config.AbilitySettings.SettingObject;
import Marlang.AbilityWar.Utils.LocationUtil;
import Marlang.AbilityWar.Utils.TimerBase;

public class DarkVision extends AbilityBase {
	
	public static SettingObject<Integer> DistanceConfig = new SettingObject<Integer>("�ɾ�", "Distance", 30,
			"# �Ÿ� ����") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 1;
		}
		
	};
	
	public DarkVision() {
		super("�ɾ�", Rank.C,
				ChatColor.translateAlternateColorCodes('&', "&f���� ������ �ʴ� ���, �÷��̾��� " + DistanceConfig.getValue() + "ĭ �ȿ� �ִ� �÷��̾����"),
				ChatColor.translateAlternateColorCodes('&', "&f�߱� ȿ���� ����˴ϴ�."));
		
		Dark.setPeriod(2);
		
		registerTimer(Dark);
		
		Vision.setPeriod(2);

		registerTimer(Vision);
	}

	TimerBase Dark = new TimerBase() {
		
		@Override
		public void TimerStart(Data<?>... args) {}
		
		@Override
		public void TimerProcess(Integer Seconds) {
			getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 0), true);
			getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 40, 2), true);
		}
		
		@Override
		public void TimerEnd() {}
		
	};
	
	TimerBase Vision = new TimerBase() {
		
		Integer Distance = DistanceConfig.getValue();
		
		@Override
		public void TimerStart(Data<?>... args) {}
		
		@Override
		public void TimerProcess(Integer Seconds) {
			for(Player p : LocationUtil.getNearbyPlayers(getPlayer(), Distance, Distance)) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 10, 0), true);
			}
		}
		
		@Override
		public void TimerEnd() {}
		
	};
	
	@Override
	public boolean ActiveSkill(ActiveMaterialType mt, ActiveClickType ct) {
		return false;
	}

	@Override
	public void PassiveSkill(Event event) {}

	@Override
	public void AbilityEvent(EventType type) {
		if(type.equals(EventType.RestrictClear)) {
			Dark.StartTimer();
			Vision.StartTimer();
		}
	}

}
