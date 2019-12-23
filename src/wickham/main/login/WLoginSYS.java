package wickham.main.login;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import wickham.main.WLogin;
import wickham.main.mysql.tables.*;

public abstract class WLoginSYS {

	private static HashMap<String, Timestamp> playerLoginList = new HashMap<String, Timestamp>(); // 存放登录的玩家名字和登录的时间戳
	private static HashSet<String> opLoginList = new HashSet<String>();

	public static boolean isLogin(String playerNameString) { // 判断玩家是否登录
		return playerLoginList.containsKey(playerNameString);
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
					statement = WLogin.main.getDatabase().getConnection().createStatement();
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
			statement = WLogin.main.getDatabase().getConnection().createStatement();
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

	public static void checkFailLoginData(Player senderPlayer, String targePlayerNameString, int page) {
		Statement statement;
		LinkedHashSet<PlayerLoginData> allDatas = new LinkedHashSet<PlayerLoginData>();
		try {
			statement = WLogin.main.getDatabase().getConnection().createStatement();
			ResultSet resultNum = statement.executeQuery("SELECT count(*) FROM playerlogindata WHERE playername = '"
					+ targePlayerNameString + "' and loginable = 0");
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
			if (page <= 0) {// 使页数不超过最小值
				page = 1;
			}
			int firstDataNum = (page - 1) * 5;
			ResultSet resultData = statement
					.executeQuery("SELECT logintime,loginable,inet_ntoa(ip) FROM playerlogindata WHERE playername = '"
							+ targePlayerNameString + "' and loginable = 0" + " limit " + firstDataNum + ",5;");
			while (resultData.next()) {// 获取数据
				PlayerLoginData playerLoginData = new PlayerLoginData();
				playerLoginData.setLoginTime(resultData.getTimestamp(1));
				playerLoginData.setLoginable(resultData.getBoolean(2));
				playerLoginData.setIpString(resultData.getString(3));
				allDatas.add(playerLoginData);
			}
			senderPlayer.sendMessage(
					ChatColor.YELLOW + "---玩家 " + ChatColor.GREEN + targePlayerNameString + ChatColor.RED + " 登录失败数据"
							+ ChatColor.YELLOW + " - 第 " + ChatColor.GREEN + page + ChatColor.YELLOW + " 页 - 总共 "
							+ ChatColor.GREEN + (int) Math.ceil((float) dataLength / 5) + ChatColor.YELLOW + " 页---");
			int tempNumber = 1;
			for (PlayerLoginData data : allDatas) {// 发送数据给玩家
				senderPlayer.sendMessage(
						ChatColor.YELLOW + "-第 " + ChatColor.GREEN + tempNumber + ChatColor.YELLOW + " 条数据-");
				senderPlayer
						.sendMessage(ChatColor.YELLOW + "尝试登录的时间 " + ChatColor.GREEN + data.getLoginTime().toString());
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

	public static void checkLoginData(Player senderPlayer, String targePlayerNameString, int page) {
		Statement statement;
		LinkedHashSet<PlayerLoginData> allDatas = new LinkedHashSet<PlayerLoginData>();
		try {
			statement = WLogin.main.getDatabase().getConnection().createStatement();
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
			if (page <= 0) {// 使页数不超过最小值
				page = 1;
			}
			int firstDataNum = (page - 1) * 5;
			ResultSet resultData = statement
					.executeQuery("SELECT logintime,loginable,inet_ntoa(ip) FROM playerlogindata WHERE playername = '"
							+ targePlayerNameString + "'" + " limit " + firstDataNum + ",5;");
			while (resultData.next()) {// 获取数据
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
			for (PlayerLoginData data : allDatas) {// 发送数据给玩家
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

	public static void checkOldPasswordData(Player senderPlayer, String targePlayerNameString, int page) {
		Statement statement;
		LinkedHashSet<PlayerOldPassword> allDatas = new LinkedHashSet<PlayerOldPassword>();
		try {
			statement = WLogin.main.getDatabase().getConnection().createStatement();
			ResultSet resultNum = statement.executeQuery(
					"SELECT count(*) FROM playeroldpassword WHERE playername = '" + targePlayerNameString + "'");
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
			if (page <= 0) {// 使页数不超过最小值
				page = 1;
			}
			int firstDataNum = (page - 1) * 5;
			ResultSet resultData = statement.executeQuery(
					"SELECT sendername,playername,time,oldpassword,inet_ntoa(ip) FROM playeroldpassword WHERE playername = '"
							+ targePlayerNameString + "'" + " limit " + firstDataNum + ",5;");
			while (resultData.next()) {// 获取数据
				PlayerOldPassword playerOldPassword = new PlayerOldPassword();
				playerOldPassword.setSenderNameString(resultData.getString(1));
				playerOldPassword.setPlayerNameString(resultData.getString(2));
				playerOldPassword.setTime(resultData.getTimestamp(3));
				playerOldPassword.setOldPasswordString(resultData.getString(4));
				playerOldPassword.setIpString(resultData.getString(5));
				allDatas.add(playerOldPassword);
			}
			senderPlayer.sendMessage(ChatColor.YELLOW + "---玩家 " + ChatColor.GREEN + targePlayerNameString
					+ ChatColor.YELLOW + " 密码修改数据 - 第 " + ChatColor.GREEN + page + ChatColor.YELLOW + " 页 - 总共 "
					+ ChatColor.GREEN + (int) Math.ceil((float) dataLength / 5) + ChatColor.YELLOW + " 页---");
			int tempNumber = 1;
			for (PlayerOldPassword data : allDatas) {// 发送数据给玩家
				senderPlayer.sendMessage(
						ChatColor.YELLOW + "-第 " + ChatColor.GREEN + tempNumber + ChatColor.YELLOW + " 条数据-");
				senderPlayer.sendMessage(ChatColor.YELLOW + "修改密码的时间 " + ChatColor.GREEN + data.getTime().toString()
						+ ChatColor.YELLOW + " 密码修改者 " + ChatColor.GREEN + data.getSenderNameString());
				senderPlayer.sendMessage(ChatColor.YELLOW + "修改者的IP地址 " + ChatColor.GREEN + data.getIpString());
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

	public static boolean checkPassword(Player player, String passwordString) {// 检查密码是否正确
		String playerNameString = player.getName();
		Statement statement;
		String passwordFromDBString = null;
		try {
			statement = WLogin.main.getDatabase().getConnection().createStatement();
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
					Statement statement = WLogin.main.getDatabase().getConnection().createStatement();
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
					playerLoginList.put(playerNameString, getNowTimestamp());
					if (player.isOp()) {
						opLoginList.add(playerNameString);
					}
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
					Statement statement = WLogin.main.getDatabase().getConnection().createStatement();
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
		for (String playerNameString : playerLoginList.keySet()) {
			unLogin(WLogin.main.getServer().getPlayer(playerNameString));
		}
	}

	public static int getPlayerNowPlayingTime(Player targePlayer) {// 获得本次游戏的时间
		String targePlayerNameString = targePlayer.getName();
		Timestamp loginTimestamp = playerLoginList.get(targePlayerNameString);
		if (loginTimestamp == null) {
			return 0;
		}
		return getTimeDifferenceMinutes(getNowTimestamp(), loginTimestamp);
	}

	public static int getPlayerDataBasePlayingTime(String targePlayerNameString) {// 获得数据库存储的游戏时间
		Statement statement;
		int playerPlayingTimeInDatabase = 0;
		try {
			statement = WLogin.main.getDatabase().getConnection().createStatement();
			ResultSet result = statement.executeQuery(
					"SELECT * FROM playerplayingtime WHERE playername = '" + targePlayerNameString + "';");
			while (result.next()) {
				playerPlayingTimeInDatabase = result.getInt(2);
			}
			result.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return playerPlayingTimeInDatabase;
	}

	public static int getPlayerTodayPlayingTime(String targePlayerNameString) {// 获得数据库存储的今日游戏时间
		Statement statement;
		int playerTodayPlayingTimeInDatabase = 0;
		try {
			statement = WLogin.main.getDatabase().getConnection().createStatement();
			ResultSet result = statement.executeQuery("SELECT * FROM playerdailyplaytime WHERE playername = '"
					+ targePlayerNameString + "' and date = curdate();");
			while (result.next()) {
				playerTodayPlayingTimeInDatabase = result.getInt(3);
			}
			result.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return playerTodayPlayingTimeInDatabase;
	}

	public static boolean isTeenagers(String playerNameString) {
		Statement statement = null;
		ResultSet result = null;
		boolean teenagersBoolean = false;
		String playerNameInDatabase = null;
		try {
			statement = WLogin.main.getDatabase().getConnection().createStatement();
			result = statement
					.executeQuery("SELECT * FROM playeristeenagers WHERE playername = '" + playerNameString + "';");
			while (result.next()) {
				playerNameInDatabase = result.getString(1);
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			try {
				statement.close();
				result.close();
			} catch (SQLException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
		if (!(playerNameInDatabase == null)) {
			teenagersBoolean = true;
		}
		return teenagersBoolean;
	}

	public static void setTeenagers(String playerNameString) {
		Statement statement = null;
		try {
			if (isTeenagers(playerNameString)) {
				return;
			} else {
				statement = WLogin.main.getDatabase().getConnection().createStatement();
				statement.executeUpdate("INSERT INTO playeristeenagers(playername)VALUES('" + playerNameString + "')");
				statement.close();
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public static void setBanData(CommandSender sender, String targePlayerNameString, String reasonString,
			int timeLengthMin) {
		Statement statement = null;
		String senderNameString = null;
		if (timeLengthMin<=0) {
			timeLengthMin=1;
		}
		try {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				senderNameString = player.getName();
			} else {
				senderNameString = "ADMIN";
			}
			statement = WLogin.main.getDatabase().getConnection().createStatement();
			statement.executeUpdate(
					"INSERT INTO banplayerdata(playername,senderplayername,reason,time,time_long)VALUES('"
							+ targePlayerNameString + "','" + senderNameString + "','" + reasonString + "',now(),"
							+ timeLengthMin + ")");
			statement.close();
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public static void removeBanData(CommandSender sender, String targePlayerNameString) {// 移除正在服刑的数据
		Statement statement = null;
		LinkedHashSet<BanPlayerData> allDatas = new LinkedHashSet<BanPlayerData>();
		try {
			statement = WLogin.main.getDatabase().getConnection().createStatement();
			ResultSet resultNum = statement.executeQuery(
					"SELECT count(*) FROM banplayerdata WHERE playername = '" + targePlayerNameString + "'");
			int dataLength = 0;
			while (resultNum.next()) {
				dataLength = resultNum.getInt(1);// 获取数据长度
			}
			if (dataLength == 0) {// 没有数据
				resultNum.close();
				statement.close();
				if (sender instanceof Player) {
					Player player = (Player) sender;
					player.sendMessage(WLogin.unknownPlayerEntityMsg());
				} else {
					sender.sendMessage("未知对象或没有数据");
				}
				return;
			}
			ResultSet resultData = statement
					.executeQuery("SELECT * FROM banplayerdata WHERE playername = '" + targePlayerNameString + "'");
			while (resultData.next()) {// 获取数据
				BanPlayerData banPlayerData = new BanPlayerData();
				banPlayerData.setPlayerNameString(resultData.getString(1));
				banPlayerData.setSenderPlayerNameString(resultData.getString(2));
				banPlayerData.setReasonString(resultData.getString(3));
				banPlayerData.setTime(resultData.getTimestamp(4));
				banPlayerData.setTimelong(resultData.getInt(5));
				allDatas.add(banPlayerData);
			}
			int tempCount = 0;
			for (BanPlayerData data : allDatas) {
				if (getTimeDifferenceMinutes(getNowTimestamp(), data.getTime()) >= data.getTimelong()) {// 服刑完毕
					continue;
				} else {
					String sqlString=("DELETE FROM banplayerdata where playername ='" + targePlayerNameString
							+ "' and time = ?");
					PreparedStatement preparedStatement=WLogin.main.getDatabase().getConnection().prepareStatement(sqlString);
					preparedStatement.setTimestamp(1, data.getTime());
					preparedStatement.execute();
					tempCount++;
				}
			}
			if (tempCount == 0) {// 没有数据
				resultNum.close();
				statement.close();
				if (sender instanceof Player) {
					Player player = (Player) sender;
					player.sendMessage(WLogin.unknownPlayerEntityMsg());
				} else {
					sender.sendMessage("未知对象或没有数据");
				}
				return;
			}
			if (sender instanceof Player) {
				Player player = (Player) sender;
				player.sendMessage(
						ChatColor.YELLOW + "删除了 " + ChatColor.GREEN + tempCount + ChatColor.YELLOW + " 条封禁中的数据，对象已解封");
			} else {
				sender.sendMessage("删除了 " + tempCount + " 条封禁中的数据，对象已解封");
			}
			resultNum.close();
			resultData.close();
			statement.close();
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public static boolean isBanning(String targePlayerNameString) {
		Statement statement = null;
		LinkedHashSet<BanPlayerData> allDatas = new LinkedHashSet<BanPlayerData>();
		try {
			statement = WLogin.main.getDatabase().getConnection().createStatement();
			ResultSet resultNum = statement.executeQuery(
					"SELECT count(*) FROM banplayerdata WHERE playername = '" + targePlayerNameString + "'");
			int dataLength = 0;
			while (resultNum.next()) {
				dataLength = resultNum.getInt(1);// 获取数据长度
			}
			if (dataLength == 0) {// 没有数据
				resultNum.close();
				statement.close();
				return false;
			}
			ResultSet resultData = statement
					.executeQuery("SELECT * FROM banplayerdata WHERE playername = '" + targePlayerNameString + "'");
			while (resultData.next()) {// 获取数据
				BanPlayerData banPlayerData = new BanPlayerData();
				banPlayerData.setPlayerNameString(resultData.getString(1));
				banPlayerData.setSenderPlayerNameString(resultData.getString(2));
				banPlayerData.setReasonString(resultData.getString(3));
				banPlayerData.setTime(resultData.getTimestamp(4));
				banPlayerData.setTimelong(resultData.getInt(5));
				allDatas.add(banPlayerData);
			}
			int tempCount = 0;
			for (BanPlayerData data : allDatas) {
				if (getTimeDifferenceMinutes(getNowTimestamp(), data.getTime()) >= data.getTimelong()) {// 服刑完毕
					continue;
				} else {
					tempCount++;
				}
			}
			if (tempCount > 0) {
				resultNum.close();
				resultData.close();
				statement.close();
				return true;
			}
			resultNum.close();
			resultData.close();
			statement.close();
			return false;
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}

	public static int getMaxBanTimeMin(String targePlayerNameString) {
		Statement statement = null;
		LinkedHashSet<BanPlayerData> allDatas = new LinkedHashSet<BanPlayerData>();
		try {
			statement = WLogin.main.getDatabase().getConnection().createStatement();
			ResultSet resultNum = statement.executeQuery(
					"SELECT count(*) FROM banplayerdata WHERE playername = '" + targePlayerNameString + "'");
			int dataLength = 0;
			while (resultNum.next()) {
				dataLength = resultNum.getInt(1);// 获取数据长度
			}
			if (dataLength == 0) {// 没有数据
				resultNum.close();
				statement.close();
				return 0;
			}
			ResultSet resultData = statement
					.executeQuery("SELECT * FROM banplayerdata WHERE playername = '" + targePlayerNameString + "'");
			while (resultData.next()) {// 获取数据
				BanPlayerData banPlayerData = new BanPlayerData();
				banPlayerData.setPlayerNameString(resultData.getString(1));
				banPlayerData.setSenderPlayerNameString(resultData.getString(2));
				banPlayerData.setReasonString(resultData.getString(3));
				banPlayerData.setTime(resultData.getTimestamp(4));
				banPlayerData.setTimelong(resultData.getInt(5));
				allDatas.add(banPlayerData);
			}
			int maxBanTimeMin = 0;
			for (BanPlayerData data : allDatas) {
				if (getTimeDifferenceMinutes(getNowTimestamp(), data.getTime()) >= data.getTimelong()) {// 服刑完毕
					continue;
				} else {
					if (data.getTimelong()
							- getTimeDifferenceMinutes(getNowTimestamp(), data.getTime()) > maxBanTimeMin) {
						maxBanTimeMin = data.getTimelong()
								- getTimeDifferenceMinutes(getNowTimestamp(), data.getTime());
					}
				}
			}
			resultNum.close();
			resultData.close();
			statement.close();
			return maxBanTimeMin;
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return 0;
	}

	public static String getMaxBanTimeReason(String targePlayerNameString) {
		Statement statement = null;
		LinkedHashSet<BanPlayerData> allDatas = new LinkedHashSet<BanPlayerData>();
		try {
			statement = WLogin.main.getDatabase().getConnection().createStatement();
			ResultSet resultNum = statement.executeQuery(
					"SELECT count(*) FROM banplayerdata WHERE playername = '" + targePlayerNameString + "'");
			int dataLength = 0;
			while (resultNum.next()) {
				dataLength = resultNum.getInt(1);// 获取数据长度
			}
			if (dataLength == 0) {// 没有数据
				resultNum.close();
				statement.close();
				return null;
			}
			ResultSet resultData = statement
					.executeQuery("SELECT * FROM banplayerdata WHERE playername = '" + targePlayerNameString + "'");
			while (resultData.next()) {// 获取数据
				BanPlayerData banPlayerData = new BanPlayerData();
				banPlayerData.setPlayerNameString(resultData.getString(1));
				banPlayerData.setSenderPlayerNameString(resultData.getString(2));
				banPlayerData.setReasonString(resultData.getString(3));
				banPlayerData.setTime(resultData.getTimestamp(4));
				banPlayerData.setTimelong(resultData.getInt(5));
				allDatas.add(banPlayerData);
			}
			int maxBanTimeMin = 0;
			String maxBanTimeReasonString = null;
			for (BanPlayerData data : allDatas) {
				if (getTimeDifferenceMinutes(getNowTimestamp(), data.getTime()) >= data.getTimelong()) {// 服刑完毕
					continue;
				} else {
					if (data.getTimelong()
							- getTimeDifferenceMinutes(getNowTimestamp(), data.getTime()) > maxBanTimeMin) {
						maxBanTimeMin = data.getTimelong()
								- getTimeDifferenceMinutes(getNowTimestamp(), data.getTime());
						maxBanTimeReasonString = data.getReasonString();
					}
				}
			}
			resultNum.close();
			resultData.close();
			statement.close();
			return maxBanTimeReasonString;
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	public static void getBanData(Player senderPlayer, String targePlayerNameString, int page) {
		Statement statement = null;
		LinkedHashSet<BanPlayerData> allDatas = new LinkedHashSet<BanPlayerData>();
		try {
			statement = WLogin.main.getDatabase().getConnection().createStatement();
			ResultSet resultNum = statement.executeQuery(
					"SELECT count(*) FROM banplayerdata WHERE playername = '" + targePlayerNameString + "'");
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
			if (page > (int) Math.ceil((float) dataLength / 2)) {// 使页数不超过最大值
				page = (int) Math.ceil((float) dataLength / 2);
			}
			if (page <= 0) {// 使页数不超过最小值
				page = 1;
			}
			int firstDataNum = (page - 1) * 2;
			ResultSet resultData = statement.executeQuery("SELECT * FROM banplayerdata WHERE playername = '"
					+ targePlayerNameString + "'" + " limit " + firstDataNum + ",2;");
			while (resultData.next()) {// 获取数据
				BanPlayerData banPlayerData = new BanPlayerData();
				banPlayerData.setPlayerNameString(resultData.getString(1));
				banPlayerData.setSenderPlayerNameString(resultData.getString(2));
				banPlayerData.setReasonString(resultData.getString(3));
				banPlayerData.setTime(resultData.getTimestamp(4));
				banPlayerData.setTimelong(resultData.getInt(5));
				allDatas.add(banPlayerData);
			}
			senderPlayer.sendMessage(ChatColor.YELLOW + "---玩家 " + ChatColor.GREEN + targePlayerNameString
					+ ChatColor.YELLOW + " 封禁数据 - 第 " + ChatColor.GREEN + page + ChatColor.YELLOW + " 页 - 总共 "
					+ ChatColor.GREEN + (int) Math.ceil((float) dataLength / 2) + ChatColor.YELLOW + " 页---");
			int tempNumber = 1;
			for (BanPlayerData data : allDatas) {// 发送数据给玩家
				senderPlayer.sendMessage(
						ChatColor.YELLOW + "-第 " + ChatColor.GREEN + tempNumber + ChatColor.YELLOW + " 条数据-");
				senderPlayer.sendMessage(ChatColor.YELLOW + "封禁时间 " + ChatColor.GREEN + data.getTime().toString()
						+ ChatColor.YELLOW + " 封禁者 " + ChatColor.GREEN + data.getSenderPlayerNameString());
				int nowMin = data.getTimelong();
				int nowTheHour = nowMin / 60;
				int nowTheMin = nowMin % 60;
				senderPlayer.sendMessage(ChatColor.YELLOW + "封禁时长 " + ChatColor.GREEN + nowTheHour + ChatColor.YELLOW
						+ " 小时 " + ChatColor.GREEN + nowTheMin + ChatColor.YELLOW + " 分钟");
				boolean endOfSentence = false;
				if (getTimeDifferenceMinutes(getNowTimestamp(), data.getTime()) >= data.getTimelong()) {
					endOfSentence = true;
				}
				senderPlayer.sendMessage(ChatColor.YELLOW + "是否服刑完毕 " + booleanToString(endOfSentence));
				senderPlayer.sendMessage(ChatColor.YELLOW + "封禁理由 " + ChatColor.BLUE + data.getReasonString());
				tempNumber++;
			}
			resultNum.close();
			resultData.close();
			statement.close();
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public static void removeTeenagers(String playerNameString) {
		Statement statement = null;
		try {
			if (isTeenagers(playerNameString)) {
				statement = WLogin.main.getDatabase().getConnection().createStatement();
				statement.executeUpdate("DELETE FROM playeristeenagers where playername ='" + playerNameString + "'");
				statement.close();
			} else {
				return;
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public static Integer getTeenagersCount() {
		Statement statement = null;
		ResultSet resultCount = null;
		int teenagersCount = 0;
		try {
			statement = WLogin.main.getDatabase().getConnection().createStatement();
			resultCount = statement.executeQuery("SELECT count(*) FROM playeristeenagers");
			while (resultCount.next()) {
				teenagersCount = resultCount.getInt(1);// 获取数据长度
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			try {
				statement.close();
				resultCount.close();
			} catch (SQLException e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
		return teenagersCount;
	}

	public static boolean teenageersChecker(Player player) {// 防沉迷验证是否通过
		if(!player.isOnline()) {
			return true;
		}
		String playerNameString = player.getName();
		int playerTodayPlayingTime = 0;
		if (WLoginSYS.isLogin(player)) {
			playerTodayPlayingTime = playerTodayPlayingTime + WLoginSYS.getPlayerNowPlayingTime(player);
		}
		playerTodayPlayingTime = playerTodayPlayingTime + WLoginSYS.getPlayerTodayPlayingTime(playerNameString);
		if (isTeenagers(playerNameString)) {
			if (playerTodayPlayingTime >= 60*3) {
				return false;
			} else {
				if (playerTodayPlayingTime >= 60 * 2 && playerTodayPlayingTime <= 60 * 2 + 5) {
					player.sendMessage(ChatColor.YELLOW + "您的游戏时间只剩" + (60 * 3 - playerTodayPlayingTime) + "分钟");
				}
				if (playerTodayPlayingTime >= 60 * 2 + 30 && playerTodayPlayingTime <= 60 * 2 + 35) {
					player.sendMessage(ChatColor.YELLOW + "您的游戏时间只剩" + (60 * 3 - playerTodayPlayingTime) + "分钟");
				}
				if (playerTodayPlayingTime >= 60 * 2 + 50) {
					player.sendMessage(
							ChatColor.RED + "您的游戏时间只剩" + (60 * 3 - playerTodayPlayingTime) + "分钟，请注意保护随身携带的物品，以防丢失");
				}
				return true;
			}
		} else {
			return true;
		}
	}

	public static void unLogin(Player player) {// 使玩家退出登录
		boolean done = true;
		if (player == null) {
			return;
		}
		String playerNameString = player.getName();
		Statement statement;
		PlayerPlayingTime playerplayingtime = new PlayerPlayingTime();
		PlayerDailyPlayTime playerdailyplaytime = new PlayerDailyPlayTime();
		try {
			statement = WLogin.main.getDatabase().getConnection().createStatement();
			ResultSet result = statement
					.executeQuery("SELECT * FROM playerplayingtime WHERE playername = '" + playerNameString + "';");
			while (result.next()) {
				playerplayingtime.setPlayerNameString(result.getString(1));
				playerplayingtime.setMin(result.getInt(2));
			}
			int playingtime = getTimeDifferenceMinutes(getNowTimestamp(), playerLoginList.get(playerNameString));
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
		try {
			statement = WLogin.main.getDatabase().getConnection().createStatement();
			ResultSet result = statement.executeQuery("SELECT * FROM playerdailyplaytime WHERE playername = '"
					+ playerNameString + "' and date = curdate();");
			while (result.next()) {
				playerdailyplaytime.setPlayerNameString(result.getString(1));
				playerdailyplaytime.setDate(result.getDate(2));
				playerdailyplaytime.setMin(result.getInt(3));
			}
			int playingtime = getTimeDifferenceMinutes(getNowTimestamp(), playerLoginList.get(playerNameString));// 这次玩了了多久
			if (playerdailyplaytime.getPlayerNameString() == null
					|| playerdailyplaytime.getPlayerNameString().length() == 0) {
				statement.executeUpdate("INSERT INTO playerdailyplaytime(playername,date,min)VALUES('"
						+ playerNameString + "',curdate()," + playingtime + ")");
				result.close();
				statement.close();
			} else {
				playingtime = playingtime + playerdailyplaytime.getMin();
				statement.executeUpdate("UPDATE playerdailyplaytime SET min = " + playingtime + " WHERE playername = '"
						+ playerNameString + "' and date = curdate()");
				result.close();
				statement.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			WLogin.main.getLogger().warning("更新玩家 " + playerNameString + " 的当日已游玩时间失败");
		}
		if (done) {
			playerLoginList.remove(playerNameString);
			if (player.isOp()) {
				opLoginList.remove(playerNameString);
			}
		}
		return;
	}

	public static boolean changePassword(Player targePlayer, String oldPasswordString, String newPasswordString) {// 修改密码
		// TODO Auto-generated method stub
		if (!checkPassword(targePlayer, oldPasswordString)) {
			targePlayer.sendMessage(ChatColor.RED + "旧密码错误");
			return false;
		}
		String targePlayerNameString = targePlayer.getName();
		String senderNameString = targePlayer.getName();
		PlayerPassword playerPassword = new PlayerPassword();
		Statement statement;
		try {
			statement = WLogin.main.getDatabase().getConnection().createStatement();
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
								+ getPlayerIPAddress(targePlayer) + "'))");
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

	public static void sendMsgToAllOp(String msgString) {
		for (String opNameString : opLoginList) {
			WLogin.main.getServer().getPlayer(opNameString).sendMessage(msgString);
		}
	}

	public static void warningToOpSbPasswordError(String errorPasswordPlayerName) {
		sendMsgToAllOp(ChatColor.RED + "玩家 " + ChatColor.YELLOW + errorPasswordPlayerName + ChatColor.RED + " 输错了他的密码");
		WLogin.main.getServer().getLogger().warning("玩家 " + errorPasswordPlayerName + " 输错了他的密码");
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

	public static boolean checkPasswordForm(Player player, String passwordString) {
		if (!(passwordString.length() >= 6 && passwordString.length() <= 18)) {
			player.sendMessage(ChatColor.RED + "密码长度应大于5或小于19");
			return false;
		} else {
			if (!(passwordString.matches("^.*[a-zA-Z]+.*$") && passwordString.matches("^.*[0-9]+.*$"))) {
				player.sendMessage(ChatColor.RED + "密码应由数字和字母组成");
				return false;
			} else {
				return true;
			}
		}

	}

	public static void checkLastLoginDataNormal(Player player) {
		String targePlayerNameString = player.getName();
		Statement statement;
		try {
			statement = WLogin.main.getDatabase().getConnection().createStatement();
			ResultSet resultCount = statement.executeQuery(
					"SELECT count(*) FROM playerlogindata WHERE playername = '" + targePlayerNameString + "'");
			int dataLength = 0;
			while (resultCount.next()) {
				dataLength = resultCount.getInt(1);// 获取数据长度
			}
			if (dataLength <= 1) {// 没有数据
				resultCount.close();
				statement.close();
				return;
			}
			int firstDataNum = dataLength - 2;
			PlayerLoginData playerLoginData = new PlayerLoginData();
			ResultSet resultData = statement
					.executeQuery("SELECT logintime,loginable,inet_ntoa(ip) FROM playerlogindata WHERE playername = '"
							+ targePlayerNameString + "'" + "limit " + firstDataNum + ",1;");
			while (resultData.next()) {// 获取数据
				playerLoginData.setLoginTime(resultData.getTimestamp(1));
				playerLoginData.setLoginable(resultData.getBoolean(2));
				playerLoginData.setIpString(resultData.getString(3));
			}
			Boolean abNormalBoolean = false;
			if (!playerLoginData.getIpString().equals(getPlayerIPAddress(player))) {
				// 发送数据给玩家
				player.sendMessage(ChatColor.YELLOW + "-检测到异地登录记录-");
				abNormalBoolean = true;
			}
			if (!playerLoginData.isLoginable()) {
				player.sendMessage(ChatColor.YELLOW + "-检测到登录失败记录-");
				abNormalBoolean = true;
			}
			if (abNormalBoolean) {
				player.sendMessage(ChatColor.RED + "-异常信息-");
				player.sendMessage(ChatColor.YELLOW + "尝试登录的时间 " + ChatColor.GREEN
						+ playerLoginData.getLoginTime().toString() + ChatColor.YELLOW + " 是否登录成功 " + ChatColor.GREEN
						+ booleanToString(playerLoginData.isLoginable()));
				player.sendMessage(ChatColor.YELLOW + "尝试登录的IP地址 " + ChatColor.GREEN + playerLoginData.getIpString());
			} else {
				player.sendMessage(ChatColor.GREEN + "-账号状态正常-");
			}

			resultCount.close();
			resultData.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return;
	}
}
