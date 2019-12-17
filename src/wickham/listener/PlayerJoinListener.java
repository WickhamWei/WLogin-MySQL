package wickham.listener;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import wickham.main.WLogin;
import wickham.main.login.WLoginSYS;


public class PlayerJoinListener implements Listener{
	
	@EventHandler
	public void listen(PlayerJoinEvent event) {// 玩家加入
		Player player = event.getPlayer();
		event.setJoinMessage("");
		if(!WLoginSYS.teenageersChecker(player)) {
			player.kickPlayer(WLogin.kickTeenagersMsg());
			return;
		}
		if(WLoginSYS.isBanning(player.getName())) {
			player.kickPlayer("您已被封禁，封禁理由是 "+WLoginSYS.getMaxBanTimeReason(player.getName())+" 封禁时间剩余 "+WLoginSYS.getMaxBanTimeMin(player.getName())+" 分钟");
			return;
		}
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
