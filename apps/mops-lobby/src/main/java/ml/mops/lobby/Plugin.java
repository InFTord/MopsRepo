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
import org.bukkit.block.Block;
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
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Plugin extends JavaPlugin implements Listener, CommandExecutor {

//    public List<EntityPlayer> hubNPCs = new ArrayList<>();
//
//    EntityPlayer woolbattleNPC1;

    HashMap<Player, Integer> pvpDogeDialogue = new HashMap<>();
    List<Location> flippable = new ArrayList<>();
    List<Location> atmButtons = new ArrayList<>();

    @Override
    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        World mainworld = Bukkit.getServer().getWorlds().get(0);

//        EntityPlayer woolbattleNPC = MopsUtils.createNPC(new Location(mainworld, -70.500, 7, -180.500), ChatColor.YELLOW + "" + ChatColor.BOLD + "WoolBattle", "SirCat07");
//        hubNPCs.add(woolbattleNPC);
//        woolbattleNPC1 = woolbattleNPC;

        flippable.add(new Location(mainworld, -102, 9, -195));
        flippable.add(new Location(mainworld, -95, 9, -195));
        flippable.add(new Location(mainworld, -85, 9, -196));
        flippable.add(new Location(mainworld, -85, 9, -192));
        flippable.add(new Location(mainworld, -60, 10, -192));
        flippable.add(new Location(mainworld, -60, 11, -192));
        flippable.add(new Location(mainworld, -58, 10, -197));
        flippable.add(new Location(mainworld, -64, 6, -251));
        flippable.add(new Location(mainworld, -64, 7, -251));
        flippable.add(new Location(mainworld, -72, 8, -233));
        flippable.add(new Location(mainworld, -71, 8, -233));
        flippable.add(new Location(mainworld, -72, 9, -233));
        flippable.add(new Location(mainworld, -71, 9, -233));
        flippable.add(new Location(mainworld, -81, 8, -202));

        atmButtons.add(new Location(mainworld, -69, 9, -205));
        atmButtons.add(new Location(mainworld, -92, 9, -176));

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for(Player player : Bukkit.getServer().getOnlinePlayers()) {
                Scoreboard lobbyscoreboard = new LobbyScoreboard().generateLobbyScoreboard(player);
                player.setScoreboard(lobbyscoreboard);
                
                Calendar calendar = Calendar.getInstance();
                if(calendar.get(Calendar.MONTH) == Calendar.DECEMBER || calendar.get(Calendar.MONTH) == Calendar.JANUARY) {
                    player.getWorld().spawnParticle(Particle.SNOWFLAKE, player.getLocation().add(0, 8, 0), 350, 15, 5, 15, 0);
                }

                Location block1 = new Location(mainworld, -78, 11, -208);
                Location block2 = new Location(mainworld, -78, 11, -208);

                //calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY
                if(true) {
                    block1.getBlock().setType(Material.AIR);
                    block2.getBlock().setType(Material.AIR);

                    FallingBlock ghostBlock1 = mainworld.spawnFallingBlock(block1, new MaterialData(Material.BLUE_CONCRETE));
                    ghostBlock1.setGravity(false);
                    ghostBlock1.addScoreboardTag("killOnDisable");
                    FallingBlock ghostBlock2 = mainworld.spawnFallingBlock(block2, new MaterialData(Material.BLUE_CONCRETE));
                    ghostBlock2.setGravity(false);
                    ghostBlock2.addScoreboardTag("killOnDisable");
                } else {
                    block1.getBlock().setType(Material.BLUE_CONCRETE);
                    block2.getBlock().setType(Material.BLUE_CONCRETE);
                }
            }
        }, 0L, 10L);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            Calendar calendar = Calendar.getInstance();
            Date date = calendar.getTime();

            long seconds = date.getSeconds() + (date.getMinutes() * 60L) + (date.getHours() * 3600L);
            long ticks = (long) (seconds * 0.2083333);

            mainworld.setTime(ticks + 21000);

            if(calendar.get(Calendar.MONTH) == Calendar.DECEMBER || calendar.get(Calendar.MONTH) == Calendar.JANUARY) {
                mainworld.setStorm(true);
            }
        }, 0L, 1200L);

//        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
//            for(Entity entity : Bukkit.getServer().getWorlds().get(0).getEntities()) {
//                if(entity.getScoreboardTags().contains("afireparticle")) {
//                    entity.getWorld().spawnParticle(Particle.FLAME, entity.getLocation(), 2, 0.01, 0.01, 0.01, 0.01);
//
//                    for(Entity nearEntities : entity.getNearbyEntities(2, 2, 2)) {
//                        if(!nearEntities.getScoreboardTags().contains("afireparticle")) {
//                            nearEntities.setFireTicks(nearEntities.getFireTicks() + 10);
//                        }
//                    }
//                }
//            }
//        }, 0L, 2L);

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

        Action action = event.getAction();



        if(action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {

//            if(player.getItemInHand().getItemMeta().getDisplayName().contains("flamethrower")) {
//                temporarySummonFire(player);
//                Bukkit.getScheduler().runTaskLater(this, () -> {
//                    temporarySummonFire(player);
//                    Bukkit.getScheduler().runTaskLater(this, () -> {
//                        temporarySummonFire(player);
//                    }, 2L);
//                }, 2L);
//            }
        }

        try {
            ItemStack itemInHand = player.getItemInHand();

            if(action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK)) {
                if (Objects.requireNonNull(itemInHand.getItemMeta()).getDisplayName().contains("katana")) {

                    List<Block> blocks = player.getLineOfSight(null, 7);
                    Entity entity = player.getNearbyEntities(5.4, 5.4, 5.4).get(0);

                    if (entity instanceof Damageable dmgEntity) {
                        boolean isInRange = false;
                        for(Block block : blocks) {
                            if(!isInRange) {
                                isInRange = block.getLocation().distance(dmgEntity.getLocation()) < 2;
                            }
                        }
                        if(isInRange) {
                            dmgEntity.damage(4);
                            dmgEntity.setVelocity(player.getEyeLocation().getDirection().multiply(0.5).add(new Vector(0, 0.2, 0)));
                        }
                    }
                }
            }

        } catch (NullPointerException ignored) {}

        if(action == Action.RIGHT_CLICK_BLOCK) {
            if(event.getClickedBlock().getLocation().equals(new Location(player.getWorld(), -111, 9, -210))) {
                player.sendMessage("вы отправляетесь в бразилию (мопс пвп)");
            }
            if(atmButtons.contains(event.getClickedBlock().getLocation())) {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
                player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_BREAK, 1, 2);

                player.getInventory().addItem(MopsUtils.addLore(MopsUtils.createItem(Material.GOLD_INGOT, ChatColor.GOLD + "MopsCoin", 1), new String[] {ChatColor.GRAY + "The main currency of MopsNetwork."}));
            }
        }

        if(!player.getScoreboardTags().contains("admin")) {
            if (!flippable.contains(event.getClickedBlock().getLocation())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityInteract(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        ArmorStand armorStand = (ArmorStand) event.getRightClicked();
        if(armorStand.getScoreboardTags().contains("armorStandHubNPC")) {
            event.setCancelled(true);
            if(armorStand.getScoreboardTags().contains("missionDogeNPC")) {
                String dialogue = "Hi, i can't currently give you missions.";
                MopsUtils.sendDialogueMessage(dialogue, player, armorStand);
                player.playSound(player.getLocation(), Sound.ENTITY_WOLF_GROWL, 10, 0);
                player.playSound(player.getLocation(), Sound.ENTITY_WOLF_AMBIENT, 10, 0);
            }
            if(armorStand.getScoreboardTags().contains("fishermanDogeNPC")) {
                String dialogue = "Giv me gfish please i want fis!!!1!!";
                MopsUtils.sendDialogueMessage(dialogue, player, armorStand);
                player.playSound(player.getLocation(), Sound.ENTITY_FISH_SWIM, 10, 2);
            }
            if(armorStand.getScoreboardTags().contains("builderDogeNPC")) {
                String dialogue = "hi this par t of hub not buildt please wait!!1";
                MopsUtils.sendDialogueMessage(dialogue, player, armorStand);
                player.playSound(player.getLocation(), Sound.ENTITY_WOLF_AMBIENT, 10, 2);
            }

            if(armorStand.getScoreboardTags().contains("pvpDogeNPC")) {
                if (pvpDogeDialogue.get(player) == 0) {
                    String dialogue = "There are no upgrades yet.";
                    MopsUtils.sendDialogueMessage(dialogue, player, armorStand);
                    player.playSound(player.getLocation(), Sound.ENTITY_WOLF_AMBIENT, 10, 2);

                    pvpDogeDialogue.put(player, pvpDogeDialogue.get(player) + 1);
                } else if (pvpDogeDialogue.get(player) == 1) {
                    String dialogue = "There is no PVP yet too.";
                    MopsUtils.sendDialogueMessage(dialogue, player, armorStand);
                    player.playSound(player.getLocation(), Sound.ENTITY_WOLF_AMBIENT, 10, 2);

                    pvpDogeDialogue.put(player, pvpDogeDialogue.get(player) + 1);
                } else if (pvpDogeDialogue.get(player) == 2) {
                    String dialogue = "I can give you a sword though, it looks cool.";
                    MopsUtils.sendDialogueMessage(dialogue, player, armorStand);
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 10, 0);
                    player.playSound(player.getLocation(), Sound.ENTITY_WOLF_AMBIENT, 10, 2);
                    player.getInventory().addItem(MopsUtils.createItem(Material.IRON_SWORD, ChatColor.GRAY + "Iron Sword"));

                    pvpDogeDialogue.put(player, pvpDogeDialogue.get(player) + 1);
                } else if (pvpDogeDialogue.get(player) == 3) {
                    String dialogue = "I don't have more swords.";
                    MopsUtils.sendDialogueMessage(dialogue, player, armorStand);
                    player.playSound(player.getLocation(), Sound.ENTITY_WOLF_AMBIENT, 10, 2);
                } else {
                    String dialogue = ChatColor.RED + "no dialogue found :p blehh (report to sircat)";
                    MopsUtils.sendDialogueMessage(dialogue, player, armorStand);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.setJoinMessage("");

        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1, 2);

        pvpDogeDialogue.putIfAbsent(player, 0);

        for(Player allPlayers : Bukkit.getOnlinePlayers()) {
            allPlayers.sendMessage( ChatColor.GOLD + "[MopsPVPs] " + ChatColor.YELLOW + player.getName() + " joined the game.");
        }


//        for(EntityPlayer NPC : hubNPCs) {
//            PlayerConnection connection = ((CraftPlayer) player).getHandle().b;
//            connection.a(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a, NPC));
//            connection.a(new PacketPlayOutNamedEntitySpawn(NPC));
//            connection.a(new PacketPlayOutEntityHeadRotation(NPC, (byte) (NPC.getBukkitEntity().getLocation().getYaw() * 256 / 360)));
//        }
    }

//    public void temporarySummonFire(Player player) {
//        ArmorStand fireparticle = (ArmorStand) player.getWorld().spawnEntity(player.getEyeLocation(), EntityType.ARMOR_STAND);
//        fireparticle.setSmall(true);
//        fireparticle.setInvisible(true);
//        fireparticle.setInvulnerable(true);
//        fireparticle.addScoreboardTag("afireparticle");
//
//        Random random = new Random();
//        double randomX = -0.005 + (0.01 - -0.005) * random.nextDouble();
//        Random random2 = new Random();
//        double randomZ = -0.005 + (0.01 - -0.005) * random2.nextDouble();
//
//        fireparticle.setVelocity(player.getEyeLocation().getDirection().multiply(0.4).add(new Vector(0, 0.4, 0)).add(new Vector(randomX, 0, randomZ)));
//
//        Bukkit.getScheduler().runTaskLater(this, () -> {
//            fireparticle.setGravity(false);
//            fireparticle.setMarker(true);
//        }, 20L);
//
//        Bukkit.getScheduler().runTaskLater(this, () -> {
//            fireparticle.teleport(new Location(fireparticle.getWorld(), 0, 1000, 0));
//        }, 40L);
//    }
}
