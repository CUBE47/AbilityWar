package Marlang.AbilityWar;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import Marlang.AbilityWar.Config.AbilityWarSettings;
import Marlang.AbilityWar.GameManager.Game;
import Marlang.AbilityWar.GameManager.MainCommand;
import Marlang.AbilityWar.GameManager.Module.Module;
import Marlang.AbilityWar.Utils.AbilityWarThread;
import Marlang.AbilityWar.Utils.Messager;
import Marlang.AbilityWar.Utils.TimerBase;

/**
 * Ability War �ɷ��� ���� �÷�����
 * @author _Marlang ����
 */
public class AbilityWar extends JavaPlugin {
	
	private static AbilityWarSettings Settings = new AbilityWarSettings();
	
	@Override
	public void onEnable() {
		AbilityWarThread.Initialize(this);
		Game.Initialize(this);
		TimerBase.Initialize(this);
		Module.Initialize(this);
		
		Settings.Setup();
		
		Load();
		
		Messager.sendMessage("�÷������� Ȱ��ȭ�Ǿ����ϴ�.");
	}
	
	public static AbilityWarSettings getSetting() {
		return Settings;
	}
	
	public void Load() {
		Bukkit.getPluginCommand("AbilityWar").setExecutor(new MainCommand());
	}
	
	@Override
	public void onDisable() {
		Messager.sendMessage("�÷������� ��Ȱ��ȭ�Ǿ����ϴ�.");
	}
	
}
