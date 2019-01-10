package Marlang.AbilityWar.Ability.List;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import Marlang.AbilityWar.Ability.AbilityBase;

public class Demigod extends AbilityBase {
	
	public Demigod() {
		super("���̰�", Rank.God,
				ChatColor.translateAlternateColorCodes('&', "&f�ݽŹ����� �ɷ����Դϴ�. ������ ������"),
				ChatColor.translateAlternateColorCodes('&', "&f40% Ȯ���� 5�ʰ� ���� ������ �ߵ��˴ϴ�."));
	}
	
	@Override
	public void ActiveSkill(ActiveMaterialType mt, ActiveClickType ct) {}
	
	@Override
	public void PassiveSkill(Event event) {
		if(event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
			if(e.getEntity() instanceof Player) {
				Player p = (Player) e.getEntity();
				if(p.equals(getPlayer())) {
					if(!e.isCancelled()) {
						Random r = new Random();
						
						if(r.nextInt(10) >= 6) {
							Integer Buff = r.nextInt(3);
							if(Buff.equals(0)) {
								p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 100, 0), true);
							} else if(Buff.equals(1)) {
								p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 1), true);
							} else if(Buff.equals(2)) {
								p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 1), true);
							}
						}
					}
				}
			}
		}
	}
	
}
