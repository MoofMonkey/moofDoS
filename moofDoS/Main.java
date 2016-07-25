package moofDoS;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import moofDoS.commands.moofDoS;

public class Main extends JavaPlugin {
	public static String prefix = ChatColor.RED + "" + ChatColor.BOLD + "[" + ChatColor.RESET + ChatColor.GOLD
			+ "moofDoS" + ChatColor.RED + ChatColor.BOLD + "] " + ChatColor.RESET;

	public Handlers candles;
	public Counter counter;

	/**
	 * Создаёт новый конфигурационный файл плагина
	 */
	public void newConfig() {
		try {
			saveDefaultConfig();
			reloadConfig();
		} catch (Throwable t) {
			t.printStackTrace();
		}
		getConfig().addDefault("moofDoS.ips", new ArrayList<String>());
		getConfig().addDefault("moofDoS.botWhiteList", false);
		getConfig().addDefault("moofDoS.forceWhiteList", false);
		getConfig().addDefault("moofDoS.blockedMsg", "=rWhite=3List =4ON=r. =6Your IP is =5%0");
		getConfig().addDefault("moofDoS.onJoinMsg", "=rWhite=3List =5enabled=r.\\n=4Only =5custom IPs =4can =3access =4to the server.");
		getConfig().addDefault("moofDoS.maxPlayerPerSecond", "3");
		getConfig().options().copyDefaults(true);
		reloadConfig();
	}

	@Override
	public void onDisable() {
		saveConfig();
		
		Counter.stop();
	}

	/**
	 * Используется при включении сервера/плагина
	 */
	@Override
	public void onEnable() {
		candles = new Handlers(this);
		
		Bukkit.getPluginManager().registerEvents(candles, this);
		getCommand("moofDoS").setExecutor(new moofDoS(this));
		
		try {
			reloadCfg();
			System.out.println("[moofDoS] Successfully loaded config!");
		} catch (Throwable t) {
			new Throwable("[moofDoS] Failed to load config!", t).printStackTrace();
		}
		
		counter.start();
	}

	public void reloadCfg() {
		if (!new File(getDataFolder() + File.separator + "config.yml").exists())
			newConfig();
		
		reloadConfig();
		
		candles.ips = getConfig().getStringList("moofDoS.ips");
		candles.botWhiteList = getConfig().getBoolean("moofDoS.botWhiteList");
		candles.forceWhiteList = getConfig().getBoolean("moofDoS.forceWhiteList");
		candles.blockedMsg = getConfig().getString("moofDoS.blockedMsg").replaceAll("=", "§");
		candles.onJoinMsg = getConfig().getString("moofDoS.onJoinMsg")
				.replaceAll("CSN", "§").replaceAll("\\\\n", "\n");
		
		Counter.maxPPS = getConfig().getInt("moofDoS.maxPlayerPerSecond");
		counter = new Counter(this);
	}
}
