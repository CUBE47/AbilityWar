package Marlang.AbilityWar.Ability.List;

import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Utils.TimerBase;

public class FastRegeneration extends AbilityBase {
	
	public FastRegeneration() {
		super("���� ȸ��", Rank.A, 
				ChatColor.translateAlternateColorCodes('&', "&f�ٸ� �ɷµ鿡 ���ؼ� �� ���� �ӵ��� ü���� ȸ���մϴ�.")
				);
		Skill.setPeriod(15);
		registerTimer(Skill);
		Skill.StartTimer();
	}
	
	TimerBase Skill = new TimerBase() {
		
		@Override
		public void TimerStart() {}
		
		@Override
		public void TimerProcess(Integer Seconds) {
			if(!isRestricted()) {
				Player p = getPlayer();
				double MaxHealth = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
				
				if(p.getHealth() < MaxHealth) {
					p.setHealth((int) p.getHealth() + 1);
				}
			}
		}
		
		@Override
		public void TimerEnd() {}
		
	};
	
	@Override
	public void ActiveSkill(ActiveMaterialType mt, ActiveClickType ct) {}
	
	@Override
	public void PassiveSkill(Event event) {}
	
}
