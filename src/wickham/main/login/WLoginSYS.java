package wickham.main.login;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import wickham.main.WLogin;
import wickham.main.mysql.tables.PlayerLoginData;
import wickham.main.mysql.tables.PlayerPassword;
import wickham.main.mysql.tables.PlayerPlayingTime;

public abstract class WLoginSYS {

	private static HashMap<String, Timestamp> loginListHashMap = new HashMap<String, Timestamp>(); // 存放登录的玩家名字和登录的时间戳

	public static boolean isLogin(String playerNameString) { // 判断玩家是否登录
		return loginListHashMap.containsKey(playerNameString);
	}

	public static boolean isLogin(Player player) {
		return isLogin(player.getName());
	}

	public static void register(Player player, String passwordString) {// 玩家注册
		BukkitRunnable bukkitRunnable = new BukkitRunnable() {

			@Override
			public void run() {
				// TODO 自动生成的方法存根
				String playerNameString = player.getName();
				Statement statement;
				try {
					statement = WLogin.mySQL.getConnection().createStatement();
					statement.executeUpdate(
							"INSERT INTO playerpassword(playername,password,ip) VALUES ('" + playerNameString + "','"
									+ passwordString + "',inet_aton('" + getPlayerIPAddress(player) + "'))");
					statement.close();
				} catch (SQLException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
					WLogin.main.getLogger().warning("玩家 " + playerNameString + " 的注册数据记录失败");
					player.sendMessage(WLogin.serverCommandErrorMsg());
					return;
				}
				return;
			}
		};
		bukkitRunnable.runTaskAsynchronously(WLogin.main);
	}

	public static boolean isRegister(String playerNameString) {
		Statement statement;
		String playerNameFromDBString = null;
		try {
			statement = WLogin.mySQL.getConnection().createStatement();
			ResultSet result = statement
					.executeQuery("SELECT * FROM playerpassword WHERE playername = '" + playerNameString + "';");
			while (result.next()) {
				playerNameFromDBString = result.getString(1);
			}
			statement.close();
			if (playerNameFromDBString == null || playerNameFromDBString.length() == 0) {
				return false;
			} else {
				return true;
			}
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return false;
		}
	}
	public static void chackFailLoginData(Player senderPlayer, String targePlayerNameString, int page) {
		Statement statement;
		LinkedHashSet<PlayerLoginData> allDatas = new LinkedHashSet<PlayerLoginData>();
		try {
			statement=WLogin.mySQL.getConnection().createStatement();
			ResultSet resultNum = statement.executeQuery(
					"SELECT count(*) FROM playerlogindata WHERE playername = '" + targePlayerNameString + "' and loginable = 0");
			int dataLength = 0;
			while (resultNum.next()) {
				dataLength = resultNum.getInt(1);// 获取数据长度
			}
			if (dataLength == 0) {// 没有数据
				resultNum.close();
				statement.close();
				senderPlayer.sendMessage(WLogin.unknownPlayerEntityMsg());
				return;
			}
			if (page > (int) Math.ceil((float) dataLength / 5)) {// 使页数不超过最大值
				page = (int) Math.ceil((float) dataLength / 5);
			}
			int firstDataNum=(page-1)*5;
			ResultSet resultData = statement
					.executeQuery("SELECT logintime,loginable,inet_ntoa(ip) FROM playerlogindata WHERE playername = '"
							+ targePlayerNameString + "' and loginable = 0" + " limit "+firstDataNum+",5;");
			while (resultData.next()) {//获取数据
				PlayerLoginData playerLoginData = new PlayerLoginData();
				playerLoginData.setLoginTime(resultData.getTimestamp(1));
				playerLoginData.setLoginable(resultData.getBoolean(2));
				playerLoginData.setIpString(resultData.getString(3));
				allDatas.add(playerLoginData);
			}
			senderPlayer.sendMessage(ChatColor.YELLOW + "---玩家 " + ChatColor.GREEN + targePlayerNameString
					+ ChatColor.RED + " 登录失败数据"+ChatColor.YELLOW+" - 第 " + ChatColor.GREEN + page + ChatColor.YELLOW + " 页 - 总共 "
					+ ChatColor.GREEN + (int) Math.ceil((float) dataLength / 5) + ChatColor.YELLOW + " 页---");
			int tempNumber = 1;
			for (PlayerLoginData data : allDatas) {//发送数据给玩家
				senderPlayer.sendMessage(
						ChatColor.YELLOW + "-第 " + ChatColor.GREEN + tempNumber + ChatColor.YELLOW + " 条数据-");
				senderPlayer.sendMessage(ChatColor.YELLOW + "尝试登录的时间 " + ChatColor.GREEN
						+ data.getLoginTime().toString());
				senderPlayer.sendMessage(ChatColor.YELLOW + "尝试登录的IP地址 " + ChatColor.GREEN + data.getIpString());
				tempNumber++;
			}
			resultNum.close();
			resultData.close();
			statement.close();
			
		} catch (SQLException e) {
			// TODO: handle exception
		}
	}

	public static void chackLoginData(Player senderPlayer, String targePlayerNameString, int page) {
		Statement statement;
		LinkedHashSet<PlayerLoginData> allDatas = new LinkedHashSet<PlayerLoginData>();
		try {
			statement = WLogin.mySQL.getConnection().createStatement();
			ResultSet resultNum = statement.executeQuery(
					"SELECT count(*) FROM playerlogindata WHERE playername = '" + targePlayerNameString + "'");
			int dataLength = 0;
			while (resultNum.next()) {
				dataLength = resultNum.getInt(1);// 获取数据长度
			}
			if (dataLength == 0) {// 没有数据
				resultNum.close();
				statement.close();
				senderPlayer.sendMessage(WLogin.unknownPlayerEntityMsg());
				return;
			}
			if (page > (int) Math.ceil((float) dataLength / 5)) {// 使页数不超过最大值
				page = (int) Math.ceil((float) dataLength / 5);
			}
			int firstDataNum=(page-1)*5;
			ResultSet resultData = statement
					.executeQuery("SELECT logintime,loginable,inet_ntoa(ip) FROM playerlogindata WHERE playername = '"
							+ targePlayerNameString + "'" + " limit "+firstDataNum+",5;");
			while (resultData.next()) {//获取数据
				PlayerLoginData playerLoginData = new PlayerLoginData();
				playerLoginData.setLoginTime(resultData.getTimestamp(1));
				playerLoginData.setLoginable(resultData.getBoolean(2));
				playerLoginData.setIpString(resultData.getString(3));
				allDatas.add(playerLoginData);
			}
			senderPlayer.sendMessage(ChatColor.YELLOW + "---玩家 " + ChatColor.GREEN + targePlayerNameString
					+ ChatColor.YELLOW + " 登录数据 - 第 " + ChatColor.GREEN + page + ChatColor.YELLOW + " 页 - 总共 "
					+ ChatColor.GREEN + (int) Math.ceil((float) dataLength / 5) + ChatColor.YELLOW + " 页---");
			int tempNumber = 1;
			for (PlayerLoginData data : allDatas) {//发送数据给玩家
				senderPlayer.sendMessage(
						ChatColor.YELLOW + "-第 " + ChatColor.GREEN + tempNumber + ChatColor.YELLOW + " 条数据-");
				senderPlayer.sendMessage(ChatColor.YELLOW + "尝试登录的时间 " + ChatColor.GREEN
						+ data.getLoginTime().toString() + ChatColor.YELLOW + " 是否登录成功 " + ChatColor.GREEN
						+ booleanToString(data.isLoginable()));
				senderPlayer.sendMessage(ChatColor.YELLOW + "尝试登录的IP地址 " + ChatColor.GREEN + data.getIpString());
				tempNumber++;
			}
			resultNum.close();
			resultData.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}

	public static String booleanToString(Boolean targeBoolean) {
		if (targeBoolean) {
			return ChatColor.GREEN + "是";
		} else {
			return ChatColor.RED + "否";
		}
	}

	public static boolean chackPassword(Player player, String passwordString) {// 检查密码是否正确
		String playerNameString = player.getName();
		Statement statement;
		String passwordFromDBString = null;
		try {
			statement = WLogin.mySQL.getConnection().createStatement();
			ResultSet result = statement
					.executeQuery("SELECT password FROM playerpassword WHERE playername = '" + playerNameString + "';");
			while (result.next()) {
				passwordFromDBString = result.getString(1);
			}
			if (passwordFromDBString == null || passwordFromDBString.length() == 0) {
				return false;
			} else {
				if (passwordFromDBString.equals(passwordString)) {
					return true;
				}
			}
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return false;
		}
		return false;
	}

	public static void login(Player player) {
		BukkitRunnable bukkitRunnable = new BukkitRunnable() {

			@Override
			public void run() {
				// TODO 自动生成的方法存根
				String playerNameString = player.getName();
				// TODO Auto-generated method stub
				boolean done = true;
				try {
					Statement statement = WLogin.mySQL.getConnection().createStatement();
					String sql = "INSERT INTO playerlogindata(playername,logintime,loginable,ip) VALUES ('"
							+ playerNameString + "', now() ," + true + ",inet_aton('" + getPlayerIPAddress(player)
							+ "'))";
					statement.executeUpdate(sql);
					statement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					WLogin.main.getLogger().warning("玩家 " + playerNameString + " 的登陆数据记录失败");
					player.sendMessage(WLogin.serverCommandErrorMsg());
					done = false;
					return;
				}
				if (done) {
					loginListHashMap.put(playerNameString, getNowTimestamp());
				}
				return;
			}
		};
		bukkitRunnable.runTaskAsynchronously(WLogin.main);
	}

	public static void loginFail(Player player) {
		BukkitRunnable bukkitRunnable = new BukkitRunnable() {

			@Override
			public void run() {
				// TODO 自动生成的方法存根
				String playerNameString = player.getName();
				// TODO Auto-generated method stub
				try {
					Statement statement = WLogin.mySQL.getConnection().createStatement();
					String sql = "INSERT INTO playerlogindata(playername,logintime,loginable,ip) VALUES ('"
							+ playerNameString + "', now() ," + false + ",inet_aton('" + getPlayerIPAddress(player)
							+ "'))";
					statement.executeUpdate(sql);
					statement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					WLogin.main.getLogger().warning("玩家 " + playerNameString + " 的登陆数据记录失败");
				}
				return;
			}
		};
		bukkitRunnable.runTaskAsynchronously(WLogin.main);
	}

	public static void unLoginAllPlayer() {
		for (String playerNameString : loginListHashMap.keySet()) {
			unLogin(WLogin.main.getServer().getPlayer(playerNameString));
		}
	}
	
	public static int getPlayerNowPlayingTime(Player targePlayer) {// 获得本次游戏的时间
		String targePlayerNameString=targePlayer.getName();
		Timestamp loginTimestamp=loginListHashMap.get(targePlayerNameString);
		if(loginTimestamp==null) {
			return 0;
		}
		return getTimeDifferenceMinutes(getNowTimestamp(), loginTimestamp);
	}
	
	public static int getPlayerDataBasePlayingTime(String targePlayerNameString) {// 获得数据库存储的游戏时间
		Statement statement;
		int playerPlayingTimeInDatabase=0;
		try {
			statement=WLogin.mySQL.getConnection().createStatement();
			ResultSet result = statement
					.executeQuery("SELECT * FROM playerplayingtime WHERE playername = '" + targePlayerNameString + "';");
			while (result.next()) {
				playerPlayingTimeInDatabase=result.getInt(2);
			}
			result.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return playerPlayingTimeInDatabase;
	}

	public static void unLogin(Player player) {// 使玩家退出登录
		boolean done = true;
		if (player == null) {
			return;
		}
		String playerNameString = player.getName();
		Statement statement;
		PlayerPlayingTime playerplayingtime = new PlayerPlayingTime();
		try {
			statement = WLogin.mySQL.getConnection().createStatement();
			ResultSet result = statement
					.executeQuery("SELECT * FROM playerplayingtime WHERE playername = '" + playerNameString + "';");
			while (result.next()) {
				playerplayingtime.setPlayerNameString(result.getString(1));
				playerplayingtime.setMin(result.getInt(2));
			}
			int playingtime = getTimeDifferenceMinutes(getNowTimestamp(), loginListHashMap.get(playerNameString));
			if (playerplayingtime.getPlayerNameString() == null
					|| playerplayingtime.getPlayerNameString().length() == 0) {
				statement.executeUpdate("INSERT INTO playerplayingtime(playername,min)VALUES('" + playerNameString
						+ "'," + playingtime + ")");
				result.close();
				statement.close();
			} else {
				playingtime = playingtime + playerplayingtime.getMin();
				statement.executeUpdate("UPDATE playerplayingtime SET min = " + playingtime + " WHERE playername = '"
						+ playerNameString + "'");
				result.close();
				statement.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			WLogin.main.getLogger().warning("更新玩家 " + playerNameString + " 的已游玩时间失败");
			done = false;
		}
		if (done) {
			loginListHashMap.remove(playerNameString);
		}
		return;
	}

	public static void unLogin(CommandSender sender, Player player) {// 使玩家退出登录
		BukkitRunnable bukkitRunnable = new BukkitRunnable() {

			@Override
			public void run() {
				// TODO 自动生成的方法存根
				// TODO Auto-generated method stub
				boolean done = true;
				if (player == null) {
					sender.sendMessage(WLogin.unknownPlayerEntityMsg());
					return;
				}
				String playerNameString = player.getName();
				Statement statement;
				PlayerPlayingTime playerplayingtime = new PlayerPlayingTime();
				try {
					statement = WLogin.mySQL.getConnection().createStatement();
					ResultSet result = statement.executeQuery(
							"SELECT * FROM playerplayingtime WHERE playername = '" + playerNameString + "';");
					while (result.next()) {
						playerplayingtime.setPlayerNameString(result.getString(1));
						playerplayingtime.setMin(result.getInt(2));
					}
					int playingtime = getTimeDifferenceMinutes(getNowTimestamp(),
							loginListHashMap.get(playerNameString));
					if (playerplayingtime.getPlayerNameString() == null
							|| playerplayingtime.getPlayerNameString().length() == 0) {
						statement.executeUpdate("INSERT INTO playerplayingtime(playername,min)VALUES('"
								+ playerNameString + "'," + playingtime + ")");
						result.close();
						statement.close();
					} else {
						playingtime = playingtime + playerplayingtime.getMin();
						statement.executeUpdate("UPDATE playerplayingtime SET min = " + playingtime
								+ " WHERE playername = '" + playerNameString + "'");
						result.close();
						statement.close();
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					WLogin.main.getLogger().warning("更新玩家 " + playerNameString + " 的已游玩时间失败");
					done = false;
				}
				if (done) {
					loginListHashMap.remove(playerNameString);
				}
				return;
			}
		};
		bukkitRunnable.runTaskAsynchronously(WLogin.main);
	}

	public static boolean changePassword(CommandSender sender, String targePlayerNameString, String newPasswordString) {// 修改密码
		// TODO Auto-generated method stub
		String senderNameString;
		Player player = null;
		if (sender instanceof Player) {
			player = (Player) sender;
			senderNameString = player.getName();
		} else {
			senderNameString = "ADMIN";
		}
		PlayerPassword playerPassword = new PlayerPassword();
		Statement statement;
		try {
			statement = WLogin.mySQL.getConnection().createStatement();
			ResultSet result = statement
					.executeQuery("SELECT * FROM playerpassword WHERE playername = '" + targePlayerNameString + "';");
			while (result.next()) {
				playerPassword.setPlayerNameString(result.getString(1));
				playerPassword.setPlayerPasswordString(result.getString(2));
			}
			if (senderNameString.equals("ADMIN")) {
				statement.executeUpdate("INSERT INTO playeroldpassword(sendername,playername,time,oldpassword)VALUES('"
						+ senderNameString + "','" + targePlayerNameString + "', now() ,'"
						+ playerPassword.getPlayerPasswordString() + "')");
			} else {
				statement.executeUpdate(
						"INSERT INTO playeroldpassword(sendername,playername,time,oldpassword,ip)VALUES('"
								+ senderNameString + "','" + targePlayerNameString + "', now() ,'"
								+ playerPassword.getPlayerPasswordString() + "',inet_aton('"
								+ getPlayerIPAddress(player) + "'))");
			}
			statement.executeUpdate("UPDATE playerpassword SET password = '" + newPasswordString
					+ "' WHERE playername = '" + targePlayerNameString + "'");
			result.close();
			statement.close();
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			WLogin.main.getLogger().warning("更新玩家 " + targePlayerNameString + " 的密码失败");
			return false;
		}
		return true;
	}

	public static Timestamp getNowTimestamp() { // 获得现在的时间戳
		Timestamp timestamp = new Timestamp(new Date().getTime());
		return timestamp;
	}

	public static String getPlayerIPAddress(Player player) {// 获得玩家的IP地址
		return player.getAddress().getAddress().getHostAddress();
	}

	public static int getTimeDifferenceMinutes(Timestamp nowTimestamp, Timestamp oldTimestamp) {// 获得时间戳的分钟差
		long t1 = nowTimestamp.getTime();
		long t2 = oldTimestamp.getTime();
		int hours = (int) ((t1 - t2) / (1000 * 60 * 60));
		int minutes = (int) (((t1 - t2) / 1000 - hours * (60 * 60)) / 60 + hours * 60);
		return minutes;
	}

	public static void initTable() {// 初始化表
		BukkitRunnable bukkitRunnable = new BukkitRunnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Statement statement;
				try {
					statement = WLogin.mySQL.getConnection().createStatement();
					String sql = "CREATE TABLE IF NOT EXISTS `playerpassword`(" + "`playername` VARCHAR(40) NOT NULL,"
							+ "`password` VARCHAR(40) NOT NULL," + "`ip` bigint(20) NOT NULL,"
							+ "PRIMARY KEY ( `playername` )" + ")ENGINE=InnoDB DEFAULT CHARSET=utf8;";
					statement.executeUpdate(sql);
					statement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					WLogin.main.getLogger().warning("创建 playerpassword 失败");
				}
				try {
					statement = WLogin.mySQL.getConnection().createStatement();
					String sql = "CREATE TABLE IF NOT EXISTS `playerlogindata`(" + "`playername` VARCHAR(40) NOT NULL,"
							+ "`logintime` TIMESTAMP NOT NULL," + "`loginable` TINYINT(1) NOT NULL,"
							+ "`ip` bigint(20) NOT NULL,"
							+ "FOREIGN KEY (playername) REFERENCES playerpassword(playername)"
							+ ")ENGINE=InnoDB DEFAULT CHARSET=utf8;";
					statement.executeUpdate(sql);
					statement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					WLogin.main.getLogger().warning("创建 playerlogindata 失败");
				}
				try {
					statement = WLogin.mySQL.getConnection().createStatement();
					String sql = "CREATE TABLE IF NOT EXISTS `playerplayingtime`("
							+ "`playername` VARCHAR(40) NOT NULL," + "`min` INT NOT NULL,"
							+ "FOREIGN KEY (playername) REFERENCES playerpassword(playername)," + "UNIQUE (playername)"
							+ ")ENGINE=InnoDB DEFAULT CHARSET=utf8;";
					statement.executeUpdate(sql);
					statement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					WLogin.main.getLogger().warning("创建 playerplayingtime 失败");
				}
				try {
					statement = WLogin.mySQL.getConnection().createStatement();
					String sql = "CREATE TABLE IF NOT EXISTS `playeroldpassword`("
							+ "`sendername` VARCHAR(40) NOT NULL," + "`playername` VARCHAR(40) NOT NULL,"
							+ "`time` TIMESTAMP NOT NULL," + "`oldpassword` VARCHAR(40) NOT NULL," + "`ip` bigint(20),"
							+ "FOREIGN KEY (playername) REFERENCES playerpassword(playername)"
							+ ")ENGINE=InnoDB DEFAULT CHARSET=utf8;";
					statement.executeUpdate(sql);
					statement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					WLogin.main.getLogger().warning("创建 playeroldpassword 失败");
				}
				try {
					statement = WLogin.mySQL.getConnection().createStatement();
					String sql = "CREATE TABLE IF NOT EXISTS `playeristeenagers`("
							+ "`playername` VARCHAR(40) NOT NULL,"
							+ "FOREIGN KEY (playername) REFERENCES playerpassword(playername),"
							+ "UNIQUE ( `playername` )" + ")ENGINE=InnoDB DEFAULT CHARSET=utf8;";
					statement.executeUpdate(sql);
					statement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					WLogin.main.getLogger().warning("创建 playeristeenagers 失败");
				}
				try {
					statement = WLogin.mySQL.getConnection().createStatement();
					String sql = "CREATE TABLE IF NOT EXISTS `banplayerdata`(" + "`playername` VARCHAR(40) NOT NULL,"
							+ "`reason` VARCHAR(300) NOT NULL," + "`time` TIMESTAMP NOT NULL," + "`time_long` INT,"
							+ "FOREIGN KEY (playername) REFERENCES playerpassword(playername)"
							+ ")ENGINE=InnoDB DEFAULT CHARSET=utf8;";
					statement.executeUpdate(sql);
					statement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					WLogin.main.getLogger().warning("创建 banplayerdata 失败");
				}
				WLogin.main.getLogger().info("表初始化完成");
			}
		};

		bukkitRunnable.runTaskAsynchronously(WLogin.main);
	}
}
