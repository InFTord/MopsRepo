package ml.mops.inventories;

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
import org.checkerframework.common.value.qual.StaticallyExecutable;
import org.jetbrains.annotations.Contract;

import java.util.Objects;

public class KitGUI implements Listener {
    private final Inventory inv;

    public KitGUI() {
        inv = Bukkit.createInventory(null, 27, "Kits List");

        setItems();
    }

    public ItemStack item(Material material) {
        return new ItemStack(material);
    }
    public ItemStack item(Material material, int i) {
        return new ItemStack(material, i);
    }

    public void setItems() {
        inv.setItem(0, MopsUtils.renameItem(item(Material.PURPLE_SHULKER_BOX), ChatColor.DARK_PURPLE + "Mage"));
        inv.setItem(1, item(Material.BLACK_SHULKER_BOX));
        inv.setItem(2, item(Material.ORANGE_SHULKER_BOX));
        inv.setItem(3, item(Material.LIGHT_BLUE_SHULKER_BOX));
        inv.setItem(4, item(Material.LIGHT_GRAY_SHULKER_BOX));
        inv.setItem(5, item(Material.RED_SHULKER_BOX));
        inv.setItem(6, item(Material.BLUE_SHULKER_BOX));
        inv.setItem(7, item(Material.MAGENTA_SHULKER_BOX));
        inv.setItem(8, item(Material.LIME_SHULKER_BOX));
        inv.setItem(10, item(Material.WHITE_SHULKER_BOX));
        inv.setItem(11, item(Material.CYAN_SHULKER_BOX));
        inv.setItem(12, item(Material.BROWN_SHULKER_BOX));
        inv.setItem(13, item(Material.GREEN_SHULKER_BOX));
        inv.setItem(14, item(Material.GRAY_SHULKER_BOX));
        inv.setItem(15, item(Material.YELLOW_SHULKER_BOX));
        inv.setItem(16, item(Material.PINK_SHULKER_BOX));
    }

    public void openInventory(Player player) {
        player.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!Objects.equals(event.getClickedInventory(), inv)) return;

        event.setCancelled(true);
    }
}