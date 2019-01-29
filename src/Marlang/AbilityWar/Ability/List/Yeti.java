package Marlang.AbilityWar.Ability.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Ability.Timer.CooldownTimer;
import Marlang.AbilityWar.Config.AbilitySettings.SettingObject;
import Marlang.AbilityWar.Utils.LocationUtil;
import Marlang.AbilityWar.Utils.Messager;
import Marlang.AbilityWar.Utils.TimerBase;
import Marlang.AbilityWar.Utils.Library.ParticleLib;

public class Yeti extends AbilityBase {

	public static SettingObject<Integer> CooldownConfig = new SettingObject<Integer>("����", "Cooldown", 80,
			"# ��Ÿ��") {
		
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

	public Yeti() {
		super("����", Rank.S,
				ChatColor.translateAlternateColorCodes('&', "&f�� ���� �� ������ �پ��� ������ �޽��ϴ�."),
				ChatColor.translateAlternateColorCodes('&', "&fö���� ��Ŭ���ϸ� �ֺ��� �� �������� �ٲߴϴ�. " + Messager.formatCooldown(CooldownConfig.getValue())));
		
		Buff.setPeriod(1);
		
		registerTimer(Buff);
		
		Ice.setPeriod(3);
		
		registerTimer(Ice);
		
		registerTimer(Cool);
	}
	
	TimerBase Buff = new TimerBase() {
		
		@Override
		public void TimerStart() {}
		
		@Override
		public void TimerProcess(Integer Seconds) {
			Material m = getPlayer().getLocation().getBlock().getType();
			Material bm = getPlayer().getLocation().subtract(0, 1, 0).getBlock().getType();
			if(m.equals(Material.SNOW) || bm.equals(Material.SNOW) || bm.equals(Material.SNOW_BLOCK)) {
				getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5, 1), true);
				getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 5, 0), true);
			}
		}
		
		@Override
		public void TimerEnd() {}
		
	};
	
	TimerBase Ice = new TimerBase(RangeConfig.getValue()) {
		
		Integer Count;
		Location center;
		
		@Override
		public void TimerStart() {
			Count = 1;
			center = getPlayer().getLocation();
		}
		
		@Override
		public void TimerProcess(Integer Seconds) {
			for(Location l : LocationUtil.getCircle(center, Count, Count * 20, true)) {
				ParticleLib.SNOWBALL.spawnParticle(l, 1, 0, 0, 0);

				l.subtract(0, 1, 0).getBlock().setType(Material.SNOW);
			}
			
			Count++;
		}
		
		@Override
		public void TimerEnd() {}
		
	};
	
	CooldownTimer Cool = new CooldownTimer(this, CooldownConfig.getValue());

	@Override
	public boolean ActiveSkill(ActiveMaterialType mt, ActiveClickType ct) {
		if(mt.equals(ActiveMaterialType.Iron_Ingot)) {
			if(ct.equals(ActiveClickType.RightClick)) {
				if(!Cool.isCooldown()) {
					Ice.StartTimer();
					
					Cool.StartTimer();
					
					return true;
				}
			}
		}
		
		return false;
	}

	@Override
	public void PassiveSkill(Event event) {}

	@Override
	public void AbilityEvent(EventType type) {
		if(type.equals(EventType.RestrictClear)) {
			Buff.StartTimer();
		}
	}

}
