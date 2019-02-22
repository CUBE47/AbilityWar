package Marlang.AbilityWar.Ability.List;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Note;
import org.bukkit.Note.Tone;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Ability.Timer.CooldownTimer;
import Marlang.AbilityWar.Ability.Timer.DurationTimer;
import Marlang.AbilityWar.Config.AbilitySettings.SettingObject;
import Marlang.AbilityWar.Utils.Messager;
import Marlang.AbilityWar.Utils.Data.AdvancedArray;
import Marlang.AbilityWar.Utils.Library.SoundLib;
import Marlang.AbilityWar.Utils.Thread.TimerBase;

public class TimeRewind extends AbilityBase {

	public static SettingObject<Integer> CooldownConfig = new SettingObject<Integer>("�ð� ����", "Cooldown", 100, 
			"# ��Ÿ��") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 0;
		}
		
	};

	public static SettingObject<Integer> TimeConfig = new SettingObject<Integer>("�ð� ����", "Time", 5, 
			"# �ɷ��� ������� �� ���� ������ ���ư��� �����մϴ�.") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 1;
		}
		
	};

	public TimeRewind(Player player) {
		super(player, "�ð� ����", Rank.S,
				ChatColor.translateAlternateColorCodes('&', "&fö���� ��Ŭ���ϸ� �ð��� ������ " + TimeConfig.getValue() + "�� ������ ���ư��ϴ�. " + Messager.formatCooldown(CooldownConfig.getValue())));
	}
	
	CooldownTimer Cool = new CooldownTimer(this, CooldownConfig.getValue());
	
	Integer Time = TimeConfig.getValue();
	
	@Override
	public boolean ActiveSkill(MaterialType mt, ClickType ct) {
		if(mt.equals(MaterialType.Iron_Ingot)) {
			if(ct.equals(ClickType.RightClick)) {
				if(!Cool.isCooldown()) {
					Skill.StartTimer();
					
					return true;
				}
			}
		}
		
		return false;
	}

	@Override
	public void PassiveSkill(Event event) {
		if(event instanceof PlayerDeathEvent) {
			PlayerDeathEvent e = (PlayerDeathEvent) event;
			if(e.getEntity().equals(getPlayer())) {
				array = new AdvancedArray<>(PlayerData.class, Time * 20);
			}
		} else if(event instanceof EntityDamageEvent) {
			EntityDamageEvent e = (EntityDamageEvent) event;
			if(e.getEntity().equals(getPlayer()) && Rewinding) {
				e.setCancelled(true);
			}
			
			if(e instanceof EntityDamageByEntityEvent) {
				EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent) e;
				if(damageEvent.getDamager().equals(getPlayer()) && Rewinding) {
					e.setCancelled(true);
				}
			}
		}
	}
	
	boolean Rewinding = false;
	
	AdvancedArray<PlayerData> array = new AdvancedArray<>(PlayerData.class, Time * 20);
	
	DurationTimer Skill = new DurationTimer(this, Time * 20, Cool) {
		
		List<PlayerData> list;
		
		@Override
		public void onDurationStart() {
			Rewinding = true;
			this.list = array.getList();
		}
		
		@Override
		public void DurationProcess(Integer Seconds) {
			PlayerData data = list.get((Time * 20) - Seconds);
			if(data != null && data.getHealth() > 0) {
				getPlayer().teleport(data.getLocation());
				getPlayer().setHealth(data.getHealth());
			}
		}
		
		@Override
		public void onDurationEnd() {
			Rewinding = false;
			SoundLib.BELL.playInstrument(getPlayer(), Note.natural(0, Tone.D));
			SoundLib.BELL.playInstrument(getPlayer(), Note.sharp(0, Tone.F));
			SoundLib.BELL.playInstrument(getPlayer(), Note.natural(1, Tone.A));
		}
		
	}.setPeriod(1);
	
	TimerBase Save = new TimerBase() {
		
		@Override
		public void onStart() {}
		
		@Override
		public void TimerProcess(Integer Seconds) {
			array.add(new PlayerData(getPlayer()));
		}

		@Override
		public void onEnd() {}
		
	}.setPeriod(1);
	
	@Override
	public void onRestrictClear() {
		Save.StartTimer();
	}
	
	private class PlayerData {
		
		Location location;
		Double Health;
		
		public PlayerData(Player p) {
			this.location = p.getLocation();
			this.Health = p.getHealth();
		}

		public Location getLocation() {
			return location;
		}

		public Double getHealth() {
			return Health;
		}
		
	}

	@Override
	public void TargetSkill(MaterialType mt, Entity entity) {}
	
}
