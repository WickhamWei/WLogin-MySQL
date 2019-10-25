package wickham.command.player;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import wickham.main.WLogin;
import wickham.main.login.WLoginSYS;

public final class Login implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length == 1) {
				BukkitRunnable bukkitRunnable=new BukkitRunnable() {
					
					@Override
					public void run() {
						// TODO 自动生成的方法存根
						if (WLoginSYS.isRegister(player)) {
							if (!WLoginSYS.isLogin(player)) {
								if (WLoginSYS.chackPassword(player, args[0])) {
									WLoginSYS.login(player);
									player.sendMessage("登录成功");
								} else {
									player.sendMessage("密码错误");
								}
							}else {
								player.sendMessage("你已经登录了");
							}
						}else {
							player.sendMessage("你还没注册");
						}
					}
				};
				bukkitRunnable.runTaskAsynchronously(WLogin.main);
				return true;
			}else{
				return false;
			}
		}else {
			sender.sendMessage("你必须是个玩家");
			return true;
		}
	}

}
