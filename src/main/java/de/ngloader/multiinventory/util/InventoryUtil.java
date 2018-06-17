package de.ngloader.multiinventory.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.ngloader.multiinventory.MultiInventory;

public class InventoryUtil {

	public static void saveInventory(Player player, World world) {
		Path path = new File(MultiInventory.getInstance().getDataFolder(), String.format("/inventory/%s.json", player.getUniqueId().toString())).toPath();

		if (Files.notExists(path))
			try {
				Files.createDirectories(path.getParent());
				Files.createFile(path);
			} catch (IOException e) {
				e.printStackTrace();
			}

		YamlConfiguration config = new YamlConfiguration();

		PlayerInventory inventory = player.getInventory();

		if(inventory.getContents() != null)
			config.set("content", inventory.getContents());
		if(inventory.getArmorContents() != null)
			config.set("armor", inventory.getArmorContents());
		if(inventory.getExtraContents() != null)
			config.set("extra", inventory.getExtraContents());
		config.set("potionEffect", player.getActivePotionEffects());

		try {
			config.save(path.toFile());
			clearPlayer(player);
		} catch (IOException e) {
			e.printStackTrace();
			player.kickPlayer("§cUnerwarteter §4Fehler §cbeim speichern deines §4Inventares");
		}
	}

	public static void loadInventory(Player player, World world) {
		Path path = new File(MultiInventory.getInstance().getDataFolder(), String.format("/inventory/%s.json", player.getUniqueId().toString())).toPath();

		if(Files.notExists(path))
			return;

		YamlConfiguration config = new YamlConfiguration();

		try {
			config.load(path.toFile());
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
			 player.kickPlayer("§cUnerwarteter §4Fehler §cbeim laden deines §4Inventares");
		}

		clearPlayer(player);

		if(config.contains(world.getName() + ".content")) {
			List<?> content = config.getList("content");
			player.getInventory().setContents(content.toArray(new ItemStack[content.size()]));
		}
		if(config.contains(world.getName() + ".armor")) {
			List<?> armor = config.getList("armor");
			player.getInventory().setArmorContents(armor.toArray(new ItemStack[armor.size()]));
		}
		if(config.contains(world.getName() + ".extra")) {
			List<?> extra = config.getList("extra");
			player.getInventory().setExtraContents(extra.toArray(new ItemStack[extra.size()]));
		}
		if(config.contains(world.getName() + ".potionEffect")) {
			List<?> potionEffect = config.getList("potionEffect");
			Arrays.asList(potionEffect.toArray(new PotionEffect[potionEffect.size()])).stream().filter(effect -> effect != null).forEach(effect -> player.addPotionEffect(effect, true));
		}
	}

	private static final void clearPlayer(Player player) {
		player.getInventory().clear();
		player.getActivePotionEffects().stream().forEach(effect -> player.removePotionEffect(effect.getType()));
	}
}