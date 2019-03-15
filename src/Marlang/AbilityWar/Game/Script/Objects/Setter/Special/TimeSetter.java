package Marlang.AbilityWar.Game.Script.Objects.Setter.Special;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import Marlang.AbilityWar.Game.Script.ScriptWizard;
import Marlang.AbilityWar.Game.Script.Objects.Setter.Setter;
import Marlang.AbilityWar.Utils.Library.Item.MaterialLib;
import Marlang.AbilityWar.Utils.Math.NumberUtil;

public class TimeSetter extends Setter<Integer> {
	
	public TimeSetter(ScriptWizard Wizard) {
		super("?���?", 30, Wizard);
	}
	
	@Override
	public void execute(Listener listener, Event event) throws EventException {}
	
	@Override
	public void onClick(ClickType click) {
		if(click.equals(ClickType.RIGHT)) {
			this.setValue(this.getValue() + 1);
		} else if(click.equals(ClickType.SHIFT_RIGHT)) {
			this.setValue(this.getValue() + 60);
		} else if(click.equals(ClickType.LEFT)) {
			if(this.getValue() >= 2) {
				this.setValue(this.getValue() - 1);
			} else {
				this.setValue(1);
			}
		} else if(click.equals(ClickType.SHIFT_LEFT)) {
			if(this.getValue() >= 61) {
				this.setValue(this.getValue() - 60);
			} else {
				this.setValue(1);
			}
		}
	}
	
	@Override
	public ItemStack getItem() {
		ItemStack watch = new ItemStack(MaterialLib.CLOCK.getMaterial());
		ItemMeta watchMeta = watch.getItemMeta();
		watchMeta.setDisplayName(ChatColor.AQUA + this.getKey());
		
		List<String> Lore = new ArrayList<String>();
		
		if(getWizard().Loop.getValue()) {
			Lore.add(ChatColor.translateAlternateColorCodes('&', "&f게임 ?��?�� ?�� &e" + NumberUtil.parseTimeString(this.getValue()) + "&f마다 ?��?��?��?��?��."));
		} else {
			Lore.add(ChatColor.translateAlternateColorCodes('&', "&f게임 ?��?�� &e" + NumberUtil.parseTimeString(this.getValue()) + " &f?��?�� ?��?��?��?��?��."));
		}
		
		Lore.add("");
		Lore.add(ChatColor.translateAlternateColorCodes('&', "&c?��?���?         &6» &e+ 1�?"));
		Lore.add(ChatColor.translateAlternateColorCodes('&', "&cSHIFT + ?��?���? &6» &e+ 1�?"));
		Lore.add(ChatColor.translateAlternateColorCodes('&', "&c좌클�?         &6» &e- 1�?"));
		Lore.add(ChatColor.translateAlternateColorCodes('&', "&cSHIFT + 좌클�? &6» &e- 1�?"));
		
		watchMeta.setLore(Lore);
		
		watch.setItemMeta(watchMeta);
		
		return watch;
	}
	
}
