package Marlang.AbilityWar.Ability.List;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Config.AbilitySettings.SettingObject;

public class FireFightWithFire extends AbilityBase {

	public static SettingObject<Integer> ChanceConfig = new SettingObject<Integer>("�̿�ġ��", "Chance", 50,
			"# ������ �޾��� �� �� �ۼ�Ʈ Ȯ���� ȸ���� ���� �����մϴ�.",
			"# 50�� 50%�� �ǹ��մϴ�.") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 1 && value <= 100;
		}
		
	};
	
	public FireFightWithFire(Player player) {
		super(player, "�̿�ġ��", Rank.B,
				ChatColor.translateAlternateColorCodes('&', "&f�� �������� ���� ��, " + ChanceConfig.getValue() + "% Ȯ���� ü���� ȸ���մϴ�."));
	}

	@Override
	public boolean ActiveSkill(ActiveMaterialType mt, ActiveClickType ct) {
		return false;
	}

	@Override
	public void PassiveSkill(Event event) {
		if(event instanceof EntityDamageEvent) {
			EntityDamageEvent e = (EntityDamageEvent) event;
			if(e.getEntity().equals(getPlayer())) {
				if(e.getCause().equals(DamageCause.FIRE) || e.getCause().equals(DamageCause.FIRE_TICK) || e.getCause().equals(DamageCause.LAVA)) {
					Random r = new Random();
					if(r.nextInt(100) <= ChanceConfig.getValue() - 1) {
						Double damage = e.getDamage();
						e.setDamage(0);
						
						Double health = getPlayer().getHealth() + damage;
						
						if(health > getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) health = getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
						
						if(!getPlayer().isDead()) {
							getPlayer().setHealth(health);
						}
					}
				}
			}
		}
	}

	@Override
	public void onRestrictClear() {}

}
