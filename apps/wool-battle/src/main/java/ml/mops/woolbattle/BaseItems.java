package ml.mops.woolbattle;

import ml.mops.utils.MopsUtils;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.awt.*;
import java.util.*;
import java.util.List;

public class BaseItems {
	final Plugin plugin;

	public BaseItems(Plugin pl) {
	this.plugin = pl;
	}

	public void startGame(String lang, Teams team, Player player) {
		Items items = new Items(plugin);

		ItemStack shears = items.shears(lang);
		player.getInventory().setItem(0, shears);
		plugin.shears.add(shears);

		ItemStack boomstickMK1 = items.boomstickMK1(lang);
		player.getInventory().setItem(1, boomstickMK1);
		plugin.boomsticks.add(boomstickMK1);

		ItemStack slimeball = items.slimeball(lang);
		player.getInventory().setItem(2, slimeball);
		plugin.slimeballs.add(slimeball);

		ItemStack bow = items.stockBow(lang);
		player.getInventory().setItem(8, bow);

		ItemStack arrow = items.arrow(lang);
		player.getInventory().setItem(17, arrow);

		ItemStack doubleJumpBoots = items.doubleJumpBoots(lang, team);
		player.getInventory().setItem(36, doubleJumpBoots);
		plugin.doubleJumpBoots.add(doubleJumpBoots);
	}
}
