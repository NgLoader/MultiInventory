package de.ngloader.multiinventory;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import de.ngloader.multiinventory.listener.PlayerListener;

public class MultiInventory extends JavaPlugin {

	private static MultiInventory instance;

	public static MultiInventory getInstance() {
		return instance;
	}

	public MultiInventory() {
		if(instance == null)
			instance = this;
		else
			Bukkit.getPluginManager().disablePlugin(this);
	}

	@Override
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
	}
}