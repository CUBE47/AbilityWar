package Marlang.AbilityWar;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Ability.AbilityList;
import Marlang.AbilityWar.Config.AbilitySettings;
import Marlang.AbilityWar.Config.AbilityWarSettings;
import Marlang.AbilityWar.GameManager.MainCommand;
import Marlang.AbilityWar.GameManager.Module.Module;
import Marlang.AbilityWar.Utils.AbilityWarThread;
import Marlang.AbilityWar.Utils.Messager;

/**
 * Ability War �ɷ��� ���� �÷�����
 * @author _Marlang ����
 */
public class AbilityWar extends JavaPlugin {
	
	private static AbilityWar Plugin;
	
	public static AbilityWar getPlugin() {
		return AbilityWar.Plugin;
	}
	
	@Override
	public void onEnable() {
		AbilityWar.Plugin = this;
		
		Load();
		
		Module.Setup();
		
		AbilityWarSettings.Setup();
		AbilitySettings.Setup();

		Messager.sendMessage("Server Version: " + Bukkit.getBukkitVersion());
		Messager.sendMessage("�÷������� Ȱ��ȭ�Ǿ����ϴ�.");
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
		if(AbilityWarThread.isGameTaskRunning()) {
			AbilityWarThread.toggleGameTask(false);
		}
		Messager.sendMessage("�÷������� ��Ȱ��ȭ�Ǿ����ϴ�.");
	}
	
}
