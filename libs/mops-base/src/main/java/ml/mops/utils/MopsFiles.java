package ml.mops.utils;

import ml.mops.network.MopsBadge;
import ml.mops.network.MopsRank;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MopsFiles {
    public static int getCoins(Player player) {
        UUID uuid = player.getUniqueId();
        int coins = 0;

        String[] list = MopsUtils.readFile("D:\\servers\\MopsNetwork\\data.txt").split("\n");
        for (String row : list) {
            String[] array = row.split(":");
            if(UUID.fromString(array[0]).equals(uuid)) {
                coins = Integer.parseInt(array[1]);
            }
        }
        return coins;
    }
    public static void setCoins(Player player, Integer coins) {
        UUID uuid = player.getUniqueId();
        StringBuilder line = new StringBuilder();
        boolean playerExists = false;

        String[] list = MopsUtils.readFile("D:\\servers\\MopsNetwork\\data.txt").split("\n");
        for (String row : list) {
            String[] array = row.split(":");
            if(UUID.fromString(array[0]).equals(uuid)) {
                array[1] = String.valueOf(coins);
                playerExists = true;
            }
            line.append(MopsUtils.combineStrings(array, ":")).append("\n");
        }
        if(!playerExists) {
            line.append(uuid).append(":").append(coins).append(":NONE:NONE:eng").append("\n");
        }
        try {
            MopsUtils.writeFile("D:\\servers\\MopsNetwork\\data.txt", line.toString());
        } catch (IOException ignored) { }
    }



    public static MopsRank getRank(Player player) {
        UUID uuid = player.getUniqueId();
        MopsRank rank = MopsRank.NONE;

        String[] list = MopsUtils.readFile("D:\\servers\\MopsNetwork\\data.txt").split("\n");
        for (String row : list) {
            String[] array = row.split(":");
            if(UUID.fromString(array[0]).equals(uuid)) {
                rank = MopsRank.valueOf(array[2]);
            }
        }
        return rank;
    }
    public static void setRank(Player player, MopsRank rank) {
        UUID uuid = player.getUniqueId();
        StringBuilder line = new StringBuilder();
        boolean playerExists = false;

        String[] list = MopsUtils.readFile("D:\\servers\\MopsNetwork\\data.txt").split("\n");
        for (String row : list) {
            String[] array = row.split(":");
            if(UUID.fromString(array[0]).equals(uuid)) {
                array[2] = String.valueOf(rank);
                playerExists = true;
            }
            line.append(MopsUtils.combineStrings(array, ":")).append("\n");
        }
        if(!playerExists) {
            line.append(uuid).append(":0:").append(rank).append(":NONE:eng").append("\n");
        }
        try {
            MopsUtils.writeFile("D:\\servers\\MopsNetwork\\data.txt", line.toString());
        } catch (IOException ignored) { }
    }



    public static MopsBadge getBadge(Player player) {
        UUID uuid = player.getUniqueId();
        MopsBadge badge = MopsBadge.NONE;

        String[] list = MopsUtils.readFile("D:\\servers\\MopsNetwork\\data.txt").split("\n");
        for (String row : list) {
            String[] array = row.split(":");
            if(UUID.fromString(array[0]).equals(uuid)) {
                badge = MopsBadge.valueOf(array[3]);
            }
        }
        return badge;
    }
    public static void setBadge(Player player, MopsBadge badge) {
        UUID uuid = player.getUniqueId();
        StringBuilder line = new StringBuilder();
        boolean playerExists = false;

        String[] list = MopsUtils.readFile("D:\\servers\\MopsNetwork\\data.txt").split("\n");
        for (String row : list) {
            String[] array = row.split(":");
            if(UUID.fromString(array[0]).equals(uuid)) {
                array[3] = String.valueOf(badge);
                playerExists = true;
            }
            line.append(MopsUtils.combineStrings(array, ":")).append("\n");
        }
        if(!playerExists) {
            line.append(uuid).append(":0:NONE:").append(badge).append(":eng").append("\n");
        }
        try {
            MopsUtils.writeFile("D:\\servers\\MopsNetwork\\data.txt", line.toString());
        } catch (IOException ignored) { }
    }



    public static String getLanguage(Player player) {
        UUID uuid = player.getUniqueId();
        String lang = "eng";

        String[] list = MopsUtils.readFile("D:\\servers\\MopsNetwork\\data.txt").split("\n");
        for (String row : list) {
            String[] array = row.split(":");
            if(UUID.fromString(array[0]).equals(uuid)) {
                lang = array[4];
            }
        }
        return lang;
    }
    public static void setLanguage(Player player, String lang) {
        UUID uuid = player.getUniqueId();
        StringBuilder line = new StringBuilder();
        boolean playerExists = false;

        String[] list = MopsUtils.readFile("D:\\servers\\MopsNetwork\\data.txt").split("\n");
        for (String row : list) {
            String[] array = row.split(":");
            if(UUID.fromString(array[0]).equals(uuid)) {
                array[4] = lang;
                playerExists = true;
            }
            line.append(MopsUtils.combineStrings(array, ":")).append("\n");
        }
        if(!playerExists) {
            line.append(uuid).append(":0:NONE:NONE:").append(lang).append("\n");
        }
        try {
            MopsUtils.writeFile("D:\\servers\\MopsNetwork\\data.txt", line.toString());
        } catch (IOException ignored) { }
    }




    public static List<String> getServerData(Plugin plugin) {
        String path = plugin.getServer().getPluginsFolder().getAbsolutePath();
        path += "\\serverData.txt";

        String[] list = MopsUtils.readFile(path).split("\n");
        List<String> valuesList = new ArrayList<>();
        for (String row : list) {
            valuesList.add(row.substring(0, row.indexOf(":")));
        }
        return valuesList;
    }









    public static boolean getWasAtPigeon(Player player) {
        UUID uuid = player.getUniqueId();
        boolean bool = false;

        String[] list = MopsUtils.readFile("D:\\servers\\MopsNetwork\\destinations.txt").split("\n");
        for (String row : list) {
            String[] array = row.split(":");
            if(UUID.fromString(array[0]).equals(uuid)) {
                bool = Boolean.parseBoolean(array[1]);
            }
        }
        return bool;
    }
    public static void setWasAtPigeon(Player player, boolean bool) {
        UUID uuid = player.getUniqueId();
        StringBuilder line = new StringBuilder();
        boolean playerExists = false;

        String[] list = MopsUtils.readFile("D:\\servers\\MopsNetwork\\destinations.txt").split("\n");
        for (String row : list) {
            String[] array = row.split(":");
            if(UUID.fromString(array[0]).equals(uuid)) {
                array[1] = String.valueOf(bool);
                playerExists = true;
            }
            line.append(MopsUtils.combineStrings(array, ":")).append("\n");
        }
        if(!playerExists) {
            line.append(uuid).append(":false").append(bool).append("\n");
        }
        try {
            MopsUtils.writeFile("D:\\servers\\MopsNetwork\\destinations.txt", line.toString());
        } catch (IOException ignored) { }
    }
}
