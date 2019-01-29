package Marlang.AbilityWar.Ability.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;

import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Ability.Timer.CooldownTimer;
import Marlang.AbilityWar.Config.AbilitySettings.SettingObject;
import Marlang.AbilityWar.Utils.LocationUtil;
import Marlang.AbilityWar.Utils.Messager;
import Marlang.AbilityWar.Utils.TimerBase;
import Marlang.AbilityWar.Utils.Library.ParticleLib;

public class Void extends AbilityBase {

	public static SettingObject<Integer> CooldownConfig = new SettingObject<Integer>("���̵�", "Cooldown", 80,
			"# ��Ÿ��") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 0;
		}
		
	};

	public Void() {
		super("���̵�", Rank.A,
				ChatColor.translateAlternateColorCodes('&', "&fö���� ��Ŭ���ϸ� ���� ������ �ִ� �÷��̾�� �ڷ���Ʈ�մϴ�. " + Messager.formatCooldown(CooldownConfig.getValue())),
				ChatColor.translateAlternateColorCodes('&', "&f�ڷ���Ʈ�� �ϰ� �� �� 5�ʰ� �������� ���� �ʽ��ϴ�."));
		
		registerTimer(Cool);
		registerTimer(Invincibility);
	}

	boolean Inv = false;
	
	CooldownTimer Cool = new CooldownTimer(this, CooldownConfig.getValue());
	
	TimerBase Invincibility = new TimerBase(5) {
		
		@Override
		public void TimerStart() {
			Inv = true;
		}
		
		@Override
		public void TimerProcess(Integer Seconds) {}
		
		@Override
		public void TimerEnd() {
			Inv = false;
		}
		
	};
	
	@Override
	public boolean ActiveSkill(ActiveMaterialType mt, ActiveClickType ct) {
		if(mt.equals(ActiveMaterialType.Iron_Ingot)) {
			if(ct.equals(ActiveClickType.RightClick)) {
				if(!Cool.isCooldown()) {
					Player target = LocationUtil.getNearestPlayer(getPlayer());

					if(target != null) {
						getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&e" + target.getName() + "&f�Կ��� �ڷ���Ʈ�մϴ�."));
						getPlayer().teleport(target);
						ParticleLib.DRAGON_BREATH.spawnParticle(getPlayer().getLocation(), 20,1, 1, 1);
						
						Invincibility.StartTimer();
						
						Cool.StartTimer();
					} else {
						getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&a���� ����� �÷��̾�&f�� �������� �ʽ��ϴ�."));
					}
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
				if(this.Inv) {
					e.setCancelled(true);
					ParticleLib.DRAGON_BREATH.spawnParticle(getPlayer().getLocation(), 20, 1, 1, 1);
				}
			}
		}
	}

	@Override
	public void AbilityEvent(EventType type) {}

}
