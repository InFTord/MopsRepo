package ml.mops.base;

import ml.mops.utils.MopsUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.HashMap;

public class KitManager {
    public Inventory createKit(String[] itemArray, String name, HashMap<String, ItemStack> map) {
        Inventory inv = Bukkit.createInventory(null, 27, name);

        int i = 0;
        while (i < 27) {
            ItemStack item = map.get(MopsUtils.combineStrings(itemArray).replaceAll(" ", "").charAt(i));
            inv.setItem(i, item);

            i++;
        }

        return inv;
    }

    public HashMap<String, ItemStack> baseMapping() {
        HashMap<String, ItemStack> map = new HashMap<>();
        map.put("0", new ItemStack(Material.AIR));
        return map;
    }
}
