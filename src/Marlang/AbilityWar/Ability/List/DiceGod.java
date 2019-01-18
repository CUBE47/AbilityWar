package Marlang.AbilityWar.Ability.List;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Ability.Timer.CooldownTimer;
import Marlang.AbilityWar.Utils.Messager;

public class DiceGod extends AbilityBase {
	
	public DiceGod() {
		super("���̽� ��", Rank.God, 
				ChatColor.translateAlternateColorCodes('&', "&fö���� ��Ŭ���ϸ� &c��� &f/ &b�ż� &f/ &6�� &f/ &5���� &f/ &8���� &f/ &7������ &fȿ�� �� �ϳ���"),
				ChatColor.translateAlternateColorCodes('&', "&f10�ʰ� �޽��ϴ�. " + Messager.formatCooldown(60)),
				ChatColor.translateAlternateColorCodes('&', "&f���� �ֻ��� ���̸� ���� �ʴ´ٴ���..."));
		
		registerTimer(Cool);
	}
	
	CooldownTimer Cool = new CooldownTimer(this, 60);
	
	@Override
	public void ActiveSkill(ActiveMaterialType mt, ActiveClickType ct) {
		if(mt.equals(ActiveMaterialType.Iron_Ingot)) {
			if(ct.equals(ActiveClickType.RightClick)) {
				if(!Cool.isCooldown()) {
					Player p = getPlayer();
					
					Random r = new Random();
					Integer random = r.nextInt(6);
					
					if(random.equals(0)) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c��� &fȿ���� �޾ҽ��ϴ�."));
						p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 1), true);
					} else if(random.equals(1)) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b�ż� &fȿ���� �޾ҽ��ϴ�."));
						p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 1), true);
					} else if(random.equals(2)) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6�� &fȿ���� �޾ҽ��ϴ�."));
						p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 200, 1), true);
					} else if(random.equals(3)) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&5���� &fȿ���� �޾ҽ��ϴ�."));
						p.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 200, 1), true);
					} else if(random.equals(4)) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8���� &fȿ���� �޾ҽ��ϴ�."));
						p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200, 1), true);
					} else if(random.equals(5)) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7������ &fȿ���� �޾ҽ��ϴ�."));
						p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 200, 1), true);
					}
					
					Cool.StartTimer();
				}
			}
		}
	}
	
	@Override
	public void PassiveSkill(Event event) {}
	
}
