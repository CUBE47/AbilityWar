package Marlang.AbilityWar.Ability.List;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Ability.CooldownTimer;
import Marlang.AbilityWar.Ability.Skill.SkillTimer;
import Marlang.AbilityWar.Ability.Skill.SkillTimer.SkillType;
import Marlang.AbilityWar.Utils.EffectUtil;
import Marlang.AbilityWar.Utils.Messager;
import Marlang.AbilityWar.Utils.NumberUtil;

public class Feather extends AbilityBase {
	
	public Feather() {
		super("����", Rank.A,
				ChatColor.translateAlternateColorCodes('&', "&fö��&f�� ��Ŭ���ϸ� 15�ʰ� ������ �� �ֽ��ϴ�. &c��Ÿ�� &7: &f80��"),
				ChatColor.translateAlternateColorCodes('&', "&f���� �������� �����մϴ�."));
	}
	
	CooldownTimer Cool = new CooldownTimer(this, 80);
	
	SkillTimer Skill = new SkillTimer(this, 15, SkillType.Active, Cool) {
		
		@Override
		public void TimerStart() {
			getPlayer().setAllowFlight(true);
		}
		
		@Override
		public void TimerProcess(Integer Seconds) {
			
		}
		
		@Override
		public void TimerEnd() {
			getPlayer().setAllowFlight(false);
			Messager.sendMessage(getPlayer(), ChatColor.translateAlternateColorCodes('&', "&6���� �ð�&f�� ����Ǿ����ϴ�."));
			super.TimerEnd();
		}
		
	};
	
	@Override
	public void ActiveSkill(ActiveMaterialType mt, ActiveClickType ct) {
		if(mt.equals(ActiveMaterialType.Iron_Ingot)) {
			if(ct.equals(ActiveClickType.LeftClick)) {
				if(!Skill.isTimerRunning()) {
					if(!Cool.isCooldown()) {
						Skill.Execute();
					}
				} else {
					Messager.sendMessage(getPlayer(), ChatColor.translateAlternateColorCodes('&', "&6���� �ð� &f" + NumberUtil.parseTimeString(Skill.getTempCount())));
				}
			}
		}
	}
	
	@Override
	public void PassiveSkill(Event event) {
		if(event instanceof EntityDamageEvent) {
			EntityDamageEvent e = (EntityDamageEvent) event;
			if(!e.isCancelled()) {
				if(e.getEntity() instanceof Player) {
					Player p = (Player) e.getEntity();
					if(p.equals(this.getPlayer())) {
						if(e.getCause().equals(DamageCause.FALL)) {
							e.setCancelled(true);
							Messager.sendMessage(p, ChatColor.translateAlternateColorCodes('&', "&a���� �������� ���� �ʽ��ϴ�."));
							EffectUtil.sendSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
						}
					}
				}
			}
		}
	}
	
}
