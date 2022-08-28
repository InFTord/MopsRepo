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

    public static HashMap<String, ItemStack> pyroMapping() {
        HashMap<String, ItemStack> map = new HashMap<>();
        map.put("0", new ItemStack(Material.AIR));

        map.put("x", MopsUtils.createItem(Material.GOLDEN_HOE, 1,  ChatColor.RED + "Backburner", true));
        map.put("y", MopsUtils.createItem(Material.IRON_HORSE_ARMOR, 1, ChatColor.GRAY + "Scorch" + ChatColor.GOLD + "Shot", true));

        ItemStack powerjack = MopsUtils.createItem(Material.NETHERITE_AXE, 1,  ChatColor.GRAY + "Powerjack", true);
        ItemMeta powermeta = powerjack.getItemMeta();
        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.movement_speed", 0.15, AttributeModifier.Operation.MULTIPLY_SCALAR_1, EquipmentSlot.HAND);
        powermeta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, modifier);
        powerjack.setItemMeta(powermeta);
        map.put("z", powerjack);

        ItemStack gasmask = MopsUtils.renameItem(MopsUtils.createCustomHead("41735ca024b86e1ee5664b2bf3eaaaa8257701051c8517b88098209df9e84072"), ChatColor.DARK_GRAY + "Gas Mask");
        gasmask.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, true);
        map.put("a", gasmask);
        map.put("b", MopsUtils.colorItem(MopsUtils.createItem(Material.LEATHER_CHESTPLATE, 1, ChatColor.RED + "Pyro Chestplate", true), "#d14226"));
        map.put("c", MopsUtils.colorItem(MopsUtils.createItem(Material.LEATHER_LEGGINGS, 1, ChatColor.RED + "Pyro Leggings", true), "#a64835"));
        map.put("d", MopsUtils.colorItem(MopsUtils.createItem(Material.LEATHER_BOOTS, 1, ChatColor.RED + "Pyro Boots", true), "#73392e"));

        return map;
    }
}
