package Marlang.AbilityWar.Ability.List;

import org.bukkit.ChatColor;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;

import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Ability.Timer.CooldownTimer;
import Marlang.AbilityWar.Config.AbilitySettings.SettingObject;
import Marlang.AbilityWar.Utils.Messager;
import Marlang.AbilityWar.Utils.TimerBase;
import Marlang.AbilityWar.Utils.Library.ParticleLib;
import Marlang.AbilityWar.Utils.Library.SoundLib;

public class Virtus extends AbilityBase {

	public static SettingObject<Integer> DurationConfig = new SettingObject<Integer>("��������", "Duration", 3,
			"# �ɷ� ���ӽð�") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 1;
		}
		
	};

	public static SettingObject<Integer> CooldownConfig = new SettingObject<Integer>("��������", "Cooldown", 70,
			"# ��Ÿ��") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 0;
		}
		
	};
	
	public Virtus() {
		super("��������", Rank.A,
				ChatColor.translateAlternateColorCodes('&', "&fö���� ��Ŭ���ϸ� ���� " + DurationConfig.getValue() + "&f�ʰ� �޴� �������� 75% �����մϴ�. " + Messager.formatCooldown(CooldownConfig.getValue())));
		
		registerTimer(Cool);
		
		registerTimer(Activate);
	}

	CooldownTimer Cool = new CooldownTimer(this, CooldownConfig.getValue());
	
	boolean Activated = false;
	
	TimerBase Activate = new TimerBase(DurationConfig.getValue()) {
		
		@Override
		public void TimerStart(Data<?>... args) {
			Activated = true;
		}
		
		@Override
		public void TimerProcess(Integer Seconds) {
			SoundLib.BLOCK_ANVIL_LAND.playSound(getPlayer());
			ParticleLib.LAVA.spawnParticle(getPlayer().getLocation(), 10, 3, 3, 3);
		}
		
		@Override
		public void TimerEnd() {
			Activated = false;
		}
		
	};
	
	@Override
	public boolean ActiveSkill(ActiveMaterialType mt, ActiveClickType ct) {
		if(mt.equals(ActiveMaterialType.Iron_Ingot)) {
			if(ct.equals(ActiveClickType.RightClick)) {
				if(!Cool.isCooldown()) {
					Activate.StartTimer();
					
					Cool.StartTimer();
					
					return true;
				}
			}
		}
		
		return false;
	}

	@Override
	public void PassiveSkill(Event event) {
		if(event instanceof EntityDamageEvent) {
			EntityDamageEvent e = (EntityDamageEvent) event;
			if(e.getEntity().equals(getPlayer())) {
				if(Activated) {
					e.setDamage(e.getDamage() / 4);
				}
			}
		}
	}

	@Override
	public void AbilityEvent(EventType type) {}

}
