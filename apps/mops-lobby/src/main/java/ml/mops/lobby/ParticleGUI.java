package ml.mops.lobby;

import ml.mops.base.maps.Map;
import ml.mops.base.maps.MapManager;
import ml.mops.utils.MopsUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ParticleGUI implements Listener {
    private final Inventory inv;

    public ParticleGUI() {
        inv = Bukkit.createInventory(null, 27, "Select a Particle");

        setItems();
    }

    public void setItems() {
        ItemStack emeraldItem = new ItemStack(Material.EMERALD_BLOCK);
        ItemMeta emeraldMeta = emeraldItem.getItemMeta();
        emeraldMeta.setDisplayName(ChatColor.GREEN + "Emerald Cube");
        List<String> emeraldLore = new ArrayList<>();
        emeraldLore.add(ChatColor.DARK_GREEN + "Spawns a large emerald cube around you.");
        emeraldMeta.setLore(emeraldLore);
        emeraldItem.setItemMeta(emeraldMeta);

        ItemStack andromedaItem = new ItemStack(Material.FIRE_CHARGE);
        ItemMeta andromedaMeta = andromedaItem.getItemMeta();
        andromedaMeta.setDisplayName(ChatColor.GOLD + "Andromeda");
        List<String> andromedaLore = new ArrayList<>();
        andromedaLore.add(ChatColor.RED + "Spawns a large planet around you.");
        andromedaLore.add(" ");
        andromedaLore.add(ChatColor.GOLD + "⚡ Silly " + ChatColor.GRAY + "club members only effect.");
        andromedaMeta.setLore(andromedaLore);
        andromedaItem.setItemMeta(andromedaMeta);

        ItemStack infinityItem = new ItemStack(Material.MUSIC_DISC_5);
        ItemMeta infinityMeta = infinityItem.getItemMeta();
        infinityMeta.setDisplayName(ChatColor.DARK_AQUA + "Infinity");
        infinityMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        List<String> infinityLore = new ArrayList<>();
        infinityLore.add(ChatColor.BLUE + "Truly Infinite.");
        infinityLore.add(" ");
        infinityLore.add(ChatColor.BLUE + "⭐ Staff " + ChatColor.GRAY + "members only effect.");
        infinityMeta.setLore(infinityLore);
        infinityItem.setItemMeta(infinityMeta);

        ItemStack resetItem = new ItemStack(Material.BARRIER);
        ItemMeta resetMeta = infinityItem.getItemMeta();
        resetMeta.setDisplayName(ChatColor.RED + "Reset");
        resetMeta.setLore(new ArrayList<String>(Collections.singletonList(ChatColor.GRAY + "Resets your effects.")));
        resetItem.setItemMeta(resetMeta);

        inv.setItem(0, emeraldItem);
        inv.setItem(1, andromedaItem);
        inv.setItem(2, infinityItem);
        inv.setItem(26, resetItem);
    }

    public void openInventory(Player player) {
        player.openInventory(inv);
    }

    public Inventory getInventory() {
        return inv;
    }
}
