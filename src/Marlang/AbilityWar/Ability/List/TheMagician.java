package Marlang.AbilityWar.Ability.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.ProjectileHitEvent;

import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Ability.AbilityManifest;
import Marlang.AbilityWar.Ability.AbilityManifest.Rank;
import Marlang.AbilityWar.Ability.Timer.CooldownTimer;
import Marlang.AbilityWar.Config.AbilitySettings.SettingObject;
import Marlang.AbilityWar.Game.Games.AbstractGame.Participant;
import Marlang.AbilityWar.Utils.Messager;
import Marlang.AbilityWar.Utils.Library.ParticleLib;
import Marlang.AbilityWar.Utils.Library.SoundLib;
import Marlang.AbilityWar.Utils.Math.LocationUtil;

@AbilityManifest(Name = "������", Rank = Rank.A)
public class TheMagician extends AbilityBase {

	public static SettingObject<Integer> CooldownConfig = new SettingObject<Integer>(TheMagician.class, "Cooldown", 5, 
			"# ��Ÿ��") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 0;
		}
		
	};

	public static SettingObject<Integer> DamageConfig = new SettingObject<Integer>(TheMagician.class, "Damage", 3, 
			"# ������") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 1;
		}
		
	};

	public TheMagician(Participant participant) {
		super(participant,
				ChatColor.translateAlternateColorCodes('&', "&fȰ�� ���� ��, ȭ���� ���� ��ġ���� 5ĭ ���� ���� �ִ� �÷��̾�鿡��"),
				ChatColor.translateAlternateColorCodes('&', "&f" + DamageConfig.getValue() + "��ŭ�� �������� �߰��� �����ϴ�. " + Messager.formatCooldown(CooldownConfig.getValue())));
	}

	@Override
	public boolean ActiveSkill(MaterialType mt, ClickType ct) {
		return false;
	}

	CooldownTimer Cool = new CooldownTimer(this, CooldownConfig.getValue());
	
	@Override
	public void PassiveSkill(Event event) {
		if(event instanceof ProjectileHitEvent) {
			ProjectileHitEvent e = (ProjectileHitEvent) event;
			if(e.getEntity() instanceof Arrow) {
				if(e.getEntity().getShooter().equals(getPlayer())) {
					if(!Cool.isCooldown()) {
						Location center = e.getEntity().getLocation();
						for(Player p : LocationUtil.getNearbyPlayers(center, 5, 5)) {
							if(!p.equals(getPlayer())) {
								if(LocationUtil.isInCircle(center, p.getLocation(), 5.0)) {
									p.damage(3, p);
									SoundLib.ENTITY_ILLUSIONER_CAST_SPELL.playSound(p);
								}
							}
						}
						
						for(Location l : LocationUtil.getCircle(center, 5, 10, true)) {
							ParticleLib.SPELL_WITCH.spawnParticle(l, 1, 0, 0, 0);
						}
						
						Cool.StartTimer();
					}
				}
			}
		}
	}

	@Override
	public void onRestrictClear() {}

	@Override
	public void TargetSkill(MaterialType mt, Entity entity) {}
	
}
