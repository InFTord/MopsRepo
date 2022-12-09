package ml.mops.pvps;

import org.bukkit.ChatColor;

public enum Map {
    DESERT("pvp/desert.txt", ChatColor.YELLOW + "Desert", MapType.PVP),
    PLAINS("pvp/sus.txt", ChatColor.GREEN + "Plains", MapType.PVP),
    AQUA("pvp/sus.txt", ChatColor.AQUA + "Aqua", MapType.PVP),
    TAIGA("pvp/sus.txt", ChatColor.DARK_GREEN + "Taiga", MapType.PVP),
    CITY("pvp/sus.txt", ChatColor.GOLD + "City", MapType.PVP),
    MOPSFORT("pvp/sus.txt", ChatColor.RED + "MopsFort", MapType.PVP);

    final String fileName;
    final String name;
    final MapType mapType;

    Map (String mapURL, String name, MapType mapType) {
        this.fileName = mapURL;
        this.name = name;
        this.mapType = mapType;
    }

    public String getFileName() {
        return fileName;
    }
    public String getName() {
        return name;
    }
    public MapType getMapType() {
        return mapType;
    }
}
