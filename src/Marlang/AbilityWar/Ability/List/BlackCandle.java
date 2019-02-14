package Marlang.AbilityWar.Ability.List;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffectType;

import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Config.AbilitySettings.SettingObject;
import Marlang.AbilityWar.Utils.TimerBase;

public class BlackCandle extends AbilityBase {

	public static SettingObject<Integer> ChanceConfig = new SettingObject<Integer>("��������", "Chance", 10,
			"# �������� �޾��� �� ü���� ȸ���� Ȯ��") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 1 && value <= 100;
		}
		
	};

	public BlackCandle(Player player) {
		super(player, "���� ����", Rank.B,
				ChatColor.translateAlternateColorCodes('&', "&f������� ���� ������, �������� ������ " + ChanceConfig.getValue() + "% Ȯ���� ü�� 1.5ĭ�� ȸ���մϴ�."));
	}

	TimerBase NoDebuff = new TimerBase() {
		
		@Override
		public void TimerStart(Data<?>... args) {}
		
		@Override
		public void TimerProcess(Integer Seconds) {
			getPlayer().removePotionEffect(PotionEffectType.BLINDNESS);
			getPlayer().removePotionEffect(PotionEffectType.CONFUSION);
			getPlayer().removePotionEffect(PotionEffectType.GLOWING);
			getPlayer().removePotionEffect(PotionEffectType.HARM);
			getPlayer().removePotionEffect(PotionEffectType.HUNGER);
			getPlayer().removePotionEffect(PotionEffectType.POISON);
			getPlayer().removePotionEffect(PotionEffectType.SLOW);
			getPlayer().removePotionEffect(PotionEffectType.SLOW_DIGGING);
			getPlayer().removePotionEffect(PotionEffectType.UNLUCK);
			getPlayer().removePotionEffect(PotionEffectType.WEAKNESS);
			getPlayer().removePotionEffect(PotionEffectType.WITHER);
		}
		
		@Override
		public void TimerEnd() {}
		
	}.setPeriod(5);
	
	@Override
	public boolean ActiveSkill(ActiveMaterialType mt, ActiveClickType ct) {
		return false;
	}

	@Override
	public void PassiveSkill(Event event) {
		if(event instanceof EntityDamageEvent) {
			EntityDamageEvent e = (EntityDamageEvent) event;
			if(e.getEntity().equals(getPlayer())) {
				Random r = new Random();
				if(r.nextInt(10) == 0) {
					Double Health = getPlayer().getHealth() + 1.5;
					if(Health > 20) Health = 20.0;
					
					if(!getPlayer().isDead()) {
						getPlayer().setHealth(Health);
					}
				}
			}
		}
	}

	@Override
	public void onRestrictClear() {
		NoDebuff.StartTimer();
	}

}
