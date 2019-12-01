package wickham.command.player;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import wickham.main.WLogin;
import wickham.main.login.WLoginSYS;
import wickham.event.WPlayerLoginEvent;

public class Login implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length == 1) {

				if (WLoginSYS.isRegister(player.getName())) {
					if (!WLoginSYS.isLogin(player)) {
						if (WLoginSYS.chackPassword(player, args[0])) {
							WPlayerLoginEvent wPlayerLoginEvent = new WPlayerLoginEvent(player);
							Bukkit.getPluginManager().callEvent(wPlayerLoginEvent);
							if (!wPlayerLoginEvent.isCancelled()) {
								WLoginSYS.login(player);
								WLogin.sendMsg(player, ChatColor.GREEN + "登录成功");
								player.setGameMode(GameMode.SURVIVAL);
							}
						} else {
							WLoginSYS.loginFail(player);
							WLogin.sendMsg(player, ChatColor.RED + "密码错误");
						}
					} else {
						player.sendMessage(ChatColor.YELLOW + "你已经登录了");
					}
				} else {
					player.sendMessage(ChatColor.RED + "你还没注册");
				}
				return true;
			} else {
				return false;
			}
		} else {
			sender.sendMessage(WLogin.playerEntityOnlyMsg());
			return true;
		}
	}

}
