package Marlang.AbilityWar.Game.Script.Objects;

import java.io.Serializable;

import org.bukkit.ChatColor;

import Marlang.AbilityWar.Game.Games.AbstractGame;
import Marlang.AbilityWar.Utils.Messager;
import Marlang.AbilityWar.Utils.Thread.TimerBase;

abstract public class AbstractScript implements Serializable {
	
	private static final long serialVersionUID = 7230527266220521425L;
	
	private final String ScriptName;
	private final int Time;
	private final boolean Loop;
	private final int LoopCount;
	private final String PreRunMessage;
	private final String RunMessage;
	private transient TimerBase Timer;
	
	public AbstractScript(String ScriptName, int Time, boolean Loop, int LoopCount, String PreRunMessage, String RunMessage) {
		this.ScriptName = ScriptName;
		this.Time = Time;
		this.Loop = Loop;
		this.LoopCount = LoopCount;
		this.PreRunMessage = PreRunMessage;
		this.RunMessage = RunMessage;
		this.Timer = newTimer();
	}
	
	private transient AbstractGame game;
	
	public void Start(AbstractGame game) {
		this.game = game;
		
		if(Timer != null) {
			Timer.StartTimer();
		} else {
			Timer = newTimer();
			Timer.StartTimer();
		}
	}
	
	private TimerBase newTimer() {
		return new TimerBase(Time) {
			
			//loopCount�� 0�� �Ǹ� ���� ����
			//loopCount�� 0���� ���� ��� ���ѷ���
			//Loop�� true�� ��쿡�� �۵�
			int loopCount = LoopCount;
			
			@Override
			public void onStart() {}
			
			@Override
			public void TimerProcess(Integer Seconds) {
				if(Seconds == (this.getCount() / 2)) {
					Messager.broadcastMessage(getPreRunMessage(Seconds));
				} else if(Seconds <= 5 && Seconds >= 1) {
					Messager.broadcastMessage(getPreRunMessage(Seconds));
				}
			}
			
			@Override
			public void onEnd() {
				Execute(game);
				Messager.broadcastMessage(getRunMessage());
				if(Loop) {
					if(loopCount > -1) {
						if(loopCount > 1) {
		 					this.StartTimer();
		 					loopCount--;
						}
					} else {
	 					this.StartTimer();
					}
				}
			}
			
		};
	}
	
	public String getScriptName() {
		return ScriptName;
	}
	
	protected boolean isLoop() {
		return Loop;
	}
	
	protected TimerBase getTimer() {
		return Timer;
	}
	
	private String getPreRunMessage(Integer Time) {
		return ChatColor.translateAlternateColorCodes('&', PreRunMessage.replaceAll("%Time%", Time.toString()).replaceAll("%ScriptName%", this.getScriptName()));
	}

	private String getRunMessage() {
		return ChatColor.translateAlternateColorCodes('&', RunMessage.replaceAll("%ScriptName%", this.getScriptName()));
	}

	abstract protected void Execute(AbstractGame game);
	
}
