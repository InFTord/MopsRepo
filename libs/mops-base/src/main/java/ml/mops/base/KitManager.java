package ml.mops.base;

import ml.mops.utils.MopsColor;
import ml.mops.utils.MopsUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

public abstract class KitManager {



    public static Inventory createKit(String[] itemArray, HashMap<String, ItemStack> map) {
        Inventory inv = Bukkit.createInventory(null, 27, "placeholder name ignore");

        int i = 0;
        while (i < 27) {
            ItemStack item = map.get(MopsUtils.combineStrings(itemArray, " ").split(" ")[i]);
            inv.setItem(i, item);
            i++;
        }

        return inv;
    }

    public static HashMap<String, ItemStack> baseMapping() {
        HashMap<String, ItemStack> map = new HashMap<>();
        map.put("0", new ItemStack(Material.AIR));
        return map;
    }

    public static HashMap<String, ItemStack> kitSpecificMapping(Kit kit) {
        HashMap<String, ItemStack> map = new HashMap<>();
        map.put("0", new ItemStack(Material.AIR));

        return map;
    }
}
