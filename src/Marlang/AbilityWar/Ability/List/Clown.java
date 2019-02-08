package Marlang.AbilityWar.Ability.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.Event;

import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Ability.Timer.CooldownTimer;
import Marlang.AbilityWar.Config.AbilitySettings.SettingObject;
import Marlang.AbilityWar.Config.AbilityWarSettings;
import Marlang.AbilityWar.Utils.Messager;

public class Clown extends AbilityBase {

	public static SettingObject<Integer> CooldownConfig = new SettingObject<Integer>("����", "Cooldown", 3, 
			"# ��Ÿ��") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 0;
		}
		
	};

	public Clown() {
		super("����", Rank.D,
				ChatColor.translateAlternateColorCodes('&', "&fö���� ��Ŭ���ϸ� �������� �̵��մϴ�. " + Messager.formatCooldown(CooldownConfig.getValue())));
	
		registerTimer(Cool);
	}

	CooldownTimer Cool = new CooldownTimer(this, CooldownConfig.getValue());
	
	@Override
	public boolean ActiveSkill(ActiveMaterialType mt, ActiveClickType ct) {
		if(mt.equals(ActiveMaterialType.Iron_Ingot)) {
			if(ct.equals(ActiveClickType.RightClick)) {
				if(!Cool.isCooldown()) {
					Location Spawn = AbilityWarSettings.getSpawnLocation();
					
					getPlayer().teleport(Spawn);
					
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
	public void AbilityEvent(EventType type) {}

}
