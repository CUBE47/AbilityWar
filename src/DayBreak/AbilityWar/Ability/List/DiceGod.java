package DayBreak.AbilityWar.Ability.List;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import DayBreak.AbilityWar.Ability.AbilityBase;
import DayBreak.AbilityWar.Ability.AbilityManifest;
import DayBreak.AbilityWar.Ability.AbilityManifest.Rank;
import DayBreak.AbilityWar.Ability.AbilityManifest.Species;
import DayBreak.AbilityWar.Ability.SubscribeEvent;
import DayBreak.AbilityWar.Ability.Timer.CooldownTimer;
import DayBreak.AbilityWar.Config.AbilitySettings.SettingObject;
import DayBreak.AbilityWar.Game.Games.Mode.AbstractGame.Participant;
import DayBreak.AbilityWar.Utils.Messager;
import DayBreak.AbilityWar.Utils.Library.EffectLib;
import DayBreak.AbilityWar.Utils.VersionCompat.VersionUtil;

@AbilityManifest(Name = "���̽� ��", Rank = Rank.A, Species = Species.GOD)
public class DiceGod extends AbilityBase {

	public static SettingObject<Integer> CooldownConfig = new SettingObject<Integer>(DiceGod.class, "Cooldown", 60, 
			"# ��Ÿ��") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 0;
		}
		
	};
	
	public DiceGod(Participant participant) {
		super(participant,
				ChatColor.translateAlternateColorCodes('&', "&fö���� ��Ŭ���ϸ� &c��� &f/ &b�ż� &f/ &6�� &f/ &3���� &f/ &8���� &f/ &7������ &fȿ�� �� �ϳ���"),
				ChatColor.translateAlternateColorCodes('&', "&f10�ʰ� �޽��ϴ�. " + Messager.formatCooldown(CooldownConfig.getValue())),
				ChatColor.translateAlternateColorCodes('&', "&f������ �޾��� �� 1/6 Ȯ���� �������� �޴� ��� ��������ŭ ü���� ȸ���մϴ�."));
	}
	
	private CooldownTimer Cool = new CooldownTimer(this, CooldownConfig.getValue());
	
	@Override
	public boolean ActiveSkill(MaterialType mt, ClickType ct) {
		if(mt.equals(MaterialType.Iron_Ingot)) {
			if(ct.equals(ClickType.RightClick)) {
				if(!Cool.isCooldown()) {
					Player p = getPlayer();
					
					Random r = new Random();
					Integer random = r.nextInt(6);
					
					if(random.equals(0)) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c��� &fȿ���� �޾ҽ��ϴ�."));
						EffectLib.REGENERATION.addPotionEffect(p, 200, 2, true);
					} else if(random.equals(1)) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b�ż� &fȿ���� �޾ҽ��ϴ�."));
						EffectLib.SPEED.addPotionEffect(p, 200, 2, true);
					} else if(random.equals(2)) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6�� &fȿ���� �޾ҽ��ϴ�."));
						EffectLib.INCREASE_DAMAGE.addPotionEffect(p, 200, 2, true);
					} else if(random.equals(3)) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3���� &fȿ���� �޾ҽ��ϴ�."));
						EffectLib.DAMAGE_RESISTANCE.addPotionEffect(p, 200, 2, true);
					} else if(random.equals(4)) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8���� &fȿ���� �޾ҽ��ϴ�."));
						EffectLib.SLOW.addPotionEffect(p, 200, 1, true);
					} else if(random.equals(5)) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7������ &fȿ���� �޾ҽ��ϴ�."));
						EffectLib.WEAKNESS.addPotionEffect(p, 200, 1, true);
					}
					
					Cool.StartTimer();
					
					return true;
				}
			}
		}
		
		return false;
	}
	
	@SubscribeEvent
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
		if(e.getEntity().equals(getPlayer())) {
			Random r = new Random();
			if(r.nextInt(6) == 0) {
				Double damage = e.getDamage();
				e.setDamage(0);
				
				Double health = getPlayer().getHealth() + damage;
				
				if(health > VersionUtil.getMaxHealth(getPlayer())) health = VersionUtil.getMaxHealth(getPlayer());
				
				if(!getPlayer().isDead()) {
					getPlayer().setHealth(health);
				}
			}
		}
	}
	
	@Override
	public void onRestrictClear() {}

	@Override
	public void TargetSkill(MaterialType mt, LivingEntity entity) {}
	
}
