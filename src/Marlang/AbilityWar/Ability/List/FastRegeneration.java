package Marlang.AbilityWar.Ability.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Config.AbilitySettings.SettingObject;
import Marlang.AbilityWar.Utils.Thread.TimerBase;
import Marlang.AbilityWar.Utils.VersionCompat.PlayerCompat;

public class FastRegeneration extends AbilityBase {
	
	public static SettingObject<Integer> RegenSpeedConfig = new SettingObject<Integer>("����ȸ��", "RegenSpeed", 20,
			"# ȸ�� �ӵ��� �����մϴ�.",
			"# ���ڰ� �������� ȸ���� ���� �������ϴ�.") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 1;
		}
		
	};
	
	public FastRegeneration(Player player) {
		super(player, "���� ȸ��", Rank.A, 
				ChatColor.translateAlternateColorCodes('&', "&f�ٸ� �ɷµ鿡 ���ؼ� �� ���� �ӵ��� ü���� ȸ���մϴ�."));
	}
	
	TimerBase Passive = new TimerBase() {
		
		@Override
		public void onStart() {}
		
		@Override
		public void TimerProcess(Integer Seconds) {
			if(!isRestricted()) {
				Player p = getPlayer();
				if(!p.isDead()) {
					double MaxHealth = PlayerCompat.getMaxHealth(p);
					
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
	public void PassiveSkill(Event event) {}

	@Override
	public void onRestrictClear() {
		Passive.StartTimer();
	}

	@Override
	public void TargetSkill(MaterialType mt, Entity entity) {}
	
}
