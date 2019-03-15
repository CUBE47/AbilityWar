package Marlang.AbilityWar.Game.Script.Objects.Setter.Special;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import Marlang.AbilityWar.Game.Script.ScriptWizard;
import Marlang.AbilityWar.Game.Script.Objects.Setter.Setter;
import Marlang.AbilityWar.Utils.Messager;

public class LoopCountSetter extends Setter<Integer> {

	public LoopCountSetter(ScriptWizard Wizard) {
		super("반복 ?��?��", -1, Wizard);
	}

	@Override
	public void execute(Listener listener, Event event) throws EventException {
		
	}

	@Override
	public void onClick(ClickType click) {
		if(!click.equals(ClickType.DROP)) {
			if(click.equals(ClickType.RIGHT)) {
				this.setValue(this.getValue() + 1);
			} else if(click.equals(ClickType.LEFT)) {
				if(getValue() > 0) {
					if(this.getValue() >= 2) {
						this.setValue(this.getValue() - 1);
					} else {
						this.setValue(1);
					}
				}
			}
		} else {
			if(getValue() > 0) {
				setValue(-1);
			} else {
				setValue(1);
			}
		}
	}

	@Override
	public ItemStack getItem() {
		ItemStack loopCount = new ItemStack(Material.DIAMOND);
		ItemMeta loopCountMeta = loopCount.getItemMeta();
		loopCountMeta.setDisplayName(ChatColor.AQUA + this.getKey());
		
		if(getWizard().Loop.getValue()) {
			if(getValue() > 0) {
				loopCountMeta.setLore(Messager.getStringList(
						ChatColor.translateAlternateColorCodes('&', "&e" + getValue() + "�? &f반복 ?��?��?��?��?��."),
						ChatColor.translateAlternateColorCodes('&', "&c?��?���?    &6» &e+ 1?��"),
						ChatColor.translateAlternateColorCodes('&', "&c좌클�?    &6» &e- 1?��"),
						ChatColor.translateAlternateColorCodes('&', "&cQ         &6» &e무한반복 ?���?")
						));
			} else {
				loopCountMeta.setLore(Messager.getStringList(
						ChatColor.translateAlternateColorCodes('&', "&e무한 &f반복?��?��?��."),
						ChatColor.translateAlternateColorCodes('&', "&cQ         &6» &e무한반복 ?���?")
						));
			}
		} else {
			loopCountMeta.setLore(Messager.getStringList(
					ChatColor.translateAlternateColorCodes('&', "&f반복 ?��?��?�� ?��?��?��?���? ?��?�� ?��?��?�� ?�� ?��?�� ?��?��?��?��?��.")
					));
		}
		
		loopCount.setItemMeta(loopCountMeta);
		
		return loopCount;
	}

}
