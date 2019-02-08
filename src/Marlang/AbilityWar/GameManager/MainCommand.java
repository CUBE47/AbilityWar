package Marlang.AbilityWar.GameManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import Marlang.AbilityWar.AbilityWar;
import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Ability.Timer.CooldownTimer;
import Marlang.AbilityWar.Config.AbilitySettings;
import Marlang.AbilityWar.Config.AbilityWarSettings;
import Marlang.AbilityWar.Config.SettingWizard;
import Marlang.AbilityWar.GameManager.Manager.GUI.AbilityGUI;
import Marlang.AbilityWar.GameManager.Manager.GUI.BlackListGUI;
import Marlang.AbilityWar.GameManager.Manager.GUI.SpecialThanksGUI;
import Marlang.AbilityWar.GameManager.Manager.GUI.SpectatorGUI;
import Marlang.AbilityWar.Utils.AbilityWarThread;
import Marlang.AbilityWar.Utils.Messager;
import Marlang.AbilityWar.Utils.NumberUtil;
import Marlang.AbilityWar.Utils.TimerBase;

public class MainCommand implements CommandExecutor, TabCompleter {
	
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
					if(AbilityWarThread.isGameTaskRunning()) {
						Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f������ &e" + sender.getName() + "&f���� ������ �������׽��ϴ�."));
						TimerBase.ResetTasks();
						
						HandlerList.unregisterAll(AbilityWarThread.getGame().getDeathManager());
						
						AbilityWarThread.toggleGameTask(false);
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
					AbilityWarSettings.Refresh();
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
							AbilitySelect select = AbilityWarThread.getGame().getAbilitySelect();
							if(select != null && !select.isAbilitySelectFinished()) {
								if(!select.getAbilitySelect(p)) {
									select.decideAbility(p, true);
								} else {
									Messager.sendMessage(p, ChatColor.translateAlternateColorCodes('&', "&c�̹� �ɷ� ������ ��ġ�̽��ϴ�."));
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
							AbilitySelect select = AbilityWarThread.getGame().getAbilitySelect();
							if(select != null && !select.isAbilitySelectFinished()) {
								if(!select.getAbilitySelect(p)) {
									select.changeAbility(p);
								} else {
									Messager.sendMessage(p, ChatColor.translateAlternateColorCodes('&', "&c�̹� �ɷ� ������ ��ġ�̽��ϴ�."));
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
				if(sender.isOp()) {
					if(AbilityWarThread.isGameTaskRunning()) {
						AbilitySelect select = AbilityWarThread.getGame().getAbilitySelect();
						if(select != null && !select.isAbilitySelectFinished()) {
							select.Skip(sender.getName());
						} else {
							Messager.sendErrorMessage(sender, ChatColor.translateAlternateColorCodes('&', "&c�ɷ��� �����ϴ� ���� �ƴմϴ�."));
						}
					} else {
						Messager.sendErrorMessage(sender, ChatColor.translateAlternateColorCodes('&', "&c�ɷ��� ������ ����ǰ� ���� �ʽ��ϴ�."));
					}
				} else {
					Messager.sendErrorMessage(sender, ChatColor.translateAlternateColorCodes('&', "&c�� ��ɾ ����Ϸ��� OP ������ �־�� �մϴ�."));
				}
			} else if(split[0].equalsIgnoreCase("util")) {
				if(sender instanceof Player) {
					Player p = (Player) sender;
					if(p.isOp()) {
						
						if(split.length > 1) {
							parseUtilCommand(p, label, Messager.removeFirstArg(split));
						} else {
							sendHelpUtilCommand(p, label, 1);
						}
					} else {
						Messager.sendErrorMessage(p, ChatColor.translateAlternateColorCodes('&', "&c�� ��ɾ ����Ϸ��� OP ������ �־�� �մϴ�."));
					}
				} else {
					Messager.sendErrorMessage(sender, ChatColor.translateAlternateColorCodes('&', "&c�ֿܼ��� ����� �� ���� ��ɾ��Դϴ�!"));
				}
			} else if(split[0].equalsIgnoreCase("specialthanks")) {
				if(sender instanceof Player) {
					Player p = (Player) sender;
					SpecialThanksGUI gui = new SpecialThanksGUI(p, AbilityWar.getPlugin());
					gui.openGUI(1);
				} else {
					Messager.sendErrorMessage(sender, ChatColor.translateAlternateColorCodes('&', "&c�ֿܼ��� ����� �� ���� ��ɾ��Դϴ�!"));
				}
			} else {
				Messager.sendErrorMessage(sender, ChatColor.translateAlternateColorCodes('&', "�������� �ʴ� ���� ��ɾ��Դϴ�."));
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
				Messager.sendErrorMessage(p, "�������� �ʴ� ��ƿ�Դϴ�.");
			}
		}
	}

	public void parseUtilCommand(Player p, String label, String[] args) {
		if(args[0].equalsIgnoreCase("abi")) {
			if(AbilityWarThread.isGameTaskRunning()) {
				if(args.length < 2) {
					Messager.sendErrorMessage(p, ChatColor.translateAlternateColorCodes('&', "���� &7: &f/" + label + " util abi <���>"));
				} else {
					if(Bukkit.getPlayerExact(args[1]) != null) {
						Player target = Bukkit.getPlayerExact(args[1]);
						if(AbilityWarThread.getGame().getPlayers().contains(target)) {
							AbilityGUI gui = new AbilityGUI(p, target, AbilityWar.getPlugin());
							gui.openAbilitySelectGUI(1);
						} else {
							Messager.sendErrorMessage(p, target.getName() + "���� Ż���߰ų� ���ӿ� �������� �ʾҽ��ϴ�.");
						}
					} else {
						Messager.sendErrorMessage(p, args[1] + "��(��) �������� �ʴ� �÷��̾��Դϴ�.");
					}
				}
			} else {
				Messager.sendErrorMessage(p, ChatColor.translateAlternateColorCodes('&', "&c�ɷ��� ������ ����ǰ� ���� �ʽ��ϴ�."));
			}
		} else if(args[0].equalsIgnoreCase("spec")) {
			SpectatorGUI gui = new SpectatorGUI(p, AbilityWar.getPlugin());
			gui.openSpectateGUI(1);
		} else if(args[0].equalsIgnoreCase("ablist")) {
			if(AbilityWarThread.isGameTaskRunning()) {
				ArrayList<String> msg = new ArrayList<String>();
				msg.add(ChatColor.translateAlternateColorCodes('&', "&2===== &a�ɷ��� ��� &2====="));

				Integer Count = 0;
				for(Player player : AbilityWarThread.getGame().getAbilities().keySet()) {
					Count++;
					AbilityBase Ability = AbilityWarThread.getGame().getAbilities().get(player);
					msg.add(ChatColor.translateAlternateColorCodes('&', "&e" + Count + ". &f" + player.getName() + " &7: &c" + Ability.getAbilityName()));
				}
				
				if(Count.equals(0)) {
					msg.add(ChatColor.translateAlternateColorCodes('&', "&f�ɷ��ڰ� �߰ߵ��� �ʾҽ��ϴ�."));
				}
				
				msg.add(ChatColor.translateAlternateColorCodes('&', "&2========================"));
				
				Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f" + p.getName() + "&a���� �÷��̾���� �ɷ��� Ȯ���Ͽ����ϴ�."));
				
				for(String m : msg) {
					Messager.sendMessage(p, m);
				}
			} else {
				Messager.sendErrorMessage(p, ChatColor.translateAlternateColorCodes('&', "&c�ɷ��� ������ ����ǰ� ���� �ʽ��ϴ�."));
			}
		} else if(args[0].equalsIgnoreCase("blacklist")) {
			BlackListGUI gui = new BlackListGUI(p, AbilityWar.getPlugin());
			gui.openBlackListGUI(1);
		} else if(args[0].equalsIgnoreCase("resetcool")) {
			if(AbilityWarThread.isGameTaskRunning()) {
				CooldownTimer.CoolReset();
				Messager.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f" + p.getName() + "&a���� �÷��̾���� �ɷ� ��Ÿ���� �ʱ�ȭ�Ͽ����ϴ�."));
			} else {
				Messager.sendErrorMessage(p, ChatColor.translateAlternateColorCodes('&', "&c�ɷ��� ������ ����ǰ� ���� �ʽ��ϴ�."));
			}
		} else {
			if(NumberUtil.isInt(args[0])) {
				sendHelpConfigCommand(p, label, Integer.valueOf(args[0]));
			} else {
				Messager.sendErrorMessage(p, "�������� �ʴ� ���Ǳ��Դϴ�.");
			}
		}
	}
	
	public void sendHelpCommand(CommandSender sender, String label, Integer Page) {
		int AllPage = 2;
		
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
						Messager.formatCommand(label, "util", "�ɷ��� ���� ��ƿ ��ɾ Ȯ���մϴ�.", true),
						Messager.formatCommand(label, "specialthanks", "�ɷ��� ���� �÷����ο� �⿩�� ������� Ȯ���մϴ�.", false)));
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

	public void sendHelpUtilCommand(CommandSender sender, String label, Integer Page) {
		int AllPage = 1;
		
		switch(Page) {
			case 1:
				Messager.sendStringList(sender, Messager.getStringList(
						Messager.formatTitle(ChatColor.GOLD, ChatColor.YELLOW, "�ɷ��� ���� ��ƿ"),
						ChatColor.translateAlternateColorCodes('&', "&b/" + label + " util <������> &7�� �� ���� ��ɾ Ȯ���ϼ���! ( &b" + Page + " ������ &7/ &b" + AllPage + " ������ &7)"),
						Messager.formatCommand(label + " util", "abi <���>", "��󿡰� �ɷ��� ���Ƿ� �ο��մϴ�.", true),
						Messager.formatCommand(label + " util", "spec", "������ ���� GUI�� ���ϴ�.", true),
						Messager.formatCommand(label + " util", "ablist", "�ɷ��� ����� Ȯ���մϴ�.", true),
						Messager.formatCommand(label + " util", "blacklist", "�ɷ� ������Ʈ ���� GUI�� ���ϴ�.", true),
						Messager.formatCommand(label + " util", "resetcool", "�÷��̾���� �ɷ� ��Ÿ���� �ʱ�ȭ��ŵ�ϴ�.", true)));
				break;
			default:
				Messager.sendErrorMessage(sender, "�������� �ʴ� �������Դϴ�.");
				break;
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		return parseTabComplete(sender, label, args);
	}
	
	public List<String> parseTabComplete(CommandSender sender, String label, String[] args) {
		if(label.equalsIgnoreCase("abilitywar") || label.equalsIgnoreCase("ability")
		|| label.equalsIgnoreCase("aw") || label.equalsIgnoreCase("va")) {
			switch(args.length) {
				case 1:
					ArrayList<String> Complete = Messager.getStringList(
							"start", "stop", "check", "yes", "no",
							"skip", "reload", "config", "util", "specialthanks");
					
					if(args[0].isEmpty()) {
						return Complete;
					} else {
						return Complete.stream().filter(s -> s.startsWith(args[0])).collect(Collectors.toList());
					}
				case 2:
					if(args[0].equalsIgnoreCase("config")) {
						ArrayList<String> Config = Messager.getStringList(
								"kit", "spawn", "inv", "game", "death");
						if(args[1].isEmpty()) {
							return Config;
						} else {
							return Config.stream().filter(s -> s.startsWith(args[1])).collect(Collectors.toList());
						}
					} else if(args[0].equalsIgnoreCase("util")) {
						ArrayList<String> Util = Messager.getStringList(
								"abi", "spec", "ablist", "blacklist", "resetcool");
						if(args[1].isEmpty()) {
							return Util;
						} else {
							return Util.stream().filter(s -> s.startsWith(args[1])).collect(Collectors.toList());
						}
					}
				case 3:
					if(args[0].equalsIgnoreCase("util") && args[1].equalsIgnoreCase("abi")) {
						ArrayList<String> Players = new ArrayList<String>();
						for(Player p : Bukkit.getOnlinePlayers()) Players.add(p.getName());
						Players.sort(new Comparator<String>() {
							
							public int compare(String obj1, String obj2) {
								return obj1.compareToIgnoreCase(obj2);
							}
							
						});
						

						if(args[2].isEmpty()) {
							return Players;
						} else {
							return Players.stream().filter(s -> s.startsWith(args[2])).collect(Collectors.toList());
						}
					}
					
			}
		}

		return Messager.getStringList();
	}
	
}
