package DayBreak.AbilityWar.Ability.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import DayBreak.AbilityWar.Ability.AbilityBase;
import DayBreak.AbilityWar.Ability.AbilityManifest;
import DayBreak.AbilityWar.Ability.AbilityManifest.Rank;
import DayBreak.AbilityWar.Ability.AbilityManifest.Species;
import DayBreak.AbilityWar.Game.Games.Mode.AbstractGame.Participant;
import DayBreak.AbilityWar.Utils.Library.EffectLib;
import DayBreak.AbilityWar.Utils.Library.ParticleLib;
import DayBreak.AbilityWar.Utils.Library.ParticleLib.RGB;
import DayBreak.AbilityWar.Utils.Math.LocationUtil;
import DayBreak.AbilityWar.Utils.Math.Geometry.Circle;
import DayBreak.AbilityWar.Utils.Thread.TimerBase;

@AbilityManifest(Name = "��ǽ�", Rank = Rank.A, Species = Species.HUMAN)
public class ShowmanShip extends AbilityBase {

	public ShowmanShip(Participant participant) {
		super(participant,
				ChatColor.translateAlternateColorCodes('&', "&f�ֺ� 10ĭ �̳��� �ִ� ��� ���� ���� ȿ���� �޽��ϴ�."),
				ChatColor.translateAlternateColorCodes('&', "&a1�� ���� &7: &f������  &a2�� �̻� &7: &f�� II  &a3�� �̻� &7: &f�� III"));
	}

	private final RGB WEAK = new RGB(214, 255, 212);
	private final RGB POWER = new RGB(255, 184, 150);
	private final RGB POWERFUL = new RGB(255, 59, 59);
	
	private TimerBase Passive = new TimerBase() {

		private Circle circle = new Circle(getPlayer().getLocation(), 10).setAmount(100).setHighestLocation(true);
		
		@Override
		public void onStart() {}
		
		@Override
		public void TimerProcess(Integer Seconds) {
			circle.setCenter(getPlayer().getLocation());
			final int Count = LocationUtil.getNearbyPlayers(getPlayer(), 10, 10).size();
			
			if(Count <= 1) {
				EffectLib.WEAKNESS.addPotionEffect(getPlayer(), 20, 0, true);
				for(Location l : circle.getLocations()) {
					ParticleLib.REDSTONE.spawnParticle(getPlayer(), l, WEAK, 0);
				}
			} else if(Count > 1 && Count <= 2) {
				EffectLib.INCREASE_DAMAGE.addPotionEffect(getPlayer(), 20, 1, true);
				for(Location l : circle.getLocations()) {
					ParticleLib.REDSTONE.spawnParticle(getPlayer(), l, POWER, 0);
				}
			} else {
				EffectLib.INCREASE_DAMAGE.addPotionEffect(getPlayer(), 20, 2, true);
				for(Location l : circle.getLocations()) {
					ParticleLib.REDSTONE.spawnParticle(getPlayer(), l, POWERFUL, 0);
				}
			}
		}
		
		@Override
		public void onEnd() {}
		
	}.setPeriod(1);
	
	@Override
	public boolean ActiveSkill(MaterialType mt, ClickType ct) {
		return false;
	}

	@Override
	public void onRestrictClear() {
		Passive.StartTimer();
	}

	@Override
	public void TargetSkill(MaterialType mt, LivingEntity entity) {}
	
}
