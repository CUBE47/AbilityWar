package DayBreak.AbilityWar.Config.Enums;

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
	Game_Death_Operation("����.���.�۾�", "Ż��",
			"# ���� ���� �� �÷��̾� ��� �� ������ �۾��� �����մϴ�.",
			"# Ż��		: �÷��̾ Ż����ŵ�ϴ�.",
			"# �������	: �÷��̾ ���� ���� ��ȯ�մϴ�.",
			"# ����		: �ƹ� �۾��� ���� �ʽ��ϴ�."),
	Game_Death_AbilityReveal("����.���.�ɷ°���", true,
			"# ���� ���� �� ��� �� �ɷ� ���� ����",
			"# true�� �����ϸ� ������ ���۵ǰ� �� �� ����� ��� �÷��̾��� �ɷ��� �����մϴ�."),
	Game_Death_AbilityRemoval("����.���.�ɷ»���", false,
			"# ���� ���� �� ��� �� �ɷ� ���� ����",
			"# true�� �����ϸ� ������ ���۵ǰ� �� �� ����� ��� �÷��̾��� �ɷ��� �����մϴ�."),
	Game_Death_ItemDrop("����.���.�����۵��", true,
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
	Game_WRECK("����.WRECK", false,
			"# true�� �����ϸ�",
			"# W onderful",
			"# R ollicking",
			"# E xciting",
			"# C hizzy",
			"# K icking",
			"# ��带 Ȱ��ȭ�մϴ�.",
			"# ��� �ɷ��� ��Ÿ�� 90% ����"),
	AbilityChangeGame_Period("ü�����ɷ�����.�ֱ�", 20,
			"# �ɷ� ���� �ֱ� (����: ��)"),
	AbilityChangeGame_Life("ü�����ɷ�����.����", 3,
			"# �׾��� �� �ٽ� �¾ �� �ִ� Ƚ��"),
	AbilityChangeGame_Eliminate("ü�����ɷ�����.Ż��", true,
			"# ������ ������ ��� Ż�� ����",
			"# Ż���� ������ ������ ���� ������ ������ ������ �� �����ϴ�.",
			"# ������ ������ ������ ���� ��� �̸� �����ϰ� ������ �� �ֽ��ϴ�."),
	SummerVacation_Kill("�ų��¿����ް�.ųȽ��", 10,
			"# ����ϱ� ���� �ʿ��� ų Ƚ��"),
	GameMode("���Ӹ��", DefaultGame.class.getName(),
			"# ���� ��� Ŭ����");
	
	private final String Path;
	private final Object Default;
	private final String[] Comments;
	
	private ConfigNodes(final String Path, final Object Default, final String... Comments) {
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
