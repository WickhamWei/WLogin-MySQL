package wickham.main;

import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import wickham.command.player.ChangePassword;
import wickham.command.player.Login;
import wickham.command.player.Register;
import wickham.command.player.UnLogin;
import wickham.listener.PlayerJoinListener;
import wickham.listener.PlayerLoginListener;
import wickham.listener.PlayerQuitGameListener;
import wickham.listener.PlayerUnLoginLimitListener;
import wickham.main.login.WLoginSYS;

public class WLogin extends JavaPlugin {

	public static WLogin main;
	public static MySQL mySQL;
	public static boolean enableMySQL = true;// mysql启动开关

	@Override
	public void onEnable() {
		main = this;
		if (enableMySQL) {
			mySQL = new MySQL();
			try {// 启动mysql
				mySQL.openConnection();
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				getLogger().warning("连接数据库失败");
			}
			try {// 检查是否启动mysql
				if (mySQL.isConnection()) {
					getLogger().info("成功连接至数据库");
					WLoginSYS.initTable();
				} else {
					enableMySQL = false;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		loadCommand();
		PreparingListener();
	}

	@Override
	public void onDisable() {
		WLoginSYS.unLoginAllPlayer();
		if (enableMySQL) {
			try {
				if (mySQL.isConnection()) {
					if (mySQL.disconnection()) {
						getLogger().info("已断开与数据库的连接");
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void loadCommand() {// 载入指令
		main.getCommand("login").setExecutor(new Login());
		main.getCommand("register").setExecutor(new Register());
		main.getCommand("unlogin").setExecutor(new UnLogin());
		main.getCommand("changepassword").setExecutor(new ChangePassword());
	}
	
	private void PreparingListener() {// 载入监听器
		getServer().getPluginManager().registerEvents(new PlayerQuitGameListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerUnLoginLimitListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerLoginListener(), this);
	}

	public boolean isMySQLEnable() {// 服务器是否启动mysql
		return enableMySQL;
	}
	
	public static String noPermissionMsg() {
		return ChatColor.RED+"你没有权限做此事";
	}
	
	public static String unknownPlayerEntityMsg() {
		return ChatColor.RED+"玩家不存在";
	}
	
	public static String playerEntityOnlyMsg() {
		return ChatColor.RED+"只有玩家实体才可执行此命令";
	}
	
	public static String serverCommandErrorMsg() {
		return ChatColor.RED+"服务器在处理命令时出错，请联系管理员";
	}
	
	public static void sendMsg(Player player,String msgString) {
		player.sendTitle("", msgString, 5, 100, 5);
	}
}
