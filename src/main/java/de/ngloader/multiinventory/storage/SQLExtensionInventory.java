package de.ngloader.multiinventory.storage;

import org.bukkit.World;
import org.bukkit.entity.Player;

import de.ngloader.multiinventory.MultiInventory;
import de.ngloader.multiinventory.config.MultiInventoryPlayerConfig;
import de.ngloader.multiinventory.config.MultiInventoryWorldConfig;
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
	public MultiInventoryWorldConfig getWorldConfig(World world) {
		MultiInventoryWorldConfig worldConfig = MultiInventory.getConfiguration().worlds.get(world.getName());
		return worldConfig != null ? worldConfig : new MultiInventoryWorldConfig();
	}

	@Override
	public MultiInventoryPlayerConfig getTemplate(World world) {
		// TODO Auto-generated method stub
		return new MultiInventoryPlayerConfig();
	}

	@Override
	public void setTemplate(World world, MultiInventoryPlayerConfig player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setInventory(Player player, World world, MultiInventoryPlayerConfig config) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MultiInventoryPlayerConfig getInventory(Player player, World world) {
		// TODO Auto-generated method stub
		return new MultiInventoryPlayerConfig();
	}
}