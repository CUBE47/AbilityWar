package Marlang.AbilityWar.Ability.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Utils.Math.LocationUtil;
import Marlang.AbilityWar.Utils.Thread.TimerBase;
import Marlang.AbilityWar.Utils.VersionCompat.PlayerCompat;
import Marlang.AbilityWar.Utils.VersionCompat.PotionEffectType;

public class ShowmanShip extends AbilityBase {

	public ShowmanShip(Player player) {
		super(player, "��ǽ�", Rank.A,
				ChatColor.translateAlternateColorCodes('&', "&f�ֺ� 10ĭ �̳��� �ִ� ��� ���� ���� ȿ���� �޽��ϴ�."),
				ChatColor.translateAlternateColorCodes('&', "&a1�� ���� &7: &f������  &a2�� �̻� &7: &f�� II  &a3�� �̻� &7: &f�� III"));
	}

	TimerBase Passive = new TimerBase() {
		
		@Override
		public void onStart() {}
		
		@Override
		public void TimerProcess(Integer Seconds) {
			Integer Count = LocationUtil.getNearbyPlayers(getPlayer(), 10, 10).size();
			
			if(Count <= 1) {
				PlayerCompat.addPotionEffect(getPlayer(), PotionEffectType.WEAKNESS, 20, 0, true);
			} else if(Count > 1 && Count <= 2) {
				PlayerCompat.addPotionEffect(getPlayer(), PotionEffectType.INCREASE_DAMAGE, 20, 1, true);
			} else {
				PlayerCompat.addPotionEffect(getPlayer(), PotionEffectType.INCREASE_DAMAGE, 20, 2, true);
			}
		}
		
		@Override
		public void onEnd() {}
		
	}.setPeriod(5);
	
	@Override
	public boolean ActiveSkill(MaterialType mt, ClickType ct) {
		return false;
	}

	@Override
	public void PassiveSkill(Event event) {}

	@Override
	public void onRestrictClear() {
		Passive.StartTimer();
	}

	@Override
	public void TargetSkill(MaterialType mt, Entity entity) {}
	
}
