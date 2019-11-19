package wickham.listener;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


public class PlayerJoinListener implements Listener{
	
	@EventHandler
	public void listen(PlayerJoinEvent event) {// 玩家加入
		Player player = event.getPlayer();
		event.setJoinMessage("");
		player.setGameMode(GameMode.SPECTATOR);
		return;
	}

}
