package moofDoS.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import moofDoS.Main;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class moofDoS implements CommandExecutor {
	Main main;

	/**
	 * Генератор нового объекта этого класса
	 *
	 * @param main
	 *            - главный метод. Праметр служит для получения Handlers,
	 *            перезагрузки конфигурации и логгера
	 */
	public moofDoS(Main main) {
		this.main = main;
	}

	/**
	 * Выполняет команду плагина
	 *
	 * @return true, Всегда
	 * @param sender
	 *            - Отправитель команды
	 * @param command
	 *            - Команда оторая была выполнена
	 * @param label
	 *            - Другие названия команды которые могут использоваться
	 * @param args
	 *            - Аргументы команды
	 * @see org.bukkit.command.CommandExecutor#onCommand(org.bukkit.command.CommandSender,
	 *      org.bukkit.command.Command, java.lang.String, java.lang.String[])
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player p = sender instanceof Player ? (Player) sender : null;

		if (p != null) {
			if (!PermissionsEx.getUser(p).has("moofDoS.commands")) {
				sender.sendMessage(
						Main.prefix + ChatColor.RED + ChatColor.BOLD + "You don't have permission to do that! [1]");
				
				return true;
			}
			if (!p.isOp()) {
				sender.sendMessage(
						Main.prefix + ChatColor.RED + ChatColor.BOLD + "You don't have permission to do that! [2]");
				
				return true;
			}
		}

		if (args.length < 1 || !args[0].equalsIgnoreCase("reload") && !args[0].equalsIgnoreCase("status")
				&& !args[0].equalsIgnoreCase("on") && !args[0].equalsIgnoreCase("off")
				&& !args[0].equalsIgnoreCase("toggle") && !args[0].equalsIgnoreCase("add")
				&& !args[0].equalsIgnoreCase("remove")) {
			sender.sendMessage(Main.prefix + ChatColor.BOLD + ChatColor.GOLD + "Usage: " + ChatColor.RESET
					+ ChatColor.AQUA + "/moofDoS <reload|status|on|off|toggle|add|remove>");
			
			return true;
		}

		if (args[0].equalsIgnoreCase("reload")) {
			try {
				main.reloadCfg();
				sender.sendMessage(Main.prefix + "Successfully reloaded config!");
			} catch (Throwable t) {
				sender.sendMessage(Main.prefix + "Failed to reload config!");
			}
			
			return true;
		}

		if (args[0].equalsIgnoreCase("status")) {
			sender.sendMessage(Main.prefix + "Status of force WhiteList: §4" + (main.candles.forceWhiteList ? "enabled" : "disabled"));
			sender.sendMessage(Main.prefix + "Status of bot WhiteList: §4" + (main.candles.botWhiteList ? "enabled" : "disabled"));
			
			return true;
		}

		if (args[0].equalsIgnoreCase("on")) {
			main.candles.botWhiteList = true;
			main.getConfig().set("moofDoS.forceWhiteList", main.candles.forceWhiteList);
			
			sender.sendMessage(Main.prefix + "WhiteList " + (main.candles.forceWhiteList ? "enabled!" : "disabled."));
		}

		if (args[0].equalsIgnoreCase("off")) {
			main.candles.botWhiteList = false;
			main.getConfig().set("moofDoS.forceWhiteList", main.candles.forceWhiteList);
			
			sender.sendMessage(Main.prefix + "WhiteList " + (main.candles.forceWhiteList ? "enabled!" : "disabled."));
		}

		if (args[0].equalsIgnoreCase("toggle")) {
			main.candles.botWhiteList = !main.candles.botWhiteList;
			main.getConfig().set("moofDoS.forceWhiteList", main.candles.forceWhiteList);
			
			sender.sendMessage(Main.prefix + "WhiteList " + (main.candles.forceWhiteList ? "enabled!" : "disabled."));
		}

		if (args[0].equalsIgnoreCase("add")) {
			String ip = args[1];
			if (args.length != 3)
				if (main.candles.ips.contains(ip))
					sender.sendMessage(Main.prefix + "IP exists!");
				else {
					main.candles.ips.add(ip);
					main.getConfig().set("moofDoS.ips", main.candles.ips);
					
					sender.sendMessage(Main.prefix + "IP added.");
				}
			else {
				sender.sendMessage(Main.prefix + ChatColor.BOLD + ChatColor.GOLD + "Usage: " + ChatColor.RESET
						+ ChatColor.AQUA + "/moofDoS add <IP>");
				
				return true;
			}
		}

		if (args[0].equalsIgnoreCase("remove")) {
			String ip = args[1];
			if (args.length != 3) {
				if (main.candles.ips.contains(ip)) {
					main.candles.ips.remove(ip);
					main.getConfig().set("moofDoS.ips", main.candles.ips);
					
					sender.sendMessage(Main.prefix + "IP removed.");
				} else
					sender.sendMessage(Main.prefix + "IP not exists!");
			} else {
				sender.sendMessage(Main.prefix + ChatColor.BOLD + ChatColor.GOLD + "Usage: " + ChatColor.RESET
						+ ChatColor.AQUA + "/moofDoS remove <IP>");
				return true;
			}
		}
		main.saveConfig();
		return true;
	}
}
