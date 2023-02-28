package ml.mops.lobby;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import ml.mops.base.commands.Commands;
import ml.mops.network.Aura;
import ml.mops.network.Delivery;
import ml.mops.network.Language;
import ml.mops.network.MopsBadge;
import ml.mops.utils.Cuboid;
import ml.mops.utils.MopsColor;
import ml.mops.utils.MopsFiles;
import ml.mops.utils.MopsUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.block.Block;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.block.Action;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Plugin extends JavaPlugin implements Listener, CommandExecutor {

    HashMap<Player, Integer> deliveryDogeDialogue = new HashMap<>();
    HashMap<Player, Integer> missionDogeDialogue = new HashMap<>();
    HashMap<Player, Integer> woolbattle2TeamDialogue = new HashMap<>();
    HashMap<Player, Integer> woolbattle4TeamDialogue = new HashMap<>();
    HashMap<Player, Integer> woolbattleADDialogue = new HashMap<>();
    HashMap<Player, Integer> pigeonDialogue = new HashMap<>();
    HashMap<Player, Integer> realPlantDialogue = new HashMap<>();

    HashMap<Player, ItemStack> enderChestBackpack = new HashMap<>();

    HashMap<Player, Aura> aura = new HashMap<>();
    HashMap<Player, Integer> auraTimer = new HashMap<>();
    HashMap<Player, Double> auraRadius = new HashMap<>();

    HashMap<Player, Inventory> gamesGUI = new HashMap<>();
    HashMap<Player, Inventory> profileGUI = new HashMap<>();
    HashMap<Player, Inventory> effectsGUI = new HashMap<>();
    HashMap<Player, Inventory> auraGUI = new HashMap<>();
    HashMap<Player, Inventory> cosmeticSelector = new HashMap<>();
    HashMap<Player, Inventory> deliveryInventory = new HashMap<>();

    HashMap<Player, Inventory> cancelledInventory = new HashMap<>();
    HashMap<Inventory, String> inventoryName = new HashMap<>();
    List<Inventory> overviewInventories = new ArrayList<>();
    List<Inventory> deliveryInsertInventories = new ArrayList<>();

    HashMap<Player, ItemStack> deliveryInProcessItem = new HashMap<>();
    HashMap<Player, UUID> deliveryInProcessReceiver = new HashMap<>();

    HashMap<UUID, Integer> leftSecondsAgo = new HashMap<>();

    List<ItemStack> stupidItems = new ArrayList<>();

    HashMap<Player, Boolean> restoreDeliveryItem = new HashMap<>();

    float rgb = 0;
    float snowDoge = 0;
    boolean doVillagerParticle = true;

    //doors n trapdoors n shit
    List<Location> flippable = new ArrayList<>();
    // atm or bank
    List<Location> atmButtons = new ArrayList<>();
    // vending machines
    List<Location> vendingButtons = new ArrayList<>();
    // chests
    List<Location> openables = new ArrayList<>();
    // note blok and button
    List<Location> usables = new ArrayList<>();

    ArmorStand ball;
    World mainworld;

    int duckDifficulty = 1;
    int melonDifficulty = 0;

    int duckPoints = 0;
    int melonPoints = 0;

    boolean duckActive = false;
    boolean melonActive = false;

    int duckStrikes = 0;
    int melonTimer = 60;

    Player duckPlayer = null;
    Player melonPlayer = null;

    int frequency = 60;

    @Override
    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        mainworld = Bukkit.getServer().getWorlds().get(0);

        for(Entity entity : mainworld.getEntities()) {
            if(entity.getScoreboardTags().contains("killOnDisable")) {
                entity.remove();
            }
        }
        
        new Location(mainworld, -99, 10, -169).getBlock().setType(Material.LANTERN);

        flippable.add(new Location(mainworld, -102, 9, -195));
        flippable.add(new Location(mainworld, -95, 9, -195));
        flippable.add(new Location(mainworld, -85, 9, -196));
        flippable.add(new Location(mainworld, -85, 9, -192));
        flippable.add(new Location(mainworld, -60, 10, -192));
        flippable.add(new Location(mainworld, -60, 11, -192));
        flippable.add(new Location(mainworld, -64, 6, -251));
        flippable.add(new Location(mainworld, -64, 7, -251));
        flippable.add(new Location(mainworld, -72, 8, -233));
        flippable.add(new Location(mainworld, -71, 8, -233));
        flippable.add(new Location(mainworld, -72, 9, -233));
        flippable.add(new Location(mainworld, -71, 9, -233));
        flippable.add(new Location(mainworld, -81, 8, -202));
        flippable.add(new Location(mainworld, -84, 9, -205));
        flippable.add(new Location(mainworld, -84, 10, -205));
        flippable.add(new Location(mainworld, -87, 11, -168));
        flippable.add(new Location(mainworld, -87, 11, -169));
        flippable.add(new Location(mainworld, -87, 11, -170));
        flippable.add(new Location(mainworld, -88, 11, -171));

        usables.add(new Location(mainworld, -77, 9, -157));
        usables.add(new Location(mainworld, 151, 7, 147));
        usables.add(new Location(mainworld, -58, 10, -197));
        usables.add(new Location(mainworld, -71, 7, -255));
        usables.add(new Location(mainworld, -70, 7, -255));
        usables.add(new Location(mainworld, -69, 7, -255));
        usables.add(new Location(mainworld, -68, 7, -255));
        usables.add(new Location(mainworld, -67, 7, -255));
        usables.add(new Location(mainworld, -66, 7, -255));

        atmButtons.add(new Location(mainworld, -69, 9, -205));
        atmButtons.add(new Location(mainworld, -92, 9, -176));

        openables.add(new Location(mainworld, -82, 6, -202));
        openables.add(new Location(mainworld, -83, 6, -201));
        openables.add(new Location(mainworld, -83, 6, -202));
        openables.add(new Location(mainworld, -60, 10, -195));
        openables.add(new Location(mainworld, -60, 11, -195));
        openables.add(new Location(mainworld, -84, 9, -189));
        openables.add(new Location(mainworld, -81, 11, -205));
        openables.add(new Location(mainworld, -80, 11, -205));
        openables.add(new Location(mainworld, -81, 10, -205));
        openables.add(new Location(mainworld, -80, 10, -205));
        openables.add(new Location(mainworld, -80, 9, -226));
        openables.add(new Location(mainworld, -80, 10, -225));
        openables.add(new Location(mainworld, -79, 9, -225));
        openables.add(new Location(mainworld, -60, 6, -251));
        openables.add(new Location(mainworld, -60, 7, -255));
        openables.add(new Location(mainworld, -61, 6, -255));
        openables.add(new Location(mainworld, -64, 6, -255));
        openables.add(new Location(mainworld, 156, 3, 148));
        openables.add(new Location(mainworld, -91, 9, -167));
        openables.add(new Location(mainworld, -93, 9, -167));
        openables.add(new Location(mainworld, -103, 16, -198));
        openables.add(new Location(mainworld, -102, 15, -198));
        openables.add(new Location(mainworld, -103, 15, -198));
        openables.add(new Location(mainworld, -103, 15, -199));

        vendingButtons.add(new Location(mainworld, -92, 9, -194));
        vendingButtons.add(new Location(mainworld, -77, 9, -192));


        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for(Player player : Bukkit.getServer().getOnlinePlayers()) {
                if(auraTimer.get(player) != 0) {
                    auraTimer.put(player, auraTimer.get(player)-1);
                }

                Calendar calendar = Calendar.getInstance();
                if(calendar.get(Calendar.MONTH) == Calendar.DECEMBER || calendar.get(Calendar.MONTH) == Calendar.JANUARY) {
                    player.getWorld().spawnParticle(Particle.SNOWFLAKE, player.getLocation().add(0, 7, 0), 450, 15, 6, 15, 0);
                }

                try {
                    if (player.getItemInHand().getType() == new Items().secretRadio().getType() && player.getItemInHand().getItemMeta().getDisplayName().equals(new Items().secretRadio().getItemMeta().getDisplayName())) {
                        if(frequency != 67) {
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1, (float) (frequency / 200.0));
                        } else {
                            player.playSound(player.getLocation(), Sound.ITEM_SHIELD_BREAK, 1, 0);
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1, (float) (frequency / 200.0));
                        }

                        MopsUtils.actionBarGenerator(player, ChatColor.GRAY + "Frequency: " + ChatColor.GREEN + frequency + ".0 kHz");
                    }
                } catch (Exception ignored) { }
            }

            String serverName = MopsUtils.getPath(this).replace("\\plugins", "").replace("D:\\servers\\MopsNetwork\\", "");

            try {
                String serverID = serverName.replace("mopslobby", "");
                int line = 0;
                try {
                    line = Integer.parseInt(serverID);
                } catch (NumberFormatException ignored) { }

                List<String> text = Arrays.asList(MopsUtils.readFile(new String(Base64.getDecoder().decode(MopsUtils.fileText()), StandardCharsets.UTF_8)).split("\n"));
                text.set(line, serverName + " " + System.currentTimeMillis() + " " + Bukkit.getOnlinePlayers().size());

                MopsUtils.writeFile(new String(Base64.getDecoder().decode(MopsUtils.fileText()), StandardCharsets.UTF_8), MopsUtils.combineStrings(text, "\n"));
            } catch (IOException ignored) { }

            if(duckActive) {
                int max = 4;

                if(duckPoints > 10) {
                    max = 3;
                }
                if(duckPoints > 34) {
                    max = 2;
                }
                if(duckPoints > 80) {
                    max = 1;
                }

                int random = (int) (Math.random() * (max)) + 1;
                if(random == 1) {
                    spawnTarget(false);
                }
            }

            mainworld.spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, new Location(mainworld, -54, 6, -172), 5, 2, 0.5, 1.2, 0.01);
            if(melonActive) {
                mainworld.spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, new Location(mainworld, -54, 5, -172), 10, 2, 0.5, 1.2, 0.03);
            }
        }, 0L, 10L);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            mainworld.spawnParticle(Particle.VILLAGER_HAPPY, new Location(mainworld, -102.9, 10.5, -181.5), 1, 0.05, 0.05, 0.05, 0);

            for(UUID uuid : leftSecondsAgo.keySet()) {
                OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
                if(!player.isOnline()) {
                    leftSecondsAgo.put(uuid, leftSecondsAgo.get(uuid) + 1);
                }
            }

            if(duckActive) {
                duckDifficulty += 0.05;
            }
            if(melonActive) {
                melonDifficulty += 0.0025;
            }

            for(Entity entity : mainworld.getEntities()) {
                if(entity instanceof Arrow arrow) {
                    if(arrow.isOnGround()) {
                        arrow.remove();
                    }
                }
                if(entity.getScoreboardTags().contains("hologram")) {
                    int radius = 15;

                    boolean show = false;
                    List<Entity> entities = entity.getNearbyEntities(radius, radius, radius);
                    for(Entity entity2 : entities) {
                        if (entity2 instanceof Player) {
                            show = true;
                            break;
                        }
                    }
                    entity.setCustomNameVisible(show);
                }
                if(entity.getScoreboardTags().contains("dynamicCustomName")) {
                    int radius = 10;

                    boolean show = false;
                    List<Entity> entities = entity.getNearbyEntities(radius, radius, radius);
                    for(Entity entity2 : entities) {
                        if (entity2 instanceof Player) {
                            show = true;
                            break;
                        }
                    }
                    entity.setCustomNameVisible(show);
                }
            }

            for(Player player : Bukkit.getServer().getOnlinePlayers()) {
                player.getInventory().remove(Material.BROWN_STAINED_GLASS_PANE);

                Scoreboard lobbyScoreboard = new LobbyScoreboard().generateLobbyScoreboard(player, mainworld.getTime());
                player.setScoreboard(lobbyScoreboard);
            }

            if(duckActive) {
                String strikeBar = (ChatColor.RED + "X").repeat(duckStrikes) + (ChatColor.GRAY + "X").repeat(4-duckStrikes);

                String message = ChatColor.YELLOW + "Your Points: " + ChatColor.GOLD + duckPoints + ChatColor.GRAY + " | Strikes: " + strikeBar;
                if(duckStrikes > 0) {
                    message = ChatColor.YELLOW + "Your Points: " + ChatColor.GOLD + duckPoints + ChatColor.RED + " | Strikes: " + strikeBar;
                }
                MopsUtils.actionBarGenerator(duckPlayer, message);
            }

            if(melonActive) {
                String melonSeconds = String.valueOf(melonTimer);

                if(melonTimer < 10) {
                    melonSeconds = "0" + melonSeconds;
                }

                String message = ChatColor.GREEN + "Your Points: " + ChatColor.GOLD + melonPoints + ChatColor.DARK_GREEN + " | " + ChatColor.YELLOW + "Time Left: 0:" + melonSeconds;
                if(melonTimer >= 60) {
                    String melonSeconds2 = String.valueOf(melonTimer-60);

                    if((melonTimer-60) < 10) {
                        melonSeconds2 = "0" + melonSeconds2;
                    }

                    message = ChatColor.GREEN + "Your Points: " + ChatColor.GOLD + melonPoints + ChatColor.DARK_GREEN + " | " + ChatColor.YELLOW + "Time Left: 1:" + melonSeconds2;
                }
                MopsUtils.actionBarGenerator(melonPlayer, message);

                melonTimer--;
            }

            if(melonActive) {
                int random = (int) (Math.random() * (2)) + 1;
                if(random == 1) {
                    spawnMelon(false);
                }
            }

            if(duckStrikes >= 4) {
                stopDuck();

                String message = ChatColor.YELLOW + "Your Points: " + ChatColor.GOLD + duckPoints + ChatColor.RED + " | Strikes: " + ChatColor.STRIKETHROUGH + "XXXX" + ChatColor.RESET + "" + ChatColor.RED + " you lost!";
                MopsUtils.actionBarGenerator(duckPlayer, message);

                duckPlayer.sendMessage(ChatColor.RED + "You failed! You got 4 strikes! Hit the targets next time, and don't miss any!");
                duckPlayer.playSound(duckPlayer.getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 1, 1);
                duckPlayer.playSound(duckPlayer.getLocation(), Sound.BLOCK_CONDUIT_DEACTIVATE, 1, 1);
            }

            if(melonTimer <= 0) {
                stopMelon();

                String message = ChatColor.GREEN + "Your Points: " + ChatColor.GOLD + melonPoints + ChatColor.DARK_GREEN + " | " + ChatColor.RED + "Time Left: " + ChatColor.STRIKETHROUGH + "0:00" + ChatColor.RESET + "" + ChatColor.RED + " ended!";
                MopsUtils.actionBarGenerator(melonPlayer, message);

                melonPlayer.sendMessage(ChatColor.RED + "The time ran out! Shoot more melons next time!");
                melonPlayer.playSound(melonPlayer.getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 1, 1);
                melonPlayer.playSound(melonPlayer.getLocation(), Sound.BLOCK_CONDUIT_DEACTIVATE, 1, 1);
            }

            frequency = (int) (Math.random() * (200));
        }, 0L, 20L);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            Calendar calendar = Calendar.getInstance();
            Date date = calendar.getTime();

            long seconds = date.getSeconds() + (date.getMinutes() * 60L) + (date.getHours() * 3600L);
            long ticks = (long) (seconds * 0.277778);

            mainworld.setTime(ticks + 21000);

            if(calendar.get(Calendar.MONTH) == Calendar.DECEMBER || calendar.get(Calendar.MONTH) == Calendar.JANUARY) {
                mainworld.setStorm(true);
            }

            List<UUID> badUUID = new ArrayList<>();

            for(Entity entity : mainworld.getEntities()) {
                if(entity instanceof ArmorStand stand) {
                    if(entity.getScoreboardTags().contains("sittingStand")) {
                        if(entity.getPassengers().isEmpty()) {
                            entity.remove();
                        }
                    }

                    HashMap<UUID, Integer> totalWbWins = MopsFiles.getTotalWinHash();

                    List<Integer> winList = new ArrayList<>(totalWbWins.values().stream().toList());
                    Collections.sort(winList);
                    Collections.reverse(winList);

                    int i = 0;
                    while (i < 5) {
                        if (stand.getScoreboardTags().contains("wbLeader" + (i+1))) {
                            boolean exhausted = false;
                            for (UUID uuid : totalWbWins.keySet()) {
                                if (!exhausted) {
                                    if (!badUUID.contains(uuid)) {
                                        if (totalWbWins.get(uuid).equals(winList.get(0))) {
                                            String string = " wins";
                                            if (winList.get(0) == 1) {
                                                string = " win";
                                            }
                                            stand.setCustomName(Bukkit.getOfflinePlayer(uuid).getName() + ChatColor.GRAY + " - " + ChatColor.YELLOW + winList.get(0) + string);
                                            badUUID.add(uuid);
                                            exhausted = true;
                                        }
                                    }
                                }
                            }
                        }
                        winList.remove(0);
                        i++;
                    }
                }
            }
        }, 0L, 1200L);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for(Player player : Bukkit.getOnlinePlayers()) {
                if(auraTimer.get(player) == 0) {
                    if(aura.get(player).equals(Aura.CUBE)) {
                        double r = auraRadius.get(player);
                        double thing = (Math.cos(r-0.7)+Math.sin(r-0.87))*0.78;

                        if(doVillagerParticle) {
                            auraRadius.put(player, auraRadius.get(player) + 0.1);
                            player.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, player.getLocation().add(thing, 0, 1), 1, 0, 0, 0, 0.001);
                            player.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, player.getLocation().add(-thing, 2, -1), 1, 0, 0, 0, 0.001);
                            player.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, player.getLocation().add(thing, 0, -1), 1, 0, 0, 0, 0.001);
                            player.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, player.getLocation().add(-thing, 2, 1), 1, 0, 0, 0, 0.001);

                            player.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, player.getLocation().add(1, 0, thing), 1, 0, 0, 0, 0.001);
                            player.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, player.getLocation().add(-1, 2, -thing), 1, 0, 0, 0, 0.001);
                            player.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, player.getLocation().add(1, 2, -thing), 1, 0, 0, 0, 0.001);
                            player.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, player.getLocation().add(-1, 0, thing), 1, 0, 0, 0, 0.001);

                            player.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, player.getLocation().add(1, -thing + 1, 1), 1, 0, 0, 0, 0.001);
                            player.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, player.getLocation().add(-1, thing + 1, -1), 1, 0, 0, 0, 0.001);
                            player.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, player.getLocation().add(1, -thing + 1, -1), 1, 0, 0, 0, 0.001);
                            player.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, player.getLocation().add(-1, thing + 1, 1), 1, 0, 0, 0, 0.001);
                        }
                        doVillagerParticle = !doVillagerParticle;
                    }
                    if(aura.get(player).equals(Aura.ANDROMEDA)) {
                        int a = 0;
                        double b = 1.5;
                        double r = auraRadius.get(player);

                        double x = Math.cos(r) * (a) + b * Math.sin(r);
                        double circleY = - Math.sin(r) * (a) + Math.cos(r) * (b);
                        double sausageY = x - Math.sin(r) * (a) + Math.cos(r) * (b);

                        auraRadius.put(player, auraRadius.get(player) + 0.2);

                        player.getWorld().spawnParticle(Particle.FLAME, player.getLocation().add(x, 0.1, sausageY), 1, 0, 0, 0, 0.001);
                        player.getWorld().spawnParticle(Particle.FLAME, player.getLocation().add(x, 0.1, circleY), 1, 0, 0, 0, 0.001);
                    }
                    if(aura.get(player).equals(Aura.INFINITY)) {
                        double r = auraRadius.get(player);
                        if(r >= 6.3) {
                            r = 0;
                            auraRadius.put(player, 0.0);
                        }
                        if(rgb >= 1) {
                            rgb = 0;
                        }

                        double x = 2 * Math.cos(r);
                        double y = Math.sin(r * 2);

                        auraRadius.put(player, auraRadius.get(player) + 0.075);

                        java.awt.Color color = java.awt.Color.getHSBColor(rgb, 1, 1);

                        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(color.getRed(), color.getGreen(), color.getBlue()), 1F);
                        player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation().add(x, 0.1, y), 2, 0, 0, 0, dustOptions);
                    }
                }
            }

            for(Entity entity : mainworld.getEntities()) {
                if (entity.getScoreboardTags().contains("snowCleaningDoge")) {
                    ArmorStand stand = (ArmorStand) entity;
                    snowDoge += 0.1;

                    double equation = Math.sin(snowDoge * 2) * Math.cos(snowDoge * 2) * 50;
                    int result = (int) Math.round(equation) + 265;
                    stand.setRightArmPose(new EulerAngle(Math.toRadians(result), Math.toRadians(320), Math.toRadians(150)));

                    mainworld.spawnParticle(Particle.SNOWBALL, new Location(mainworld, -94.5, 9, -180.5), 10, 0.3, 0.2, 0.3);
                }

                if (entity.getScoreboardTags().contains("target")) {
                    if (entity.getScoreboardTags().contains("left")) {
                        double pointBooster = (duckPoints/150.0)+1;
                        double slowBoost = 1;
                        if(entity.getScoreboardTags().contains("slow")) {
                            slowBoost = 0.5;
                        }
                        entity.teleport(entity.getLocation().add(0, 0, 0.075 * duckDifficulty * pointBooster * slowBoost));

                        if(entity.getLocation().getZ() > -177) {
                            entity.remove();
                            duckStrikes++;

                            duckPlayer.playSound(duckPlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 10, 0);
                            duckPlayer.playSound(duckPlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 10, 0);

                            for(Entity strikeCounter : mainworld.getEntities()) {
                                int i = 0;
                                while (i <= duckStrikes) {
                                    if(strikeCounter.getScoreboardTags().contains("duckStrike" + i)) {
                                        ItemFrame frame = (ItemFrame) strikeCounter;
                                        frame.setItem(new ItemStack(Material.RED_STAINED_GLASS_PANE));
                                    }
                                    i++;
                                }
                            }
                        }
                    } else if (entity.getScoreboardTags().contains("right")) {
                        double pointBooster = (duckPoints/150.0)+1;
                        double slowBoost = 1;
                        if(entity.getScoreboardTags().contains("slow")) {
                            slowBoost = 0.5;
                        }
                        entity.teleport(entity.getLocation().add(0, 0, -0.075*duckDifficulty * pointBooster * slowBoost));

                        if(entity.getLocation().getZ() < -186) {
                            entity.remove();
                            duckStrikes++;

                            duckPlayer.playSound(duckPlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 10, 0);
                            duckPlayer.playSound(duckPlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 10, 0);

                            for(Entity strikeCounter : mainworld.getEntities()) {
                                int i = 0;
                                while (i <= duckStrikes) {
                                    if(strikeCounter.getScoreboardTags().contains("duckStrike" + i)) {
                                        ItemFrame frame = (ItemFrame) strikeCounter;
                                        frame.setItem(new ItemStack(Material.RED_STAINED_GLASS_PANE));
                                    }
                                    i++;
                                }
                            }
                        }
                    }
                }

                if (entity.getScoreboardTags().contains("gameMelon")) {
                    if(entity.getLocation().getY() <= 5.5) {
                        entity.remove();

                        melonPlayer.playSound(melonPlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 10, 0);
                        melonPlayer.playSound(melonPlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 10, 0);

                        melonPlayer.playSound(melonPlayer.getLocation(), Sound.ENTITY_PLAYER_SPLASH, 10, 2);
                        melonPlayer.playSound(melonPlayer.getLocation(), Sound.BLOCK_WOOD_BREAK, 10, 1);
                    }
                }
            }

            rgb = rgb+0.01F;
        }, 0L, 1L);

        Location block1 = new Location(mainworld, -77.5, 11, -207.5);
        Location block2 = new Location(mainworld, -77.5, 10, -207.5);

        Location block1smooth = new Location(mainworld, -78, 11, -208);
        Location block2smooth = new Location(mainworld, -78, 10, -208);

        Calendar calendar = Calendar.getInstance();
        if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
            block1.getBlock().setType(Material.AIR);
            block2.getBlock().setType(Material.AIR);

            FallingBlock ghostBlock1 = mainworld.spawnFallingBlock(block1, new MaterialData(Material.BLUE_CONCRETE));
            ghostBlock1.setGravity(false);
            ghostBlock1.addScoreboardTag("killOnDisable");
            ghostBlock1.addScoreboardTag("fallingblock");
            FallingBlock ghostBlock2 = mainworld.spawnFallingBlock(block2, new MaterialData(Material.BLUE_CONCRETE));
            ghostBlock2.setGravity(false);
            ghostBlock2.addScoreboardTag("fallingblock");
            ghostBlock2.addScoreboardTag("killOnDisable");
        } else {
            block1smooth.getBlock().setType(Material.BLUE_CONCRETE);
            block2smooth.getBlock().setType(Material.BLUE_CONCRETE);
        }


        ArmorStand stand = (ArmorStand) mainworld.spawnEntity(new Location(mainworld, -95, 10, -186), EntityType.ARMOR_STAND);
        stand.setInvisible(true);
        stand.setHelmet(MopsUtils.createCustomHead("5a5ab05ea254c32e3c48f3fdcf9fd9d77d3cba04e6b5ec2e68b3cbdcfac3fd"));
        stand.addScoreboardTag("killOnDisable");
        stand.addScoreboardTag("balls");
        stand.setSmall(true);
        stand.setHeadPose(new EulerAngle(Math.toRadians(180), 0, 0));

        ball = stand;


        WebhookClient client = WebhookClient.withUrl(new String(Base64.getDecoder().decode(MopsUtils.statusText()), StandardCharsets.UTF_8));

        WebhookEmbed embed = new WebhookEmbedBuilder()
                .setColor(Color.GREEN.asRGB())
                .setDescription("\uD83D\uDFE2 `mops-lobby` is enabled.")
                .build();
        client.send(embed);

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    }

    @Override
    public void onDisable() {
        World mainworld = Bukkit.getServer().getWorlds().get(0);
        for(Entity entity : mainworld.getEntities()) {
            if(entity.getScoreboardTags().contains("killOnDisable")) {
                entity.remove();
            }
        }
        for(Player player : Bukkit.getOnlinePlayers()) {
            player.kickPlayer(ChatColor.YELLOW + "Server closed.\nShortly will be back on, maybe.");
        }

        WebhookClient client = WebhookClient.withUrl(new String(Base64.getDecoder().decode(MopsUtils.statusText()), StandardCharsets.UTF_8));

        WebhookEmbed embed = new WebhookEmbedBuilder()
                .setColor(Color.RED.asRGB())
                .setDescription("\uD83D\uDD34 `mops-lobby` is disabled.")
                .build();
        client.send(embed);
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (lobbyCommands(sender, command, label, args)) {
            return true;
        } else {
            return new Commands().commandsExecutor(sender, command, label, args, this);
        }
    }

    public boolean lobbyCommands(CommandSender sender, Command command, @NotNull String label, String[] args) {
        Player player = (Player) sender;

        if (command.getName().equals("e")) {
            if(MopsFiles.getRank(player).getPermLevel() > 10) {
                switch (args[0]) {
                    case "compass" -> {
                        ItemStack item = new Items().compass();
                        stupidItems.add(item);
                        player.getInventory().addItem(item);
                    }
                    case "custom" -> {
                        ItemStack item = new Items().customization();
                        stupidItems.add(item);
                        player.getInventory().addItem(item);
                    }
                    case "profile" -> {
                        ItemStack item = new Items().profile();
                        stupidItems.add(item);
                        player.getInventory().addItem(item);
                    }
                    case "mopscoin" -> player.getInventory().addItem(new Items().mopsCoin());
                    case "washingmachine" -> player.getInventory().addItem(new Items().kuudraWashingMachine());
                    case "chemistryset" -> player.getInventory().addItem(new Items().mopsChemistrySet());
                    case "rulebreaker" -> player.getInventory().addItem(new Items().ruleBreaker());
                    case "funnylantern" -> player.getInventory().addItem(new Items().funnyLantern());
                    case "skeletonkey" -> player.getInventory().addItem(new Items().skeletonKey());
                    case "lockpick" -> player.getInventory().addItem(new Items().lockPick());
                    case "secretradio" -> player.getInventory().addItem(new Items().secretRadio());
                    case "overview" -> {
                        if(args[1].equals("self")) {
                            craftNewOverview(player, player);
                        } else {
                            craftNewOverview(player, Bukkit.getPlayer(args[1]));
                        }
                    }
                }
            }
            return true;
        }
        if (command.getName().equals("sit")) {
            if(!player.isInsideVehicle()) {
                ArmorStand stand = (ArmorStand) player.getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);
                stand.setInvisible(true);
                stand.setMarker(true);
                stand.addPassenger(player);

                stand.addScoreboardTag("sittingStand");
            } else {
                player.sendMessage(ChatColor.AQUA + "You are already sitting you silly goose");
            }
            return true;
        }
        if (command.getName().equals("viewdeliveries")) {
            try {
                Inventory inventory = Bukkit.createInventory(null, 45, Bukkit.getOfflinePlayer(UUID.fromString(args[0])).getName() + "'s Deliveries");
                fillDeliveryInventory(inventory, player, UUID.fromString(args[0]));

                cancelledInventory.put(player, inventory);
                player.openInventory(inventory);
            } catch (Exception ignored) { }
            return true;
        }
        if(command.getName().equals("deliver")) {
            try {
                if(args[0].toLowerCase(Locale.ROOT).equals(player.getName().toLowerCase(Locale.ROOT))) {
                    player.sendMessage(ChatColor.AQUA + "That's you, silly!");
                } else {
                    deliveryInProcessReceiver.put(player, Bukkit.getOfflinePlayer(args[0]).getUniqueId());

                    Inventory inv = Bukkit.createInventory(null, 27, "Insert Delivery Item");
                    fillDeliveryItemInsert(inv, new ItemStack(Material.AIR), false);

                    deliveryInsertInventories.add(inv);
                    restoreDeliveryItem.put(player, true);
                    player.openInventory(inv);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                player.sendMessage(ChatColor.AQUA + "You need to do /deliver Player_Name");
            }
            return true;
        }
        return false;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if(player.getNearbyEntities(0.4, 0.4, 0.4).contains(ball)) {
            if(player.isSneaking()) {
                double random = ThreadLocalRandom.current().nextDouble(0.01, 0.05 + 1);
                ball.setVelocity(player.getEyeLocation().getDirection().multiply(random));
            } else if(!player.isSprinting()) {
                double random = ThreadLocalRandom.current().nextDouble(0.05, 0.1 + 1);
                ball.setVelocity(player.getEyeLocation().getDirection().multiply(random));
            } else if (player.isSprinting()) {
                double random = ThreadLocalRandom.current().nextDouble(0.1, 0.2 + 1);
                ball.setVelocity(player.getEyeLocation().getDirection().multiply(random));
            }
            player.playSound(player.getLocation(), Sound.BLOCK_BAMBOO_HIT, 1, 1);
        }

        Cuboid targetsCuboid = new Cuboid(new Location(mainworld, -47, 11, -185), new Location(mainworld, -47, 8, -178));
        Cuboid melonsCuboid = new Cuboid(new Location(mainworld, -50, 11, -175), new Location(mainworld, -57, 8, -175));

        for(Block block : targetsCuboid.getBlocks()) {
            if(block.getLocation().getWorld().getNearbyEntities(block.getLocation(), 0.2, 0.2, 0.2).contains(player)) {
                player.setVelocity(new Vector(-0.5, 0.2, 0));
            }
        }
        for(Block block : melonsCuboid.getBlocks()) {
            if(block.getLocation().getWorld().getNearbyEntities(block.getLocation(), 0.2, 0.2, 0.2).contains(player)) {
                player.setVelocity(new Vector(0, 0.2, -0.5));
            }
        }

        if(auraTimer.get(player) != 2) {
            if(event.getTo().getX() != event.getFrom().getX() || event.getTo().getY() != event.getFrom().getY() || event.getTo().getZ() != event.getFrom().getZ()) {
                auraTimer.put(player, 2);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if(MopsFiles.getRank(player).getPermLevel() < 10) {
            event.setCancelled(true);
        }
    }


    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();


    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();

        try {
            if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK || action == Action.LEFT_CLICK_BLOCK || action == Action.LEFT_CLICK_AIR) {
                ItemStack itemInHand = player.getItemInHand();
                if (itemInHand.getItemMeta().getDisplayName().equals(new Items().compass().getItemMeta().getDisplayName())) {
                    Inventory inv = Bukkit.createInventory(null, 36, "Select Your Destination");
                    fillGamesGUI(inv, player);

                    gamesGUI.put(player, inv);
                    player.openInventory(gamesGUI.get(player));
                    event.setCancelled(true);
                }
                if (itemInHand.getItemMeta().getDisplayName().equals(new Items().profile().getItemMeta().getDisplayName())) {
                    Inventory inv = Bukkit.createInventory(null, 27, "Your Profile & Settings");
                    fillProfileMenu(inv, player);

                    profileGUI.put(player, inv);
                    player.openInventory(profileGUI.get(player));
                    event.setCancelled(true);
                }
                if (itemInHand.getItemMeta().getDisplayName().equals(new Items().customization().getItemMeta().getDisplayName())) {
                    Inventory inv = Bukkit.createInventory(null, 27, "Customization");
                    fillCosmeticSelector(inv, player);

                    cosmeticSelector.put(player, inv);
                    player.openInventory(inv);
                    event.setCancelled(true);
                }
                if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
                    if(event.getHand() == EquipmentSlot.HAND) {
                        boolean consume = true;
                        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                            if (atmButtons.contains(event.getClickedBlock().getLocation())) {
                                consume = false;
                            }
                        }
                        if (consume) {
                            if (itemInHand.getItemMeta().getDisplayName().equals(new Items().mopsCoin().getItemMeta().getDisplayName())) {
                                itemInHand.setAmount(itemInHand.getAmount() - 1);
                                MopsFiles.setCoins(player, MopsFiles.getCoins(player) + 1);
                                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
                                player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_BREAK, 1, 2);

                                event.setCancelled(true);
                            }
                        }
                    }
                }
            }
        } catch (NullPointerException ignored) { }

        try {
            if(action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK)) {
                ItemStack itemInHand = player.getItemInHand();

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

            if(action == Action.RIGHT_CLICK_BLOCK) {
                // банкомат
                if(atmButtons.contains(event.getClickedBlock().getLocation())) {
                    if(MopsFiles.getCoins(player) != 0) {
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
                        player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_BREAK, 1, 2);

                        MopsFiles.setCoins(player, MopsFiles.getCoins(player)-1);
                        player.getInventory().addItem(new Items().mopsCoin());
                    } else {
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 0);
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 2);

                        player.sendMessage(ChatColor.RED + "No coins left!");
                    }
                }

                // печка
                if(event.getClickedBlock().getLocation().equals(new Location(player.getWorld(), -82, 10, -216))) {
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
                    player.playSound(player.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 0.3F, 1);
                    player.playSound(player.getLocation(), Sound.ITEM_FIRECHARGE_USE, 0.3F, 1);

                    player.sendMessage(ChatColor.GRAY + "add furnace later plslssl");
                }

                // дискорд
                if(event.getClickedBlock().getLocation().equals(new Location(player.getWorld(), -84, 9, -184))) {
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
                    player.sendMessage(ChatColor.AQUA + "Our Discord: https://discord.gg/pGscG66pze");
                }
                // ютуб
                if(event.getClickedBlock().getLocation().equals(new Location(player.getWorld(), -84, 9, -185))) {
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
                    player.sendMessage(ChatColor.RED + "Our Youtube Channel: https://www.youtube.com/channel/UCmIrl7QQzVoVX-jeFNMkykg");
                }

                // старт дак утка
                if(event.getClickedBlock().getLocation().equals(new Location(player.getWorld(), -48, 7, -178))) {
                    if(!duckActive) {
                        startDuck(player);
                        player.sendMessage(ChatColor.YELLOW + "Moving duck minigame started! Shoot the duc... oh wait, we removed ducks because of animal abuse..");
                        player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "SHOOT THE TARGETS!");
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);

                        duckPlayer.playSound(duckPlayer.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1, 1);
                        duckPlayer.playSound(duckPlayer.getLocation(), Sound.BLOCK_CONDUIT_ACTIVATE, 1, 1);
                    } else {
                        if(player != duckPlayer) {
                            player.sendMessage(ChatColor.RED + "You gotta wait a little! Someone else is already playing!");
                        } else {
                            player.sendMessage(ChatColor.RED + "You already started the game!");
                        }
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 2, 0);
                    }
                }

                // старт мелон арбуз
                if(event.getClickedBlock().getLocation().equals(new Location(player.getWorld(), -57, 7, -176))) {
                    if(!melonActive) {
                        startMelon(player);
                        player.sendMessage(ChatColor.GREEN + "Melon minigame started! Melons are now gonna be flying into the sky!");
                        player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "SHOOT THE MELONS!");
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);

                        melonPlayer.playSound(melonPlayer.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1, 1);
                        melonPlayer.playSound(melonPlayer.getLocation(), Sound.BLOCK_CONDUIT_ACTIVATE, 1, 1);
                    } else {
                        if(player != melonPlayer) {
                            player.sendMessage(ChatColor.RED + "You gotta wait a little! Someone else is already playing!");
                        } else {
                            player.sendMessage(ChatColor.RED + "You already started the game!");
                        }
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 2, 0);
                    }
                }

                // голубь выход
                if(event.getClickedBlock().getLocation().equals(new Location(player.getWorld(), 151, 7, 147))) {
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);

                    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 1, true, false));

                    Bukkit.getScheduler().runTaskLater(this, () -> {
                        player.setVelocity(new Vector(0, 1, 0));
                        Bukkit.getScheduler().runTaskLater(this, () -> {
                            Location loc = new Location(player.getWorld(), -76, 9, -157);
                            loc.setYaw(90);
                            player.teleport(loc);
                        }, 5L);
                    }, 10L);
                }

                // библиотека
                if(event.getClickedBlock().getLocation().equals(new Location(player.getWorld(), -104, 12, -181))) {
                    ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
                    BookMeta bookMeta = (BookMeta) book.getItemMeta();
                    bookMeta.setAuthor(ChatColor.DARK_AQUA + "SirCat07");
                    bookMeta.setTitle("1000 и 1 факт про Астарту");

                    ArrayList<String> pages = new ArrayList<>();

                    pages.add(0, "1000 и 1 факт про Астарту.");
                    pages.add(1, "1 факт: порода Астарты - корниш-рекс.");
                    pages.add(2, "2 факт: раскраска у Астарты как у сиамской кошки.");
                    pages.add(3, "3 факт: Астарте надо долго привыкать к новому корму. Когда Астарте надо привыкать к новому корму, то она чешет своё ухо и иногда у неё появляются покраснения");
                    pages.add(4, "4 факт: Астарта любит спать с Расокет.");
                    pages.add(5, "5 факт: иногда Астарта зовёт сестру Расокет, чтобы она отправлялась спать. Когда сестра идёт с ней в кровать, Астарта сразу же уходит.");
                    pages.add(6, "6 факт: Астарта по какой-то причине закапывает свою мочу.");
                    pages.add(7, "7 факт: Астарта - не единственное имя Астарты. Все её имена: Астарта, Астарточка, Асстарта, Манюня, Манюша, Миланья, Милания.");
                    pages.add(8, "8 факт: Астарта третья кошка Расокет.");
                    pages.add(9, "9 факт: у Астарты было двое хозяинов: первая семья и семья Расокет.");
                    pages.add(10, "10 факт: прошлым хозяинам Астарты пришлось сделать объявления о том, что они отдают свою кошку из-за того, что у прошлых хозяинов родился ребёнок с аллергией на шерсть.");
                    pages.add(11, "11 факт: у прошлых хозяинов Астарта много рожала.");
                    pages.add(12, "12 факт: к сожалению, прошлые хозяины продавали котят Астарты.");
                    pages.add(13, "13 факт: Астарта любит сидеть на работающей стиральной машине.");
                    pages.add(14, "14 факт: Астарта прикольно зевает.");
                    pages.add(15, "15 факт: Астарта прикольно шипит.");
                    pages.add(16, "16 факт: когда Астарта только появилась у Расокет дома, Астарта на всё шипела и била.");
                    pages.add(17, "17 факт: когда Астарта только появилась у Расокет дома, она постоянно залезала на шкаф в кухне.");
                    pages.add(18, "18 факт: семья Расокет стерилизовала Астарту.");
                    pages.add(19, "19 факт: после операции, Астарта опять стала на всех шипеть и бить");
                    pages.add(20, "20 факт: после операции семья Расокет решили закрыть Астарту в переноске.");
                    pages.add(21, "21 факт: когда Астарта спит, она нагревается.");
                    pages.add(22, "22 факт: у Астарты хриплый голос.");
                    pages.add(23, "23 факт: Астарта мило мяукает и мурчит.");
                    pages.add(24, "24 факт: Астарта любит биться с первой кошкой Расокет, Джиной (играются).");
                    pages.add(25, "25 факт: мама Расокет захотела забрать Астарту.");
                    pages.add(26, "26 факт: у Астарты острые когти.");
                    pages.add(27, "27 факт: после того, как Астарте подстригают когти, она их быстро наращивает.");
                    pages.add(28, "28 факт: если засвет попадает на глаза Астарты, то её зрачки становятся красными.");
                    pages.add(29, "29 факт: у Астарты острые клыки.");
                    pages.add(30, "30 факт: когда Астарта ходит по кому-либо, то это действие с какой-то стороны можно считать за массаж.");
                    pages.add(31, "31 факт: если злобную Астарту почесать, то у неё будет прикольная улыбка");
                    pages.add(32, "32 факт: Астарта любит греться у ноутбука Расокет, когда она играет.");
                    pages.add(33, "33 факт: Расокет делала Астарту в Споре.");
                    pages.add(34, "34 факт: иногда, Астарта приходит к Расокет, когда она во что-либо играет. Она часто следит за чем-либо двигающимся.");
                    pages.add(35, "35 факт: Астарта любит наблюдать за существами Расокет в Споре.");
                    pages.add(36, "36 факт: до 03.05.2022, Астарту рисовала только Расокет");
                    pages.add(37, "37 факт: айсчатовцы любят Астарту.");
                    pages.add(38, "38 факт: подруга Расокет почему-то любит больше Джину, чем Астарту.");
                    pages.add(39, "39 факт: когда Расокет научилась рисовать корниш-рексов, то корниш-рексы, которых она рисовала в 99% случаях превращались в Астарту.");
                    pages.add(40, "40 факт: Астарта любит греться на солнышке.");
                    pages.add(41, "41 факт: Астарта любит спать клубочком.");
                    pages.add(42, "42 факт: когда Астарта спит, она СИЛЬНО нагревается.");
                    pages.add(43, "43 факт: спящая Астарта - хорошее снотворное!");
                    pages.add(44, "44 факт: когда Расокет смотрит в окно, то Астарта сразу же откуда-то появляется, запрыгивает на стол, залезает на подоконник и тоже начинает смотреть в окно.");
                    pages.add(45, "45 факт: одной ночью, Расокет решила посмотреть в окно. Астарта тоже решила посмотреть в окно. В небе была луна. Астарта увидела её. Она на неё удивлённо смотрела. Скорее всего, для неё полная луна в небе - очень удивительное событие.");
                    pages.add(46, "46 факт: иногда, когда Джина начинает мыться, Астарта тоже начинает мыться и наоборот.");
                    pages.add(47, "47 факт: иногда, когда Расокет начинает есть на кухне, Астарта тоже начинает есть.");
                    pages.add(48, "48 факт: Астарта громко пьёт и ест.");
                    pages.add(49, "49 факт: Астарта любит точить когти.");
                    pages.add(50, "50 факт: Астарта любит садится на мамин журнал с японскими сканвордами, на учебники и на тетради Расокет.");
                    pages.add(51, "Продолжите читать \"1000 и 1 факт про Астарту\" за $1999.99!");
                    pages.add(52, " ");
                    pages.add(53, "*страница поцарапана мопсом*");
                    pages.add(54, "1000 факт: Астарта.");

                    bookMeta.setPages(pages);

                    book.setItemMeta(bookMeta);

                    player.openBook(book);
                    player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1, 0);
                }

                // ченджлог
                if(event.getClickedBlock().getLocation().equals(new Location(player.getWorld(), -104, 10, -182))) {
                    ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
                    BookMeta bookMeta = (BookMeta) book.getItemMeta();
                    bookMeta.setAuthor(ChatColor.DARK_AQUA + "SirCat07");
                    bookMeta.setTitle("MopsNet Changelog");

                    ArrayList<String> pages = new ArrayList<>();
                    pages.add(0,
                            "     " + ChatColor.GREEN + ChatColor.BOLD + "CHANGELOG" + "\n" + ChatColor.RESET +
                            ChatColor.DARK_GREEN + "+ " + ChatColor.BLACK + "WoolBattle" + "\n" +
                            "    Leaderboards" + "\n" +
                            ChatColor.DARK_GREEN + "+ " + ChatColor.BLACK + "Woolbattle Attack" + "\n" +
                            "    Defense NPC" + "\n" +
                            ChatColor.DARK_GREEN + "+ " + ChatColor.BLACK + "Deliveries" + "\n" +
                            ChatColor.GRAY + "- " + ChatColor.BLACK + "Bug Fixes" + "\n" +
                            ChatColor.GRAY + "- " + ChatColor.BLACK + "AutoMod Updates" + "\n" +
                            ChatColor.GRAY + "- " + ChatColor.BLACK + "Aura Changes" + "\n"
                    );
                    bookMeta.setPages(pages);

                    book.setItemMeta(bookMeta);

                    player.openBook(book);
                    player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1, 0);
                }

                // фанни лантерн
                if(event.getClickedBlock().getLocation().equals(new Location(player.getWorld(), -99, 10, -169))) {
                    if(event.getHand() == EquipmentSlot.HAND) {
                        boolean giveLamp = true;
                        try {
                            if(player.getInventory().contains(Material.LANTERN)) {
                                giveLamp = false;
                            }
                            if(player.getInventory().getItemInOffHand().getType() == Material.LANTERN) {
                                giveLamp = false;
                            }
                            if(player.getInventory().getItemInMainHand().getType() == Material.LANTERN) {
                                giveLamp = false;
                            }
                        } catch (Exception ignored) { }
                        if(giveLamp) {
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
                            player.sendMessage(ChatColor.GRAY + "You got the secret " + ChatColor.GOLD + "Funny Lantern" + ChatColor.GRAY + "!");

                            ItemStack lantern = MopsUtils.createItem(Material.LANTERN, ChatColor.GOLD + "Funny Lantern");
                            ItemMeta meta = lantern.getItemMeta();
                            meta.setLore(Collections.singletonList(ChatColor.GRAY + "It is very funny tho"));
                            lantern.setItemMeta(meta);

                            player.getInventory().addItem(lantern);
                            new Location(player.getWorld(), -99, 10, -169).getBlock().setType(Material.AIR);
                        }
                    }
                }

                // локпик
                if(event.getClickedBlock().getType() == Material.IRON_TRAPDOOR) {
                    if(event.getItem().getType() == new Items().lockPick().getType() && event.getItem().getItemMeta().getDisplayName().equals(new Items().lockPick().getItemMeta().getDisplayName())) {
                        player.teleport(event.getClickedBlock().getLocation());
                        player.setSwimming(true);

                        player.getInventory().remove(MopsUtils.amount(new Items().lockPick(), 1));
                        event.setCancelled(true);
                    }
                }

                // лантерн поставить
                if(event.getItem().getType() == new Items().funnyLantern().getType() && event.getItem().getItemMeta().getDisplayName().equals(new Items().funnyLantern().getItemMeta().getDisplayName())) {
                    player.sendMessage(ChatColor.GRAY + "You have shined the light back again.");

                    new Location(mainworld, -99, 9, -169).getBlock().setType(Material.LANTERN);
                    player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1, 2);
                    player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1, 2);
                    player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1, 1);
                    player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1, 0);
                    player.getInventory().remove(MopsUtils.amount(new Items().funnyLantern(), 1));
                }
            }

            if(action == Action.RIGHT_CLICK_BLOCK || action == Action.LEFT_CLICK_BLOCK) {
                // голубь вход
                if (event.getClickedBlock().getLocation().equals(new Location(player.getWorld(), -77, 9, -157))) {
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);

                    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 1, true, false));

                    Bukkit.getScheduler().runTaskLater(this, () -> {
                        Location loc = new Location(player.getWorld(), 151.5, 11.0, 147.5);
                        loc.setYaw(-90);
                        player.teleport(loc);

                        MopsFiles.setWasAtPigeon(player, true);
                    }, 5L);
                }
            }

            //лантерн

            if(MopsFiles.getRank(player).getPermLevel() < 10) {
                if (!flippable.contains(event.getClickedBlock().getLocation())) {
                    if(!usables.contains(event.getClickedBlock().getLocation())) {
                        if(!openables.contains(event.getClickedBlock().getLocation())) {
                            if(!atmButtons.contains(event.getClickedBlock().getLocation())) {
                                event.setCancelled(true);
                            }
                        }
                    }
                }
            }
        } catch (NullPointerException ignored) {}
    }

    @EventHandler
    public void onEntityInteract(PlayerInteractEntityEvent event) {
        if(event.getHand().equals(EquipmentSlot.HAND)) {
            if(event.getRightClicked() instanceof Player clickedAt) {
                craftNewOverview(clickedAt, event.getPlayer());
            }
        }
    }

    @EventHandler
    public void onAtEntityInteract(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();

        if(event.getHand().equals(EquipmentSlot.HAND)) {
            String dialogue = ChatColor.RED + "no dialogue found :p blehh (report to sircat)";
            boolean cancelDialogue = false;

            if (entity.getScoreboardTags().contains("armorStandHubNPC")) {
                event.setCancelled(true);
                Set<String> scoreboardTags = entity.getScoreboardTags();

                if (scoreboardTags.contains("fishermanDogeNPC")) {
                    dialogue = "Giv me gfish please i want fis!!!1!!";
                    player.playSound(player.getLocation(), Sound.ENTITY_FISH_SWIM, 10, 2);
                }
                if (scoreboardTags.contains("snowCleaningDoge")) {
                    Calendar calendar = Calendar.getInstance();

                    if(calendar.get(Calendar.MONTH) == Calendar.DECEMBER || calendar.get(Calendar.MONTH) == Calendar.JANUARY) {
                        dialogue = "i am cleaning snow i think";
                        player.playSound(player.getLocation(), Sound.ENTITY_WOLF_AMBIENT, 10, 2);
                    } else {
                        dialogue = "i am cleaning something i think, i may not be";
                        player.playSound(player.getLocation(), Sound.ENTITY_WOLF_AMBIENT, 10, 2);
                    }
                }
                if (scoreboardTags.contains("builderDogeNPC")) {
                    dialogue = "hi this par t of hub not buildt please wait!!1";
                    player.playSound(player.getLocation(), Sound.ENTITY_WOLF_AMBIENT, 10, 2);
                }
                if (scoreboardTags.contains("tord")) {
                    List<String> dialogueList = new ArrayList<>();
                    dialogueList.add("hi im clown");
                    dialogueList.add("hi");

                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BANJO, 2, 0);

                    MopsUtils.sendRandomDialogueMessage(dialogueList, player, entity);
                    cancelDialogue = true;
                }
                if (scoreboardTags.contains("missionDogeNPC")) {
                    switch (missionDogeDialogue.get(player)) {
                        case 0 -> {
                            dialogue = "I can't currently give you missions.";
                            player.playSound(player.getLocation(), Sound.ENTITY_WOLF_GROWL, 10, 0);
                            player.playSound(player.getLocation(), Sound.ENTITY_WOLF_AMBIENT, 10, 0);
                        }
                    }
                }
                if (scoreboardTags.contains("woolbattle4TeamDoge")) {
                    switch (woolbattle4TeamDialogue.get(player)) {
                        case 0 -> {
                            dialogue = ChatColor.RED + "Wool" + ChatColor.YELLOW + "battle" + ChatColor.GREEN + " 4-" + ChatColor.AQUA + "Teams" + ChatColor.WHITE + " Is a gamemode of Woolbattle where you have 4 Teams. Click again to join.";
                            player.playSound(player.getLocation(), Sound.ENTITY_WOLF_AMBIENT, 10, 2);

                            woolbattle4TeamDialogue.put(player, woolbattle4TeamDialogue.get(player) + 1);
                        }
                        case 1 -> {
                            cancelDialogue = true;

                            MopsUtils.sendToServer(this, player, "woolbattle4teams");
                        }
                    }
                }
                if (scoreboardTags.contains("woolbattle2TeamDoge")) {
                    switch (woolbattle2TeamDialogue.get(player)) {
                        case 0 -> {
                            dialogue = ChatColor.RED + "Woolbattle" + ChatColor.AQUA + " 2-Teams" + ChatColor.WHITE + " Is a gamemode of Woolbattle where you have 2 Teams. Click again to join.";
                            player.playSound(player.getLocation(), Sound.ENTITY_WOLF_AMBIENT, 10, 2);

                            woolbattle2TeamDialogue.put(player, woolbattle2TeamDialogue.get(player) + 1);
                        }
                        case 1 -> {
                            cancelDialogue = true;

                            MopsUtils.sendToServer(this, player, "woolbattle2teams");
                        }
                    }
                }
                if (scoreboardTags.contains("woolbattleADDoge")) {
                    switch (woolbattleADDialogue.get(player)) {
                        case 0 -> {
                            dialogue = "Woolbattle " + ChatColor.GOLD + "Attack" + ChatColor.GRAY + "/" + ChatColor.GREEN + "Defense" + ChatColor.WHITE + " Is a gamemode of Woolbattle where the " + ChatColor.GREEN + "Defense" + ChatColor.WHITE + " command has all the Generators, and the goal of " + ChatColor.GOLD + "Attack" + ChatColor.WHITE + " team is to capture them all. Click again to join.";
                            player.playSound(player.getLocation(), Sound.ENTITY_WOLF_AMBIENT, 10, 2);

                            woolbattleADDialogue.put(player, woolbattleADDialogue.get(player) + 1);
                        }
                        case 1 -> {
                            cancelDialogue = true;

                            MopsUtils.sendToServer(this, player, "woolbattleAD");
                        }
                    }
                }
                if (scoreboardTags.contains("kitManager")) {
                    dialogue = "hi i manage kits";
                    player.playSound(player.getLocation(), Sound.ENTITY_FROG_AMBIENT, 2, 1);
                }
                if (scoreboardTags.contains("realPlantNPC")) {
                    switch (realPlantDialogue.get(player)) {
                        case 0 -> {
                            dialogue = "shhh, im a plant.";
                            player.playSound(player.getLocation(), Sound.BLOCK_GRASS_BREAK, 0.5F, 1.5F);

                            realPlantDialogue.put(player, realPlantDialogue.get(player) + 1);
                        }
                        case 1 -> {
                            dialogue = "a real real plant! can you believe it?";
                            player.playSound(player.getLocation(), Sound.BLOCK_GRASS_BREAK, 0.5F, 1.5F);

                            realPlantDialogue.put(player, realPlantDialogue.get(player) + 1);
                        }
                        case 2 -> {
                            dialogue = "you could join private games if you donate";
                            player.playSound(player.getLocation(), Sound.BLOCK_GRASS_BREAK, 0.5F, 1.5F);

                            realPlantDialogue.put(player, realPlantDialogue.get(player) + 1);
                        }
                        case 3 -> {
                            dialogue = "right here, at the plant!";
                            player.playSound(player.getLocation(), Sound.BLOCK_GRASS_BREAK, 0.5F, 1.5F);

                            realPlantDialogue.put(player, realPlantDialogue.get(player) + 1);
                        }
                        case 4 -> {
                            dialogue = "crazy world we live in....";
                            player.playSound(player.getLocation(), Sound.BLOCK_GRASS_BREAK, 0.5F, 1.5F);

                            realPlantDialogue.put(player, 0);
                        }
                    }
                }
                if (scoreboardTags.contains("pvpDogeNPC")) {
                    if(MopsFiles.getDeliveries(player.getUniqueId()).isEmpty()) {
                        switch (deliveryDogeDialogue.get(player)) {
                            case 0 -> {
                                dialogue = "You don't have any Deliveries. Do you want to send one instead? Use /delivery PlayerName to deliver items!";
                                player.playSound(player.getLocation(), Sound.ENTITY_WOLF_AMBIENT, 10, 2);
                            }
                        }
                    } else {
                        dialogue = "Delivery time!";
                        player.playSound(player.getLocation(), Sound.ENTITY_WOLF_AMBIENT, 10, 2);

                        Inventory inventory = Bukkit.createInventory(null, 45, "Your Deliveries");
                        fillDeliveryInventory(inventory, player, player.getUniqueId());

                        deliveryInventory.put(player, inventory);
                        player.openInventory(inventory);
                    }
                }
                if (scoreboardTags.contains("lonelyPigeon")) {
                    dialogue = "hey " + ChatColor.GRAY + "(add quest later)";
                    player.playSound(player.getLocation(), Sound.ENTITY_PARROT_AMBIENT, 10, 2);
                }

                try {
                    if(player.getItemInHand().getType() == Material.LANTERN) {
                        int random = (int) (Math.random() * (20 + 1)) + 1;
                        if(random == 1) {
                            dialogue = ChatColor.GOLD + "lmao thats a funny lantern";
                            player.sendTitle("", ChatColor.DARK_AQUA + "Something tells you to put it away.", 10, 30, 20);
                        }

                        int random2 = (int) (Math.random() * (60 + 1)) + 1;
                        if(random2 == 1) {
                            String name = entity.getCustomName();
                            if(!name.contains(ChatColor.MAGIC + "")) {
                                entity.setCustomName(ChatColor.RED + "" + ChatColor.MAGIC + "" + ChatColor.BOLD + "Funny Lantern");
                                player.sendTitle("", ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Something tells you to put it away.", 10, 30, 20);

                                Bukkit.getScheduler().runTaskLater(this, () -> {
                                    entity.setCustomName(name);
                                }, 5L);

                                cancelDialogue = true;
                            }
                        }
                    }
                } catch (Exception ignored) { }

                if(!scoreboardTags.contains("guideline") && !cancelDialogue) {
                    MopsUtils.sendDialogueMessage(dialogue, player, entity);
                } else if (scoreboardTags.contains("furnaceGuideline")) {
                    player.sendMessage(ChatColor.GRAY + "This furnace is the only one in the hub. It smelts corn for the Theatre, or fish for the Fisherman. It also needs to be fueled, however, not by coal. It uses MopsCoins. But the Doge are not always smart. They tried to put all sorts of items in there to fuel the Smelter. And sometimes when you put in MopsCoins, it gives you some cool items. It may still give you something!");
                }
            }
            if (entity.getScoreboardTags().contains("adminfrog")) {
                dialogue = "It is Friday, my dudes.";
                player.playSound(player.getLocation(), Sound.ENTITY_FROG_AMBIENT, 10, 1);

                MopsUtils.sendDialogueMessage(dialogue, player, entity);
            }
            if (entity.getScoreboardTags().contains("blehhcat")) {
                List<String> dialogueList = new ArrayList<>();
                dialogueList.add("meow");
                dialogueList.add("meowww");
                dialogueList.add("blehh");
                dialogueList.add(":p");
                dialogueList.add("mrow");
                dialogueList.add("get sillay");

                if(MopsUtils.sendRandomDialogueMessage(dialogueList, player, entity).equals("blehh")) {
                    player.playSound(player.getLocation(), Sound.ENTITY_FROG_TONGUE, 2, 1);
                }
                player.playSound(player.getLocation(), Sound.ENTITY_CAT_AMBIENT, 2, 1);
            }
            if (entity.getScoreboardTags().contains("shulk")) {
                int maxSize = (int) (Math.random() * (12 - 2 + 1)) + 2;
                StringBuilder text = new StringBuilder();
                String alphabet = "abcdefghijklmnopqrstuvwxyz";

                int i = 0;
                while(i < maxSize) {
                    int letterNumber = (int) (Math.random() * (25 + 1)) + 1;
                    text.append(alphabet.charAt(letterNumber-1));
                    i++;
                }
                String realText = text.toString().replace("mops", ChatColor.GOLD + "mops" + ChatColor.RESET);

                MopsUtils.sendDialogueMessage(realText, player, entity);
                player.playSound(player.getLocation(), Sound.ENTITY_SHULKER_AMBIENT, 2, 1);
            }

            if (entity.getScoreboardTags().contains("actualPlant")) {
                event.setCancelled(true);
                player.playSound(player.getLocation(), Sound.BLOCK_GRASS_BREAK, 1F, 1.5F);
            }
            if (entity.getScoreboardTags().contains("skeletonLock")) {
                event.setCancelled(true);

                boolean open = false;
                try {
                    if (player.getItemInHand().getType() == new Items().skeletonKey().getType() && player.getItemInHand().getItemMeta().getDisplayName().equals(new Items().skeletonKey().getItemMeta().getDisplayName())) {
                        open = true;

                        player.playSound(player.getLocation(), Sound.ENTITY_SKELETON_STEP, 4F, 0F);
                        player.sendMessage(ChatColor.GRAY + "You do have the key.");

                        entity.setVelocity(new Vector(0, 0.25, 0));
                        Bukkit.getScheduler().runTaskLater(this, () -> {
                            entity.teleport(new Location(mainworld, -94, 4, -170));

                            player.playSound(player.getLocation(), Sound.ENTITY_SKELETON_HURT, 1, 0);
                            player.playSound(player.getLocation(), Sound.ENTITY_SKELETON_CONVERTED_TO_STRAY, 1, 0);
                            player.playSound(player.getLocation(), Sound.ENTITY_SKELETON_CONVERTED_TO_STRAY, 1, 0);

                            Particle.DustOptions dustOptions = new Particle.DustOptions(Color.WHITE, 1F);
                            mainworld.spawnParticle(Particle.REDSTONE, new Location(mainworld, -94, 9.5, -169), 10, 0.5, 0.5, 0.5, dustOptions);

                            new Location(mainworld, -94, 8, -168).getBlock().setType(Material.REDSTONE_TORCH);

                            Bukkit.getScheduler().runTaskLater(this, () -> {
                                new Location(mainworld, -94, 8, -168).getBlock().setType(Material.AIR);

                                entity.teleport(new Location(mainworld, -93.95, 9, -168.5));
                            }, 2400L);
                        }, 5L);
                    }
                } catch (Exception ignored) { }

                if(!open) {
                    player.playSound(player.getLocation(), Sound.ENTITY_SKELETON_STEP, 4F, 0F);
                    player.sendMessage(ChatColor.GRAY + "You do not have the key.");
                }
            }


            if (entity.getScoreboardTags().contains("wbLeaderboard")) {
                event.setCancelled(true);
                Inventory inv = Bukkit.createInventory(null, 27, ChatColor.YELLOW + "Your Woolbattle Stats");
                int i = 0;
                while(i < 27) {
                    inv.setItem(i, MopsUtils.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
                    i++;
                }
                inv.setItem(11, MopsUtils.createItem(Material.BOOK, ChatColor.GRAY + "Killwins: " + ChatColor.YELLOW + MopsFiles.getWoolbattleWins(player)));
                inv.setItem(13, MopsUtils.createItem(Material.BOOK, ChatColor.GRAY + "Wipeouts: " + ChatColor.YELLOW + MopsFiles.getWoolbattleWipeouts(player)));
                inv.setItem(15, MopsUtils.createItem(Material.BOOK, ChatColor.GRAY + "Dominations: " + ChatColor.YELLOW + MopsFiles.getWoolbattleDominations(player)));

                cancelledInventory.put(player, inv);
                player.openInventory(inv);
            }

            if(entity == ball) {
                event.setCancelled(true);
            }
        }
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.setJoinMessage("");

        auraTimer.put(player, 4);
        auraRadius.put(player, 0.0);
        aura.put(player, MopsFiles.getAura(player));

        leftSecondsAgo.putIfAbsent(player.getUniqueId(), 500);
        restoreDeliveryItem.put(player, true);

        if(duckActive) {
            duckPlayer.hidePlayer(this, player);
        }

        Bukkit.getScheduler().runTaskLater(this, () -> {
            int random1 = (int) (Math.random() * 3);
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, random1);
            player.sendTitle(ChatColor.AQUA + "Welcome!", ChatColor.DARK_AQUA + "To MopsNetwork", 10, 30, 20);
            Bukkit.getScheduler().runTaskLater(this, () -> {
                int random2 = (int) (Math.random() * 3);
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, random2);
                Bukkit.getScheduler().runTaskLater(this, () -> {
                    int random3 = (int) (Math.random() * 3);
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, random3);
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1, random3);
                }, 4L);
            }, 4L);
        }, 50L);

        player.setPlayerListName((MopsFiles.getRank(player).getPrefix() + player.getName() + MopsFiles.getBadge(player).getSymbol()).trim());

        manipulateEnderChest(player);

        if(leftSecondsAgo.get(player.getUniqueId()) > 20) {
            player.getInventory().clear();
        } else {
            player.sendMessage(ChatColor.GREEN + "Your inventory was saved since you logged off very recently!");
        }

        Items items = new Items();

        ItemStack compass = items.compass();
        ItemStack profile = items.profile();
        ItemStack custom = items.customization();
        stupidItems.add(compass); stupidItems.add(custom); stupidItems.add(profile);
        player.getInventory().setItem(0, compass);
        player.getInventory().setItem(1, profile);
        player.getInventory().setItem(8, custom);



        Location spawn = new Location(player.getWorld(), -106.0, 9, -186.0);
        spawn.setYaw(-90);

        player.teleport(spawn);

        deliveryDogeDialogue.putIfAbsent(player, 0);
        woolbattle2TeamDialogue.putIfAbsent(player, 0);
        woolbattle4TeamDialogue.putIfAbsent(player, 0);
        woolbattleADDialogue.putIfAbsent(player, 0);
        missionDogeDialogue.putIfAbsent(player, 0);
        realPlantDialogue.putIfAbsent(player, 0);
        pigeonDialogue.putIfAbsent(player, 0);

        player.setGameMode(GameMode.SURVIVAL);
        for(Player allPlayers : Bukkit.getOnlinePlayers()) {
            allPlayers.sendMessage( ChatColor.GOLD + "[MopsPVPs] " + ChatColor.YELLOW + player.getName() + " joined the game.");
        }

        if(!MopsFiles.getDeliveries(player.getUniqueId()).isEmpty()) {
            int unclaimedDeliveries = MopsFiles.getDeliveries(player.getUniqueId()).size();
            String delivery = "delivery";
            if(unclaimedDeliveries > 1) {
                delivery = "deliveries";
            }
            player.sendMessage( ChatColor.BLUE + "[MopsDeliveryService] " + ChatColor.RED + "You have " + ChatColor.BOLD + unclaimedDeliveries + ChatColor.RESET + "" + ChatColor.RED + " unclaimed " + delivery + "!");
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.setQuitMessage("");

        leftSecondsAgo.put(player.getUniqueId(), 0);

        if(player == duckPlayer) {
            stopDuck();
        }

        for(Player allPlayers : Bukkit.getOnlinePlayers()) {
            allPlayers.sendMessage( ChatColor.GOLD + "[MopsPVPs] " + ChatColor.YELLOW + player.getName() + " left the game. " + ChatColor.AQUA + ":(");
        }
    }


    @EventHandler
    public void onDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();

        if(MopsFiles.getRank(player).getPermLevel() < 10) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {
        Player player = event.getPlayer();

        String message = MopsUtils.emojify(event.getMessage());
        if(MopsFiles.getRank(player).getPermLevel() > 10) {
            message = MopsUtils.convertColorCodes(message);
        }
        event.setCancelled(true);

        for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if(message.toLowerCase(Locale.ROOT).contains(onlinePlayer.getName().toLowerCase(Locale.ROOT)) && !MopsUtils.isAutomodded(message)) {
                String[] texts = message.split(" ");
                int i = 0;
                while (i < texts.length) {
                    if(texts[i].toLowerCase(Locale.ROOT).equals(onlinePlayer.getName().toLowerCase(Locale.ROOT))) {
                        texts[i] = MopsFiles.getRank(onlinePlayer).getPrefix() + onlinePlayer.getName() + ChatColor.RESET;
                        onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
                    }
                    i++;
                }
                message = MopsUtils.combineStrings(texts, " ");
            }
        }

        TextComponent preMessage = Component.text(MopsFiles.getRank(player).getPrefix() + player.getName());
        TextComponent messageBadge = Component.text(MopsFiles.getBadge(player).getSymbol()).hoverEvent(Component.text(MopsFiles.getBadge(player).getDescription()));
        TextComponent afterMessage = Component.text(ChatColor.RESET + ": " + message.trim());

        TextComponent fullMessage = preMessage.append(messageBadge).append(afterMessage);

        if(MopsUtils.isAutomodded(message)) {
            player.sendMessage(ChatColor.GRAY + "Your message was blocked or not delivered.");
            player.sendMessage(ChatColor.RED + ChatColor.stripColor(MopsFiles.getRank(player).getPrefix() + player.getName() + MopsFiles.getBadge(player).getSymbol() + ChatColor.RESET + ": " + message.trim()));
        } else {
            for (Player allPlayers : Bukkit.getOnlinePlayers()) {
                MopsUtils.sendTextComponentMessage(allPlayers, fullMessage);
            }
        }

        System.out.println(player.getName() + ": " + message);
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if(!event.getEntity().getScoreboardTags().contains("nohunger")) {
            event.setFoodLevel(20);
        }
    }

    //надо билд

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (event.getClickedInventory() == cancelledInventory.get(player)) {
            event.setCancelled(true);
        }
        if (event.getClickedInventory() == deliveryInventory.get(player)) {
            event.setCancelled(true);

            try {
                ItemStack item = event.getClickedInventory().getItem(event.getSlot());
                ItemMeta meta = item.getItemMeta();
                String deliveryID = meta.getLore().get(6).replace(ChatColor.DARK_GRAY + "Delivery ID: ", "");

                if(event.getClick().isLeftClick()) {
                    player.getInventory().addItem(MopsFiles.getDelivery(deliveryID).getDeliveredItem());
                    MopsFiles.removeDelivery(deliveryID);

                    player.sendMessage(ChatColor.GREEN + "You claimed your delivery!");
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
                } else if(event.getClick().isRightClick()) {
                    Delivery delivery = MopsFiles.getDelivery(deliveryID);
                    Delivery deliveryBack = new Delivery().createNewDelivery(delivery.getDeliveredItem(), delivery.getReceiver(), delivery.getSender());
                    MopsFiles.addDelivery(deliveryBack);

                    MopsFiles.removeDelivery(deliveryID);

                    player.sendMessage(ChatColor.GREEN + "You sent the delivery back.");
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 0);
                }

                Inventory inventory = Bukkit.createInventory(null, 45, "Your Deliveries");
                fillDeliveryInventory(inventory, player, player.getUniqueId());

                deliveryInventory.put(player, inventory);
                player.openInventory(inventory);

            } catch (Exception ignored) { }
        }
        if (event.getClickedInventory() == gamesGUI.get(player)) {
            event.setCancelled(true);
            if(event.getClick().isShiftClick()) {
                for(ItemStack item : stupidItems) {
                    gamesGUI.get(player).remove(item);
                }
            }
            try {
                event.getClickedInventory().getItem(event.getSlot()).getType();

                World world = event.getWhoClicked().getWorld();
                Location newDestination = new Location(world, -106.0, 9, -186.0);

                switch (event.getSlot()) {
                    case 10 -> {
                        newDestination.setYaw(-90);
                        player.teleport(newDestination);
                        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 2);
                    }
                    case 11 -> {
                        newDestination = new Location(world, -101.0, 9, -177.0);
                        newDestination.setYaw(45);
                        player.teleport(newDestination);
                        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 2);
                    }
                    case 12 -> {
                        newDestination = new Location(world, -70.5, 7, -185.5);
                        player.teleport(newDestination);
                        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 2);
                    }
                    case 13 -> {
                        newDestination = new Location(world, -130.0, 9, -174.0);
                        newDestination.setYaw(90);
                        player.teleport(newDestination);
                        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 2);
                    }
                    case 14 -> {
                        newDestination = new Location(world, -82.0, 9, -167.0);
                        newDestination.setYaw(-45);
                        player.teleport(newDestination);
                        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 2);
                    }
                    case 15 -> {
                        newDestination = new Location(world, -60.5, 9, -200.5);
                        newDestination.setYaw(-90);
                        player.teleport(newDestination);
                        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 2);
                    }
                    case 16 -> {
                        newDestination = new Location(world, -74.5, 9, -208.5);
                        newDestination.setYaw(180);
                        player.teleport(newDestination);
                        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 2);
                    }
                }
                if(event.getSlot() == 19) {
                    if(MopsFiles.getWasAtPigeon(player)) {
                        newDestination = new Location(world, 151.5, 9.0, 147.5);
                        newDestination.setYaw(-90);
                        player.teleport(newDestination);
                        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 2);
                    } else {
                        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 0);
                        player.sendMessage(ChatColor.RED + "Locked destination!");
                    }
                }
            } catch (Exception ignored) { }
        }
        if (event.getClickedInventory() == profileGUI.get(player)) {
            event.setCancelled(true);

            if(event.getSlot() == 11) {
                player.openInventory(player.getEnderChest());
                player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 1, 1);
            }
            if(event.getSlot() == 15) {
                List<Language> languages = new ArrayList<>(Arrays.asList(Language.values()));
                HashMap<Language, Integer> map = new HashMap<>();

                int i = 0;
                while (i < languages.size()) {
                    map.put(languages.get(i), i);
                    i++;
                }

                int playerLanguage = map.get(MopsFiles.getLanguage(player));
                int nextLanguage = 0;

                if((playerLanguage + 1) < languages.size()) {
                    nextLanguage = playerLanguage + 1;
                }

                MopsFiles.setLanguage(player, languages.get(nextLanguage));

                player.sendMessage(ChatColor.GREEN + "You have changed your language to " + ChatColor.stripColor(languages.get(nextLanguage).getName()));
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);

                Inventory inv = Bukkit.createInventory(null, 27, "Your Profile & Settings");
                fillProfileMenu(inv, player);

                profileGUI.put(player, inv);
                player.openInventory(profileGUI.get(player));
            }
        }
        if (event.getClickedInventory() == cosmeticSelector.get(player)) {
            event.setCancelled(true);

            if(event.getSlot() == 12) {
                Inventory inv = Bukkit.createInventory(null, 36, "Select Your Effect");
                fillEffectsGUI(inv, player);

                effectsGUI.put(player, inv);
                player.openInventory(effectsGUI.get(player));
            }
            if(event.getSlot() == 14) {
                Inventory inv = Bukkit.createInventory(null, 36, "Select Your Aura");
                fillAurasGUI(inv, player);

                auraGUI.put(player, inv);
                player.openInventory(auraGUI.get(player));
            }
        }
        if (event.getClickedInventory() == auraGUI.get(player)) {
            event.setCancelled(true);
            if(event.getClick().isShiftClick()) {
                for(ItemStack item : stupidItems) {
                    auraGUI.get(player).remove(item);
                }
            }
            try {
                event.getClickedInventory().getItem(event.getSlot()).getType();

                switch (event.getSlot()) {
                    case 10 -> {
                        aura.put(player, Aura.CUBE);
                        player.playSound(player.getLocation(), Sound.ENTITY_ALLAY_ITEM_GIVEN, 1, 1);
                    }
                    case 11 -> {
                        if(MopsFiles.getBadge(player) == MopsBadge.SILLY || MopsFiles.getRank(player).getPermLevel() > 10) {
                            aura.put(player, Aura.ANDROMEDA);
                            player.playSound(player.getLocation(), Sound.BLOCK_FIRE_AMBIENT, 2, 2);
                            player.playSound(player.getLocation(), Sound.ENTITY_ALLAY_ITEM_GIVEN, 1, 1);
                        } else {
                            player.sendMessage(ChatColor.RED + "You can't use this effect.");
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 0);
                        }
                    }
                    case 12 -> {
                        if(MopsFiles.getBadge(player) == MopsBadge.STAFF || MopsFiles.getRank(player).getPermLevel() > 10) {
                            aura.put(player, Aura.INFINITY);
                            player.playSound(player.getLocation(), Sound.ENTITY_ALLAY_ITEM_GIVEN, 1, 1);
                        } else {
                            player.sendMessage(ChatColor.RED + "You can't use this effect.");
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 0);
                        }
                    }
                    case 35 -> {
                        aura.put(player, Aura.NONE);
                        auraRadius.put(player, 0.0);
                        player.playSound(player.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 2);
                        player.sendMessage(ChatColor.GREEN + "You reset your effects.");
                    }
                }
                MopsFiles.setAura(player, aura.get(player));
            } catch (Exception ignored) { }
        }
        if (event.getClickedInventory() == effectsGUI.get(player)) {
            event.setCancelled(true);
            if(event.getClick().isShiftClick()) {
                for(ItemStack item : stupidItems) {
                    effectsGUI.get(player).remove(item);
                }
            }
        }
        if(overviewInventories.contains(event.getClickedInventory())) {
            event.setCancelled(true);

            if(event.getSlot() == 35) {
                Inventory inv = Bukkit.createInventory(null, 27, "Insert Delivery Item");
                fillDeliveryItemInsert(inv, new ItemStack(Material.AIR), false);

                deliveryInsertInventories.add(inv);
                restoreDeliveryItem.put(player, true);
                player.openInventory(inv);

                deliveryInProcessReceiver.put(player, UUID.fromString(inventoryName.get(event.getClickedInventory())));
            }
        }
        if(deliveryInsertInventories.contains(event.getClickedInventory())) {
            if(event.getSlot() != 13) {
                event.setCancelled(true);
            }
            Bukkit.getScheduler().runTaskLater(this, () -> {
                try {
                    deliveryInProcessItem.put(player, event.getClickedInventory().getItem(13));

                    if(event.getClickedInventory().getItem(event.getSlot()) != null && event.getClickedInventory().getItem(event.getSlot()).getType() != Material.AIR) {
                        Inventory inv = Bukkit.createInventory(null, 27, "Insert Delivery Item");
                        fillDeliveryItemInsert(inv, event.getClickedInventory().getItem(13), true);

                        deliveryInsertInventories.add(inv);
                        restoreDeliveryItem.put(player, false);
                        player.openInventory(inv);
                        restoreDeliveryItem.put(player, true);
                    }

                    List<Material> blockedItems = new ArrayList<>();
                    blockedItems.add(Material.AIR);

                    if(event.getSlot() == 22 && event.getClickedInventory().getItem(event.getSlot()).getType() == Material.LIME_STAINED_GLASS_PANE) {
                        if (!(blockedItems.contains(deliveryInProcessItem.get(player).getType()) || stupidItems.contains(deliveryInProcessItem.get(player)))) {
                            if (MopsFiles.getCoins(player) >= 2) {
                                MopsFiles.setCoins(player, MopsFiles.getCoins(player) - 2);
                                restoreDeliveryItem.put(player, false);
                                player.getOpenInventory().close();

                                Delivery delivery = new Delivery().createNewDelivery(deliveryInProcessItem.get(player), player.getUniqueId(), deliveryInProcessReceiver.get(player));
                                MopsFiles.addDelivery(delivery);

                                if (Bukkit.getOfflinePlayer(deliveryInProcessReceiver.get(player)).getName() == null) {
                                    player.sendMessage(ChatColor.GREEN + "You have delivered an item!");
                                } else {
                                    player.sendMessage(ChatColor.GREEN + "You have delivered an item to " + Bukkit.getOfflinePlayer(deliveryInProcessReceiver.get(player)).getName() + "!");
                                }
                                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
                                deliveryInProcessItem.put(player, new ItemStack(Material.AIR));
                                event.getClickedInventory().setItem(13, new ItemStack(Material.AIR));

                                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(deliveryInProcessReceiver.get(player));
                                if (offlinePlayer.isOnline()) {
                                    if (!MopsFiles.getDeliveries(offlinePlayer.getUniqueId()).isEmpty()) {
                                        Player onlinePlayer = offlinePlayer.getPlayer();
                                        int unclaimedDeliveries = MopsFiles.getDeliveries(offlinePlayer.getUniqueId()).size();
                                        onlinePlayer.sendMessage(ChatColor.BLUE + "[MopsDeliveryService] " + ChatColor.RED + "You have " + ChatColor.BOLD + unclaimedDeliveries + ChatColor.RESET + "" + ChatColor.RED + " unclaimed delivery!");
                                    }
                                }
                            } else {
                                player.sendMessage(ChatColor.RED + "Not enough coins!");
                                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 0);
                            }
                        }
                    }
                } catch (Exception ignored) { }
            }, 5L);

            if(event.getSlot() == 22 && event.getClickedInventory().getItem(event.getSlot()).getType() == Material.RED_STAINED_GLASS_PANE) {
                player.sendMessage(ChatColor.RED + "You do not have an item inserted!");
            }
        }
        if(event.getInventory().getType() == InventoryType.ENDER_CHEST) {
            if((event.getSlot() >= 5 && event.getSlot() <= 8 && event.getSlotType().equals(InventoryType.SlotType.CONTAINER)) || (event.getSlot() >= 14 && event.getSlot() <= 15 && event.getSlotType().equals(InventoryType.SlotType.CONTAINER)) || (event.getSlot() == 17 && event.getSlotType().equals(InventoryType.SlotType.CONTAINER)) || (event.getSlot() >= 23 && event.getSlot() <= 26 && event.getSlotType().equals(InventoryType.SlotType.CONTAINER))) {
                if(event.getClickedInventory().getType() == InventoryType.ENDER_CHEST) {
                    event.setCancelled(true);
                }
            }
            if(event.getSlot() == 16) {
                try {
                    if (!event.getCursor().getType().toString().contains("SHULKER_BOX")) {
                        event.setCancelled(true);
                    } else {
                        ItemStack item = event.getCursor();
                        if (!event.getCurrentItem().getType().toString().contains("SHULKER_BOX")) {
                            ItemMeta meta = item.getItemMeta();
                            List<String> lore = meta.getLore();
                            lore.add(ChatColor.YELLOW + "Left-Click to open" + ChatColor.GOLD + " | " + ChatColor.YELLOW + "Right-Click to remove");
                            meta.setLore(lore);
                            item.setItemMeta(meta);
                        }

                        event.setCursor(item);
                    }
                } catch (Exception ignored) { }

                if(event.getCurrentItem().getType().toString().contains("SHULKER_BOX")) {
                    if (event.isShiftClick()) {
                        if(!event.isRightClick()) {
                            event.setCancelled(true);
                        }
                    }
                    if(event.isLeftClick()) {
                        BlockStateMeta bsm = (BlockStateMeta) event.getCurrentItem().getItemMeta();
                        ShulkerBox box = (ShulkerBox) bsm.getBlockState();

                        enderChestBackpack.put(player, event.getCurrentItem());

                        player.playSound(player.getLocation(), Sound.ITEM_BUNDLE_DROP_CONTENTS, 5, 1);
                        player.openInventory(box.getInventory());
                    }
                    if(event.isRightClick()) {
                        try {
                            event.getCursor();

                            enderChestBackpack.put(player, null);
                            event.setCancelled(false);

                            try {
                                ItemStack item = event.getCurrentItem();
                                ItemMeta meta = item.getItemMeta();
                                List<String> lore = meta.getLore();
                                for(String string : lore) {
                                    lore.remove(ChatColor.YELLOW + "Left-Click to open" + ChatColor.GOLD + " | " + ChatColor.YELLOW + "Right-Click to remove");
                                }
                                meta.setLore(lore);
                                item.setItemMeta(meta);
                            } catch (Exception ignored) { }

                        } catch (Exception e) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
            manipulateEnderChest(player);
        }

        player.getInventory().remove(Material.BROWN_STAINED_GLASS_PANE);

        try {
            if(player.getItemOnCursor().getType() == Material.BROWN_STAINED_GLASS_PANE) {
                player.setItemOnCursor(new ItemStack(Material.BROWN_STAINED_GLASS_PANE, 0));
            }

            if (stupidItems.contains(event.getCurrentItem())) {
                event.setCancelled(true);
            }

            if(event.getInventory().getType() == InventoryType.SHULKER_BOX) {
                if (enderChestBackpack.get(player) != null) {
                    if(event.getCurrentItem().getType() == Material.BLACK_STAINED_GLASS_PANE) {
                        event.setCancelled(true);
                    }

                    ItemStack item = enderChestBackpack.get(player);

                    BlockStateMeta bsm = (BlockStateMeta) item.getItemMeta();
                    ShulkerBox box = (ShulkerBox) bsm.getBlockState();
                    Inventory shulkerBoxInv = player.getOpenInventory().getTopInventory();
                    box.getInventory().setContents(shulkerBoxInv.getContents());

                    bsm.setBlockState(box);
                    box.update();
                    item.setItemMeta(bsm);

                    Bukkit.getScheduler().runTaskLater(this, () -> {
                        box.getInventory().setContents(shulkerBoxInv.getContents());

                        bsm.setBlockState(box);
                        box.update();
                        item.setItemMeta(bsm);

                        player.getEnderChest().setItem(16, item);
                        manipulateEnderChest(player);
                    }, 5L);
                }
            }
        } catch (Exception ignored) { }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        Inventory inv = event.getInventory();

        try {
            if (deliveryInsertInventories.contains(inv)) {
                if(restoreDeliveryItem.get(player)) {
                    player.getInventory().addItem(inv.getItem(13));
                }
            }
        } catch (Exception ignored) { }
    }

    //МНЕ БИЛД ПЖ

    @EventHandler
    public void onMoveItemEvent(InventoryMoveItemEvent event) {
        if (stupidItems.contains(event.getItem())) {
            event.setCancelled(true);
        }
        if(event.getItem().getType() == Material.BROWN_STAINED_GLASS_PANE) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player player) {
            if(player == duckPlayer) {
                event.setCancelled(true);
            } else {
                event.setDamage(0);
            }
        } else if(!event.getCause().equals(EntityDamageEvent.DamageCause.VOID)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void entityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity victim = event.getEntity();

        if(damager instanceof Player player) {
            if(victim == ball) {
                if (player.isSneaking()) {
                    double random = ThreadLocalRandom.current().nextDouble(0.1, 0.3 + 1);
                    ball.setVelocity(player.getEyeLocation().getDirection().multiply(random));
                } else if (!player.isSprinting()) {
                    double random = ThreadLocalRandom.current().nextDouble(0.3, 0.5 + 1);
                    ball.setVelocity(player.getEyeLocation().getDirection().multiply(random));
                } else if (player.isSprinting()) {
                    double random = ThreadLocalRandom.current().nextDouble(0.6, 1 + 1);
                    ball.setVelocity(player.getEyeLocation().getDirection().multiply(random).add(new Vector(0, 0.2, 0)));
                }
                player.playSound(player.getLocation(), Sound.BLOCK_BAMBOO_HIT, 1, 1);
            }
        }
        if(damager instanceof Arrow arrow) {
            if(arrow.getShooter() instanceof Player player && player.equals(duckPlayer)) {
                if(victim.getScoreboardTags().contains("target")) {
                    victim.remove();
                    damager.remove();
                    duckPoints++;

                    String strikeBar = (ChatColor.RED + "X").repeat(duckStrikes) + (ChatColor.GRAY + "X").repeat(4-duckStrikes);

                    String message = ChatColor.YELLOW + "Your Points: " + ChatColor.GOLD + duckPoints + ChatColor.GRAY + " | Strikes: " + strikeBar;
                    if(duckStrikes > 0) {
                        message = ChatColor.YELLOW + "Your Points: " + ChatColor.GOLD + duckPoints + ChatColor.RED + " | Strikes: " + strikeBar;
                    }
                    MopsUtils.actionBarGenerator(duckPlayer, message);

                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                }
            }
            if(arrow.getShooter() instanceof Player player && player.equals(melonPlayer)) {
                if(victim.getScoreboardTags().contains("gameMelon")) {
                    victim.remove();
                    damager.remove();
                    melonPoints++;

                    melonPlayer.sendTitle("", ChatColor.GREEN + "+1 Second!", 0, 5, 10);
                    melonTimer++;

                    String melonSeconds = String.valueOf(melonTimer);

                    if(melonTimer < 10) {
                        melonSeconds = "0" + melonSeconds;
                    }

                    String message = ChatColor.GREEN + "Your Points: " + ChatColor.GOLD + melonPoints + ChatColor.DARK_GREEN + " | " + ChatColor.YELLOW + "Time Left: 0:" + melonSeconds;
                    if(melonTimer >= 60) {
                        message = ChatColor.GREEN + "Your Points: " + ChatColor.GOLD + melonPoints + ChatColor.DARK_GREEN + " | " + ChatColor.YELLOW + "Time Left: 1:" + melonSeconds;
                    }
                    MopsUtils.actionBarGenerator(melonPlayer, message);

                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                }
            }
        }
    }

    @EventHandler
    public void onVehicleDamage(VehicleDamageEvent event) {
        if(event.getVehicle().getType() == EntityType.MINECART_CHEST) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void blockFadeEvent(BlockFadeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onDrinkEvent(PlayerItemConsumeEvent event) {
        event.setCancelled(true);
    }

    public void announceRareDrop(String string, Player player) {
        for(Player allPlayers : Bukkit.getOnlinePlayers()) {
            allPlayers.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
            String message = player.getName() + " got the " + string;
            MopsUtils.actionBarGenerator(player, message);
        }
    }

    public void manipulateEnderChest(Player player) {
        Bukkit.getScheduler().runTaskLater(this, () -> {
            int value = getEnderchestValue(player);

            player.getEnderChest().setItem(5, MopsUtils.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
            player.getEnderChest().setItem(6, MopsUtils.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
            player.getEnderChest().setItem(7, MopsUtils.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
            player.getEnderChest().setItem(8, MopsUtils.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
            player.getEnderChest().setItem(14, MopsUtils.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
            player.getEnderChest().setItem(15, MopsUtils.createItem(Material.GOLD_INGOT, ChatColor.GOLD + "Value: " + value));
            player.getEnderChest().setItem(17, MopsUtils.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
            player.getEnderChest().setItem(23, MopsUtils.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
            player.getEnderChest().setItem(24, MopsUtils.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
            player.getEnderChest().setItem(25, MopsUtils.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
            player.getEnderChest().setItem(26, MopsUtils.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));

            for(ItemStack item : stupidItems) {
                player.getEnderChest().remove(item);
            }
            if(player.getEnderChest().contains(Material.BROWN_STAINED_GLASS_PANE)) {
                player.getEnderChest().remove(Material.BROWN_STAINED_GLASS_PANE);
            }
            player.getInventory().remove(Material.BROWN_STAINED_GLASS_PANE);

            try {
                if (!player.getEnderChest().getItem(16).getType().toString().contains("SHULKER_BOX") && !(player.getEnderChest().getItem(16).getType() == Material.BROWN_STAINED_GLASS_PANE)) {
                    player.getEnderChest().setItem(16, MopsUtils.createItem(Material.BROWN_STAINED_GLASS_PANE, MopsColor.BROWN.getColor() + "Backpack Slot"));
                }
            } catch (Exception e) {
                player.getEnderChest().setItem(16, MopsUtils.createItem(Material.BROWN_STAINED_GLASS_PANE, MopsColor.BROWN.getColor() + "Backpack Slot"));
            }
        }, 5L);
    }

    public int getEnderchestValue(Player player) {
        ItemStack[] contents = player.getEnderChest().getContents();
        int value = getAmount(contents, new Items().mopsCoin());

        try {
            BlockStateMeta bsm = (BlockStateMeta) player.getEnderChest().getItem(16).getItemMeta();
            ShulkerBox box = (ShulkerBox) bsm.getBlockState();
            ItemStack[] boxContents = box.getInventory().getContents();

            value += getAmount(boxContents, new Items().mopsCoin());

            for(ItemStack item : boxContents) {
                try {
                    if(item.getItemMeta().getDisplayName().contains(ChatColor.RED + "Kuudra Washing Machine 2.0")) {
                        value += item.getAmount()*1000;
                    }
                    if(item.getType() == Material.CORNFLOWER || item.getType() == Material.DANDELION) {
                        value += item.getAmount()*200;
                    }
                    if(item.getType() == Material.WITHER_ROSE || item.getType() == Material.BAMBOO) {
                        value += item.getAmount()*400;
                    }
                } catch (Exception ignored) { }
            }
        } catch (Exception igonred) { }

        for(ItemStack item : contents) {
            try {
                if(item.getItemMeta().getDisplayName().contains(ChatColor.RED + "Kuudra Washing Machine 2.0")) {
                    value += item.getAmount()*1000;
                }
                if(item.getType() == Material.CORNFLOWER || item.getType() == Material.DANDELION) {
                    value += item.getAmount()*200;
                }
                if(item.getType() == Material.WITHER_ROSE || item.getType() == Material.BAMBOO) {
                    value += item.getAmount()*400;
                }
            } catch (Exception ignored) { }
        }

        return value;
    }

    public void craftNewOverview(Player clickedAt, Player opening) {
        Inventory inv = Bukkit.createInventory(null, 36, ChatColor.AQUA + clickedAt.getName() + "'s Overview");
        int i = 0;
        while(i < 36) {
            inv.setItem(i, MopsUtils.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
            i++;
        }

        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
        skullMeta.setOwner(clickedAt.getName());
        skullMeta.setDisplayName(MopsFiles.getRank(clickedAt).getPrefix() + clickedAt.getName() + MopsFiles.getBadge(clickedAt).getSymbol() + ChatColor.AQUA + "'s Profile");
        head.setItemMeta(skullMeta);

        ItemStack deliver = new ItemStack(Material.BARREL);
        ItemMeta deliverMeta = deliver.getItemMeta();
        deliverMeta.setDisplayName(MopsColor.BROWN.getColor() + "Deliver");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Send a Delivery to " + MopsFiles.getRank(clickedAt).getPrefix() + clickedAt.getName() + MopsFiles.getBadge(clickedAt).getSymbol());
        deliverMeta.setLore(lore);
        deliver.setItemMeta(deliverMeta);

        inv.setItem(13, head);
        inv.setItem(22, MopsUtils.createItem(Material.GOLD_INGOT, ChatColor.GOLD + "EnderChest Value: " + getEnderchestValue(clickedAt)));
        inv.setItem(35, deliver);

        overviewInventories.add(inv);
        inventoryName.put(inv, clickedAt.getUniqueId().toString());
        opening.openInventory(inv);
    }

    public void fillGamesGUI(Inventory inv, Player player) {
        int i = 0;
        while(i < 9) {
            inv.setItem(i, MopsUtils.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
            i++;
        }
        int i2 = 27;
        while(i2 < 36) {
            inv.setItem(i2, MopsUtils.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
            i2++;
        }
        inv.setItem(9, MopsUtils.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
        inv.setItem(17, MopsUtils.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
        inv.setItem(18, MopsUtils.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
        inv.setItem(26, MopsUtils.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));

        inv.setItem(10, MopsUtils.createItem(Material.GOLD_BLOCK, ChatColor.GOLD + "Spawn"));
        inv.setItem(11, MopsUtils.createItem(Material.CALCITE, ChatColor.WHITE + "MopsPVP"));
        inv.setItem(12, MopsUtils.createItem(Material.RED_WOOL, ChatColor.RED + "WoolBattle"));
        inv.setItem(13, MopsUtils.createItem(Material.LIGHT_BLUE_SHULKER_BOX, ChatColor.AQUA + "Kits"));
        inv.setItem(14, MopsUtils.createItem(Material.MELON, ChatColor.GREEN + "Market"));
        inv.setItem(15, MopsUtils.createItem(Material.GREEN_TERRACOTTA, ChatColor.DARK_GREEN + "Missions"));
        inv.setItem(16, MopsUtils.createItem(Material.LAPIS_BLOCK, ChatColor.BLUE + "Smelter"));

        if(MopsFiles.getWasAtPigeon(player)) {
            ItemStack item = MopsUtils.createCustomHead("b7ea4c017e3456cf09a5c263f34d3cc5f41577b74d60f6f8196c60e07f8c5a96");
            inv.setItem(19, MopsUtils.renameItem(item, ChatColor.AQUA + "Lonely Pigeon's Shack"));
        } else {
            ItemStack item = MopsUtils.createCustomHead("974fe9cb80029d66345277aa560d41ef1030962b7f29abf23961d9eba84250a3");
            inv.setItem(19, MopsUtils.renameItem(item, ChatColor.DARK_GRAY + "Locked Destination"));
        }
    }


    public void fillCosmeticSelector(Inventory inv, Player player) {
        int i = 0;
        while(i < 27) {
            inv.setItem(i, MopsUtils.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
            i++;
        }

        ItemStack effects = MopsUtils.createItem(Material.GOLDEN_CARROT, ChatColor.GOLD + "Effects");
        ItemMeta effectsMeta = effects.getItemMeta();
        effectsMeta.setLore(new ArrayList<>(Collections.singleton(ChatColor.GRAY + "Selected Effect: NONE")));
        effectsMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        effects.setItemMeta(effectsMeta);

        ItemStack auras = MopsUtils.createItem(Material.MUSIC_DISC_5, ChatColor.DARK_AQUA + "Auras");
        ItemMeta auraMeta = auras.getItemMeta();
        auraMeta.setLore(new ArrayList<>(Collections.singleton(ChatColor.GRAY + "Selected Aura: " + aura.get(player).getName())));
        auraMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        auras.setItemMeta(auraMeta);

        inv.setItem(12, effects);
        inv.setItem(14, auras);
    }


    public void fillAurasGUI(Inventory inv, Player player) {
        int i = 0;
        while(i < 9) {
            inv.setItem(i, MopsUtils.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
            i++;
        }
        int i2 = 27;
        while(i2 < 36) {
            inv.setItem(i2, MopsUtils.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
            i2++;
        }
        inv.setItem(9, MopsUtils.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
        inv.setItem(17, MopsUtils.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
        inv.setItem(18, MopsUtils.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
        inv.setItem(26, MopsUtils.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));

        ItemStack emeraldItem = new ItemStack(Material.EMERALD_BLOCK);
        ItemMeta emeraldMeta = emeraldItem.getItemMeta();
        emeraldMeta.setDisplayName(ChatColor.GREEN + "Emerald Cube");
        List<String> emeraldLore = new ArrayList<>();
        emeraldLore.add(ChatColor.GREEN + "Spawns a large emerald cube around you.");
        emeraldMeta.setLore(emeraldLore);
        emeraldItem.setItemMeta(emeraldMeta);

        boolean andromedaBool = MopsFiles.getBadge(player) == MopsBadge.SILLY;
        ChatColor andromedaColor = ChatColor.RED;
        if(andromedaBool) {
            andromedaColor = ChatColor.GREEN;
        }
        ItemStack andromedaItem = new ItemStack(Material.FIRE_CHARGE);
        ItemMeta andromedaMeta = andromedaItem.getItemMeta();
        andromedaMeta.setDisplayName(andromedaColor + "Andromeda");
        List<String> andromedaLore = new ArrayList<>();
        andromedaLore.add(ChatColor.GOLD + "Spawns a large planet around you.");
        andromedaLore.add(" ");
        andromedaLore.add(ChatColor.GOLD + "⚡ Silly " + ChatColor.GRAY + "club members only aura.");
        if(!andromedaBool) {
            andromedaLore.add(ChatColor.RED + "You can't use this!");
        }
        andromedaMeta.setLore(andromedaLore);
        andromedaItem.setItemMeta(andromedaMeta);


        boolean infinityBool = MopsFiles.getBadge(player) == MopsBadge.STAFF || player.getName().equals("SirCat07");
        ChatColor infinityColor = ChatColor.RED;
        if(infinityBool) {
            infinityColor = ChatColor.GREEN;
        }
        ItemStack infinityItem = new ItemStack(Material.MUSIC_DISC_5);
        ItemMeta infinityMeta = infinityItem.getItemMeta();
        infinityMeta.setDisplayName(infinityColor + "Infinity");
        infinityMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        List<String> infinityLore = new ArrayList<>();
        infinityLore.add(ChatColor.DARK_AQUA + "Truly Infinite.");
        infinityLore.add(" ");
        infinityLore.add(ChatColor.BLUE + "⭐ Staff " + ChatColor.GRAY + "members only aura.");
        if(!infinityBool) {
            infinityLore.add(ChatColor.RED + "You can't use this!");
        }
        infinityMeta.setLore(infinityLore);
        infinityItem.setItemMeta(infinityMeta);

        ItemStack resetItem = new ItemStack(Material.BARRIER);
        ItemMeta resetMeta = resetItem.getItemMeta();
        resetMeta.setDisplayName(ChatColor.RED + "Reset");
        resetMeta.setLore(new ArrayList<String>(Collections.singletonList(ChatColor.GRAY + "Resets your auras.")));
        resetItem.setItemMeta(resetMeta);

        inv.setItem(10, emeraldItem);
        inv.setItem(11, andromedaItem);
        inv.setItem(12, infinityItem);
        inv.setItem(35, resetItem);
    }

    public void fillEffectsGUI(Inventory inv, Player player) {
        int i = 0;
        while(i < 9) {
            inv.setItem(i, MopsUtils.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
            i++;
        }
        int i2 = 27;
        while(i2 < 36) {
            inv.setItem(i2, MopsUtils.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
            i2++;
        }
        inv.setItem(9, MopsUtils.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
        inv.setItem(17, MopsUtils.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
        inv.setItem(18, MopsUtils.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
        inv.setItem(26, MopsUtils.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));

        ItemStack resetItem = new ItemStack(Material.BARRIER);
        ItemMeta resetMeta = resetItem.getItemMeta();
        resetMeta.setDisplayName(ChatColor.RED + "Reset");
        resetMeta.setLore(new ArrayList<>(Collections.singletonList(ChatColor.GRAY + "Resets your effects.")));
        resetItem.setItemMeta(resetMeta);

        inv.setItem(35, resetItem);
    }

    public void fillProfileMenu(Inventory inv, Player player) {
        int i = 0;
        while(i < 27) {
            inv.setItem(i, MopsUtils.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
            i++;
        }

        inv.setItem(11, MopsUtils.createItem(Material.ENDER_CHEST, ChatColor.DARK_GREEN + "EnderChest Shortcut"));


        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
        skullMeta.setOwner(player.getName());
        skullMeta.setDisplayName(MopsFiles.getRank(player).getPrefix() + player.getName() + MopsFiles.getBadge(player).getSymbol());

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Rank: " + MopsFiles.getRank(player).getScoreboardText() + ChatColor.GRAY + "  |  Badge: " + MopsFiles.getBadge(player).getName());
        lore.add(ChatColor.GRAY + "Coins: " + ChatColor.GOLD + MopsFiles.getCoins(player));

        skullMeta.setLore(lore);
        head.setItemMeta(skullMeta);

        inv.setItem(13, head);


        ItemStack language = MopsUtils.createCustomHead(MopsFiles.getLanguage(player).getHeadID());
        ItemMeta languageMeta = language.getItemMeta();
        languageMeta.setDisplayName(ChatColor.GRAY + "Language: " + MopsFiles.getLanguage(player).getName());
        List<String> languageLore = new ArrayList<>();

        List<Language> languages = new ArrayList<>(Arrays.asList(Language.values()));
        for(Language lang : languages) {
            if(lang != MopsFiles.getLanguage(player)) {
                languageLore.add(ChatColor.DARK_GRAY + "- " + ChatColor.stripColor(lang.getName()));
            }
        }

        languageLore.add(" ");
        languageLore.add(ChatColor.GREEN + "Click to cycle!");
        languageMeta.setLore(languageLore);
        language.setItemMeta(languageMeta);

        inv.setItem(15, language);
    }

    public void fillDeliveryInventory(Inventory inv, Player player, UUID whosDeliveries) {
        int t = 0;
        while(t < 9) {
            inv.setItem(t, MopsUtils.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
            t++;
        }
        int b = 36;
        while(b < 45) {
            inv.setItem(b, MopsUtils.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
            b++;
        }
        inv.setItem(9, MopsUtils.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
        inv.setItem(17, MopsUtils.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
        inv.setItem(18, MopsUtils.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
        inv.setItem(26, MopsUtils.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
        inv.setItem(27, MopsUtils.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
        inv.setItem(35, MopsUtils.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));

        List<Delivery> deliveries = MopsFiles.getDeliveries(whosDeliveries);

        int i = 0;
        while(i < deliveries.size()) {
            Delivery delivery = deliveries.get(i);
            ItemStack deliveredItem = delivery.getDeliveredItem();

            int number = i+1;
            String zeroInfront = "";
            if(number < 10) {
                zeroInfront = "0";
            }

            ItemStack packageItem = MopsUtils.createItem(Material.BARREL, MopsColor.BROWN.getColor() + "Package #" + zeroInfront + number);
            ItemMeta packageMeta = packageItem.getItemMeta();
            List<String> lore = new ArrayList<>();

            String senderName = MopsFiles.getRank(delivery.getSender()).getPrefix() + Bukkit.getOfflinePlayer(delivery.getSender()).getName();
            String receiverName = MopsFiles.getRank(delivery.getReceiver()).getPrefix() + Bukkit.getOfflinePlayer(delivery.getReceiver()).getName();

            if(delivery.getSender() == player.getUniqueId()) {
                senderName = ChatColor.AQUA + "You";
                player.sendMessage("akmfgjmd");
                player.sendMessage(player.getUniqueId());
            }
            if(delivery.getReceiver() == player.getUniqueId()) {
                receiverName = ChatColor.AQUA + "You";
                player.sendMessage("akmfgjmd 3");
                player.sendMessage(player.getUniqueId());
            }

            player.sendMessage(player.getUniqueId());

            lore.add(" ");
            lore.add(ChatColor.GRAY + "Item: " + ChatColor.WHITE + deliveredItem.getItemMeta().getDisplayName() + ChatColor.DARK_GRAY + " x" + deliveredItem.getAmount());
            lore.add(ChatColor.GRAY + "Sender: " + senderName);
            lore.add(ChatColor.GRAY + "Receiver: " + receiverName);
            lore.add(" ");
            lore.add(ChatColor.YELLOW + "Left-Click to claim" + ChatColor.GOLD + " | " + ChatColor.YELLOW + "Right-Click to reject");
            lore.add(ChatColor.DARK_GRAY + "Delivery ID: " + delivery.getDeliveryID());

            packageMeta.setLore(lore);
            packageItem.setItemMeta(packageMeta);

            if(i <= 6) {
                inv.setItem(i+10, packageItem);
            } else if(i <= 13) {
                inv.setItem(i + 12, packageItem);
            } else if(i <= 20) {
                inv.setItem(i+14, packageItem);
            }
            i++;
        }
    }

    public void fillDeliveryItemInsert(Inventory inv, ItemStack item, boolean itemInserted) {
        int b = 0;
        while(b < 27) {
            inv.setItem(b, MopsUtils.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
            b++;
        }
        inv.setItem(13, item);

        if(itemInserted) {
            try {
                ItemStack itemStack = MopsUtils.createItem(Material.LIME_STAINED_GLASS_PANE, ChatColor.GREEN + "^ " + item.getItemMeta().getDisplayName() + ChatColor.GREEN + " ^");
                MopsUtils.addLore(itemStack, new String[]{ChatColor.GRAY + "Delivery cost: " + ChatColor.GOLD + "2", ChatColor.GREEN + "Click to deliver!"});

                inv.setItem(22, itemStack);
            } catch (Exception ignored) { }
        } else {
            ItemStack itemStack = MopsUtils.createItem(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + "^ Insert Item ^");
            MopsUtils.addLore(itemStack, new String[] {ChatColor.RED + "Insert an item you want to deliver above."});

            inv.setItem(22, itemStack);
        }
    }

    public void spawnTarget(boolean first) {
        boolean spawn = true;
        for(Entity entity : mainworld.getEntities()) {
            if (entity.getScoreboardTags().contains("slow")) {
                spawn = false;
                break;
            }
        }

        if(spawn) {

            int random = (int) (Math.random() * (2 + 1)) + 1;
            int random2 = (int) (Math.random() * (2 + 1)) + 1;
            Location loc = new Location(mainworld, -43.5, 9.5, -177);

            if (random == 1) {
                loc.setZ(-186);
            }
            if (random2 == 1) {
                loc.setY(7.5);
            }

            loc.setYaw(90);

            ArmorStand target = (ArmorStand) mainworld.spawnEntity(loc, EntityType.ARMOR_STAND);
            target.setInvisible(true);
            target.setGravity(false);

            if (first) {
                target.setCustomNameVisible(true);
                target.setCustomName(ChatColor.RED + "Shoot the Target!");

                target.getScoreboardTags().add("slow");
            }

            target.setHeadPose(new EulerAngle(Math.toRadians(180), 0, 0));

            target.setHelmet(new ItemStack(Material.TARGET));

            target.addScoreboardTag("target");

            if (random == 1) {
                target.addScoreboardTag("left");
            } else {
                target.addScoreboardTag("right");
            }
        }
    }

    public void spawnMelon(boolean named) {
        int randomX = (int) (Math.random() * (-49 + 57)) + -57;
        int randomZ = (int) (Math.random() * (-169 + 173)) + -173;
        Location loc = new Location(mainworld, randomX+0.5, 6.5, randomZ);

        int randomYaw = (int) (Math.random() * (360 + -180)) + -180;
        loc.setYaw(randomYaw);

        ArmorStand melon = (ArmorStand) mainworld.spawnEntity(loc, EntityType.ARMOR_STAND);
        melon.setInvisible(true);

        if(named) {
            melon.setCustomNameVisible(true);
            melon.setCustomName(ChatColor.GREEN + "Shoot the Melon!");
        }

        melon.setHeadPose(new EulerAngle(Math.toRadians(180), 0, 0));

        melon.setHelmet(MopsUtils.createCustomHead("351883d6b7fa42dd0cf0d9cd999615a8540bc07c6fcd5be5dbea5617fd139fd4"));

        melon.addScoreboardTag("gameMelon");

        int randomVectorY = (int) (Math.random() * (20));

        melon.setVelocity(new Vector(0, 1 + melonDifficulty + (randomVectorY/100), 0));
    }

    public void startDuck(Player player) {
        duckActive = true;
        duckPlayer = player;
        duckPoints = 0;
        duckDifficulty = 1;

        spawnTarget(true);

        Location newDestination = new Location(mainworld, -51, 7, -181.0);
        newDestination.setYaw(-90);
        player.teleport(newDestination);

        player.getInventory().addItem(new Items().bow());
        ItemStack arrow = new Items().arrow();

        if(player.getInventory().getItem(17) == null) {
            player.getInventory().setItem(17, arrow);
        } else {
            ItemStack item = player.getInventory().getItem(17);
            player.getInventory().setItem(17, arrow);
            player.getInventory().addItem(item);
        }

        for(Player onlinePlayers : Bukkit.getOnlinePlayers()) {
            player.hidePlayer(this, onlinePlayers);
        }
    }

    public void stopDuck() {
        duckActive = false;
        duckStrikes = 0;
        duckPlayer.getInventory().remove(new Items().bow());
        duckPlayer.getInventory().remove(new Items().arrow());

        for(Player onlinePlayers : Bukkit.getOnlinePlayers()) {
            duckPlayer.showPlayer(this, onlinePlayers);
        }

        for(Entity entity : mainworld.getEntities()) {
            if(entity.getScoreboardTags().contains("target")) {
                entity.remove();

                for(Entity strikeCounter : mainworld.getEntities()) {
                    int i = 0;
                    while (i < 5) {
                        if (strikeCounter.getScoreboardTags().contains("duckStrike" + i)) {
                            ItemFrame frame = (ItemFrame) strikeCounter;
                            frame.setItem(new ItemStack(Material.AIR));
                        }
                        i++;
                    }
                }
            }
        }
    }

    public void startMelon(Player player) {
        melonActive = true;
        melonPlayer = player;
        melonPoints = 0;
        melonDifficulty = 0;

        spawnMelon(true);

        Location newDestination = new Location(mainworld, -53.0, 7, -179);
        player.teleport(newDestination);

        player.getInventory().addItem(new Items().bow());
        ItemStack arrow = new Items().arrow();

        if(player.getInventory().getItem(17) == null) {
            player.getInventory().setItem(17, arrow);
        } else {
            ItemStack item = player.getInventory().getItem(17);
            player.getInventory().setItem(17, arrow);
            player.getInventory().addItem(item);
        }

        for(Player onlinePlayers : Bukkit.getOnlinePlayers()) {
            player.hidePlayer(this, onlinePlayers);
        }
    }

    public void stopMelon() {
        melonActive = false;
        melonTimer = 60;
        melonPlayer.getInventory().remove(new Items().bow());
        melonPlayer.getInventory().remove(new Items().arrow());

        for(Player onlinePlayers : Bukkit.getOnlinePlayers()) {
            melonPlayer.showPlayer(this, onlinePlayers);
        }

        for(Entity entity : mainworld.getEntities()) {
            if(entity.getScoreboardTags().contains("gameMelon")) {
                entity.remove();
            }
        }
    }

    public static int getAmount(ItemStack[] inventory, ItemStack item) {
        if (item == null)
            return 0;
        int itemCount = 0;
        for (ItemStack slot : inventory) {
            if (slot == null || !slot.isSimilar(item))
                continue;
            itemCount += slot.getAmount();
        }
        return itemCount;
    }
}
