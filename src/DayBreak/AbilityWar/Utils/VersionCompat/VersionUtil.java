package DayBreak.AbilityWar.Utils.VersionCompat;

import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class VersionUtil {
	
	private VersionUtil() {}
	
	/**
	 * �÷��̾��� �տ� �ִ� �������� ��ȯ�մϴ�.
	 * @param p	�տ� �ִ� �������� Ȯ���� �÷��̾�
	 * @return	�տ� �ִ� ������
	 */
	@SuppressWarnings("deprecation")
	public static ItemStack getItemInHand(Player p) {
		if(ServerVersion.getVersion() >= 9) {
			return p.getInventory().getItemInMainHand();
		} else {
			return p.getInventory().getItemInHand();
		}
	}
	
	/**
	 * �÷��̾��� �ִ� ü���� ��ȯ�մϴ�.
	 * @param p	�ִ� ü���� Ȯ���� �÷��̾�
	 * @return	�ִ� ü��
	 */
	@SuppressWarnings("deprecation")
	public static double getMaxHealth(Player p) {
		if(ServerVersion.getVersion() >= 9) {
			return p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
		} else {
			return p.getMaxHealth();
		}
	}
	
	@SuppressWarnings("deprecation")
	public static double getMaxHealth(Damageable d) {
		if(ServerVersion.getVersion() >= 11) {
			if(d instanceof Attributable) {
				return ((Attributable) d).getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
			} else {
				return 1;
			}
		} else {
			return d.getMaxHealth();
		}
	}
	
}
