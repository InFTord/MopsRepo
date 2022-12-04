package ml.mops.pvps;

import ml.mops.utils.MopsColor;
import org.bukkit.ChatColor;
import org.bukkit.Color;

public enum Map {
    DESERT("https://cdn.discordapp.com/attachments/897853554340020254/1049080525161242685/desert.txt", ChatColor.YELLOW + "Desert"),
    PLAINS("", ChatColor.GREEN + "Plains"),
    AQUA("", ChatColor.AQUA + "Aqua"),
    TAIGA("", ChatColor.DARK_GREEN + "Taiga"),
    CITY("", ChatColor.GOLD + "City"),
    MOPSFORT("", ChatColor.RED + "MopsFort");

    final String mapURL;
    final String name;

    Map (String mapURL, String name) {
        this.mapURL = mapURL;
        this.name = name;
    }
}
