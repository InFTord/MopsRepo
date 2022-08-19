package ml.mops.lobby;

import ml.mops.commands.Commands;
import ml.mops.utils.MopsUtils;
import net.minecraft.network.protocol.game.PacketPlayOutEntityHeadRotation;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutNamedEntitySpawn;
import net.minecraft.network.protocol.game.PacketPlayOutPlayerInfo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.network.PlayerConnection;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import org.bukkit.craftbukkit.v1_19_R1.CraftServer;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class Plugin extends JavaPlugin implements Listener, CommandExecutor {

    public List<EntityPlayer> hubNPCs = new ArrayList<>();

    @Override
    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        World mainworld = Bukkit.getServer().getWorlds().get(0);

        EntityPlayer woolbattleNPC = MopsUtils.createNPC(new Location(mainworld, -70.500, 7, -180.500), ChatColor.YELLOW + "" + ChatColor.BOLD + "WoolBattle", "SirCat07");
        hubNPCs.add(woolbattleNPC);
    }

    @Override
    public void onDisable() {
        World mainworld = Bukkit.getServer().getWorlds().get(0);
        for(Entity entity : mainworld.getEntities()) {
            if(entity.getScoreboardTags().contains("killOnDisable")) {
                entity.teleport(new Location(mainworld, 0, -1000, 0));
            }
        }
        for(Player player : Bukkit.getOnlinePlayers()) {
            player.kickPlayer(ChatColor.YELLOW + "всем привет с вами сиркет \n мопс ппвп закрылся");
        }
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        return new Commands().commandsExecutor(sender, command, label, args, this);
    }


    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if(!player.getScoreboardTags().contains("admin")) {
            event.setCancelled(true);
        }
    }


    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if(!player.getScoreboardTags().contains("admin")) {
            event.setCancelled(true);
        }
    }
    

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if(!player.getScoreboardTags().contains("admin")) {
            event.setCancelled(true);
        }

        try {
            if (event.getAction().isRightClick()) {
                if (player.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Cat Detector")) {
                    if (player.getName().toLowerCase(Locale.ROOT).contains("cat")) {
                        player.sendTitle("You are:", ChatColor.GREEN + "CAT", 40, 30, 20);
                        player.playSound(player.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_5, 1, 1);
                    } else {
                        player.sendTitle("You are:", ChatColor.RED + "NOT CAT", 40, 30, 20);
                        player.playSound(player.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_3, 1, 1);
                    }
                }
            }
        } catch (Exception ignored) { }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        for(EntityPlayer NPC : hubNPCs) {
            PlayerConnection connection = ((CraftPlayer) player).getHandle().b;
            connection.a(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a, NPC));
            connection.a(new PacketPlayOutNamedEntitySpawn(NPC));
            connection.a(new PacketPlayOutEntityHeadRotation(NPC, (byte) (NPC.getBukkitEntity().getLocation().getYaw() * 256 / 360)));

            Bukkit.getScheduler().runTaskLater(this, () -> {
                connection.a(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.e, NPC));
            }, 10);
        }
    }
}
