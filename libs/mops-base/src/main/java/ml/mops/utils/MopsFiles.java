package ml.mops.utils;

import ml.mops.network.Aura;
import ml.mops.network.Delivery;
import ml.mops.network.MopsBadge;
import ml.mops.network.MopsRank;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.util.*;

public class MopsFiles {
    public static int getCoins(Player player) {
        int coins = 0;

        String text = getString("data.txt", player.getUniqueId(), 1);
        if(!text.equals("")) {
            coins = Integer.parseInt(text);
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
            line.append(uuid).append(":").append(coins).append(":NONE:NONE:eng:NONE").append("\n");
        }
        try {
            MopsUtils.writeFile("D:\\servers\\MopsNetwork\\data.txt", line.toString());
        } catch (IOException ignored) { }
    }





    public static MopsRank getRank(Player player) {
        MopsRank rank = MopsRank.NONE;

        String text = getString("data.txt", player.getUniqueId(), 2);
        if(!text.equals("")) {
            rank = MopsRank.valueOf(text);
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
            line.append(uuid).append(":0:").append(rank).append(":NONE:eng:NONE").append("\n");
        }
        try {
            MopsUtils.writeFile("D:\\servers\\MopsNetwork\\data.txt", line.toString());
        } catch (IOException ignored) { }
    }






    public static MopsBadge getBadge(Player player) {
        MopsBadge badge = MopsBadge.NONE;

        String text = getString("data.txt", player.getUniqueId(), 3);
        if(!text.equals("")) {
            badge = MopsBadge.valueOf(text);
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
            line.append(uuid).append(":0:NONE:").append(badge).append(":eng:NONE").append("\n");
        }
        try {
            MopsUtils.writeFile("D:\\servers\\MopsNetwork\\data.txt", line.toString());
        } catch (IOException ignored) { }
    }



    public static String getLanguage(Player player) {
        String lang = "eng";

        String text = getString("data.txt", player.getUniqueId(), 4);
        if(!text.equals("")) {
            lang = text;
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
            line.append(uuid).append(":0:NONE:NONE:").append(lang).append(":NONE").append("\n");
        }
        try {
            MopsUtils.writeFile("D:\\servers\\MopsNetwork\\data.txt", line.toString());
        } catch (IOException ignored) { }
    }



    public static Aura getAura(Player player) {
        Aura aura = Aura.NONE;

        String text = getString("data.txt", player.getUniqueId(), 5);
        if(!text.equals("")) {
            aura = Aura.valueOf(text);
        }
        return aura;
    }

    public static void setAura(Player player, Aura aura) {
        UUID uuid = player.getUniqueId();
        StringBuilder line = new StringBuilder();
        boolean playerExists = false;

        String[] list = MopsUtils.readFile("D:\\servers\\MopsNetwork\\data.txt").split("\n");
        for (String row : list) {
            String[] array = row.split(":");
            if(UUID.fromString(array[0]).equals(uuid)) {
                array[5] = aura.toString();
                playerExists = true;
            }
            line.append(MopsUtils.combineStrings(array, ":")).append("\n");
        }
        if(!playerExists) {
            line.append(uuid).append(":0:NONE:NONE:eng:").append(aura.toString()).append("\n");
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
            String[] array = row.split(":");
            valuesList.add(array[1]);
        }
        return valuesList;
    }










    public static Integer getWoolbattleWins(Player player) {
        int wins = 0;

        String text = getString("woolbattleStats.txt", player.getUniqueId(), 1);
        if(!text.equals("")) {
            wins = Integer.parseInt(text);
        }
        return wins;
    }

    public static void setWoolbattleWins(Player player, Integer wins) {
        UUID uuid = player.getUniqueId();
        StringBuilder line = new StringBuilder();
        boolean playerExists = false;

        String[] list = MopsUtils.readFile("D:\\servers\\MopsNetwork\\woolbattleStats.txt").split("\n");
        for (String row : list) {
            String[] array = row.split(":");
            if(UUID.fromString(array[0]).equals(uuid)) {
                array[1] = String.valueOf(wins);
                playerExists = true;
            }
            line.append(MopsUtils.combineStrings(array, ":")).append("\n");
        }
        if(!playerExists) {
            line.append(uuid).append(":").append(wins).append(":0:0").append("\n");
        }
        try {
            MopsUtils.writeFile("D:\\servers\\MopsNetwork\\woolbattleStats.txt", line.toString());
        } catch (IOException ignored) { }
    }




    public static Integer getWoolbattleWipeouts(Player player) {
        int wipeouts = 0;

        String text = getString("woolbattleStats.txt", player.getUniqueId(), 2);
        if(!text.equals("")) {
            wipeouts = Integer.parseInt(text);
        }
        return wipeouts;
    }

    public static void setWoolbattleWipeouts(Player player, Integer wipeouts) {
        UUID uuid = player.getUniqueId();
        StringBuilder line = new StringBuilder();
        boolean playerExists = false;

        String[] list = MopsUtils.readFile("D:\\servers\\MopsNetwork\\woolbattleStats.txt").split("\n");
        for (String row : list) {
            String[] array = row.split(":");
            if(UUID.fromString(array[0]).equals(uuid)) {
                array[2] = String.valueOf(wipeouts);
                playerExists = true;
            }
            line.append(MopsUtils.combineStrings(array, ":")).append("\n");
        }
        if(!playerExists) {
            line.append(uuid).append(":0:").append(wipeouts).append(":0").append("\n");
        }
        try {
            MopsUtils.writeFile("D:\\servers\\MopsNetwork\\woolbattleStats.txt", line.toString());
        } catch (IOException ignored) { }
    }




    public static Integer getWoolbattleDominations(Player player) {
        int dominations = 0;

        String text = getString("woolbattleStats.txt", player.getUniqueId(), 3);
        if(!text.equals("")) {
            dominations = Integer.parseInt(text);
        }
        return dominations;
    }

    public static void setWoolbattleDominations(Player player, Integer dominations) {
        UUID uuid = player.getUniqueId();
        StringBuilder line = new StringBuilder();
        boolean playerExists = false;

        String[] list = MopsUtils.readFile("D:\\servers\\MopsNetwork\\woolbattleStats.txt").split("\n");
        for (String row : list) {
            String[] array = row.split(":");
            if(UUID.fromString(array[0]).equals(uuid)) {
                array[3] = String.valueOf(dominations);
                playerExists = true;
            }
            line.append(MopsUtils.combineStrings(array, ":")).append("\n");
        }
        if(!playerExists) {
            line.append(uuid).append(":0:0:").append(dominations).append("\n");
        }
        try {
            MopsUtils.writeFile("D:\\servers\\MopsNetwork\\woolbattleStats.txt", line.toString());
        } catch (IOException ignored) { }
    }


    public static HashMap<UUID, Integer> getTotalWinHash() {
        HashMap<UUID, Integer> totalWins = new HashMap<>();

        String[] list = MopsUtils.readFile("D:\\servers\\MopsNetwork\\woolbattleStats.txt").split("\n");
        for (String row : list) {
            String[] array = row.split(":");

            int totalWinNumber = 0;
            totalWinNumber += Integer.parseInt(array[1]);
            totalWinNumber += Integer.parseInt(array[2]);
            totalWinNumber += Integer.parseInt(array[3]);

            totalWins.put(UUID.fromString(array[0]), totalWinNumber);
        }

        return totalWins;
    }










    public static boolean getWasAtPigeon(Player player) {
        boolean bool = false;

        String text = getString("destinations.txt", player.getUniqueId(), 1);
        if(!text.equals("")) {
            bool = Boolean.parseBoolean(text);
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
            line.append(uuid).append(":").append(bool).append("\n");
        }
        try {
            MopsUtils.writeFile("D:\\servers\\MopsNetwork\\destinations.txt", line.toString());
        } catch (IOException ignored) { }
    }








    public static void addDelivery(UUID recieverUUID, ItemStack item, UUID senderUUID) {
        String string = MopsUtils.readFile("D:\\servers\\MopsNetwork\\packages.txt");
        Delivery delivery = new Delivery().createNewDelivery(item, senderUUID, recieverUUID);
        string += delivery.toString() + "\n";
        addUsedUpDeliveryID(delivery.getDeliveryID());

        try {
            MopsUtils.writeFile("D:\\servers\\MopsNetwork\\packages.txt", string);
        } catch (IOException ignored) { }
    }

    public static void addDelivery(Delivery delivery) {
        String string = MopsUtils.readFile("D:\\servers\\MopsNetwork\\packages.txt");
        string += delivery.toString() + "\n";
        addUsedUpDeliveryID(delivery.getDeliveryID());

        try {
            MopsUtils.writeFile("D:\\servers\\MopsNetwork\\packages.txt", string);
        } catch (IOException ignored) { }
    }



    public static void removeDelivery(String deliveryID) {
        List<String> list = Arrays.asList(MopsUtils.readFile("D:\\servers\\MopsNetwork\\packages.txt").split("\n"));
        for (String row : list) {
            String[] array = row.split(";;");
            if(array[3].equals(deliveryID)) {
                list.remove(row);
            }
        }
        removeUsedUpDeliveryID(deliveryID);

        try {
            MopsUtils.writeFile("D:\\servers\\MopsNetwork\\packages.txt", MopsUtils.combineStrings(list, "\n"));
        } catch (IOException ignored) { }
    }



    public static Delivery getDelivery(String deliveryID) {
        Delivery delivery = new Delivery();

        List<String> list = Arrays.asList(MopsUtils.readFile("D:\\servers\\MopsNetwork\\packages.txt").split("\n"));
        for (String row : list) {
            String[] array = row.split(";;");
            if(array[3].equals(deliveryID)) {
                delivery = delivery.fromString(row);
            }
        }

        return delivery;
    }



    public static List<Delivery> getDeliveries(UUID recieverUUID) {
        List<Delivery> deliveries = new ArrayList<>();

        String[] list = MopsUtils.readFile("D:\\servers\\MopsNetwork\\packages.txt").split("\n");
        for (String row : list) {
            String[] array = row.split(";;");
            if(UUID.fromString(array[0]).equals(recieverUUID)) {
                Delivery delivery = new Delivery().fromString(row);
                deliveries.add(delivery);
            }
        }
        return deliveries;
    }



    public static List<Delivery> getDeliveries() {
        List<Delivery> deliveries = new ArrayList<>();

        String[] list = MopsUtils.readFile("D:\\servers\\MopsNetwork\\packages.txt").split("\n");
        for (String row : list) {
            Delivery delivery = new Delivery().fromString(row);
            deliveries.add(delivery);
        }
        return deliveries;
    }



    public static List<String> getUsedUpDeliveryIDs() {
        return Arrays.asList(MopsUtils.readFile("D:\\servers\\MopsNetwork\\usedUpDeliveryIDs.txt").split("\n"));
    }
    public static void addUsedUpDeliveryID(String deliveryID) {
        String string = MopsUtils.readFile("D:\\servers\\MopsNetwork\\usedUpDeliveryIDs.txt");
        string += deliveryID + "\n";
        try {
            MopsUtils.writeFile("D:\\servers\\MopsNetwork\\usedUpDeliveryIDs.txt", string);
        } catch (IOException ignored) { }
    }
    public static void removeUsedUpDeliveryID(String deliveryID) {
        List<String> list = Arrays.asList(MopsUtils.readFile("D:\\servers\\MopsNetwork\\usedUpDeliveryIDs.txt").split("\n"));
        list.remove(deliveryID);

        try {
            MopsUtils.writeFile("D:\\servers\\MopsNetwork\\usedUpDeliveryIDs.txt", MopsUtils.combineStrings(list, "\n"));
        } catch (IOException ignored) { }
    }









    private static String getString(String fileName, UUID keyUUID, int arrayNumber) {
        String returnString = "";

        String[] list = MopsUtils.readFile("D:\\servers\\MopsNetwork\\" + fileName).split("\n");
        for (String row : list) {
            String[] array = row.split(":");
            if(UUID.fromString(array[0]).equals(keyUUID)) {
                returnString = array[arrayNumber];
            }
        }
        return returnString;
    }
}
