package Marlang.AbilityWar.Ability.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Ability.Timer.CooldownTimer;
import Marlang.AbilityWar.Ability.Timer.DurationTimer;
import Marlang.AbilityWar.Config.AbilitySettings.SettingObject;
import Marlang.AbilityWar.Utils.Messager;
import Marlang.AbilityWar.Utils.Library.EffectLib;

public class Berserker extends AbilityBase {

	public static SettingObject<Integer> CooldownConfig = new SettingObject<Integer>("����Ŀ", "Cooldown", 80,
			"# ��Ÿ��") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 0;
		}
		
	};

	public static SettingObject<Integer> StrengthConfig = new SettingObject<Integer>("����Ŀ", "Strength", 3,
			"# ���� ��ȭ ���") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 2;
		}
		
	};

	public static SettingObject<Integer> DebuffConfig = new SettingObject<Integer>("����Ŀ", "Debuff", 10,
			"# �ɷ� ��� �� ������� �޴� �ð�",
			"# ���� : ��") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 1;
		}
		
	};
	
	public Berserker(Player player) {
		super(player, "����Ŀ", Rank.B,
				ChatColor.translateAlternateColorCodes('&', "&fö���� ��Ŭ���� �� 5�� �ȿ� �ϴ� ���� ������ ��ȭ�˴ϴ�. " + Messager.formatCooldown(CooldownConfig.getValue())),
				ChatColor.translateAlternateColorCodes('&', "&f��ȭ�� ������ " + StrengthConfig.getValue() + "���� �������� ����, ��ȭ�� ������ ����� ��"),
				ChatColor.translateAlternateColorCodes('&', "&f" + DebuffConfig.getValue() + "�ʰ� �������� ���� �� �����ϴ�."));
	}

	Integer Strength = StrengthConfig.getValue();
	
	CooldownTimer Cool = new CooldownTimer(this, CooldownConfig.getValue());
	
	DurationTimer Duration = new DurationTimer(this, 5, Cool) {
		
		@Override
		public void onDurationStart() {
			Strengthen = true;
		}
		
		@Override
		public void DurationProcess(Integer Seconds) {}
		
		@Override
		public void onDurationEnd() {
			Strengthen = false;
		}
		
	};
	
	boolean Strengthen = false;
	
	@Override
	public boolean ActiveSkill(MaterialType mt, ClickType ct) {
		if(mt.equals(MaterialType.Iron_Ingot)) {
			if(ct.equals(ClickType.RightClick)) {
				if(!Duration.isDuration() && !Cool.isCooldown()) {
					Duration.StartTimer();
					
					return true;
				}
			}
		}
		
		return false;
	}

	Integer DebuffTime = DebuffConfig.getValue();
	
	@Override
	public void PassiveSkill(Event event) {
		if(event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
			if(e.getDamager().equals(getPlayer())) {
				if(Strengthen) {
					if(Duration.isDuration()) Duration.StopTimer(false);
					e.setDamage(e.getDamage() * Strength);
					EffectLib.WEAKNESS.addPotionEffect(getPlayer(), DebuffTime * 20, 1, true);
				}
			}
		}
	}

	@Override
	public void onRestrictClear() {}

	@Override
	public void TargetSkill(MaterialType mt, Entity entity) {}
	
}
