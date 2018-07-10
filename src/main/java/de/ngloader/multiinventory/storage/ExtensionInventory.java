package de.ngloader.multiinventory.storage;

import org.bukkit.World;
import org.bukkit.entity.Player;

import de.ngloader.multiinventory.config.MultiInventoryPlayerConfig;
import de.ngloader.multiinventory.config.MultiInventoryWorldConfig;
import net.imprex.storage.api.StorageExtension;

public interface ExtensionInventory extends StorageExtension {

	MultiInventoryWorldConfig getWorldConfig(World world);

	MultiInventoryPlayerConfig getTemplate(World world);
	void setTemplate(World world, MultiInventoryPlayerConfig playerConfig);

	void setInventory(Player player, World world, MultiInventoryPlayerConfig playerConfig);
	MultiInventoryPlayerConfig getInventory(Player player, World world);
}