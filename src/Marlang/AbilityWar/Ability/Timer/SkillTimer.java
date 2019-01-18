package Marlang.AbilityWar.Ability.Timer;

import org.bukkit.ChatColor;

import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Utils.Messager;
import Marlang.AbilityWar.Utils.TimerBase;

abstract public class SkillTimer extends TimerBase {
	
	AbilityBase Ability;
	SkillType SkillType;
	
	DurationTimer Duration;
	CooldownTimer Cool;
	
	/**
	 * ��Ÿ���� ���� ��ų
	 */
	public SkillTimer(AbilityBase Ability, Integer Count, SkillType SkillType) {
		super(Count);
		this.Ability = Ability;
		this.SkillType = SkillType;
	}
	
	/**
	 * ��Ÿ���� �ִ� ��� ��ų
	 */
	public SkillTimer(AbilityBase Ability, Integer Count, SkillType SkillType, CooldownTimer Cool) {
		super(Count);
		this.Ability = Ability;
		this.SkillType = SkillType;
		this.Cool = Cool;
	}
	
	@Override
	public void TimerEnd() {
		if(Duration != null) {
			Duration.StartTimer();
		} else {
			if(Cool != null) {
				Cool.StartTimer();
			}
		}
	}
	
	public void Execute() {
		if(!this.isTimerRunning()) {
			Messager.sendMessage(Ability.getPlayer(), ChatColor.translateAlternateColorCodes('&', "&d�ɷ��� ����Ͽ����ϴ�!"));
			this.StartTimer();
		}
	}
	
	public enum SkillType {
		Active(ChatColor.translateAlternateColorCodes('&', "&d�ɷ��� ����Ͽ����ϴ�.")),
		Passive(ChatColor.translateAlternateColorCodes('&', "&d�нú갡 �ߵ��Ǿ����ϴ�."));
		
		String Message;
		
		private SkillType(String msg) {
			this.Message = msg;
		}
		
		public String getMessage() {
			return Message;
		}
		
	}
	
}
