package de.ngloader.multiinventory.storage;

import org.bukkit.entity.Player;

import net.imprex.storage.api.StorageExtension;

public interface ExtensionInventory extends StorageExtension {

	void loadInventory(Player player, String inventoryId);
	void saveInventory(Player player, String inventoryId);
}