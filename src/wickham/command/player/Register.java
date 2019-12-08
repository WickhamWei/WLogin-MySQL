package wickham.command.player;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import wickham.event.WPlayerLoginEvent;
import wickham.main.WLogin;
import wickham.main.login.WLoginSYS;

public class Register implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// TODO 自动生成的方法存根
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length == 2 && args[0].equals(args[1])) {
				// TODO 自动生成的方法存根
				if (!WLoginSYS.isRegister(player.getName())) {
					if(WLoginSYS.checkPasswordForm(player, args[0])) {
						WLoginSYS.register(player, args[0]);
						WPlayerLoginEvent wPlayerLoginEvent = new WPlayerLoginEvent(player);
						Bukkit.getPluginManager().callEvent(wPlayerLoginEvent);
						if (!wPlayerLoginEvent.isCancelled()) {
							WLoginSYS.login(player);
							WLogin.sendMsg(player, ChatColor.GREEN + "注册成功，已经自动登录");
							player.setGameMode(GameMode.SURVIVAL);
						}
					}else {
						return false;
					}
				} else {
					player.sendMessage(ChatColor.YELLOW + "你已经注册了");
				}

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
