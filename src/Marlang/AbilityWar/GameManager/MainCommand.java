package Marlang.AbilityWar.GameManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import Marlang.AbilityWar.AbilityWar;
import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Config.AbilitySettings;
import Marlang.AbilityWar.Config.AbilityWarSettings;
import Marlang.AbilityWar.Config.SettingWizard;
import Marlang.AbilityWar.GameManager.Manager.GUI.AbilityGUI;
import Marlang.AbilityWar.GameManager.Manager.GUI.SpectatorGUI;
import Marlang.AbilityWar.Utils.AbilityWarThread;
import Marlang.AbilityWar.Utils.Messager;
import Marlang.AbilityWar.Utils.NumberUtil;
import Marlang.AbilityWar.Utils.TimerBase;

public class MainCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		parseCommand(sender, label, args);
		return true;
	}
	
	public void parseCommand(CommandSender sender, String label, String[] split) {
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
					Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f������ &e" + sender.getName() + "&f���� ������ �������׽��ϴ�."));
					if(AbilityWarThread.isGameTaskRunning()) {
						TimerBase.StopAllTasks();
						HandlerList.unregisterAll(AbilityWarThread.getGame().getDeathManager());
						Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7������ �ʱ�ȭ�Ǿ����ϴ�."));
						if(AbilityWarThread.isAbilitySelectTaskRunning()) {
							AbilityWarThread.toggleAbilitySelectTask(false);
						}
						AbilityWarThread.toggleGameTask(false);
						AbilityWarThread.setGame(null);
					} else {
						Messager.sendErrorMessage(sender, ChatColor.translateAlternateColorCodes('&', "&c�ɷ��� ������ ����ǰ� ���� �ʽ��ϴ�."));
					}
				} else {
					Messager.sendErrorMessage(sender, ChatColor.translateAlternateColorCodes('&', "&c�� ��ɾ ����Ϸ��� OP ������ �־�� �մϴ�."));
				}
			} else if(split[0].equalsIgnoreCase("reload")) {
				if(sender.isOp()) {
					AbilityWarSettings.Reload();
					AbilitySettings.Reload();
					Messager.sendMessage(sender, ChatColor.translateAlternateColorCodes('&', "&2�ɷ��� ���� ���Ǳ�&a�� ���ε�Ǿ����ϴ�."));
				} else {
					Messager.sendErrorMessage(sender, ChatColor.translateAlternateColorCodes('&', "&c�� ��ɾ ����Ϸ��� OP ������ �־�� �մϴ�."));
				}
			} else if(split[0].equalsIgnoreCase("config")) {
				if(sender instanceof Player) {
					Player p = (Player) sender;
					if(p.isOp()) {
						
						if(split.length > 1) {
							parseConfigCommand(p, label, Messager.removeFirstArg(split));
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
			} else if(split[0].equalsIgnoreCase("skip")) {
				if(sender instanceof Player) {
					Player p = (Player) sender;
					if(p.isOp()) {
						if(AbilityWarThread.isGameTaskRunning()) {
							if(AbilityWarThread.getAbilitySelect() != null) {
								AbilityWarThread.getAbilitySelect().Skip(p.getName());
							} else {
								Messager.sendErrorMessage(p, ChatColor.translateAlternateColorCodes('&', "&c�ɷ��� �����ϴ� ���� �ƴմϴ�."));
							}
						} else {
							Messager.sendErrorMessage(p, ChatColor.translateAlternateColorCodes('&', "&c�ɷ��� ������ ����ǰ� ���� �ʽ��ϴ�."));
						}
					} else {
						Messager.sendErrorMessage(p, ChatColor.translateAlternateColorCodes('&', "&c�� ��ɾ ����Ϸ��� OP ������ �־�� �մϴ�."));
					}
				}
			} else if(split[0].equalsIgnoreCase("abi")) {
				if(sender instanceof Player) {
					Player p = (Player) sender;
					if(AbilityWarThread.isGameTaskRunning()) {
						if(p.isOp()) {
							if(split.length < 2) {
								Messager.sendErrorMessage(p, ChatColor.translateAlternateColorCodes('&', "���� &7: &f/" + label + " abi <���>"));
							} else {
								if(Bukkit.getPlayerExact(split[1]) != null) {
									Player target = Bukkit.getPlayerExact(split[1]);
									if(AbilityWarThread.getGame().getPlayers().contains(target)) {
										AbilityGUI gui = new AbilityGUI(p, target, AbilityWar.getPlugin());
										gui.openAbilitySelectGUI(1);
									} else {
										Messager.sendErrorMessage(p, target.getName() + "���� Ż���߰ų� ���ӿ� �������� �ʾҽ��ϴ�.");
									}
								} else {
									Messager.sendErrorMessage(p, split[1] + "��(��) �������� �ʴ� �÷��̾��Դϴ�.");
								}
							}
						} else {
							Messager.sendErrorMessage(p, ChatColor.translateAlternateColorCodes('&', "&c�� ��ɾ ����Ϸ��� OP ������ �־�� �մϴ�."));
						}
					} else {
						Messager.sendErrorMessage(p, ChatColor.translateAlternateColorCodes('&', "&c�ɷ��� ������ ����ǰ� ���� �ʽ��ϴ�."));
					}
				} else {
					Messager.sendErrorMessage(sender, ChatColor.translateAlternateColorCodes('&', "&c�ֿܼ��� ����� �� ���� ��ɾ��Դϴ�!"));
				}
			} else if(split[0].equalsIgnoreCase("spec")) {
				if(sender instanceof Player) {
					Player p = (Player) sender;
					if(p.isOp()) {
						SpectatorGUI gui = new SpectatorGUI(p, AbilityWar.getPlugin());
						gui.openSpectateGUI(1);
					} else {
						Messager.sendErrorMessage(p, ChatColor.translateAlternateColorCodes('&', "&c�� ��ɾ ����Ϸ��� OP ������ �־�� �մϴ�."));
					}
				} else {
					Messager.sendErrorMessage(sender, ChatColor.translateAlternateColorCodes('&', "&c�ֿܼ��� ����� �� ���� ��ɾ��Դϴ�!"));
				}
			}
			
		}
	}
	
	public void parseConfigCommand(Player p, String label, String[] args) {
		SettingWizard wizard = new SettingWizard(p, AbilityWar.getPlugin());
		if(args[0].equalsIgnoreCase("kit")) {
			wizard.openKitGUI();
		} else if(args[0].equalsIgnoreCase("spawn")) {
			wizard.openSpawnGUI();
		} else if(args[0].equalsIgnoreCase("inv")) {
			wizard.openInvincibilityGUI();
		} else if(args[0].equalsIgnoreCase("game")) {
			wizard.openGameGUI();
		} else if(args[0].equalsIgnoreCase("death")) {
			wizard.openDeathGUI();
		} else {
			if(NumberUtil.isInt(args[0])) {
				sendHelpConfigCommand(p, label, Integer.valueOf(args[0]));
			} else {
				Messager.sendErrorMessage(p, "�������� �ʴ� ���Ǳ��Դϴ�.");
			}
		}
	}
	
	public void sendHelpCommand(CommandSender sender, String label, Integer Page) {
		int AllPage = 3;
		
		switch(Page) {
			case 1:
				Messager.sendStringList(sender, Messager.getStringList(
						Messager.formatTitle(ChatColor.GOLD, ChatColor.YELLOW, "�ɷ��� ����"),
						ChatColor.translateAlternateColorCodes('&', "&b/" + label + " help <������> &7�� �� ���� ��ɾ Ȯ���ϼ���! ( &b" + Page + " ������ &7/ &b" + AllPage + " ������ &7)"),
						Messager.formatCommand(label, "start", "�ɷ��� ������ ���۽�ŵ�ϴ�.", true),
						Messager.formatCommand(label, "stop", "�ɷ��� ������ ������ŵ�ϴ�.", true),
						Messager.formatCommand(label, "check", "�ڽ��� �ɷ��� Ȯ���մϴ�.", false),
						Messager.formatCommand(label, "yes", "�ڽ��� �ɷ��� Ȯ���մϴ�.", false),
						Messager.formatCommand(label, "no", "�ڽ��� �ɷ��� �����մϴ�.", false)));
				break;
			case 2:
				Messager.sendStringList(sender, Messager.getStringList(
						Messager.formatTitle(ChatColor.GOLD, ChatColor.YELLOW, "�ɷ��� ����"),
						ChatColor.translateAlternateColorCodes('&', "&b/" + label + " help <������> &7�� �� ���� ��ɾ Ȯ���ϼ���! ( &b" + Page + " ������ &7/ &b" + AllPage + " ������ &7)"),
						Messager.formatCommand(label, "skip", "��� ������ �ɷ��� ������ Ȯ���մϴ�.", true),
						Messager.formatCommand(label, "reload", "�ɷ��� ���� ���Ǳ׸� ���ε��մϴ�.", true),
						Messager.formatCommand(label, "config", "�ɷ��� ���� ���Ǳ� ��ɾ Ȯ���մϴ�.", true),
						Messager.formatCommand(label, "abi <���>", "��󿡰� �ɷ��� ���Ƿ� �ο��մϴ�.", true),
						Messager.formatCommand(label, "spec", "������ ���� GUI�� ���ϴ�.", true)));
				break;
			case 3:
				Messager.sendStringList(sender, Messager.getStringList(
						Messager.formatTitle(ChatColor.GOLD, ChatColor.YELLOW, "�ɷ��� ����"),
						ChatColor.translateAlternateColorCodes('&', "&b/" + label + " help <������> &7�� �� ���� ��ɾ Ȯ���ϼ���! ( &b" + Page + " ������ &7/ &b" + AllPage + " ������ &7)"),
						Messager.formatCommand(label, "asdf <�̸�>", "��ũ��Ʈ�� �ۼ��մϴ�.", true)
						));
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
				Messager.sendStringList(sender, Messager.getStringList(
						Messager.formatTitle(ChatColor.GOLD, ChatColor.YELLOW, "�ɷ��� ���� ���Ǳ�"),
						ChatColor.translateAlternateColorCodes('&', "&b/" + label + " config <������> &7�� �� ���� ��ɾ Ȯ���ϼ���! ( &b" + Page + " ������ &7/ &b" + AllPage + " ������ &7)"),
						Messager.formatCommand(label + " config", "kit", "�ɷ��� ���� �⺻���� �����մϴ�.", true),
						Messager.formatCommand(label + " config", "spawn", "�ɷ��� ���� ������ �����մϴ�.", true),
						Messager.formatCommand(label + " config", "inv", "�ʹ� ������ �����մϴ�.", true),
						Messager.formatCommand(label + " config", "game", "������ �������� �κе��� �����մϴ�.", true),
						Messager.formatCommand(label + " config", "death", "�÷��̾� ����� ���õ� ���Ǳ׸� �����մϴ�.", true)));
				break;
			default:
				Messager.sendErrorMessage(sender, "�������� �ʴ� �������Դϴ�.");
				break;
		}
	}
	
}
