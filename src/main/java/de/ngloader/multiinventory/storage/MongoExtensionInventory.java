package de.ngloader.multiinventory.storage;

import org.bukkit.entity.Player;

import net.imprex.storage.api.StorageProvider;
import net.imprex.storage.mongo.MongoStorage;

public class MongoExtensionInventory implements StorageProvider<MongoStorage>, ExtensionInventory {

	@Override
	public void registered(MongoStorage storage) {
	}

	@Override
	public void unregistered() {
	}

	@Override
	public void loadInventory(Player player, String id) {
		// TODO fill
	}

	@Override
	public void saveInventory(Player player, String id) {
		// TODO fill
	}
}