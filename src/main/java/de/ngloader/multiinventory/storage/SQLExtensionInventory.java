package de.ngloader.multiinventory.storage;

import org.bukkit.entity.Player;

import net.imprex.storage.api.StorageProvider;
import net.imprex.storage.sql.SQLStorage;

public class SQLExtensionInventory implements StorageProvider<SQLStorage>, ExtensionInventory {

	@Override
	public void registered(SQLStorage storage) {
	}

	@Override
	public void unregistered() {
	}

	@Override
	public void loadInventory(Player player, String inventoryId) {
		// TODO fill
	}

	@Override
	public void saveInventory(Player player, String inventoryId) {
		// TODO fill
	}
}