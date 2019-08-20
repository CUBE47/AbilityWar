package DayBreak.AbilityWar.Ability.List;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;

import DayBreak.AbilityWar.Ability.AbilityBase;
import DayBreak.AbilityWar.Ability.AbilityManifest;
import DayBreak.AbilityWar.Ability.AbilityManifest.Rank;
import DayBreak.AbilityWar.Ability.AbilityManifest.Species;
import DayBreak.AbilityWar.Ability.Timer.CooldownTimer;
import DayBreak.AbilityWar.Config.AbilitySettings.SettingObject;
import DayBreak.AbilityWar.Game.Games.Mode.AbstractGame.Participant;
import DayBreak.AbilityWar.Utils.Messager;
import DayBreak.AbilityWar.Utils.Math.LocationUtil;

@AbilityManifest(Name = "����", Rank = Rank.B, Species = Species.HUMAN)
public class Imprison extends AbilityBase {

	public static SettingObject<Integer> CooldownConfig=new SettingObject<Integer>(Imprison.class,"Cooldown",25,"# ��Ÿ��"){

	@Override public boolean Condition(Integer value){return value>=0;}

	};

	public static SettingObject<Integer> SizeConfig=new SettingObject<Integer>(Imprison.class,"Size",3,"# ��ų ũ��"){

	@Override public boolean Condition(Integer value){return value>=0;}

	};

	public Imprison(Participant participant) {
		super(participant, ChatColor.translateAlternateColorCodes('&',
				"&f������ ö���� ��Ŭ���ϸ� ����� ������ �ӿ� ���Ӵϴ�. " + Messager.formatCooldown(CooldownConfig.getValue())));
	}

	private CooldownTimer Cool = new CooldownTimer(this, CooldownConfig.getValue());

	private final int size = SizeConfig.getValue();

	@Override
	public boolean ActiveSkill(MaterialType mt, ClickType ct) {
		return false;
	}

	@Override
	public void onRestrictClear() {}

	@Override
	public void TargetSkill(MaterialType mt, LivingEntity entity) {
		if (mt.equals(MaterialType.Iron_Ingot)) {
			if (entity != null) {
				if (!Cool.isCooldown()) {
					List<Block> blocks = LocationUtil.getBlocks(entity.getLocation(), size, true, false, true);
					for (Block b : blocks) {
						b.setType(Material.GLASS);
					}

					Cool.StartTimer();
				}
			} else {
				Cool.isCooldown();
			}
		}
	}

}
