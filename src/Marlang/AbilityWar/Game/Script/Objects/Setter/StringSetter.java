package Marlang.AbilityWar.Game.Script.Objects.Setter;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import Marlang.AbilityWar.Game.Script.ScriptWizard;
import Marlang.AbilityWar.Utils.Messager;

public class StringSetter extends Setter<String> {
	
	public StringSetter(String Key, String Default, ScriptWizard Wizard) {
		super(Key, Default, Wizard);
		
		registerEvent(AsyncPlayerChatEvent.class);
	}
	
	@Override
	public void execute(Listener listener, Event event) throws EventException {
		if(event instanceof AsyncPlayerChatEvent) {
			AsyncPlayerChatEvent e = (AsyncPlayerChatEvent) event;
			if(e.getPlayer().equals(getWizard().getPlayer())) {
				if(Setting) {
					Setting = false;
					e.setCancelled(true);
					
					if(!e.getMessage().equals("%")) {
						//���� ������Ʈ�Ǹ� �κ��丮�� �ڵ����� ����
						this.setValue(e.getMessage());
					} else {
						this.updateGUI();
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
		Messager.sendMessage(getWizard().getPlayer(), ChatColor.translateAlternateColorCodes('&', "&f������ &6�ؽ�Ʈ&f�� ä��â�� �Է����ּ���. ����Ϸ��� &e%&f�� �Է����ּ���."));
	}
	
	@Override
	public ItemStack getItem() {
		ItemStack string = new ItemStack(Material.PAPER);
		ItemMeta stringMeta = string.getItemMeta();
		stringMeta.setDisplayName(ChatColor.AQUA + this.getKey());
		stringMeta.setLore(Messager.getStringList(
				ChatColor.translateAlternateColorCodes('&', "&f\"" + this.getValue() + "&f\""),
				"",
				ChatColor.translateAlternateColorCodes('&', "&6�ؽ�Ʈ&f�� �����Ϸ��� Ŭ���ϼ���.")
				));
		
		string.setItemMeta(stringMeta);
		
		return string;
	}
	
}
