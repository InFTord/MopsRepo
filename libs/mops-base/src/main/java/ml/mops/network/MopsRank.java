package ml.mops.network;

import org.bukkit.ChatColor;
import org.bukkit.Color;

public enum MopsRank {
	NONE(0, ChatColor.GRAY + "NONE"),

	PIGEON(0, ChatColor.AQUA + "[PIGEON]"),
	PUG(0, ChatColor.YELLOW + "[PUG]"),
	FROG(0, ChatColor.DARK_GREEN + "[FROG]"),
	WARDEN(0, ChatColor.DARK_AQUA + "[WARDEN]"),

	VIP(1, ChatColor.GOLD + "[VIP]"),

	HELPER(3, ChatColor.BLUE + "[HELPER]"),
	ADMIN(4, ChatColor.RED + "[ADMIN]"),
	DEV(5, ChatColor.GREEN + "[DEV]");


	final int permLevel;
	final String prefix;

	MopsRank (int permLevel, String prefix) {
		this.permLevel = permLevel;
		this.prefix = prefix;
	}

	public String getPrefix() {
		return prefix;
	}

	public int getPermLevel() {
		return permLevel;
	}
}