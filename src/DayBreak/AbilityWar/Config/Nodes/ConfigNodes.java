package DayBreak.AbilityWar.Config.Nodes;

import org.bukkit.Bukkit;

import DayBreak.AbilityWar.Game.Games.Default.DefaultGame;
import DayBreak.AbilityWar.Utils.Messager;
import DayBreak.AbilityWar.Utils.Data.FileManager;

public enum ConfigNodes {
	
	Game_NoHunger("����.�����", true,
			"# ����� ������ Ȱ��ȭ ����"),
	Game_StartLevel("����.����", 180,
			"# ���� ���۽� ������ ����",
			"# 0���� �����ϸ� ���޵��� ����"),
	Game_Invincibility_Enable("����.�ʹݹ���.Ȱ��ȭ", true,
			"# �ʹ� ���� Ȱ��ȭ ����"),
	Game_Invincibility_Duration("����.�ʹݹ���.���ӽð�", 5,
			"# �ʹ� ���� Ȱ��ȭ ���� �ð� (����: ��)"),
	Game_Kit("����.�⺻��", FileManager.getItemStackList(),
			"# �⺻�� ����"),
	Game_InventoryClear("����.�κ��丮�ʱ�ȭ", true,
			"# ���� ���۽� �κ��丮 �ʱ�ȭ ����"),
	Game_DrawAbility("����.�ɷ���÷", true,
			"# ���� ���۽� �ɷ� ��÷ ����"),
	Game_InfiniteDurability("����.����������", false,
			"# ������ ���� ����"),
	Game_Firewall("����.��ȭ��", true,
			"# ��ȭ�� Ȱ��ȭ ����",
			"# true�� �����ϸ� ������ ���۵ǰ� �� �� ������ �Ǵ� �����ڰ� �ƴ� ������ ������ �� �����ϴ�.",
			"# ������ ������ ������ ���� ��� �̸� �����ϰ� ������ �� �ֽ��ϴ�."),
	Game_Death_Header("����.���", "",
			"# �÷��̾� ��� ���Ǳ�"),
	Game_Deaeth_Eliminate("����.���.Ż��", true,
			"# ���� ���� �� ��� �� Ż�� ����",
			"# true�� �����ϸ� ������ ���۵ǰ� �� �� ����� ��� Ż���մϴ�.",
			"# Ż���� ������ ������ ���� ������ ������ ������ �� �����ϴ�.",
			"# ������ ������ ������ ���� ��� �̸� �����ϰ� ������ �� �ֽ��ϴ�."),
	Game_Deaeth_AbilityReveal("����.���.�ɷ°���", true,
			"# ���� ���� �� ��� �� �ɷ� ���� ����",
			"# true�� �����ϸ� ������ ���۵ǰ� �� �� ����� ��� �÷��̾��� �ɷ��� �����մϴ�."),
	Game_Deaeth_AbilityRemoval("����.���.�ɷ»���", false,
			"# ���� ���� �� ��� �� �ɷ� ���� ����",
			"# true�� �����ϸ� ������ ���۵ǰ� �� �� ����� ��� �÷��̾��� �ɷ��� �����մϴ�."),
	Game_Deaeth_ItemDrop("����.���.�����۵��", true,
			"# ���� ���� �� ��� �� ������ ��� ����",
			"# true�� �����ϸ� ������ ���۵ǰ� �� �� ����� ��� �������� ����մϴ�."),
	Game_ClearWeather("����.��������", false,
			"# ���� ���� ���� ����",
			"# true�� �����ϸ� ������ ����Ǵ� ���� ���� ������ �����˴ϴ�."),
	Game_Spawn_Location("����.����.��ġ", Bukkit.getWorlds().get(0).getSpawnLocation(),
			"# ���� ��ġ ����"),
	Game_Spawn_Enable("����.����.�̵�", true,
			"# �ʹ� ���� �̵� Ȱ��ȭ ����"),
	Game_VisualEffect("����.�ð�ȿ��", true,
			"# ��ƼŬ Ȱ��ȭ ����"),
	Game_BlackList("����.������Ʈ", Messager.getStringList(),
			"# �ɷ��� ��÷�� �� ������� ���� �ɷ��� �����մϴ�."),
	AbilityChangeGame_Period("ü�����ɷ�����.�ֱ�", 20,
			"# �ɷ� ���� �ֱ� (����: ��)"),
	GameMode("���Ӹ��", DefaultGame.class.getName(),
			"# ���� ��� Ŭ����");
	
	private String Path;
	private Object Default;
	private String[] Comments;
	
	private ConfigNodes(String Path, Object Default, String... Comments) {
		
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
