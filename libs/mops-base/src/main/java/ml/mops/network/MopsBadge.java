package ml.mops.network;

import org.bukkit.ChatColor;

public enum MopsBadge {
    NONE("", ""),
    SILLY(ChatColor.GOLD + "⚡" + ChatColor.RESET, ChatColor.GOLD + "⚡ Silly" + ChatColor.GRAY + " - Badge\n \nIt means that this member is very silly."),
    STAFF(ChatColor.BLUE + "⭐" + ChatColor.RESET, ChatColor.BLUE + "⭐ Staff" + ChatColor.GRAY + " - Badge\n \nIt means that this is a MopsNet staff member.");

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