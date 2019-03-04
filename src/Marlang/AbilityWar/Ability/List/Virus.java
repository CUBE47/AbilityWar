package Marlang.AbilityWar.Ability.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.PlayerDeathEvent;

import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.GameManager.Object.Participant;
import Marlang.AbilityWar.Utils.Thread.AbilityWarThread;

public class Virus extends AbilityBase {

	public Virus(Participant participant) {
		super(participant, "���̷���", Rank.D,
				ChatColor.translateAlternateColorCodes('&', "&f�� �ɷ��� ����� ���� ������� �Űܰ��ϴ�."));
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
				Player Killer = getPlayer().getKiller();
				if(Killer != null) {
					if(AbilityWarThread.isGameTaskRunning()) {
						this.getParticipant().transferAbility(Killer);
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
