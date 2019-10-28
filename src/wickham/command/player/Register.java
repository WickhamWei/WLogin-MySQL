package wickham.command.player;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import wickham.main.WLogin;
import wickham.main.login.WLoginSYS;

public class Register implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// TODO 自动生成的方法存根
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length == 2 && args[0].equals(args[1])) {
				BukkitRunnable bukkitRunnable = new BukkitRunnable() {

					@Override
					public void run() {
						// TODO 自动生成的方法存根
						if (!WLoginSYS.isRegister(player.getName())) {
							WLoginSYS.register(player, args[0]);
							WLoginSYS.login(player);
						} else {
							player.sendMessage("你已经注册了");
						}
					}
				};
				bukkitRunnable.runTaskAsynchronously(WLogin.main);
				return true;
			} else {
				return false;
			}
		} else {
			sender.sendMessage(WLogin.playerEntityOnlyMsg());
		}
		return true;
	}

}
