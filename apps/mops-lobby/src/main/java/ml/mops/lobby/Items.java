package ml.mops.lobby;

import ml.mops.utils.MopsColor;
import ml.mops.utils.MopsUtils;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Items {
    public ItemStack compass() {
        ItemStack item = new ItemStack(Material.COMPASS);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY + "Compass");
        meta.setLore(new ArrayList<>(Collections.singletonList(ChatColor.GRAY + "Easy teleport to games.")));
        item.setItemMeta(meta);

        return item;
    }
    public ItemStack customization() {
        ItemStack item = new ItemStack(Material.GLISTERING_MELON_SLICE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "Customize");
        meta.setLore(new ArrayList<>(Collections.singletonList(ChatColor.GRAY + "Change your appearance.")));
        item.setItemMeta(meta);

        return item;
    }
    public ItemStack profile() {
        ItemStack item = new ItemStack(Material.BOOK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(MopsColor.BROWN.getColor() + "Profile & Settings");
        meta.setLore(new ArrayList<>(Collections.singletonList(ChatColor.GRAY + "Change your profile and settings.")));
        item.setItemMeta(meta);

        return item;
    }
    public ItemStack mopsCoin() {
        ItemStack item = new ItemStack(Material.GOLD_INGOT);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "MopsCoin");
        meta.setLore(new ArrayList<>(Collections.singletonList(ChatColor.GRAY + "The main currency of MopsNetwork.")));
        item.setItemMeta(meta);

        return item;
    }

    public ItemStack funnyLantern() {
        ItemStack item = MopsUtils.createItem(Material.LANTERN, ChatColor.GOLD + "Funny Lantern");
        ItemMeta meta = item.getItemMeta();
        meta.setLore(new ArrayList<>(Collections.singletonList(ChatColor.GRAY + "It is very funny tho")));
        item.setItemMeta(meta);

        return item;
    }
    public ItemStack skeletonKey() {
        ItemStack item = MopsUtils.createItem(Material.BONE, ChatColor.GRAY + "Skeleton Key");
        ItemMeta meta = item.getItemMeta();
        meta.setLore(new ArrayList<>(Collections.singletonList(ChatColor.GRAY + "Definitely opens something.")));
        item.setItemMeta(meta);

        return item;
    }


    public ItemStack kuudraWashingMachine() {
        ItemStack item = MopsUtils.createCustomHead("c7de63d401f85eed8a5e08cfa6cb290c40b11a7c72b579b2f06e8bb4a8a7c099");
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Kuudra Washing Machine 2.0");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "New compact all-in-one combo washing machine!");
        lore.add(ChatColor.DARK_GRAY + "Dimensions: 60.9cm x 85.1cm x 56,5cm");
        lore.add(" ");
        lore.add(ChatColor.GRAY + "Fuel Type: Electric");
        lore.add(ChatColor.GRAY + "Energy Consumption: 120 kW/Hour");
        lore.add(ChatColor.GRAY + "Amperage: 13 A");
        lore.add(ChatColor.GRAY + "Voltage: 120 V");
        lore.add(ChatColor.RED + "" + ChatColor.BOLD + "Limited warranty: 4.5b years");
        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }
    public ItemStack mopsChemistrySet() {
        ItemStack item = new ItemStack(Material.POTION);
        PotionMeta meta = ((PotionMeta) item.getItemMeta());
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        meta.setColor(Color.fromRGB(55, 207, 50));
        meta.setDisplayName(ChatColor.GREEN + "Mops Chemistry Set A254");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Brand new Chemistry Set from Mops!");
        lore.add(ChatColor.RED + "<" + ChatColor.GOLD + "<" + ChatColor.YELLOW + "<" + ChatColor.GRAY + " Now contains: " + ChatColor.GREEN + "" + ChatColor.BOLD + "URANIUM-238! " + ChatColor.YELLOW + ">" + ChatColor.GOLD + ">" + ChatColor.RED + ">");
        lore.add(" ");
        lore.add(ChatColor.DARK_GREEN + "Ever wanted to " + ChatColor.GREEN + "shine" + ChatColor.DARK_GREEN + "? Well, now you can!");
        lore.add(ChatColor.RED + "(Right-Click)" + ChatColor.GRAY + " to activate!");
        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    public ItemStack bow() {
        ItemStack item = new ItemStack(Material.BOW);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Bow");
        meta.setLore(new ArrayList<>(Collections.singletonList(ChatColor.GRAY + "Bow to shoot targets and melons.")));
        meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

        item.setItemMeta(meta);

        return item;
    }

    public ItemStack arrow() {
        ItemStack item = new ItemStack(Material.ARROW);
        MopsUtils.renameItem(item, ChatColor.GRAY + "Arrow");

        return item;
    }

    public ItemStack ruleBreaker() {
        ItemStack item = MopsUtils.createCustomHead("8da332abde333a15a6c6fcfeca83f0159ea94b68e8f274bafc04892b6dbfc");
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "RuleBreaker");
        meta.setLore(new ArrayList<String>(Collections.singletonList(ChatColor.GRAY + "You broke The Law.")));
        item.setItemMeta(meta);

        return item;
    }
}
