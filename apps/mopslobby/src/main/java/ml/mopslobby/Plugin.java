package ml.mopslobby;

//import ml.mopspvps.Commands;
import com.google.errorprone.annotations.Var;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import ml.mopslobby.Commands;
import org.bukkit.util.EulerAngle;
import org.jetbrains.annotations.NotNull;

public class Plugin extends JavaPlugin implements Listener, CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        return new Commands().commandsExecutor(sender, command, label, args, this);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        event.setCancelled(!player.getScoreboardTags().contains("admin"));
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        event.setCancelled(!player.getScoreboardTags().contains("admin"));
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();

        ItemStack item = player.getInventory().getItemInMainHand();
        if(item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            assert meta != null;

            if (meta.getDisplayName().equals(ChatColor.RESET + "кто")) {
                int i = 0;

                while (i < 9) {
                    Entity entity = player.getLocation().getWorld().spawnEntity(player.getLocation(), EntityType.FROG);
                    Frog frog = (Frog) entity;

                    entity.setGravity(false);

                    entity.getLocation().setYaw(player.getLocation().getYaw());
                    entity.getLocation().setPitch(player.getLocation().getPitch());

                    int randomY = (int) (Math.random() * (1 - 3 + 1)) + 1;
                    entity.teleport(entity.getLocation().add(0, randomY, 0));

                    int frogVariant = (int) (Math.random() * (1 - 3 + 1)) + 1;
                    Frog.Variant variant = Frog.Variant.COLD;
                    switch (frogVariant) {
                        case 1 -> variant = Frog.Variant.TEMPERATE;
                        case 3 -> variant = Frog.Variant.WARM;
                    }
                    frog.setVariant(variant);

                    Bukkit.getScheduler().runTaskLater(this, () -> {
                        entity.teleport(entity.getLocation().add(0, -1000, 0));
                    }, 180L);

                    Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
                        entity.teleport(((Frog) entity).getEyeLocation());

                        for(Entity entity2 : entity.getNearbyEntities(3, 3, 3)) {
                            if(entity2 instanceof Player && entity2 != player) {
                                ((Player) entity2).damage(1);
                            }
                        }
                    }, 0L, 0L);

                    i++;
                }
            }
        }

        event.setCancelled(!player.getScoreboardTags().contains("admin"));
    }

}
