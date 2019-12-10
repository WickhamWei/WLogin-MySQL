package wickham.command.admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import wickham.main.WLogin;
import wickham.main.login.WLoginSYS;

public class PasswordChangeHistory implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player player=(Player)sender;
			if(player.isOp()) {
				if (args.length==1) {
					BukkitRunnable bukkitRunnable = new BukkitRunnable() {
						
						@Override
						public void run() {
							WLoginSYS.checkOldPasswordData(player, args[0], 1);
							// TODO 自动生成的方法存根
							
						}
					};
					bukkitRunnable.runTaskAsynchronously(WLogin.main);
					return true;
				}else if (args.length==2) {
					try {
						int page=Integer.parseInt(args[1]);
						BukkitRunnable bukkitRunnable = new BukkitRunnable() {
							
							@Override
							public void run() {
								WLoginSYS.checkOldPasswordData(player, args[0], page);
								// TODO 自动生成的方法存根
								
							}
						};
						bukkitRunnable.runTaskAsynchronously(WLogin.main);
					} catch (NumberFormatException exception) {
						// TODO: handle exception
						return false;
					}
					return true;
				}else {
					return false;
				}
			}else {
				player.sendMessage(WLogin.noPermissionMsg());
				return true;
			}
		}else {
			sender.sendMessage(WLogin.playerEntityOnlyMsg());
			return true;
		}
		// TODO 自动生成的方法存根
	}
	
}
