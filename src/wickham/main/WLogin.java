package wickham.main;

import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import wickham.command.admin.UnLogin;
import wickham.command.player.ChangePassword;
import wickham.command.player.Login;
import wickham.command.player.LoginData;
import wickham.command.player.LoginFailData;
import wickham.command.player.PlayingTime;
import wickham.command.player.Register;
import wickham.listener.PlayerJoinListener;
import wickham.listener.PlayerLoginListener;
import wickham.listener.PlayerQuitGameListener;
import wickham.listener.PlayerUnLoginLimitListener;
import wickham.listener.ServerLoadEventListener;
import wickham.main.login.WLoginSYS;
import wickham.main.mysql.MySQL;

public class WLogin extends JavaPlugin {

	public static WLogin main;
	private MySQL mySQL;
	private boolean enableMySQL;// mysql启动开关
	private boolean mySQLNormal=true;
	private FileConfiguration config = getConfig();
	public static final String url="https://api.github.com/repos/WickhamWei/Wlogin/releases/latest";

	@Override
	public void onEnable() {
		main = this;
		loadConfig();
		loadMySQL();
		loadCommand();
		PreparingListener();
	}

	@Override
	public void onDisable() {
		WLoginSYS.unLoginAllPlayer();
		saveConfiguration();
		unloadMySQL();
	}

	private void loadCommand() {// 载入指令
		main.getCommand("login").setExecutor(new Login());
		main.getCommand("register").setExecutor(new Register());
		main.getCommand("unlogin").setExecutor(new UnLogin());
		main.getCommand("changepassword").setExecutor(new ChangePassword());
		main.getCommand("logindata").setExecutor(new LoginData());
		main.getCommand("loginfaildata").setExecutor(new LoginFailData());
		main.getCommand("playingtime").setExecutor(new PlayingTime());
	}
	
	private void PreparingListener() {// 载入监听器
		getServer().getPluginManager().registerEvents(new PlayerQuitGameListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerUnLoginLimitListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerLoginListener(), this);
		getServer().getPluginManager().registerEvents(new ServerLoadEventListener(), this);
	}
	
	private void loadConfig() {
		this.saveDefaultConfig();
		enableMySQL=config.getBoolean("启动MySQL");
	}

	public boolean isMySQLEnable() {// 服务器是否启动mysql
		return enableMySQL;
	}
	
	public static String noPermissionMsg() {
		return ChatColor.RED+"你没有权限做此事";
	}
	
	public static String unknownPlayerEntityMsg() {
		return ChatColor.RED+"玩家不在线或不存在";
	}
	
	public static String playerEntityOnlyMsg() {
		return ChatColor.RED+"只有玩家实体才可执行此命令";
	}
	
	public static String serverCommandErrorMsg() {
		return ChatColor.RED+"WLogin插件在处理命令时出错，请联系管理员";
	}
	
	public static void sendMsg(Player player,String msgString) {
		player.sendTitle("", msgString, 5, 100, 5);
	}
	
	public MySQL getDatabase() {
		return this.mySQL;
	}
	public FileConfiguration getConfiguration() {
		return config;
	}
	
	public void saveConfiguration() {
		saveConfig();
	}
	
	private void loadMySQL() {
		if (enableMySQL) {
			mySQL = new MySQL();
			mySQL.setUsername(config.getString("用户名"));
			mySQL.setPassword(config.getString("密码"));
			mySQL.setHost(config.getString("MySQL服务器地址"));
			mySQL.setPort(config.getInt("MySQL服务器端口"));
			mySQL.setDatabase(config.getString("数据库名字"));
			try {// 启动mysql
				mySQL.openConnection();
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				getLogger().warning("连接数据库失败");
				mySQLNormal=false;
			}
			try {// 检查是否启动mysql
				if (mySQL.isConnection()) {
					getLogger().info("成功连接至数据库");
					WLoginSYS.initTable();
				} 
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void unloadMySQL() {
		if (mySQLNormal) {
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
	
	public boolean isMySQLNormal() {
		return mySQLNormal;
	}
}
