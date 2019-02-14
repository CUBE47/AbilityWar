package Marlang.AbilityWar.Ability;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;

import Marlang.AbilityWar.Ability.List.Ares;
import Marlang.AbilityWar.Ability.List.Assassin;
import Marlang.AbilityWar.Ability.List.Berserker;
import Marlang.AbilityWar.Ability.List.BlackCandle;
import Marlang.AbilityWar.Ability.List.Chaos;
import Marlang.AbilityWar.Ability.List.Chaser;
import Marlang.AbilityWar.Ability.List.Clown;
import Marlang.AbilityWar.Ability.List.DarkVision;
import Marlang.AbilityWar.Ability.List.Demigod;
import Marlang.AbilityWar.Ability.List.DiceGod;
import Marlang.AbilityWar.Ability.List.EnergyBlocker;
import Marlang.AbilityWar.Ability.List.FastRegeneration;
import Marlang.AbilityWar.Ability.List.Feather;
import Marlang.AbilityWar.Ability.List.FireFightWithFire;
import Marlang.AbilityWar.Ability.List.Flora;
import Marlang.AbilityWar.Ability.List.Gladiator;
import Marlang.AbilityWar.Ability.List.Hacker;
import Marlang.AbilityWar.Ability.List.HigherBeing;
import Marlang.AbilityWar.Ability.List.Ira;
import Marlang.AbilityWar.Ability.List.Muse;
import Marlang.AbilityWar.Ability.List.Nex;
import Marlang.AbilityWar.Ability.List.OnlyOddNumber;
import Marlang.AbilityWar.Ability.List.Pumpkin;
import Marlang.AbilityWar.Ability.List.ShowmanShip;
import Marlang.AbilityWar.Ability.List.Terrorist;
import Marlang.AbilityWar.Ability.List.TheEmperor;
import Marlang.AbilityWar.Ability.List.TheEmpress;
import Marlang.AbilityWar.Ability.List.TheHighPriestess;
import Marlang.AbilityWar.Ability.List.TheMagician;
import Marlang.AbilityWar.Ability.List.Virtus;
import Marlang.AbilityWar.Ability.List.Virus;
import Marlang.AbilityWar.Ability.List.Void;
import Marlang.AbilityWar.Ability.List.Yeti;
import Marlang.AbilityWar.Ability.List.Zeus;
import Marlang.AbilityWar.Ability.List.Zombie;
import Marlang.AbilityWar.Config.AbilitySettings.SettingObject;
import Marlang.AbilityWar.Utils.Messager;

public class AbilityList {
	
	private static HashMap<String, Class<? extends AbilityBase>> Abilities = new HashMap<String, Class<? extends AbilityBase>>();
	
	/**
	 * �ɷ� ���
	 */
	public static void registerAbility(String name, Class<? extends AbilityBase> Ability) {
		if(!Abilities.containsKey(name)) {
			Abilities.put(name, Ability);
		}
		
		try {
			for(Field field : Ability.getFields()) {
				if(Modifier.isStatic(field.getModifiers()) && field.getType().equals(SettingObject.class)) {
					field.get(new Object());
				}
			}
		} catch (IllegalAccessException | IllegalArgumentException e) {
			Messager.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e" + Ability.getName() + " &f�ɷ� ����� ������ �߻��Ͽ����ϴ�."));
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
		registerAbility("�Ʒ���", Ares.class);
		registerAbility("���콺", Zeus.class);
		registerAbility("����Ŀ", Berserker.class);
		registerAbility("����", Zombie.class);
		registerAbility("�׷�����Ʈ", Terrorist.class);
		registerAbility("����", Yeti.class);
		registerAbility("�۷�������", Gladiator.class);
		registerAbility("ī����", Chaos.class);
		registerAbility("���̵�", Void.class);
		registerAbility("�ɾ�", DarkVision.class);
		registerAbility("��������", HigherBeing.class);
		registerAbility("���� ����", BlackCandle.class);
		registerAbility("�̿�ġ��", FireFightWithFire.class);
		registerAbility("��Ŀ", Hacker.class);
		registerAbility("����", Muse.class);
		registerAbility("������", Chaser.class);
		registerAbility("�÷ζ�", Flora.class);
		registerAbility("��ǽ�", ShowmanShip.class);
		registerAbility("��������", Virtus.class);
		registerAbility("�ؽ�", Nex.class);
		registerAbility("�̶�", Ira.class);
		registerAbility("Ȧ��������", OnlyOddNumber.class);
		registerAbility("����", Clown.class);
		registerAbility("������", TheMagician.class);
		registerAbility("��Ȳ", TheHighPriestess.class);
		registerAbility("����", TheEmpress.class);
		registerAbility("Ȳ��", TheEmperor.class);
		registerAbility("ȣ��", Pumpkin.class);
		registerAbility("���̷���", Virus.class);
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
