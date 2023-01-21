package ml.mops.network;

import org.bukkit.ChatColor;

public enum MopsBadge {
    NONE("", ""),
    SILLY(ChatColor.GOLD + " ⚡ " + ChatColor.RESET, ChatColor.GRAY + "This member is very silly."),
    STAFF(ChatColor.BLUE + " ⭐ " + ChatColor.RESET, ChatColor.BLUE + "This is a staff member of MopsNet.");

    final String symbol;
    final String description;

    MopsBadge (String symbol, String description) {
        this.symbol = symbol;
        this.description = description;
    }

    public String getSymbol() {
        return symbol;
    }
    public String getDescription() { return description; }
}