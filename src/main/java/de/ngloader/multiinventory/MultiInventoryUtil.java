package de.ngloader.multiinventory;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;

import de.ngloader.multiinventory.config.MultiInventoryPlayerConfig;
import de.ngloader.multiinventory.config.MultiInventoryWorldConfig;
import de.ngloader.multiinventory.config.MultiInvetoryWorldConfigSettings;
import de.ngloader.multiinventory.storage.ExtensionInventory;
import net.imprex.storage.api.StorageApi;

public class MultiInventoryUtil {

	public static YamlConfiguration convertPlayerConfigToYaml(MultiInventoryPlayerConfig playerConfig) {
		YamlConfiguration config = new YamlConfiguration();

		config.set("location", playerConfig.location);
		config.set("bedspawnlocation", playerConfig.bedSpawnLocation);

		config.set("displayname", playerConfig.displayName);

		config.set("gamemode", playerConfig.gameMode.ordinal());

		config.set("allowflying", playerConfig.allowFlying);

		config.set("walkspeed", playerConfig.walkSpeed);
		config.set("flyspeed", playerConfig.flySpeed);

		config.set("health", playerConfig.health);
		config.set("healthscale", playerConfig.healthScale);
		config.set("food", playerConfig.food);

		config.set("fireticks", playerConfig.fireTicks);

		config.set("exhaustion", playerConfig.exhaustion);

		config.set("totalexperience", playerConfig.totalExperience);
		config.set("level", playerConfig.level);
		config.set("exp", playerConfig.exp);

		config.set("content", playerConfig.content);
		config.set("armor", playerConfig.armor);
		config.set("extra", playerConfig.extra);

		config.set("enderchest", playerConfig.enderChest);

		config.set("potioneffect", playerConfig.potionEffect);
		return config;
	}

	public static MultiInventoryPlayerConfig convertYamlToPlayerConfig(YamlConfiguration config) {
		MultiInventoryPlayerConfig playerConfig = new MultiInventoryPlayerConfig();

		playerConfig.location = (Location) config.get("location");
		playerConfig.bedSpawnLocation = (Location) config.get("bedspawnlocation");

		playerConfig.displayName = config.getString("displayname");

		playerConfig.gameMode = config.contains("gamemode") ? GameMode.values()[config.getInt("gamemode")] : null;

		playerConfig.allowFlying = config.getBoolean("allowflying");

		playerConfig.walkSpeed = Float.parseFloat(Double.toString(config.getDouble("walkspeed")));
		playerConfig.flySpeed = Float.parseFloat(Double.toString(config.getDouble("flyspeed")));

		playerConfig.health = config.getDouble("health");
		playerConfig.healthScale = config.getDouble("healthscale");
		playerConfig.food = config.getInt("food");

		playerConfig.fireTicks = config.getInt("fireticks");

		playerConfig.exhaustion = Float.parseFloat(Double.toString(config.getDouble("exhaustion")));

		playerConfig.totalExperience = config.getInt("totalexperience");
		playerConfig.level = config.getInt("level");
		playerConfig.exp = Float.parseFloat(Double.toString(config.getDouble("exp")));

		playerConfig.content = config.contains("content") ? config.getList("content").toArray(new ItemStack[config.getList("content").size()]) : null;
		playerConfig.armor = config.contains("armor") ? config.getList("armor").toArray(new ItemStack[config.getList("armor").size()]) : null;
		playerConfig.extra = config.contains("extra") ? config.getList("extra").toArray(new ItemStack[config.getList("extra").size()]) : null;

		playerConfig.enderChest = config.contains("enderchest") ? config.getList("enderchest").toArray(new ItemStack[config.getList("enderchest").size()]) : null;

		playerConfig.potionEffect = config.contains("potioneffect") ? config.getList("potioneffect").stream().map(obj -> (PotionEffect) obj).collect(Collectors.toList()) : null;

		return playerConfig;
	}

	public static YamlConfiguration getPlayerConfigInYaml(Player player) {
		YamlConfiguration config = new YamlConfiguration();

		config.set("location", player.getLocation());
		config.set("bedspawnlocation", player.getBedSpawnLocation());

		config.set("displayname", player.getDisplayName());

		config.set("gamemode", player.getGameMode().ordinal());

		config.set("allowflying", player.getAllowFlight());

		config.set("walkspeed", player.getWalkSpeed());
		config.set("flyspeed", player.getFlySpeed());

		config.set("health", player.getHealth());
		config.set("healthscale", player.getHealthScale());
		config.set("food", player.getFoodLevel());

		config.set("fireticks", player.getFireTicks());

		config.set("exhaustion", player.getExhaustion());

		config.set("totalexperience", player.getTotalExperience());
		config.set("level", player.getLevel());
		config.set("exp", player.getExp());

		PlayerInventory inventory = player.getInventory();
		config.set("content", inventory.getContents());
		config.set("armor", inventory.getArmorContents());
		config.set("extra", inventory.getExtraContents());

		config.set("enderchest", player.getEnderChest().getContents());

		config.set("potioneffect", player.getActivePotionEffects());
		return config;
	}

	public static MultiInventoryPlayerConfig getPlayerConfig(Player player) {
		MultiInventoryPlayerConfig playerConfig = new MultiInventoryPlayerConfig();

		playerConfig.location = player.getLocation();
		playerConfig.bedSpawnLocation = player.getBedSpawnLocation();

		playerConfig.displayName = player.getDisplayName();

		playerConfig.gameMode = player.getGameMode();

		playerConfig.allowFlying = player.getAllowFlight();

		playerConfig.walkSpeed = player.getWalkSpeed();
		playerConfig.flySpeed = player.getWalkSpeed();

		playerConfig.health = player.getHealth();
		playerConfig.healthScale = player.getHealthScale();
		playerConfig.food = player.getFoodLevel();

		playerConfig.fireTicks = player.getFireTicks();

		playerConfig.exhaustion = player.getExhaustion();

		playerConfig.totalExperience = player.getTotalExperience();
		playerConfig.level = player.getLevel();
		playerConfig.exp = player.getExp();

		playerConfig.content = player.getInventory().getContents();
		playerConfig.armor = player.getInventory().getArmorContents();
		playerConfig.extra = player.getInventory().getExtraContents();

		playerConfig.enderChest = player.getEnderChest().getContents();

		playerConfig.potionEffect = player.getActivePotionEffects();

		return playerConfig;
	}

	public static void loadPlayerConfig(Player player, World world, MultiInventoryPlayerConfig playerConfig) {
		ExtensionInventory extension = StorageApi.getStorageService().getExtension(ExtensionInventory.class);

		MultiInventoryPlayerConfig templateConfig = extension.getTemplate(world);
		MultiInventoryWorldConfig worldConfig = extension.getWorldConfig(world);
		MultiInvetoryWorldConfigSettings worldSettings = worldConfig.settings != null ? worldConfig.settings : new MultiInvetoryWorldConfigSettings();

		// Location
		if(worldSettings.setLocation)
			player.teleport((playerConfig.location != null ? playerConfig.location
					: templateConfig != null ? templateConfig.location
							: world.getSpawnLocation()).add(0, .2, 0));

		// BedSpawnLocation
		if(worldSettings.setBedSpawnLocation)
			player.setBedSpawnLocation(playerConfig.bedSpawnLocation != null ? playerConfig.bedSpawnLocation
					: templateConfig.bedSpawnLocation != null ? templateConfig.bedSpawnLocation
							: null);

		// DisplayName
		if(worldSettings.setDisplayName)
			player.setDisplayName(playerConfig.displayName != null ? playerConfig.displayName
					: templateConfig != null ? templateConfig.displayName
							: player.getName());

		// Gamemode
		if(worldSettings.setGamemode)
			player.setGameMode(playerConfig.gameMode != null ? playerConfig.gameMode
					: templateConfig.gameMode != null ? templateConfig.gameMode
							: GameMode.SURVIVAL);

		// AllowFlying
		if(worldSettings.setAllowFlying)
			player.setAllowFlight(playerConfig.allowFlying != null ? playerConfig.allowFlying
					: templateConfig.allowFlying != null ? templateConfig.allowFlying
							: false);

		// WalkSpeed
		if(worldSettings.setWalkSpeed)
			player.setWalkSpeed(playerConfig.walkSpeed != null ? playerConfig.walkSpeed
					: templateConfig.walkSpeed != null ? templateConfig.walkSpeed
							: .2F);

		// FlySpeed
		if(worldSettings.setFlySpeed)
			player.setFlySpeed(playerConfig.flySpeed != null ? playerConfig.flySpeed
					: templateConfig.flySpeed != null ? templateConfig.flySpeed
							: .2F);

		// HealthScale
		if(worldSettings.setHealthScale)
			player.setHealthScale(playerConfig.healthScale != null ? playerConfig.healthScale
					: templateConfig.healthScale != null ? templateConfig.healthScale
							: 20);

		// Health
		if(worldSettings.setHealth)
			player.setHealth(playerConfig.healthScale != null ? playerConfig.healthScale
					: templateConfig.healthScale != null ? templateConfig.healthScale
							: 20);

		// Food
		if(worldSettings.setFood)
			player.setFoodLevel(playerConfig.food != null ? playerConfig.food
					: templateConfig.food != null ? templateConfig.food
							: 20);

		// TotalExperience
		if(worldSettings.setTotalExperience)
			player.setTotalExperience(playerConfig.totalExperience != null ? playerConfig.totalExperience
					: templateConfig.totalExperience != null ? templateConfig.totalExperience
							: 0);

		// Level
		if(worldSettings.setLevel)
			player.setLevel(playerConfig.level != null ? playerConfig.level
					: templateConfig.level != null ? templateConfig.level
							: 0);

		// Exp
		if(worldSettings.setExp)
			player.setExp(playerConfig.exp != null ? playerConfig.exp
					: templateConfig.exp != null ? templateConfig.exp
							: 0);

		// FireTicks
		if(worldSettings.setFireTicks)
			player.setFireTicks(playerConfig.fireTicks != null ? playerConfig.fireTicks
					: templateConfig.fireTicks != null ? templateConfig.fireTicks
							: 0);

		// Exhaustion
		if(worldSettings.setExhaustion)
			player.setExhaustion(playerConfig.exhaustion != null ? playerConfig.exhaustion
					: templateConfig.exhaustion != null ? templateConfig.exhaustion
							: 0);

		// InventoryContent
		if (worldSettings.setInventoryContent)
			player.getInventory()
					.setContents(playerConfig.content != null ? playerConfig.content
							: templateConfig.content != null ? templateConfig.content
									: new ItemStack[player.getInventory().getContents().length]);

		// InventoryArmor
		if (worldSettings.setInventoryArmor)
			player.getInventory()
					.setArmorContents(playerConfig.armor != null ? playerConfig.armor
							: templateConfig.armor != null ? templateConfig.armor
									: new ItemStack[player.getInventory().getArmorContents().length]);

		// InventoryExtra
		if (worldSettings.setInventoryExtra)
			player.getInventory()
					.setExtraContents(playerConfig.extra != null ? playerConfig.extra
							: templateConfig.extra != null ? templateConfig.extra
									: new ItemStack[player.getInventory().getExtraContents().length]);

		// Enderchest
		if (worldSettings.setEnderChest)
			player.getEnderChest()
					.setContents(playerConfig.enderChest != null ? playerConfig.enderChest
							: templateConfig.enderChest != null ? templateConfig.enderChest
									: new ItemStack[player.getEnderChest().getContents().length]);

		// PotionEffects
		if (worldSettings.setPotionEffect)
			player.addPotionEffects(playerConfig.potionEffect != null ? playerConfig.potionEffect
					: templateConfig.potionEffect != null ? templateConfig.potionEffect
							: new ArrayList<>());
	}
}