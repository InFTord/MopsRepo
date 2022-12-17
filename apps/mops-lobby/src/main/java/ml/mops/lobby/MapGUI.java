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
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;

public class MapGUI implements Listener {
    private final Inventory inv;

    public MapGUI() {
        inv = Bukkit.createInventory(null, 36, "Select Your Map");

        setItems();
    }

    public void setItems() {
        List<Map> maps = MapManager.getPlayableMaps();

        int i = 0;

        while(i < maps.size()) {
            Map map = maps.get(i);

            inv.setItem(i, MopsUtils.createItem(map.getType(), map.getName()));
            i++;
        }
    }

    public void openInventory(Player player) {
        player.openInventory(inv);
    }

    public Inventory getInventory() {
        return inv;
    }
}
