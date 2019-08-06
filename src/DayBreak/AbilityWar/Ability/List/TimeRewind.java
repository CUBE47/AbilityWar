package DayBreak.AbilityWar.Ability.List;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Note;
import org.bukkit.Note.Tone;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import DayBreak.AbilityWar.Ability.AbilityBase;
import DayBreak.AbilityWar.Ability.AbilityManifest;
import DayBreak.AbilityWar.Ability.AbilityManifest.Rank;
import DayBreak.AbilityWar.Ability.AbilityManifest.Species;
import DayBreak.AbilityWar.Ability.SubscribeEvent;
import DayBreak.AbilityWar.Ability.Timer.CooldownTimer;
import DayBreak.AbilityWar.Ability.Timer.DurationTimer;
import DayBreak.AbilityWar.Config.AbilitySettings.SettingObject;
import DayBreak.AbilityWar.Game.Games.Mode.AbstractGame.Participant;
import DayBreak.AbilityWar.Utils.Messager;
import DayBreak.AbilityWar.Utils.Data.PushingArray;
import DayBreak.AbilityWar.Utils.Library.SoundLib;
import DayBreak.AbilityWar.Utils.Thread.TimerBase;

@AbilityManifest(Name = "�ð� ����", Rank = Rank.S, Species = Species.HUMAN)
public class TimeRewind extends AbilityBase {

	public static SettingObject<Integer> CooldownConfig = new SettingObject<Integer>(TimeRewind.class, "Cooldown", 100, 
			"# ��Ÿ��") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 0;
		}
		
	};

	public static SettingObject<Integer> TimeConfig = new SettingObject<Integer>(TimeRewind.class, "Time", 5, 
			"# �ɷ��� ������� �� ���� ������ ���ư��� �����մϴ�.") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 1;
		}
		
	};

	public TimeRewind(Participant participant) {
		super(participant,
				ChatColor.translateAlternateColorCodes('&', "&fö���� ��Ŭ���ϸ� �ð��� ������ " + TimeConfig.getValue() + "�� ������ ���ư��ϴ�. " + Messager.formatCooldown(CooldownConfig.getValue())));
	}
	
	private CooldownTimer Cool = new CooldownTimer(this, CooldownConfig.getValue());
	
	private int Time = TimeConfig.getValue();
	
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

	@SubscribeEvent
	public void onPlayerDeath(PlayerDeathEvent e) {
		if(e.getEntity().equals(getPlayer())) {
			array = new PushingArray<>(PlayerData.class, Time * 20);
		}
	}
	
	@SubscribeEvent
	public void onEntityDamage(EntityDamageEvent e) {
		if(e.getEntity().equals(getPlayer()) && Rewinding) {
			e.setCancelled(true);
		}
	}
	
	@SubscribeEvent
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
		if(e.getDamager().equals(getPlayer()) && Rewinding) {
			e.setCancelled(true);
		}
	}
	
	private boolean Rewinding = false;
	
	private PushingArray<PlayerData> array = new PushingArray<>(PlayerData.class, Time * 20);
	
	private DurationTimer Skill = new DurationTimer(this, Time * 10, Cool) {
		
		private List<PlayerData> list;
		
		@Override
		public void onDurationStart() {
			Rewinding = true;
			this.list = array.toList();
		}
		
		@Override
		public void DurationProcess(Integer Seconds) {
			PlayerData data = list.get((Time * 10 - Seconds));
			if(data != null && data.getHealth() > 0) {
				getPlayer().teleport(data.getLocation());
				if(getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() >= data.getHealth()) getPlayer().setHealth(data.getHealth());
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
	
	private TimerBase Save = new TimerBase() {
		
		@Override
		public void onStart() {}
		
		@Override
		public void TimerProcess(Integer Seconds) {
			array.add(new PlayerData(getPlayer()));
		}

		@Override
		public void onEnd() {}
		
	}.setPeriod(2);
	
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
