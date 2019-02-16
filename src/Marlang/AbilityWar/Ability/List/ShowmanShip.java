package Marlang.AbilityWar.Ability.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Utils.Math.LocationUtil;
import Marlang.AbilityWar.Utils.Thread.TimerBase;

public class ShowmanShip extends AbilityBase {

	public ShowmanShip(Player player) {
		super(player, "��ǽ�", Rank.A,
				ChatColor.translateAlternateColorCodes('&', "&f�ֺ� 10ĭ �̳��� �ִ� ��� ���� ���� ȿ���� �޽��ϴ�."),
				ChatColor.translateAlternateColorCodes('&', "&a1�� ���� &7: &f������  &a2�� �̻� &7: &f�� I  &a3�� �̻� &7: &f�� II"));
	}

	TimerBase Passive = new TimerBase() {
		
		@Override
		public void onStart() {}
		
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
		public void onEnd() {}
		
	}.setPeriod(5);
	
	@Override
	public boolean ActiveSkill(ActiveMaterialType mt, ActiveClickType ct) {
		return false;
	}

	@Override
	public void PassiveSkill(Event event) {}

	@Override
	public void onRestrictClear() {
		Passive.StartTimer();
	}

}
