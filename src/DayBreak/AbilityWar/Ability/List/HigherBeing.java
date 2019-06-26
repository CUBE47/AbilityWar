package DayBreak.AbilityWar.Ability.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import DayBreak.AbilityWar.Ability.AbilityBase;
import DayBreak.AbilityWar.Ability.AbilityManifest;
import DayBreak.AbilityWar.Ability.AbilityManifest.Rank;
import DayBreak.AbilityWar.Config.AbilitySettings.SettingObject;
import DayBreak.AbilityWar.Game.Games.Mode.AbstractGame.Participant;
import DayBreak.AbilityWar.Utils.Library.ParticleLib;
import DayBreak.AbilityWar.Utils.Library.SoundLib;

@AbilityManifest(Name = "��������", Rank = Rank.B)
public class HigherBeing extends AbilityBase {

	public static SettingObject<Double> DamageConfig = new SettingObject<Double>(HigherBeing.class, "DamageMultiple", 2.0,
			"# ���� ���") {
		
		@Override
		public boolean Condition(Double value) {
			return value > 1;
		}
		
	};
	
	public HigherBeing(Participant participant) {
		super(participant,
				ChatColor.translateAlternateColorCodes('&', "&f�ڽź��� ���� ��ġ�� �ִ� ����ü�� ���� ���� �� ��"),
				ChatColor.translateAlternateColorCodes('&', "&f" + DamageConfig.getValue() + "�� �����ϰ� �����մϴ�."),
				ChatColor.translateAlternateColorCodes('&', "&f�ڽź��� ���� ��ġ�� �ִ� ����ü�� ���� �������� �������� ���� �� �����ϴ�."));
	}

	@Override
	public boolean ActiveSkill(MaterialType mt, ClickType ct) {
		return false;
	}

	Double Multiple = DamageConfig.getValue();
	
	@Override
	public void PassiveSkill(Event event) {
		if(event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
			if(e.getDamager().equals(getPlayer())) {
				if(e.getEntity().getLocation().getY() < getPlayer().getLocation().getY()) {
					e.setDamage(e.getDamage() * Multiple);
					SoundLib.ENTITY_EXPERIENCE_ORB_PICKUP.playSound(getPlayer());
					ParticleLib.LAVA.spawnParticle(e.getEntity().getLocation(), 5, 1, 1, 1);
				} else if(e.getEntity().getLocation().getY() != getPlayer().getLocation().getY()) {
					e.setCancelled(true);
					SoundLib.BLOCK_ANVIL_BREAK.playSound(getPlayer());
				}
			}
		}
	}

	@Override
	public void onRestrictClear() {}

	@Override
	public void TargetSkill(MaterialType mt, Entity entity) {}
	
}
