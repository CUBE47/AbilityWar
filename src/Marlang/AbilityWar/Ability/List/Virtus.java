package Marlang.AbilityWar.Ability.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;

import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Ability.AbilityManifest;
import Marlang.AbilityWar.Ability.AbilityManifest.Rank;
import Marlang.AbilityWar.Ability.Timer.CooldownTimer;
import Marlang.AbilityWar.Config.AbilitySettings.SettingObject;
import Marlang.AbilityWar.GameManager.Object.Participant;
import Marlang.AbilityWar.Utils.Messager;
import Marlang.AbilityWar.Utils.Library.ParticleLib;
import Marlang.AbilityWar.Utils.Library.SoundLib;
import Marlang.AbilityWar.Utils.Thread.TimerBase;

@AbilityManifest(Name = "��������", Rank = Rank.A)
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
	
	public Virtus(Participant participant) {
		super(participant,
				ChatColor.translateAlternateColorCodes('&', "&fö���� ��Ŭ���ϸ� ���� " + DurationConfig.getValue() + "&f�ʰ� �޴� �������� 75% �����մϴ�. " + Messager.formatCooldown(CooldownConfig.getValue())));
	}

	CooldownTimer Cool = new CooldownTimer(this, CooldownConfig.getValue());
	
	boolean Activated = false;
	
	TimerBase Activate = new TimerBase(DurationConfig.getValue()) {
		
		@Override
		public void onStart() {
			Activated = true;
		}
		
		@Override
		public void TimerProcess(Integer Seconds) {
			SoundLib.BLOCK_ANVIL_LAND.playSound(getPlayer());
			ParticleLib.LAVA.spawnParticle(getPlayer().getLocation(), 10, 3, 3, 3);
		}
		
		@Override
		public void onEnd() {
			Activated = false;
		}
		
	};
	
	@Override
	public boolean ActiveSkill(MaterialType mt, ClickType ct) {
		if(mt.equals(MaterialType.Iron_Ingot)) {
			if(ct.equals(ClickType.RightClick)) {
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
	public void onRestrictClear() {}

	@Override
	public void TargetSkill(MaterialType mt, Entity entity) {}
	
}
