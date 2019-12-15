package wickham.main.mysql.tables;

import java.sql.Date;

public class PlayerDailyPlayTime {
	String playerNameString;
	Date date;
	int min;
	public String getPlayerNameString() {
		return playerNameString;
	}
	public void setPlayerNameString(String playerNameString) {
		this.playerNameString = playerNameString;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getMin() {
		return min;
	}
	public void setMin(int min) {
		this.min = min;
	}
	
}
