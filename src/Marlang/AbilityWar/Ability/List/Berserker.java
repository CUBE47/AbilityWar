package Marlang.AbilityWar.Ability.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Ability.Timer.CooldownTimer;
import Marlang.AbilityWar.Ability.Timer.DurationTimer;
import Marlang.AbilityWar.Config.AbilitySettings.SettingObject;
import Marlang.AbilityWar.Utils.Messager;

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
		public void TimerStart(Data<?>... args) {
			Strengthen = true;
			
			super.TimerStart();
		}
		
		@Override
		public void DurationSkill(Integer Seconds) {}
		
		@Override
		public void TimerEnd() {
			Strengthen = false;
			
			super.TimerEnd();
		}
		
	};
	
	boolean Strengthen = false;
	
	@Override
	public boolean ActiveSkill(ActiveMaterialType mt, ActiveClickType ct) {
		if(mt.equals(ActiveMaterialType.Iron_Ingot)) {
			if(ct.equals(ActiveClickType.RightClick)) {
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
					getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, DebuffTime * 20, 1), true);
				}
			}
		}
	}

	@Override
	public void AbilityEvent(EventType type) {}

}
