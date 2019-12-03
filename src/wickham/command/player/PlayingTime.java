package wickham.command.player;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import wickham.main.WLogin;
import wickham.main.login.WLoginSYS;

public class PlayingTime implements CommandExecutor{
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length == 0) {
				String playerNameString=player.getName();
				BukkitRunnable bukkitRunnable=new BukkitRunnable() {
					
					@Override
					public void run() {
						// TODO 自动生成的方法存根
						int nowMin=WLoginSYS.getPlayerNowPlayingTime(player);
						int nowTheHour=nowMin/60;
						int nowTheMin=nowMin%60;
						player.sendMessage(ChatColor.YELLOW + "您本次游戏了 "+ChatColor.GREEN+nowTheHour+ChatColor.YELLOW+" 小时 "+ChatColor.GREEN+nowTheMin+ChatColor.YELLOW+" 分钟");
						nowMin=nowMin+WLoginSYS.getPlayerDataBasePlayingTime(playerNameString);
						nowTheHour=nowMin/60;
						nowTheMin=nowMin%60;
						player.sendMessage(ChatColor.YELLOW + "您总共游戏了 "+ChatColor.GREEN+nowTheHour+ChatColor.YELLOW+" 小时 "+ChatColor.GREEN+nowTheMin+ChatColor.YELLOW+" 分钟");
					}
				};
				bukkitRunnable.runTaskAsynchronously(WLogin.main);
				return true;
			}
		}
		return false;
	}
}
