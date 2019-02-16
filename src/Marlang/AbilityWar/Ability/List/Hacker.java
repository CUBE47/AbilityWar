package Marlang.AbilityWar.Ability.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;

import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Ability.Timer.CooldownTimer;
import Marlang.AbilityWar.Config.AbilitySettings.SettingObject;
import Marlang.AbilityWar.Utils.Messager;
import Marlang.AbilityWar.Utils.Library.ParticleLib;
import Marlang.AbilityWar.Utils.Math.LocationUtil;
import Marlang.AbilityWar.Utils.Thread.TimerBase;

public class Hacker extends AbilityBase {

	public static SettingObject<Integer> CooldownConfig = new SettingObject<Integer>("��Ŀ", "Cooldown", 180, 
			"# ��Ÿ��") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 0;
		}
		
	};

	public static SettingObject<Integer> DurationConfig = new SettingObject<Integer>("��Ŀ", "Duration", 5, 
			"# �ɷ� ���ӽð�") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 1;
		}
		
	};
	
	public Hacker(Player player) {
		super(player, "��Ŀ", Rank.A,
				ChatColor.translateAlternateColorCodes('&', "&fö���� ��Ŭ���ϸ� �ڽſ��� ���� ����� �÷��̾��� ��ǥ�� �˸���"),
				ChatColor.translateAlternateColorCodes('&', "&f" + DurationConfig.getValue() + "�ʰ� �ش� �÷��̾ �������� ���ϰ� �մϴ�."));
	}

	Player CantMove = null;
	
	CooldownTimer Cool = new CooldownTimer(this, CooldownConfig.getValue());
	
	TimerBase Move = new TimerBase(DurationConfig.getValue() * 20) {
		
		@Override
		public void onStart() {}
		
		@Override
		public void TimerProcess(Integer Seconds) {
			if(CantMove != null) {
				ParticleLib.SPELL.spawnParticle(CantMove.getLocation(), 2, 3, 3, 3);
			}
		}
		
		@Override
		public void onEnd() {
			CantMove = null;
		}
		
	}.setPeriod(1);
	
	@Override
	public boolean ActiveSkill(ActiveMaterialType mt, ActiveClickType ct) {
		if(mt.equals(ActiveMaterialType.Iron_Ingot)) {
			if(ct.equals(ActiveClickType.RightClick)) {
				if(!Cool.isCooldown()) {
					Player target = LocationUtil.getNearestPlayer(getPlayer());
					
					if(target != null) {
						int X = (int) target.getLocation().getX();
						int Y = (int) target.getLocation().getY();
						int Z = (int) target.getLocation().getZ();
						
						Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&e" + target.getName() + "&f���� &aX " + X + "&f, &aY " + Y + "&f, &aZ " + Z + "&f�� �ֽ��ϴ�."));
						
						CantMove = target;
						Move.StartTimer();
						
						Cool.StartTimer();
						
						return true;
					} else {
						Messager.sendMessage(getPlayer(), ChatColor.translateAlternateColorCodes('&', "&a���� ����� �÷��̾�&f�� �������� �ʽ��ϴ�."));
					}
				}
			}
		}
		
		return false;
	}

	@Override
	public void PassiveSkill(Event event) {
		if(event instanceof PlayerMoveEvent) {
			PlayerMoveEvent e = (PlayerMoveEvent) event;
			if(CantMove != null) {
				if(e.getPlayer().equals(CantMove)) {
					e.setCancelled(true);
				}
			}
		}
	}

	@Override
	public void onRestrictClear() {}
	
}
