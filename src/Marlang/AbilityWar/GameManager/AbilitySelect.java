package Marlang.AbilityWar.GameManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Ability.AbilityList;
import Marlang.AbilityWar.Config.AbilityWarSettings;
import Marlang.AbilityWar.Utils.AbilityWarThread;
import Marlang.AbilityWar.Utils.Messager;
import Marlang.AbilityWar.Utils.TimerBase;

/**
 * �ɷ� ����
 * @author _Marlang ����
 */
public class AbilitySelect extends TimerBase {
	
	public HashMap<Player, Boolean> AbilitySelect = new HashMap<Player, Boolean>();

	ArrayList<AbilityBase> IdleAbilities = new ArrayList<AbilityBase>();
	
	public boolean getAbilitySelect(Player p) {
		return AbilitySelect.get(p);
	}
	
	public boolean setAbilitySelect(Player p, Boolean bool) {
		return AbilitySelect.put(p, bool);
	}
	
	private Game game;
	
	public AbilitySelect(ArrayList<Player> Players, Game game) {
		for(Player p : Players) {
			AbilitySelect.put(p, false);
		}
		
		this.game = game;
	}
	
	int AbilitySelectTime = 0;
	
	/**
	 * bool�� true�� �Ϲ� Ȯ��, false�� ���� Ȯ��
	 */
	public void decideAbility(Player p, Boolean bool) {
		if(AbilitySelect.containsKey(p)) {
			setAbilitySelect(p, true);
			
			if(bool) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6�ɷ��� Ȯ���ϼ̽��ϴ�. �ٸ� �÷��̾ ��ٷ��ּ���."));
				
				Messager.broadcastStringList(Messager.getStringList(
						ChatColor.translateAlternateColorCodes('&', "&e" + p.getName() + "&f���� �ɷ��� Ȯ���ϼ̽��ϴ�."),
						ChatColor.translateAlternateColorCodes('&', "&a���� �ο� &7: &f" + getLeftPlayers() + "��")));
			} else {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6�ɷ��� ������ Ȯ���Ǿ����ϴ�. �ٸ� �÷��̾ ��ٷ��ּ���."));
			}
		}
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
	
	/**
	 * ��ü �ɷ� ��÷
	 */
	public void randomAbilityToAll() {
		IdleAbilities.clear();
		
		for(String name : AbilityList.values()) {
			try {
				if(!AbilityWarSettings.getBlackList().contains(name)) {
					IdleAbilities.add(AbilityList.getByString(name).newInstance());
				}
			} catch (InstantiationException | IllegalAccessException e) {}
		}
		
		if(AbilitySelect.keySet().size() <= IdleAbilities.size()) {
			Random random = new Random();
			
			for(Player p : AbilitySelect.keySet()) {
				AbilityBase Ability = IdleAbilities.get(random.nextInt(IdleAbilities.size()));
				IdleAbilities.remove(Ability);
				
				Ability.setPlayer(p);
				game.addAbility(Ability);
				
				Messager.sendStringList(p, Messager.getStringList(
						ChatColor.translateAlternateColorCodes('&', "&a��ſ��� �ɷ��� �Ҵ�Ǿ����ϴ�. &e/ability check&f�� Ȯ�� �� �� �ֽ��ϴ�."),
						ChatColor.translateAlternateColorCodes('&', "&e/ability yes &f��ɾ ����ϸ� �ɷ��� Ȯ���մϴ�."),
						ChatColor.translateAlternateColorCodes('&', "&e/ability no &f��ɾ ����ϸ� 1ȸ�� ���� �ɷ��� ������ �� �ֽ��ϴ�.")));
			}
		} else {
			Messager.broadcastErrorMessage("��� ������ �ɷ��� ���� �÷��̾��� ������ ���� ������ �����մϴ�.");
			this.StopTimer(true);
			AbilityWarThread.toggleGameTask(false);
			AbilityWarThread.setGame(null);
			Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7������ �ʱ�ȭ�Ǿ����ϴ�."));
		}
	}
	
	/**
	 * ���� �ɷ� ����÷
	 */
	public void changeAbility(Player p) {
		if(IdleAbilities.size() > 0) {
			Random random = new Random();
			
			AbilityBase oldAbility = game.getAbilities().get(p);
			if(oldAbility != null) {
				AbilityBase Ability = IdleAbilities.get(random.nextInt(IdleAbilities.size()));
				IdleAbilities.remove(Ability);
				Ability.setPlayer(p);
				
				game.removeAbility(p);
				game.addAbility(Ability);
				
				Messager.sendMessage(p, ChatColor.translateAlternateColorCodes('&', "&a����� �ɷ��� ����Ǿ����ϴ�. &e/ability check&f�� Ȯ�� �� �� �ֽ��ϴ�."));
				
				decideAbility(p, false);

				try {
					IdleAbilities.add(oldAbility.getClass().newInstance());
				} catch(Exception ex) {}
			}
		} else {
			Messager.sendErrorMessage(p, "�ɷ��� ������ �� �����ϴ�.");
		}
	}
	
	public int getLeftPlayers() {
		int i = 0;
		for(Player p : AbilitySelect.keySet()) {
			if(!AbilitySelect.get(p)) {
				i++;
			}
		}
		
		return i;
	}
	
	public boolean isEveryoneReady() {
		boolean bool = true;
		for(Player Key : AbilitySelect.keySet()) {
			if(!AbilitySelect.get(Key)) {
				bool = false;
			}
		}
		
		return bool;
	}
	
	public void AbilitySelectWarning(Integer Time) {
		if(Time == 20) {
			Messager.broadcastStringList(Messager.getStringList(
					ChatColor.translateAlternateColorCodes('&', "&c���� ��� ������ �ɷ��� Ȯ������ �ʾҽ��ϴ�."),
					ChatColor.translateAlternateColorCodes('&', "&c/ability yes�� /ability no ��ɾ�� �ɷ��� Ȯ�����ּ���.")));
			setAbilitySelectTime(0);
		}
	}

	public void setAbilitySelectTime(int abilitySelectTime) {
		AbilitySelectTime = abilitySelectTime;
	}

	public int getAbilitySelectTime() {
		return AbilitySelectTime;
	}
	
	boolean AbilitySelectFinished = false;
	
	public boolean isAbilitySelectFinished() {
		return AbilitySelectFinished;
	}
	
	@Override
	public void TimerStart() {
		
	}

	@Override
	public void TimerProcess(Integer Seconds) {
		if(!isEveryoneReady()) {
			AbilitySelectTime++;
			AbilitySelectWarning(getAbilitySelectTime());
		} else {
			this.StopTimer(false);
		}
	}

	@Override
	public void TimerEnd() {
		AbilitySelectFinished = true;
	}
	
}
