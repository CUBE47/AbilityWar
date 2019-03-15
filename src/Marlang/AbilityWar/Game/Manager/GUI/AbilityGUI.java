package Marlang.AbilityWar.Game.Manager.GUI;

import java.util.ArrayList;
import java.util.Comparator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Ability.AbilityList;
import Marlang.AbilityWar.Game.Games.AbstractGame;
import Marlang.AbilityWar.Game.Games.AbstractGame.Participant;
import Marlang.AbilityWar.Utils.Messager;
import Marlang.AbilityWar.Utils.Thread.AbilityWarThread;

/**
 * ?��?�� �??�� GUI
 */
public class AbilityGUI implements Listener {

	private Player p;
	private Participant target;

	public AbilityGUI(Player p, Plugin Plugin) {
		this.p = p;
		Bukkit.getPluginManager().registerEvents(this, Plugin);
		
		Values = new ArrayList<String>(AbilityList.nameValues());
		Values.sort(new Comparator<String>() {
			
			public int compare(String obj1, String obj2) {
				return obj1.compareToIgnoreCase(obj2);
			}
			
		});
	}

	public AbilityGUI(Player p, Participant target, Plugin Plugin) {
		this.p = p;
		this.target = target;
		Bukkit.getPluginManager().registerEvents(this, Plugin);
		
		Values = new ArrayList<String>(AbilityList.nameValues());
		Values.sort(new Comparator<String>() {
			
			public int compare(String obj1, String obj2) {
				return obj1.compareToIgnoreCase(obj2);
			}
			
		});
	}
	
	private ArrayList<String> Values;
	
	private Integer PlayerPage = 1;
	
	private Inventory AbilityGUI;
	
	public void openAbilityGUI(Integer page) {
		Integer MaxPage = ((Values.size() - 1) / 36) + 1;
		if (MaxPage < page) page = 1;
		if(page < 1) page = 1;
		AbilityGUI = Bukkit.createInventory(null, 54, ChatColor.translateAlternateColorCodes('&', "&cAbilityWar &e?��?�� 목록"));
		PlayerPage = page;
		int Count = 0;
		
		for (String name : Values) {
			ItemStack is = new ItemStack(Material.IRON_BLOCK);
			ItemMeta im = is.getItemMeta();
			im.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&b" + name));
			im.setLore(Messager.getStringList(
					ChatColor.translateAlternateColorCodes('&', "&2» &f?�� ?��?��?�� �??��?��?���? ?���??��?��?��.")
					));
			is.setItemMeta(im);
			
			if (Count / 36 == page - 1) {
				AbilityGUI.setItem(Count % 36, is);
			}
			Count++;
		}
		
		if(page > 1) {
			ItemStack previousPage = new ItemStack(Material.ARROW, 1);
			ItemMeta previousMeta = previousPage.getItemMeta();
			previousMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&b?��?�� ?��?���?"));
			previousPage.setItemMeta(previousMeta);
			AbilityGUI.setItem(48, previousPage);
		}
		
		if(page != MaxPage) {
			ItemStack nextPage = new ItemStack(Material.ARROW, 1);
			ItemMeta nextMeta = nextPage.getItemMeta();
			nextMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&b?��?�� ?��?���?"));
			nextPage.setItemMeta(nextMeta);
			AbilityGUI.setItem(50, nextPage);
		}

		ItemStack Page = new ItemStack(Material.PAPER, 1);
		ItemMeta PageMeta = Page.getItemMeta();
		PageMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
				"&6?��?���? &e" + page + " &6/ &e" + MaxPage));
		Page.setItemMeta(PageMeta);
		AbilityGUI.setItem(49, Page);
		
		p.openInventory(AbilityGUI);
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		if(e.getInventory().equals(this.AbilityGUI)) {
			HandlerList.unregisterAll(this);
		}
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if(e.getInventory().equals(AbilityGUI)) {
			Player p = (Player) e.getWhoClicked();
			e.setCancelled(true);
			if(e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasDisplayName()) {
				if(e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', "&b?��?�� ?��?���?"))) {
					openAbilityGUI(PlayerPage - 1);
				} else if(e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', "&b?��?�� ?��?���?"))) {
					openAbilityGUI(PlayerPage + 1);
				}
			}
			
			if(e.getCurrentItem().getType().equals(Material.IRON_BLOCK)) {
				if(e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasDisplayName()) {
					String AbilityName = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
					
					Class<? extends AbilityBase> abilityClass = AbilityList.getByString(AbilityName);
					try {
						if(abilityClass != null) {
							if(AbilityWarThread.isGameTaskRunning()) {
								AbstractGame game = AbilityWarThread.getGame();
								if(target != null) {
									target.setAbility(abilityClass);
									Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&e" + p.getName() + "&a?��?�� &f" + target.getPlayer().getName() + "&a?��?���? ?��?��?�� ?��?���? �??��?��???��?��?��."));
								} else {
									for(Participant participant : game.getParticipants()) {
										participant.setAbility(abilityClass);
									}
									Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&e" + p.getName() + "&a?��?�� &f?���? ?��??&a?���? ?��?��?�� ?��?���? �??��?��???��?��?��."));
								}
							}
						} else {
							throw new Exception("Reflection Error");
						}
					} catch(Exception ex) {
						ex.printStackTrace();
						if(ex.getMessage() != null && !ex.getMessage().isEmpty()) {
							Messager.sendErrorMessage(p, ex.getMessage());
						} else {
							Messager.sendErrorMessage(p, "?��?�� ?���? ?��류�? 발생?��???��?��?��.");
						}
					}
					
					p.closeInventory();
				}
			}
		}
	}
	
}
