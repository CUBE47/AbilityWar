package DayBreak.AbilityWar.Ability.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.PlayerDeathEvent;

import DayBreak.AbilityWar.Ability.AbilityBase;
import DayBreak.AbilityWar.Ability.AbilityManifest;
import DayBreak.AbilityWar.Ability.AbilityManifest.Rank;
import DayBreak.AbilityWar.Game.Games.AbstractGame.Participant;
import DayBreak.AbilityWar.Utils.Thread.AbilityWarThread;

@AbilityManifest(Name = "���̷���", Rank = Rank.D)
public class Virus extends AbilityBase {

	public Virus(Participant participant) {
		super(participant,
				ChatColor.translateAlternateColorCodes('&', "&f�� �ɷ��� ����� ���� ������� �����˴ϴ�."));
	}

	@Override
	public boolean ActiveSkill(MaterialType mt, ClickType ct) {
		return false;
	}

	@Override
	public void PassiveSkill(Event event) {
		if(event instanceof PlayerDeathEvent) {
			PlayerDeathEvent e = (PlayerDeathEvent) event;
			if(e.getEntity().equals(getPlayer())) {
				if(AbilityWarThread.isGameTaskRunning()) {
					Player Killer = getPlayer().getKiller();
					if(Killer != null && AbilityWarThread.getGame().isParticipating(Killer)) {
						try {
							Participant target = AbilityWarThread.getGame().getParticipant(Killer);
							target.setAbility(Virus.class);
						} catch (Exception ex) {}
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
