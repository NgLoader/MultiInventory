package de.ngloader.multiinventory.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.ngloader.multiinventory.util.InventoryUtil;

public class PlayerListener implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		InventoryUtil.loadInventory(event.getPlayer(), event.getPlayer().getWorld());
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		InventoryUtil.saveInventory(event.getPlayer(), event.getPlayer().getWorld());
	}

	@EventHandler
	public void onWorldChange(PlayerChangedWorldEvent event) {
		InventoryUtil.saveInventory(event.getPlayer(), event.getFrom());
		InventoryUtil.loadInventory(event.getPlayer(), event.getPlayer().getWorld());
	}
}