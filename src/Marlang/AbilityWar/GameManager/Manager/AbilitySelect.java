package Marlang.AbilityWar.GameManager.Manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.GameManager.Object.Participant;
import Marlang.AbilityWar.Utils.Messager;
import Marlang.AbilityWar.Utils.Thread.TimerBase;

/**
 * �ɷ� ����
 * @author _Marlang ����
 */
abstract public class AbilitySelect extends TimerBase {
	
	protected List<Class<? extends AbilityBase>> Abilities = setupAbilities();
	
	private HashMap<Participant, Boolean> AbilitySelect = new HashMap<Participant, Boolean>();
	
	public HashMap<Participant, Boolean> getMap() {
		return AbilitySelect;
	}

	public List<Participant> getSelectors() {
		return new ArrayList<Participant>(AbilitySelect.keySet());
	}
	
	public boolean hasDecided(Participant p) {
		return AbilitySelect.get(p);
	}
	
	private boolean setDecided(Participant p, Boolean bool) {
		return AbilitySelect.put(p, bool);
	}
	
	public AbilitySelect() {
		for(Participant p : setupPlayers()) {
			AbilitySelect.put(p, false);
		}
		
		this.drawAbility();
		this.StartTimer();
	}
	
	/**
	 * bool�� true�� �Ϲ� Ȯ��, false�� ���� Ȯ��
	 */
	public void decideAbility(Participant participant, Boolean bool) {
		Player p = participant.getPlayer();
		
		if(AbilitySelect.containsKey(participant)) {
			setDecided(participant, true);
			
			if(bool) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6�ɷ��� Ȯ���ϼ̽��ϴ�. �ٸ� �÷��̾ ��ٷ��ּ���."));
				
			} else {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6�ɷ��� ������ Ȯ���Ǿ����ϴ�. �ٸ� �÷��̾ ��ٷ��ּ���."));
			}
			
			Messager.broadcastStringList(Messager.getStringList(
					ChatColor.translateAlternateColorCodes('&', "&e" + p.getName() + "&f���� �ɷ��� Ȯ���ϼ̽��ϴ�."),
					ChatColor.translateAlternateColorCodes('&', "&a���� �ο� &7: &f" + getLeftPlayers() + "��")));
		}
	}

	private int getLeftPlayers() {
		int i = 0;
		for(Participant p : AbilitySelect.keySet()) {
			if(!AbilitySelect.get(p)) {
				i++;
			}
		}
		
		return i;
	}
	
	public void Skip(String admin) {
		for(Participant p : AbilitySelect.keySet()) {
			if(!AbilitySelect.get(p)) {
				decideAbility(p, false);
			}
		}

		Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f������ &e" + admin + "&f���� ��� �÷��̾��� �ɷ��� ������ Ȯ�����׽��ϴ�."));
		this.StopTimer(false);
	}
	
	abstract protected void drawAbility();
	
	public abstract void changeAbility(Participant participant);
	
	abstract protected List<Participant> setupPlayers();
	
	abstract protected List<Class<? extends AbilityBase>> setupAbilities();
	
	abstract protected boolean endCondition();
	
	private boolean Ended = false;
	
	public boolean isEnded() {
		return Ended;
	}
	
	private int Count = 0;
	
	private void Warn() {
		if(Count >= 20) {
			Messager.broadcastStringList(Messager.getStringList(
					ChatColor.translateAlternateColorCodes('&', "&c���� ��� ������ �ɷ��� Ȯ������ �ʾҽ��ϴ�."),
					ChatColor.translateAlternateColorCodes('&', "&c/ability yes�� /ability no ��ɾ�� �ɷ��� Ȯ�����ּ���.")));
			Count = 0;
		}
	}
	
	@Override
	public void onStart() {}
	
	@Override
	public void TimerProcess(Integer Seconds) {
		if(!endCondition()) {
			Count++;
			Warn();
		} else {
			this.StopTimer(false);
		}
	}
	
	@Override
	public void onEnd() {
		Ended = true;
	}
	
}
