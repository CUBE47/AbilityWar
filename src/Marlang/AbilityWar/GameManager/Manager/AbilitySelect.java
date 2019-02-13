package Marlang.AbilityWar.GameManager.Manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Utils.Messager;
import Marlang.AbilityWar.Utils.TimerBase;

/**
 * �ɷ� ����
 * @author _Marlang ����
 */
abstract public class AbilitySelect extends TimerBase {
	
	protected ArrayList<Class<? extends AbilityBase>> Abilities = new ArrayList<Class<? extends AbilityBase>>();
	
	private HashMap<Player, Boolean> AbilitySelect = new HashMap<Player, Boolean>();
	
	public HashMap<Player, Boolean> getMap() {
		return AbilitySelect;
	}
	
	public boolean hasDecided(Player p) {
		return AbilitySelect.get(p);
	}
	
	private boolean setDecided(Player p, Boolean bool) {
		return AbilitySelect.put(p, bool);
	}
	
	public AbilitySelect() {
		for(Player p : setupPlayers()) {
			AbilitySelect.put(p, false);
		}
		
		this.drawAbility();
		this.StartTimer();
	}
	
	/**
	 * bool�� true�� �Ϲ� Ȯ��, false�� ���� Ȯ��
	 */
	public void decideAbility(Player p, Boolean bool) {
		if(AbilitySelect.containsKey(p)) {
			setDecided(p, true);
			
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
		for(Player p : AbilitySelect.keySet()) {
			if(!AbilitySelect.get(p)) {
				i++;
			}
		}
		
		return i;
	}
	
	public void Skip(String admin) {
		for(Player p : AbilitySelect.keySet()) {
			if(!AbilitySelect.get(p)) {
				decideAbility(p, false);
			}
		}

		Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f������ &e" + admin + "&f���� ��� �÷��̾��� �ɷ��� ������ Ȯ�����׽��ϴ�."));
		this.StopTimer(false);
	}
	
	abstract protected void drawAbility();
	
	public abstract void changeAbility(Player p);
	
	abstract protected List<Player> setupPlayers();
	
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
	public void TimerStart(Data<?>... args) {}
	
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
	public void TimerEnd() {
		Ended = true;
	}
	
}
