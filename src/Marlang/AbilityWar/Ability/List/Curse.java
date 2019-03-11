package Marlang.AbilityWar.Ability.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Ability.AbilityManifest;
import Marlang.AbilityWar.Ability.AbilityManifest.Rank;
import Marlang.AbilityWar.Config.AbilitySettings.SettingObject;
import Marlang.AbilityWar.GameManager.Game.AbstractGame.Participant;
import Marlang.AbilityWar.Utils.Messager;
import Marlang.AbilityWar.Utils.Library.SoundLib;
import Marlang.AbilityWar.Utils.Library.Item.EnchantLib;

@AbilityManifest(Name = "�ý�", Rank = Rank.B)
public class Curse extends AbilityBase {

	public static SettingObject<Integer> CountConfig = new SettingObject<Integer>(Curse.class, "Count", 1,
			"# �ɷ� ���Ƚ��") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 1;
		}
		
	};

	public Curse(Participant participant) {
		super(participant,
				ChatColor.translateAlternateColorCodes('&', "&f������ ö���� Ÿ���ϸ� ������ �����ϰ� �ִ� ��� ���ʿ� �ͼ����ָ� �̴ϴ�."),
				ChatColor.translateAlternateColorCodes('&', "&f" + CountConfig.getValue() + "���� ����� �� �ֽ��ϴ�."));
	}
	
	private Integer Count = CountConfig.getValue();

	@Override
	public boolean ActiveSkill(MaterialType mt, ClickType ct) {
		return false;
	}

	@Override
	public void PassiveSkill(Event event) {}

	@Override
	public void onRestrictClear() {}

	@Override
	public void TargetSkill(MaterialType mt, Entity entity) {
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
					Messager.sendMessage(getPlayer(), ChatColor.translateAlternateColorCodes('&', "&c�� �̻� �� �ɷ��� ����� �� �����ϴ�!"));
				}
			}
		}
	}
	
}
