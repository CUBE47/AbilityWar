package DayBreak.AbilityWar.Ability.List;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;

import DayBreak.AbilityWar.Ability.AbilityBase;
import DayBreak.AbilityWar.Ability.AbilityManifest;
import DayBreak.AbilityWar.Ability.AbilityManifest.Rank;
import DayBreak.AbilityWar.Ability.AbilityManifest.Species;
import DayBreak.AbilityWar.Ability.SubscribeEvent;
import DayBreak.AbilityWar.Config.AbilitySettings.SettingObject;
import DayBreak.AbilityWar.Game.Games.Mode.AbstractGame.Participant;
import DayBreak.AbilityWar.Utils.Library.SoundLib;

@AbilityManifest(Name = "����ȭ��", Rank = Rank.S, Species = Species.HUMAN)
public class BombArrow extends AbilityBase {

	public static SettingObject<Integer> ChanceConfig = new SettingObject<Integer>(BombArrow.class, "Chance", 50,
			"# ȭ���� ������ �� �� �ۼ�Ʈ Ȯ���� ������ ����ų�� �����մϴ�.",
			"# 50�� 50%�� �ǹ��մϴ�.") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 1 && value <= 100;
		}
		
	};

	public static SettingObject<Integer> SizeConfig = new SettingObject<Integer>(BombArrow.class, "Size", 2,
			"# ȭ���� ������ �� �󸶳� ū ������ ����ų�� �����մϴ�.") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 1;
		}
		
	};
	
	public BombArrow(Participant participant) {
		super(participant,
				ChatColor.translateAlternateColorCodes('&', "&fȭ���� ������ �� " + ChanceConfig.getValue() + "% Ȯ���� ������ ����ŵ�ϴ�."));
	}

	@Override
	public boolean ActiveSkill(MaterialType mt, ClickType ct) {
		return false;
	}
	
	private final int Chance = ChanceConfig.getValue();
	private final int Size = SizeConfig.getValue();
	
	private ArrayList<Arrow> ArrowList = new ArrayList<>();
	
	@SubscribeEvent
	public void onProjectileLaunch(ProjectileLaunchEvent e) {
		if(e.getEntity().getShooter().equals(getPlayer())) {
			if(e.getEntity() instanceof Arrow) {
				ArrowList.add((Arrow) e.getEntity());
			}
		}
	}
	
	@SubscribeEvent
	public void onProjectileHit(ProjectileHitEvent e) {
		if(e.getEntity().getShooter().equals(getPlayer())) {
			if(e.getEntity() instanceof Arrow) {
				Arrow arrow = (Arrow) e.getEntity();

				if(ArrowList.contains(arrow)) {
					ArrowList.remove(arrow);
					Random r = new Random();
					
					if((r.nextInt(100) + 1) <= Chance) {
						SoundLib.BLOCK_NOTE_BLOCK_BELL.playSound(getPlayer());
						Location l = arrow.getLocation();
						l.getWorld().createExplosion(l, Size, false);
					}
				}
			}
		}
	}
	
	@Override
	public void onRestrictClear() {}

	@Override
	public void TargetSkill(MaterialType mt, Entity entity) {}
	
}
