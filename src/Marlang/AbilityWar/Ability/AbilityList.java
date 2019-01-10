package Marlang.AbilityWar.Ability;

import java.util.ArrayList;
import java.util.HashMap;

import Marlang.AbilityWar.Ability.List.Assassin;
import Marlang.AbilityWar.Ability.List.Demigod;
import Marlang.AbilityWar.Ability.List.DiceGod;
import Marlang.AbilityWar.Ability.List.EnergyBlocker;
import Marlang.AbilityWar.Ability.List.FastRegeneration;
import Marlang.AbilityWar.Ability.List.Feather;

public class AbilityList {
	
	private static HashMap<String, Class<? extends AbilityBase>> Abilities = new HashMap<String, Class<? extends AbilityBase>>();
	
	/**
	 * �ɷ� ���
	 */
	public static void registerAbility(String name, Class<? extends AbilityBase> Ability) {
		if(!Abilities.containsKey(name)) {
			Abilities.put(name, Ability);
		}
	}
	
	/**
	 * �÷����� �⺻ �ɷ� ���
	 */
	static {
		registerAbility("�ϻ���", Assassin.class);
		registerAbility("����", Feather.class);
		registerAbility("���̰�", Demigod.class);
		registerAbility("���� ȸ��", FastRegeneration.class);
		registerAbility("������ ���Ŀ", EnergyBlocker.class);
		registerAbility("���̽� ��", DiceGod.class);
	}
	
	public static ArrayList<String> values() {
		ArrayList<String> Values = new ArrayList<String>();
		
		for(String s : Abilities.keySet()) {
			Values.add(s);
		}
		
		return Values;
	}
	
	public static Class<? extends AbilityBase> getByString(String name) {
		return Abilities.get(name);
	}
	
}
