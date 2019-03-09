package Marlang.AbilityWar.Ability.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;

import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Ability.AbilityManifest;
import Marlang.AbilityWar.Ability.AbilityManifest.Rank;
import Marlang.AbilityWar.Ability.Timer.CooldownTimer;
import Marlang.AbilityWar.Config.AbilitySettings.SettingObject;
import Marlang.AbilityWar.GameManager.Game.AbstractGame.Participant;
import Marlang.AbilityWar.Utils.Messager;
import Marlang.AbilityWar.Utils.Library.EffectLib;
import Marlang.AbilityWar.Utils.Library.ParticleLib;
import Marlang.AbilityWar.Utils.Math.LocationUtil;
import Marlang.AbilityWar.Utils.Thread.TimerBase;

@AbilityManifest(Name = "����", Rank = Rank.S)
public class Yeti extends AbilityBase {

	public static SettingObject<Integer> CooldownConfig = new SettingObject<Integer>("����", "Cooldown", 80, "# ��Ÿ��") {

		@Override
		public boolean Condition(Integer value) {
			return value >= 0;
		}

	};

	public static SettingObject<Integer> RangeConfig = new SettingObject<Integer>("����", "Range", 10,
			"# ��ų ��� �� �� �������� �ٲ� ����") {

		@Override
		public boolean Condition(Integer value) {
			return value >= 1 && value <= 50;
		}

	};

	public Yeti(Participant participant) {
		super(participant,
				ChatColor.translateAlternateColorCodes('&', "&f�� ���� �� ������ �پ��� ������ �޽��ϴ�."),
				ChatColor.translateAlternateColorCodes('&', "&fö���� ��Ŭ���ϸ� �ֺ��� �� �������� �ٲߴϴ�. " + Messager.formatCooldown(CooldownConfig.getValue())));
	}

	TimerBase Buff = new TimerBase() {

		@Override
		public void onStart() {
		}

		@Override
		public void TimerProcess(Integer Seconds) {
			Material m = getPlayer().getLocation().getBlock().getType();
			Material bm = getPlayer().getLocation().subtract(0, 1, 0).getBlock().getType();
			if (m.equals(Material.SNOW) || bm.equals(Material.SNOW) || bm.equals(Material.SNOW_BLOCK)) {
				EffectLib.SPEED.addPotionEffect(getPlayer(), 5, 1, true);
				EffectLib.INCREASE_DAMAGE.addPotionEffect(getPlayer(), 5, 0, true);
			}
		}

		@Override
		public void onEnd() {
		}

	}.setPeriod(1);

	TimerBase Ice = new TimerBase(RangeConfig.getValue()) {

		Integer Count;
		Location center;

		@Override
		public void onStart() {
			Count = 1;
			center = getPlayer().getLocation();

			center.getWorld().getHighestBlockAt(center).setType(Material.SNOW);
		}

		@Override
		public void TimerProcess(Integer Seconds) {
			for (Location l : LocationUtil.getCircle(center, Count, Count * 20, true)) {
				ParticleLib.SNOWBALL.spawnParticle(l, 1, 0, 0, 0);

				l.subtract(0, 1, 0).getBlock().setType(Material.SNOW);

				Block db = l.subtract(0, 1, 0).getBlock();

				if (db.getType().equals(Material.WATER)) {
					db.setType(Material.PACKED_ICE);
				} else if(db.getType().equals(Material.LAVA)) {
					db.setType(Material.OBSIDIAN);
				}
			}

			Count++;
		}

		@Override
		public void onEnd() {
		}

	}.setPeriod(3);

	CooldownTimer Cool = new CooldownTimer(this, CooldownConfig.getValue());

	@Override
	public boolean ActiveSkill(MaterialType mt, ClickType ct) {
		if (mt.equals(MaterialType.Iron_Ingot)) {
			if (ct.equals(ClickType.RightClick)) {
				if (!Cool.isCooldown()) {
					Ice.StartTimer();

					Cool.StartTimer();

					return true;
				}
			}
		}

		return false;
	}

	@Override
	public void PassiveSkill(Event event) {
	}

	@Override
	public void onRestrictClear() {
		Buff.StartTimer();
	}

	@Override
	public void TargetSkill(MaterialType mt, Entity entity) {}
	
}
