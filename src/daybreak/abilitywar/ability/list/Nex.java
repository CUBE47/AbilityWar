package daybreak.abilitywar.ability.list;

import daybreak.abilitywar.ability.AbilityBase;
import daybreak.abilitywar.ability.AbilityManifest;
import daybreak.abilitywar.ability.AbilityManifest.Rank;
import daybreak.abilitywar.ability.AbilityManifest.Species;
import daybreak.abilitywar.ability.SubscribeEvent;
import daybreak.abilitywar.config.AbilitySettings.SettingObject;
import daybreak.abilitywar.game.games.mode.AbstractGame.Participant;
import daybreak.abilitywar.utils.FallBlock;
import daybreak.abilitywar.utils.Messager;
import daybreak.abilitywar.utils.library.ParticleLib;
import daybreak.abilitywar.utils.library.SoundLib;
import daybreak.abilitywar.utils.math.LocationUtil;
import daybreak.abilitywar.utils.versioncompat.ServerVersion;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

@SuppressWarnings("deprecation")
@AbilityManifest(Name = "넥스", Rank = Rank.B, Species = Species.GOD)
public class Nex extends AbilityBase {

	public static final SettingObject<Integer> CooldownConfig = new SettingObject<Integer>(Nex.class, "Cooldown", 120, "# 쿨타임") {

		@Override
		public boolean Condition(Integer value) {
			return value >= 0;
		}

	};

	public static final SettingObject<Integer> DamageConfig = new SettingObject<Integer>(Nex.class, "Damage", 20, "# 데미지") {

		@Override
		public boolean Condition(Integer value) {
			return value >= 1;
		}

	};
	
	public Nex(Participant participant) {
		super(participant,
				ChatColor.translateAlternateColorCodes('&', "&f철괴를 우클릭하면 공중으로 올라갔다가 바닥으로 내려 찍으며"),
				ChatColor.translateAlternateColorCodes('&', "주변의 플레이어들에게 데미지를 입힙니다. " + Messager.formatCooldown(CooldownConfig.getValue())));
	}

	private final CooldownTimer Cool = new CooldownTimer(CooldownConfig.getValue());

	@Override
	public boolean ActiveSkill(MaterialType mt, ClickType ct) {
		if (mt.equals(MaterialType.IRON_INGOT)) {
			if (ct.equals(ClickType.RIGHT_CLICK)) {
				if(!Cool.isCooldown()) {
					for(Player player : LocationUtil.getNearbyPlayers(getPlayer(), 5, 5)) {
						SoundLib.ENTITY_WITHER_SPAWN.playSound(player);
					}
					SoundLib.ENTITY_WITHER_SPAWN.playSound(getPlayer());
					Skill.startTimer();
					
					Cool.startTimer();
					
					return true;
				}
			}
		}
		
		return false;
	}

	private boolean NoFall = false;
	private boolean RunSkill = false;

	private final Timer Skill = new Timer(4) {

		@Override
		public void onStart() {
			NoFall = true;
			Vector v = new Vector(0, 4, 0);

			getPlayer().setVelocity(getPlayer().getVelocity().add(v));
		}

		@Override
		public void onProcess(int count) {
		}

		@Override
		public void onEnd() {
			RunSkill = true;
			Vector v = new Vector(0, -4, 0);

			getPlayer().setVelocity(getPlayer().getVelocity().add(v));
		}

	}.setPeriod(10);
	
	private final int Damage = DamageConfig.getValue();
	
	@SubscribeEvent
	public void onEntityDamage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			if(e.getEntity().equals(getPlayer())) {
				if (NoFall) {
					if (e.getCause().equals(DamageCause.FALL)) {
						e.setCancelled(true);
						NoFall = false;
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerMove(PlayerMoveEvent e) {
		if(e.getPlayer().equals(getPlayer())) {
			if(RunSkill) {
				Block b = getPlayer().getLocation().getBlock();
				Block db = getPlayer().getLocation().subtract(0, 1, 0).getBlock();
				
				if(!b.getType().equals(Material.AIR) || !db.getType().equals(Material.AIR)) {
					RunSkill = false;
					for(Damageable d : LocationUtil.getNearbyEntities(Damageable.class, getPlayer(), 5, 5)) {
						if(d instanceof Player) SoundLib.ENTITY_GENERIC_EXPLODE.playSound((Player) d);
						d.damage(Damage, getPlayer());
					}
					SoundLib.ENTITY_GENERIC_EXPLODE.playSound(getPlayer());
					
					Material particleMat;
					if(!db.getType().equals(Material.AIR)) {
						particleMat = db.getType();
					} else {
						particleMat = b.getType();
					}

					if(ServerVersion.getVersion() >= 13) {
						ParticleLib.BLOCK_CRACK.spawnParticle(getPlayer().getLocation(), 2, 2, 2, 30, particleMat.createBlockData());
					} else {
						ParticleLib.BLOCK_CRACK.spawnParticle(getPlayer().getLocation(), 30, 2, 2, 2, new MaterialData(particleMat));
					}
					
					FallBlock.startTimer();
				}
			}
		}
	}
	
	private final Timer FallBlock = new Timer(5) {
		
		Location center;
		
		@Override
		public void onStart() {
			this.center = getPlayer().getLocation();
		}
		
		@Override
		public void onProcess(int count) {
			Integer Distance = 6 - count;
			
			for(Block block : LocationUtil.getBlocks(center, Distance, true, true, false)) {
				FallBlock fb = new FallBlock(block.getType(), block.getLocation().add(0, 1, 0), new Vector(0, 0.5, 0)) {
					
					@Override
					public void onChangeBlock(FallingBlock block) {}
					
				};
				
				fb.Spawn();
			}
			
			for(Damageable e : LocationUtil.getNearbyDamageableEntities(center, 5, 5)) {
				if(!e.equals(getPlayer())) {
					e.setVelocity(center.toVector().subtract(e.getLocation().toVector()).multiply(-1).setY(1.2));
				}
			}
		}
		
		@Override
		public void onEnd() {}
		
	}.setPeriod(4);
	
	@Override
	public void onRestrictClear() {}

	@Override
	public void TargetSkill(MaterialType mt, LivingEntity entity) {}
	
}
