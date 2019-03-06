package Marlang.AbilityWar.Ability.List;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;

import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Ability.AbilityManifest;
import Marlang.AbilityWar.Ability.AbilityManifest.Rank;
import Marlang.AbilityWar.Config.AbilitySettings.SettingObject;
import Marlang.AbilityWar.GameManager.Object.Participant;
import Marlang.AbilityWar.Utils.Library.SoundLib;

@AbilityManifest(Name = "����ȭ��", Rank = Rank.S)
public class BombArrow extends AbilityBase {

	public static SettingObject<Integer> ChanceConfig = new SettingObject<Integer>("����ȭ��", "Chance", 50,
			"# ȭ���� ������ �� �� �ۼ�Ʈ Ȯ���� ������ ����ų�� �����մϴ�.",
			"# 50�� 50%�� �ǹ��մϴ�.") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 1 && value <= 100;
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
	
	private Integer Chance = ChanceConfig.getValue();
	
	private ArrayList<Arrow> ArrowList = new ArrayList<>();
	
	@Override
	public void PassiveSkill(Event event) {
		if(event instanceof ProjectileLaunchEvent) {
			ProjectileLaunchEvent e = (ProjectileLaunchEvent) event;
			if(e.getEntity().getShooter().equals(getPlayer())) {
				if(e.getEntity() instanceof Arrow) {
					ArrowList.add((Arrow) e.getEntity());
				}
			}
		} else if(event instanceof ProjectileHitEvent) {
			ProjectileHitEvent e = (ProjectileHitEvent) event;
			if(e.getEntity().getShooter().equals(getPlayer())) {
				if(e.getEntity() instanceof Arrow) {
					Arrow arrow = (Arrow) e.getEntity();

					if(ArrowList.contains(arrow)) {
						ArrowList.remove(arrow);
						Random r = new Random();
						
						if((r.nextInt(100) + 1) <= Chance) {
							SoundLib.BLOCK_NOTE_BLOCK_BELL.playSound(getPlayer());
							Location l = arrow.getLocation();
							l.getWorld().createExplosion(l, 2, false);
						}
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
