package Marlang.AbilityWar.Config.Nodes;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import Marlang.AbilityWar.Utils.FileManager;

public enum ConfigNodes {
	
	Game_Header("����", "",
			"# �ɷ��� ���� ���� ����"),
	Game_Invincibility_Header("����.�ʹݹ���", "",
			"# �ʹ� ���� ����"),
	Game_Invincibility_Enable("����.�ʹݹ���.Ȱ��ȭ", true,
			"# �ʹ� ���� Ȱ��ȭ ����"),
	Game_Invincibility_Duration("����.�ʹݹ���.���ӽð�", 5,
			"# �ʹ� ���� Ȱ��ȭ ���� �ð� (����: ��)"),
	Game_Kit("����.�⺻��", FileManager.getItemStackList(new ItemStack(Material.DIAMOND_SWORD)),
			"# �⺻�� ����");
	
	String Path;
	Object Default;
	String[] Comments;
	
	ConfigNodes(String Path, Object Default, String... Comments) {
		
		this.Path = Path;
		this.Default = Default;
		this.Comments = Comments;
	}
	
	public String getPath() {
		return Path;
	}
	
	public Object getDefault() {
		return Default;
	}
	
	public String[] getComments() {
		return Comments;
	}
	
}
