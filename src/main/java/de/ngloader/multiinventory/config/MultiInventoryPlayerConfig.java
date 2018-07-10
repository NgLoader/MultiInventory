package de.ngloader.multiinventory.config;

import java.util.Collection;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class MultiInventoryPlayerConfig {

	public Location location;
	public Location bedSpawnLocation;

	public String displayName;

	public GameMode gameMode;

	public Boolean allowFlying;

	public Float walkSpeed;
	public Float flySpeed;

	public Double health;
	public Double healthScale;
	public Integer food;

	public Integer fireTicks;	

	public Float exhaustion;

	public Integer totalExperience;
	public Integer level;
	public Float exp;

	public ItemStack[] content;
	public ItemStack[] armor;
	public ItemStack[] extra;

	public ItemStack[] enderChest;

	public Collection<PotionEffect> potionEffect;
}