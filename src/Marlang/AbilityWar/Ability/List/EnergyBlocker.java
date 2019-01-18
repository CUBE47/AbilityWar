package Marlang.AbilityWar.Ability.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Ability.Timer.CooldownTimer;
import Marlang.AbilityWar.Config.AbilitySettings.SettingObject;
import Marlang.AbilityWar.Utils.Messager;

public class EnergyBlocker extends AbilityBase {

	public static SettingObject<Integer> CooldownConfig = new SettingObject<Integer>("���������Ŀ", "Cooldown", 10, 
			"# ��Ÿ��") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 0;
		}
		
	};
	
	boolean Default = true;
	
	public EnergyBlocker() {
		super("������ ���Ŀ", Rank.A,
				ChatColor.translateAlternateColorCodes('&', "&f���Ÿ� ���� ���ظ� ��������, �ٰŸ� ���� ���ظ� �� ��� �ްų�"),
				ChatColor.translateAlternateColorCodes('&', "&f���Ÿ� ���� ���ظ� �� ���, �ٰŸ� ���� ���ظ� �������� ���� �� �ֽ��ϴ�."),
				ChatColor.translateAlternateColorCodes('&', "ö���� ��Ŭ���ϸ� ������ ���� ������ �ڹٲߴϴ�. " + Messager.formatCooldown(CooldownConfig.getValue())),
				ChatColor.translateAlternateColorCodes('&', "&f���� ���� &7: &b���Ÿ� &f����&7, &a�ٰŸ� &f�� ��"));
		
		registerTimer(Cool);
	}
	
	CooldownTimer Cool = new CooldownTimer(this, CooldownConfig.getValue());
	
	@Override
	public void ActiveSkill(ActiveMaterialType mt, ActiveClickType ct) {
		if(mt.equals(ActiveMaterialType.Iron_Ingot)) {
			if(ct.equals(ActiveClickType.RightClick)) {
				if(!Cool.isCooldown()) {
					Default = !Default;
					Player p = getPlayer();
					if(Default) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b���Ÿ� &f����&7, &a�ٰŸ� &f�� ��� ����Ǿ����ϴ�."));
						this.setExplain(
								ChatColor.translateAlternateColorCodes('&', "&f���Ÿ� ���� ���ظ� ��������, �ٰŸ� ���� ���ظ� �� ��� �ްų�"),
								ChatColor.translateAlternateColorCodes('&', "&f���Ÿ� ���� ���ظ� �� ���, �ٰŸ� ���� ���ظ� �������� ���� �� �ֽ��ϴ�."),
								ChatColor.translateAlternateColorCodes('&', "ö���� ��Ŭ���ϸ� ������ ���� ������ �ڹٲߴϴ�. &c��Ÿ�� &7: &f30��"),
								ChatColor.translateAlternateColorCodes('&', "&f���� ���� &7: &b���Ÿ� &f����&7, &a�ٰŸ� &f�� ��"));
					} else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b���Ÿ� &f�� ��&7, &a�ٰŸ� &f�������� ����Ǿ����ϴ�."));
						this.setExplain(
								ChatColor.translateAlternateColorCodes('&', "&f���Ÿ� ���� ���ظ� ��������, �ٰŸ� ���� ���ظ� �� ��� �ްų�"),
								ChatColor.translateAlternateColorCodes('&', "&f���Ÿ� ���� ���ظ� �� ���, �ٰŸ� ���� ���ظ� �������� ���� �� �ֽ��ϴ�."),
								ChatColor.translateAlternateColorCodes('&', "ö���� ��Ŭ���ϸ� ������ ���� ������ �ڹٲߴϴ�. &c��Ÿ�� &7: &f30��"),
								ChatColor.translateAlternateColorCodes('&', "&f���� ���� &7: &b���Ÿ� &f�� ��&7, &a�ٰŸ� &f����"));
					}
					
					Cool.StartTimer();
				}
			}
		}
	}
	
	@Override
	public void PassiveSkill(Event event) {
		if(event instanceof EntityDamageEvent) {
			EntityDamageEvent e = (EntityDamageEvent) event;
			if(e.getEntity() instanceof Player) {
				Player p = (Player) e.getEntity();
				if(p.equals(getPlayer())) {
					if(!e.isCancelled()) {
						DamageCause dc = e.getCause();
						if(dc != null) {
							if(dc.equals(DamageCause.PROJECTILE)) {
								if(Default) {
									e.setDamage(e.getDamage() / 2);
								} else {
									e.setDamage(e.getDamage() * 2);
								}
							} else if(dc.equals(DamageCause.ENTITY_ATTACK)) {
								if(Default) {
									e.setDamage(e.getDamage() * 2);
								} else {
									e.setDamage(e.getDamage() / 2);
								}
							}
						}
					}
				}
			}
		}
	}
	
}
