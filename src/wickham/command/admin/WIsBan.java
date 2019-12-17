package wickham.command.admin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import wickham.main.WLogin;
import wickham.main.login.WLoginSYS;

public class WIsBan implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// TODO 自动生成的方法存根
		if (sender instanceof Player) {
			Player senderPlayer = (Player) sender;
			if(senderPlayer.isOp()) {
				if(args.length==1) {
					String targePlayerNameString=args[0];
					BukkitRunnable bukkitRunnable=new BukkitRunnable() {
						
						@Override
						public void run() {
							// TODO 自动生成的方法存根
							if(WLoginSYS.isBanning(targePlayerNameString)) {
								senderPlayer.sendMessage(ChatColor.GREEN+targePlayerNameString+ChatColor.YELLOW+" 正在封禁中");
							}else {
								senderPlayer.sendMessage(ChatColor.GREEN+targePlayerNameString+ChatColor.YELLOW+" 未在封禁中");
							}
						}
					};
					bukkitRunnable.runTaskAsynchronously(WLogin.main);
					return true;
				}else {
					return false;
				}
			}else {
				senderPlayer.sendMessage(WLogin.noPermissionMsg());
				return true;
			}
		}else {
			if(args.length==1) {
				String targePlayerNameString=args[0];
				BukkitRunnable bukkitRunnable=new BukkitRunnable() {
					
					@Override
					public void run() {
						// TODO 自动生成的方法存根
						if(WLoginSYS.isBanning(targePlayerNameString)) {
							sender.sendMessage(targePlayerNameString+" 正在封禁中");
						}else {
							sender.sendMessage(targePlayerNameString+" 未在封禁中");
						}
					}
				};
				bukkitRunnable.runTaskAsynchronously(WLogin.main);
				return true;
			}else {
				return false;
			}
		}
	}

}
