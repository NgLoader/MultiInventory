package de.ngloader.multiinventory.storage;

import org.bukkit.World;
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
	public void loadInventory(Player player, World world) {
		// TODO fill
	}

	@Override
	public void saveInventory(Player player, World world) {
		// TODO fill
	}
}