package daybreak.abilitywar.ability.list;

import daybreak.abilitywar.AbilityWar;
import daybreak.abilitywar.ability.AbilityBase;
import daybreak.abilitywar.ability.AbilityManifest;
import daybreak.abilitywar.ability.AbilityManifest.Rank;
import daybreak.abilitywar.ability.AbilityManifest.Species;
import daybreak.abilitywar.ability.SubscribeEvent;
import daybreak.abilitywar.ability.event.AbilityRestrictionClearEvent;
import daybreak.abilitywar.config.AbilitySettings.SettingObject;
import daybreak.abilitywar.game.games.mode.AbstractGame.Participant;
import daybreak.abilitywar.utils.Messager;
import daybreak.abilitywar.utils.versioncompat.NMSUtil.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

@AbilityManifest(Name = "교주", Rank = Rank.A, Species = Species.HUMAN)
public class ReligiousLeader extends AbilityBase {

	public static final SettingObject<Integer> CooldownConfig = new SettingObject<Integer>(ReligiousLeader.class, "Cooldown", 150,
			"# 쿨타임") {

		@Override
		public boolean Condition(Integer value) {
			return value >= 0;
		}

	};

	public ReligiousLeader(Participant participant) {
		super(participant,
				ChatColor.translateAlternateColorCodes('&', "&f처음 시작하면 새로운 종교를 창시하며, 상대방을 철괴로 우클릭하면"),
				ChatColor.translateAlternateColorCodes('&', "&f신자로 영입할 수 있습니다. 신자는 최대 현재 게임에 참가중인"),
				ChatColor.translateAlternateColorCodes('&', "&f참가자 수의 1/2만큼 모을 수 있으며, 신자가 참가자 수의 1/4 이상 모이면"),
				ChatColor.translateAlternateColorCodes('&', "&f철괴를 좌클릭해 이단 심판을 시작할 수 있습니다. " + Messager.formatCooldown(CooldownConfig.getValue())),
				ChatColor.translateAlternateColorCodes('&', "&f이단 심판이 시작되면 신자들과 교주는 서로 물리적으로 공격할 수 없으며,"),
				ChatColor.translateAlternateColorCodes('&', "&f신자가 아닌 참가자를 공격할 때 추가 데미지를 주며 심판합니다."));
	}

	private final int maxBelivers = getGame().getParticipants().size() / 2;
	private final int minBelivers = getGame().getParticipants().size() / 4;
	private final Set<Participant> belivers = new HashSet<>();
	private boolean nameSelecting = false;
	private String religionName = null;

	private void sendMessage(String message) {
		getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&5[&d교주&5] &f" + message));
	}

	private void newReligion(String name) {
		religionName = name;
		new BukkitRunnable() {
			@Override
			public void run() {
				Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&5" + name + "&f가 창시되었습니다."));
			}
		}.runTask(AbilityWar.getPlugin());
	}

	private final Timer nameSelect = new Timer(30) {
		@Override
		protected void onStart() {
			sendMessage("창시할 종교의 이름을 채팅창에 입력하세요. (최대 10글자)");
			sendMessage("예시: &e'능력자'&f를 입력했다면, 종교의 이름은 &e'능력자교'&f가 됩니다.");
			nameSelecting = true;
		}

		@Override
		protected void onProcess(int count) {
			PlayerUtil.sendActionbar(getPlayer(), ChatColor.translateAlternateColorCodes('&', "&5종교&d의 이름을 정하세요: &e" + count + ""), 0, 23, 0);
		}

		@Override
		protected void onEnd() {
			nameSelecting = false;
			newReligion("능력자교");
			sendMessage("종교의 이름이 선택되지 않아 이름이 임의로 설정되었습니다.");
			noticer.startTimer();
		}

		@Override
		protected void onSilentEnd() {
			noticer.startTimer();
		}
	};

	private final Timer noticer = new Timer() {
		@Override
		protected void onProcess(int count) {
			if (!cooldownTimer.isRunning()) {
				PlayerUtil.sendActionbar(getPlayer(), ChatColor.translateAlternateColorCodes('&', "&5" + religionName + " &d신자 수&f: &e" + belivers.size()), 0, 7, 0);
			}
		}
	}.setPeriod(5);

	private final CooldownTimer cooldownTimer = new CooldownTimer(CooldownConfig.getValue());

	private boolean inquisition = false;

	private final DurationTimer skill = new DurationTimer(10, cooldownTimer) {
		@Override
		protected void onDurationStart() {
			Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&4" + religionName + " &c이단 심판이 시작되었습니다."));
			inquisition = true;
		}

		@Override
		protected void onDurationProcess(int seconds) {
		}

		@Override
		protected void onDurationEnd() {
			Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&4" + religionName + " &c이단 심판이 끝났습니다."));
			inquisition = false;
		}

		@Override
		protected void onSilentEnd() {
			Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&4" + religionName + " &c이단 심판이 끝났습니다."));
			inquisition = false;
		}
	};

	@SubscribeEvent(onlyRelevant = true)
	private void onAsyncPlayerChat(AsyncPlayerChatEvent e) {
		if (nameSelecting) {
			e.setCancelled(true);
			String name = e.getMessage();
			if (name.length() <= 10) {
				nameSelecting = false;
				nameSelect.stopTimer(true);
				newReligion(name + "교");
			} else {
				sendMessage("종교 이름은 최대 10글자입니다.");
			}
		}
	}

	@SubscribeEvent(onlyRelevant = true)
	private void onRestrictionClear(AbilityRestrictionClearEvent e) {
		if (religionName == null) {
			nameSelect.startTimer();
		}
	}

	@SubscribeEvent
	private void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
		Player damager = null;
		Player entity = null;
		if (e.getEntity() instanceof Player) {
			entity = (Player) e.getEntity();
		}
		if (e.getDamager() instanceof Player) {
			damager = (Player) e.getDamager();
		} else if (e.getDamager() instanceof Projectile) {
			Projectile projectile = (Projectile) e.getDamager();
			if (projectile.getShooter() instanceof Player) {
				damager = (Player) projectile.getShooter();
			}
		}
		if (inquisition && entity != null && damager != null) {
			if (isReligious(damager)) {
				if (isReligious(entity)) {
					e.setCancelled(true);
					damager.sendMessage(ChatColor.translateAlternateColorCodes('&', "&5이단 심판 &d중에 &f같은 종교&d의 신자를 때릴 수 없습니다."));
				} else {
					entity.getWorld().strikeLightningEffect(entity.getLocation());
					e.setDamage(e.getDamage() * 1.5);
				}
			}
		}
	}

	public boolean isReligious(Player player) {
		if (getGame().isParticipating(player)) {
			Participant participant = getGame().getParticipant(player);
			if (belivers.contains(participant) || getParticipant().equals(participant)) return true;
		}
		return false;
	}

	@Override
	public boolean ActiveSkill(Material materialType, ClickType ct) {
		if (materialType.equals(Material.IRON_INGOT) && ct.equals(ClickType.LEFT_CLICK) && religionName != null && !skill.isDuration() && !cooldownTimer.isCooldown()) {
			if (belivers.size() >= minBelivers) {
				skill.startTimer();
			} else {
				sendMessage("신자 수가 부족합니다. (최소 " + minBelivers + "명)");
			}
		}
		return false;
	}

	@Override
	public void TargetSkill(Material materialType, LivingEntity entity) {
		if (materialType.equals(Material.IRON_INGOT) && religionName != null && entity instanceof Player) {
			if (belivers.size() < maxBelivers) {
				Player target = (Player) entity;
				if (getGame().isParticipating(target) && belivers.add(getGame().getParticipant(target))) {
					sendMessage("&e" + target.getName() + "&f님은 이제 &5" + religionName + "&f를 믿습니다. &f( &5" + religionName + " &d신자 수&f: &e" + belivers.size() + " &f)");
					target.sendMessage(ChatColor.translateAlternateColorCodes('&', "&5" + religionName + " &f만세!"));
				}
			} else {
				sendMessage("신자 수가 최대치에 도달하여 더이상 모을 수 없습니다.");
			}
		}
	}

}