package Marlang.AbilityWar.Ability.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Ability.AbilityManifest;
import Marlang.AbilityWar.Ability.AbilityManifest.Rank;
import Marlang.AbilityWar.Ability.Timer.CooldownTimer;
import Marlang.AbilityWar.Config.AbilitySettings.SettingObject;
import Marlang.AbilityWar.GameManager.Object.Participant;
import Marlang.AbilityWar.Utils.Messager;

@AbilityManifest(Name = "������ ���Ŀ", Rank = Rank.A)
public class EnergyBlocker extends AbilityBase {

	public static SettingObject<Integer> CooldownConfig = new SettingObject<Integer>("���������Ŀ", "Cooldown", 10, 
			"# ��Ÿ��") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 0;
		}
		
	};
	
	boolean Default = true;
	
	public EnergyBlocker(Participant participant) {
		super(participant,
				ChatColor.translateAlternateColorCodes('&', "&f���Ÿ� ���� ���ظ� ��������, �ٰŸ� ���� ���ظ� �� ��� �ްų�"),
				ChatColor.translateAlternateColorCodes('&', "&f���Ÿ� ���� ���ظ� �� ���, �ٰŸ� ���� ���ظ� �������� ���� �� �ֽ��ϴ�."),
				ChatColor.translateAlternateColorCodes('&', "&fö���� ��Ŭ���ϸ� ������ ���� ������ �ڹٲߴϴ�. " + Messager.formatCooldown(CooldownConfig.getValue())),
				ChatColor.translateAlternateColorCodes('&', "&fö���� ��Ŭ���ϸ� ���� ���¸� Ȯ���� �� �ֽ��ϴ�."));
	}
	
	CooldownTimer Cool = new CooldownTimer(this, CooldownConfig.getValue());
	
	@Override
	public boolean ActiveSkill(MaterialType mt, ClickType ct) {
		if(mt.equals(MaterialType.Iron_Ingot)) {
			if(ct.equals(ClickType.RightClick)) {
				if(!Cool.isCooldown()) {
					Default = !Default;
					Player p = getPlayer();
					if(Default) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b���Ÿ� &f����&7, &a�ٰŸ� &f�� ��� ����Ǿ����ϴ�."));
					} else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b���Ÿ� &f�� ��&7, &a�ٰŸ� &f�������� ����Ǿ����ϴ�."));
					}
					
					Cool.StartTimer();
				}
			} else if(ct.equals(ClickType.LeftClick)) {
				if(Default) {
					Messager.sendMessage(getPlayer(), ChatColor.translateAlternateColorCodes('&', "&6���� ����&f: &b���Ÿ� &f����&7, &a�ٰŸ� &f�� ��"));
				} else {
					Messager.sendMessage(getPlayer(), ChatColor.translateAlternateColorCodes('&', "&6���� ����&f: &b���Ÿ� &f�� ��&7, &a�ٰŸ� &f����"));
				}
			}
		}
		
		return false;
	}
	
	@Override
	public void PassiveSkill(Event event) {
		if(event instanceof EntityDamageEvent) {
			EntityDamageEvent e = (EntityDamageEvent) event;
			if(e.getEntity() instanceof Player) {
				Player p = (Player) e.getEntity();
				if(p.equals(getPlayer())) {
					if(!e.isCancelled()) {
						DamageCause dc = e.getCause();
						if(dc != null) {
							if(dc.equals(DamageCause.PROJECTILE)) {
								if(Default) {
									e.setDamage(e.getDamage() / 2);
								} else {
									e.setDamage(e.getDamage() * 2);
								}
							} else if(dc.equals(DamageCause.ENTITY_ATTACK)) {
								if(Default) {
									e.setDamage(e.getDamage() * 2);
								} else {
									e.setDamage(e.getDamage() / 2);
								}
							}
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
