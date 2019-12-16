package wickham.listener;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import wickham.main.WLogin;
import wickham.main.login.WLoginSYS;

public abstract class TeenagersListener {
	public static void enableTeenagersListener() {
		BukkitRunnable bukkitRunnable=new BukkitRunnable() {
			
			@Override
			public void run() {
				// TODO 自动生成的方法存根
				for(Player player:WLogin.main.getServer().getOnlinePlayers()) {
					if(WLoginSYS.teenageersChecker(player)) {
						return;
					}else {
						player.kickPlayer(WLogin.kickTeenagersMsg());
					}
				}
			}
		};
		bukkitRunnable.runTaskTimer(WLogin.main, 0, 20*60*5);
	}
}
