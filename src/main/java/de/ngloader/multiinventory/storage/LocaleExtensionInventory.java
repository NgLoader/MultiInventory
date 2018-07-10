package de.ngloader.multiinventory.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import de.ngloader.multiinventory.MultiInventory;
import de.ngloader.multiinventory.MultiInventoryUtil;
import de.ngloader.multiinventory.config.MultiInventoryPlayerConfig;
import de.ngloader.multiinventory.config.MultiInventoryWorldConfig;
import net.imprex.storage.api.StorageProvider;

public class LocaleExtensionInventory implements StorageProvider<LocaleStorage>, ExtensionInventory {

	@Override
	public void registered(LocaleStorage storage) {
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
		MultiInventoryWorldConfig worldConfig = this.getWorldConfig(world);
		String inventory = worldConfig.templateId != null ? worldConfig.templateId : MultiInventory.getConfiguration().defaultInventoryId;
		Path path = Paths.get(MultiInventory.getInstance().getDataFolder().getAbsolutePath(), String.format("/template/%s.yml", inventory));

		if(Files.exists(path)) {
			YamlConfiguration config = new YamlConfiguration();

			try {
				config.load(path.toFile());

				return MultiInventoryUtil.convertYamlToPlayerConfig(config);
			} catch (IOException | InvalidConfigurationException e) {
				e.printStackTrace();
			}
		}
		return new MultiInventoryPlayerConfig();
	}

	@Override
	public void setTemplate(World world, MultiInventoryPlayerConfig playerConfig) {
		MultiInventoryWorldConfig worldConfig = this.getWorldConfig(world);
		String inventory = worldConfig.templateId != null ? worldConfig.templateId : MultiInventory.getConfiguration().defaultTemplateId;
		Path path = Paths.get(MultiInventory.getInstance().getDataFolder().getAbsolutePath(), String.format("/template/%s.yml", inventory));

		try {
			if(Files.notExists(path)) {
				Files.createDirectories(path.getParent());
				Files.createFile(path);
			}

			MultiInventoryUtil.convertPlayerConfigToYaml(playerConfig).save(path.toFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setInventory(Player player, World world, MultiInventoryPlayerConfig playerConfig) {
		try {
			MultiInventoryWorldConfig worldConfig = this.getWorldConfig(world);
			String inventory = worldConfig.templateId != null ? worldConfig.templateId : MultiInventory.getConfiguration().defaultInventoryId;
			Path path = Paths.get(MultiInventory.getInstance().getDataFolder().getAbsolutePath(), String.format("/inventory/%s/%s.yml", player.getUniqueId().toString(), inventory));

			if (Files.notExists(path)) {
				Files.createDirectories(path.getParent());
				Files.createFile(path);
			}

			MultiInventoryUtil.convertPlayerConfigToYaml(playerConfig).save(path.toFile());
		} catch (Exception e) {
			e.printStackTrace();
			player.kickPlayer("Error saveing inventory");
		}
	}

	@Override
	public MultiInventoryPlayerConfig getInventory(Player player, World world) {
		try {
			MultiInventoryWorldConfig worldConfig = this.getWorldConfig(world);
			String inventory = worldConfig.templateId != null ? worldConfig.templateId : MultiInventory.getConfiguration().defaultInventoryId;
			Path path = Paths.get(MultiInventory.getInstance().getDataFolder().getAbsolutePath(), String.format("/inventory/%s/%s.yml", player.getUniqueId().toString(), inventory));

			if (Files.notExists(path))
				return this.getTemplate(world);

			YamlConfiguration config = new YamlConfiguration();

			config.load(path.toFile());

			return MultiInventoryUtil.convertYamlToPlayerConfig(config);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new MultiInventoryPlayerConfig();
	}
}