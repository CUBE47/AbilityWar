package Marlang.AbilityWar.Ability.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.TippedArrow;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;

import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Utils.Messager;

public class TheEmperor extends AbilityBase {

	public TheEmperor() {
		super("Ȳ��", Rank.A,
				ChatColor.translateAlternateColorCodes('&', "&f�ǰ� ��ĭ�� �� �������� ���� �ʽ��ϴ�."),
				ChatColor.translateAlternateColorCodes('&', "&f���� ȭ���� ������ ȭ���� �� ��󿡰Ե� ���� ȿ���� �����ϰ� �����մϴ�."),
				Messager.formatTarotCard(4, "The Emperor"));
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
				if(getPlayer().getHealth() == 2) {
					e.setCancelled(true);
				}
			}
		} else if(event instanceof ProjectileHitEvent) {
			ProjectileHitEvent e = (ProjectileHitEvent) event;
			if(e.getHitEntity() != null && e.getHitEntity().equals(getPlayer())) {
				if(e.getEntity() instanceof TippedArrow) {
					TippedArrow arrow = (TippedArrow) e.getEntity();
					if(arrow.getShooter() instanceof LivingEntity) {
						((LivingEntity) arrow.getShooter()).addPotionEffect(new PotionEffect(PotionType.FIRE_RESISTANCE.getEffectType(), 60, 0), true);
					}
				}
			}
		}
	}

	@Override
	public void AbilityEvent(EventType type) {}

}
