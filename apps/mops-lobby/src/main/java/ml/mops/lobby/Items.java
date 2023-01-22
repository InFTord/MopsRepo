package ml.mops.lobby;

import ml.mops.utils.MopsUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Items {
    public ItemStack compass() {
        ItemStack item = new ItemStack(Material.COMPASS);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY + "Compass");
        meta.setLore(new ArrayList<String>(Collections.singletonList(ChatColor.GRAY + "Easy teleport to games.")));
        item.setItemMeta(meta);

        return item;
    }
    public ItemStack effects() {
        ItemStack item = new ItemStack(Material.GLISTERING_MELON_SLICE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "Effects");
        meta.setLore(new ArrayList<String>(Collections.singletonList(ChatColor.GRAY + "Change your appearance.")));
        item.setItemMeta(meta);

        return item;
    }
    public ItemStack mopsCoin() {
        ItemStack item = new ItemStack(Material.GOLD_INGOT);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "MopsCoin");
        meta.setLore(new ArrayList<String>(Collections.singletonList(ChatColor.GRAY + "The main currency of MopsNetwork.")));
        item.setItemMeta(meta);

        return item;
    }
//    public ItemStack kuudraWashingMachine() {
//        ItemStack item = new ItemStack(Material.GOLD_INGOT);
//        ItemMeta meta = item.getItemMeta();
//        meta.setDisplayName(ChatColor.GOLD + "MopsCoin");
//        List<String> lore = new ArrayList<>();
//        lore.add(ChatColor.GRAY + "the um balls i guess");
//        lore.add(" ");
//        lore.add()
//
//
//        item.setItemMeta(meta);
//
//        return item;
//    }

}
