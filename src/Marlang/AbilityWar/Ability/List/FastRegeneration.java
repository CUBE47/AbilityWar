package Marlang.AbilityWar.Ability.List;

import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Config.AbilitySettings.SettingObject;
import Marlang.AbilityWar.Utils.TimerBase;

public class FastRegeneration extends AbilityBase {
	
	public static SettingObject<Integer> RegenSpeedConfig = new SettingObject<Integer>("����ȸ��", "RegenSpeed", 20,
			"# ȸ�� �ӵ��� �����մϴ�.",
			"# ���ڰ� �������� ȸ���� ���� �������ϴ�.") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 1;
		}
		
	};
	
	public FastRegeneration() {
		super("���� ȸ��", Rank.A, 
				ChatColor.translateAlternateColorCodes('&', "&f�ٸ� �ɷµ鿡 ���ؼ� �� ���� �ӵ��� ü���� ȸ���մϴ�.")
				);
		Passive.setPeriod(RegenSpeedConfig.getValue());
		registerTimer(Passive);
	}
	
	TimerBase Passive = new TimerBase() {
		
		@Override
		public void TimerStart() {}
		
		@Override
		public void TimerProcess(Integer Seconds) {
			if(!isRestricted()) {
				Player p = getPlayer();
				if(!p.isDead()) {
					double MaxHealth = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
					
					if(p.getHealth() < MaxHealth) {
						p.setHealth((int) p.getHealth() + 1);
					}
				}
			}
		}
		
		@Override
		public void TimerEnd() {}
		
	};
	
	@Override
	public boolean ActiveSkill(ActiveMaterialType mt, ActiveClickType ct) {
		return false;
	}
	
	@Override
	public void PassiveSkill(Event event) {}

	@Override
	public void AbilityEvent(EventType type) {
		if(type.equals(EventType.RestrictClear)) {
			Passive.StartTimer();
		}
	}
	
}
