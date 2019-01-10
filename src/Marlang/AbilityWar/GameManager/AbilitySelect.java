package Marlang.AbilityWar.GameManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Ability.AbilityList;
import Marlang.AbilityWar.Utils.AbilityWarThread;
import Marlang.AbilityWar.Utils.Messager;

/**
 * �ɷ� ����
 * @author _Marlang ����
 */
public class AbilitySelect extends Thread {
	
	HashMap<Player, Boolean> AbilitySelect = new HashMap<Player, Boolean>();

	ArrayList<AbilityBase> IdleAbilities = new ArrayList<AbilityBase>();
	
	public boolean getAbilitySelect(Player p) {
		return AbilitySelect.get(p);
	}
	
	public boolean setAbilitySelect(Player p, Boolean bool) {
		return AbilitySelect.put(p, bool);
	}
	
	public AbilitySelect(ArrayList<Player> Players) {
		for(Player p : Players) {
			AbilitySelect.put(p, false);
		}
	}
	
	int AbilitySelectTime = 0;

	@Override
	public void run() {
		if(!isEveryoneReady()) {
			AbilitySelectTime++;
			AbilitySelectWarning(getAbilitySelectTime());
		} else {
			AbilityWarThread.toggleAbilitySelectTask(false);
		}
	}
	
	/**
	 * bool�� true�� �Ϲ� Ȯ��, false�� ���� Ȯ��
	 */
	public void decideAbility(Player p, Boolean bool) {
		if(AbilitySelect.containsKey(p)) {
			setAbilitySelect(p, true);
			
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
	
	public void Skip(Player admin) {
		for(Player p : AbilitySelect.keySet()) {
			if(!AbilitySelect.get(p)) {
				decideAbility(p, false);
			}
		}

		Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f������ &e" + admin.getName() + "&f���� ��� �÷��̾��� �ɷ��� ������ Ȯ�����׽��ϴ�."));
	}
	
	public void randomAbilityToAll() {
		IdleAbilities.clear();
		
		for(String name : AbilityList.values()) {
			try {
				IdleAbilities.add(AbilityList.getByString(name).newInstance());
			} catch (InstantiationException | IllegalAccessException e) {}
		}
		
		if(AbilitySelect.keySet().size() <= IdleAbilities.size()) {
			Random random = new Random();
			
			for(Player p : AbilitySelect.keySet()) {
				AbilityBase Ability = IdleAbilities.get(random.nextInt(IdleAbilities.size()));
				IdleAbilities.remove(Ability);
				
				Ability.setPlayer(p);
				AbilityWarThread.getGame().getAbilities().put(p, Ability);
				Messager.sendStringList(p, Messager.getStringList(
						ChatColor.translateAlternateColorCodes('&', "&a��ſ��� �ɷ��� �Ҵ�Ǿ����ϴ�. &e/ability check&f�� Ȯ�� �� �� �ֽ��ϴ�."),
						ChatColor.translateAlternateColorCodes('&', "&e/ability yes &f��ɾ ����ϸ� �ɷ��� Ȯ���մϴ�."),
						ChatColor.translateAlternateColorCodes('&', "&e/ability no &f��ɾ ����ϸ� 1ȸ�� ���� �ɷ��� ������ �� �ֽ��ϴ�.")));
			}
		} else {
			Messager.broadcastErrorMessage("�ɷ��� ���� �÷��̾��� ������ ���� ������ ������ �� �����ϴ�.");
			if(AbilityWarThread.isAbilitySelectTaskRunning()) {
				AbilityWarThread.toggleAbilitySelectTask(false);
			}
			if(AbilityWarThread.isAbilitySelectTaskRunning()) {
				AbilityWarThread.toggleAbilitySelectTask(false);
			}
			AbilityWarThread.toggleGameTask(false);
			AbilityWarThread.setGame(null);
			Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7������ �ʱ�ȭ�Ǿ����ϴ�."));
		}
	}
	
	public void changeAbility(Player p) {
		if(IdleAbilities.size() > 0) {
			Random random = new Random();
			
			AbilityBase oldAbility = AbilityWarThread.getGame().getAbilities().get(p);
			oldAbility.setPlayer(null);
			AbilityBase Ability = IdleAbilities.get(random.nextInt(IdleAbilities.size()));
			IdleAbilities.remove(Ability);
			
			Ability.setPlayer(p);
			
			AbilityWarThread.getGame().getAbilities().put(p, Ability);
			
			Messager.sendMessage(p, ChatColor.translateAlternateColorCodes('&', "&a����� �ɷ��� ����Ǿ����ϴ�. &e/ability check&f�� Ȯ�� �� �� �ֽ��ϴ�."));
			
			decideAbility(p, false);

			IdleAbilities.add(oldAbility);
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
	
}
