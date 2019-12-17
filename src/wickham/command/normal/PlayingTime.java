package wickham.command.normal;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import wickham.main.WLogin;
import wickham.main.login.WLoginSYS;

public class PlayingTime implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length == 0) {
				String playerNameString = player.getName();
				BukkitRunnable bukkitRunnable = new BukkitRunnable() {

					@Override
					public void run() {
						// TODO 自动生成的方法存根
						int nowMin = WLoginSYS.getPlayerNowPlayingTime(player);
						int nowTheHour = nowMin / 60;
						int nowTheMin = nowMin % 60;
						player.sendMessage(ChatColor.GREEN + playerNameString + ChatColor.YELLOW + " 本次游戏了 "
								+ ChatColor.GREEN + nowTheHour + ChatColor.YELLOW + " 小时 " + ChatColor.GREEN + nowTheMin
								+ ChatColor.YELLOW + " 分钟");
						int todayMin = nowMin + WLoginSYS.getPlayerTodayPlayingTime(playerNameString);
						nowTheHour = todayMin / 60;
						nowTheMin = todayMin % 60;
						player.sendMessage(ChatColor.GREEN + playerNameString + ChatColor.YELLOW + " 今天游戏了 "
								+ ChatColor.GREEN + nowTheHour + ChatColor.YELLOW + " 小时 " + ChatColor.GREEN + nowTheMin
								+ ChatColor.YELLOW + " 分钟");
						int historyMin = nowMin + WLoginSYS.getPlayerDataBasePlayingTime(playerNameString);
						nowTheHour = historyMin / 60;
						nowTheMin = historyMin % 60;
						player.sendMessage(ChatColor.GREEN + playerNameString + ChatColor.YELLOW + " 总共游戏了 "
								+ ChatColor.GREEN + nowTheHour + ChatColor.YELLOW + " 小时 " + ChatColor.GREEN + nowTheMin
								+ ChatColor.YELLOW + " 分钟");
					}
				};
				bukkitRunnable.runTaskAsynchronously(WLogin.main);
				return true;
			} else if (args.length == 1 && player.isOp()) {
				String targePlayerNameString = args[0];
				BukkitRunnable bukkitRunnable = new BukkitRunnable() {

					@Override
					public void run() {
						Player targePlayer=WLogin.main.getServer().getPlayer(targePlayerNameString);
						// TODO 自动生成的方法存根
						int nowMin =0;
						if(!(targePlayer==null)) {
							nowMin = WLoginSYS.getPlayerNowPlayingTime(targePlayer);
						}
						int nowTheHour = nowMin / 60;
						int nowTheMin = nowMin % 60;
						if (nowMin==0) {
							player.sendMessage(ChatColor.GREEN + targePlayerNameString + ChatColor.YELLOW + " 现在不在线 ");
						}else {
							player.sendMessage(ChatColor.GREEN + targePlayerNameString + ChatColor.YELLOW + " 本次游戏了 "
									+ ChatColor.GREEN + nowTheHour + ChatColor.YELLOW + " 小时 " + ChatColor.GREEN + nowTheMin
									+ ChatColor.YELLOW + " 分钟");
						}
						int todayMin = nowMin + WLoginSYS.getPlayerTodayPlayingTime(targePlayerNameString);
						nowTheHour = todayMin / 60;
						nowTheMin = todayMin % 60;
						if (todayMin==0) {
							player.sendMessage(ChatColor.GREEN + targePlayerNameString + ChatColor.YELLOW + " 今天没上过线 ");
						}else {
							player.sendMessage(ChatColor.GREEN + targePlayerNameString + ChatColor.YELLOW + " 今天游戏了 "
									+ ChatColor.GREEN + nowTheHour + ChatColor.YELLOW + " 小时 " + ChatColor.GREEN + nowTheMin
									+ ChatColor.YELLOW + " 分钟");
						}
						int historyMin = nowMin + WLoginSYS.getPlayerDataBasePlayingTime(targePlayerNameString);
						nowTheHour = historyMin / 60;
						nowTheMin = historyMin % 60;
						player.sendMessage(ChatColor.GREEN + targePlayerNameString + ChatColor.YELLOW + " 总共游戏了 "
								+ ChatColor.GREEN + nowTheHour + ChatColor.YELLOW + " 小时 " + ChatColor.GREEN + nowTheMin
								+ ChatColor.YELLOW + " 分钟");
					}
				};
				bukkitRunnable.runTaskAsynchronously(WLogin.main);
				return true;
			}else if(args.length == 1 && !player.isOp()) {
				player.sendMessage(WLogin.noPermissionMsg());
				return true;
			}else {
				return false;
			}
		} else {
			sender.sendMessage(WLogin.playerEntityOnlyMsg());
			return true;
		}
	}
}
