package DayBreak.AbilityWar.Ability.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import DayBreak.AbilityWar.Ability.AbilityBase;
import DayBreak.AbilityWar.Ability.AbilityManifest;
import DayBreak.AbilityWar.Ability.AbilityManifest.Rank;
import DayBreak.AbilityWar.Ability.AbilityManifest.Species;
import DayBreak.AbilityWar.Config.AbilitySettings.SettingObject;
import DayBreak.AbilityWar.Game.Games.Mode.AbstractGame.Participant;
import DayBreak.AbilityWar.Utils.Messager;
import DayBreak.AbilityWar.Utils.Library.SoundLib;
import DayBreak.AbilityWar.Utils.Library.Item.EnchantLib;

@AbilityManifest(Name = "컬스", Rank = Rank.C, Species = Species.HUMAN)
public class Curse extends AbilityBase {

	public static SettingObject<Integer> CountConfig = new SettingObject<Integer>(Curse.class, "Count", 3,
			"# 능력 사용횟수") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 1;
		}
		
	};

	public Curse(Participant participant) {
		super(participant,
				ChatColor.translateAlternateColorCodes('&', "&f상대방을 철괴로 우클릭하면 상대방이 착용하고 있는 모든 갑옷에 귀속저주를 겁니다."),
				ChatColor.translateAlternateColorCodes('&', "&f" + CountConfig.getValue() + "번만 사용할 수 있습니다."));
	}
	
	private int Count = CountConfig.getValue();

	@Override
	public boolean ActiveSkill(MaterialType mt, ClickType ct) {
		return false;
	}

	@Override
	public void onRestrictClear() {}

	@Override
	public void TargetSkill(MaterialType mt, LivingEntity entity) {
		if(mt.equals(MaterialType.Iron_Ingot)) {
			if(entity != null) {
				if(entity instanceof Player) {
					Player p = (Player) entity;
					if(Count > 0) {
						ItemStack Helmet = p.getInventory().getHelmet();
						if(Helmet != null) {
							p.getInventory().setHelmet(EnchantLib.BINDING_CURSE.addEnchantment(Helmet, 1));
						}

						ItemStack Chestplate = p.getInventory().getChestplate();
						if(Chestplate != null) {
							p.getInventory().setChestplate(EnchantLib.BINDING_CURSE.addEnchantment(Chestplate, 1));
						}
						
						ItemStack Leggings = p.getInventory().getLeggings();
						if(Leggings != null) {
							p.getInventory().setLeggings(EnchantLib.BINDING_CURSE.addEnchantment(Leggings, 1));
						}

						ItemStack Boots = p.getInventory().getBoots();
						if(Boots != null) {
							p.getInventory().setBoots(EnchantLib.BINDING_CURSE.addEnchantment(Boots, 1));
						}
						
						SoundLib.ENTITY_ELDER_GUARDIAN_CURSE.playSound(p);
						
						Count--;
					}
				}
			} else {
				if(Count <= 0) {
					Messager.sendMessage(getPlayer(), ChatColor.translateAlternateColorCodes('&', "&c더 이상 이 능력을 사용할 수 없습니다!"));
				}
			}
		}
	}
	
}
