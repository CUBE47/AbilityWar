package DayBreak.AbilityWar.Ability.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import DayBreak.AbilityWar.Ability.AbilityBase;
import DayBreak.AbilityWar.Ability.AbilityManifest;
import DayBreak.AbilityWar.Ability.AbilityManifest.Rank;
import DayBreak.AbilityWar.Ability.AbilityManifest.Species;
import DayBreak.AbilityWar.Ability.SubscribeEvent;
import DayBreak.AbilityWar.Config.AbilitySettings.SettingObject;
import DayBreak.AbilityWar.Game.Games.Mode.AbstractGame.Participant;

@AbilityManifest(Name = "�̶�", Rank = Rank.S, Species = Species.HUMAN)
public class Ira extends AbilityBase {

	public static SettingObject<Integer> AttackConfig = new SettingObject<Integer>(Ira.class, "AttackTime", 4,
			"# ��� ������ ���ϸ� ������ ����ų�� �����մϴ�.",
			"# �⺻��: 4") {
		
		@Override
		public boolean Condition(Integer value) {
			return value > 1;
		}
		
	};
	
	public Ira(Participant participant) {
		super(participant,
				ChatColor.translateAlternateColorCodes('&', "&f" + AttackConfig.getValue() + "�� ������ ���� ������ ������ ��ġ�� ������ ����ŵ�ϴ�."),
				ChatColor.translateAlternateColorCodes('&', "&f�ڱ� �ڽŵ� ���� �������� �Խ��ϴ�."));
	}

	@Override
	public boolean ActiveSkill(MaterialType mt, ClickType ct) {
		return false;
	}

	private int ExplodeCount = 0;
	
	@SubscribeEvent
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
		if(!e.isCancelled() && e.getEntity().equals(getPlayer())) {
			if(ExplodeCount >= AttackConfig.getValue() - 1) {
				ExplodeCount = 0;
				
				Entity Damager = e.getDamager();
				
				if(Damager instanceof Projectile) {
					if(((Projectile) Damager).getShooter() instanceof LivingEntity) {
						LivingEntity entity = (LivingEntity) ((Projectile) Damager).getShooter();
						getPlayer().getWorld().createExplosion(entity.getLocation(), 2, false);
						if(entity.getVelocity().getY() > 0) {
							entity.setVelocity(entity.getVelocity().setY(0));
						}
					}
				} else {
					getPlayer().getWorld().createExplosion(Damager.getLocation(), 2, false);
					if(Damager.getVelocity().getY() > 0) {
						Damager.setVelocity(Damager.getVelocity().setY(0));
					}
				}
			} else {
				ExplodeCount++;
			}
		}
	}
	
	@Override
	public void onRestrictClear() {}

	@Override
	public void TargetSkill(MaterialType mt, LivingEntity entity) {}
	
}
