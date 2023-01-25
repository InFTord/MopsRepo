package ml.mops.utils;

import ml.mops.network.MopsBadge;
import ml.mops.network.MopsRank;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class MopsFiles {
    public static int getCoins(Player player) {
        UUID uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + player.getName()).getBytes(StandardCharsets.UTF_8));
        int coins = 0;

        String[] list = MopsUtils.readFile("D:\\servers\\MopsNetwork\\data.txt").split("\n");
        for (String row : list) {
            String[] array = row.split(":");
            if(UUID.fromString(array[0]) == uuid) {
                coins = Integer.parseInt(array[1]);
            }
        }
        return coins;
    }
    public static void setCoins(Player player, Integer coins) {
        UUID uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + player.getName()).getBytes(StandardCharsets.UTF_8));
        StringBuilder line = new StringBuilder();
        boolean playerExists = false;

        String[] list = MopsUtils.readFile("D:\\servers\\MopsNetwork\\data.txt").split("\n");
        for (String row : list) {
            String[] array = row.split(":");
            if(UUID.fromString(array[0]) == uuid) {
                array[1] = String.valueOf(coins);
                playerExists = true;
            }
            line.append(MopsUtils.combineStrings(array, ":")).append("\n");
        }
        if(!playerExists) {
            line.append(uuid).append(":").append(coins).append(":NONE:NONE").append("\n");
        }
        try {
            MopsUtils.writeFile("D:\\servers\\MopsNetwork\\data.txt", line.toString());
        } catch (IOException ignored) { }
    }



    public static MopsRank getRank(Player player) {
        UUID uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + player.getName()).getBytes(StandardCharsets.UTF_8));
        MopsRank rank = MopsRank.NONE;

        String[] list = MopsUtils.readFile("D:\\servers\\MopsNetwork\\data.txt").split("\n");
        for (String row : list) {
            String[] array = row.split(":");
            if(UUID.fromString(array[0]) == uuid) {
                rank = MopsRank.valueOf(array[2]);
            }
        }
        return rank;
    }
    public static void setRank(Player player, MopsRank rank) {
        UUID uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + player.getName()).getBytes(StandardCharsets.UTF_8));
        StringBuilder line = new StringBuilder();
        boolean playerExists = false;

        String[] list = MopsUtils.readFile("D:\\servers\\MopsNetwork\\data.txt").split("\n");
        for (String row : list) {
            String[] array = row.split(":");
            if(UUID.fromString(array[0]) == uuid) {
                array[2] = String.valueOf(rank);
                playerExists = true;
            }
            line.append(MopsUtils.combineStrings(array, ":")).append("\n");
        }
        if(!playerExists) {
            line.append(uuid).append(":0:").append(rank).append(":NONE").append("\n");
        }
        try {
            MopsUtils.writeFile("D:\\servers\\MopsNetwork\\data.txt", line.toString());
        } catch (IOException ignored) { }
    }



    public static MopsBadge getBadge(Player player) {
        UUID uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + player.getName()).getBytes(StandardCharsets.UTF_8));
        MopsBadge badge = MopsBadge.NONE;

        String[] list = MopsUtils.readFile("D:\\servers\\MopsNetwork\\data.txt").split("\n");
        for (String row : list) {
            String[] array = row.split(":");
            if(UUID.fromString(array[0]) == uuid) {
                badge = MopsBadge.valueOf(array[3]);
            }
        }
        return badge;
    }
    public static void setBadge(Player player, MopsBadge badge) {
        UUID uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + player.getName()).getBytes(StandardCharsets.UTF_8));
        StringBuilder line = new StringBuilder();
        boolean playerExists = false;

        String[] list = MopsUtils.readFile("D:\\servers\\MopsNetwork\\data.txt").split("\n");
        for (String row : list) {
            String[] array = row.split(":");
            if(UUID.fromString(array[0]) == uuid) {
                array[3] = String.valueOf(badge);
                playerExists = true;
            }
            line.append(MopsUtils.combineStrings(array, ":")).append("\n");
        }
        if(!playerExists) {
            line.append(uuid).append(":0:NONE:").append(badge).append("\n");
        }
        try {
            MopsUtils.writeFile("D:\\servers\\MopsNetwork\\data.txt", line.toString());
        } catch (IOException ignored) { }
    }
}
