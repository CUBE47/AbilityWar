package Marlang.AbilityWar.Ability.List;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Ability.CooldownTimer;
import Marlang.AbilityWar.Ability.Skill.SkillTimer;
import Marlang.AbilityWar.Ability.Skill.SkillTimer.SkillType;
import Marlang.AbilityWar.Utils.EffectUtil;

public class Assassin extends AbilityBase {
	
	public Assassin() {
		super("�ϻ���", Rank.A,
				ChatColor.translateAlternateColorCodes('&', "&fö���� ��Ŭ���ϸ� �ִ� 4���� ������ �ڷ���Ʈ�ϸ�"),
				ChatColor.translateAlternateColorCodes('&', "&f�������� �ݴϴ�. &c��Ÿ�� &7: &f60��"));
		Skill.setPeriod(5);
	}
	
	CooldownTimer Cool = new CooldownTimer(this, 60);
	
	SkillTimer Skill = new SkillTimer(this, 5, SkillType.Active, Cool) {
		
		ArrayList<Damageable> Entities = new ArrayList<Damageable>();
		
		@Override
		public void TimerStart() {
			Entities.addAll(getNearbyDamageableEntities());
		}
		
		@Override
		public void TimerProcess(Integer Seconds) {
			if(Entities.size() >= 1) {
				Damageable e = Entities.get(0);
				Entities.remove(e);
				getPlayer().teleport(e);
				e.damage(10, getPlayer());
				EffectUtil.sendSound(getPlayer(), Sound.ENTITY_PLAYER_ATTACK_SWEEP);
				EffectUtil.sendSound(getPlayer(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
			} else {
				this.StopTimer();
			}
		}
		
	};
	
	@Override
	public void ActiveSkill(ActiveMaterialType mt, ActiveClickType ct) {
		if(mt.equals(ActiveMaterialType.Iron_Ingot)) {
			if(ct.equals(ActiveClickType.LeftClick)) {
				if(!Cool.isCooldown()) {
					Skill.Execute();
				}
			}
		}
	}
	
	public ArrayList<Damageable> getNearbyDamageableEntities() {
		Player p = this.getPlayer();
		ArrayList<Damageable> Entities = new ArrayList<Damageable>();
		for(Entity e : p.getNearbyEntities(6, 3, 6)) {
			if(e instanceof Damageable) {
				Entities.add((Damageable) e);
			}
		}
		
		return Entities;
	}
	
	@Override
	public void PassiveSkill(Event event) {}
	
}
