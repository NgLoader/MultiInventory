package de.ngloader.multiinventory.listener;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.ngloader.multiinventory.Config;
import de.ngloader.multiinventory.MultiInventory;
import de.ngloader.multiinventory.storage.ExtensionInventory;
import net.imprex.storage.api.StorageApi;

public class PlayerListener implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		clearPlayer(event.getPlayer());
		StorageApi.getStorageService().getExtension(ExtensionInventory.class).loadInventory(event.getPlayer(), searchId(event.getPlayer().getWorld()));
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		StorageApi.getStorageService().getExtension(ExtensionInventory.class).saveInventory(event.getPlayer(), searchId(event.getPlayer().getWorld()));
	}

	@EventHandler
	public void onWorldChange(PlayerChangedWorldEvent event) {
		ExtensionInventory extensionInventory = StorageApi.getStorageService().getExtension(ExtensionInventory.class);

		if(searchId(event.getFrom()).equals(searchId(event.getPlayer().getWorld())))
			return;

		extensionInventory.saveInventory(event.getPlayer(), searchId(event.getFrom()));
		clearPlayer(event.getPlayer());
		extensionInventory.loadInventory(event.getPlayer(), searchId(event.getPlayer().getWorld()));
	}

	private String searchId(World world) {
		Config config = MultiInventory.getConfiguration();

		return config.worlds.getOrDefault(world.getName().toLowerCase(), config.defaultWorldId);
	}

	private void clearPlayer(Player player) {
		player.setHealth(20);
		player.setHealthScale(20);
		player.setFoodLevel(20);
		player.setLevel(0);
		player.getInventory().clear();
		player.getActivePotionEffects().stream().forEach(effect -> player.removePotionEffect(effect.getType()));
	}
}