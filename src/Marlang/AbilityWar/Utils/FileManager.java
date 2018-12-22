package Marlang.AbilityWar.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

public class FileManager {
	
	private static File getDataFolder() {
		return new File("plugins/AbilityWar");
	}
	
	private static boolean createDataFolder() {
		File Folder = getDataFolder();
		if(!Folder.exists()) {
			Folder.mkdirs();
			return true;
		} else {
			return false;
		}
	}
	
	public static ArrayList<ItemStack> getItemStackList(ItemStack... List) {
		ArrayList<ItemStack> aList = new ArrayList<ItemStack>();
		for(ItemStack is : List) {
			aList.add(is);
		}
		
		return aList;
	}
	
	public static File getFile(String File) {
		
		File f = new File(getDataFolder().getPath() + "/" + File);
		
		try {
			if(createDataFolder()) {
				Messager.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e" + getDataFolder().getPath() + "&f ������ �����߽��ϴ�."));
			}
			
			if(!f.exists()) {
				f.createNewFile();
			}
			
			return f;
		} catch (IOException e) {
			Messager.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + f.getPath() + " ������ �������� ���߽��ϴ�."));
			return null;
		}
	}
	
}
