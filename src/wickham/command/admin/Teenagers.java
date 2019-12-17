package wickham.command.admin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import wickham.main.WLogin;
import wickham.main.login.WLoginSYS;

public class Teenagers implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String labelString, String[] args) {
		// TODO 自动生成的方法存根
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (player.isOp()) {
				if (args.length == 1) {
					String targePlayerNameString = args[0];
					Player targePlayer=WLogin.main.getServer().getPlayer(targePlayerNameString);
					BukkitRunnable bukkitRunnable = new BukkitRunnable() {

						@Override
						public void run() {
							// TODO 自动生成的方法存根
							WLoginSYS.setTeenagers(targePlayerNameString);
							player.sendMessage(ChatColor.YELLOW + "已将 " + ChatColor.GREEN + targePlayerNameString
									+ ChatColor.YELLOW + " 纳入防沉迷系统");
							if(!WLoginSYS.teenageersChecker(targePlayer)) {
								targePlayer.kickPlayer(WLogin.kickTeenagersMsg());
								return;
							}
						}
					};
					bukkitRunnable.runTaskAsynchronously(WLogin.main);
					return true;
				} else {
					return false;
				}
			} else {
				player.sendMessage(WLogin.noPermissionMsg());
				return true;
			}
		} else {
			if (args.length == 1) {
				String targePlayerNameString = args[0];
				BukkitRunnable bukkitRunnable = new BukkitRunnable() {

					@Override
					public void run() {
						// TODO 自动生成的方法存根
						WLoginSYS.setTeenagers(targePlayerNameString);
						sender.sendMessage("已将 " + targePlayerNameString + " 纳入防沉迷系统");
					}
				};
				bukkitRunnable.runTaskAsynchronously(WLogin.main);
				return true;
			} else {
				return false;
			}
		}
	}

}
