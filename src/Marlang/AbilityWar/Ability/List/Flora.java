package Marlang.AbilityWar.Ability.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Ability.Timer.CooldownTimer;
import Marlang.AbilityWar.Config.AbilitySettings.SettingObject;
import Marlang.AbilityWar.Utils.Messager;
import Marlang.AbilityWar.Utils.Library.ParticleLib;
import Marlang.AbilityWar.Utils.Math.LocationUtil;
import Marlang.AbilityWar.Utils.Thread.TimerBase;
import Marlang.AbilityWar.Utils.VersionCompat.PlayerCompat;
import Marlang.AbilityWar.Utils.VersionCompat.PotionEffectType;

public class Flora extends AbilityBase {

	public static SettingObject<Integer> CooldownConfig = new SettingObject<Integer>("�÷ζ�", "Cooldown", 10, 
			"# ��Ÿ��") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 0;
		}
		
	};
	
	public Flora(Player player) {
		super(player, "�÷ζ�", Rank.GOD,
				ChatColor.translateAlternateColorCodes('&', "&f�ɰ� ǳ���� ����."),
				ChatColor.translateAlternateColorCodes('&', "&f�ֺ��� �ִ� ��� �÷��̾�� ��� ȿ���� �ְų� �ż� ȿ���� �ݴϴ�."),
				ChatColor.translateAlternateColorCodes('&', "&fö���� ��Ŭ���ϸ� ȿ���� �ڹٲߴϴ�. " + Messager.formatCooldown(CooldownConfig.getValue())));
	}
	
	EffectType type = EffectType.Speed;
	
	TimerBase Passive = new TimerBase() {
		
		Location center;
		
		@Override
		public void onStart() {}
		
		@Override
		public void TimerProcess(Integer Seconds) {
			center = getPlayer().getLocation();
			for(Location l : LocationUtil.getCircle(center, 6, 20, true)) {
				ParticleLib.SPELL.spawnParticle(l.subtract(0, 1, 0), 1, 0, 0, 0);
			}
			
			for(Player p : LocationUtil.getNearbyPlayers(center, 6, 200)) {
				if(LocationUtil.isInCircle(center, p.getLocation(), 6.0)) {
					PlayerCompat.addPotionEffect(p, type.getPotionEffect(), 40, 1, true);
				}
			}
		}
		
		@Override
		public void onEnd() {}
		
	}.setPeriod(1);
	
	CooldownTimer Cool = new CooldownTimer(this, CooldownConfig.getValue());
	
	@Override
	public boolean ActiveSkill(MaterialType mt, ClickType ct) {
		if(mt.equals(MaterialType.Iron_Ingot)) {
			if(ct.equals(ClickType.RightClick)) {
				if(!Cool.isCooldown()) {
					Player p = getPlayer();
					if(type.equals(EffectType.Speed)) {
						type = EffectType.Regeneration;
					} else {
						type = EffectType.Speed;
					}
					
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', type.getName() + "&f���� ����Ǿ����ϴ�."));
					
					Cool.StartTimer();
				}
			} else if(ct.equals(ClickType.LeftClick)) {
				Messager.sendMessage(getPlayer(), ChatColor.translateAlternateColorCodes('&', "&6���� ����&f: " + type.getName()));
			}
		}
		
		return false;
	}

	@Override
	public void PassiveSkill(Event event) {}

	@Override
	public void onRestrictClear() {
		Passive.StartTimer();
	}

	private enum EffectType {
		
		Regeneration(PotionEffectType.REGENERATION, ChatColor.translateAlternateColorCodes('&', "&c���")),
		Speed(PotionEffectType.SPEED, ChatColor.translateAlternateColorCodes('&', "&b�ż�"));
		
		PotionEffectType potionEffect;
		String name;
		
		private EffectType(PotionEffectType potionEffect, String name) {
			this.potionEffect = potionEffect;
			this.name = name;
		}

		public PotionEffectType getPotionEffect() {
			return potionEffect;
		}

		public String getName() {
			return name;
		}
		
	}

	@Override
	public void TargetSkill(MaterialType mt, Entity entity) {}
	
}
