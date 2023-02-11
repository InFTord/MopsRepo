package ml.mops.network;

import ml.mops.utils.MopsFiles;
import ml.mops.utils.MopsUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class Delivery {
    private ItemStack deliveredItem = MopsUtils.addLore(MopsUtils.createItem(Material.SUGAR, "kuudra washing machine powder"), new String[] {ChatColor.GRAY + "(legal)"});
    private UUID sender = UUID.randomUUID();
    private UUID reciever = UUID.randomUUID();
    private String deliveryID;
    private String key = "player";

    public Delivery() {
        generateNewID();

        while(MopsFiles.getUsedUpDeliveryIDs().contains(deliveryID)) {
            generateNewID();
        }
    }

    public String toString() {
        String string = "";

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            dataOutput.writeObject(deliveredItem);

            dataOutput.close();

            string = Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception ignored) { }

        return reciever.toString() + ";;" +
               string + ";;" +
               sender.toString() + ";;" +
               deliveryID + ";;" +
               key;
    }

    public Delivery fromString(String string) {
        String[] array = string.split(";;");

        reciever = UUID.fromString(array[0]);

        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(array[1]));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);

            ItemStack item = (ItemStack) dataInput.readObject();

            dataInput.close();
            deliveredItem = item;
        } catch (Exception ignored) { }

        sender = UUID.fromString(array[2]);
        deliveryID = array[3];
        key = array[4];

        return this;
    }

    public Delivery createNewDelivery(ItemStack deliveredItem, UUID sender, UUID reciever) {
        this.deliveredItem = deliveredItem;
        this.sender = sender;
        this.reciever = reciever;

        generateNewID();

        while(MopsFiles.getUsedUpDeliveryIDs().contains(deliveryID)) {
            generateNewID();
        }

        return this;
    }


    public void generateNewID() {
        int maxSize = (int) (Math.random() * (12 - 6 + 1)) + 10;
        StringBuilder text = new StringBuilder();
        String alphabet = "abcdefghijklmnopqrstuvwxyz1234567890!@";
        int i = 0;
        while(i < maxSize) {
            int letterNumber = (int) (Math.random() * (37 + 1)) + 1;
            text.append(alphabet.charAt(letterNumber-1));
            i++;
        }
        deliveryID = text.toString();
    }


    public UUID getReciever() {
        return reciever;
    }

    public ItemStack getDeliveredItem() {
        return deliveredItem;
    }

    public UUID getSender() {
        return sender;
    }

    public String getDeliveryID() {
        return deliveryID;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String newKey) {
        key = newKey;
    }
}
