package Marlang.AbilityWar.Ability.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.util.Vector;

import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Ability.Timer.CooldownTimer;
import Marlang.AbilityWar.Config.AbilitySettings.SettingObject;
import Marlang.AbilityWar.Utils.Messager;
import Marlang.AbilityWar.Utils.Thread.AbilityWarThread;

public class Celebrity extends AbilityBase {

	public static SettingObject<Integer> CooldownConfig = new SettingObject<Integer>("���� �λ�", "Cooldown", 60,
			"# ��Ÿ��") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 0;
		}
		
	};

	public Celebrity(Player player) {
		super(player, "���� �λ�", Rank.D,
				ChatColor.translateAlternateColorCodes('&', "&fö���� ��Ŭ���ϸ� ��� �÷��̾ �ڽ��� ������ �ٶ󺾴ϴ�. " + Messager.formatCooldown(CooldownConfig.getValue())));
	}
	
	CooldownTimer Cool = new CooldownTimer(this, CooldownConfig.getValue());
	
	@Override
	public boolean ActiveSkill(ActiveMaterialType mt, ActiveClickType ct) {
		if(mt.equals(ActiveMaterialType.Iron_Ingot)) {
			if(ct.equals(ActiveClickType.RightClick)) {
				if(!Cool.isCooldown()) {
					
					if(AbilityWarThread.isGameTaskRunning()) {
						Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f�ȳ��ϼ���~ &e" + getPlayer().getName() + "&f�̿���!"));
						for(Player p : AbilityWarThread.getGame().getParticipants()) {
							if(!p.equals(getPlayer())) {
								Vector vector = getPlayer().getLocation().toVector().subtract(p.getLocation().toVector());
								p.teleport(p.getLocation().setDirection(vector));
							}
						}
					}
					
					Cool.StartTimer();
					
					return true;
				}
			}
		}
		
		return false;
	}

	@Override
	public void PassiveSkill(Event event) {}

	@Override
	public void onRestrictClear() {
		
	}

}
