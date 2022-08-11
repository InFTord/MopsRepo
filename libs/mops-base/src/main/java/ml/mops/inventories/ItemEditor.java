package ml.mops.inventories;

import ml.mops.utils.MopsUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class ItemEditor implements Listener {
    private final Inventory inv;

    public ItemEditor(ItemStack heldItem) {
        inv = Bukkit.createInventory(null, 27, "Item Editor");

        setItems(heldItem);
        MopsUtils.fillInventory(inv, new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1));
    }

    public void setItems(ItemStack heldItem) {
        inv.setItem(11, heldItem);
    }

    public void openInventory(final HumanEntity ent) {
        ent.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (!e.getInventory().equals(inv)) return;

        e.setCancelled(true);
    }
}