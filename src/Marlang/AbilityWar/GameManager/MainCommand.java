package Marlang.AbilityWar.GameManager;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import Marlang.AbilityWar.AbilityWar;
import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Config.SettingWizard;
import Marlang.AbilityWar.Utils.AbilityWarThread;
import Marlang.AbilityWar.Utils.NumberUtil;
import Marlang.AbilityWar.Utils.Messager;

public class MainCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		return parseCommand(sender, label, args);
	}
	
	public boolean parseCommand(CommandSender sender, String label, String[] split) {
		if(split.length == 0) {
			sendHelpCommand(sender, label, 1);
		} else {
			if(split[0].equalsIgnoreCase("help")) {
				if(split.length > 1) {
					if(NumberUtil.isInt(split[1])) {
						sendHelpCommand(sender, label, Integer.valueOf(split[1]));
					} else {
						Messager.sendErrorMessage(sender, "�������� �ʴ� �������Դϴ�.");
					}
				} else {
					sendHelpCommand(sender, label, 1);
				}
			} else if(split[0].equalsIgnoreCase("start")) {
				if(sender.isOp()) {
					if(!AbilityWarThread.isGameTaskRunning()) {
						AbilityWarThread.toggleGameTask(true);
						Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f������ &e" + sender.getName() + "&f���� ������ ���۽��׽��ϴ�."));
					} else {
						Messager.sendErrorMessage(sender, ChatColor.translateAlternateColorCodes('&', "&c�ɷ��� ������ �̹� ����ǰ� �ֽ��ϴ�."));
					}
				} else {
					Messager.sendErrorMessage(sender, ChatColor.translateAlternateColorCodes('&', "&c�� ��ɾ ����Ϸ��� OP ������ �־�� �մϴ�."));
				}
			} else if(split[0].equalsIgnoreCase("stop")) {
				if(sender.isOp()) {
					if(AbilityWarThread.isGameTaskRunning()) {
						AbilityWarThread.toggleGameTask(false);
						Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f������ &e" + sender.getName() + "&f���� ������ �������׽��ϴ�."));
						AbilityWarThread.setGame(null);
						Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7������ �ʱ�ȭ�Ǿ����ϴ�."));
					} else {
						Messager.sendErrorMessage(sender, ChatColor.translateAlternateColorCodes('&', "&c�ɷ��� ������ ����ǰ� ���� �ʽ��ϴ�."));
					}
				} else {
					Messager.sendErrorMessage(sender, ChatColor.translateAlternateColorCodes('&', "&c�� ��ɾ ����Ϸ��� OP ������ �־�� �մϴ�."));
				}
			} else if(split[0].equalsIgnoreCase("reload")) {
				if(sender.isOp()) {
					AbilityWar.getSetting().Reload();
					Messager.sendMessage(sender, ChatColor.translateAlternateColorCodes('&', "&2�ɷ��� ���� ���Ǳװ� ���ε�Ǿ����ϴ�."));
				} else {
					Messager.sendErrorMessage(sender, ChatColor.translateAlternateColorCodes('&', "&c�� ��ɾ ����Ϸ��� OP ������ �־�� �մϴ�."));
				}
			} else if(split[0].equalsIgnoreCase("config")) {
				if(sender instanceof Player) {
					Player p = (Player) sender;
					if(p.isOp()) {
						
						if(split.length > 1) {
							if(split[1].equalsIgnoreCase("kit")) {
								SettingWizard.openKitGUI(p);
							} else {
								if(NumberUtil.isInt(split[1])) {
									sendHelpConfigCommand(p, label, Integer.valueOf(split[1]));
								} else {
									Messager.sendErrorMessage(p, "�������� �ʴ� ���Ǳ��Դϴ�.");
								}
							}
						} else {
							sendHelpConfigCommand(p, label, 1);
						}
					} else {
						Messager.sendErrorMessage(p, ChatColor.translateAlternateColorCodes('&', "&c�� ��ɾ ����Ϸ��� OP ������ �־�� �մϴ�."));
					}
				} else {
					Messager.sendErrorMessage(sender, ChatColor.translateAlternateColorCodes('&', "&c�ֿܼ��� ����� �� ���� ��ɾ��Դϴ�!"));
				}
			} else if(split[0].equalsIgnoreCase("check")) {
				if(sender instanceof Player) {
					Player p = (Player) sender;
					if(AbilityWarThread.isGameTaskRunning()) {
						if(AbilityWarThread.getGame().getAbilities().containsKey(p)) {
							AbilityBase Ability = AbilityWarThread.getGame().getAbilities().get(p);
							Messager.sendStringList(p, Messager.formatAbility(Ability));
						} else {
							Messager.sendErrorMessage(sender, ChatColor.translateAlternateColorCodes('&', "&c��ſ��� �ɷ��� �Ҵ���� �ʾҽ��ϴ�."));
						}
					} else {
						Messager.sendErrorMessage(sender, ChatColor.translateAlternateColorCodes('&', "&c�ɷ��� ������ ����ǰ� ���� �ʽ��ϴ�."));
					}
				} else {
					Messager.sendErrorMessage(sender, ChatColor.translateAlternateColorCodes('&', "&c�ֿܼ��� ����� �� ���� ��ɾ��Դϴ�!"));
				}
			} else if(split[0].equalsIgnoreCase("yes")) {
				if(sender instanceof Player) {
					Player p = (Player) sender;
					if(AbilityWarThread.isGameTaskRunning()) {
						if(AbilityWarThread.getGame().getAbilities().containsKey(p)) {
							if(AbilityWarThread.getAbilitySelect() != null) {
								if(!AbilityWarThread.getAbilitySelect().getAbilitySelect(p)) {
									AbilityWarThread.getAbilitySelect().decideAbility(p, true);
								} else {
									p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c�̹� �ɷ� ������ ��ġ�̽��ϴ�."));
								}
							} else {
								Messager.sendErrorMessage(sender, ChatColor.translateAlternateColorCodes('&', "&c�ɷ��� �����ϴ� ���� �ƴմϴ�."));
							}
						} else {
							Messager.sendErrorMessage(sender, ChatColor.translateAlternateColorCodes('&', "&c��ſ��� �ɷ��� �Ҵ���� �ʾҽ��ϴ�."));
						}
					} else {
						Messager.sendErrorMessage(sender, ChatColor.translateAlternateColorCodes('&', "&c�ɷ��� ������ ����ǰ� ���� �ʽ��ϴ�."));
					}
				} else {
					Messager.sendErrorMessage(sender, ChatColor.translateAlternateColorCodes('&', "&c�ֿܼ��� ����� �� ���� ��ɾ��Դϴ�!"));
				}
			} else if(split[0].equalsIgnoreCase("no")) {
				if(sender instanceof Player) {
					Player p = (Player) sender;
					if(AbilityWarThread.isGameTaskRunning()) {
						if(AbilityWarThread.getGame().getAbilities().containsKey(p)) {
							if(AbilityWarThread.getAbilitySelect() != null) {
								if(!AbilityWarThread.getAbilitySelect().getAbilitySelect(p)) {
									AbilityWarThread.getAbilitySelect().changeAbility(p);
								} else {
									p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c�̹� �ɷ� ������ ��ġ�̽��ϴ�."));
								}
							} else {
								Messager.sendErrorMessage(sender, ChatColor.translateAlternateColorCodes('&', "&c�ɷ��� �����ϴ� ���� �ƴմϴ�."));
							}
						} else {
							Messager.sendErrorMessage(sender, ChatColor.translateAlternateColorCodes('&', "&c��ſ��� �ɷ��� �Ҵ���� �ʾҽ��ϴ�."));
						}
					} else {
						Messager.sendErrorMessage(sender, ChatColor.translateAlternateColorCodes('&', "&c�ɷ��� ������ ����ǰ� ���� �ʽ��ϴ�."));
					}
				} else {
					Messager.sendErrorMessage(sender, ChatColor.translateAlternateColorCodes('&', "&c�ֿܼ��� ����� �� ���� ��ɾ��Դϴ�!"));
				}
			}
		}
		
		return true;
	}

	public void sendHelpCommand(CommandSender sender, String label, Integer Page) {
		int AllPage = 2;
		
		switch(Page) {
			case 1:
				Messager.sendStringList(sender, Messager.getArrayList(
						Messager.formatTitle(ChatColor.GOLD, ChatColor.YELLOW, "�ɷ��� ����"),
						ChatColor.translateAlternateColorCodes('&', "&b/" + label + " help <������> &7�� �� ���� ��ɾ Ȯ���ϼ���! ( &b" + Page + " ������ &7/ &b" + AllPage + " ������ &7)"),
						Messager.formatCommand(label, "start", "�ɷ��� ������ ���۽�ŵ�ϴ�.", true),
						Messager.formatCommand(label, "stop", "�ɷ��� ������ ������ŵ�ϴ�.", true),
						Messager.formatCommand(label, "check", "�ڽ��� �ɷ��� Ȯ���մϴ�.", false),
						Messager.formatCommand(label, "yes", "�ڽ��� �ɷ��� Ȯ���մϴ�.", false),
						Messager.formatCommand(label, "no", "�ڽ��� �ɷ��� �����մϴ�.", false)));
				break;
			case 2:
				Messager.sendStringList(sender, Messager.getArrayList(
						Messager.formatTitle(ChatColor.GOLD, ChatColor.YELLOW, "�ɷ��� ����"),
						ChatColor.translateAlternateColorCodes('&', "&b/" + label + " help <������> &7�� �� ���� ��ɾ Ȯ���ϼ���! ( &b" + Page + " ������ &7/ &b" + AllPage + " ������ &7)"),
						Messager.formatCommand(label, "reload", "�ɷ��� ������ ���ε��մϴ�.", true),
						Messager.formatCommand(label, "config", "�ɷ��� ���� ���Ǳ� ��ɾ Ȯ���մϴ�.", true)));
				break;
			default:
				Messager.sendErrorMessage(sender, "�������� �ʴ� �������Դϴ�.");
				break;
		}
	}

	public void sendHelpConfigCommand(CommandSender sender, String label, Integer Page) {
		int AllPage = 1;
		
		switch(Page) {
			case 1:
				Messager.sendStringList(sender, Messager.getArrayList(
						Messager.formatTitle(ChatColor.GOLD, ChatColor.YELLOW, "�ɷ��� ���� ���Ǳ�"),
						ChatColor.translateAlternateColorCodes('&', "&b/" + label + " config <������> &7�� �� ���� ��ɾ Ȯ���ϼ���! ( &b" + Page + " ������ &7/ &b" + AllPage + " ������ &7)"),
						Messager.formatCommand(label + " config", "kit", "�ɷ��� ���� �⺻���� �����մϴ�.", true)));
				break;
			default:
				Messager.sendErrorMessage(sender, "�������� �ʴ� �������Դϴ�.");
				break;
		}
	}
	
}
