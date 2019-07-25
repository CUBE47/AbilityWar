	package DayBreak.AbilityWar.Ability.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import DayBreak.AbilityWar.Ability.AbilityBase;
import DayBreak.AbilityWar.Ability.AbilityManifest;
import DayBreak.AbilityWar.Ability.AbilityManifest.Rank;
import DayBreak.AbilityWar.Ability.AbilityManifest.Species;
import DayBreak.AbilityWar.Ability.Timer.CooldownTimer;
import DayBreak.AbilityWar.Config.AbilitySettings.SettingObject;
import DayBreak.AbilityWar.Game.Games.Mode.AbstractGame.Participant;
import DayBreak.AbilityWar.Utils.Messager;
import DayBreak.AbilityWar.Utils.VersionCompat.VersionUtil;

@AbilityManifest(Name = "������", Rank = Rank.B, Species = Species.HUMAN)
public class Chaser extends AbilityBase {

	public static SettingObject<Integer> CooldownConfig = new SettingObject<Integer>(Chaser.class, "Cooldown", 120,
			"# ��Ÿ��") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 0;
		}
		
	};
	
	public Chaser(Participant participant) {
		super(participant,
				ChatColor.translateAlternateColorCodes('&', "&f������ ö���� Ÿ���ϸ� ��󿡰� ���� ��ġ�� �����մϴ�. " + Messager.formatCooldown(CooldownConfig.getValue())),
				ChatColor.translateAlternateColorCodes('&', "&f���� ö���� ��Ŭ���ϸ� ���� ��ġ�� ������ �÷��̾��� ��ǥ�� �� �� �ֽ��ϴ�."),
				ChatColor.translateAlternateColorCodes('&', "&f���� ��ġ�� �Ѹ��Ը� ������ �� �ֽ��ϴ�."));
	}

	CooldownTimer Cool = new CooldownTimer(this, CooldownConfig.getValue());
	
	Player target = null;
	
	@Override
	public boolean ActiveSkill(MaterialType mt, ClickType ct) {
		if(mt.equals(MaterialType.Iron_Ingot)) {
			if(ct.equals(ClickType.LeftClick)) {
				Cool.isCooldown();
			} else if(ct.equals(ClickType.RightClick)) {
				if(target != null) {
					int X = (int) target.getLocation().getX();
					int Y = (int) target.getLocation().getY();
					int Z = (int) target.getLocation().getZ();
					
					Messager.sendMessage(getPlayer(), ChatColor.translateAlternateColorCodes('&', "&e" + target.getName() + "&f���� &aX " + X + "&f, &aY " + Y + "&f, &aZ " + Z + "&f�� �ֽ��ϴ�."));
				} else {
					Messager.sendMessage(getPlayer(), ChatColor.translateAlternateColorCodes('&', "&f�ƹ����Ե� ���� ��ġ�� �������� �ʾҽ��ϴ�. &8( &7���� �Ұ��� &8)"));
				}
			}
		}
		
		return false;
	}

	@Override
	public void PassiveSkill(Event event) {
		if(event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
			if(e.getDamager().equals(getPlayer())) {
				if(e.getEntity() instanceof Player) {
					if(!e.isCancelled()) {
						if(VersionUtil.getItemInHand(getPlayer()).getType().equals(Material.IRON_INGOT)) {
							if(!Cool.isCooldown()) {
								Player p = (Player) e.getEntity();
								this.target = p;
								Messager.sendMessage(getPlayer(), ChatColor.translateAlternateColorCodes('&', "&e" + p.getName() + "&f�Կ��� ���� ��ġ�� �����Ͽ����ϴ�."));
								
								Cool.StartTimer();
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
