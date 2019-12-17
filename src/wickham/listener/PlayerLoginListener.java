package wickham.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import wickham.event.WPlayerLoginEvent;
import wickham.main.WLogin;
import wickham.main.login.WLoginSYS;

public class PlayerLoginListener implements Listener {

	@EventHandler
	public void listen(WPlayerLoginEvent event) {// 玩家加入
		Player player = event.getPlayer();
		if(!WLoginSYS.teenageersChecker(player)) {
			player.kickPlayer(WLogin.kickTeenagersMsg());
			event.setCancelled(true);
			return;
		}
		if(WLoginSYS.isBanning(player.getName())) {
			player.kickPlayer("您已被封禁，封禁理由是 "+WLoginSYS.getMaxBanTimeReason(player.getName())+" 封禁时间剩余 "+WLoginSYS.getMaxBanTimeMin(player.getName())+" 分钟");
			event.setCancelled(true);
			return;
		}
		WLogin.main.getServer().broadcastMessage(ChatColor.YELLOW + player.getName() + ChatColor.GREEN + " 加入了游戏");
		WLogin.main.getLogger().info(player.getName() + " 加入了游戏");
		player.sendMessage(ChatColor.YELLOW + "在线的玩家有 " + ChatColor.GREEN
				+ WLogin.main.getServer().getOnlinePlayers().size() + ChatColor.YELLOW + " 个");
		int todayMin=WLoginSYS.getPlayerTodayPlayingTime(player.getName());
		int hour=todayMin/60;
		int min=todayMin%60;
		player.sendMessage(ChatColor.YELLOW + "您今天游戏了 "+ChatColor.GREEN+hour+ChatColor.YELLOW+" 小时 "+ChatColor.GREEN+min+ChatColor.YELLOW+" 分钟");
		if(hour>=3) {
			player.sendMessage(ChatColor.YELLOW+"请注意休息");
		}
		BukkitRunnable bukkitRunnable=new BukkitRunnable() {
			
			@Override
			public void run() {
				// TODO 自动生成的方法存根
				WLoginSYS.checkLastLoginDataNormal(player);
			}
		};
		bukkitRunnable.runTaskLater(WLogin.main, 40);
	}
}
