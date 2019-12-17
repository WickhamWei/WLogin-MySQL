package wickham.main.mysql.tables;

import java.sql.Timestamp;

public class BanPlayerData {
	String playerNameString;
	String senderPlayerNameString;
	public String getSenderPlayerNameString() {
		return senderPlayerNameString;
	}
	public void setSenderPlayerNameString(String senderPlayerNameString) {
		this.senderPlayerNameString = senderPlayerNameString;
	}
	String reasonString;
	Timestamp time;
	int timelong;
	public String getPlayerNameString() {
		return playerNameString;
	}
	public void setPlayerNameString(String playerNameString) {
		this.playerNameString = playerNameString;
	}
	public String getReasonString() {
		return reasonString;
	}
	public void setReasonString(String reasonString) {
		this.reasonString = reasonString;
	}
	public Timestamp getTime() {
		return time;
	}
	public void setTime(Timestamp time) {
		this.time = time;
	}
	public int getTimelong() {
		return timelong;
	}
	public void setTimelong(int timelong) {
		this.timelong = timelong;
	}
	
}
