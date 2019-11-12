package wickham.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import wickham.main.WLogin;
import wickham.main.login.WLoginSYS;

public class PlayerUnLoginListener implements Listener{
	
	public void noRegister(Player player) {// 未注册的信息
		WLogin.sendMsg(player, ChatColor.RED + "你还没注册，输入/register 密码 重复密码 来注册");
	}

	public void noLogin(Player player) {// 未登录的信息
		WLogin.sendMsg(player, ChatColor.RED + "你还没登陆，输入/login 密码 来登陆");
	}
	
	@EventHandler
	public void stop(PlayerMoveEvent event) {
		if (WLoginSYS.isLogin(event.getPlayer().getName())) {
			return;
		} else if (!WLoginSYS.isRegister(event.getPlayer().getName())) {
			noRegister(event.getPlayer());
			event.setCancelled(true);
		} else {
			noLogin(event.getPlayer());
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void stop(PlayerArmorStandManipulateEvent event) {
		if (WLoginSYS.isLogin(event.getPlayer().getName())) {
			return;
		} else if (!WLoginSYS.isRegister(event.getPlayer().getName())) {
			noRegister(event.getPlayer());
			event.setCancelled(true);
		} else {
			noLogin(event.getPlayer());
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void stop(PlayerBedEnterEvent event) {
		if (WLoginSYS.isLogin(event.getPlayer().getName())) {
			return;
		} else if (!WLoginSYS.isRegister(event.getPlayer().getName())) {
			noRegister(event.getPlayer());
			event.setCancelled(true);
		} else {
			noLogin(event.getPlayer());
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void stop(PlayerBedLeaveEvent event) {
		if (WLoginSYS.isLogin(event.getPlayer().getName())) {
			return;
		} else if (!WLoginSYS.isRegister(event.getPlayer().getName())) {
			noRegister(event.getPlayer());
			event.setSpawnLocation(false);
		} else {
			noLogin(event.getPlayer());
			event.setSpawnLocation(false);
		}
	}

	@EventHandler
	public void stop(PlayerBucketEmptyEvent event) {
		if (WLoginSYS.isLogin(event.getPlayer().getName())) {
			return;
		} else if (!WLoginSYS.isRegister(event.getPlayer().getName())) {
			noRegister(event.getPlayer());
			event.setCancelled(true);
		} else {
			noLogin(event.getPlayer());
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void stop(AsyncPlayerChatEvent event) {
		if (WLoginSYS.isLogin(event.getPlayer().getName())) {
			return;
		} else if (!WLoginSYS.isRegister(event.getPlayer().getName())) {
			noRegister(event.getPlayer());
			event.setCancelled(true);
		} else {
			noLogin(event.getPlayer());
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void stop(PlayerDropItemEvent event) {
		if (WLoginSYS.isLogin(event.getPlayer().getName())) {
			return;
		} else if (!WLoginSYS.isRegister(event.getPlayer().getName())) {
			noRegister(event.getPlayer());
			event.setCancelled(true);
		} else {
			noLogin(event.getPlayer());
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void stop(PlayerInteractEvent event) {
		if (WLoginSYS.isLogin(event.getPlayer().getName())) {
			return;
		} else if (!WLoginSYS.isRegister(event.getPlayer().getName())) {
			noRegister(event.getPlayer());
			event.setCancelled(true);
		} else {
			noLogin(event.getPlayer());
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void stop(InventoryOpenEvent event) {
		if (WLoginSYS.isLogin(event.getPlayer().getName())) {
			return;
		} else if (!WLoginSYS.isRegister(event.getPlayer().getName())) {
			noRegister((Player) event.getPlayer());
			event.setCancelled(true);
		} else {
			noLogin((Player) event.getPlayer());
			event.setCancelled(true);
		}
	}
}
