package ml.mops.lobby;

//import club.minnced.discord.webhook.WebhookClient;
//import club.minnced.discord.webhook.send.WebhookEmbed;
//import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
//import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import ml.mops.base.commands.Commands;
import ml.mops.utils.MopsUtils;
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
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.List;

public class Plugin extends JavaPlugin implements Listener, CommandExecutor {

    HashMap<Player, Integer> pvpDogeDialogue = new HashMap<>();
    HashMap<Player, Integer> woolbattleDogeDialogue = new HashMap<>();
    HashMap<Player, Integer> pigeonDialogue = new HashMap<>();

    //doors n trapdoors n shit
    List<Location> flippable = new ArrayList<>();
    // atm or bank
    List<Location> atmButtons = new ArrayList<>();
    // chests
    List<Location> openables = new ArrayList<>();
    // note blok and button
    List<Location> usables = new ArrayList<>();

    @Override
    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        World mainworld = Bukkit.getServer().getWorlds().get(0);

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


        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for(Player player : Bukkit.getServer().getOnlinePlayers()) {
                Scoreboard lobbyscoreboard = new LobbyScoreboard().generateLobbyScoreboard(player, mainworld.getTime());
                player.setScoreboard(lobbyscoreboard);
                
                Calendar calendar = Calendar.getInstance();
                if(calendar.get(Calendar.MONTH) == Calendar.DECEMBER || calendar.get(Calendar.MONTH) == Calendar.JANUARY) {
                    player.getWorld().spawnParticle(Particle.SNOWFLAKE, player.getLocation().add(0, 7, 0), 450, 15, 6, 15, 0);
                }
            }
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



        WebhookClient client = WebhookClient.withUrl("https://discord.com/api/webhooks/983390269665865778/DzC0nsW5ge9Zl4mgoQQseOM26KMSfmgX-_gFlCLTMfOpLwxrK-5QbpFvEdQhVxY0GZ4x");

        WebhookEmbed embed = new WebhookEmbedBuilder()
                .setColor(Color.GREEN.asRGB())
                .setDescription("\uD83D\uDFE2 mopslobby запущен.")
                .build();
        client.send(embed);
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
            player.kickPlayer(ChatColor.YELLOW + "Server closed.\nShortly will be back on, maybe.");
        }

        WebhookClient client = WebhookClient.withUrl("https://discord.com/api/webhooks/983390269665865778/DzC0nsW5ge9Zl4mgoQQseOM26KMSfmgX-_gFlCLTMfOpLwxrK-5QbpFvEdQhVxY0GZ4x");

        WebhookEmbed embed = new WebhookEmbedBuilder()
                .setColor(Color.RED.asRGB())
                .setDescription("\uD83D\uDD34 mopslobby выключен.")
                .build();
        client.send(embed);
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

            // банкомат
            if(atmButtons.contains(event.getClickedBlock().getLocation())) {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
                player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_BREAK, 1, 2);

                player.getInventory().addItem(MopsUtils.addLore(MopsUtils.createItem(Material.GOLD_INGOT, ChatColor.GOLD + "MopsCoin", 1), new String[] {ChatColor.GRAY + "The main currency of MopsNetwork."}));
            }

            // печка
            if(event.getClickedBlock().getLocation().equals(new Location(player.getWorld(), -82, 10, -216))) {
                event.setCancelled(true);

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
                pages.add(54, "1000 факт: Астарта один из главных персонажей МопсПВП.");

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
                }, 5L);
            }
        }

        if(!player.getScoreboardTags().contains("admin")) {
            if (!flippable.contains(event.getClickedBlock().getLocation())) {
                if (!usables.contains(event.getClickedBlock().getLocation())) {
                    if (!openables.contains(event.getClickedBlock().getLocation())) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEntityInteract(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();

        Entity entity = event.getRightClicked();

        if(event.getHand().equals(EquipmentSlot.HAND)) {

            String dialogue = ChatColor.RED + "no dialogue found :p blehh (report to sircat)";
            boolean randomDialogue = false;

            if (entity.getScoreboardTags().contains("armorStandHubNPC")) {
                event.setCancelled(true);
                if (entity.getScoreboardTags().contains("missionDogeNPC")) {
                    dialogue = "Hi, i can't currently give you missions.";
                    player.playSound(player.getLocation(), Sound.ENTITY_WOLF_GROWL, 10, 0);
                    player.playSound(player.getLocation(), Sound.ENTITY_WOLF_AMBIENT, 10, 0);
                }
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
                    dialogueList.add("привет я клоун");
                    dialogueList.add("прив");

                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BANJO, 2, 0);

                    MopsUtils.sendRandomDialogueMessage(dialogueList, player, entity);
                    randomDialogue = true;
                }
                if (entity.getScoreboardTags().contains("woolbattleDogeNPC")) {
                    switch (woolbattleDogeDialogue.get(player)) {
                        case 0 -> {
                            dialogue = "Hi, woolbattle isn't done yet.";
                            player.playSound(player.getLocation(), Sound.ENTITY_WOLF_AMBIENT, 10, 2);

                            woolbattleDogeDialogue.put(player, woolbattleDogeDialogue.get(player) + 1);
                        }
                        case 1 -> {
                            dialogue = "It will be out soon, though!";
                            player.playSound(player.getLocation(), Sound.ENTITY_WOLF_AMBIENT, 10, 2);

                            woolbattleDogeDialogue.put(player, woolbattleDogeDialogue.get(player) + 1);
                        }
                        case 2 -> {
                            dialogue = "Have a great day!";
                            player.playSound(player.getLocation(), Sound.ENTITY_WOLF_AMBIENT, 10, 2);
                        }
                    }
                }

                if (entity.getScoreboardTags().contains("pvpDogeNPC")) {
                    switch (pvpDogeDialogue.get(player)) {
                        case 0 -> {
                            dialogue = "There are no upgrades yet.";
                            player.playSound(player.getLocation(), Sound.ENTITY_WOLF_AMBIENT, 10, 2);

                            pvpDogeDialogue.put(player, pvpDogeDialogue.get(player) + 1);
                        }
                        case 1 -> {
                            dialogue = "There is no PVP yet too.";
                            player.playSound(player.getLocation(), Sound.ENTITY_WOLF_AMBIENT, 10, 2);

                            pvpDogeDialogue.put(player, pvpDogeDialogue.get(player) + 1);
                        }
                        case 2 -> {
                            dialogue = "I can give you a sword though, it looks cool.";
                            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 10, 0);
                            player.playSound(player.getLocation(), Sound.ENTITY_WOLF_AMBIENT, 10, 2);
                            player.getInventory().addItem(MopsUtils.createItem(Material.IRON_SWORD, ChatColor.GRAY + "Iron Sword"));

                            pvpDogeDialogue.put(player, pvpDogeDialogue.get(player) + 1);
                        }
                        case 3 -> {
                            dialogue = "I don't have any more swords.";
                            player.playSound(player.getLocation(), Sound.ENTITY_WOLF_AMBIENT, 10, 2);

                            pvpDogeDialogue.put(player, pvpDogeDialogue.get(player) + 1);
                        }
                        case 4 -> {

                        }
                    }
                }
                if (entity.getScoreboardTags().contains("lonelyPigeon")) {
                    dialogue = "hey " + ChatColor.GRAY + "(add quest later)";
                    player.playSound(player.getLocation(), Sound.ENTITY_PARROT_AMBIENT, 10, 2);
                }

                if(!entity.getScoreboardTags().contains("guideline") && !randomDialogue) {
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
        }
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.setJoinMessage("");


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

        Location spawn = new Location(player.getWorld(), -106.0, 9, -186.0);
        spawn.setYaw(-90);

        player.teleport(spawn);

        pvpDogeDialogue.putIfAbsent(player, 0);
        woolbattleDogeDialogue.putIfAbsent(player, 0);
        pigeonDialogue.putIfAbsent(player, 0);

        player.setGameMode(GameMode.ADVENTURE);

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

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.setQuitMessage("");

        for(Player allPlayers : Bukkit.getOnlinePlayers()) {
            allPlayers.sendMessage( ChatColor.GOLD + "[MopsPVPs] " + ChatColor.YELLOW + player.getName() + " left the game. " + ChatColor.AQUA + ":(");
        }
    }

    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {
        Player player = event.getPlayer();

        String rank = "";
        String name = player.getName();
        String message = event.getMessage();

        event.setCancelled(true);

        for(Player allPlayers : Bukkit.getOnlinePlayers()) {
            allPlayers.sendMessage(rank + name + ChatColor.WHITE + ": " + MopsUtils.convertColorCodes(message).trim());
        }
    }

    @EventHandler
    public void blockFadeEvent(BlockFadeEvent event) {
        event.setCancelled(true);
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

    public void announceRareDrop(String string, Player player) {
        for(Player allPlayers : Bukkit.getOnlinePlayers()) {
            allPlayers.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
            String message = player.getName() + " got the " + string;
            MopsUtils.actionBarGenerator(player, message);
        }
    }
}
