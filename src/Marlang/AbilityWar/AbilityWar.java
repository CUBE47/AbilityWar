package Marlang.AbilityWar;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import Marlang.AbilityWar.GameManager.Game;
import Marlang.AbilityWar.GameManager.MainCommand;
import Marlang.AbilityWar.Utils.AbilityWarThread;
import Marlang.AbilityWar.Utils.Messager;

/**
 * Ability War �ɷ��� ���� �÷�����
 * @author _Marlang ����
 */
public class AbilityWar extends JavaPlugin {
	
	@Override
	public void onEnable() {
		AbilityWarThread.Initialize(this);
		Messager.Initialize(this);
		Game.Initialize(this);
		
		Load();
		
		Messager.sendMessage("�÷������� Ȱ��ȭ�Ǿ����ϴ�.");
	}
	
	public void Load() {
		Bukkit.getPluginCommand("AbilityWar").setExecutor(new MainCommand());
	}
	
	@Override
	public void onDisable() {
		Messager.sendMessage("�÷������� ��Ȱ��ȭ�Ǿ����ϴ�.");
	}
	
}
