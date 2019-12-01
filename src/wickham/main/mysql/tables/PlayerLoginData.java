package wickham.main.mysql.tables;

import java.sql.Timestamp;

public class PlayerLoginData {
	String playerNameString;
	Timestamp loginTime;
	boolean loginable;
	String ipString;
	public String getPlayerNameString() {
		return playerNameString;
	}
	public void setPlayerNameString(String playerNameString) {
		this.playerNameString = playerNameString;
	}
	public Timestamp getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(Timestamp loginTime) {
		this.loginTime = loginTime;
	}
	public boolean isLoginable() {
		return loginable;
	}
	public void setLoginable(boolean loginable) {
		this.loginable = loginable;
	}
	public String getIpString() {
		return ipString;
	}
	public void setIpString(String ipString) {
		this.ipString = ipString;
	}
}
