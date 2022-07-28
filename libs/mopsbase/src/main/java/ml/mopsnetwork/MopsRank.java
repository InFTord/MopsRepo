package ml.mopsbase;

import org.bukkit.ChatColor;
import org.bukkit.Color;

public enum MopsRank {
	NONE(0, ChatColor.GRAY + ""),

	PIGEON(0, ChatColor.AQUA + "[PIGEON] "),
	PUG(0, ChatColor.YELLOW + "[PUG] "),
	FROG(0, ChatColor.DARK_GREEN + "[FROG] "),
	WARDEN(0, ChatColor.DARK_AQUA + "[WARDEN] "),

	VIP(1, ChatColor.GOLD + "[VIP] "),

	HELPER(3, ChatColor.BLUE + "[HELPER] "),
	ADMIN(4, ChatColor.RED + "[ADMIN] "),
	DEV(5, ChatColor.GREEN + "[DEV] ");


	final int permLevel; // permission level
	final String rankName; // rank name

	MopsRank (int permLevel, String rankName) {
		this.permLevel = permLevel;
		this.rankName = rankName;
	}
}