package wickham.command.normal;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import wickham.main.WLogin;
import wickham.main.login.WLoginSYS;

public class LoginData implements CommandExecutor {

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
						WLoginSYS.checkLoginData(player, playerNameString, 1);
					}
				};
				bukkitRunnable.runTaskAsynchronously(WLogin.main);
				return true;
			} else if (args.length == 1) {
				try {
					int page = Integer.parseInt(args[0]);
					String playerNameString = player.getName();
					BukkitRunnable bukkitRunnable = new BukkitRunnable() {

						@Override
						public void run() {
							// TODO 自动生成的方法存根
							WLoginSYS.checkLoginData(player, playerNameString, page);
						}
					};
					bukkitRunnable.runTaskAsynchronously(WLogin.main);
					return true;
				} catch (NumberFormatException e) {
					// TODO: handle exception
					return false;
				}
			}else if (args.length == 2&&player.isOp()) {
				try {
					int page = Integer.parseInt(args[1]);
					String playerNameString = args[0];
					BukkitRunnable bukkitRunnable = new BukkitRunnable() {

						@Override
						public void run() {
							// TODO 自动生成的方法存根
							WLoginSYS.checkLoginData(player, playerNameString, page);
						}
					};
					bukkitRunnable.runTaskAsynchronously(WLogin.main);
					return true;
				} catch (NumberFormatException e) {
					// TODO: handle exception
					return false;
				}
			}else if(args.length == 2 && !player.isOp()) {
				player.sendMessage(WLogin.noPermissionMsg());
				return true;
			}else {
				return false;
			}
		}else {
			sender.sendMessage(WLogin.playerEntityOnlyMsg());
			return true;
		}
		
	}

}
