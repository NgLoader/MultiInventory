package de.ngloader.multiinventory.event;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import de.ngloader.multiinventory.config.MultiInventoryPlayerConfig;

public class MultiInventoryLoadPlayerEvent extends PlayerEvent {

	private static final HandlerList handlers = new HandlerList();

	private final World world;

	private MultiInventoryPlayerConfig playerConfig;

	public MultiInventoryLoadPlayerEvent(Player player, World world, MultiInventoryPlayerConfig playerConfig) {
		super(player);

		this.world = world;
		this.playerConfig = playerConfig;
	}

	public World getWorld() {
		return this.world;
	}

	public MultiInventoryPlayerConfig getPlayerConfig() {
		return this.playerConfig;
	}

	public void setPlayerConfig(MultiInventoryPlayerConfig playerConfig) {
		this.playerConfig = playerConfig;
	}

	@Override
	public HandlerList getHandlers() {
		return MultiInventoryLoadPlayerEvent.handlers;
	}

	public static HandlerList getHandlerList() {
		return MultiInventoryLoadPlayerEvent.handlers;
	}
}
