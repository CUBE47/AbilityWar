package Marlang.AbilityWar.Ability.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Ability.Timer.CooldownTimer;
import Marlang.AbilityWar.Ability.Timer.DurationTimer;
import Marlang.AbilityWar.Config.AbilitySettings.SettingObject;
import Marlang.AbilityWar.Utils.Messager;
import Marlang.AbilityWar.Utils.Library.ParticleLib;
import Marlang.AbilityWar.Utils.Library.SoundLib;
import Marlang.AbilityWar.Utils.Math.LocationUtil;
import Marlang.AbilityWar.Utils.VersionCompat.PlayerCompat;
import Marlang.AbilityWar.Utils.VersionCompat.PotionEffectType;

public class TheHighPriestess extends AbilityBase {

	public static SettingObject<Integer> CooldownConfig = new SettingObject<Integer>("��Ȳ", "Cooldown", 80,
			"# ��Ÿ��") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 0;
		}
		
	};

	public static SettingObject<Integer> DurationConfig = new SettingObject<Integer>("��Ȳ", "Duration", 6,
			"# ��ų ���ӽð�") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 1 && value <= 50;
		}
		
	};

	public static SettingObject<Integer> RangeConfig = new SettingObject<Integer>("��Ȳ", "Range", 8,
			"# ��ų ��� �� �ڽ��� ������ ������ ����") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 1 && value <= 50;
		}
		
	};

	public TheHighPriestess(Player player) {
		super(player, "��Ȳ", Rank.A,
				ChatColor.translateAlternateColorCodes('&', "&fö���� ��Ŭ���ϸ� " + DurationConfig.getValue() + "�ʰ� �ֺ� " + RangeConfig.getValue() + "ĭ�� �ڽ��� ������ �����մϴ�. " + Messager.formatCooldown(CooldownConfig.getValue())),
				ChatColor.translateAlternateColorCodes('&', "&f���� �ȿ��� �ڽ��� ��� ȿ����, ������ ���� ȿ���� �޽��ϴ�."));
	}

	final Integer Duration = DurationConfig.getValue();
	final Integer Range = RangeConfig.getValue();
	
	CooldownTimer Cool = new CooldownTimer(this, CooldownConfig.getValue());
	
	DurationTimer Skill = new DurationTimer(this, DurationConfig.getValue() * 20, Cool) {
		
		private Location center;
		
		@Override
		protected void onDurationStart() {
			center = getPlayer().getLocation();
			
			for(Player p : LocationUtil.getNearbyPlayers(center, Range, Range)) {
				if(LocationUtil.isInCircle(p.getLocation(), center, Double.valueOf(Range))) {
					SoundLib.ENTITY_EVOCATION_ILLAGER_CAST_SPELL.playSound(p);
				}
			}
		}

		@Override
		public void DurationProcess(Integer Seconds) {
			for(Location l : LocationUtil.getCircle(center, Range, Range * Range, true)) {
				ParticleLib.SPELL_INSTANT.spawnParticle(l, 1, 0, 0, 0);
			}
			
			for(Player p : LocationUtil.getNearbyPlayers(center, Range, Range)) {
				if(LocationUtil.isInCircle(p.getLocation(), center, Double.valueOf(Range))) {
					if(p.equals(getPlayer())) {
						PlayerCompat.addPotionEffect(p, PotionEffectType.REGENERATION, 100, 1, true);
					} else {
						PlayerCompat.addPotionEffect(p, PotionEffectType.WITHER, 100, 1, true);
					}
				}
			}
		}
		
		@Override
		protected void onDurationEnd() {}
		
	}.setPeriod(1);
	
	@Override
	public boolean ActiveSkill(ActiveMaterialType mt, ActiveClickType ct) {
		if (mt.equals(ActiveMaterialType.Iron_Ingot)) {
			if (ct.equals(ActiveClickType.RightClick)) {
				if(!Skill.isDuration() && !Cool.isCooldown()) {
					
					Skill.StartTimer();
					
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

}
