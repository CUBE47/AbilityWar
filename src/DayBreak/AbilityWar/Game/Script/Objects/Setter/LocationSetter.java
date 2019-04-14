package DayBreak.AbilityWar.Game.Script.Objects.Setter;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import DayBreak.AbilityWar.Game.Script.ScriptWizard;
import DayBreak.AbilityWar.Utils.Messager;

public class LocationSetter extends Setter<Location> {

	public LocationSetter(String Key, Location Default, ScriptWizard Wizard) {
		super(Key, Default, Wizard);

		registerEvent(PlayerInteractEvent.class);
	}

	@Override
	public void execute(Listener listener, Event event) throws EventException {
		if(event instanceof PlayerInteractEvent) {
			PlayerInteractEvent e = (PlayerInteractEvent) event;
			if(e.getPlayer().equals(getWizard().getPlayer())) {
				Action action = e.getAction();
				if(action.equals(Action.LEFT_CLICK_BLOCK)) {
					if(Setting) {
						Setting = false;
						e.setCancelled(true);
						
						this.setValue(e.getClickedBlock().getLocation());
					}
				} else if(action.equals(Action.RIGHT_CLICK_BLOCK)) {
					if(Setting) {
						Setting = false;
						e.setCancelled(true);
						
						this.setValue(e.getClickedBlock().getLocation().add(0, 1, 0));
					}
				}
			}
		}
	}
	
	private boolean Setting = false;
	
	@Override
	public void onClick(ClickType click) {
		Setting = true;
		getWizard().safeClose();
		Messager.sendMessage(getWizard().getPlayer(), ChatColor.translateAlternateColorCodes('&', "&f������ &6��ġ&f�� Ŭ�����ּ���. &e��Ŭ��&f�� Ŭ���� ����� ��ġ�� �����ϰ�,"));
		Messager.sendMessage(getWizard().getPlayer(), ChatColor.translateAlternateColorCodes('&', "&e��Ŭ��&f�� Ŭ���� ��� ���� ��ġ�� �����մϴ�."));
	}

	@Override
	public ItemStack getItem() {
		ItemStack loc = new ItemStack(Material.COMPASS);
		ItemMeta locMeta = loc.getItemMeta();
		locMeta.setDisplayName(ChatColor.AQUA + this.getKey());
		if(this.getValue() != null) {
			Location l = this.getValue();
			String world = l.getWorld().getName();
			Double X = l.getX();
			Double Y = l.getY();
			Double Z = l.getZ();
			locMeta.setLore(Messager.getStringList(
					ChatColor.translateAlternateColorCodes('&', "&a����&f: " + world),
					ChatColor.translateAlternateColorCodes('&', "&bX&f: " + X),
					ChatColor.translateAlternateColorCodes('&', "&bY&f: " + Y),
					ChatColor.translateAlternateColorCodes('&', "&bZ&f: " + Z)
					));
		} else {
			locMeta.setLore(Messager.getStringList(
					ChatColor.translateAlternateColorCodes('&', "&f������ ��ġ�� �����ϴ�.")
					));
		}
		
		loc.setItemMeta(locMeta);
		
		return loc;
	}

}
