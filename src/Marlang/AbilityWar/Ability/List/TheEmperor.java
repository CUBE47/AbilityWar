package Marlang.AbilityWar.Ability.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Config.AbilitySettings.SettingObject;
import Marlang.AbilityWar.GameManager.Object.Participant;
import Marlang.AbilityWar.Utils.Library.EffectLib;
import Marlang.AbilityWar.Utils.Thread.TimerBase;

public class TheEmperor extends AbilityBase {

	public static SettingObject<Integer> DamageDecreaseConfig = new SettingObject<Integer>("Ȳ��", "DamageDecrease", 20, 
			"# ���� ���� ���ҷ�",
			"# 10���� �����ϸ� ������ �޾��� �� ��ü ������� 90%�� �޽��ϴ�.") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 1 && value <= 100;
		}
		
	};
	
	public TheEmperor(Participant participant) {
		super(participant, "Ȳ��", Rank.A, 
				ChatColor.translateAlternateColorCodes('&', "&f������ ǰ���ְ� �ɾ�� ���� ���ذ� ������ �����մϴ�."),
				ChatColor.translateAlternateColorCodes('&', "&fü���� ��ĭ ������ �� ���� ���ظ� ���� �ʽ��ϴ�."));
	}
	
	TimerBase Passive = new TimerBase() {
		
		@Override
		public void onStart() {}
		
		@Override
		public void TimerProcess(Integer Seconds) {
			EffectLib.SLOW.addPotionEffect(getPlayer(), 30, 1, true);
		}
		
		@Override
		public void onEnd() {}
		
	};
	
	@Override
	public boolean ActiveSkill(MaterialType mt, ClickType ct) {
		return false;
	}
	
	Integer DamageDecrease = DamageDecreaseConfig.getValue();
	
	@Override
	public void PassiveSkill(Event event) {
		if(event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
			if(e.getEntity().equals(getPlayer())) {
				Double damage = (e.getDamage() / 100) * (100 - DamageDecrease);
				e.setDamage(damage);
				
				Integer Health = (int) getPlayer().getHealth();
				if(Health <= 2) {
					e.setCancelled(true);
				}
			}
		}
	}

	@Override
	public void onRestrictClear() {
		Passive.StartTimer();
	}

	@Override
	public void TargetSkill(MaterialType mt, Entity entity) {}
	
}
