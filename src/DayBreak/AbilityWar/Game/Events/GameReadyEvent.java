package DayBreak.AbilityWar.Game.Events;

import DayBreak.AbilityWar.Game.Games.Mode.AbstractGame;
import DayBreak.AbilityWar.Utils.Thread.TimerBase;

/**
 * {@link AbstractGame} {@link TimerBase}�� ����� �� ȣ��Ǵ� �̺�Ʈ�Դϴ�.
 */
public class GameReadyEvent extends GameEvent {

	public GameReadyEvent(AbstractGame game) {
		super(game);
	}

}
