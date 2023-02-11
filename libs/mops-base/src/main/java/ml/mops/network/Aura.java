package ml.mops.network;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;

public enum Aura {
    NONE(ChatColor.GRAY + "NONE"),
    CUBE(ChatColor.GREEN + "CUBE"),
    ANDROMEDA(ChatColor.GOLD + "ANDROMEDA"),
    INFINITY(ChatColor.DARK_AQUA + "INFINITY");

    final String auraName;

    Aura (String auraName) {
        this.auraName = auraName;
    }

    public String getName() {
        return auraName;
    }
}
