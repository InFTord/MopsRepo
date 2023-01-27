package ml.mops.network;

import org.bukkit.ChatColor;
import org.bukkit.Color;

public enum MopsRank {
	NONE(0, ChatColor.GRAY + "", ChatColor.GRAY + "NONE"),

	PIGEON(1, ChatColor.AQUA + "[PIGEON] ", ChatColor.AQUA + "[PIGEON]"),
	PUG(2, ChatColor.YELLOW + "[PUG] ", ChatColor.YELLOW + "[PUG]"),
	FROG(3, ChatColor.DARK_GREEN + "[FROG] ", ChatColor.DARK_GREEN + "[FROG]"),
	WARDEN(4, ChatColor.DARK_AQUA + "[WARDEN] ", ChatColor.DARK_AQUA + "[WARDEN]"),

	VIP(7, ChatColor.GOLD + "[VIP] ", ChatColor.GOLD + "[VIP]"),

	HELPER(13, ChatColor.BLUE + "[HELPER] ", ChatColor.BLUE + "[HELPER]"),
	ADMIN(14, ChatColor.RED + "[ADMIN] ", ChatColor.RED + "[ADMIN]"),
	DEV(15, ChatColor.GREEN + "[DEV] ", ChatColor.GREEN + "[DEV]");

	final int permLevel;
	final String prefix;
	final String scoreboardText;

	MopsRank (int permLevel, String prefix, String scoreboardText) {
		this.permLevel = permLevel;
		this.prefix = prefix;
		this.scoreboardText = scoreboardText;
	}

	public String getPrefix() {
		return prefix;
	}
	public int getPermLevel() { return permLevel; }
	public String getScoreboardText() { return scoreboardText; }
}