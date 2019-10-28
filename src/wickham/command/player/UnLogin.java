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
		if (args.length == 0) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				BukkitRunnable bukkitRunnable = new BukkitRunnable() {

					@Override
					public void run() {
						// TODO 自动生成的方法存根
						if (WLoginSYS.isRegister(player.getName())) {
							if (WLoginSYS.isLogin(player)) {
								WLoginSYS.unlogin(player);
							} else {
								player.sendMessage("你还没登录");
							}
						} else {
							player.sendMessage("你还未注册");
						}
					}
				};
				bukkitRunnable.runTaskAsynchronously(WLogin.main);
				return true;
			} else {
				sender.sendMessage(WLogin.playerEntityOnlyMsg());
			}
		} else if (args.length == 1) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (player.isOp() || args[0].equals(player.getName())) {
					String targePlayerNameString = args[0];
					BukkitRunnable bukkitRunnable = new BukkitRunnable() {

						@Override
						public void run() {
							// TODO 自动生成的方法存根
							if (WLoginSYS.isRegister(targePlayerNameString)) {
								if (WLoginSYS.isLogin(targePlayerNameString)) {
									if(!WLoginSYS.unlogin(WLogin.main.getServer().getPlayer(targePlayerNameString))) {
										player.sendMessage(WLogin.unknownPlayerEntityMsg());
									}
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
				}
			} else {
				String targePlayerNameString = args[0];
				BukkitRunnable bukkitRunnable = new BukkitRunnable() {

					@Override
					public void run() {
						// TODO 自动生成的方法存根
						if (WLoginSYS.isRegister(targePlayerNameString)) {
							if (WLoginSYS.isLogin(targePlayerNameString)) {
								if(!WLoginSYS.unlogin(WLogin.main.getServer().getPlayer(targePlayerNameString))) {
									sender.sendMessage(WLogin.unknownPlayerEntityMsg());
								}
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
