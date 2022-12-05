package ml.mops.pvps;

import org.bukkit.ChatColor;

public enum Map {
    DESERT("desert.txt", ChatColor.YELLOW + "Desert"),
    PLAINS("", ChatColor.GREEN + "Plains"),
    AQUA("", ChatColor.AQUA + "Aqua"),
    TAIGA("", ChatColor.DARK_GREEN + "Taiga"),
    CITY("", ChatColor.GOLD + "City"),
    MOPSFORT("", ChatColor.RED + "MopsFort");

    final String fileName;
    final String name;

    Map (String mapURL, String name) {
        this.fileName = mapURL;
        this.name = name;
    }

    public String getFileName() {
        return fileName;
    }

    public String getName() {
        return name;
    }
}
