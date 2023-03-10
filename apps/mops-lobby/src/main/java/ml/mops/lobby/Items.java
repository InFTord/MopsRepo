package ml.mops.lobby;

import ml.mops.utils.MopsUtils;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
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
    public ItemStack kuudraWashingMachine() {
        ItemStack item = MopsUtils.createCustomHead("c7de63d401f85eed8a5e08cfa6cb290c40b11a7c72b579b2f06e8bb4a8a7c099");
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Kuudra Washing Machine 2.0");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "New compact all-in-one combo washing machine!");
        lore.add(ChatColor.DARK_GRAY + "Dimensions: 60.9cm x 85.1cm x 56,5cm");
        lore.add(" ");
        lore.add(ChatColor.GRAY + "Fuel Type: Electric");
        lore.add(ChatColor.GRAY + "Energy Consumption: 120 kWh/Year");
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
        meta.setColor(Color.fromRGB(55, 207, 50));
        meta.setDisplayName(ChatColor.GREEN + "Mops Chemistry Set!");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Brand new Chemistry Set from Mops!");
        lore.add(ChatColor.RED + "<" + ChatColor.GRAY + "<" + ChatColor.YELLOW + "<" + ChatColor.GRAY + " Now contains: " + ChatColor.GREEN + "" + ChatColor.BOLD + "URANIUM-238! " + ChatColor.YELLOW + ">" + ChatColor.GOLD + ">" + ChatColor.YELLOW + ">");
        lore.add(" ");
        lore.add(ChatColor.DARK_GREEN + "Ever wanted to " + ChatColor.GREEN + "shine" + ChatColor.DARK_GREEN + "? Well, now you can!");
        lore.add(ChatColor.RED + "Right-Click" + ChatColor.GRAY + " to activate!");
        meta.setLore(lore);
        item.setItemMeta(meta);

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
