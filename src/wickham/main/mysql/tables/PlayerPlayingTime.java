package wickham.main.mysql.tables;

public class PlayerPlayingTime {
	String playerNameString;
	int min;
	
	public void setPlayerNameString(String playerNameString) {
		this.playerNameString=playerNameString;
	}
	
	public void setMin(int min) {
		this.min=min;
	}
	
	public String getPlayerNameString() {
		return playerNameString;
	}
	
	public int getMin() {
		return min;
	}
}
