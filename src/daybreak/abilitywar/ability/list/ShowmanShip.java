package daybreak.abilitywar.ability.list;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import daybreak.abilitywar.ability.AbilityBase;
import daybreak.abilitywar.ability.AbilityManifest;
import daybreak.abilitywar.ability.AbilityManifest.Rank;
import daybreak.abilitywar.ability.AbilityManifest.Species;
import daybreak.abilitywar.game.games.mode.AbstractGame.Participant;
import daybreak.abilitywar.utils.library.EffectLib;
import daybreak.abilitywar.utils.library.ParticleLib;
import daybreak.abilitywar.utils.library.ParticleLib.RGB;
import daybreak.abilitywar.utils.math.LocationUtil;
import daybreak.abilitywar.utils.math.geometry.Circle;
import daybreak.abilitywar.utils.thread.TimerBase;

@AbilityManifest(Name = "쇼맨쉽", Rank = Rank.A, Species = Species.HUMAN)
public class ShowmanShip extends AbilityBase {

	public ShowmanShip(Participant participant) {
		super(participant,
				ChatColor.translateAlternateColorCodes('&', "&f주변 10칸 이내에 있는 사람 수에 따라 효과를 받습니다."),
				ChatColor.translateAlternateColorCodes('&', "&a1명 이하 &7: &f나약함  &a2명 이상 &7: &f힘 II  &a3명 이상 &7: &f힘 III"));
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