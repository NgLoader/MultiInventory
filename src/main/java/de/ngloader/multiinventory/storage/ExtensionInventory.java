package de.ngloader.multiinventory.storage;

import org.bukkit.World;
import org.bukkit.entity.Player;

import net.imprex.storage.api.StorageExtension;

public interface ExtensionInventory extends StorageExtension {

	void loadInventory(Player player, World world);
	void saveInventory(Player player, World world);
}