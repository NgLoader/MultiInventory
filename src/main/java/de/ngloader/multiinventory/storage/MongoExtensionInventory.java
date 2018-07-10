package de.ngloader.multiinventory.storage;

import org.bson.Document;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

import de.ngloader.multiinventory.MultiInventory;
import de.ngloader.multiinventory.MultiInventoryUtil;
import de.ngloader.multiinventory.config.MultiInventoryPlayerConfig;
import de.ngloader.multiinventory.config.MultiInventoryWorldConfig;
import net.imprex.storage.api.StorageProvider;
import net.imprex.storage.mongo.MongoStorage;

public class MongoExtensionInventory implements StorageProvider<MongoStorage>, ExtensionInventory {

	private static final Document EMPTY_DOCUMENT = new Document();

	private MongoCollection<Document> collection_inventorys;
	private MongoCollection<Document> collection_templates;

	@Override
	public void registered(MongoStorage storage) {
		this.collection_inventorys = storage.getCollection("multiinventory-inventory");
		this.collection_templates = storage.getCollection("multiinventory-template");
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
		try {
			MultiInventoryWorldConfig worldConfig = this.getWorldConfig(world);
			String template = worldConfig.templateId != null ? worldConfig.templateId : MultiInventory.getConfiguration().defaultTemplateId;

			Document document = this.collection_templates.find(Filters.eq("template", template)).first();

			YamlConfiguration config = new YamlConfiguration();

			if (document != null && document.containsKey("content"))
				config.loadFromString(document.getString("content"));

			return MultiInventoryUtil.convertYamlToPlayerConfig(config);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new MultiInventoryPlayerConfig();
	}

	@Override
	public void setTemplate(World world, MultiInventoryPlayerConfig playerConfig) {
		try {
			MultiInventoryWorldConfig worldConfig = this.getWorldConfig(world);
			String template = worldConfig.templateId != null ? worldConfig.templateId : MultiInventory.getConfiguration().defaultTemplateId;

			if (this.collection_templates.find(Filters.eq("template", template)).first() != null)
				this.collection_templates.updateOne(Filters.eq("template", template),
						new Document("$set", new Document("content", MultiInventoryUtil.convertPlayerConfigToYaml(playerConfig).saveToString())));
			else
				this.collection_templates.insertOne(new Document("template", template).append("content", MultiInventoryUtil.convertPlayerConfigToYaml(playerConfig).saveToString()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setInventory(Player player, World world, MultiInventoryPlayerConfig playerConfig) {
		try {
			MultiInventoryWorldConfig worldConfig = this.getWorldConfig(world);
			String inventory = worldConfig.templateId != null ? worldConfig.templateId : MultiInventory.getConfiguration().defaultInventoryId;

			if (this.collection_inventorys.find(Filters.eq("uuid", player.getUniqueId().toString())).first() != null)
				this.collection_inventorys.updateOne(Filters.eq("uuid", player.getUniqueId().toString()),
						new Document("$set", new Document(String.format("worlds.%s", inventory), MultiInventoryUtil.convertPlayerConfigToYaml(playerConfig).saveToString())));
			else
				this.collection_inventorys.insertOne(new Document("uuid", player.getUniqueId().toString())
						.append(String.format("worlds.%s", inventory), MultiInventoryUtil.convertPlayerConfigToYaml(playerConfig).saveToString()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public MultiInventoryPlayerConfig getInventory(Player player, World world) {
		try {
			MultiInventoryWorldConfig worldConfig = this.getWorldConfig(world);
			String inventory = worldConfig.templateId != null ? worldConfig.templateId : MultiInventory.getConfiguration().defaultInventoryId;

			Document document = this.collection_inventorys.find(Filters.eq("uuid", player.getUniqueId().toString())).first();

			YamlConfiguration config;

			if (document != null && document.containsKey(String.format("worlds.%s", inventory)))
				(config = new YamlConfiguration()).loadFromString(document.get(String.format("worlds.%s", inventory), EMPTY_DOCUMENT).getString("content"));
			else
				config = MultiInventoryUtil.convertPlayerConfigToYaml(this.getTemplate(world));

			return MultiInventoryUtil.convertYamlToPlayerConfig(config);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new MultiInventoryPlayerConfig();
	}
}