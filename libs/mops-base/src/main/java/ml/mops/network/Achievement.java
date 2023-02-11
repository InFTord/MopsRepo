package ml.mops.network;

import ml.mops.utils.MopsUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public enum Achievement {
    DELIVERED("Delivered", "Get a Delivery.", 1, false, new ItemStack(Material.BARREL)),
    DELIVERY("Delivery", "Send a Delivery.", 1, false, new ItemStack(Material.CHEST_MINECART)),
    DOMINATION("DOMINATION!!!", "Win by Domination in Woolbattle.", 1,false, new ItemStack(Material.IRON_BLOCK)),
    GENOCIDE("Genocide", "Win by killing in Woolbattle.", 1, false, new ItemStack(Material.GOLDEN_SWORD)),
    PACIFIST("Pacifist", "Win with no kills in Woolbattle.", 1,false, new ItemStack(Material.WOODEN_SWORD)),
    FASTBLOOD("Fast Blood", "Kill someone in the first 20 seconds of a Woolbattle game.", 1, false, new ItemStack(Material.REDSTONE)),
    WOOLLIMIT("A Lot of Sheep", "Get to the Wool limit in Woolbattle.", 1,false, new ItemStack(Material.LIGHT_BLUE_WOOL)),
    MANAGED("Managed", "Talk to Kit Manager.", 1,false, MopsUtils.createCustomHead("6a56870fe52c7c509045bab3e9fc66d2c163cf1c842ba0a9ab42f98a79cb306a")),
    PATCHED("Patched", "Read the Changelog.", 1,false, new ItemStack(Material.BOOKSHELF)),
    LONELYPIGEON("No More Lonely", "Talk to Lonely Pigeon.", 1,true, MopsUtils.createCustomHead("b7ea4c017e3456cf09a5c263f34d3cc5f41577b74d60f6f8196c60e07f8c5a96")),
    SILLY("silly :p", "Talk to Milly.", 1, true, MopsUtils.createCustomHead("340097271bb680fe981e859e8ba93fea28b813b1042bd277ea3329bec493eef3")),
    CORN("CORNer", "Find the Corn room.", 1, true, new ItemStack(Material.YELLOW_CANDLE)),
    ASTARTA("Astarta", "Read the entirety of 1000 и 1 факт про Астарту.", 1,true, new ItemStack(Material.BOOK)),
    FRIENDLY("Friendly", "Speak to all the Doges in the Hub.", 9,false, MopsUtils.createCustomHead("a39e43db83a966c5b6f6c03a17d7c6da4ba1b622e67c28ce6017607689f6e232"));

    final String name;
    final String description;
    final int progress;
    final boolean hidden;
    final ItemStack item;

    Achievement (String name, String description, int progress, boolean hidden, ItemStack item) {
        this.name = name;
        this.description = description;
        this.progress = progress;
        this.hidden = hidden;
        this.item = item;
    }

    public ItemStack getItem() {
        if(hidden) {
            return MopsUtils.renameItem(MopsUtils.createCustomHead("974fe9cb80029d66345277aa560d41ef1030962b7f29abf23961d9eba84250a3"), ChatColor.DARK_GRAY + name);
        }

        ItemStack itemCopy = item;
        ItemMeta meta = itemCopy.getItemMeta();
        List<String> lore = new ArrayList<>();
        String[] split = description.split(" ");

        if(split.length > 7) {
            StringBuilder firstLine = new StringBuilder();
            int i = 0;
            while (i < 7) {
                firstLine.append(split[i]);
                i++;
            }
            lore.add(ChatColor.GRAY + firstLine.toString());

            StringBuilder secondLine = new StringBuilder();
            int i2 = 7;
            while (i2 < split.length) {
                secondLine.append(split[i2+7]);
                i2++;
            }
            lore.add(ChatColor.GRAY + secondLine.toString());
        } else {
            lore.add(ChatColor.GRAY + MopsUtils.combineStrings(split, " "));
        }
        meta.setLore(lore);
        itemCopy.setItemMeta(meta);

        return itemCopy;
    }
}