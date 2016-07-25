package moofDoS;

import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.server.ServerListPingEvent;

public class Handlers implements Listener {
	Main main;
	public List<String> ips;
	public boolean botWhiteList = false;
	public boolean forceWhiteList = false;
	public String blockedMsg = "§rWhite§3List §4ON§r. §6Your IP is §5%0";
	public String onJoinMsg = "§rWhite§3List §5enabled§r.\n§4Only §5custom IPs §4can §3access §4to the server.";

	/**
	 * Генератор нового объекта этого класса
	 *
	 * @param main
	 *            - главный класс. Служит для получения пути к папке плагина.
	 */
	public Handlers(Main main) {
		this.main = main;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void PlayerLoginEvent(PlayerLoginEvent event) {
		String playerIP = event.getAddress().toString().replaceAll("/", "");
		
		if((botWhiteList || forceWhiteList) && !ips.contains(playerIP))
			event.disallow(Result.KICK_FULL, Utils.format(onJoinMsg, new String[] { playerIP.replaceAll("-", "\\.") }));
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void ServerListPing(ServerListPingEvent event) {
		String playerIP = event.getAddress().toString().replaceAll("/", "");
		
		if ((botWhiteList || forceWhiteList) && !ips.contains(playerIP)) {
			event.setMotd(Utils.format(blockedMsg, new String[] { playerIP }));
			event.setMaxPlayers(-999999999);
		}
	}
}
