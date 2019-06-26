package DayBreak.AbilityWar.Ability.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.util.Vector;

import DayBreak.AbilityWar.Ability.AbilityBase;
import DayBreak.AbilityWar.Ability.AbilityManifest;
import DayBreak.AbilityWar.Ability.AbilityManifest.Rank;
import DayBreak.AbilityWar.Ability.Timer.CooldownTimer;
import DayBreak.AbilityWar.Ability.Timer.DurationTimer;
import DayBreak.AbilityWar.Config.AbilitySettings.SettingObject;
import DayBreak.AbilityWar.Game.Games.Mode.AbstractGame.Participant;
import DayBreak.AbilityWar.Utils.Messager;
import DayBreak.AbilityWar.Utils.Library.ParticleLib;
import DayBreak.AbilityWar.Utils.Math.LocationUtil;

@AbilityManifest(Name = "ī����", Rank = Rank.GOD)
public class Chaos extends AbilityBase {

	public static SettingObject<Integer> CooldownConfig = new SettingObject<Integer>(Chaos.class, "Cooldown", 80,
			"# ��Ÿ��") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 0;
		}
		
	};

	public static SettingObject<Integer> DurationConfig = new SettingObject<Integer>(Chaos.class, "Duration", 5,
			"# �ɷ� ���� �ð�") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 1;
		}
		
	};

	public static SettingObject<Integer> DistanceConfig = new SettingObject<Integer>(Chaos.class, "Distance", 5,
			"# �Ÿ� ����") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 1;
		}
		
	};

	public Chaos(Participant participant) {
		super(participant,
				ChatColor.translateAlternateColorCodes('&', "&f������ �� ī����."),
				ChatColor.translateAlternateColorCodes('&', "&fö���� ��Ŭ���ϸ� 5�ʰ� £�� ���� ������ �ֺ��� ����ü����"),
				ChatColor.translateAlternateColorCodes('&', "&f��� ������ϴ�. " + Messager.formatCooldown(CooldownConfig.getValue())));
	}

	CooldownTimer Cool = new CooldownTimer(this, CooldownConfig.getValue());
	
	DurationTimer Duration = new DurationTimer(this, DurationConfig.getValue() * 20, Cool) {

		Integer Distance = DistanceConfig.getValue();
		
		Location center;
		
		@Override
		public void onDurationStart() {
			center = getPlayer().getLocation();
		}
		
		@Override
		public void DurationProcess(Integer Seconds) {
			ParticleLib.SMOKE_NORMAL.spawnParticle(center, 100, 2, 2, 2);
			for(Damageable d : LocationUtil.getNearbyDamageableEntities(center, Distance, Distance)) {
				if(!d.equals(getPlayer())) {
					d.damage(0.7);
					Vector vector = center.toVector().subtract(d.getLocation().toVector());
					d.setVelocity(vector);
				}
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
