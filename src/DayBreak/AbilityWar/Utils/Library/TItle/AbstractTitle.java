package DayBreak.AbilityWar.Utils.Library.TItle;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * �߻� Ÿ��Ʋ
 * @author DayBreak ����
 */
abstract public class AbstractTitle {
	
	public abstract void sendTo(Player p);
	
	public void Broadcast() {
		for(Player p : Bukkit.getOnlinePlayers()) sendTo(p);
	}
	
}
