package Marlang.AbilityWar;

import org.bukkit.plugin.java.JavaPlugin;

import Marlang.AbilityWar.Utils.Messager;

/**
 * Ability War �ɷ��� ���� �÷�����
 * @author _Marlang ����
 */
public class AbilityWar extends JavaPlugin {
	
	@Override
	public void onEnable() {
		Messager.sendMessage("�÷������� Ȱ��ȭ�Ǿ����ϴ�.");
	}
	
	@Override
	public void onDisable() {
		Messager.sendMessage("�÷������� ��Ȱ��ȭ�Ǿ����ϴ�.");
	}
	
}
