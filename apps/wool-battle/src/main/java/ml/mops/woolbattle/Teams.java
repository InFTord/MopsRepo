package ml.mops.woolbattle;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;

public enum Teams {
	RED(Color.fromRGB(255, 10, 10), "&c", "RED", 1, ChatColor.RED, "team.RED", Material.RED_WOOL, "red"),
	YELLOW(Color.fromRGB(255, 207, 36), "&e", "YELLOW", 2, ChatColor.YELLOW, "team.YELLOW", Material.YELLOW_WOOL, "yellow"),
	GREEN(Color.fromRGB(105, 255, 82), "&a", "GREEN", 3, ChatColor.GREEN, "team.GREEN", Material.LIME_WOOL, "green"),
	BLUE(Color.fromRGB(47, 247, 227), "&b", "BLUE", 4, ChatColor.AQUA, "team.BLUE", Material.LIGHT_BLUE_WOOL, "blue"),
	SPECTATOR(Color.fromRGB(170, 170, 170), "&7", "SPECTATOR", 0, ChatColor.GRAY, "team.Nobody", Material.LIGHT_GRAY_WOOL, "spectator"); //на будущие

	final String getColorString;
	final Color getLeatherColor;
	final String getName;
	final Integer getNumber;
	final ChatColor getChatColor;
	final String getTranslationKey;
	final Material getType;
	final String getID;

	Teams (Color getLeatherColor, String getColorString, String getName, Integer getNumber, ChatColor getChatColor, String getTranslationKey, Material getType, String getID) {
		this.getColorString = getColorString;
		this.getLeatherColor = getLeatherColor;
		this.getName = getName;
		this.getNumber = getNumber;
		this.getChatColor = getChatColor;
		this.getTranslationKey = getTranslationKey;
		this.getType = getType;
		this.getID = getID;
	}
}
