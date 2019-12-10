package wickham.main.login;

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
	private static HashSet<String> opLoginList= new HashSet<String>();

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
			statement=WLogin.main.getDatabase().getConnection().createStatement();
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
			int firstDataNum=(page-1)*5;
			ResultSet resultData = statement
					.executeQuery("SELECT sendername,playername,time,oldpassword,inet_ntoa(ip) FROM playeroldpassword WHERE playername = '"
							+ targePlayerNameString + "'" + " limit "+firstDataNum+",5;");
			while (resultData.next()) {//获取数据
				PlayerOldPassword playerOldPassword=new PlayerOldPassword();
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
			for (PlayerOldPassword data : allDatas) {//发送数据给玩家
				senderPlayer.sendMessage(
						ChatColor.YELLOW + "-第 " + ChatColor.GREEN + tempNumber + ChatColor.YELLOW + " 条数据-");
				senderPlayer.sendMessage(ChatColor.YELLOW + "修改密码的时间 " + ChatColor.GREEN
						+ data.getTime().toString() + ChatColor.YELLOW + " 密码修改者 " + ChatColor.GREEN
						+ data.getSenderNameString());
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
					if(player.isOp()) {
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
		String targePlayerNameString=targePlayer.getName();
		Timestamp loginTimestamp=playerLoginList.get(targePlayerNameString);
		if(loginTimestamp==null) {
			return 0;
		}
		return getTimeDifferenceMinutes(getNowTimestamp(), loginTimestamp);
	}
	
	public static int getPlayerDataBasePlayingTime(String targePlayerNameString) {// 获得数据库存储的游戏时间
		Statement statement;
		int playerPlayingTimeInDatabase=0;
		try {
			statement=WLogin.main.getDatabase().getConnection().createStatement();
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
		if (done) {
			playerLoginList.remove(playerNameString);
			if(player.isOp()) {
				opLoginList.remove(playerNameString);
			}
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
					statement = WLogin.main.getDatabase().getConnection().createStatement();
					ResultSet result = statement.executeQuery(
							"SELECT * FROM playerplayingtime WHERE playername = '" + playerNameString + "';");
					while (result.next()) {
						playerplayingtime.setPlayerNameString(result.getString(1));
						playerplayingtime.setMin(result.getInt(2));
					}
					int playingtime = getTimeDifferenceMinutes(getNowTimestamp(),
							playerLoginList.get(playerNameString));
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
					playerLoginList.remove(playerNameString);
				}
				return;
			}
		};
		bukkitRunnable.runTaskAsynchronously(WLogin.main);
	}

	public static boolean changePassword(Player targePlayer,String oldPasswordString, String newPasswordString) {// 修改密码
		// TODO Auto-generated method stub
		if (!checkPassword(targePlayer, oldPasswordString)) {
			targePlayer.sendMessage(ChatColor.RED+"旧密码错误");
			return false;
		}
		String targePlayerNameString=targePlayer.getName();
		String senderNameString=targePlayer.getName();
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
		for (String opNameString:opLoginList) {
			WLogin.main.getServer().getPlayer(opNameString).sendMessage(msgString);
		}
	}
	
	public static void warningToOpSbPasswordError(String errorPasswordPlayerName) {
		sendMsgToAllOp(ChatColor.RED+"玩家 "+ChatColor.YELLOW+errorPasswordPlayerName+ChatColor.RED+" 输错了他的密码");
		WLogin.main.getServer().getLogger().warning("玩家 "+errorPasswordPlayerName+" 输错了他的密码");
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
	
	public static boolean checkPasswordForm(Player player,String passwordString) {
		if(!(passwordString.length()>=6&&passwordString.length()<=18)) {
			player.sendMessage(ChatColor.RED+"密码长度应大于5或小于19");
			return false;
		}else {
			if(!(passwordString.matches("^.*[a-zA-Z]+.*$") && passwordString.matches("^.*[0-9]+.*$"))) {
				player.sendMessage(ChatColor.RED+"密码应由数字和字母组成");
				return false;
			}else {
				return true;
			}	
		}
		
	}
	
	public static void checkLastLoginDataNormal(Player player) {
		String targePlayerNameString=player.getName();
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
			int firstDataNum=dataLength-2;
			PlayerLoginData playerLoginData = new PlayerLoginData();
			ResultSet resultData = statement
					.executeQuery("SELECT logintime,loginable,inet_ntoa(ip) FROM playerlogindata WHERE playername = '"
							+ targePlayerNameString + "'" + "limit "+firstDataNum+",1;");
			while (resultData.next()) {//获取数据
				playerLoginData.setLoginTime(resultData.getTimestamp(1));
				playerLoginData.setLoginable(resultData.getBoolean(2));
				playerLoginData.setIpString(resultData.getString(3));
			}
			Boolean abNormalBoolean=false;
			if(!playerLoginData.getIpString().equals(getPlayerIPAddress(player))) {
				//发送数据给玩家
				player.sendMessage(ChatColor.YELLOW+"-检测到异地登录记录-");
				abNormalBoolean=true;
			}
			if(!playerLoginData.isLoginable()) {
				player.sendMessage(ChatColor.YELLOW+"-检测到登录失败记录-");
				abNormalBoolean=true;
			}
			if(abNormalBoolean) {
				player.sendMessage(ChatColor.RED+"-异常信息-");
				player.sendMessage(ChatColor.YELLOW + "尝试登录的时间 " + ChatColor.GREEN
						+ playerLoginData.getLoginTime().toString() + ChatColor.YELLOW + " 是否登录成功 " + ChatColor.GREEN
						+ booleanToString(playerLoginData.isLoginable()));
				player.sendMessage(ChatColor.YELLOW + "尝试登录的IP地址 " + ChatColor.GREEN + playerLoginData.getIpString());
			}else {
				player.sendMessage(ChatColor.GREEN+"-账号状态正常-");
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
