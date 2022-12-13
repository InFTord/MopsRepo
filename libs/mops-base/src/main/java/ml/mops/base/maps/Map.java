package ml.mops.base.maps;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Map {
    DESERT("https://cdn.discordapp.com/attachments/897853554340020254/1052219255392849920/desert.txt", ChatColor.YELLOW + "Desert", MapType.PVP, Material.SANDSTONE),
    PLAINS("https://cdn.discordapp.com/attachments/897853554340020254/1052222190877163620/plains.txt", ChatColor.GREEN + "Plains", MapType.PVP, Material.GRASS_BLOCK),
    AQUA("desert", ChatColor.AQUA + "Aqua", MapType.PVP, Material.WARPED_WART_BLOCK),
    TAIGA("desert", ChatColor.DARK_GREEN + "Taiga", MapType.PVP, Material.SPRUCE_LEAVES),
    CITY("desert", ChatColor.GOLD + "City", MapType.PVP, Material.BRICKS),
    MOPSFORT("desert", ChatColor.RED + "MopsFort", MapType.PVP, Material.RED_TERRACOTTA);

    final String fileURL;
    final String name;
    final MapType mapType;
    final Material type;

    Map (String fileName, String name, MapType mapType, Material type) {
        this.fileURL = fileName;
        this.name = name;
        this.mapType = mapType;
        this.type = type;
    }

    public String getFileURL() {
        return fileURL;
    }

    public String[] getRowArray() {
        String[] rowArray = new String[] {""};

        try {
            InputStream stream = new URL(fileURL).openStream();
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
