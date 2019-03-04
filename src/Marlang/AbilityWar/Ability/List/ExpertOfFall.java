package Marlang.AbilityWar.Ability.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.GameManager.Object.Participant;
import Marlang.AbilityWar.Utils.Library.SoundLib;

public class ExpertOfFall extends AbilityBase {

	public ExpertOfFall(Participant participant) {
		super(participant, "������ ����", Rank.C,
				ChatColor.translateAlternateColorCodes('&', "&f������ ���� ����� �� �ڵ����� �������� �մϴ�."));
	}

	@Override
	public boolean ActiveSkill(MaterialType mt, ClickType ct) {
		return false;
	}

	@Override
	public void PassiveSkill(Event event) {
		if(event instanceof EntityDamageEvent) {
			EntityDamageEvent e = (EntityDamageEvent) event;
			if(e.getEntity().equals(getPlayer())) {
				if(e.getCause().equals(DamageCause.FALL)) {
					e.setCancelled(true);
					getPlayer().getLocation().getBlock().setType(Material.WATER);
					SoundLib.ENTITY_PLAYER_SPLASH.playSound(getPlayer());
				}
			}
		}
	}

	@Override
	public void onRestrictClear() {}

	@Override
	public void TargetSkill(MaterialType mt, Entity entity) {}
	
}
