package Marlang.AbilityWar.Ability.List;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Ability.Timer.CooldownTimer;
import Marlang.AbilityWar.Config.AbilitySettings.SettingObject;
import Marlang.AbilityWar.Utils.Messager;
import Marlang.AbilityWar.Utils.Math.LocationUtil;

public class Imprison extends AbilityBase {

	public static SettingObject<Integer> CooldownConfig = new SettingObject<Integer>("����", "Cooldown", 25, 
			"# ��Ÿ��") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 0;
		}
		
	};
	
	public Imprison(Player player) {
		super(player, "����", Rank.B,
				ChatColor.translateAlternateColorCodes('&', "&f������ ö���� Ÿ���ϸ� ����� ������ �ӿ� ���Ӵϴ�. " + Messager.formatCooldown(CooldownConfig.getValue())));
	}

	CooldownTimer Cool = new CooldownTimer(this, CooldownConfig.getValue());

	@Override
	public boolean ActiveSkill(ActiveMaterialType mt, ActiveClickType ct) {
		if(mt.equals(ActiveMaterialType.Iron_Ingot)) {
			if(ct.equals(ActiveClickType.LeftClick)) {
				Cool.isCooldown();
			}
		}
		
		return false;
	}

	@Override
	public void PassiveSkill(Event event) {
		if(event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
			if(e.getDamager().equals(getPlayer())) {
				if(e.getEntity() instanceof Player) {
					if(!e.isCancelled()) {
						if(getPlayer().getInventory().getItemInMainHand().getType().equals(Material.IRON_INGOT)) {
							if(!Cool.isCooldown()) {
								List<Block> blocks = LocationUtil.getBlocks(e.getEntity().getLocation(), 3, true, false, true);
								for(Block b : blocks) {
									b.setType(Material.GLASS);
								}
								
								Cool.StartTimer();
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void onRestrictClear() {}

}
