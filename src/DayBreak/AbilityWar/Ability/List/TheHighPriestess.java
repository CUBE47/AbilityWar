package DayBreak.AbilityWar.Ability.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import DayBreak.AbilityWar.Ability.AbilityBase;
import DayBreak.AbilityWar.Ability.AbilityManifest;
import DayBreak.AbilityWar.Ability.AbilityManifest.Rank;
import DayBreak.AbilityWar.Ability.AbilityManifest.Species;
import DayBreak.AbilityWar.Ability.Timer.CooldownTimer;
import DayBreak.AbilityWar.Ability.Timer.DurationTimer;
import DayBreak.AbilityWar.Config.AbilitySettings.SettingObject;
import DayBreak.AbilityWar.Game.Games.Mode.AbstractGame.Participant;
import DayBreak.AbilityWar.Utils.Messager;
import DayBreak.AbilityWar.Utils.Library.EffectLib;
import DayBreak.AbilityWar.Utils.Library.ParticleLib;
import DayBreak.AbilityWar.Utils.Library.SoundLib;
import DayBreak.AbilityWar.Utils.Math.LocationUtil;

@AbilityManifest(Name = "��Ȳ", Rank = Rank.A, Species = Species.HUMAN)
public class TheHighPriestess extends AbilityBase {

	public static SettingObject<Integer> CooldownConfig = new SettingObject<Integer>(TheHighPriestess.class, "Cooldown", 80,
			"# ��Ÿ��") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 0;
		}
		
	};

	public static SettingObject<Integer> DurationConfig = new SettingObject<Integer>(TheHighPriestess.class, "Duration", 6,
			"# ��ų ���ӽð�") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 1 && value <= 50;
		}
		
	};

	public static SettingObject<Integer> RangeConfig = new SettingObject<Integer>(TheHighPriestess.class, "Range", 8,
			"# ��ų ��� �� �ڽ��� ������ ������ ����") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 1 && value <= 50;
		}
		
	};

	public TheHighPriestess(Participant participant) {
		super(participant,
				ChatColor.translateAlternateColorCodes('&', "&fö���� ��Ŭ���ϸ� " + DurationConfig.getValue() + "�ʰ� �ֺ� " + RangeConfig.getValue() + "ĭ�� �ڽ��� ������ �����մϴ�. " + Messager.formatCooldown(CooldownConfig.getValue())),
				ChatColor.translateAlternateColorCodes('&', "&f���� �ȿ��� �ڽ��� ��� ȿ����, ������ ���� ȿ���� �޽��ϴ�."));
	}

	private final Integer Duration = DurationConfig.getValue();
	private final Integer Range = RangeConfig.getValue();
	
	private CooldownTimer Cool = new CooldownTimer(this, CooldownConfig.getValue());
	
	private DurationTimer Skill = new DurationTimer(this, Duration * 20, Cool) {
		
		private Location center;
		
		@Override
		protected void onDurationStart() {
			center = getPlayer().getLocation();
			
			for(Player p : LocationUtil.getNearbyPlayers(center, Range, Range)) {
				if(LocationUtil.isInCircle(center, p.getLocation(), Double.valueOf(Range))) {
					SoundLib.ENTITY_EVOKER_CAST_SPELL.playSound(p);
				}
			}
		}

		@Override
		public void DurationProcess(Integer Seconds) {
			for(Location l : LocationUtil.getCircle(center, Range, Range * Range, true)) {
				ParticleLib.SPELL_INSTANT.spawnParticle(l, 1, 0, 0, 0);
			}
			
			for(Player p : LocationUtil.getNearbyEntities(Player.class, center, Range, Range)) {
				if(LocationUtil.isInCircle(center, p.getLocation(), Double.valueOf(Range))) {
					if(p.equals(getPlayer())) {
						EffectLib.REGENERATION.addPotionEffect(p, 100, 0, false);
					} else {
						EffectLib.WITHER.addPotionEffect(p, 100, 0, false);
					}
				}
			}
		}
		
		@Override
		protected void onDurationEnd() {}
		
	}.setPeriod(1);
	
	@Override
	public boolean ActiveSkill(MaterialType mt, ClickType ct) {
		if (mt.equals(MaterialType.Iron_Ingot)) {
			if (ct.equals(ClickType.RightClick)) {
				if(!Skill.isDuration() && !Cool.isCooldown()) {
					
					Skill.StartTimer();
					
					return true;
				}
			}
		}
		
		return false;
	}

	@Override
	public void onRestrictClear() {}

	@Override
	public void TargetSkill(MaterialType mt, Entity entity) {}
	
}
