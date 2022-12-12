package ml.mops.base.maps;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Map {
    DESERT("desert", ChatColor.YELLOW + "Desert", MapType.PVP, Material.SANDSTONE),
    PLAINS("desert", ChatColor.GREEN + "Plains", MapType.PVP, Material.GRASS_BLOCK),
    AQUA("desert", ChatColor.AQUA + "Aqua", MapType.PVP, Material.WARPED_WART_BLOCK),
    TAIGA("desert", ChatColor.DARK_GREEN + "Taiga", MapType.PVP, Material.SPRUCE_LEAVES),
    CITY("desert", ChatColor.GOLD + "City", MapType.PVP, Material.BRICKS),
    MOPSFORT("desert", ChatColor.RED + "MopsFort", MapType.PVP, Material.RED_TERRACOTTA);

    final String fileName;
    final String name;
    final MapType mapType;
    final Material type;

    Map (String fileName, String name, MapType mapType, Material type) {
        this.fileName = fileName;
        this.name = name;
        this.mapType = mapType;
        this.type = type;
    }

    public String getFileName() {
        return fileName;
    }

    public String[] getRowArray() {
        InputStream stream = Map.class.getResourceAsStream(mapType.getFilePath() + "/" + fileName + ".txt");
        String[] rowArray = new String[] {""};

        try {
            assert stream != null;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));

            StringBuilder stringBuilder = new StringBuilder();

            String inputLine;
            while ((inputLine = bufferedReader.readLine()) != null) {
                stringBuilder.append(inputLine);
                stringBuilder.append(System.lineSeparator());
            }
            bufferedReader.close();

            rowArray = stringBuilder.toString().split("\n");;

        } catch (IOException ignored) { }

        return rowArray;
    }

    public Material getType() {
        return type;
    }

    public String getName() {
        return name;
    }
    public MapType getMapType() {
        return mapType;
    }
}
