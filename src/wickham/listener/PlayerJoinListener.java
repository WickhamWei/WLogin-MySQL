package wickham.listener;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import wickham.main.login.WLoginSYS;


public class PlayerJoinListener implements Listener{
	
	@EventHandler
	public void listen(PlayerJoinEvent event) {// 玩家加入
		Player player = event.getPlayer();
		event.setJoinMessage("");
		player.setGameMode(GameMode.SPECTATOR);
		if (WLoginSYS.isLogin(event.getPlayer().getName())) {
			return;
		} else if (!WLoginSYS.isRegister(event.getPlayer().getName())) {
			PlayerUnLoginLimitListener.noRegisterMsg(event.getPlayer());
		} else {
			PlayerUnLoginLimitListener.noLoginMsg(event.getPlayer());
		}
	}

}
