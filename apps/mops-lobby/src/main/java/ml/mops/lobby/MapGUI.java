package ml.mops.lobby;

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
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Objects;

public class MapGUI implements Listener {
    private final Inventory inv;

    public MapGUI() {
        inv = Bukkit.createInventory(null, 36, "Select Your Map");

        setItems();
    }

    public void setItems() {
        int i = 0;
        while(i < 27) {
            inv.setItem(i, MopsUtils.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
            i++;
        }

        ItemStack effects = MopsUtils.createItem(Material.GOLDEN_CARROT, ChatColor.GOLD + "Effects");
        ItemMeta effectsMeta = effects.getItemMeta();
        effectsMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        effects.setItemMeta(effectsMeta);

        ItemStack auras = MopsUtils.createItem(Material.MUSIC_DISC_5, ChatColor.DARK_AQUA + "Auras");
        ItemMeta auraMeta = auras.getItemMeta();
        auraMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        auras.setItemMeta(auraMeta);

        inv.setItem(12, effects);
        inv.setItem(14, auras);
    }

    public void openInventory(Player player) {
        player.openInventory(inv);
    }

    public Inventory getInventory() {
        return inv;
    }
}
