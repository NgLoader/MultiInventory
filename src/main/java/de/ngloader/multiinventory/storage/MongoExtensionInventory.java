package de.ngloader.multiinventory.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

import de.ngloader.multiinventory.MultiInventory;
import net.imprex.storage.api.StorageProvider;
import net.imprex.storage.mongo.MongoStorage;

public class MongoExtensionInventory implements StorageProvider<MongoStorage>, ExtensionInventory {

	private static final Document EMPTY_DOCUMENT = new Document();

	private MongoCollection<Document> collection;

	@Override
	public void registered(MongoStorage storage) {
		this.collection = storage.getCollection("multiinventory");
	}

	@Override
	public void unregistered() {
	}

	@Override
	public void loadInventory(Player player, World world) {
		String inventoryId = MultiInventory.getConfiguration().worlds.getOrDefault(world.getName(), MultiInventory.getConfiguration().defaultWorldId);

		Document document = this.collection.find(Filters.eq("uuid", player.getUniqueId().toString())).first();

		YamlConfiguration config = new YamlConfiguration();

		if(document != null && document.containsKey(String.format("worlds.%s", inventoryId))) {
			try {
				config.loadFromString(document.get(String.format("worlds.%s", inventoryId), EMPTY_DOCUMENT).getString("content"));
			} catch (InvalidConfigurationException e) {
				e.printStackTrace();
				 player.kickPlayer("Error loading inventory");
			}
		} else {
			Path pathCopy = Paths.get(
					MultiInventory.getInstance().getDataFolder().getAbsolutePath(),
					"default",
					MultiInventory.getConfiguration().defaultInventorys.getOrDefault(world.getName(), MultiInventory.getConfiguration().defaultInventoryId) + ".yml");

			if(Files.exists(pathCopy))
				try {
					config.load(pathCopy.toFile());
				} catch (IOException | InvalidConfigurationException e) {
					e.printStackTrace();
				}
			else
				return;
		}

		player.setHealthScale(config.getDouble("healthScale"));
		player.setHealth(config.getDouble("health"));
		player.setFoodLevel(config.getInt("food"));
		player.setLevel(config.getInt("level"));

		PlayerInventory inventory = player.getInventory();

		List<?> content = config.getList("content");
		inventory.setContents(content.toArray(new ItemStack[content.size()]));
		List<?> armor = config.getList("armor");
		inventory.setArmorContents(armor.toArray(new ItemStack[armor.size()]));
		List<?> extra = config.getList("extra");
		inventory.setExtraContents(extra.toArray(new ItemStack[extra.size()]));

		List<?> potionEffect = config.getList("potionEffect");
		Arrays.asList(potionEffect.toArray(new PotionEffect[potionEffect.size()])).stream()
				.filter(effect -> effect != null).forEach(effect -> player.addPotionEffect(effect, true));
	}

	@Override
	public void saveInventory(Player player, World world) {
		String inventoryId = MultiInventory.getConfiguration().worlds.getOrDefault(world.getName(), MultiInventory.getConfiguration().defaultWorldId);

		YamlConfiguration config = new YamlConfiguration();
		PlayerInventory inventory = player.getInventory();

		config.set("health", player.getHealth());
		config.set("healthScale", player.getHealthScale());
		config.set("food", player.getFoodLevel());
		config.set("level", player.getLevel());

		config.set("content", inventory.getContents());
		config.set("armor", inventory.getArmorContents());
		config.set("extra", inventory.getExtraContents());

		config.set("potionEffect", player.getActivePotionEffects());

		if(this.collection.find(Filters.eq("uuid", player.getUniqueId().toString())).first() != null)
			this.collection.updateOne(Filters.eq("uuid", player.getUniqueId().toString()), new Document("$set", new Document(String.format("worlds.%s", inventoryId), config.saveToString())));
		else
			this.collection.insertOne(new Document("uuid", player.getUniqueId().toString()).append(String.format("worlds.%s", inventoryId), config.saveToString()));
	}
}