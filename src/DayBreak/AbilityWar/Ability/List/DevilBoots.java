package DayBreak.AbilityWar.Ability.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerMoveEvent;

import DayBreak.AbilityWar.Ability.AbilityBase;
import DayBreak.AbilityWar.Ability.AbilityManifest;
import DayBreak.AbilityWar.Ability.AbilityManifest.Rank;
import DayBreak.AbilityWar.Ability.AbilityManifest.Species;
import DayBreak.AbilityWar.Ability.SubscribeEvent;
import DayBreak.AbilityWar.Game.Games.Mode.AbstractGame.Participant;
import DayBreak.AbilityWar.Utils.Library.EffectLib;
import DayBreak.AbilityWar.Utils.Thread.TimerBase;

@AbilityManifest(Name = "�Ǹ��� ����", Rank = Rank.B, Species = Species.HUMAN)
public class DevilBoots extends AbilityBase {

	public DevilBoots(Participant participant) {
		super(participant,
				ChatColor.translateAlternateColorCodes('&', "&f�ż��ϰ� �̵��ϸ� �������� �ڸ��� ���� �ٽ��ϴ�. ȭ�� ���ظ� ���� �ʽ��ϴ�."));
	}
	
	@Override
	public boolean ActiveSkill(MaterialType mt, ClickType ct) {
		return false;
	}
	
	private TimerBase speed = new TimerBase() {
		
		@Override
		protected void onStart() {}
		
		@Override
		protected void onEnd() {}
		
		@Override
		protected void TimerProcess(Integer Seconds) {
			EffectLib.SPEED.addPotionEffect(getPlayer(), 20, 1, true);
		}
	};
	
	@SubscribeEvent
	public void onPlayerMove(PlayerMoveEvent e) {
		if(e.getPlayer().equals(getPlayer())) {
			Location To = e.getTo();
			if(To.getBlock().getType().equals(Material.AIR)) {
				To.getBlock().setType(Material.FIRE);
			}
		}
	}
	
	@SubscribeEvent
	public void onEntityDamage(EntityDamageEvent e) {
		if(e.getEntity().equals(getPlayer())) {
			DamageCause cause = e.getCause();
			if(cause.equals(DamageCause.FIRE) || cause.equals(DamageCause.FIRE_TICK) || cause.equals(DamageCause.LAVA)) {
				e.setCancelled(true);
			}
		}
	}
	
	@Override
	public void onRestrictClear() {
		speed.StartTimer();
	}

	@Override
	public void TargetSkill(MaterialType mt, LivingEntity entity) {}
	
}
