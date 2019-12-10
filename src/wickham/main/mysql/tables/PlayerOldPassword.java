package wickham.main.mysql.tables;

import java.sql.Timestamp;

public class PlayerOldPassword {
	String senderNameString;
	String playerNameString;
	Timestamp time;
	String oldPasswordString;
	String ipString;
	public String getPlayerNameString() {
		return playerNameString;
	}
	public void setPlayerNameString(String playerNameString) {
		this.playerNameString = playerNameString;
	}
	public Timestamp getTime() {
		return time;
	}
	public void setTime(Timestamp time) {
		this.time = time;
	}
	public String getOldPasswordString() {
		return oldPasswordString;
	}
	public void setOldPasswordString(String oldPasswordString) {
		this.oldPasswordString = oldPasswordString;
	}
	public String getIpString() {
		return ipString;
	}
	public void setIpString(String ipString) {
		this.ipString = ipString;
	}
	public String getSenderNameString() {
		return senderNameString;
	}
	public void setSenderNameString(String senderNameString) {
		this.senderNameString = senderNameString;
	}
	
	
}
