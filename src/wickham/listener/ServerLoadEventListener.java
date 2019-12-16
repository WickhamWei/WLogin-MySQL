package wickham.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;

import wickham.main.WLogin;
import wickham.updateChecker.WUpdateChecker;

public class ServerLoadEventListener implements Listener {
	@EventHandler
	public void listen(ServerLoadEvent event) {
		if (WLogin.main.isMySQLNormal()) {
			;
		} else {
			WLogin.main.getLogger().warning("WLogin 无法连接到数据库，服务器将强制关闭。");
			WLogin.main.getServer().shutdown();
		}
		WUpdateChecker updateChecker = new WUpdateChecker(WLogin.main.getDescription().getVersion(), WLogin.url);
		if (updateChecker.isUpTodate()) {
			if (updateChecker.isNetworkNormal()) {
				WLogin.main.getLogger().info("WLogin 已是最新版本");
			} else {
				WLogin.main.getLogger().warning("WLogin 无法连接到 Github");
				WLogin.main.getLogger().warning("WLogin 无法检查更新");
			}
		} else {
			WLogin.main.getLogger().info("WLogin 当前版本为 " + updateChecker.getCurrentVersionString());
			WLogin.main.getLogger().info("WLogin 最新版本为 " + updateChecker.getNewestVersionString() + " 发布于 "
					+ updateChecker.getNewestVersionPTimeString());
			WLogin.main.getLogger().info("请及时更新");
		}
		TeenagersListener.enableTeenagersListener();
	}
}
