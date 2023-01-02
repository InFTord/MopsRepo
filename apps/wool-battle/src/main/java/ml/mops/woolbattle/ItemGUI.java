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

        ItemStack shears = items.shears(plugin.lang);
        plugin.shears.add(shears);
        inv.setItem(0, shears);

        ItemStack boomstickMK1 = items.boomstickMK1(plugin.lang);
        plugin.boomsticks.add(boomstickMK1);
        inv.setItem(1, boomstickMK1);
        ItemStack boomstickMK2 = items.boomstickMK2(plugin.lang);
        plugin.boomsticksMK2.add(boomstickMK2);
        inv.setItem(2, boomstickMK2);
        ItemStack boomstickMK3 = items.boomstickMK3(plugin.lang);
        plugin.boomsticksMK3.add(boomstickMK3);
        inv.setItem(3, boomstickMK3);

        ItemStack slimeball = items.slimeball(plugin.lang);
        plugin.slimeballs.add(slimeball);
        inv.setItem(4, slimeball);

        ItemStack platform = items.platform(plugin.lang);
        plugin.platforms.add(platform);
        inv.setItem(5, platform);
        ItemStack platform3d = items.platform3d(plugin.lang);
        plugin.platforms3d.add(platform3d);
        inv.setItem(6, platform3d);

        ItemStack doubleJumpBootsRED = items.doubleJumpBoots(plugin.lang, Teams.RED);
        plugin.doubleJumpBoots.add(doubleJumpBootsRED);
        inv.setItem(7, doubleJumpBootsRED);
        ItemStack doubleJumpBootsYELLOW = items.doubleJumpBoots(plugin.lang, Teams.YELLOW);
        plugin.doubleJumpBoots.add(doubleJumpBootsYELLOW);
        inv.setItem(8, doubleJumpBootsYELLOW);
        ItemStack doubleJumpBootsGREEN = items.doubleJumpBoots(plugin.lang, Teams.GREEN);
        plugin.doubleJumpBoots.add(doubleJumpBootsGREEN);
        inv.setItem(9, doubleJumpBootsGREEN);
        ItemStack doubleJumpBootsBLUE = items.doubleJumpBoots(plugin.lang, Teams.BLUE);
        plugin.doubleJumpBoots.add(doubleJumpBootsBLUE);
        inv.setItem(10, doubleJumpBootsBLUE);
        ItemStack doubleJumpBootsSPECTATOR = items.doubleJumpBoots(plugin.lang, Teams.SPECTATOR);
        plugin.doubleJumpBoots.add(doubleJumpBootsSPECTATOR);
        inv.setItem(10, doubleJumpBootsSPECTATOR);

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
