package DayBreak.AbilityWar.Ability.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerMoveEvent;

import DayBreak.AbilityWar.Ability.AbilityBase;
import DayBreak.AbilityWar.Ability.AbilityManifest;
import DayBreak.AbilityWar.Ability.AbilityManifest.Rank;
import DayBreak.AbilityWar.Game.Games.AbstractGame.Participant;

@AbilityManifest(Name = "�Ǹ��� ����", Rank = Rank.B)
public class DevilBoots extends AbilityBase {

	public DevilBoots(Participant participant) {
		super(participant,
				ChatColor.translateAlternateColorCodes('&', "&f�������� �ڸ��� ���� �ٽ��ϴ�. ȭ�� ���ظ� ���� �ʽ��ϴ�."));
	}
	
	@Override
	public boolean ActiveSkill(MaterialType mt, ClickType ct) {
		return false;
	}
	
	@Override
	public void PassiveSkill(Event event) {
		if(event instanceof PlayerMoveEvent) {
			PlayerMoveEvent e = (PlayerMoveEvent) event;
			if(e.getPlayer().equals(getPlayer())) {
				Location To = e.getTo();
				if(To.getBlock().getType().equals(Material.AIR)) {
					To.getBlock().setType(Material.FIRE);
				}
			}
		} else if(event instanceof EntityDamageEvent) {
			EntityDamageEvent e = (EntityDamageEvent) event;
			if(e.getEntity().equals(getPlayer())) {
				DamageCause cause = e.getCause();
				if(cause.equals(DamageCause.FIRE) || cause.equals(DamageCause.FIRE_TICK) || cause.equals(DamageCause.LAVA)) {
					e.setCancelled(true);
				}
			}
		}
	}
	
	@Override
	public void onRestrictClear() {}

	@Override
	public void TargetSkill(MaterialType mt, Entity entity) {}
	
}
