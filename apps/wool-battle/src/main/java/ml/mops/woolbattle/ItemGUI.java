package ml.mops.woolbattle;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;

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

        inv.setItem(0, items.shears(plugin.lang));
        inv.setItem(1, items.boomstickMK1(plugin.lang));
        inv.setItem(2, items.boomstickMK2(plugin.lang));
        inv.setItem(3, items.boomstickMK3(plugin.lang));
        inv.setItem(4, items.slimeball(plugin.lang));
        inv.setItem(5, items.platform(plugin.lang));
        inv.setItem(6, items.platform3d(plugin.lang));
        inv.setItem(7, items.doubleJumpBoots(plugin.lang, Teams.RED));
        inv.setItem(8, items.doubleJumpBoots(plugin.lang, Teams.YELLOW));
        inv.setItem(9, items.doubleJumpBoots(plugin.lang, Teams.GREEN));
        inv.setItem(10, items.doubleJumpBoots(plugin.lang, Teams.BLUE));
        inv.setItem(11, items.doubleJumpBoots(plugin.lang, Teams.SPECTATOR));
        inv.setItem(12, items.leaves(plugin.lang));
        inv.setItem(13, items.enderpearl(plugin.lang));
        inv.setItem(14, items.shield(plugin.lang));
        inv.setItem(15, items.axe(plugin.lang));
        inv.setItem(16, items.stockBow(plugin.lang));
        inv.setItem(17, items.potion(plugin.lang));

    }

    public void openInventory(Player player) {
        player.openInventory(inv);
    }

    public Inventory getInventory() {
        return inv;
    }
}
