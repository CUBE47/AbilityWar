package Marlang.AbilityWar.Ability;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;

import Marlang.AbilityWar.Ability.List.Ares;
import Marlang.AbilityWar.Ability.List.Assassin;
import Marlang.AbilityWar.Ability.List.Berserker;
import Marlang.AbilityWar.Ability.List.BlackCandle;
import Marlang.AbilityWar.Ability.List.BombArrow;
import Marlang.AbilityWar.Ability.List.Brewer;
import Marlang.AbilityWar.Ability.List.Celebrity;
import Marlang.AbilityWar.Ability.List.Chaos;
import Marlang.AbilityWar.Ability.List.Chaser;
import Marlang.AbilityWar.Ability.List.Clown;
import Marlang.AbilityWar.Ability.List.Curse;
import Marlang.AbilityWar.Ability.List.DarkVision;
import Marlang.AbilityWar.Ability.List.Demigod;
import Marlang.AbilityWar.Ability.List.DevilBoots;
import Marlang.AbilityWar.Ability.List.DiceGod;
import Marlang.AbilityWar.Ability.List.EnergyBlocker;
import Marlang.AbilityWar.Ability.List.ExpertOfFall;
import Marlang.AbilityWar.Ability.List.FastRegeneration;
import Marlang.AbilityWar.Ability.List.Feather;
import Marlang.AbilityWar.Ability.List.FireFightWithFire;
import Marlang.AbilityWar.Ability.List.Flora;
import Marlang.AbilityWar.Ability.List.Gladiator;
import Marlang.AbilityWar.Ability.List.Hacker;
import Marlang.AbilityWar.Ability.List.Hermit;
import Marlang.AbilityWar.Ability.List.HigherBeing;
import Marlang.AbilityWar.Ability.List.Imprison;
import Marlang.AbilityWar.Ability.List.Ira;
import Marlang.AbilityWar.Ability.List.Muse;
import Marlang.AbilityWar.Ability.List.Nex;
import Marlang.AbilityWar.Ability.List.OnlyOddNumber;
import Marlang.AbilityWar.Ability.List.Pumpkin;
import Marlang.AbilityWar.Ability.List.ShowmanShip;
import Marlang.AbilityWar.Ability.List.SuperNova;
import Marlang.AbilityWar.Ability.List.Terrorist;
import Marlang.AbilityWar.Ability.List.TheEmperor;
import Marlang.AbilityWar.Ability.List.TheEmpress;
import Marlang.AbilityWar.Ability.List.TheHighPriestess;
import Marlang.AbilityWar.Ability.List.TheMagician;
import Marlang.AbilityWar.Ability.List.TimeRewind;
import Marlang.AbilityWar.Ability.List.Virtus;
import Marlang.AbilityWar.Ability.List.Virus;
import Marlang.AbilityWar.Ability.List.Void;
import Marlang.AbilityWar.Ability.List.Yeti;
import Marlang.AbilityWar.Ability.List.Zeus;
import Marlang.AbilityWar.Ability.List.Zombie;
import Marlang.AbilityWar.Config.AbilitySettings.SettingObject;
import Marlang.AbilityWar.Utils.Messager;

/**
 * �ɷ��� ���� �÷������� �ɷ� ����� �����ϴ� Ŭ�����Դϴ�.
 */
public class AbilityList {
	
	private static ArrayList<Class<? extends AbilityBase>> Abilities = new ArrayList<>();
	
	/**
	 * �ɷ��� ����մϴ�.
	 * 
	 * �ɷ��� ����ϱ� ��, AbilityManifest ������̼��� Ŭ������ �����ϴ���,
	 * ��ġ�� �̸��� ������, �����ڴ� �ùٸ��� Ȯ�����ֽñ� �ٶ��ϴ�.
	 * 
	 * �̹� ��ϵ� �ɷ��� ��� �ٽ� ����� ���� �ʽ��ϴ�.
	 * @param Ability		�ɷ� Ŭ����
	 */
	public static void registerAbility(Class<? extends AbilityBase> Ability) {
		if(!Abilities.contains(Ability)) {
			AbilityManifest manifest = Ability.getAnnotation(AbilityManifest.class);
			
			if(manifest != null) {
				if(!containsName(manifest.Name())) {
					Abilities.add(Ability);
					
					try {
						for(Field field : Ability.getFields()) {
							if(field.getType().equals(SettingObject.class) && Modifier.isStatic(field.getModifiers())) {
								field.get(null);
							}
						}
					} catch (IllegalAccessException | IllegalArgumentException e) {
						Messager.sendErrorMessage(ChatColor.translateAlternateColorCodes('&', "&e" + Ability.getName() + " &f�ɷ� ����� ������ �߻��Ͽ����ϴ�."));
					} catch (Exception ex) {
						if(ex.getMessage() != null && !ex.getMessage().isEmpty()) {
							Messager.sendErrorMessage(ex.getMessage());
						} else {
							Messager.sendErrorMessage(ChatColor.translateAlternateColorCodes('&', "&e" + Ability.getName() + " &f�ɷ� ����� ������ �߻��Ͽ����ϴ�."));
						}
					}
				} else {
					Messager.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e" + Ability.getName() + " &f�ɷ��� ��ġ�� �̸��� �־� ��ϵ��� �ʾҽ��ϴ�."));
				}
			} else {
				Messager.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e" + Ability.getName() + " &f�ɷ��� AbilityManifest ������̼��� �������� �ʾ� ��ϵ��� �ʾҽ��ϴ�."));
			}
		}
	}
	
	private static boolean containsName(String name) {
		for(Class<? extends AbilityBase> abilityClass : Abilities) {
			AbilityManifest manifest = abilityClass.getAnnotation(AbilityManifest.class);
			if(manifest != null) {
				if(manifest.Name().equalsIgnoreCase(name)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * �÷����� �⺻ �ɷ� ���
	 */
	static {
		registerAbility(Assassin.class);
		registerAbility(Feather.class);
		registerAbility(Demigod.class);
		registerAbility(FastRegeneration.class);
		registerAbility(EnergyBlocker.class);
		registerAbility(DiceGod.class);
		registerAbility(Ares.class);
		registerAbility(Zeus.class);
		registerAbility(Berserker.class);
		registerAbility(Zombie.class);
		registerAbility(Terrorist.class);
		registerAbility(Yeti.class);
		registerAbility(Gladiator.class);
		registerAbility(Chaos.class);
		registerAbility(Void.class);
		registerAbility(DarkVision.class);
		registerAbility(HigherBeing.class);
		registerAbility(BlackCandle.class);
		registerAbility(FireFightWithFire.class);
		registerAbility(Hacker.class);
		registerAbility(Muse.class);
		registerAbility(Chaser.class);
		registerAbility(Flora.class);
		registerAbility(ShowmanShip.class);
		registerAbility(Virtus.class);
		registerAbility(Nex.class);
		registerAbility(Ira.class);
		registerAbility(OnlyOddNumber.class);
		registerAbility(Clown.class);
		registerAbility(TheMagician.class);
		registerAbility(TheHighPriestess.class);
		registerAbility(TheEmpress.class);
		registerAbility(TheEmperor.class);
		registerAbility(Pumpkin.class);
		registerAbility(Virus.class);
		registerAbility(Hermit.class);
		registerAbility(DevilBoots.class);
		registerAbility(BombArrow.class);
		registerAbility(Brewer.class);
		registerAbility(Imprison.class);
		registerAbility(SuperNova.class);
		registerAbility(Celebrity.class);
		registerAbility(ExpertOfFall.class);
		registerAbility(Curse.class);
		registerAbility(TimeRewind.class);
	}
	
	/**
	 * ��ϵ� �ɷµ��� �̸��� String List�� ��ȯ�մϴ�.
	 * AbilityManifest�� �������� �ʴ� �ɷ��� ���Ե��� �ʽ��ϴ�.
	 */
	public static List<String> nameValues() {
		ArrayList<String> Values = new ArrayList<String>();
		
		for(Class<? extends AbilityBase> abilityClass : Abilities) {
			AbilityManifest manifest = abilityClass.getAnnotation(AbilityManifest.class);
			if(manifest != null) {
				Values.add(manifest.Name());
			}
		}
		
		return Values;
	}

	/**
	 * ��ϵ� �ɷ� �� �ش� �̸��� �ɷ��� ��ȯ�մϴ�.
	 * AbilityManifest�� �������� �ʴ� �ɷ��̰ų� �������� �ʴ� �ɷ��� ��� null�� ��ȯ�մϴ�.
	 * @param name	�ɷ��� �̸�
	 * @return		�ɷ� Class
	 */
	public static Class<? extends AbilityBase> getByString(String name) {
		for(Class<? extends AbilityBase> abilityClass : Abilities) {
			AbilityManifest manifest = abilityClass.getAnnotation(AbilityManifest.class);
			if(manifest != null) {
				if(manifest.Name().equalsIgnoreCase(name)) {
					return abilityClass;
				}
			}
		}
		
		return null;
	}
	
}
