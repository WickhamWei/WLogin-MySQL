package wickham.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import wickham.main.login.WLoginSYS;

public class PlayerQuitGameListener implements Listener {

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (WLoginSYS.isLogin(player.getName())) {
			WLoginSYS.unLogin(player);
			return;
		}
	}
}
