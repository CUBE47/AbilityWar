package Marlang.AbilityWar.Ability.List;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Ability.Timer.CooldownTimer;
import Marlang.AbilityWar.Config.AbilitySettings.SettingObject;
import Marlang.AbilityWar.Utils.LocationUtil;
import Marlang.AbilityWar.Utils.Messager;
import Marlang.AbilityWar.Utils.TimerBase;

public class Gladiator extends AbilityBase {
	
	public static SettingObject<Integer> CooldownConfig = new SettingObject<Integer>("�۷�������", "Cooldown", 120,
			"# ��Ÿ��") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 0;
		}
		
	};

	public Gladiator() {
		super("�۷�������", Rank.S,
				ChatColor.translateAlternateColorCodes('&', "&f������ ö���� Ÿ���ϸ� �������� �����Ǹ� �� �ȿ���"),
				ChatColor.translateAlternateColorCodes('&', "&f1:1 ����� �ϰ� �˴ϴ�."));
		
		registerTimer(Cool);
		
		Field.setPeriod(2);
		
		registerTimer(Field);
		
		FieldClear.setSilentNotice(true);
		
		registerTimer(FieldClear);
	}
	
	CooldownTimer Cool = new CooldownTimer(this, CooldownConfig.getValue());
	
	HashMap<Block, MaterialData> Saves = new HashMap<Block, MaterialData>();
	
	TimerBase FieldClear = new TimerBase(20) {
		
		@Override
		public void TimerStart() {}
		
		@Override
		public void TimerProcess(Integer Seconds) {
			Messager.sendMessage(getTarget(), ChatColor.translateAlternateColorCodes('&', "&4[&c������&4] &f" + Seconds + "�� �Ŀ� �������� �����˴ϴ�."));
			Messager.sendMessage(getPlayer(), ChatColor.translateAlternateColorCodes('&', "&4[&c������&4] &f" + Seconds + "�� �Ŀ� �������� �����˴ϴ�."));
		}
		
		@Override
		public void TimerEnd() {
			for(Block b : Saves.keySet()) {
				b.setType(Saves.get(b).getItemType());
				b.getState().setData(Saves.get(b));
			}
			
			Saves.clear();
		}
		
	};
	
	Player target;
	
	public void setTarget(Player p) {
		this.target = p;
	}
	
	public Player getTarget() {
		return this.target;
	}
	
	TimerBase Field = new TimerBase(26) {
		
		Integer Count;
		Integer TotalCount;
		Location center;
		
		@Override
		public void TimerStart() {
			Count = 1;
			TotalCount = 1;
			center = getPlayer().getLocation();
			Saves.putIfAbsent(center.clone().subtract(0, 1, 0).getBlock(), center.clone().subtract(0, 1, 0).getBlock().getState().getData());
			center.subtract(0, 1, 0).getBlock().setType(Material.SMOOTH_BRICK);
		}
		
		@Override
		public void TimerProcess(Integer Seconds) {
			if(TotalCount <= 10) {
				for(Location l : LocationUtil.getCircle(center, Count, Count * this.getCount() * 30, false)) {
					Saves.putIfAbsent(l.getBlock(), l.getBlock().getState().getData());
					l.getBlock().setType(Material.SMOOTH_BRICK);
				}
				
				Count++;
			} else if(TotalCount > 10 && TotalCount <= 15) {
				for(Location l : LocationUtil.getCircle(center, Count - 1, Count * 30, false)) {
					Saves.putIfAbsent(l.clone().add(0, TotalCount - 10, 0).getBlock(), l.clone().add(0, TotalCount - 10, 0).getBlock().getState().getData());
					l.add(0, TotalCount - 10, 0).getBlock().setType(Material.IRON_FENCE);
				}
				for(Location l : LocationUtil.getCircle(center, Count, Count * 30, false)) {
					Saves.putIfAbsent(l.clone().add(0, TotalCount - 10, 0).getBlock(), l.clone().add(0, TotalCount - 10, 0).getBlock().getState().getData());
					l.add(0, TotalCount - 10, 0).getBlock().setType(Material.IRON_FENCE);
				}
			} else if(TotalCount > 15 && TotalCount <= 26) {
				for(Location l : LocationUtil.getCircle(center, Count, Count * 30, false)) {
					Saves.putIfAbsent(l.clone().add(0, 6, 0).getBlock(), l.clone().add(0, 6, 0).getBlock().getState().getData());
					l.add(0, 6, 0).getBlock().setType(Material.SMOOTH_BRICK);
				}
				
				Count--;
			}
			TotalCount++;
		}
		
		@Override
		public void TimerEnd() {
			Location check = center.clone().add(0, 6, 0);
			
			if(!check.getBlock().getType().equals(Material.SMOOTH_BRICK)) {
				Saves.putIfAbsent(check.getBlock(), check.getBlock().getState().getData());
				check.getBlock().setType(Material.SMOOTH_BRICK);
			}
			
			Location teleport = center.clone().add(0, 1, 0);
			
			getPlayer().teleport(teleport);
			getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 400, 4), true);
			getTarget().teleport(teleport);
			
			FieldClear.StartTimer();
		}
		
	};
	
	@Override
	public void ActiveSkill(ActiveMaterialType mt, ActiveClickType ct) {
		if(mt.equals(ActiveMaterialType.Iron_Ingot)) {
			if(ct.equals(ActiveClickType.LeftClick)) {
				if(!Cool.isCooldown()) {
					Messager.sendMessage(getPlayer(), ChatColor.translateAlternateColorCodes('&', "&a���&f�� �����ϴ�!"));
				}
			}
		}
	}

	@Override
	public void PassiveSkill(Event event) {
		if(event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
			if(e.getDamager().equals(getPlayer())) {
				if(e.getEntity() instanceof Player) {
					if(!e.isCancelled()) {
						if(getPlayer().getInventory().getItemInMainHand().getType().equals(Material.IRON_INGOT)) {
							if(!Cool.isCooldown()) {
								setTarget((Player) e.getEntity());
								Field.StartTimer();
								
								Cool.StartTimer();
							}
						}
					}
				}
			}
		} else if(event instanceof BlockBreakEvent) {
			BlockBreakEvent e = (BlockBreakEvent) event;
			if(Saves.keySet().contains(e.getBlock())) {
				if(!e.isCancelled()) {
					e.setCancelled(true);
					Player p = e.getPlayer();
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c������&f�� �μ� �� �����ϴ�!"));
				}
			}
		}
	}

	@Override
	public void AbilityEvent(EventType type) {}
	
}