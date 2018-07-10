package de.ngloader.multiinventory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.gson.GsonBuilder;

import de.ngloader.multiinventory.autosave.MultiInventoryAutoSaveHandler;
import de.ngloader.multiinventory.command.CommandMultiInventory;
import de.ngloader.multiinventory.config.MultiInventoryConfig;
import de.ngloader.multiinventory.event.MultiInventorySavePlayerEvent;
import de.ngloader.multiinventory.listener.PlayerListener;
import de.ngloader.multiinventory.storage.ExtensionInventory;
import de.ngloader.multiinventory.storage.LocaleExtensionInventory;
import de.ngloader.multiinventory.storage.LocaleStorage;
import de.ngloader.multiinventory.storage.MongoExtensionInventory;
import de.ngloader.multiinventory.storage.SQLExtensionInventory;
import net.imprex.storage.api.StorageApi;
import net.imprex.storage.api.StorageService;
import net.imprex.storage.mongo.MongoStorage;
import net.imprex.storage.sql.SQLStorage;

public class MultiInventory extends JavaPlugin {

	private static MultiInventory instance;

	public static MultiInventory getInstance() {
		return instance;
	}

	public static MultiInventoryConfig getConfiguration() {
		return instance.getConfiguration0();
	}

	public static StorageService getDatabaseService() {
		return instance.getDatabaseService0();
	}

	private StorageService databaseService;
	private MultiInventoryConfig config;

	private Boolean enabled = false;

	public MultiInventory() {
		if(instance == null)
			instance = this;
		else
			Bukkit.getPluginManager().disablePlugin(this);
	}

	@Override
	public void onLoad() {
		this.databaseService = StorageApi.getStorageService();

		this.databaseService.registerExtension("multiinventory", ExtensionInventory.class);

		this.databaseService.registerStorage(LocaleStorage.class, "locale", new LocaleStorage());

		if(this.databaseService.getStorage(MongoStorage.class) != null)
			this.databaseService.getStorage(MongoStorage.class).registerProvider(ExtensionInventory.class, new MongoExtensionInventory());
		if(this.databaseService.getStorage(SQLStorage.class) != null)
			this.databaseService.getStorage(SQLStorage.class).registerProvider(ExtensionInventory.class, new SQLExtensionInventory());
		if(this.databaseService.getStorage(LocaleStorage.class) != null)
			this.databaseService.getStorage(LocaleStorage.class).registerProvider(ExtensionInventory.class, new LocaleExtensionInventory());

		try {
			loadConfigs();
		} catch(Exception e) {
			e.printStackTrace();
			Bukkit.shutdown();
		}
	}

	@Override
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);

		getCommand("multiinventory").setExecutor(new CommandMultiInventory());

		MultiInventoryAutoSaveHandler.check();

		this.enabled = true;
	}

	@Override
	public void onDisable() {
		for(Player player : Bukkit.getOnlinePlayers())
			try {
				if(PlayerListener.NO_SAVE_WHILE_ERROR.contains(player.getUniqueId())) {
					PlayerListener.NO_SAVE_WHILE_ERROR.remove(player.getUniqueId());
					continue;
				}

				MultiInventorySavePlayerEvent inventorySavePlayerEvent = new MultiInventorySavePlayerEvent(player, player.getWorld(), MultiInventoryUtil.getPlayerConfig(player));
				Bukkit.getPluginManager().callEvent(inventorySavePlayerEvent);

				StorageApi.getStorageService().getExtension(ExtensionInventory.class).setInventory(player, player.getWorld(), inventorySavePlayerEvent.getPlayerConfig());
			} catch(Exception e) {
				e.printStackTrace();

				PlayerListener.NO_SAVE_WHILE_ERROR.add(player.getUniqueId());

				player.kickPlayer(MultiInventory.getConfiguration().kickReason);
			}
	}

	public void loadConfigs() {
		Path pathDefaultInventory = new File(this.getDataFolder(), "/template/template-example.yml").toPath();
		Path pathConfig = new File(this.getDataFolder(), "config.yml").toPath();

		if(Files.notExists(pathDefaultInventory.getParent()))
			try {
				Files.createDirectories(pathDefaultInventory.getParent());
				Files.copy(MultiInventory.class.getResourceAsStream("/template-example.yml"), pathDefaultInventory);
			} catch (IOException e) {
				e.printStackTrace();
			}

		if(Files.notExists(pathConfig))
			try {
				Files.createDirectories(pathConfig.getParent());
				Files.copy(MultiInventory.class.getResourceAsStream("/config.yml"), pathConfig);
			} catch (IOException e) {
				e.printStackTrace();
			}

		try (BufferedReader reader = Files.newBufferedReader(pathConfig)) {
			this.config = new GsonBuilder().create().fromJson(reader, MultiInventoryConfig.class);
		} catch(IOException e) {
			e.printStackTrace();
		}

		if(enabled)
			MultiInventoryAutoSaveHandler.check();
	}

	protected MultiInventoryConfig getConfiguration0() {
		return this.config;
	}

	protected StorageService getDatabaseService0() {
		return this.databaseService;
	}
}