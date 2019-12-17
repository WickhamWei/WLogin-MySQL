package wickham.command.admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import wickham.main.WLogin;
import wickham.main.login.WLoginSYS;

public class WBanData implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// TODO 自动生成的方法存根
		if (sender instanceof Player) {
			Player senderPlayer = (Player) sender;
			if (senderPlayer.isOp()) {
				if (args.length == 1) {
					String targePlayerNameString = args[0];
					BukkitRunnable bukkitRunnable = new BukkitRunnable() {

						@Override
						public void run() {
							// TODO 自动生成的方法存根
							WLoginSYS.getBanData(senderPlayer, targePlayerNameString, 1);
						}
					};
					bukkitRunnable.runTaskAsynchronously(WLogin.main);
					return true;
				} else if (args.length == 2) {
					String targePlayerNameString = args[0];
					int page = 0;
					try {
						page = Integer.parseInt(args[1]);
					} catch (NumberFormatException e) {
						// TODO: handle exception
					}
					int tempNum = page;
					BukkitRunnable bukkitRunnable = new BukkitRunnable() {

						@Override
						public void run() {
							// TODO 自动生成的方法存根
							WLoginSYS.getBanData(senderPlayer, targePlayerNameString, tempNum);
						}
					};
					bukkitRunnable.runTaskAsynchronously(WLogin.main);
					return true;
				} else {
					return false;
				}
			} else {
				senderPlayer.sendMessage(WLogin.noPermissionMsg());
				return true;
			}
		} else {
			sender.sendMessage(WLogin.playerEntityOnlyMsg());
			return true;
		}
	}

}
