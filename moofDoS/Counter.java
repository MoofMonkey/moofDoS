package moofDoS;

import org.bukkit.Bukkit;

public class Counter implements Runnable {
	public static double seconds = 5;
	public static long ticks = Math.round(seconds * 20);
	
	public static int maxPPS = 3;
	public static int c = 0;
	public static int taskNum;
	
	public static Main main;
	
	
	public Counter(Main main) {
		Counter.main = main;
	}
	
	@Override
	public void run() {
		if(!main.candles.forceWhiteList) {
			if((c / seconds) > maxPPS)
				main.candles.botWhiteList = true;
			else
				main.candles.botWhiteList = false;
			
			main.getConfig().set("moofDoS.botWhiteList", main.candles.botWhiteList);
		}
		
		c = 0;
	}
	
	public void start() {
		if(taskNum == -1)
			taskNum = Bukkit.getScheduler().scheduleSyncDelayedTask(main, this, ticks);
	}
	
	public static void stop() {
		if(taskNum != -1) {
			Bukkit.getScheduler().cancelTask(taskNum);
		
			taskNum = -1;
		}
	}
}
