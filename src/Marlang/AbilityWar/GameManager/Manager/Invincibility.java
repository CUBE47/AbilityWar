package Marlang.AbilityWar.GameManager.Manager;

import org.bukkit.ChatColor;

import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Config.AbilityWarSettings;
import Marlang.AbilityWar.GameManager.Game.Game;
import Marlang.AbilityWar.Utils.Messager;
import Marlang.AbilityWar.Utils.NumberUtil;
import Marlang.AbilityWar.Utils.PacketUtil.TitleObject;
import Marlang.AbilityWar.Utils.TimerBase;
import Marlang.AbilityWar.Utils.Library.SoundLib;

/**
 * �ʹ� ����
 * @author _Marlang ����
 */
public class Invincibility extends TimerBase {
	
	private Integer Duration = AbilityWarSettings.getInvincibilityDuration();
	private Game game;
	
	public Invincibility(Game game) {
		super(AbilityWarSettings.getInvincibilityDuration() * 60);
		this.game = game;
	}
	
	@Override
	public void TimerStart(Data<?>... args) {
		Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&a�ʹ� ������ &f" + NumberUtil.parseTimeString(Duration * 60) + "&a���� ����˴ϴ�."));
	}
	
	@Override
	public void TimerProcess(Integer Seconds) {
		if(Seconds == (Duration * 60) / 2) {
			Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&a�ʹ� ������ &f" + NumberUtil.parseTimeString(Seconds) + " &a�Ŀ� �����˴ϴ�."));
		}
		

		if(Seconds <= 5 && Seconds >= 1) {
			Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&a�ʹ� ������ &f" + NumberUtil.parseTimeString(Seconds) + " &a�Ŀ� �����˴ϴ�."));
			SoundLib.BLOCK_NOTE_HARP.broadcastSound();
		}
	}
	
	@Override
	public void TimerEnd() {
		game.setRestricted(false);
		Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&a�ʹ� ������ �����Ǿ����ϴ�."));
		SoundLib.ENTITY_ENDERDRAGON_AMBIENT.broadcastSound();
		
		TitleObject title = new TitleObject(ChatColor.translateAlternateColorCodes('&', "&c&lWarning"),
				ChatColor.translateAlternateColorCodes('&', "&f�ʹ� ������ �����Ǿ����ϴ�."));
		title.Broadcast(20, 60, 20);
		
		for(AbilityBase Ability : game.getAbilities().values()) {
			Ability.setRestricted(false);
		}
	}
	
}
