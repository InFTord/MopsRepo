package ml.mops.lobby;

import ml.mops.utils.MopsUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class MapGUI implements Listener {
    private final Inventory inv;

    public MapGUI() {
        inv = Bukkit.createInventory(null, 45, "Map List");

        setItems();
    }

    public ItemStack item(Material material) {
        return new ItemStack(material);
    }
    public ItemStack item(Material material, int i) {
        return new ItemStack(material, i);
    }

    public void setItems() {

    }

    public void openInventory(Player player) {
        player.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == inv) {
            event.setCancelled(true);


        }
    }
}
