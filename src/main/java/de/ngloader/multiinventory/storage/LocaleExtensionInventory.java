package de.ngloader.multiinventory.storage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

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
	public void loadInventory(Player player, String id) {
		Path path = new File(MultiInventory.getInstance().getDataFolder(), String.format("/inventory/%s/%s.json", player.getUniqueId().toString(), id)).toPath();

		if(Files.notExists(path))
			return;

		YamlConfiguration config = new YamlConfiguration();

		try {
			config.load(path.toFile());
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
			 player.kickPlayer("Error loading inventory");
		}

		if(config.contains("health"))
			player.setHealth(config.getDouble("health"));
		if(config.contains("healthScale"))
			player.setHealthScale(config.getDouble("healthScale"));
		if(config.contains("food"))
			player.setFoodLevel(config.getInt("food"));
		if(config.contains("level"))
			player.setLevel(config.getInt("level"));

		if(config.contains("content")) {
			List<?> content = config.getList("content");
			player.getInventory().setContents(content.toArray(new ItemStack[content.size()]));
		}
		if(config.contains("armor")) {
			List<?> armor = config.getList("armor");
			player.getInventory().setArmorContents(armor.toArray(new ItemStack[armor.size()]));
		}
		if(config.contains("extra")) {
			List<?> extra = config.getList("extra");
			player.getInventory().setExtraContents(extra.toArray(new ItemStack[extra.size()]));
		}
		if(config.contains("potionEffect")) {
			List<?> potionEffect = config.getList("potionEffect");
			Arrays.asList(potionEffect.toArray(new PotionEffect[potionEffect.size()])).stream().filter(effect -> effect != null).forEach(effect -> player.addPotionEffect(effect, true));
		}
	}

	@Override
	public void saveInventory(Player player, String id) {
		Path path = new File(MultiInventory.getInstance().getDataFolder(), String.format("/inventory/%s/%s.json", player.getUniqueId().toString(), id)).toPath();

		if (Files.notExists(path))
			try {
				Files.createDirectories(path.getParent());
				Files.createFile(path);
			} catch (IOException e) {
				e.printStackTrace();
			}

		YamlConfiguration config = new YamlConfiguration();

		PlayerInventory inventory = player.getInventory();

		config.set("health", player.getHealth());
		config.set("healthScale", player.getHealthScale());
		config.set("food", player.getFoodLevel());
		config.set("level", player.getLevel());

		if(inventory.getContents() != null)
			config.set("content", inventory.getContents());
		if(inventory.getArmorContents() != null)
			config.set("armor", inventory.getArmorContents());
		if(inventory.getExtraContents() != null)
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