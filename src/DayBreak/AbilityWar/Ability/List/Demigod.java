package DayBreak.AbilityWar.Ability.List;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import DayBreak.AbilityWar.Ability.AbilityBase;
import DayBreak.AbilityWar.Ability.AbilityManifest;
import DayBreak.AbilityWar.Ability.AbilityManifest.Rank;
import DayBreak.AbilityWar.Ability.AbilityManifest.Species;
import DayBreak.AbilityWar.Ability.SubscribeEvent;
import DayBreak.AbilityWar.Config.AbilitySettings.SettingObject;
import DayBreak.AbilityWar.Game.Games.Mode.AbstractGame.Participant;
import DayBreak.AbilityWar.Utils.Library.EffectLib;

@AbilityManifest(Name = "데미갓", Rank = Rank.S, Species = Species.DEMIGOD)
public class Demigod extends AbilityBase {
	
	public static SettingObject<Integer> ChanceConfig = new SettingObject<Integer>(Demigod.class, "Chance", 40,
			"# 공격을 받았을 시 몇 퍼센트 확률로 랜덤 버프를 받을지 설정합니다.",
			"# 40은 40%를 의미합니다.") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 1 && value <= 100;
		}
		
	};
	
	public Demigod(Participant participant) {
		super(participant,
				ChatColor.translateAlternateColorCodes('&', "&f반신반인의 능력자입니다. 공격을 받으면"),
				ChatColor.translateAlternateColorCodes('&', "&f" + ChanceConfig.getValue() + "% 확률로 5초간 랜덤 버프가 발동됩니다."));
	}
	
	@Override
	public boolean ActiveSkill(MaterialType mt, ClickType ct) {
		return false;
	}
	
	private final int Chance = ChanceConfig.getValue();
	
	@SubscribeEvent
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
		if(e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if(p.equals(getPlayer())) {
				if(!e.isCancelled()) {
					Random r = new Random();
					
					if((r.nextInt(100) + 1) <= Chance) {
						Integer Buff = r.nextInt(3);
						if(Buff.equals(0)) {
							EffectLib.ABSORPTION.addPotionEffect(p, 100, 1, true);
						} else if(Buff.equals(1)) {
							EffectLib.REGENERATION.addPotionEffect(p, 100, 0, true);
						} else if(Buff.equals(2)) {
							EffectLib.DAMAGE_RESISTANCE.addPotionEffect(p, 100, 1, true);
						}
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
