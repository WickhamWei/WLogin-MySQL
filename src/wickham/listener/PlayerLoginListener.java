package wickham.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import wickham.event.WPlayerLoginEvent;
import wickham.main.WLogin;

public class PlayerLoginListener implements Listener {

	@EventHandler
	public void listen(WPlayerLoginEvent event) {// 玩家加入
		Player player = event.getPlayer();
		WLogin.main.getServer().broadcastMessage(ChatColor.YELLOW + player.getName() + ChatColor.GREEN + " 加入了游戏");
		WLogin.main.getLogger().info(player.getName() + " 加入了游戏");
		player.sendMessage(ChatColor.YELLOW + "在线的玩家有 " + ChatColor.GREEN
				+ WLogin.main.getServer().getOnlinePlayers().size() + ChatColor.YELLOW + " 个");
		return;
	}
}
