package DayBreak.AbilityWar.Ability.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;

import DayBreak.AbilityWar.Ability.AbilityBase;
import DayBreak.AbilityWar.Ability.AbilityManifest;
import DayBreak.AbilityWar.Ability.AbilityManifest.Rank;
import DayBreak.AbilityWar.Ability.AbilityManifest.Species;
import DayBreak.AbilityWar.Ability.Timer.CooldownTimer;
import DayBreak.AbilityWar.Config.AbilitySettings.SettingObject;
import DayBreak.AbilityWar.Game.Games.Mode.AbstractGame.Participant;
import DayBreak.AbilityWar.Utils.FallBlock;
import DayBreak.AbilityWar.Utils.Messager;
import DayBreak.AbilityWar.Utils.Math.LocationUtil;
import DayBreak.AbilityWar.Utils.VersionCompat.ServerVersion;

@AbilityManifest(Name = "ī���", Rank = Rank.A, Species = Species.GOD)
public class Khazhad extends AbilityBase {

	private static SettingObject<Integer> LeftCooldownConfig = new SettingObject<Integer>(Khazhad.class, "LeftCooldown", 4, "# ��Ŭ�� ��Ÿ��") {
		
		@Override
		public boolean Condition(Integer arg0) {
			return arg0 >= 0;
		}

	};

	private static SettingObject<Integer> RightCooldownConfig = new SettingObject<Integer>(Khazhad.class, "RightCooldown", 10, "# ��Ŭ�� ��Ÿ��") {
		
		@Override
		public boolean Condition(Integer arg0) {
			return arg0 >= 0;
		}

	};
	
	public Khazhad(Participant participant) {
		super(participant,
				ChatColor.translateAlternateColorCodes('&', "&fö���� ��Ŭ���ϸ� �ڽ��� ���� �ִ� �������� ������ �����ϴ�. " + Messager.formatCooldown(LeftCooldownConfig.getValue())),
				ChatColor.translateAlternateColorCodes('&', "&fö���� ��Ŭ���ϸ� �ڽ��� ���� �ִ� ��� 5ĭ �ֺ��� ���� ��� �󸳴ϴ�. "),
				ChatColor.translateAlternateColorCodes('&', Messager.formatCooldown(RightCooldownConfig.getValue())));
	}

	private CooldownTimer LeftCool = new CooldownTimer(this, LeftCooldownConfig.getValue(), "��Ŭ��");
	private CooldownTimer RightCool = new CooldownTimer(this, RightCooldownConfig.getValue(), "��Ŭ��").setActionbarNotice(false);
	
	@Override
	public boolean ActiveSkill(MaterialType mt, ClickType ct) {
		if(mt.equals(MaterialType.Iron_Ingot)) {
			if(ct.equals(ClickType.LeftClick)) {
				if(!LeftCool.isCooldown()) {
					FallBlock fall = new FallBlock(Material.PACKED_ICE, getPlayer().getEyeLocation(), getPlayer().getLocation().getDirection().multiply(1.7)) {
						
						@Override
						public void onChangeBlock(FallingBlock block) {
							for(int x = -2; x < 2; x++) {
								for(int y = -2; y < 2; y++) {
									for(int z = -2; z < 2; z++) {
										Location l = block.getLocation().clone().add(x, y, z);
										l.getBlock().setType(Material.PACKED_ICE);
									}
								}
							}
						}
						
					};
					
					fall.toggleGlowing(true).toggleSetBlock(true).Spawn();
					
					LeftCool.StartTimer();
					return true;
				}
			} else if(ct.equals(ClickType.RightClick)) {
				if(!RightCool.isCooldown()) {
					for(Block b : LocationUtil.getBlocks(getPlayer().getTargetBlock(null, 30).getLocation(), 5, false, false, false)) {
						if(b.getType().equals(Material.WATER) || (ServerVersion.getVersion() < 13 && b.getType().equals(Material.valueOf("STATIONARY_WATER")))) {
							b.setType(Material.PACKED_ICE);
						}
					}
					
					RightCool.StartTimer();
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void TargetSkill(MaterialType mt, LivingEntity entity) {}

	@Override
	protected void onRestrictClear() {}

}
