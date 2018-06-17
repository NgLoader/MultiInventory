package de.ngloader.multiinventory.storage;

import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class PlayerContent {

	public ItemStack[] content;
	public ItemStack[] armor;
	public ItemStack[] extra;
	public ItemStack[] storage;

	public List<PotionEffect> effects;

	public PlayerContent(ItemStack[] content, ItemStack[] armor, ItemStack[] extra, ItemStack[] storage, List<PotionEffect> effects) {
		this.content = content;
		this.armor = armor;
		this.extra = extra;
		this.storage = storage;
		this.effects = effects;
	}
}