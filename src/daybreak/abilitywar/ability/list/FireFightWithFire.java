package daybreak.abilitywar.ability.list;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import daybreak.abilitywar.ability.AbilityBase;
import daybreak.abilitywar.ability.AbilityManifest;
import daybreak.abilitywar.ability.SubscribeEvent;
import daybreak.abilitywar.ability.AbilityManifest.Rank;
import daybreak.abilitywar.ability.AbilityManifest.Species;
import daybreak.abilitywar.config.AbilitySettings.SettingObject;
import daybreak.abilitywar.game.games.mode.AbstractGame.Participant;
import daybreak.abilitywar.utils.versioncompat.VersionUtil;

@AbilityManifest(Name = "이열치열", Rank = Rank.B, Species = Species.HUMAN)
public class FireFightWithFire extends AbilityBase {

	public static SettingObject<Integer> ChanceConfig = new SettingObject<Integer>(FireFightWithFire.class, "Chance", 50,
			"# 공격을 받았을 시 몇 퍼센트 확률로 회복을 할지 설정합니다.",
			"# 50은 50%를 의미합니다.") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 1 && value <= 100;
		}
		
	};
	
	public FireFightWithFire(Participant participant) {
		super(participant,
				ChatColor.translateAlternateColorCodes('&', "&f불 데미지를 받을 때, " + ChanceConfig.getValue() + "% 확률로 체력을 회복합니다."));
	}

	@Override
	public boolean ActiveSkill(MaterialType mt, ClickType ct) {
		return false;
	}

	@SubscribeEvent
	public void onEntityDamage(EntityDamageEvent e) {
		if(e.getEntity().equals(getPlayer())) {
			if(e.getCause().equals(DamageCause.FIRE) || e.getCause().equals(DamageCause.FIRE_TICK) || e.getCause().equals(DamageCause.LAVA)) {
				Random r = new Random();
				if(r.nextInt(100) <= ChanceConfig.getValue() - 1) {
					Double damage = e.getDamage();
					e.setDamage(0);
					
					Double health = getPlayer().getHealth() + damage;
					
					if(health > VersionUtil.getMaxHealth(getPlayer())) health = VersionUtil.getMaxHealth(getPlayer());
					
					if(!getPlayer().isDead()) {
						getPlayer().setHealth(health);
					}
				}
			}
		}
	}
	
	@Override
	public void onRestrictClear() {}

	@Override
	public void TargetSkill(MaterialType mt, LivingEntity entity) {}
	
}
