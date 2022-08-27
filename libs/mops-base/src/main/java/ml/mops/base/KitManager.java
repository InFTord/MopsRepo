package ml.mops.base;

import ml.mops.utils.MopsColor;
import ml.mops.utils.MopsUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.HashMap;

public abstract class KitManager {
    public static Inventory createKit(String[] itemArray, HashMap<String, ItemStack> map) {
        Inventory inv = Bukkit.createInventory(null, 27, "placeholder name ignore");

        int i = 0;
        while (i < 27) {
            ItemStack item = map.get(MopsUtils.combineStrings(itemArray).replaceAll(" ", "").charAt(i));
            inv.setItem(i, item);
            i++;
        }

        return inv;
    }

    public static HashMap<String, ItemStack> baseMapping() {
        HashMap<String, ItemStack> map = new HashMap<>();
        map.put("0", new ItemStack(Material.AIR));

        map.put("z", MopsUtils.createItem(Material.NETHERITE_HOE, 1,  ChatColor.RED +"Phlogistinator"));
        map.put("x", MopsUtils.createItem(Material.IRON_HORSE_ARMOR, 1, ChatColor.GRAY + "Scorch" + ChatColor.GOLD + "Shot"));
        map.put("c", MopsUtils.createItem(Material.NETHERITE_AXE, 1,  ChatColor.GRAY + "Powerjack"));



        return map;
    }

    public static HashMap<String, ItemStack> kitSpecificMapping(Kit kit) {
        HashMap<String, ItemStack> map = new HashMap<>();
        map.put("0", new ItemStack(Material.AIR));

        return map;
    }
}
