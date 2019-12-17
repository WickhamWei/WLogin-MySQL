package wickham.command.admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import wickham.main.WLogin;
import wickham.main.login.WLoginSYS;

public class WBan implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// TODO 自动生成的方法存根
		if (sender instanceof Player) {
			Player senderPlayer = (Player) sender;
			if (senderPlayer.isOp()) {
				if (args.length == 3) {
					String targePlayerNameString = args[0];
					if (!WLoginSYS.isRegister(targePlayerNameString)) {
						senderPlayer.sendMessage(WLogin.unknownPlayerEntityMsg());
						return true;
					}
					int banTime = 0;
					try {
						banTime = Integer.parseInt(args[1]);
					} catch (NumberFormatException e) {
						// TODO: handle exception
						return false;
					}
					int tempNum = banTime;
					String reasonString = args[2];
					Player targePlayer = WLogin.main.getServer().getPlayer(targePlayerNameString);
					WLoginSYS.setBanData(sender, targePlayerNameString, reasonString, tempNum);
					if (targePlayer != null) {
						targePlayer.kickPlayer("您已被封禁，封禁理由是 " + WLoginSYS.getMaxBanTimeReason(targePlayerNameString)
								+ " 封禁时间剩余 " + WLoginSYS.getMaxBanTimeMin(targePlayerNameString) + " 分钟");
					}
					return true;
				} else {
					return false;
				}
			} else {
				senderPlayer.sendMessage(WLogin.noPermissionMsg());
			}
		} else {
			if (args.length == 3) {
				String targePlayerNameString = args[0];
				if (!WLoginSYS.isRegister(targePlayerNameString)) {
					sender.sendMessage("玩家不在线、不存在或者数据不存在");
					return true;
				}
				int banTime = 0;
				try {
					banTime = Integer.parseInt(args[1]);
				} catch (NumberFormatException e) {
					// TODO: handle exception
					return false;
				}
				String reasonString = args[2];
				int tempNum = banTime;

				Player targePlayer = WLogin.main.getServer().getPlayer(targePlayerNameString);
				WLoginSYS.setBanData(sender, targePlayerNameString, reasonString, tempNum);
				if (targePlayer != null) {
					targePlayer.kickPlayer("您已被封禁，封禁理由是 " + WLoginSYS.getMaxBanTimeReason(targePlayerNameString)
							+ " 封禁时间剩余 " + WLoginSYS.getMaxBanTimeMin(targePlayerNameString) + " 分钟");
				}

				return true;
			} else {
				return false;
			}
		}
		return false;
	}

}
