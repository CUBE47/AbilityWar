package Marlang.AbilityWar.Ability.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Ability.Timer.CooldownTimer;
import Marlang.AbilityWar.Config.AbilitySettings.SettingObject;
import Marlang.AbilityWar.Utils.Messager;

public class Chaser extends AbilityBase {

	public static SettingObject<Integer> CooldownConfig = new SettingObject<Integer>("������", "Cooldown", 120,
			"# ��Ÿ��") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 0;
		}
		
	};
	
	public Chaser() {
		super("������", Rank.B,
				ChatColor.translateAlternateColorCodes('&', "&f������ ö���� Ÿ���ϸ� ��󿡰� ���� ��ġ�� �����մϴ�. " + Messager.formatCooldown(CooldownConfig.getValue())),
				ChatColor.translateAlternateColorCodes('&', "&f���� ö���� ��Ŭ���ϸ� ���� ��ġ�� ������ �÷��̾��� ��ǥ�� �� �� �ֽ��ϴ�."),
				ChatColor.translateAlternateColorCodes('&', "&f���� ��ġ�� �Ѹ��Ը� ������ �� �ֽ��ϴ�."));
		
		registerTimer(Cool);
	}

	CooldownTimer Cool = new CooldownTimer(this, CooldownConfig.getValue());
	
	Player target = null;
	
	@Override
	public void ActiveSkill(ActiveMaterialType mt, ActiveClickType ct) {
		if(mt.equals(ActiveMaterialType.Iron_Ingot)) {
			if(ct.equals(ActiveClickType.LeftClick)) {
				if(!Cool.isCooldown()) {
					Messager.sendMessage(getPlayer(), ChatColor.translateAlternateColorCodes('&', "&a���&f�� �����ϴ�!"));
				}
			} else if(ct.equals(ActiveClickType.RightClick)) {
				if(target != null) {
					int X = (int) target.getLocation().getX();
					int Y = (int) target.getLocation().getY();
					int Z = (int) target.getLocation().getZ();
					
					Messager.sendMessage(getPlayer(), ChatColor.translateAlternateColorCodes('&', "&e" + target.getName() + "&f���� &aX " + X + "&f, &aY " + Y + "&f, &aZ " + Z + "&f�� �ֽ��ϴ�."));
				} else {
					Messager.sendMessage(getPlayer(), ChatColor.translateAlternateColorCodes('&', "&f�ƹ����Ե� ���� ��ġ�� ��ô���� �ʾҽ��ϴ�. &8( &7���� �Ұ��� &8)"));
				}
			}
		}
	}

	@Override
	public void PassiveSkill(Event event) {
		if(event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
			if(e.getDamager().equals(getPlayer())) {
				if(e.getEntity() instanceof Player) {
					if(!e.isCancelled()) {
						if(getPlayer().getInventory().getItemInMainHand().getType().equals(Material.IRON_INGOT)) {
							if(!Cool.isCooldown()) {
								Player p = (Player) e.getEntity();
								this.target = p;
								Messager.sendMessage(getPlayer(), ChatColor.translateAlternateColorCodes('&', "&e" + p.getName() + "&f�Կ��� ���� ��ġ�� �����Ͽ����ϴ�."));
								
								Cool.StartTimer();
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void AbilityEvent(EventType type) {}

}
