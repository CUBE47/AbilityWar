package Marlang.AbilityWar.Ability.List;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Ability.Timer.CooldownTimer;
import Marlang.AbilityWar.Ability.Timer.DurationTimer;
import Marlang.AbilityWar.Config.AbilitySettings.SettingObject;
import Marlang.AbilityWar.Utils.Messager;
import Marlang.AbilityWar.Utils.Library.ParticleLib;
import Marlang.AbilityWar.Utils.Math.LocationUtil;

public class Ares extends AbilityBase {
	
	public static SettingObject<Integer> DamageConfig = new SettingObject<Integer>("�Ʒ���", "Damage", 8, 
			"# ��ų ������") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 0;
		}
		
	};
	
	public static SettingObject<Integer> CooldownConfig = new SettingObject<Integer>("�Ʒ���", "Cooldown", 60, 
			"# ��Ÿ��") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 0;
		}
		
	};
	
	public static SettingObject<Boolean> DashConfig = new SettingObject<Boolean>("�Ʒ���", "DashIntoTheAir", false, 
			"# true�� �����ϸ� �Ʒ��� �ɷ� ��� �� �������� ���� �� �� �ֽ��ϴ�.") {
		
		@Override
		public boolean Condition(Boolean value) {
			return true;
		}
		
	};
	
	public Ares(Player player) {
		super(player, "�Ʒ���", Rank.GOD, 
				ChatColor.translateAlternateColorCodes('&', "&f������ �� �Ʒ���."),
				ChatColor.translateAlternateColorCodes('&', "&fö���� ��Ŭ���ϸ� ������ �����ϸ� ������ ��ƼƼ���� �������� �ָ�,"),
				ChatColor.translateAlternateColorCodes('&', "&f�������� ���� ��ƼƼ���� ���� ���ϴ�. ") + Messager.formatCooldown(CooldownConfig.getValue()));
	}
	
	CooldownTimer Cool = new CooldownTimer(this, CooldownConfig.getValue());
	
	DurationTimer Duration = new DurationTimer(this, 14, Cool) {
		
		private boolean DashIntoTheAir = DashConfig.getValue();
		private int Damage = DamageConfig.getValue();
		private ArrayList<Damageable> Attacked;
		
		@Override
		protected void onDurationStart() {
			Attacked = new ArrayList<Damageable>();
		}
		
		@Override
		public void DurationProcess(Integer Seconds) {
			Player p = getPlayer();
			
			ParticleLib.LAVA.spawnParticle(p.getLocation(), 40, 4, 4, 4);
			
			if(DashIntoTheAir) {
				p.setVelocity(p.getVelocity().add(p.getLocation().getDirection()));
			} else {
				p.setVelocity(p.getVelocity().add(p.getLocation().getDirection().setY(0)));
			}
			
			for(Damageable d : LocationUtil.getNearbyDamageableEntities(p, 4, 4)) {
				if(!Attacked.contains(d)) {
					d.damage(Damage, p);
					Attacked.add(d);
				}
			}
			
			for(Damageable d : Attacked) {
				d.teleport(p);
			}
		}
		
		@Override
		protected void onDurationEnd() {}
		
	}.setPeriod(1);
	
	@Override
	public boolean ActiveSkill(MaterialType mt, ClickType ct) {
		if(mt.equals(MaterialType.Iron_Ingot)) {
			if(ct.equals(ClickType.RightClick)) {
				if(!Duration.isDuration() && !Cool.isCooldown()) {
					Duration.StartTimer();
					
					return true;
				}
			}
		}
		
		return false;
	}
	
	@Override
	public void PassiveSkill(Event event) {}

	@Override
	public void onRestrictClear() {}

	@Override
	public void TargetSkill(MaterialType mt, Entity entity) {}
	
}
