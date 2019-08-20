package DayBreak.AbilityWar.Ability.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import DayBreak.AbilityWar.Ability.AbilityBase;
import DayBreak.AbilityWar.Ability.AbilityManifest;
import DayBreak.AbilityWar.Ability.AbilityManifest.Rank;
import DayBreak.AbilityWar.Ability.AbilityManifest.Species;
import DayBreak.AbilityWar.Config.AbilitySettings.SettingObject;
import DayBreak.AbilityWar.Game.Games.Mode.AbstractGame.Participant;
import DayBreak.AbilityWar.Utils.Thread.TimerBase;
import DayBreak.AbilityWar.Utils.VersionCompat.VersionUtil;

@AbilityManifest(Name = "���� ȸ��", Rank = Rank.A, Species = Species.HUMAN)
public class FastRegeneration extends AbilityBase {
	
	public static SettingObject<Integer> RegenSpeedConfig = new SettingObject<Integer>(FastRegeneration.class, "RegenSpeed", 20,
			"# ȸ�� �ӵ��� �����մϴ�.",
			"# ���ڰ� �������� ȸ���� ���� �������ϴ�.") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 1;
		}
		
	};
	
	public FastRegeneration(Participant participant) {
		super(participant,
				ChatColor.translateAlternateColorCodes('&', "&f�ٸ� �ɷµ鿡 ���ؼ� �� ���� �ӵ��� ü���� ȸ���մϴ�."));
	}
	
	private TimerBase Passive = new TimerBase() {
		
		@Override
		public void onStart() {}
		
		@Override
		public void TimerProcess(Integer Seconds) {
			if(!isRestricted()) {
				Player p = getPlayer();
				if(!p.isDead()) {
					double MaxHealth = VersionUtil.getMaxHealth(p);
					
					if(p.getHealth() < MaxHealth) {
						p.setHealth((int) p.getHealth() + 1);
					}
				}
			}
		}
		
		@Override
		public void onEnd() {}
		
	}.setPeriod(RegenSpeedConfig.getValue());
	
	@Override
	public boolean ActiveSkill(MaterialType mt, ClickType ct) {
		return false;
	}
	
	@Override
	public void onRestrictClear() {
		Passive.StartTimer();
	}

	@Override
	public void TargetSkill(MaterialType mt, LivingEntity entity) {}
	
}
