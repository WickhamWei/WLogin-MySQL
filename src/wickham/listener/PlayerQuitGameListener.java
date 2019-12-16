package wickham.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import wickham.main.WLogin;
import wickham.main.login.WLoginSYS;

public class PlayerQuitGameListener implements Listener {

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (WLoginSYS.isLogin(player.getName())) {
			WLoginSYS.unLogin(player);
			event.setQuitMessage("");
			WLogin.main.getServer().broadcastMessage(ChatColor.YELLOW+player.getName()+" 退出了游戏");
			WLogin.main.getLogger().info(event.getPlayer().getName()+" 退出了游戏");
			return;
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerKickEvent event) {
		Player player = event.getPlayer();
		if (WLoginSYS.isLogin(player.getName())) {
			WLoginSYS.unLogin(player);
			event.setLeaveMessage("");
			WLogin.main.getServer().broadcastMessage(ChatColor.YELLOW+player.getName()+" 退出了游戏");
			WLogin.main.getLogger().info(event.getPlayer().getName()+" 退出了游戏");
			return;
		}
	}
}
