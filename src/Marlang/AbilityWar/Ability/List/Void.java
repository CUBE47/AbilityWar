package Marlang.AbilityWar.Ability.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;

import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Ability.AbilityManifest;
import Marlang.AbilityWar.Ability.AbilityManifest.Rank;
import Marlang.AbilityWar.Ability.Timer.CooldownTimer;
import Marlang.AbilityWar.Config.AbilitySettings.SettingObject;
import Marlang.AbilityWar.GameManager.Game.AbstractGame.Participant;
import Marlang.AbilityWar.Utils.Messager;
import Marlang.AbilityWar.Utils.Library.ParticleLib;
import Marlang.AbilityWar.Utils.Math.LocationUtil;
import Marlang.AbilityWar.Utils.Thread.TimerBase;

@AbilityManifest(Name = "���̵�", Rank = Rank.A)
public class Void extends AbilityBase {

	public static SettingObject<Integer> CooldownConfig = new SettingObject<Integer>("���̵�", "Cooldown", 80,
			"# ��Ÿ��") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 0;
		}
		
	};

	public Void(Participant participant) {
		super(participant,
				ChatColor.translateAlternateColorCodes('&', "&fö���� ��Ŭ���ϸ� ���� ������ �ִ� �÷��̾�� �ڷ���Ʈ�մϴ�. " + Messager.formatCooldown(CooldownConfig.getValue())),
				ChatColor.translateAlternateColorCodes('&', "&f�ڷ���Ʈ�� �ϰ� �� �� 5�ʰ� �������� ���� �ʽ��ϴ�."));
	}

	boolean Inv = false;
	
	CooldownTimer Cool = new CooldownTimer(this, CooldownConfig.getValue());
	
	TimerBase Invincibility = new TimerBase(5) {
		
		@Override
		public void onStart() {
			Inv = true;
		}
		
		@Override
		public void TimerProcess(Integer Seconds) {}
		
		@Override
		public void onEnd() {
			Inv = false;
		}
		
	};
	
	@Override
	public boolean ActiveSkill(MaterialType mt, ClickType ct) {
		if(mt.equals(MaterialType.Iron_Ingot)) {
			if(ct.equals(ClickType.RightClick)) {
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
	public void onRestrictClear() {}

	@Override
	public void TargetSkill(MaterialType mt, Entity entity) {}
	
}
