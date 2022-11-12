package ml.mops.lobby;

//import club.minnced.discord.webhook.WebhookClient;
//import club.minnced.discord.webhook.send.WebhookEmbed;
//import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
//import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import ml.mops.base.commands.Commands;
import ml.mops.utils.MopsUtils;
import net.minecraft.network.protocol.game.PacketPlayOutEntityHeadRotation;
import net.minecraft.network.protocol.game.PacketPlayOutNamedEntitySpawn;
import net.minecraft.network.protocol.game.PacketPlayOutPlayerInfo;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.network.PlayerConnection;
import org.bukkit.entity.Player;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.block.Action;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Plugin extends JavaPlugin implements Listener, CommandExecutor {

//    public List<EntityPlayer> hubNPCs = new ArrayList<>();
//
//    EntityPlayer woolbattleNPC1;

    @Override
    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        World mainworld = Bukkit.getServer().getWorlds().get(0);

//        EntityPlayer woolbattleNPC = MopsUtils.createNPC(new Location(mainworld, -70.500, 7, -180.500), ChatColor.YELLOW + "" + ChatColor.BOLD + "WoolBattle", "SirCat07");
//        hubNPCs.add(woolbattleNPC);
//        woolbattleNPC1 = woolbattleNPC;

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for(Player player : Bukkit.getServer().getOnlinePlayers()) {
                Scoreboard lobbyscoreboard = new LobbyScoreboard().generateLobbyScoreboard(player);
                player.setScoreboard(lobbyscoreboard);
            }
        }, 10L, 10L);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for(Entity entity : Bukkit.getServer().getWorlds().get(0).getEntities()) {
                if(entity.getScoreboardTags().contains("afireparticle")) {
                    entity.getWorld().spawnParticle(Particle.FLAME, entity.getLocation(), 1, 0.01, 0.01, 0.01, 0.01);

                    for(Entity nearEntities : entity.getNearbyEntities(1, 1, 1)) {
                        if(!nearEntities.getScoreboardTags().contains("afireparticle")) {
                            nearEntities.setFireTicks(nearEntities.getFireTicks() + 10);
                        }
                    }
                }
            }
        }, 0L, 1L);

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

    //ж


    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        Action action = event.getAction();

        if(action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
            if(player.getItemInHand().getItemMeta().getDisplayName().contains("flamethrower")) {
                temporarySummonFire(player);
                Bukkit.getScheduler().runTaskLater(this, () -> {
                    temporarySummonFire(player);
                    Bukkit.getScheduler().runTaskLater(this, () -> {
                        temporarySummonFire(player);
                    }, 8L);
                }, 8L);
            }
        }

        if(action == Action.RIGHT_CLICK_BLOCK) {
            if(event.getClickedBlock().getLocation().equals(new Location(player.getWorld(), -111, 9, -210))) {
                player.sendMessage("вы отправляетесь в бразилию (мопс пвп)");
                ByteArrayOutputStream b = new ByteArrayOutputStream();
                DataOutputStream out = new DataOutputStream(b);
                try {
                    out.writeUTF("Connect");
                    out.writeUTF("mopspvps");
                } catch (IOException eee) {
                    Bukkit.getLogger().info("You'll never see me!");
                }
                Bukkit.getPlayer(String.valueOf(player)).sendPluginMessage(this, "BungeeCord", b.toByteArray());
            }
        }

        if(!player.getScoreboardTags().contains("admin")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

//        for(EntityPlayer NPC : hubNPCs) {
//            PlayerConnection connection = ((CraftPlayer) player).getHandle().b;
//            connection.a(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a, NPC));
//            connection.a(new PacketPlayOutNamedEntitySpawn(NPC));
//            connection.a(new PacketPlayOutEntityHeadRotation(NPC, (byte) (NPC.getBukkitEntity().getLocation().getYaw() * 256 / 360)));
//        }
    }

    public void temporarySummonFire(Player player) {
        ArmorStand fireparticle = (ArmorStand) player.getWorld().spawnEntity(player.getEyeLocation(), EntityType.ARMOR_STAND);
        fireparticle.setMarker(true);
        fireparticle.setSmall(true);
        fireparticle.setInvisible(true);
        fireparticle.setInvulnerable(true);
        fireparticle.addScoreboardTag("afireparticle");

        Random random = new Random();
        double randomX = -0.02 + (0.02 - -0.02) * random.nextDouble();
        Random random2 = new Random();
        double randomY = -0.02 + (0.02 - -0.02) * random2.nextDouble();
        Random random3 = new Random();
        double randomZ = -0.02 + (0.02 - -0.02) * random3.nextDouble();

        fireparticle.setVelocity(player.getEyeLocation().getDirection().multiply(0.4).add(new Vector(0, 0.4, 0)).add(new Vector(randomX, randomY, randomZ)));

        Bukkit.getScheduler().runTaskLater(this, () -> {
            fireparticle.setGravity(false);
        }, 20L);

        Bukkit.getScheduler().runTaskLater(this, () -> {
            fireparticle.teleport(new Location(fireparticle.getWorld(), 0, 1000, 0));
        }, 40L);
    }
}
