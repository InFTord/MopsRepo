package ml.mops.woolbattle;

import ml.mops.base.maps.Map;
import ml.mops.base.maps.MapManager;
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

import java.util.List;
import java.util.Objects;

public class ItemGUI implements Listener {
    private final Inventory inv;
    Plugin plugin;

    public ItemGUI(Plugin plugin) {
        this.plugin = plugin;

        inv = Bukkit.createInventory(null, 27, "infinite items wooo");

        setItems();
    }

    public void setItems() {
        Items items = new Items(plugin);

        inv.setItem(0, items.shears("eng"));
        inv.setItem(1, items.boomstickMK1("eng"));
        inv.setItem(2, items.boomstickMK2("eng"));
        inv.setItem(3, items.slimeball("eng"));
        inv.setItem(4, items.platform("eng"));
        inv.setItem(5, items.platform3d("eng"));
        inv.setItem(6, items.doubleJumpBoots("eng", Teams.RED));
        inv.setItem(7, items.doubleJumpBoots("eng", Teams.YELLOW));
        inv.setItem(8, items.doubleJumpBoots("eng", Teams.GREEN));
        inv.setItem(9, items.doubleJumpBoots("eng", Teams.BLUE));
        inv.setItem(10, items.doubleJumpBoots("eng", Teams.SPECTATOR));
        inv.setItem(11, items.leaves("eng"));
        inv.setItem(12, items.enderpearl("eng"));
        inv.setItem(13, items.shield("eng"));
        inv.setItem(14, items.axe("eng"));
        inv.setItem(15, items.stockBow("eng"));
        inv.setItem(16, items.potion("eng"));
        inv.setItem(17, items.flareGun("eng"));
        inv.setItem(18, items.sniperRifle("eng"));

    }

    public void openInventory(Player player) {
        player.openInventory(inv);
    }

    public Inventory getInventory() {
        return inv;
    }
}
