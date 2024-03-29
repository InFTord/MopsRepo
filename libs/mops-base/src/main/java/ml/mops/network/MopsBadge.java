package ml.mops.network;

import org.bukkit.ChatColor;

public enum MopsBadge {
    NONE("", ChatColor.GRAY + "NONE", ""),
    SILLY(ChatColor.GOLD + " ⚡" + ChatColor.RESET, ChatColor.GOLD + "⚡ Silly", ChatColor.GRAY + " - Badge\n \nIt means that this member is very silly."),
    MUSIC(ChatColor.DARK_PURPLE + " ♪", ChatColor.DARK_PURPLE + "♪ Music", ChatColor.GRAY + " - Badge\n \nThis person doesn't produce music for MopsNet, however\nthey have a cool exclusive guitar."),
    TOXIC(ChatColor.GREEN + " ☠" + ChatColor.RESET, ChatColor.GREEN + "☠ Toxic", ChatColor.GRAY + " - Badge\n \nIt means that this person is known for being toxic."),
    STAFF(ChatColor.BLUE + " ⭐" + ChatColor.RESET, ChatColor.BLUE + "⭐ Staff", ChatColor.GRAY + " - Badge\n \nIt means that this is a MopsNet staff member.");

    final String symbol;
    final String name;
    final String description;

    MopsBadge (String symbol, String name, String description) {
        this.symbol = symbol;
        this.name = name;
        this.description = description;
    }

    public String getSymbol() {
        return symbol;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return name + description;
    }
}