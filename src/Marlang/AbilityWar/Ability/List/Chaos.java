package Marlang.AbilityWar.Ability.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.util.Vector;

import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Ability.Timer.CooldownTimer;
import Marlang.AbilityWar.Ability.Timer.DurationTimer;
import Marlang.AbilityWar.Config.AbilitySettings.SettingObject;
import Marlang.AbilityWar.Utils.LocationUtil;
import Marlang.AbilityWar.Utils.Messager;
import Marlang.AbilityWar.Utils.Library.ParticleLib;

public class Chaos extends AbilityBase {

	public static SettingObject<Integer> CooldownConfig = new SettingObject<Integer>("ī����", "Cooldown", 80,
			"# ��Ÿ��") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 0;
		}
		
	};

	public static SettingObject<Integer> DurationConfig = new SettingObject<Integer>("ī����", "Duration", 5,
			"# �ɷ� ���� �ð�") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 1;
		}
		
	};

	public static SettingObject<Integer> DistanceConfig = new SettingObject<Integer>("ī����", "Distance", 5,
			"# �Ÿ� ����") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 1;
		}
		
	};

	public Chaos(Player player) {
		super(player, "ī����", Rank.God,
				ChatColor.translateAlternateColorCodes('&', "&f������ �� ī����."),
				ChatColor.translateAlternateColorCodes('&', "&fö���� ��Ŭ���ϸ� 5�ʰ� £�� ���� ������ �ֺ��� ����ü����"),
				ChatColor.translateAlternateColorCodes('&', "&f��� ������ϴ�. " + Messager.formatCooldown(CooldownConfig.getValue())));
		
		Duration.setPeriod(1);
	}

	CooldownTimer Cool = new CooldownTimer(this, CooldownConfig.getValue());
	
	DurationTimer Duration = new DurationTimer(this, DurationConfig.getValue() * 20, Cool) {

		Integer Distance = DistanceConfig.getValue();
		
		Location center;
		
		@Override
		public void TimerStart(Data<?>... args) {
			center = getPlayer().getLocation();
			
			super.TimerStart();
		}
		
		@Override
		public void DurationSkill(Integer Seconds) {
			ParticleLib.SMOKE_NORMAL.spawnParticle(center, 100, 2, 2, 2);
			for(Damageable d : LocationUtil.getNearbyDamageableEntities(center, Distance, Distance)) {
				if(!d.equals(getPlayer())) {
					d.damage(0.5);
					Vector vector = center.toVector().subtract(d.getLocation().toVector());
					d.setVelocity(vector);
				}
			}
		}
		
	};
	
	@Override
	public boolean ActiveSkill(ActiveMaterialType mt, ActiveClickType ct) {
		if(mt.equals(ActiveMaterialType.Iron_Ingot)) {
			if(ct.equals(ActiveClickType.RightClick)) {
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
	public void AbilityEvent(EventType type) {}

}
