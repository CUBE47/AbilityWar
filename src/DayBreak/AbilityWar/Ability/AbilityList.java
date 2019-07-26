package DayBreak.AbilityWar.Ability;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;

import DayBreak.AbilityWar.Ability.AbilityManifest.Rank;
import DayBreak.AbilityWar.Ability.AbilityManifest.Species;
import DayBreak.AbilityWar.Ability.List.Ares;
import DayBreak.AbilityWar.Ability.List.Assassin;
import DayBreak.AbilityWar.Ability.List.Berserker;
import DayBreak.AbilityWar.Ability.List.BlackCandle;
import DayBreak.AbilityWar.Ability.List.BombArrow;
import DayBreak.AbilityWar.Ability.List.Brewer;
import DayBreak.AbilityWar.Ability.List.Celebrity;
import DayBreak.AbilityWar.Ability.List.Chaos;
import DayBreak.AbilityWar.Ability.List.Chaser;
import DayBreak.AbilityWar.Ability.List.Clown;
import DayBreak.AbilityWar.Ability.List.Curse;
import DayBreak.AbilityWar.Ability.List.DarkVision;
import DayBreak.AbilityWar.Ability.List.Demigod;
import DayBreak.AbilityWar.Ability.List.DevilBoots;
import DayBreak.AbilityWar.Ability.List.DiceGod;
import DayBreak.AbilityWar.Ability.List.EnergyBlocker;
import DayBreak.AbilityWar.Ability.List.ExpertOfFall;
import DayBreak.AbilityWar.Ability.List.FastRegeneration;
import DayBreak.AbilityWar.Ability.List.Feather;
import DayBreak.AbilityWar.Ability.List.FireFightWithFire;
import DayBreak.AbilityWar.Ability.List.Flora;
import DayBreak.AbilityWar.Ability.List.Gladiator;
import DayBreak.AbilityWar.Ability.List.Hacker;
import DayBreak.AbilityWar.Ability.List.Hermit;
import DayBreak.AbilityWar.Ability.List.HigherBeing;
import DayBreak.AbilityWar.Ability.List.Imprison;
import DayBreak.AbilityWar.Ability.List.Ira;
import DayBreak.AbilityWar.Ability.List.JellyFish;
import DayBreak.AbilityWar.Ability.List.Khazhad;
import DayBreak.AbilityWar.Ability.List.Muse;
import DayBreak.AbilityWar.Ability.List.Nex;
import DayBreak.AbilityWar.Ability.List.OnlyOddNumber;
import DayBreak.AbilityWar.Ability.List.Pumpkin;
import DayBreak.AbilityWar.Ability.List.ShowmanShip;
import DayBreak.AbilityWar.Ability.List.Sniper;
import DayBreak.AbilityWar.Ability.List.SuperNova;
import DayBreak.AbilityWar.Ability.List.Terrorist;
import DayBreak.AbilityWar.Ability.List.TheEmperor;
import DayBreak.AbilityWar.Ability.List.TheEmpress;
import DayBreak.AbilityWar.Ability.List.TheHighPriestess;
import DayBreak.AbilityWar.Ability.List.TheMagician;
import DayBreak.AbilityWar.Ability.List.TimeRewind;
import DayBreak.AbilityWar.Ability.List.Virtus;
import DayBreak.AbilityWar.Ability.List.Virus;
import DayBreak.AbilityWar.Ability.List.Void;
import DayBreak.AbilityWar.Ability.List.Yeti;
import DayBreak.AbilityWar.Ability.List.Zeus;
import DayBreak.AbilityWar.Ability.List.Zombie;
import DayBreak.AbilityWar.Config.AbilitySettings.SettingObject;
import DayBreak.AbilityWar.Utils.Messager;

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
		//��â�� �ɷ���
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
		
		//2019 ���� ������Ʈ
		registerAbility(Khazhad.class);
		registerAbility(Sniper.class);
		registerAbility(JellyFish.class);
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

	public static List<String> getAbilityNames(Rank r) {
		List<String> list = new ArrayList<String>();
		
		for(String name : AbilityList.nameValues()) {
			Class<? extends AbilityBase> clazz = AbilityList.getByString(name);
			AbilityManifest manifest = clazz.getAnnotation(AbilityManifest.class);
			if(manifest != null) {
				if(manifest.Rank().equals(r)) {
					list.add(name);
				}
			}
		}
		
		return list;
	}

	public static List<String> getAbilityNames(Species s) {
		List<String> list = new ArrayList<String>();
		
		for(String name : AbilityList.nameValues()) {
			Class<? extends AbilityBase> clazz = AbilityList.getByString(name);
			AbilityManifest manifest = clazz.getAnnotation(AbilityManifest.class);
			if(manifest != null) {
				if(manifest.Species().equals(s)) {
					list.add(name);
				}
			}
		}
		
		return list;
	}
	
}
