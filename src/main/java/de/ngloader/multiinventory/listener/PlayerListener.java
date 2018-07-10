package de.ngloader.multiinventory.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.ngloader.multiinventory.MultiInventory;
import de.ngloader.multiinventory.MultiInventoryUtil;
import de.ngloader.multiinventory.event.MultiInventoryLoadPlayerEvent;
import de.ngloader.multiinventory.event.MultiInventorySavePlayerEvent;
import de.ngloader.multiinventory.storage.ExtensionInventory;
import net.imprex.storage.api.StorageApi;

public class PlayerListener implements Listener {

	public static final List<UUID> NO_SAVE_WHILE_ERROR = new ArrayList<UUID>();

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		try {
			Player player = event.getPlayer();

			if(NO_SAVE_WHILE_ERROR.contains(player.getUniqueId()))
				NO_SAVE_WHILE_ERROR.remove(player.getUniqueId());

			clearPlayer(event.getPlayer());

			MultiInventoryLoadPlayerEvent inventoryLoadPlayerEvent = new MultiInventoryLoadPlayerEvent(
					player,
					player.getWorld(),
					StorageApi.getStorageService().getExtension(ExtensionInventory.class).getInventory(event.getPlayer(), event.getPlayer().getWorld()));
			Bukkit.getPluginManager().callEvent(inventoryLoadPlayerEvent);

			MultiInventoryUtil.loadPlayerConfig(event.getPlayer(), event.getPlayer().getWorld(), inventoryLoadPlayerEvent.getPlayerConfig());
		} catch(Exception e) {
			e.printStackTrace();

			NO_SAVE_WHILE_ERROR.add(event.getPlayer().getUniqueId());

			try {
				event.getPlayer().kickPlayer(MultiInventory.getConfiguration().kickReason);
			} catch(Exception e2) {
				event.getPlayer().kickPlayer("MultiInventory: FATAL ERROR");
			}
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		try {
			Player player = event.getPlayer();

			if(NO_SAVE_WHILE_ERROR.contains(player.getUniqueId())) {
				NO_SAVE_WHILE_ERROR.remove(player.getUniqueId());
				return;
			}

			MultiInventorySavePlayerEvent inventorySavePlayerEvent = new MultiInventorySavePlayerEvent(player, player.getWorld(), MultiInventoryUtil.getPlayerConfig(player));
			Bukkit.getPluginManager().callEvent(inventorySavePlayerEvent);

			StorageApi.getStorageService().getExtension(ExtensionInventory.class).setInventory(event.getPlayer(), event.getPlayer().getWorld(), inventorySavePlayerEvent.getPlayerConfig());
		} catch(Exception e) {
			e.printStackTrace();

			try {
				event.getPlayer().kickPlayer(MultiInventory.getConfiguration().kickReason);
			} catch(Exception e2) {
				event.getPlayer().kickPlayer("MultiInventory: FATAL ERROR");
			}
		}
	}

	@EventHandler
	public void onWorldChange(PlayerChangedWorldEvent event) {
		try {
			Player player = event.getPlayer();
			ExtensionInventory extensionInventory = StorageApi.getStorageService().getExtension(ExtensionInventory.class);

			MultiInventorySavePlayerEvent inventorySavePlayerEvent = new MultiInventorySavePlayerEvent(player, event.getFrom(), MultiInventoryUtil.getPlayerConfig(player));
			Bukkit.getPluginManager().callEvent(inventorySavePlayerEvent);

			extensionInventory.setInventory(event.getPlayer(), event.getFrom(), inventorySavePlayerEvent.getPlayerConfig());

			if(!getWorldId(event.getFrom()).equals(getWorldId(event.getPlayer().getWorld())))
				clearPlayer(event.getPlayer());

			MultiInventoryLoadPlayerEvent inventoryLoadPlayerEvent = new MultiInventoryLoadPlayerEvent(player, player.getWorld(), extensionInventory.getInventory(player, player.getWorld()));
			Bukkit.getPluginManager().callEvent(inventoryLoadPlayerEvent);

			MultiInventoryUtil.loadPlayerConfig(player, player.getWorld(), inventoryLoadPlayerEvent.getPlayerConfig());
		} catch(Exception e) {
			e.printStackTrace();

			NO_SAVE_WHILE_ERROR.add(event.getPlayer().getUniqueId());

			try {
				event.getPlayer().kickPlayer(MultiInventory.getConfiguration().kickReason);
			} catch(Exception e2) {
				event.getPlayer().kickPlayer("MultiInventory: FATAL ERROR");
			}
		}
	}

	private String getWorldId(World world) {
		String worldId = MultiInventory.getConfiguration().worlds.get(world.getName()).inventoryId;

		if(worldId == null)
			worldId = MultiInventory.getConfiguration().defaultInventoryId;

		return worldId != null ? worldId : "inventory-unknown";
	}

	private void clearPlayer(Player player) {
		player.setBedSpawnLocation(null);
		player.setDisplayName(player.getName());
		player.setGameMode(GameMode.SURVIVAL);
		player.setAllowFlight(false);
		player.setWalkSpeed(.2F);
		player.setFlySpeed(.2F);
		player.setExhaustion(0);
		player.setTotalExperience(0);
		player.setHealth(20);
		player.setHealthScale(20);
		player.setFoodLevel(20);
		player.setFireTicks(0);
		player.setLevel(0);
		player.setExp(0);
		player.getInventory().clear();
		player.getEnderChest().clear();
		player.getActivePotionEffects().stream().forEach(effect -> player.removePotionEffect(effect.getType()));
	}
}