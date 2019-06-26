package DayBreak.AbilityWar.Ability.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;

import DayBreak.AbilityWar.Ability.AbilityBase;
import DayBreak.AbilityWar.Ability.AbilityManifest;
import DayBreak.AbilityWar.Ability.AbilityManifest.Rank;
import DayBreak.AbilityWar.Config.AbilitySettings.SettingObject;
import DayBreak.AbilityWar.Game.Games.Mode.AbstractGame.Participant;
import DayBreak.AbilityWar.Utils.VersionCompat.VersionUtil;

@AbilityManifest(Name = "Ȧ��������", Rank = Rank.S)
public class OnlyOddNumber extends AbilityBase {

	public static SettingObject<Integer> PercentageConfig = new SettingObject<Integer>(OnlyOddNumber.class, "Percentage", 79, 
			"# ü���� �� �ۼ�Ʈ ������ �� �ɷ��� �ߵ����� �����մϴ�.",
			"# 1 �̻�, 100 ������ �� �� Ȧ���θ� ������ �� �ֽ��ϴ�.") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 1 && value <= 100 && value % 2 != 0;
		}
		
	};

	public static SettingObject<Integer> OddNumberConfig = new SettingObject<Integer>(OnlyOddNumber.class, "OddNumber", 39, 
			"# ü���� Ȧ���� �� �������� �� �ۼ�Ʈ �ٿ� ������ �����մϴ�.",
			"# 60���� �����ϸ� ���� �������� 40%�� �޽��ϴ�.") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 1 && value <= 100 && value % 2 != 0;
		}
		
	};

	public static SettingObject<Integer> EvenNumberConfig = new SettingObject<Integer>(OnlyOddNumber.class, "EvenNumber", 29, 
			"# ü���� ¦���� �� �������� �� �ۼ�Ʈ �÷� ������ �����մϴ�.",
			"# 30���� �����ϸ� ���� �������� 130%�� �޽��ϴ�.") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 1 && value <= 100 && value % 2 != 0;
		}
		
	};
	
	public OnlyOddNumber(Participant participant) {
		super(participant,
				ChatColor.translateAlternateColorCodes('&', "&fü���� �ִ� ü���� " + PercentageConfig.getValue() + "% ������ ��"),
				ChatColor.translateAlternateColorCodes('&', "&f������ ������ ü�¿� ���� �ٸ� ȿ���� �޽��ϴ�. &fü���� Ȧ���� ��� ��������"),
				ChatColor.translateAlternateColorCodes('&', OddNumberConfig.getValue() + "% �ٿ� �ް�, ü���� ¦���� ��� �������� " + EvenNumberConfig.getValue() + "% �÷� �޽��ϴ�."));
	}
	
	@Override
	public boolean ActiveSkill(MaterialType mt, ClickType ct) {
		return false;
	}
	
	Integer Percentage = PercentageConfig.getValue();
	Integer Odd = OddNumberConfig.getValue();
	Integer Even = EvenNumberConfig.getValue();
	
	@Override
	public void PassiveSkill(Event event) {
		if(event instanceof EntityDamageEvent) {
			EntityDamageEvent e = (EntityDamageEvent) event;
			if(e.getEntity().equals(getPlayer())) {
				Double doubleMaxHealth = VersionUtil.getMaxHealth(getPlayer());
				Double doubleHealth = getPlayer().getHealth();
				
				Integer Health = (int) getPlayer().getHealth();
				
				if(doubleHealth <= (doubleMaxHealth / 100) * Percentage) {
					if(Health % 2 == 0) { //¦��
						e.setDamage(e.getDamage() + ((e.getDamage() / 100) * Even));
					} else { //Ȧ��
						e.setDamage(e.getDamage() - ((e.getDamage() / 100) * Odd));
					}
				}
			}
		}
	}

	@Override
	public void onRestrictClear() {}

	@Override
	public void TargetSkill(MaterialType mt, Entity entity) {}
	
}
