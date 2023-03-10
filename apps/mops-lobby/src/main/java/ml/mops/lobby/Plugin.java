package ml.mops.lobby;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import ml.mops.base.commands.Commands;
import ml.mops.network.MopsBadge;
import ml.mops.network.MopsRank;
import ml.mops.utils.MopsUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.block.Block;
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
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
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
    HashMap<Player, Integer> woolbattleDogeDialogue = new HashMap<>();
    HashMap<Player, Integer> pigeonDialogue = new HashMap<>();
    HashMap<Player, Integer> realPlantDialogue = new HashMap<>();

    HashMap<String, Integer> rawCoins = new HashMap<>();
    HashMap<Player, Integer> coins = new HashMap<>();
    HashMap<String, MopsRank> rank = new HashMap<>();
    HashMap<String, MopsBadge> badge = new HashMap<>();

    HashMap<Player, String> aura = new HashMap<>();
    HashMap<Player, Integer> auraTimer = new HashMap<>();
    HashMap<Player, Double> auraRadius = new HashMap<>();

    float rgb = 0;
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

    Inventory mapGUI = new MapGUI().getInventory();
    Inventory gamesGUI = new GamesGUI().getInventory();
    Inventory effectsGUI = new ParticleGUI().getInventory();

    ArmorStand ball;


    @Override
    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        World mainworld = Bukkit.getServer().getWorlds().get(0);

        for(Entity entity : mainworld.getEntities()) {
            if(entity.getScoreboardTags().contains("killOnDisable")) {
                entity.remove();
            }
        }

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
        
        usables.add(new Location(mainworld, -77, 9, -157));
        usables.add(new Location(mainworld, -95, 10, -170));
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

        vendingButtons.add(new Location(mainworld, -92, 9, -194));
        vendingButtons.add(new Location(mainworld, -77, 9, -192));


        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for(Player player : Bukkit.getServer().getOnlinePlayers()) {
                Scoreboard lobbyscoreboard = new LobbyScoreboard().generateLobbyScoreboard(player, mainworld.getTime(), coins, rank);
                player.setScoreboard(lobbyscoreboard);

                if(auraTimer.get(player) != 0) {
                    auraTimer.put(player, auraTimer.get(player)-1);
                }

                Calendar calendar = Calendar.getInstance();
                if(calendar.get(Calendar.MONTH) == Calendar.DECEMBER || calendar.get(Calendar.MONTH) == Calendar.JANUARY) {
                    player.getWorld().spawnParticle(Particle.SNOWFLAKE, player.getLocation().add(0, 7, 0), 450, 15, 6, 15, 0);
                }
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
        }, 0L, 10L);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            Calendar calendar = Calendar.getInstance();
            Date date = calendar.getTime();

            long seconds = date.getSeconds() + (date.getMinutes() * 60L) + (date.getHours() * 3600L);
            long ticks = (long) (seconds * 0.277778);

            mainworld.setTime(ticks + 21000);

            if(calendar.get(Calendar.MONTH) == Calendar.DECEMBER || calendar.get(Calendar.MONTH) == Calendar.JANUARY) {
                mainworld.setStorm(true);
            }
        }, 0L, 1200L);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for(Player player : Bukkit.getOnlinePlayers()) {
                if(auraTimer.get(player) == 0) {
                    if(aura.get(player).equals("cube")) {
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
                    if(aura.get(player).equals("andromeda")) {
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
                    if(aura.get(player).equals("infinity")) {
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

        String[] coinList = MopsUtils.readFile("D:\\servers\\MopsNetwork\\coins.txt").split("\n");
        for (String coinRow : coinList) {
            String[] string = coinRow.split(":");
            rawCoins.put(string[0], Integer.parseInt(string[1]));
        }
        String[] rankList = MopsUtils.readFile("D:\\servers\\MopsNetwork\\ranks.txt").split("\n");
        for (String rankRow : rankList) {
            String[] string = rankRow.split(":");
            rank.put(string[0], MopsRank.valueOf(string[1]));
        }
        String[] badgeList = MopsUtils.readFile("D:\\servers\\MopsNetwork\\badges.txt").split("\n");
        for (String badgeRow : badgeList) {
            String[] string = badgeRow.split(":");
            badge.put(string[0], MopsBadge.valueOf(string[1]));
        }

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

        try {
            for(Player player : coins.keySet()) {
                rawCoins.put(player.getName(), coins.get(player));
            }
            StringBuilder largeText = new StringBuilder();
            for(String name : rawCoins.keySet()) {
                largeText.append(name).append(":").append(rawCoins.get(name)).append("\n");
            }
            MopsUtils.writeFile("D:\\servers\\MopsNetwork\\coins.txt", largeText.toString());
        } catch (Exception ignored) { }

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

        if(command.getName().equals("e")) {
            switch (args[0]) {
                case "compass" -> player.getInventory().addItem(new Items().compass());
                case "effects" -> player.getInventory().addItem(new Items().effects());
                case "mopscoin" -> player.getInventory().addItem(new Items().mopsCoin());
                case "washingmachine" -> player.getInventory().addItem(new Items().kuudraWashingMachine());
                case "chemistryset" -> player.getInventory().addItem(new Items().mopsChemistrySet());
                case "rulebreaker" -> player.getInventory().addItem(new Items().ruleBreaker());
            }
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

        if(auraTimer.get(player) != 2) {
            if(event.getTo().getX() != event.getFrom().getX() || event.getTo().getY() != event.getFrom().getY() || event.getTo().getZ() != event.getFrom().getZ()) {
                auraTimer.put(player, 2);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if(rank.get(player.getName()).getPermLevel() < 10) {
            event.setCancelled(true);
        }
    }


    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if(rank.get(player.getName()).getPermLevel() < 10) {
            event.setCancelled(true);
        }
    }


    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();

        try {
            if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK || action == Action.LEFT_CLICK_BLOCK || action == Action.LEFT_CLICK_AIR) {
                ItemStack itemInHand = player.getItemInHand();
                if (itemInHand.getItemMeta().getDisplayName().equals(ChatColor.GRAY + "Compass")) {
                    player.openInventory(gamesGUI);
                    event.setCancelled(true);
                }
                if (itemInHand.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Effects")) {
                    player.openInventory(effectsGUI);
                    event.setCancelled(true);
                }
                if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
                    if (itemInHand.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "MopsCoin")) {
                        itemInHand.setAmount(itemInHand.getAmount() - 1);
                        coins.put(player, coins.get(player) + 1);

                        event.setCancelled(true);
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
                // ????????????????
                if(atmButtons.contains(event.getClickedBlock().getLocation())) {
                    if(coins.get(player) != 0) {
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
                        player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_BREAK, 1, 2);

                        coins.put(player, coins.get(player) - 1);
                        player.getInventory().addItem(new Items().mopsCoin());
                    } else {
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 0);
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 2);

                        player.sendMessage(ChatColor.RED + "No coins left!");
                    }
                    rawCoins.put(player.getName(), coins.get(player));
                }

                // ??????????
                if(event.getClickedBlock().getLocation().equals(new Location(player.getWorld(), -82, 10, -216))) {
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
                    player.playSound(player.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 0.3F, 1);
                    player.playSound(player.getLocation(), Sound.ITEM_FIRECHARGE_USE, 0.3F, 1);

                    player.sendMessage(ChatColor.GRAY + "add furnace later plslssl");
                }

                // ??????????????
                if(event.getClickedBlock().getLocation().equals(new Location(player.getWorld(), -84, 9, -184))) {
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
                    player.sendMessage(ChatColor.AQUA + "Our Discord: https://discord.gg/pGscG66pze");
                }
                // ????????
                if(event.getClickedBlock().getLocation().equals(new Location(player.getWorld(), -84, 9, -185))) {
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
                    player.sendMessage(ChatColor.RED + "Our Youtube Channel: https://www.youtube.com/channel/UCmIrl7QQzVoVX-jeFNMkykg");
                }

                // ???????????? ??????????
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

                // ????????????????????
                if(event.getClickedBlock().getLocation().equals(new Location(player.getWorld(), -104, 12, -181))) {
                    ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
                    BookMeta bookMeta = (BookMeta) book.getItemMeta();
                    bookMeta.setAuthor(ChatColor.DARK_AQUA + "SirCat07");
                    bookMeta.setTitle("1000 ?? 1 ???????? ?????? ??????????????");

                    ArrayList<String> pages = new ArrayList<>();

                    pages.add(0, "1000 ?? 1 ???????? ?????? ??????????????.");
                    pages.add(1, "1 ????????: ???????????? ?????????????? - ????????????-????????.");
                    pages.add(2, "2 ????????: ?????????????????? ?? ?????????????? ?????? ?? ???????????????? ??????????.");
                    pages.add(3, "3 ????????: ?????????????? ???????? ?????????? ?????????????????? ?? ???????????? ??????????. ?????????? ?????????????? ???????? ?????????????????? ?? ???????????? ??????????, ???? ?????? ?????????? ???????? ?????? ?? ???????????? ?? ?????? ???????????????????? ??????????????????????");
                    pages.add(4, "4 ????????: ?????????????? ?????????? ?????????? ?? ??????????????.");
                    pages.add(5, "5 ????????: ???????????? ?????????????? ?????????? ???????????? ??????????????, ?????????? ?????? ???????????????????????? ??????????. ?????????? ???????????? ???????? ?? ?????? ?? ??????????????, ?????????????? ?????????? ???? ????????????.");
                    pages.add(6, "6 ????????: ?????????????? ???? ??????????-???? ?????????????? ???????????????????? ???????? ????????.");
                    pages.add(7, "7 ????????: ?????????????? - ???? ???????????????????????? ?????? ??????????????. ?????? ???? ??????????: ??????????????, ????????????????????, ????????????????, ????????????, ????????????, ??????????????, ??????????????.");
                    pages.add(8, "8 ????????: ?????????????? ???????????? ?????????? ??????????????.");
                    pages.add(9, "9 ????????: ?? ?????????????? ???????? ???????? ????????????????: ???????????? ?????????? ?? ?????????? ??????????????.");
                    pages.add(10, "10 ????????: ?????????????? ???????????????? ?????????????? ???????????????? ?????????????? ???????????????????? ?? ??????, ?????? ?????? ???????????? ???????? ?????????? ????-???? ????????, ?????? ?? ?????????????? ???????????????? ?????????????? ?????????????? ?? ?????????????????? ???? ????????????.");
                    pages.add(11, "11 ????????: ?? ?????????????? ???????????????? ?????????????? ?????????? ????????????.");
                    pages.add(12, "12 ????????: ?? ??????????????????, ?????????????? ?????????????? ?????????????????? ?????????? ??????????????.");
                    pages.add(13, "13 ????????: ?????????????? ?????????? ???????????? ???? ???????????????????? ???????????????????? ????????????.");
                    pages.add(14, "14 ????????: ?????????????? ?????????????????? ????????????.");
                    pages.add(15, "15 ????????: ?????????????? ?????????????????? ??????????.");
                    pages.add(16, "16 ????????: ?????????? ?????????????? ???????????? ?????????????????? ?? ?????????????? ????????, ?????????????? ???? ?????? ???????????? ?? ????????.");
                    pages.add(17, "17 ????????: ?????????? ?????????????? ???????????? ?????????????????? ?? ?????????????? ????????, ?????? ?????????????????? ???????????????? ???? ???????? ?? ??????????.");
                    pages.add(18, "18 ????????: ?????????? ?????????????? ?????????????????????????? ??????????????.");
                    pages.add(19, "19 ????????: ?????????? ????????????????, ?????????????? ?????????? ?????????? ???? ???????? ???????????? ?? ????????");
                    pages.add(20, "20 ????????: ?????????? ???????????????? ?????????? ?????????????? ???????????? ?????????????? ?????????????? ?? ??????????????????.");
                    pages.add(21, "21 ????????: ?????????? ?????????????? ????????, ?????? ??????????????????????.");
                    pages.add(22, "22 ????????: ?? ?????????????? ?????????????? ??????????.");
                    pages.add(23, "23 ????????: ?????????????? ???????? ?????????????? ?? ????????????.");
                    pages.add(24, "24 ????????: ?????????????? ?????????? ???????????? ?? ???????????? ???????????? ??????????????, ???????????? (????????????????).");
                    pages.add(25, "25 ????????: ???????? ?????????????? ???????????????? ?????????????? ??????????????.");
                    pages.add(26, "26 ????????: ?? ?????????????? ???????????? ??????????.");
                    pages.add(27, "27 ????????: ?????????? ????????, ?????? ?????????????? ?????????????????????? ??????????, ?????? ???? ???????????? ????????????????????.");
                    pages.add(28, "28 ????????: ???????? ???????????? ???????????????? ???? ?????????? ??????????????, ???? ???? ???????????? ???????????????????? ????????????????.");
                    pages.add(29, "29 ????????: ?? ?????????????? ???????????? ??????????.");
                    pages.add(30, "30 ????????: ?????????? ?????????????? ?????????? ???? ????????-????????, ???? ?????? ???????????????? ?? ??????????-???? ?????????????? ?????????? ?????????????? ???? ????????????.");
                    pages.add(31, "31 ????????: ???????? ?????????????? ?????????????? ????????????????, ???? ?? ?????? ?????????? ???????????????????? ????????????");
                    pages.add(32, "32 ????????: ?????????????? ?????????? ?????????????? ?? ???????????????? ??????????????, ?????????? ?????? ????????????.");
                    pages.add(33, "33 ????????: ?????????????? ???????????? ?????????????? ?? ??????????.");
                    pages.add(34, "34 ????????: ????????????, ?????????????? ???????????????? ?? ??????????????, ?????????? ?????? ???? ??????-???????? ????????????. ?????? ?????????? ???????????? ???? ??????-???????? ??????????????????????.");
                    pages.add(35, "35 ????????: ?????????????? ?????????? ?????????????????? ???? ???????????????????? ?????????????? ?? ??????????.");
                    pages.add(36, "36 ????????: ???? 03.05.2022, ?????????????? ???????????????? ???????????? ??????????????");
                    pages.add(37, "37 ????????: ???????????????????? ?????????? ??????????????.");
                    pages.add(38, "38 ????????: ?????????????? ?????????????? ????????????-???? ?????????? ???????????? ??????????, ?????? ??????????????.");
                    pages.add(39, "39 ????????: ?????????? ?????????????? ?????????????????? ???????????????? ????????????-????????????, ???? ????????????-??????????, ?????????????? ?????? ???????????????? ?? 99% ?????????????? ???????????????????????? ?? ??????????????.");
                    pages.add(40, "40 ????????: ?????????????? ?????????? ?????????????? ???? ????????????????.");
                    pages.add(41, "41 ????????: ?????????????? ?????????? ?????????? ??????????????????.");
                    pages.add(42, "42 ????????: ?????????? ?????????????? ????????, ?????? ???????????? ??????????????????????.");
                    pages.add(43, "43 ????????: ???????????? ?????????????? - ?????????????? ????????????????????!");
                    pages.add(44, "44 ????????: ?????????? ?????????????? ?????????????? ?? ????????, ???? ?????????????? ?????????? ???? ????????????-???? ????????????????????, ?????????????????????? ???? ????????, ???????????????? ???? ???????????????????? ?? ???????? ???????????????? ???????????????? ?? ????????.");
                    pages.add(45, "45 ????????: ?????????? ??????????, ?????????????? ???????????? ???????????????????? ?? ????????. ?????????????? ???????? ???????????? ???????????????????? ?? ????????. ?? ???????? ???????? ????????. ?????????????? ?????????????? ????. ?????? ???? ?????? ?????????????????? ????????????????. ???????????? ??????????, ?????? ?????? ???????????? ???????? ?? ???????? - ?????????? ???????????????????????? ??????????????.");
                    pages.add(46, "46 ????????: ????????????, ?????????? ?????????? ???????????????? ????????????, ?????????????? ???????? ???????????????? ???????????? ?? ????????????????.");
                    pages.add(47, "47 ????????: ????????????, ?????????? ?????????????? ???????????????? ???????? ???? ??????????, ?????????????? ???????? ???????????????? ????????.");
                    pages.add(48, "48 ????????: ?????????????? ???????????? ???????? ?? ??????.");
                    pages.add(49, "49 ????????: ?????????????? ?????????? ???????????? ??????????.");
                    pages.add(50, "50 ????????: ?????????????? ?????????? ?????????????? ???? ?????????? ???????????? ?? ?????????????????? ??????????????????????, ???? ???????????????? ?? ???? ?????????????? ??????????????.");
                    pages.add(51, "???????????????????? ???????????? \"1000 ?? 1 ???????? ?????? ??????????????\" ???? $1999.99!");
                    pages.add(52, " ");
                    pages.add(53, "*???????????????? ???????????????????? ????????????*");
                    pages.add(54, "1000 ????????: ??????????????.");

                    bookMeta.setPages(pages);

                    book.setItemMeta(bookMeta);

                    player.openBook(book);
                    player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1, 0);
                }
            }

            if(action == Action.RIGHT_CLICK_BLOCK || action == Action.LEFT_CLICK_BLOCK) {
                // ???????????? ????????
                if (event.getClickedBlock().getLocation().equals(new Location(player.getWorld(), -77, 9, -157))) {
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);

                    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 1, true, false));

                    Bukkit.getScheduler().runTaskLater(this, () -> {
                        Location loc = new Location(player.getWorld(), 151.5, 11.0, 147.5);
                        loc.setYaw(-90);
                        player.teleport(loc);
                    }, 5L);
                }
            }

            if(rank.get(player.getName()).getPermLevel() < 10) {
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
    public void onEntityInteract(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();

        Entity entity = event.getRightClicked();

        if(event.getHand().equals(EquipmentSlot.HAND)) {

            String dialogue = ChatColor.RED + "no dialogue found :p blehh (report to sircat)";
            boolean cancelDialogue = false;

            if (entity.getScoreboardTags().contains("armorStandHubNPC")) {
                event.setCancelled(true);
                if (entity.getScoreboardTags().contains("fishermanDogeNPC")) {
                    dialogue = "Giv me gfish please i want fis!!!1!!";
                    player.playSound(player.getLocation(), Sound.ENTITY_FISH_SWIM, 10, 2);
                }
                if (entity.getScoreboardTags().contains("builderDogeNPC")) {
                    dialogue = "hi this par t of hub not buildt please wait!!1";
                    player.playSound(player.getLocation(), Sound.ENTITY_WOLF_AMBIENT, 10, 2);
                }
                if (entity.getScoreboardTags().contains("tord")) {
                    List<String> dialogueList = new ArrayList<>();
                    dialogueList.add("hi im clown");
                    dialogueList.add("hi");

                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BANJO, 2, 0);

                    MopsUtils.sendRandomDialogueMessage(dialogueList, player, entity);
                    cancelDialogue = true;
                }
                if (entity.getScoreboardTags().contains("missionDogeNPC")) {
                    switch (missionDogeDialogue.get(player)) {
                        case 0 -> {
                            dialogue = "Wow.";
                            player.playSound(player.getLocation(), Sound.ENTITY_WOLF_GROWL, 10, 0);
                            player.playSound(player.getLocation(), Sound.ENTITY_WOLF_AMBIENT, 10, 0);

                            missionDogeDialogue.put(player, missionDogeDialogue.get(player) + 1);
                        }
                        case 1 -> {
                            dialogue = "The snow got so bad, we needed to clear it with a machine.";
                            player.playSound(player.getLocation(), Sound.ENTITY_WOLF_GROWL, 10, 0);
                            player.playSound(player.getLocation(), Sound.ENTITY_WOLF_AMBIENT, 10, 0);

                            missionDogeDialogue.put(player, missionDogeDialogue.get(player) + 1);
                        }
                        case 2 -> {
                            dialogue = "And it got stuck down here.";
                            player.playSound(player.getLocation(), Sound.ENTITY_WOLF_GROWL, 10, 0);
                            player.playSound(player.getLocation(), Sound.ENTITY_WOLF_AMBIENT, 10, 0);

                            missionDogeDialogue.put(player, missionDogeDialogue.get(player) + 1);
                        }
                        case 3 -> {
                            dialogue = "I still can't give you missions though.";
                            player.playSound(player.getLocation(), Sound.ENTITY_WOLF_GROWL, 10, 0);
                            player.playSound(player.getLocation(), Sound.ENTITY_WOLF_AMBIENT, 10, 0);

                            missionDogeDialogue.put(player, missionDogeDialogue.get(player) + 1);
                        }
                    }
                }
                if (entity.getScoreboardTags().contains("woolbattleDogeNPC")) {
                    switch (woolbattleDogeDialogue.get(player)) {
                        case 0 -> {
                            dialogue = "Hello, woolbattle is open for testing! Click again to join.";
                            player.playSound(player.getLocation(), Sound.ENTITY_WOLF_AMBIENT, 10, 2);

                            woolbattleDogeDialogue.put(player, woolbattleDogeDialogue.get(player) + 1);
                        }
                        case 1 -> {
                            cancelDialogue = true;

                            MopsUtils.sendToServer(this, player, "woolbattle");
                        }
                    }
                }
                if (entity.getScoreboardTags().contains("realPlantNPC")) {
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
                if (entity.getScoreboardTags().contains("pvpDogeNPC")) {
                    switch (deliveryDogeDialogue.get(player)) {
                        case 0 -> {
                            dialogue = "placeholder text.";
                            player.playSound(player.getLocation(), Sound.ENTITY_WOLF_AMBIENT, 10, 2);

                            deliveryDogeDialogue.put(player, deliveryDogeDialogue.get(player) + 1);
                        }
                        case 1 -> {
                            dialogue = "IM SILLY IM SILLY IM SILLY IM SILLY";
                            player.playSound(player.getLocation(), Sound.ENTITY_WOLF_AMBIENT, 10, 2);

                            deliveryDogeDialogue.put(player, deliveryDogeDialogue.get(player) - 1);
                        }
                    }
                }

                if (entity.getScoreboardTags().contains("lonelyPigeon")) {
                    dialogue = "hey " + ChatColor.GRAY + "(add quest later)";
                    player.playSound(player.getLocation(), Sound.ENTITY_PARROT_AMBIENT, 10, 2);
                }


                if(!entity.getScoreboardTags().contains("guideline") && !cancelDialogue) {
                    MopsUtils.sendDialogueMessage(dialogue, player, entity);
                } else if (entity.getScoreboardTags().contains("furnaceGuideline")) {
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

                player.playSound(player.getLocation(), Sound.ENTITY_CAT_AMBIENT, 2, 1);

                MopsUtils.sendRandomDialogueMessage(dialogueList, player, entity);
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
        aura.put(player, "none");

        Bukkit.getScheduler().runTaskLater(this, () -> {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 0);
            player.sendTitle(ChatColor.AQUA + "Welcome!", ChatColor.DARK_AQUA + "To MopsNetwork", 10, 30, 20);
            Bukkit.getScheduler().runTaskLater(this, () -> {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                Bukkit.getScheduler().runTaskLater(this, () -> {
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1, 2);
                }, 4L);
            }, 4L);
        }, 50L);

        String playerName = player.getName();
        if(rawCoins.get(playerName) == null) {
            coins.putIfAbsent(player, 0);
        } else {
            coins.putIfAbsent(player, rawCoins.get(playerName));
        }
        if(rank.get(playerName) == null) {
            rank.putIfAbsent(playerName, MopsRank.NONE);
        }
        if(badge.get(playerName) == null) {
            badge.putIfAbsent(playerName, MopsBadge.NONE);
        }

        player.setPlayerListName((rank.get(playerName).getPrefix() + " " + player.getName() + " " + badge.get(playerName).getSymbol()).trim());

        manipulateEnderChest(player);

        player.getInventory().clear();
        Items items = new Items();
        player.getInventory().setItem(0, items.compass());
        player.getInventory().setItem(8, items.effects());

        Location spawn = new Location(player.getWorld(), -106.0, 9, -186.0);
        spawn.setYaw(-90);

        player.teleport(spawn);

        deliveryDogeDialogue.putIfAbsent(player, 0);
        woolbattleDogeDialogue.putIfAbsent(player, 0);
        missionDogeDialogue.putIfAbsent(player, 0);
        pigeonDialogue.putIfAbsent(player, 0);

        player.setGameMode(GameMode.ADVENTURE);

        for(Player allPlayers : Bukkit.getOnlinePlayers()) {
            allPlayers.sendMessage( ChatColor.GOLD + "[MopsPVPs] " + ChatColor.YELLOW + player.getName() + " joined the game.");
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.setQuitMessage("");

        for(Player allPlayers : Bukkit.getOnlinePlayers()) {
            allPlayers.sendMessage( ChatColor.GOLD + "[MopsPVPs] " + ChatColor.YELLOW + player.getName() + " left the game. " + ChatColor.AQUA + ":(");
        }

        rawCoins.put(player.getName(), coins.get(player));
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();

        if(rank.get(player.getName()).getPermLevel() < 10) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {
        Player player = event.getPlayer();

        String chatRank = "";
        String name = player.getName();
        String badgeSymbol = "";
        String badgeDescription = "";

        if(rank.get(player.getName()) == MopsRank.NONE) {
            name = ChatColor.GRAY + player.getName();
        } else {
            chatRank = rank.get(player.getName()).getPrefix() + " ";
        }
        if(badge.get(player.getName()) != MopsBadge.NONE) {
            badgeSymbol = badge.get(player.getName()).getSymbol() + " ";
            badgeDescription = badge.get(player.getName()).getDescription();
        }

        String message = event.getMessage().replaceAll(":skull:", ChatColor.GRAY + "???" + ChatColor.RESET);
        if(rank.get(player.getName()).getPermLevel() > 10) {
            message = MopsUtils.convertColorCodes(message);
        }
        event.setCancelled(true);

        TextComponent preMessage = Component.text(chatRank + name);
        TextComponent messageBadge = Component.text(badgeSymbol).hoverEvent(Component.text(badgeDescription));
        TextComponent afterMessage = Component.text(ChatColor.RESET + ": " + message.trim());

        TextComponent fullMessage = preMessage.append(messageBadge).append(afterMessage);

        for(Player allPlayers : Bukkit.getOnlinePlayers()) {
            MopsUtils.sendTextComponentMessage(allPlayers, fullMessage);
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if(!event.getEntity().getScoreboardTags().contains("nohunger")) {
            event.setFoodLevel(20);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (event.getClickedInventory() == mapGUI) {
            event.setCancelled(true);
        }
        if (event.getClickedInventory() == gamesGUI) {
            event.setCancelled(true);
            if(event.getClick().isShiftClick()) {
                gamesGUI.remove(Material.COMPASS);
                gamesGUI.remove(Material.GLISTERING_MELON_SLICE);
            }
            try {
                event.getClickedInventory().getItem(event.getSlot()).getType();

                World world = event.getWhoClicked().getWorld();
                Location newDestination = new Location(world, -106.0, 9, -186.0);

                switch (event.getSlot()) {
                    case 0 -> {
                        newDestination.setYaw(-90);
                    }
                    case 1 -> {
                        newDestination = new Location(world, -101.0, 9, -177.0);
                        newDestination.setYaw(45);
                    }
                    case 2 -> newDestination = new Location(world, -70.5, 7, -185.5);
                    case 3 -> {
                        newDestination = new Location(world, -87.0, 9, -204.0);
                        newDestination.setYaw(180);
                    }
                    case 4 -> {
                        newDestination = new Location(world, -82.0, 9, -167.0);
                        newDestination.setYaw(-45);
                    }
                    case 5 -> {
                        newDestination = new Location(world, -60.5, 9, -200.5);
                        newDestination.setYaw(-90);
                    }
                    case 6 -> {
                        newDestination = new Location(world, -74.5, 9, -208.5);
                        newDestination.setYaw(180);
                    }
                }

                player.teleport(newDestination);
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 2);
            } catch (Exception ignored) { }
        }
        if (event.getClickedInventory() == effectsGUI) {
            event.setCancelled(true);
            if(event.getClick().isShiftClick()) {
                effectsGUI.remove(Material.COMPASS);
                effectsGUI.remove(Material.GLISTERING_MELON_SLICE);
            }
            try {
                event.getClickedInventory().getItem(event.getSlot()).getType();

                switch (event.getSlot()) {
                    case 0 -> {
                        aura.put(player, "cube");
                        player.playSound(player.getLocation(), Sound.ENTITY_ALLAY_ITEM_GIVEN, 1, 1);
                    }
                    case 1 -> {
                        if(badge.get(player.getName()) == MopsBadge.SILLY) {
                            aura.put(player, "andromeda");
                            player.playSound(player.getLocation(), Sound.BLOCK_FIRE_AMBIENT, 2, 2);
                            player.playSound(player.getLocation(), Sound.ENTITY_ALLAY_ITEM_GIVEN, 1, 1);
                        } else {
                            player.sendMessage(ChatColor.RED + "You can't use this effect.");
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 0);
                        }
                    }
                    case 2 -> {
                        if(badge.get(player.getName()) == MopsBadge.STAFF || player.getName().equals("SirCat07")) {
                            aura.put(player, "infinity");
                            player.playSound(player.getLocation(), Sound.ENTITY_ALLAY_ITEM_GIVEN, 1, 1);
                        } else {
                            player.sendMessage(ChatColor.RED + "You can't use this effect.");
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 0);
                        }
                    }
                    case 26 -> {
                        aura.put(player, "none");
                        auraRadius.put(player, 0.0);
                        player.playSound(player.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 2);
                        player.sendMessage(ChatColor.GREEN + "You reset your effects.");
                    }
                }
            } catch (Exception ignored) { }
        }

        if(event.getInventory().getType() == InventoryType.ENDER_CHEST) {
            if((event.getSlot() >= 5 && event.getSlot() <= 8) || (event.getSlot() >= 14 && event.getSlot() <= 17) || (event.getSlot() >= 23 && event.getSlot() <= 26)) {
                event.setCancelled(true);
            }
            manipulateEnderChest(player);
        }
    }

    @EventHandler
    public void onMoveItemEvent(InventoryMoveItemEvent event) {
        if(event.getItem().getType() == Material.COMPASS) {
            event.setCancelled(true);
        }
        if(event.getItem().getType() == Material.GLISTERING_MELON_SLICE) {
            event.setCancelled(true);
        }

        if(event.getDestination() == effectsGUI) {
            event.setCancelled(true);
        }
        if(event.getDestination() == gamesGUI) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if(event.getEntityType() == EntityType.PLAYER) {
            event.setDamage(0);
        } else {
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
            player.getEnderChest().setItem(16, MopsUtils.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
            player.getEnderChest().setItem(17, MopsUtils.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
            player.getEnderChest().setItem(23, MopsUtils.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
            player.getEnderChest().setItem(24, MopsUtils.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
            player.getEnderChest().setItem(25, MopsUtils.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
            player.getEnderChest().setItem(26, MopsUtils.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));

            if(player.getEnderChest().contains(Material.COMPASS)) {
                player.getEnderChest().remove(Material.COMPASS);
            }
            if(player.getEnderChest().contains(Material.GLISTERING_MELON_SLICE)) {
                player.getEnderChest().remove(Material.GLISTERING_MELON_SLICE);
            }
        }, 5L);
    }

    public int getEnderchestValue(Player player) {
        int value = getAmount(player.getEnderChest(), new Items().mopsCoin());

        try {
            for (ItemStack item : player.getEnderChest().getContents()) {
                if (item.getItemMeta().getDisplayName().equals(ChatColor.RED + "kuudra washing machine")) {
                    value += 1000;
                }
            }
        } catch (Exception ignored) { }

        return value;
    }

    public static int getAmount(Inventory inventory, ItemStack item) {
        if (item == null)
            return 0;
        int itemCount = 0;
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack slot = inventory.getItem(i);
            if (slot == null || !slot.isSimilar(item))
                continue;
            itemCount += slot.getAmount();
        }
        return itemCount;
    }
}
