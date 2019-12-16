package wickham.command.player;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import wickham.main.WLogin;
import wickham.main.login.WLoginSYS;

public class ChangePassword implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// TODO 自动生成的方法存根
		if (sender instanceof Player) {
			if (args.length == 3) {
				Player player = (Player) sender;
				if (args[1].equals(args[2])) {
					BukkitRunnable bukkitRunnable = new BukkitRunnable() {

						@Override
						public void run() {
							// TODO 自动生成的方法存根
							if (WLoginSYS.checkPasswordForm(player, args[1])) {
								if (WLoginSYS.changePassword(player, args[0], args[1])) {
									player.sendMessage(ChatColor.GREEN + "修改密码成功");
								} else {
									player.sendMessage(ChatColor.RED + "修改密码失败");
								}
							} else {
								player.sendMessage(ChatColor.RED + "修改密码失败");
							}

						}
					};
					bukkitRunnable.runTaskAsynchronously(WLogin.main);
					return true;
				} else {
					player.sendMessage(ChatColor.RED + "两次新密码不一样");
					return false;
				}
			}else {
				return false;
			}
		} else {
			sender.sendMessage(WLogin.playerEntityOnlyMsg());
			return true;
		}
	}
}
