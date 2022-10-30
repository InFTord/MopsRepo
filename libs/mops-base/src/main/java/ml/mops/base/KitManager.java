package ml.mops.base;

import ml.mops.utils.MopsColor;
import ml.mops.utils.MopsUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;

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

    public static HashMap<String, ItemStack> kitSpecificMapping(String kit) {
        HashMap<String, ItemStack> map = new HashMap<>();
        map.put("0", new ItemStack(Material.AIR));

        switch (kit) {
            case "bear" -> {
                map.put("b", MopsUtils.createItem(Material.BEETROOT_SOUP, ChatColor.RED + "Borsch", 1, new String[] {ChatColor.GRAY + "Instantly regenerates hunger."}));

                ItemStack icestaff = MopsUtils.createItem(Material.STICK, ChatColor.DARK_AQUA + "Ice Staff", 1, new String[] {ChatColor.GRAY + MopsUtils.upperSquares(12),
                ChatColor.YELLOW + "Cooldown: 10s", ChatColor.GRAY + MopsUtils.bottomSquares(12), ChatColor.RED + "(Right-Click) " + ChatColor.GRAY + "to use.", ChatColor.AQUA + "Freezes " + ChatColor.GRAY + "everything infront of you."});
                icestaff.addEnchant(Enchantment.DAMAGE_ALL, 9, true);
                map.put("s", icestaff);

                ItemStack head = MopsUtils.createCustomHead("e622b11fe8cd9d048859072de21d4b264230af6d87bd297b56a76236dc531547");
                MopsUtils.renameItem(head, ChatColor.AQUA + "Ice Bear's Head");
                head.addEnchant(Enchantment.WATER_WORKER, 1, true); head.addEnchant(Enchantment.OXYGEN, 1, true);
                map.put("h", head);

                ItemStack chestplate = MopsUtils.createItem(Material.CHAINMAIL_CHESTPLATE, ChatColor.AQUA + "Ice Bear's Chestplate");
                chestplate.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
                map.put("c", chestplate);

                ItemStack leggings = MopsUtils.createItem(Material.CHAINMAIL_LEGGINGS, ChatColor.AQUA + "Ice Bear's Leggings", 1, new String[] {ChatColor.GRAY + "Protect you from knockback."});
                leggings.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 8, true);
                map.put("l", leggings);

                ItemStack boots = MopsUtils.createItem(Material.DIAMOND_BOOTS, ChatColor.AQUA + "Icy Boots");
                boots.addEnchant(Enchantment.PROTECTION_FALL, 5, true);
                boots.addEnchant(Enchantment.FROST_WALKER, 1, true);
                boots.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true);
                map.put("bo", boots);

                ItemStack potion = MopsUtils.createSplashPotion(PotionEffectType.SPEED, 1800, 2, Color.AQUA);
                MopsUtils.renameItem(potion, ChatColor.AQUA + "Splash Potion of Speed (1:30)");
                map.put("p", potion);
            }
        }

        return map;
    }
}
