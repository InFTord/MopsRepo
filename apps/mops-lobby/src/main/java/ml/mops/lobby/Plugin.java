package ml.mops.lobby;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import ml.mops.base.commands.Commands;
import ml.mops.network.MopsBadge;
import ml.mops.network.MopsRank;
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
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
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
    HashMap<Player, Integer> woolbattleDogeDialogue = new HashMap<>();
    HashMap<Player, Integer> pigeonDialogue = new HashMap<>();
    HashMap<Player, Integer> realPlantDialogue = new HashMap<>();

    HashMap<Player, ItemStack> enderChestBackpack = new HashMap<>();

    HashMap<Player, String> aura = new HashMap<>();
    HashMap<Player, Integer> auraTimer = new HashMap<>();
    HashMap<Player, Double> auraRadius = new HashMap<>();

    HashMap<Player, Inventory> gamesGUI = new HashMap<>();
    HashMap<Player, Inventory> effectsSelector = new HashMap<>();
    HashMap<Player, Inventory> effectsGUI = new HashMap<>();
    HashMap<Player, Inventory> auraGUI = new HashMap<>();
    List<Inventory> overviewInventories = new ArrayList<>();

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

    Inventory mapGUI = new MapGUI().getInventory();

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
                Scoreboard lobbyscoreboard = new LobbyScoreboard().generateLobbyScoreboard(player, mainworld.getTime());
                player.setScoreboard(lobbyscoreboard);

                if(auraTimer.get(player) != 0) {
                    auraTimer.put(player, auraTimer.get(player)-1);
                }

                Calendar calendar = Calendar.getInstance();
                if(calendar.get(Calendar.MONTH) == Calendar.DECEMBER || calendar.get(Calendar.MONTH) == Calendar.JANUARY) {
                    player.getWorld().spawnParticle(Particle.SNOWFLAKE, player.getLocation().add(0, 7, 0), 450, 15, 6, 15, 0);
                }

                player.getInventory().remove(Material.BROWN_STAINED_GLASS_PANE);
            }

            for(Entity entity : mainworld.getEntities()) {
                if(entity.getScoreboardTags().contains("snowCleaningDoge")) {
                    ArmorStand stand = (ArmorStand) entity;
                    snowDoge += 0.2;

                    double equation = Math.sin(snowDoge*2) * Math.cos(snowDoge*2) * 50;
                    int result = (int) Math.round(equation) + 265;
                    stand.setRightArmPose(new EulerAngle(Math.toRadians(result), Math.toRadians(320), Math.toRadians(150)));

                    mainworld.spawnParticle(Particle.SNOWBALL, entity.getLocation(), 10, 2, 1, 2);
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
            mainworld.spawnParticle(Particle.VILLAGER_HAPPY, new Location(mainworld, -102.9, 10.5, -181.5), 1, 0.05, 0.05, 0.05, 0);
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

        if(MopsFiles.getRank(player).getPermLevel() > 10) {
            if (command.getName().equals("e")) {
                switch (args[0]) {
                    case "compass" -> player.getInventory().addItem(new Items().compass());
                    case "effects" -> player.getInventory().addItem(new Items().effects());
                    case "mopscoin" -> player.getInventory().addItem(new Items().mopsCoin());
                    case "washingmachine" -> player.getInventory().addItem(new Items().kuudraWashingMachine());
                    case "chemistryset" -> player.getInventory().addItem(new Items().mopsChemistrySet());
                    case "rulebreaker" -> player.getInventory().addItem(new Items().ruleBreaker());
                }
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
        if(MopsFiles.getRank(player).getPermLevel() < 10) {
            event.setCancelled(true);
        }
    }


    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if(MopsFiles.getRank(player).getPermLevel() < 10) {
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
                    Inventory inv = Bukkit.createInventory(null, 36, "Select Your Destination");
                    fillGamesGUI(inv, player);

                    gamesGUI.put(player, inv);
                    player.openInventory(gamesGUI.get(player));
                    event.setCancelled(true);
                }
                if (itemInHand.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Effects")) {
                    Inventory inv = Bukkit.createInventory(null, 27, "Effects");
                    fillEffectSelector(inv);

                    effectsSelector.put(player, inv);
                    player.openInventory(effectsSelector.get(player));
                    event.setCancelled(true);
                }
                if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
                    boolean consume = true;
                    if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                        if(atmButtons.contains(event.getClickedBlock().getLocation())) {
                            consume = false;
                        }
                    }
                    if(consume) {
                        if (itemInHand.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "MopsCoin")) {
                            itemInHand.setAmount(itemInHand.getAmount() - 1);
                            MopsFiles.setCoins(player, MopsFiles.getCoins(player) + 1);
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
                            player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_BREAK, 1, 2);

                            event.setCancelled(true);
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
                            ChatColor.DARK_GREEN + " + " + ChatColor.BLACK + "EnderChest Back" + "\n" +
                            "    packs Fix" + "\n" +
                            ChatColor.DARK_GREEN + " + " + ChatColor.BLACK + "Added new NPC" + "\n" +
                            ChatColor.DARK_GREEN + " + " + ChatColor.BLACK + "Woolbattle now" + "\n" +
                            "    gives coins" + "\n" +
                            ChatColor.GRAY + " - " + ChatColor.BLACK + "Other Bug Fixes" + "\n" +
                            ChatColor.DARK_GREEN + " + " + ChatColor.BLACK + "SnowDoge animation" + "\n"
                    );
                    bookMeta.setPages(pages);

                    book.setItemMeta(bookMeta);

                    player.openBook(book);
                    player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1, 0);
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

                        MopsFiles.setPigeon(player, true);
                    }, 5L);
                }
            }

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
                Inventory inv = Bukkit.createInventory(null, 36, ChatColor.AQUA + clickedAt.getName() + "'s Overview");
                int i = 0;
                while(i < 36) {
                    inv.setItem(i, MopsUtils.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
                    i++;
                }

                ItemStack head = new ItemStack(Material.PLAYER_HEAD);
                SkullMeta meta = (SkullMeta) head.getItemMeta();
                meta.setOwner(ChatColor.AQUA + clickedAt.getName());
                meta.setDisplayName(ChatColor.YELLOW + clickedAt.getName() + "'s Profile");
                head.setItemMeta(meta);

                inv.setItem(13, head);
                inv.setItem(22, MopsUtils.createItem(Material.GOLD_INGOT, ChatColor.GOLD + "EnderChest Value: " + getEnderchestValue(clickedAt)));

                overviewInventories.add(inv);
                event.getPlayer().openInventory(inv);
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
                if (entity.getScoreboardTags().contains("fishermanDogeNPC")) {
                    dialogue = "Giv me gfish please i want fis!!!1!!";
                    player.playSound(player.getLocation(), Sound.ENTITY_FISH_SWIM, 10, 2);
                }
                if (entity.getScoreboardTags().contains("snowCleaningDoge")) {
                    dialogue = "i am cleaning snow i think";
                    player.playSound(player.getLocation(), Sound.ENTITY_WOLF_AMBIENT, 10, 2);
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

                            missionDogeDialogue.put(player, 0);
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
                if (entity.getScoreboardTags().contains("kitManager")) {
                    dialogue = "hi i manage kits";
                    player.playSound(player.getLocation(), Sound.ENTITY_FROG_AMBIENT, 2, 1);
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

                if(MopsUtils.sendRandomDialogueMessage(dialogueList, player, entity).equals("blehh")) {
                    player.playSound(player.getLocation(), Sound.ENTITY_FROG_TONGUE, 2, 1);
                }
                player.playSound(player.getLocation(), Sound.ENTITY_CAT_AMBIENT, 2, 1);
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

        player.setPlayerListName((MopsFiles.getRank(player).getPrefix() + player.getName() + MopsFiles.getBadge(player).getSymbol()).trim());

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
        realPlantDialogue.putIfAbsent(player, 0);
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

        String message = event.getMessage().replaceAll(":skull:", ChatColor.GRAY + "☠" + ChatColor.RESET);
        if(MopsFiles.getRank(player).getPermLevel() > 10) {
            message = MopsUtils.convertColorCodes(message);
        }
        event.setCancelled(true);

        for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if(message.toLowerCase(Locale.ROOT).contains(onlinePlayer.getName().toLowerCase(Locale.ROOT))) {
                message = message.replaceAll(onlinePlayer.getName().toLowerCase(Locale.ROOT), MopsFiles.getRank(onlinePlayer).getPrefix() + onlinePlayer.getName() + ChatColor.RESET);
                onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
            }
        }

        TextComponent preMessage = Component.text(MopsFiles.getRank(player).getPrefix() + player.getName());
        TextComponent messageBadge = Component.text(MopsFiles.getBadge(player).getSymbol()).hoverEvent(Component.text(MopsFiles.getBadge(player).getDescription()));
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
        if (event.getClickedInventory() == gamesGUI.get(player)) {
            event.setCancelled(true);
            if(event.getClick().isShiftClick()) {
                gamesGUI.get(player).remove(Material.COMPASS);
                gamesGUI.get(player).remove(Material.GLISTERING_MELON_SLICE);
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
                        newDestination = new Location(world, -87.0, 9, -204.0);
                        newDestination.setYaw(180);
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
                    if(MopsFiles.getPigeon(player)) {
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
        if (event.getClickedInventory() == effectsGUI.get(player)) {
            event.setCancelled(true);
        }
        if (event.getClickedInventory() == effectsSelector.get(player)) {
            event.setCancelled(true);

            if(event.getSlot() == 12) {
                Inventory inv = Bukkit.createInventory(null, 36, "Select Your Effect");

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
                auraGUI.get(player).remove(Material.COMPASS);
                auraGUI.get(player).remove(Material.GLISTERING_MELON_SLICE);
            }
            try {
                event.getClickedInventory().getItem(event.getSlot()).getType();

                switch (event.getSlot()) {
                    case 10 -> {
                        aura.put(player, "cube");
                        player.playSound(player.getLocation(), Sound.ENTITY_ALLAY_ITEM_GIVEN, 1, 1);
                    }
                    case 11 -> {
                        if(MopsFiles.getBadge(player) == MopsBadge.SILLY) {
                            aura.put(player, "andromeda");
                            player.playSound(player.getLocation(), Sound.BLOCK_FIRE_AMBIENT, 2, 2);
                            player.playSound(player.getLocation(), Sound.ENTITY_ALLAY_ITEM_GIVEN, 1, 1);
                        } else {
                            player.sendMessage(ChatColor.RED + "You can't use this effect.");
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 0);
                        }
                    }
                    case 12 -> {
                        if(MopsFiles.getBadge(player) == MopsBadge.STAFF || player.getName().equals("SirCat07")) {
                            aura.put(player, "infinity");
                            player.playSound(player.getLocation(), Sound.ENTITY_ALLAY_ITEM_GIVEN, 1, 1);
                        } else {
                            player.sendMessage(ChatColor.RED + "You can't use this effect.");
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 0);
                        }
                    }
                    case 35 -> {
                        aura.put(player, "none");
                        auraRadius.put(player, 0.0);
                        player.playSound(player.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 2);
                        player.sendMessage(ChatColor.GREEN + "You reset your effects.");
                    }
                }
            } catch (Exception ignored) { }
        }
        if (event.getClickedInventory() == effectsGUI.get(player)) {
            event.setCancelled(true);
            if(event.getClick().isShiftClick()) {
                effectsGUI.get(player).remove(Material.COMPASS);
                effectsGUI.get(player).remove(Material.GLISTERING_MELON_SLICE);
            }
            try {
                event.getClickedInventory().getItem(event.getSlot()).getType();

                switch (event.getSlot()) {
                    case 10 -> {
                        aura.put(player, "cube");
                        player.playSound(player.getLocation(), Sound.ENTITY_ALLAY_ITEM_GIVEN, 1, 1);
                    }
                    case 11 -> {
                        if(MopsFiles.getBadge(player) == MopsBadge.SILLY) {
                            aura.put(player, "andromeda");
                            player.playSound(player.getLocation(), Sound.BLOCK_FIRE_AMBIENT, 2, 2);
                            player.playSound(player.getLocation(), Sound.ENTITY_ALLAY_ITEM_GIVEN, 1, 1);
                        } else {
                            player.sendMessage(ChatColor.RED + "You can't use this effect.");
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 0);
                        }
                    }
                    case 12 -> {
                        if(MopsFiles.getBadge(player) == MopsBadge.STAFF || player.getName().equals("SirCat07")) {
                            aura.put(player, "infinity");
                            player.playSound(player.getLocation(), Sound.ENTITY_ALLAY_ITEM_GIVEN, 1, 1);
                        } else {
                            player.sendMessage(ChatColor.RED + "You can't use this effect.");
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 0);
                        }
                    }
                    case 35 -> {
                        aura.put(player, "none");
                        auraRadius.put(player, 0.0);
                        player.playSound(player.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 2);
                        player.sendMessage(ChatColor.GREEN + "You reset your effects.");
                    }
                }
            } catch (Exception ignored) { }
        }
        if(overviewInventories.contains(event.getClickedInventory())) {
            event.setCancelled(true);
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
                        ItemStack item = event.getCurrentItem();
                        ItemMeta meta = item.getItemMeta();
                        List<String> lore = meta.getLore();
                        lore.add(ChatColor.YELLOW + "Left-Click to open" + ChatColor.GOLD + " | " + ChatColor.YELLOW + "Right-Click to remove");
                        meta.setLore(lore);
                        item.setItemMeta(meta);
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

                        Inventory boxInv = box.getInventory();
                        Inventory newBoxInv = Bukkit.createInventory(null, 27, enderChestBackpack.get(player).getItemMeta().getDisplayName());
                        int i = 0;
                        while (i < boxInv.getSize()) {
                            newBoxInv.setItem(i, boxInv.getItem(i));
                        }

                        player.openInventory(newBoxInv);
                    }
                    if(event.isRightClick()) {
                        try {
                            event.getCursor();

                            enderChestBackpack.put(player, null);
                            event.setCancelled(false);

                            ItemStack item = event.getCurrentItem();
                            ItemMeta meta = item.getItemMeta();
                            List<String> lore = meta.getLore();
                            lore.remove(lore.size() - 1);
                            meta.setLore(lore);
                            item.setItemMeta(meta);

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

            if (event.getCurrentItem().getType() == Material.COMPASS) {
                event.setCancelled(true);
            }
            if (event.getCurrentItem().getType() == Material.GLISTERING_MELON_SLICE) {
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
                    }, 5L);
                }
            }
        } catch (Exception ignored) { }
    }

    @EventHandler
    public void onMoveItemEvent(InventoryMoveItemEvent event) {
        if(event.getItem().getType() == Material.COMPASS) {
            event.setCancelled(true);
        }
        if(event.getItem().getType() == Material.GLISTERING_MELON_SLICE) {
            event.setCancelled(true);
        }
        if(event.getItem().getType() == Material.BROWN_STAINED_GLASS_PANE) {
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

            if(player.getEnderChest().contains(Material.COMPASS)) {
                player.getEnderChest().remove(Material.COMPASS);
            }
            if(player.getEnderChest().contains(Material.GLISTERING_MELON_SLICE)) {
                player.getEnderChest().remove(Material.GLISTERING_MELON_SLICE);
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

//        try {
//            BlockStateMeta bsm = (BlockStateMeta) player.getEnderChest().getItem(16);
//            ShulkerBox box = (ShulkerBox) bsm.getBlockState();
//            ItemStack[] boxContents = box.getInventory().getContents();
//            add value here
//        } catch (Exception igonred) { }


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
        inv.setItem(16, MopsUtils.createItem(Material.LAPIS_BLOCK, ChatColor.BLUE + "MopsLobby Section 2"));

        if(MopsFiles.getPigeon(player)) {
            ItemStack item = MopsUtils.createCustomHead("b7ea4c017e3456cf09a5c263f34d3cc5f41577b74d60f6f8196c60e07f8c5a96");
            inv.setItem(19, MopsUtils.renameItem(item, ChatColor.AQUA + "Lonely Pigeon's Shack"));
        } else {
            ItemStack item = MopsUtils.createCustomHead("974fe9cb80029d66345277aa560d41ef1030962b7f29abf23961d9eba84250a3");
            inv.setItem(19, MopsUtils.renameItem(item, ChatColor.DARK_GRAY + "Locked Destination"));
        }
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
        ItemMeta resetMeta = infinityItem.getItemMeta();
        resetMeta.setDisplayName(ChatColor.RED + "Reset");
        resetMeta.setLore(new ArrayList<String>(Collections.singletonList(ChatColor.GRAY + "Resets your auras.")));
        resetItem.setItemMeta(resetMeta);

        inv.setItem(10, emeraldItem);
        inv.setItem(11, andromedaItem);
        inv.setItem(12, infinityItem);
        inv.setItem(35, resetItem);
    }

//    public void fillEffectsGUI(Inventory inv, Player player) {
//        int i = 0;
//        while(i < 9) {
//            inv.setItem(i, MopsUtils.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
//            i++;
//        }
//        int i2 = 27;
//        while(i2 < 36) {
//            inv.setItem(i2, MopsUtils.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
//            i2++;
//        }
//        inv.setItem(9, MopsUtils.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
//        inv.setItem(17, MopsUtils.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
//        inv.setItem(18, MopsUtils.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
//        inv.setItem(26, MopsUtils.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
//
//        ItemStack emeraldItem = new ItemStack(Material.EMERALD_BLOCK);
//        ItemMeta emeraldMeta = emeraldItem.getItemMeta();
//        emeraldMeta.setDisplayName(ChatColor.GREEN + "Emerald Cube");
//        List<String> emeraldLore = new ArrayList<>();
//        emeraldLore.add(ChatColor.GREEN + "Spawns a large emerald cube around you.");
//        emeraldMeta.setLore(emeraldLore);
//        emeraldItem.setItemMeta(emeraldMeta);
//
//        boolean andromedaBool = badge.get(player.getName()) == MopsBadge.SILLY;
//        ChatColor andromedaColor = ChatColor.RED;
//        if(andromedaBool) {
//            andromedaColor = ChatColor.GREEN;
//        }
//        ItemStack andromedaItem = new ItemStack(Material.FIRE_CHARGE);
//        ItemMeta andromedaMeta = andromedaItem.getItemMeta();
//        andromedaMeta.setDisplayName(andromedaColor + "Andromeda");
//        List<String> andromedaLore = new ArrayList<>();
//        andromedaLore.add(ChatColor.GOLD + "Spawns a large planet around you.");
//        andromedaLore.add(" ");
//        andromedaLore.add(ChatColor.GOLD + "⚡ Silly " + ChatColor.GRAY + "club members only aura.");
//        if(!andromedaBool) {
//            andromedaLore.add(ChatColor.RED + "You can't use this!");
//        }
//        andromedaMeta.setLore(andromedaLore);
//        andromedaItem.setItemMeta(andromedaMeta);
//
//
//        boolean infinityBool = badge.get(player.getName()) == MopsBadge.STAFF || player.getName().equals("SirCat07");
//        ChatColor infinityColor = ChatColor.RED;
//        if(infinityBool) {
//            infinityColor = ChatColor.GREEN;
//        }
//        ItemStack infinityItem = new ItemStack(Material.MUSIC_DISC_5);
//        ItemMeta infinityMeta = infinityItem.getItemMeta();
//        infinityMeta.setDisplayName(infinityColor + "Infinity");
//        infinityMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
//        List<String> infinityLore = new ArrayList<>();
//        infinityLore.add(ChatColor.DARK_AQUA + "Truly Infinite.");
//        infinityLore.add(" ");
//        infinityLore.add(ChatColor.BLUE + "⭐ Staff " + ChatColor.GRAY + "members only aura.");
//        if(!infinityBool) {
//            infinityLore.add(ChatColor.RED + "You can't use this!");
//        }
//        infinityMeta.setLore(infinityLore);
//        infinityItem.setItemMeta(infinityMeta);
//
//        ItemStack resetItem = new ItemStack(Material.BARRIER);
//        ItemMeta resetMeta = infinityItem.getItemMeta();
//        resetMeta.setDisplayName(ChatColor.RED + "Reset");
//        resetMeta.setLore(new ArrayList<String>(Collections.singletonList(ChatColor.GRAY + "Resets your auras.")));
//        resetItem.setItemMeta(resetMeta);
//
//        inv.setItem(10, emeraldItem);
//        inv.setItem(11, andromedaItem);
//        inv.setItem(12, infinityItem);
//        inv.setItem(35, resetItem);
//    }

    public void fillEffectSelector(Inventory inv) {
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
