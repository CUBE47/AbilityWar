package Marlang.AbilityWar;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Ability.AbilityList;
import Marlang.AbilityWar.Config.AbilitySettings;
import Marlang.AbilityWar.Config.AbilityWarSettings;
import Marlang.AbilityWar.GameManager.MainCommand;
import Marlang.AbilityWar.GameManager.Module.Module;
import Marlang.AbilityWar.Utils.Messager;
import Marlang.AbilityWar.Utils.AutoUpdate.AutoUpdate;
import Marlang.AbilityWar.Utils.AutoUpdate.AutoUpdate.Branch;

/**
 * Ability War �ɷ��� ���� �÷�����
 * @author _Marlang ����
 */
public class AbilityWar extends JavaPlugin {
	
	private static Plugin Plugin;
	
	public static Plugin getPlugin() {
		return AbilityWar.Plugin;
	}
	
	AutoUpdate au = new AutoUpdate("Marlang365", "test", this, Branch.Master);
	
	@Override
	public void onEnable() {
		AbilityWar.Plugin = this;
		
		if(au.Check()) {
			Messager.sendMessage("Server Version: " + Bukkit.getServer().getBukkitVersion());
			
			Load();
			
			Module.Setup();
			
			AbilityWarSettings.Setup();
			AbilitySettings.Setup();
			
			Messager.sendMessage("�÷������� Ȱ��ȭ�Ǿ����ϴ�.");
		}
	}
	
	public void Load() {
		Bukkit.getPluginCommand("AbilityWar").setExecutor(new MainCommand());
		
		for(String name : AbilityList.values()) {
			try {
				Class<? extends AbilityBase> Ability = AbilityList.getByString(name);
				Class.forName(Ability.getName());
			} catch(Exception e) {
				Messager.sendErrorMessage(ChatColor.translateAlternateColorCodes('&', "&e" + name + " &f�ɷ��� �ҷ����� ���Ͽ����ϴ�."));
			}
		}
	}
	
	@Override
	public void onDisable() {
		AbilityWarSettings.Refresh();
		
		Messager.sendMessage("�÷������� ��Ȱ��ȭ�Ǿ����ϴ�.");
	}
	
}
