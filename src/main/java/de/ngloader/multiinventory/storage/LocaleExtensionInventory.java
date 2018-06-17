package de.ngloader.multiinventory.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;

import de.ngloader.multiinventory.MultiInventory;
import net.imprex.storage.api.StorageProvider;

public class LocaleExtensionInventory implements StorageProvider<LocaleStorage>, ExtensionInventory {

	@Override
	public void registered(LocaleStorage storage) {
	}

	@Override
	public void unregistered() {
	}

	@Override
	public void loadInventory(Player player, World world) {
		String inventoryId = MultiInventory.getConfiguration().worlds.getOrDefault(world.getName(), MultiInventory.getConfiguration().defaultWorldId);

		Path path = Paths.get(MultiInventory.getInstance().getDataFolder().getAbsolutePath(), String.format("/inventory/%s/%s.yml", player.getUniqueId().toString(), inventoryId));

		if(Files.notExists(path)) {
			try {
				Files.createDirectories(path.getParent());

				Path pathCopy = Paths.get(
						MultiInventory.getInstance().getDataFolder().getAbsolutePath(),
						"default",
						MultiInventory.getConfiguration().defaultInventorys.getOrDefault(world.getName(), MultiInventory.getConfiguration().defaultInventoryId) + ".yml");
				if(Files.exists(pathCopy))
					Files.copy(pathCopy, path);
				else
					return;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		YamlConfiguration config = new YamlConfiguration();

		try {
			config.load(path.toFile());
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
			 player.kickPlayer("Error loading inventory");
		}

		player.setHealthScale(config.getDouble("healthScale"));
		player.setHealth(config.getDouble("health"));
		player.setFoodLevel(config.getInt("food"));
		player.setLevel(config.getInt("level"));

		List<?> content = config.getList("content");
		player.getInventory().setContents(content.toArray(new ItemStack[content.size()]));
		List<?> armor = config.getList("armor");
		player.getInventory().setArmorContents(armor.toArray(new ItemStack[armor.size()]));
		List<?> extra = config.getList("extra");
		player.getInventory().setExtraContents(extra.toArray(new ItemStack[extra.size()]));
		List<?> potionEffect = config.getList("potionEffect");
		Arrays.asList(potionEffect.toArray(new PotionEffect[potionEffect.size()])).stream().filter(effect -> effect != null).forEach(effect -> player.addPotionEffect(effect, true));
	}

	@Override
	public void saveInventory(Player player, World world) {
		String inventoryId = MultiInventory.getConfiguration().worlds.getOrDefault(world.getName(), MultiInventory.getConfiguration().defaultWorldId);

		Path path = Paths.get(MultiInventory.getInstance().getDataFolder().getAbsolutePath(), String.format("/inventory/%s/%s.yml", player.getUniqueId().toString(), inventoryId));

		if (Files.notExists(path))
			try {
				Files.createDirectories(path.getParent());
				Files.createFile(path);
			} catch (IOException e) {
				e.printStackTrace();
			}

		YamlConfiguration config = new YamlConfiguration();

		config.set("health", player.getHealth());
		config.set("healthScale", player.getHealthScale());
		config.set("food", player.getFoodLevel());
		config.set("level", player.getLevel());

		PlayerInventory inventory = player.getInventory();

		config.set("content", inventory.getContents());
		config.set("armor", inventory.getArmorContents());
		config.set("extra", inventory.getExtraContents());

		config.set("potionEffect", player.getActivePotionEffects());

		try {
			config.save(path.toFile());
		} catch (IOException e) {
			e.printStackTrace();
			player.kickPlayer("Error saveing inventory");
		}
	}
}