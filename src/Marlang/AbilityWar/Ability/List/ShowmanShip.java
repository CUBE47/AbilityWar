package Marlang.AbilityWar.Ability.List;

import org.bukkit.ChatColor;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Utils.LocationUtil;
import Marlang.AbilityWar.Utils.TimerBase;

public class ShowmanShip extends AbilityBase {

	public ShowmanShip() {
		super("��ǽ�", Rank.A,
				ChatColor.translateAlternateColorCodes('&', "&f�ֺ� 10ĭ �̳��� �ִ� ��� ���� ���� ȿ���� �޽��ϴ�."),
				ChatColor.translateAlternateColorCodes('&', "&a1�� ���� &7: &f������  &a2�� �̻� &7: &f�� I  &a3�� �̻� &7: &f�� II"));
		
		Passive.setPeriod(5);
		registerTimer(Passive);
	}

	TimerBase Passive = new TimerBase() {
		
		@Override
		public void TimerStart(Data<?>... args) {}
		
		@Override
		public void TimerProcess(Integer Seconds) {
			Integer Count = LocationUtil.getNearbyPlayers(getPlayer(), 10, 10).size();
			
			if(Count <= 1) {
				getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20, 0), true);
			} else if(Count > 1 && Count <= 2) {
				getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20, 0), true);
			} else {
				getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20, 1), true);
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
			Passive.StartTimer();
		}
	}

}
