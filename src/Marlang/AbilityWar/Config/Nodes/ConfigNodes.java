package Marlang.AbilityWar.Config.Nodes;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import Marlang.AbilityWar.Utils.FileManager;

public enum ConfigNodes {
	
	Game_Header("����", "",
			"# ��� ���Ǳ״� �ΰ��ӿ��� /va config ��ɾ��",
			"# ������ �� �ֽ��ϴ�."),
	Game_NoHunger("����.�����", true,
			"",
			"# ����� ������ Ȱ��ȭ ����"),
	Game_StartLevel("����.����", 180,
			"",
			"# ���� ���۽� ������ ����",
			"# 0���� �����ϸ� ���޵��� ����"),
	Game_Invincibility_Enable("����.�ʹݹ���.Ȱ��ȭ", true,
			"# �ʹ� ���� Ȱ��ȭ ����"),
	Game_Invincibility_Duration("����.�ʹݹ���.���ӽð�", 5,
			"# �ʹ� ���� Ȱ��ȭ ���� �ð� (����: ��)"),
	Game_Kit("����.�⺻��", FileManager.getItemStackList(new ItemStack(Material.DIAMOND_SWORD)),
			"# �⺻�� ����"),
	Game_Spawn_Location("����.����.��ġ", Bukkit.getWorlds().get(0).getSpawnLocation(),
			"# ���� ��ġ ����"),
	Game_Spawn_Enable("����.����.�̵�", true,
			"# �ʹ� ���� �̵� Ȱ��ȭ ����"),
	Game_OldMechanics_Header("����.������", "",
			"",
			"# ��ġ �� �������� �÷����ϰ� �ִ� ��ó�� ������ ���ݴϴ�.",
			"# �� ���� ������ �ɷ��� ������ ���۵Ǹ� ����˴ϴ�."),
	Game_OldMechanics_Enchant("����.������.�����ο�", false,
			"# ���� �ο��� û�ݼ� ���� �� �� �ְ� ���ݴϴ�.");
	
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
