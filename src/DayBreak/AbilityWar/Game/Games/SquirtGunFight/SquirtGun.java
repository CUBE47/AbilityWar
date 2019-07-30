package DayBreak.AbilityWar.Game.Games.SquirtGunFight;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import DayBreak.AbilityWar.Ability.AbilityBase;
import DayBreak.AbilityWar.Ability.AbilityManifest;
import DayBreak.AbilityWar.Ability.AbilityManifest.Rank;
import DayBreak.AbilityWar.Ability.AbilityManifest.Species;
import DayBreak.AbilityWar.Ability.SubscribeEvent;
import DayBreak.AbilityWar.Ability.Timer.CooldownTimer;
import DayBreak.AbilityWar.Game.Games.Mode.AbstractGame.Participant;
import DayBreak.AbilityWar.Utils.Messager;
import DayBreak.AbilityWar.Utils.Library.EffectLib;
import DayBreak.AbilityWar.Utils.Library.ParticleLib;
import DayBreak.AbilityWar.Utils.Library.SoundLib;
import DayBreak.AbilityWar.Utils.Math.LocationUtil;
import DayBreak.AbilityWar.Utils.Thread.TimerBase;

@AbilityManifest(Name = "����", Rank = Rank.SPECIAL, Species = Species.SPECIAL)
public class SquirtGun extends AbilityBase {

	public SquirtGun(Participant participant) {
		super(participant,
				ChatColor.translateAlternateColorCodes('&', "&f�� �ȿ��� ��ũ���� ���� �ӵ��� ������ ���ư��ϴ�."),
				ChatColor.translateAlternateColorCodes('&', "&fȰ�� ��� ������ ������, �÷��̾ ���߸� �ѹ濡 ���� �� �ֽ��ϴ�. " + Messager.formatCooldown(3)),
				ChatColor.translateAlternateColorCodes('&', "&fö���� ��Ŭ���ϸ� ����ź�� �Ͷ߸���, �ֺ� �÷��̾�鿡�� ���ظ� �ݴϴ�."),
				ChatColor.translateAlternateColorCodes('&', Messager.formatCooldown(30)),
				ChatColor.translateAlternateColorCodes('&', "&fö���� ��Ŭ���ϸ� �������� �ֺ��� ���� ���Ƶ��Դϴ�. " + Messager.formatCooldown(15)),
				ChatColor.translateAlternateColorCodes('&', "&f�ÿ��� &e���� &f��������!"));
	}

	CooldownTimer bombCool = new CooldownTimer(this, 30, "����ź").setActionbarNotice(false);

	CooldownTimer spongeCool = new CooldownTimer(this, 15, "������").setActionbarNotice(false);
	
	@Override
	public boolean ActiveSkill(MaterialType mt, ClickType ct) {
		if(mt.equals(MaterialType.Iron_Ingot)) {
			if(ct.equals(ClickType.RightClick)) {
				if(!bombCool.isCooldown()) {
					Location center = getPlayer().getLocation();
					for(int i = 2; i > 0; i--)
					for(Location l : LocationUtil.getSphere(center, i, 40)) {
						l.getBlock().setType(Material.WATER);
					}
					for(Player p : LocationUtil.getNearbyPlayers(center, 5, 5)) {
						if(!p.equals(getPlayer())) {
							p.damage(20, getPlayer());
						}
					}
					
					SoundLib.ENTITY_PLAYER_SPLASH.playSound(getPlayer());
					
					bombCool.StartTimer();
				}
			} else {
				if(!spongeCool.isCooldown()) {
					Location center = getPlayer().getLocation();
					for(int i = 10; i > 0; i--)
					for(Location l : LocationUtil.getSphere(center, i, 40)) {
						if(l.getBlock().getType().equals(Material.WATER) || l.getBlock().getType().equals(Material.STATIONARY_WATER)) {
							l.getBlock().setType(Material.AIR);
						}
					}
					
					SoundLib.ENTITY_PLAYER_SPLASH.playSound(getPlayer());
					
					spongeCool.StartTimer();
				}
			}
		}
		return false;
	}

	CooldownTimer gunCool = new CooldownTimer(this, 3, "����");
	
	List<Arrow> arrows = new ArrayList<Arrow>();
	
	TimerBase passive = new TimerBase() {
		
		@Override
		protected void onStart() {}
		
		@Override
		protected void onEnd() {}
		
		@Override
		protected void TimerProcess(Integer Seconds) {
			for(Arrow a : arrows) {
				ParticleLib.DRIP_WATER.spawnParticle(a.getLocation(), 10, 1, 1, 1);
			}
			EffectLib.NIGHT_VISION.addPotionEffect(getPlayer(), 400, 0, true);
		}
	}.setPeriod(3);
	
	@SubscribeEvent
	public void onProjectileLaunch(ProjectileLaunchEvent e) {
		if(e.getEntity().getShooter().equals(getPlayer()) && e.getEntity() instanceof Arrow) {
			Arrow a = (Arrow) e.getEntity();
			arrows.add(a);
		}
	}
	
	@SubscribeEvent
	public void onProjectileHit(ProjectileHitEvent e) {
		if(e.getEntity() instanceof Arrow) {
			arrows.remove(e.getEntity());
			if(e.getEntity().getShooter().equals(getPlayer())) {
				if(!gunCool.isCooldown()) {
					if(e.getHitEntity() != null && e.getHitEntity() instanceof Damageable) {
						((Damageable) e.getHitEntity()).damage(200, getPlayer());
					}
					SoundLib.ENTITY_PLAYER_SPLASH.playSound(getPlayer());
					Location center = e.getHitEntity() != null ? e.getHitEntity().getLocation() : e.getHitBlock().getLocation();
					for(Location l : LocationUtil.getRandomLocations(center, 10, 20)) {
						l.getBlock().setType(Material.WATER);
					}
					
					gunCool.StartTimer();
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerMove(PlayerMoveEvent e) {
		if(e.getPlayer().equals(getPlayer()) && (e.getTo().getBlock().getType().equals(Material.WATER) || e.getTo().getBlock().getType().equals(Material.STATIONARY_WATER)) && getPlayer().isSneaking()) {
			getPlayer().setVelocity(getPlayer().getLocation().getDirection().multiply(1.3));
		}
	}
	
	@SubscribeEvent
	public void onEntityDamage(EntityDamageEvent e) {
		if(e.getEntity().equals(getPlayer()) && e.getCause().equals(DamageCause.FALL)) {
			e.setDamage(e.getDamage() / 5);
		}
	}
	
	@Override
	public void TargetSkill(MaterialType mt, Entity entity) {}

	@Override
	protected void onRestrictClear() {
		passive.StartTimer();
	}

}
