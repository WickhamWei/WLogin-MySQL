package wickham.main.mysql;

import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.scheduler.BukkitRunnable;

import wickham.main.WLogin;

public abstract class AllTables {
	public static void initTable() {// 初始化表

		BukkitRunnable bukkitRunnable = new BukkitRunnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				boolean normal = true;
				Statement statement;
				try {
					statement = WLogin.main.getDatabase().getConnection().createStatement();
					String sql = "CREATE TABLE IF NOT EXISTS `playerpassword`(" + "`playername` VARCHAR(40) NOT NULL,"
							+ "`password` VARCHAR(40) NOT NULL," + "`ip` bigint(20) NOT NULL,"
							+ "PRIMARY KEY ( `playername` )" + ")ENGINE=InnoDB DEFAULT CHARSET=utf8;";
					statement.executeUpdate(sql);
					statement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					normal = false;
					e.printStackTrace();
					WLogin.main.getLogger().warning("创建 playerpassword 失败");
				}
				try {
					statement = WLogin.main.getDatabase().getConnection().createStatement();
					String sql = "CREATE TABLE IF NOT EXISTS `playerlogindata`(" + "`playername` VARCHAR(40) NOT NULL,"
							+ "`logintime` TIMESTAMP NOT NULL," + "`loginable` TINYINT(1) NOT NULL,"
							+ "`ip` bigint(20) NOT NULL,"
							+ "FOREIGN KEY (playername) REFERENCES playerpassword(playername)"
							+ ")ENGINE=InnoDB DEFAULT CHARSET=utf8;";
					statement.executeUpdate(sql);
					statement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					normal = false;
					e.printStackTrace();
					WLogin.main.getLogger().warning("创建 playerlogindata 失败");
				}
				try {
					statement = WLogin.main.getDatabase().getConnection().createStatement();
					String sql = "CREATE TABLE IF NOT EXISTS `playerplayingtime`("
							+ "`playername` VARCHAR(40) NOT NULL," + "`min` INT NOT NULL,"
							+ "FOREIGN KEY (playername) REFERENCES playerpassword(playername)," + "UNIQUE (playername)"
							+ ")ENGINE=InnoDB DEFAULT CHARSET=utf8;";
					statement.executeUpdate(sql);
					statement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					normal = false;
					e.printStackTrace();
					WLogin.main.getLogger().warning("创建 playerplayingtime 失败");
				}
				try {
					statement = WLogin.main.getDatabase().getConnection().createStatement();
					String sql = "CREATE TABLE IF NOT EXISTS `playerdailyplaytime`("
							+ "`playername` VARCHAR(40) NOT NULL," + "`date` DATE NOT NULL," + "`min` INT NOT NULL,"
							+ "FOREIGN KEY (playername) REFERENCES playerpassword(playername))ENGINE=InnoDB DEFAULT CHARSET=utf8;";
					statement.executeUpdate(sql);
					statement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					normal = false;
					e.printStackTrace();
					WLogin.main.getLogger().warning("创建 playerdailyplaytime 失败");
				}
				try {
					statement = WLogin.main.getDatabase().getConnection().createStatement();
					String sql = "CREATE TABLE IF NOT EXISTS `playeroldpassword`("
							+ "`sendername` VARCHAR(40) NOT NULL," + "`playername` VARCHAR(40) NOT NULL,"
							+ "`time` TIMESTAMP NOT NULL," + "`oldpassword` VARCHAR(40) NOT NULL," + "`ip` bigint(20),"
							+ "FOREIGN KEY (playername) REFERENCES playerpassword(playername)"
							+ ")ENGINE=InnoDB DEFAULT CHARSET=utf8;";
					statement.executeUpdate(sql);
					statement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					normal = false;
					e.printStackTrace();
					WLogin.main.getLogger().warning("创建 playeroldpassword 失败");
				}
				try {
					statement = WLogin.main.getDatabase().getConnection().createStatement();
					String sql = "CREATE TABLE IF NOT EXISTS `playeristeenagers`("
							+ "`playername` VARCHAR(40) NOT NULL,"
							+ "FOREIGN KEY (playername) REFERENCES playerpassword(playername),"
							+ "UNIQUE ( `playername` )" + ")ENGINE=InnoDB DEFAULT CHARSET=utf8;";
					statement.executeUpdate(sql);
					statement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					normal = false;
					e.printStackTrace();
					WLogin.main.getLogger().warning("创建 playeristeenagers 失败");
				}
				try {
					statement = WLogin.main.getDatabase().getConnection().createStatement();
					String sql = "CREATE TABLE IF NOT EXISTS `banplayerdata`(" + "`playername` VARCHAR(40) NOT NULL,"+"`senderplayername` VARCHAR(40) NOT NULL,"
							+ "`reason` VARCHAR(300) NOT NULL," + "`time` TIMESTAMP NOT NULL," + "`time_long` INT,"
							+ "FOREIGN KEY (playername) REFERENCES playerpassword(playername)"
							+ ")ENGINE=InnoDB DEFAULT CHARSET=utf8;";
					statement.executeUpdate(sql);
					statement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					normal = false;
					e.printStackTrace();
					WLogin.main.getLogger().warning("创建 banplayerdata 失败");
				}
				if (normal) {
					WLogin.main.getLogger().info("表初始化完成");
				} else {
					WLogin.main.mySQLError();
					WLogin.main.getLogger().info("表初始化失败，服务器即将关闭。");
				}
			}
		};

		bukkitRunnable.runTaskAsynchronously(WLogin.main);
	}
}
