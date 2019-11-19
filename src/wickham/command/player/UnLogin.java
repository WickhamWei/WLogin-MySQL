package wickham.command.player;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import wickham.main.WLogin;
import wickham.main.login.WLoginSYS;

public class UnLogin implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// TODO 自动生成的方法存根
		if (args.length == 1) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (player.isOp()) {
					String targePlayerNameString = args[0];
					BukkitRunnable bukkitRunnable = new BukkitRunnable() {

						@Override
						public void run() {
							// TODO 自动生成的方法存根
							if (WLoginSYS.isRegister(targePlayerNameString)) {
								if (WLoginSYS.isLogin(targePlayerNameString)) {
									WLoginSYS.unLogin(player, WLogin.main.getServer().getPlayer(targePlayerNameString));

								} else {
									player.sendMessage(targePlayerNameString + " 还没登录");
								}
							} else {
								player.sendMessage(targePlayerNameString + " 还未注册");
							}
						}
					};
					bukkitRunnable.runTaskAsynchronously(WLogin.main);
					return true;
				} else {
					player.sendMessage(WLogin.noPermissionMsg());
				}
			} else {
				String targePlayerNameString = args[0];
				BukkitRunnable bukkitRunnable = new BukkitRunnable() {

					@Override
					public void run() {
						// TODO 自动生成的方法存根
						if (WLoginSYS.isRegister(targePlayerNameString)) {
							if (WLoginSYS.isLogin(targePlayerNameString)) {
								WLoginSYS.unLogin(sender, WLogin.main.getServer().getPlayer(targePlayerNameString));
							} else {
								sender.sendMessage(targePlayerNameString + " 还没登录");
							}
						} else {
							sender.sendMessage(targePlayerNameString + " 还未注册");
						}
					}
				};
				bukkitRunnable.runTaskAsynchronously(WLogin.main);
				return true;
			}
		}
		return true;
	}

}
