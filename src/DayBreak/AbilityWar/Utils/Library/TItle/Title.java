package DayBreak.AbilityWar.Utils.Library.TItle;

import org.bukkit.entity.Player;

/**
 * ���� �޽���
 * @author DayBreak ����
 */
public class Title extends AbstractTitle {
	
	private String Title;
	private String SubTitle;
	private int fadeIn;
	private int stay;
	private int fadeOut;

	/**
	 * ���� �޽���
	 * @param Title 	����
	 * @param SubTitle 	������
	 * @param fadeIn 	FadeIn �ð� (ƽ ����)
	 * @param stay 		Stay �ð� (ƽ ����)
	 * @param fadeOut 	FadeOut �ð� (ƽ ����)
	 */
	public Title(String Title, String SubTitle, int fadeIn, int stay, int fadeOut) {
		this.Title = Title;
		this.SubTitle = SubTitle;
		this.fadeIn = fadeIn;
		this.stay = stay;
		this.fadeOut = fadeOut;
	}

	public void sendTo(Player p) {
		p.sendTitle(Title, SubTitle, fadeIn, stay, fadeOut);
	}
	
}
