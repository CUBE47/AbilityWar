package DayBreak.AbilityWar.Game.Manager;

import org.bukkit.ChatColor;

import DayBreak.AbilityWar.Ability.AbilityBase;
import DayBreak.AbilityWar.Config.AbilityWarSettings;
import DayBreak.AbilityWar.Game.Games.Mode.AbstractGame;
import DayBreak.AbilityWar.Game.Games.Mode.AbstractGame.Participant;
import DayBreak.AbilityWar.Utils.Messager;
import DayBreak.AbilityWar.Utils.Library.SoundLib;
import DayBreak.AbilityWar.Utils.Library.Packet.TitlePacket;
import DayBreak.AbilityWar.Utils.Math.NumberUtil;
import DayBreak.AbilityWar.Utils.Thread.TimerBase;

/**
 * ����
 * @author DayBreak ����
 */
public class Invincibility {
	
	private final Integer Duration = AbilityWarSettings.getInvincibilityDuration();
	private final AbstractGame game;
	
	public Invincibility(AbstractGame game) {
		this.game = game;
	}
	
	private TimerBase InvincibilityTimer;
	
	public boolean Start(boolean Infinite) {
		if(this.InvincibilityTimer == null || !this.InvincibilityTimer.isTimerRunning()) {
			if(!Infinite) {
				this.InvincibilityTimer = new TimerBase(Duration * 60) {

					@Override
					protected void onStart() {
						game.setRestricted(true);
						for(Participant participant : game.getParticipants()) {
							if(participant.hasAbility()) {
								AbilityBase ability = participant.getAbility();
								ability.setRestricted(true);
							}
						}
						
						Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&a������ &f" + NumberUtil.parseTimeString(Duration * 60) + "&a���� ����˴ϴ�."));
					}
					
					@Override
					protected void TimerProcess(Integer Seconds) {
						if(Seconds == (Duration * 60) / 2) {
							Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&a������ &f" + NumberUtil.parseTimeString(Seconds) + " &a�Ŀ� �����˴ϴ�."));
						}
						

						if(Seconds <= 5 && Seconds >= 1) {
							Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&a������ &f" + NumberUtil.parseTimeString(Seconds) + " &a�Ŀ� �����˴ϴ�."));
							SoundLib.BLOCK_NOTE_BLOCK_HARP.broadcastSound();
						}
					}
					
					@Override
					protected void onEnd() {
						game.setRestricted(false);
						TitlePacket titlePacket = new TitlePacket(ChatColor.translateAlternateColorCodes('&', "&c&lWarning"),
								ChatColor.translateAlternateColorCodes('&', "&f������ �����Ǿ����ϴ�."), 20, 60, 20);
						titlePacket.Broadcast();
						SoundLib.ENTITY_ENDER_DRAGON_AMBIENT.broadcastSound();
						
						for(Participant participant : game.getParticipants()) {
							if(participant.hasAbility()) {
								participant.getAbility().setRestricted(false);
							}
						}
					}
					
				};
			} else {
				this.InvincibilityTimer = new TimerBase() {

					@Override
					protected void onStart() {
						game.setRestricted(true);
						for(Participant participant : game.getParticipants()) {
							if(participant.hasAbility()) {
								AbilityBase ability = participant.getAbility();
								ability.setRestricted(true);
							}
						}
						Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&a������ ����Ǿ����ϴ�. ���ݺ��� ������ ������ ������ �������� ���� �ʽ��ϴ�."));
					}
					
					@Override
					protected void TimerProcess(Integer Seconds) {}
					
					@Override
					protected void onEnd() {
						game.setRestricted(false);
						TitlePacket titlePacket = new TitlePacket(ChatColor.translateAlternateColorCodes('&', "&c&lWarning"),
								ChatColor.translateAlternateColorCodes('&', "&f������ �����Ǿ����ϴ�."), 20, 60, 20);
						titlePacket.Broadcast();
						SoundLib.ENTITY_ENDER_DRAGON_AMBIENT.broadcastSound();
						
						for(Participant participant : game.getParticipants()) {
							if(participant.hasAbility()) {
								participant.getAbility().setRestricted(false);
							}
						}
					}
					
				};
			}
			
			this.InvincibilityTimer.StartTimer();
			return true;
		}
		
		return false;
	}
	
	public boolean Stop() {
		if(this.InvincibilityTimer != null && this.InvincibilityTimer.isTimerRunning()) {
			this.InvincibilityTimer.StopTimer(false);
			return true;
		}
		
		return false;
	}
	
	public boolean isInvincible() {
		return this.InvincibilityTimer != null && this.InvincibilityTimer.isTimerRunning();
	}
	
}
