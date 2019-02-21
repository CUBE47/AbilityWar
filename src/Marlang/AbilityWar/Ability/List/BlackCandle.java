package Marlang.AbilityWar.Ability.List;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;

import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Config.AbilitySettings.SettingObject;
import Marlang.AbilityWar.Utils.Thread.TimerBase;
import Marlang.AbilityWar.Utils.VersionCompat.PlayerCompat;
import Marlang.AbilityWar.Utils.VersionCompat.PotionEffectType;

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
		public void onStart() {}
		
		@Override
		public void TimerProcess(Integer Seconds) {
			PlayerCompat.removePotionEffect(getPlayer(), PotionEffectType.BLINDNESS);
			PlayerCompat.removePotionEffect(getPlayer(), PotionEffectType.CONFUSION);
			PlayerCompat.removePotionEffect(getPlayer(), PotionEffectType.GLOWING);
			PlayerCompat.removePotionEffect(getPlayer(), PotionEffectType.HARM);
			PlayerCompat.removePotionEffect(getPlayer(), PotionEffectType.HUNGER);
			PlayerCompat.removePotionEffect(getPlayer(), PotionEffectType.POISON);
			PlayerCompat.removePotionEffect(getPlayer(), PotionEffectType.SLOW);
			PlayerCompat.removePotionEffect(getPlayer(), PotionEffectType.SLOW_DIGGING);
			PlayerCompat.removePotionEffect(getPlayer(), PotionEffectType.UNLUCK);
			PlayerCompat.removePotionEffect(getPlayer(), PotionEffectType.WEAKNESS);
			PlayerCompat.removePotionEffect(getPlayer(), PotionEffectType.WITHER);
		}
		
		@Override
		public void onEnd() {}
		
	}.setPeriod(5);
	
	@Override
	public boolean ActiveSkill(MaterialType mt, ClickType ct) {
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

	@Override
	public void TargetSkill(MaterialType mt, Entity entity) {}
	
}
