package wickham.command.player;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import wickham.main.WLogin;
import wickham.main.login.WLoginSYS;

public class ChangePassword implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// TODO 自动生成的方法存根
		if(args.length==2) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if(args[0].equals(args[1])) {
					BukkitRunnable bukkitRunnable=new BukkitRunnable() {
						
						@Override
						public void run() {
							// TODO 自动生成的方法存根
							if (WLoginSYS.changePassword(player, player.getName(), args[0])) {
								player.sendMessage("修改密码成功");
							}else {
								player.sendMessage(WLogin.serverCommandErrorMsg());
							}
						}
					};
					bukkitRunnable.runTaskAsynchronously(WLogin.main);
				}else {
					player.sendMessage("两次密码不一样");
					return false;
				}
			}else {
				sender.sendMessage("只有玩家可以执行这个命令");
				return false;
			}
		}else if (args.length==3) {
			if(sender instanceof Player) {
				Player player=(Player) sender;
				if(args[0].equals(player.getName())) {
					if(args[1].equals(args[2])) {
						BukkitRunnable bukkitRunnable=new BukkitRunnable() {
							
							@Override
							public void run() {
								// TODO 自动生成的方法存根
								if (WLoginSYS.changePassword(player, player.getName(), args[1])) {
									player.sendMessage("修改密码成功");
								}else {
									player.sendMessage(WLogin.serverCommandErrorMsg());
								}
							}
						};
						bukkitRunnable.runTaskAsynchronously(WLogin.main);
					}else {
						player.sendMessage("两次密码不一样");
						return false;
					}
				}else {
					if(player.isOp()) {
						if(WLoginSYS.isRegister(args[0])) {
							if(args[1].equals(args[2])) {
								BukkitRunnable bukkitRunnable=new BukkitRunnable() {
									
									@Override
									public void run() {
										// TODO 自动生成的方法存根
										if (WLoginSYS.changePassword(player, args[0], args[1])) {
											player.sendMessage("修改 "+args[0]+" 的密码成功");
										}else {
											player.sendMessage(WLogin.serverCommandErrorMsg());
										}
										
										
									}
								};
								bukkitRunnable.runTaskAsynchronously(WLogin.main);
							}else {
								player.sendMessage("两次密码不一样");
								return false;
							}
						}else {
							player.sendMessage(WLogin.unknownPlayerEntityMsg());
							return false;
						}
					}else {
						player.sendMessage(WLogin.noPermissionMsg());
						return false;
					}
				}
			}else if (sender instanceof ConsoleCommandSender) {
				if(WLoginSYS.isRegister(args[0])) {
					if(args[1].equals(args[2])) {
						BukkitRunnable bukkitRunnable=new BukkitRunnable() {
							
							@Override
							public void run() {
								// TODO 自动生成的方法存根
								if (WLoginSYS.changePassword(sender, args[0], args[1])) {
									sender.sendMessage("修改 "+args[0]+" 的密码成功");
								}else {
									sender.sendMessage(WLogin.serverCommandErrorMsg());
								}
							}
						};
						bukkitRunnable.runTaskAsynchronously(WLogin.main);
					}else {
						sender.sendMessage("两次密码不一样");
						return false;
					}
				}else {
					sender.sendMessage(WLogin.unknownPlayerEntityMsg());
					return false;
				}
			}else {
				sender.sendMessage(WLogin.playerEntityOnlyMsg());
				return false;
			}
		}
		return true;
	}
	
}
