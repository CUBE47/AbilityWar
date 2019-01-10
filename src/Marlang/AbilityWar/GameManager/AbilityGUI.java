package Marlang.AbilityWar.GameManager;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Ability.AbilityList;
import Marlang.AbilityWar.GameManager.Module.Module;
import Marlang.AbilityWar.Utils.AbilityWarThread;
import Marlang.AbilityWar.Utils.Messager;

public class AbilityGUI extends Module implements Listener {
	
	public AbilityGUI() {
		RegisterListener(this);
	}
	
	static HashMap<Player, Integer> PlayerPage = new HashMap<Player, Integer>();
	
	static HashMap<Player, Player> AbilitySelect = new HashMap<Player, Player>();
	
	static Inventory AbilitySelectGUI;
	
	public static void openAbilitySelectGUI(Player p, Player target, Integer page) {
		AbilitySelect.put(p, target);
		
		try {
			if ((AbilityList.values().size() - 1) / 36 + 1 < page)
				page = 1;
			if(page < 1) page = 1;
			AbilitySelectGUI = Bukkit.createInventory(null, 54, ChatColor.translateAlternateColorCodes('&', "&cAbilityWar &e�ɷ� ���"));
			PlayerPage.put(p, page);
			int Count = 0;
			Integer MaxPage = ((AbilityList.values().size() - 1) / 36) + 1;
			
			for (String name : AbilityList.values()) {
				ItemStack is = new ItemStack(Material.IRON_BLOCK);
				ItemMeta im = is.getItemMeta();
				im.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&b" + name));
				im.setLore(Messager.getStringList(
						ChatColor.translateAlternateColorCodes('&', "&2�� &a�� �ɷ��� �ο��Ϸ��� Ŭ���ϼ���.")
						));
				is.setItemMeta(im);
				
				if (Count / 36 == page - 1) {
					AbilitySelectGUI.setItem(Count % 36, is);
				}
				Count++;
			}
			
			if(page > 1) {
				ItemStack previousPage = new ItemStack(Material.ARROW, 1);
				ItemMeta previousMeta = previousPage.getItemMeta();
				previousMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&b���� ������"));
				previousPage.setItemMeta(previousMeta);
				AbilitySelectGUI.setItem(48, previousPage);
			}
			
			if(page != MaxPage) {
				ItemStack nextPage = new ItemStack(Material.ARROW, 1);
				ItemMeta nextMeta = nextPage.getItemMeta();
				nextMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&b���� ������"));
				nextPage.setItemMeta(nextMeta);
				AbilitySelectGUI.setItem(50, nextPage);
			}

			ItemStack Page = new ItemStack(Material.PAPER, 1);
			ItemMeta PageMeta = Page.getItemMeta();
			PageMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
					"&6������ &e" + page + " &6/ &e" + MaxPage));
			Page.setItemMeta(PageMeta);
			AbilitySelectGUI.setItem(49, Page);
			
			p.openInventory(AbilitySelectGUI);
		} catch(Exception e) {}
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		Player p = (Player) e.getPlayer();
		if(AbilitySelect.containsKey(p)) {
			AbilitySelect.remove(p);
		}
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if(e.getInventory().equals(AbilitySelectGUI)) {
			Player p = (Player) e.getWhoClicked();
			e.setCancelled(true);
			if(e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasDisplayName()) {
				if(e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', "&b���� ������"))) {
					openAbilitySelectGUI(p, AbilitySelect.get(p), PlayerPage.get(p) - 1);
				} else if(e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', "&b���� ������"))) {
					openAbilitySelectGUI(p, AbilitySelect.get(p), PlayerPage.get(p) + 1);
				}
			}
			
			if(e.getCurrentItem().getType().equals(Material.IRON_BLOCK)) {
				if(e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasDisplayName()) {
					Player target = Bukkit.getPlayer(AbilitySelect.get(p).getName());
					String AbilityName = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
					
					Class<? extends AbilityBase> l = AbilityList.getByString(AbilityName);
					try {
						if(target != null) {
							if(l != null) {
								AbilityBase Ability = l.newInstance();
								Ability.setPlayer(target);
								
								if(AbilityWarThread.getGame().getInvincibility().isTimerRunning()) {
									Ability.setRestricted(true);
								} else {
									if(AbilityWarThread.getGame().isGameStarted()) {
										Ability.setRestricted(false);
									} else {
										Ability.setRestricted(true);
									}
								}
								
								AbilityWarThread.getGame().removeAbility(target);
								AbilityWarThread.getGame().addAbility(Ability);
							}
						} else {
							throw new Exception("�ش� �÷��̾ �������� �ʽ��ϴ�.");
						}
					} catch(Exception ex) {
						if(!ex.getMessage().isEmpty()) {
							Messager.sendErrorMessage(p, ex.getMessage());
						} else {
							Messager.sendErrorMessage(p, "���� ���� ������ �߻��Ͽ����ϴ�.");
						}
					}
					
					p.closeInventory();
					
					Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&e" + p.getName() + "&a���� &f" + target.getName() + "&a�Կ��� �ɷ��� ���Ƿ� �ο��Ͽ����ϴ�."));
				}
			}
		}
	}
}
