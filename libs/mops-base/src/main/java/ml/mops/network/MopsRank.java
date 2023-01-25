package ml.mops.network;

import org.bukkit.ChatColor;
import org.bukkit.Color;

public enum MopsRank {
	NONE(0, ChatColor.GRAY + ""),

	PIGEON(1, ChatColor.AQUA + "[PIGEON] "),
	PUG(2, ChatColor.YELLOW + "[PUG] "),
	FROG(3, ChatColor.DARK_GREEN + "[FROG] "),
	WARDEN(4, ChatColor.DARK_AQUA + "[WARDEN] "),

	VIP(7, ChatColor.GOLD + "[VIP] "),

	HELPER(13, ChatColor.BLUE + "[HELPER] "),
	ADMIN(14, ChatColor.RED + "[ADMIN ]"),
	DEV(15, ChatColor.GREEN + "[DEV] ");

	final int permLevel;
	final String prefix;

	MopsRank (int permLevel, String prefix) {
		this.permLevel = permLevel;
		this.prefix = prefix;
	}

	public String getPrefix() {
		return prefix;
	}

	public int getPermLevel() { return permLevel; }
}