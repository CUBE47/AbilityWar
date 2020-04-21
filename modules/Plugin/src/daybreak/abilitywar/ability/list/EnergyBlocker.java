package daybreak.abilitywar.ability.list;

import daybreak.abilitywar.ability.AbilityBase;
import daybreak.abilitywar.ability.AbilityManifest;
import daybreak.abilitywar.ability.AbilityManifest.Rank;
import daybreak.abilitywar.ability.AbilityManifest.Species;
import daybreak.abilitywar.ability.Scheduled;
import daybreak.abilitywar.ability.SubscribeEvent;
import daybreak.abilitywar.ability.decorator.ActiveHandler;
import daybreak.abilitywar.game.AbstractGame.Participant;
import daybreak.abilitywar.game.AbstractGame.Participant.ActionbarNotification.ActionbarChannel;
import daybreak.abilitywar.utils.base.concurrent.TimeUnit;
import daybreak.abilitywar.utils.library.ParticleLib;
import daybreak.abilitywar.utils.library.ParticleLib.RGB;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

@AbilityManifest(name = "Energy blocker", rank = Rank.A, species = Species.HUMAN, explain = {
		"Choose to take ranged damage reduced to one-third and take doubled melee damage,",
		"or take melee damage reduced to one-third and take doubled ranged damage.",
		"Right-click iron ingot to change reducing damage type.",
		"Left-click iron ingot to see current status."
})
public class EnergyBlocker extends AbilityBase implements ActiveHandler {

	private boolean projectileBlocking = true;

	public EnergyBlocker(Participant participant) {
		super(participant);
	}

	private final ActionbarChannel actionbarChannel = newActionbarChannel();

	@Override
	public boolean ActiveSkill(Material materialType, ClickType clickType) {
		if (materialType.equals(Material.IRON_INGOT)) {
			if (clickType.equals(ClickType.RIGHT_CLICK)) {
				projectileBlocking = !projectileBlocking;
				getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', getState() + "로 변경되었습니다."));
				actionbarChannel.update(getState());
			} else if (clickType.equals(ClickType.LEFT_CLICK)) {
				getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&6현재 상태&f: ") + getState());
			}
		}

		return false;
	}

	public String getState() {
		if (projectileBlocking) return ChatColor.translateAlternateColorCodes('&', "&b원거리 &f1/3 배&7, &a근거리 &f두 배");
		else return ChatColor.translateAlternateColorCodes('&', "&b원거리 &f두 배&7, &a근거리 &f1/3 배");
	}

	private static final RGB LONG_DISTANCE = RGB.of(116, 237, 167);
	private static final RGB SHORT_DISTANCE = RGB.of(85, 237, 242);

	@Scheduled
	private final Timer particle = new Timer() {

		@Override
		public void run(int count) {
			if (projectileBlocking) {
				ParticleLib.REDSTONE.spawnParticle(getPlayer().getLocation().add(0, 2.2, 0), LONG_DISTANCE);
			} else {
				ParticleLib.REDSTONE.spawnParticle(getPlayer().getLocation().add(0, 2.2, 0), SHORT_DISTANCE);
			}
		}

	}.setPeriod(TimeUnit.TICKS, 1);

	@SubscribeEvent
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
		if (e.getEntity().equals(getPlayer())) {
			DamageCause cause = e.getCause();
			if (cause.equals(DamageCause.PROJECTILE)) {
				if (projectileBlocking) {
					e.setDamage(e.getDamage() / 3);
				} else {
					e.setDamage(e.getDamage() * 2);
				}
			} else if (cause.equals(DamageCause.ENTITY_ATTACK)) {
				if (projectileBlocking) {
					e.setDamage(e.getDamage() * 2);
				} else {
					e.setDamage(e.getDamage() / 3);
				}
			}
		}
	}

	@Override
	protected void onUpdate(Update update) {
		if (update == Update.RESTRICTION_CLEAR) {
			actionbarChannel.update(getState());
		}
	}

}
