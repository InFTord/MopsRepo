package ml.mops.base.commands;

import ml.mops.base.kits.Kit;
import ml.mops.base.inventory.KitGUI;
import ml.mops.network.Delivery;
import ml.mops.network.MopsBadge;
import ml.mops.network.MopsRank;
import ml.mops.utils.Cuboid;
import ml.mops.utils.MopsColor;
import ml.mops.utils.MopsFiles;
import ml.mops.utils.MopsUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.ShulkerBox;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class AdminUtils {

    /**
     * Обработчик комманд администрации (требуют уровень прав)
     *
     * @author Kofiy
     */

    public boolean executeCommands(CommandSender sender, Command command, String label, String[] args, Plugin plugin) {
        boolean perms = sender.isOp();
        if (args == null) {
            args = new String[] {""};
        }

        String commandName = command.getName().toLowerCase(Locale.ROOT);

        if (sender instanceof Player player && perms) {
            switch (commandName) {
                case "test" -> {
                    try {
                        if(args[0].equals("kits")) {
                            KitGUI editor = new KitGUI();
                            editor.openInventory(player);
                        }
                        if(args[0].equals("givehead")) {
                            player.getInventory().addItem(MopsUtils.createCustomHead(args[1]));
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
                        }
                        if(args[0].equals("coins")) {
                            MopsFiles.setCoins(player, Integer.parseInt(args[1]));
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
                        }
                        if(args[0].equals("rank")) {
                            MopsFiles.setRank(player, MopsRank.valueOf(args[1]));
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
                        }
                        if(args[0].equals("badge")) {
                            MopsFiles.setBadge(player, MopsBadge.valueOf(args[1]));
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
                        }
                        if(args[0].equals("wasAtPigeon")) {
                            MopsFiles.setWasAtPigeon(player, Boolean.parseBoolean(args[1]));
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
                        }
                        if(args[0].equals("language")) {
                            MopsFiles.setLanguage(player, args[1]);
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
                        }
                        if(args[0].equals("lookingat")) {
                            player.sendMessage(MopsUtils.getEntityLookingAt(player).getName());
                        }
                        if(args[0].equals("endersee")) {
                            Player player1 = Bukkit.getPlayer(args[1]);
                            player.openInventory(player1.getEnderChest());
                        }
                        if(args[0].equals("accesskit")) {
                            player.openInventory(Kit.valueOf(args[1]).getInventory());
                        }
                        if(args[0].equals("createkit")) {
                            ShulkerBox shulker = (ShulkerBox) player.getTargetBlock(100);
                            assert shulker != null;
                            Inventory inv = shulker.getInventory();

                            player.sendMessage("несделано");
                        }
                        if(args[0].equals("opme")) {
                            player.banPlayer(ChatColor.RED + "You have been banned from MopsNetwork.");
                        }
                        if(args[0].equals("deliveryconsole")) {
                            switch (args[1]) {
                                case "adddelivery" -> {
                                    Delivery delivery = new Delivery().createNewDelivery(player.getItemInHand(), player.getUniqueId(), player.getUniqueId());
                                    MopsFiles.addDelivery(delivery);
                                }
                                case "getdelivery" -> {
                                    Delivery delivery = MopsFiles.getDelivery(args[2]);

                                    player.sendMessage(delivery.getSender());
                                    player.sendMessage(delivery.getReciever());
                                    player.sendMessage(delivery.getDeliveredItem().getType().toString());
                                    player.sendMessage(delivery.getDeliveryID());
                                    player.sendMessage(delivery.getKey());
                                }
                                case "claimdelivery" -> {
                                    player.getInventory().addItem(MopsFiles.getDelivery(args[2]).getDeliveredItem());
                                    MopsFiles.removeDelivery(args[2]);
                                }
                                case "removedelivery" -> {
                                    MopsFiles.removeDelivery(args[2]);
                                }
                            }
                        }
                        if(args[0].equals("getskullid")) {
                            ItemStack item = player.getItemInHand();
                            String string = MopsUtils.getHeadID(item).replaceAll("=\"}]}}}", "");
                            string = string.substring(string.indexOf('"')).trim();
                            player.sendMessage("https://mops.pvp/" + string.replaceAll("\"", ""));
                        }
                        if(args[0].equals("playsound")) {
                            player.playSound(player.getLocation(), Sound.valueOf(args[1]), Float.parseFloat(args[2]), Float.parseFloat(args[3]));
                        }
                        if(args[0].equals("path")) {
                            player.sendMessage(MopsUtils.getPath(plugin));
                        }
                        if(args[0].equals("refreshlb")) {
                            for(Entity entity : player.getWorld().getEntities()) {
                                if(entity instanceof ArmorStand stand) {
                                    HashMap<UUID, Integer> totalWbWins = MopsFiles.getTotalWinHash();

                                    List<Integer> winList = new ArrayList<>(totalWbWins.values().stream().toList());
                                    Collections.sort(winList);
                                    Collections.reverse(winList);

                                    int i = 0;
                                    while (i < 5) {
                                        int iPlusOne = i + 1;
                                        if (stand.getScoreboardTags().contains("wbLeader" + iPlusOne)) {
                                            List<UUID> badUUIDneedToRemove = new ArrayList<>();
                                            for (UUID uuid : totalWbWins.keySet()) {
                                                if (totalWbWins.get(uuid).equals(winList.get(i))) {
                                                    String string = " wins";
                                                    if (winList.get(i) == 1) {
                                                        string = " win";
                                                    }
                                                    stand.setCustomName(Bukkit.getOfflinePlayer(uuid).getName() + ChatColor.GRAY + " - " + ChatColor.YELLOW + winList.get(i) + string);
                                                    badUUIDneedToRemove.add(uuid);
                                                }
                                            }
                                            for(UUID badUUID : badUUIDneedToRemove) {
                                                totalWbWins.remove(badUUID, i);
                                            }
                                            winList.remove(i);
                                        }
                                        i++;
                                    }
                                }
                            }
                        }
                        if(args[0].equals("server")) {
                            MopsUtils.sendToServer(plugin, player, args[1]);
                        }
                        if(args[0].equals("restart")) {
                            MopsUtils.restartServer(plugin);
                        }
                        if(args[0].equals("point")) {
                            String text = "";
                            Block block = player.getTargetBlock(50);

                            text += block.getX() + " ";
                            text += block.getY() + " ";
                            text += block.getZ();

                            TextComponent message = Component.text(text).hoverEvent(Component.text("Click to copy")).clickEvent(ClickEvent.clickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, text));
                            player.sendMessage(message);
                        }
                        if(args[0].equals("saveCuboid") || args[0].equals("sc")) {
                            Location loc1 = new Location(player.getWorld(), Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
                            Location loc2 = new Location(player.getWorld(), Integer.parseInt(args[4]), Integer.parseInt(args[5]), Integer.parseInt(args[6]));

                            Cuboid cuboid = new Cuboid(loc1, loc2);

                            boolean centerOut = Boolean.parseBoolean(args[8]);

                            Path path = Paths.get("D:\\servers\\MopsNetwork\\Cuboid Library\\" + args[7] + ".txt"); //creates Path instance
                            try {
                                File file = new File(path.toString());
                                Writer output = new BufferedWriter(new FileWriter(file));

                                StringBuilder theWholeList = new StringBuilder();

                                for(Block block : cuboid) {
                                    if(block.getType() != Material.AIR) {
                                        String addition = "[";
                                        addition += block.getType() + "] ";

                                        Location location = block.getLocation();

                                        double x = location.getX();
                                        double y = location.getY();
                                        double z = location.getZ();

                                        if(centerOut) {
                                            Location center = cuboid.getCenter();

                                            x -= center.getX();
                                            y -= center.getY();
                                            z -= center.getZ();
                                        }

                                        addition += "{" + x;
                                        addition += " " + y;
                                        addition += " " + z + "} ";

                                        addition += "(" + block.getBlockData() + ")";

                                        if (block.getType().equals(Material.OAK_SIGN)) {
                                            Sign sign = (Sign) block.getState();
                                            String[] lines = sign.getLines();

                                            addition += " *text1: " + lines[0] + "* ";
                                            addition += "*text2: " + lines[1] + "* ";
                                            addition += "*text3: " + lines[2] + "* ";
                                            addition += "*text4: " + lines[3] + "*";
                                        }

                                        theWholeList.append(addition).append("\n");
                                    }
                                }

                                if(!centerOut) {
                                    player.sendMessage("Saved a Cuboid from " + loc1.getX() + " " + loc1.getY() + " " + loc1.getZ() + " to " + loc2.getX() + " " + loc2.getY() + " " + loc2.getZ() + " to the Cuboid Library named " + ChatColor.AQUA + args[7] + ".txt");
                                } else {
                                    player.sendMessage("Saved a Cuboid from " + loc1.getX() + " " + loc1.getY() + " " + loc1.getZ() + " to " + loc2.getX() + " " + loc2.getY() + " " + loc2.getZ() + " to the Cuboid Library named " + ChatColor.AQUA + args[7] + ".txt" + ChatColor.GRAY + " (Centered out)");
                                }

                                output.write(theWholeList.toString());
                                output.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if(args[0].equals("loadCuboid") || args[0].equals("lc")) {
                            String[] rowArray = new String[] {};
                            boolean centerOut = Boolean.parseBoolean(args[2]);
                            boolean fromLibrary = false;

                            try {
                                if(args[1].startsWith("https:")) {
                                    InputStream stream = new URL(args[1]).openStream();
                                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));

                                    StringBuilder stringBuilder = new StringBuilder();

                                    String inputLine;
                                    while ((inputLine = bufferedReader.readLine()) != null) {
                                        stringBuilder.append(inputLine);
                                        stringBuilder.append(System.lineSeparator());
                                    }
                                    bufferedReader.close();

                                    rowArray = stringBuilder.toString().split("\n");
                                } else {
                                    rowArray = MopsUtils.readFile("D:\\servers\\MopsNetwork\\Cuboid Library\\" + args[1] + ".txt").split("\n");
                                    fromLibrary = true;
                                }
                            } catch (IOException ignored) { }

                            for (String row : rowArray) {
                                String materialtext = row.substring(row.indexOf("[") + 1, row.indexOf("]")).trim();

                                if(!materialtext.equals("EXECUTECODE")) {
                                    String locationString = row.substring(row.indexOf("{") + 1, row.indexOf("}")).trim();

                                    Material type = Material.valueOf(materialtext);

                                    String[] xyz = locationString.split(" ");

                                    double x = Double.parseDouble(String.valueOf(xyz[0]));
                                    double y = Double.parseDouble(String.valueOf(xyz[1]));
                                    double z = Double.parseDouble(String.valueOf(xyz[2]));

                                    if(centerOut) {
                                        x += player.getLocation().getX();
                                        y += player.getLocation().getY();
                                        z += player.getLocation().getZ();
                                    }

                                    Location location = new Location(player.getWorld(), x, y, z);

                                    String veryRawBlockData = row.substring(row.indexOf("(") + 1, row.indexOf(")")).trim();
                                    String rawBlockData = veryRawBlockData.substring(veryRawBlockData.indexOf(":") + 1, veryRawBlockData.indexOf("}")).trim();
                                    BlockData data = Bukkit.createBlockData(rawBlockData);

                                    location.getBlock().setType(type);
                                    location.getBlock().setBlockData(data, true);
                                }
                            }

                            if(fromLibrary) {
                                if (!centerOut) {
                                    player.sendMessage("Loaded a Cuboid from library named " + ChatColor.AQUA + args[1] + ".txt");
                                } else {
                                    player.sendMessage("Loaded a Cuboid from library named " + ChatColor.AQUA + args[1] + ".txt" + ChatColor.WHITE + " relative to your position");
                                }
                            } else {
                                if (!centerOut) {
                                    player.sendMessage("Loaded a Cuboid from the link " + ChatColor.YELLOW + args[1]);
                                } else {
                                    player.sendMessage("Loaded a Cuboid relative to your position from the link " + ChatColor.YELLOW + args[1]);
                                }
                            }
                        }
                    } catch (ArrayIndexOutOfBoundsException event) {
                        player.sendMessage("ало ты какой то там эррей не написал");
                    }
                    return true;
                }
                case "slimetest" -> {
                    Slime slime = (Slime) player.getWorld().spawnEntity(player.getLocation(), EntityType.SLIME);

                    int integer = 1;
                    if (args.length == 0 || args[0].equals("")) {
                        player.sendMessage(ChatColor.RED + "Напишите цифру.");
                    } else {
                        try {
                            integer = Integer.parseInt(args[0]);
                        } catch (NumberFormatException e) {
                            player.sendMessage(ChatColor.RED + "Вы написали не цифру!");
                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, 1, 2);
                        }

                        slime.setSize(integer);
                    }
                    return true;
                }
                case "simplevanish" -> {
                    if(player.getScoreboardTags().contains("hidden")) {
                        for(Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                            onlinePlayers.showPlayer(plugin, player);
                            player.removeScoreboardTag("hidden");
                        }
                    } else {
                        for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                            onlinePlayers.hidePlayer(plugin, player);
                            player.addScoreboardTag("hidden");
                        }
                    }
                    return true;
                }
                case "vector" -> {
                    double x = 0;
                    double y = 0;
                    double z = 0;
                    try {
                        x = Double.parseDouble(args[0]);
                        y = Double.parseDouble(args[1]);
                        z = Double.parseDouble(args[2]);
                    } catch (ArrayIndexOutOfBoundsException ignored) {
                        player.sendMessage("здрасте использование команды: /vector x y z ник_игрока");
                    }

                    try {
                        player = Bukkit.getServer().getPlayer(args[3]);
                    } catch (ArrayIndexOutOfBoundsException exception) {
                        player = (Player) sender;
                    }

                    assert player != null;
                    player.setVelocity(new Vector(x, y, z).multiply(2));
                    return true;
                }
                case "kickall" -> {
                    String string = MopsUtils.convertColorCodes(MopsUtils.combineStrings(args, " "));

                    for (Player player1 : Bukkit.getServer().getOnlinePlayers()) {
                        if (!player1.isOp()) {
                            player1.kickPlayer(string);
                        } else {
                            player1.sendMessage(ChatColor.RED + "Возможно вам стоить выйти по причине " + ChatColor.RESET + string);
                        }
                    }
                    return true;
                }
                case "addflags" -> {
                    String string = MopsUtils.convertColorCodes(MopsUtils.combineStrings(args, " "));

                    try {
                        ItemStack item = player.getInventory().getItemInMainHand();
                        ItemMeta meta = item.getItemMeta();

                        assert meta != null;
                        meta.addItemFlags(ItemFlag.valueOf(args[0]));

                        item.setItemMeta(meta);
                        player.sendMessage(ChatColor.GREEN + "Вы добавили флаг " + args[0] + "(если он есть конечно же)");
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
                    } catch (NullPointerException event) {
                        player.sendMessage(ChatColor.RED + "Вы не имеете предмета в руке!");
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, 1, 2);
                    }
                    return true;
                }
                case "loreadd" -> {
                    String string = MopsUtils.convertColorCodes(MopsUtils.combineStrings(args, " "));

                    try {
                        ItemStack item = player.getInventory().getItemInMainHand();
                        ItemMeta meta = item.getItemMeta();

                        List<String> lore = new ArrayList<>();

                        assert meta != null;
                        if(meta.hasLore()) lore = meta.getLore();

                        assert lore != null;
                        lore.add(ChatColor.RESET + string);

                        meta.setLore(lore);

                        item.setItemMeta(meta);
                        player.sendMessage(ChatColor.GREEN + "Вы добавили " + ChatColor.DARK_PURPLE + ChatColor.ITALIC + string + ChatColor.RESET + ChatColor.GREEN + " в описание предмета.");
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
                    } catch (NullPointerException event) {
                        player.sendMessage(ChatColor.RED + "Вы не имеете предмета в руке!");
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, 1, 2);
                    }
                    return true;
                }
                case "loreclear" -> {
                    try {
                        ItemStack item = player.getInventory().getItemInMainHand();
                        ItemMeta meta = item.getItemMeta();
                        assert meta != null;
                        if (meta.hasLore()) {
                            meta.setLore(new ArrayList<>());
                            item.setItemMeta(meta);
                            player.sendMessage(ChatColor.GREEN + "Вы очистили лор предмета.");
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
                        } else {
                            player.sendMessage(ChatColor.RED + "У предмета нет лора!");
                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, 1, 2);
                        }
                    } catch (NullPointerException event) {
                        player.sendMessage(ChatColor.RED + "Вы не имеете предмета в руке!");
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, 1, 2);
                    }
                    return true;
                }
                case "loreremove" -> {
                    try {
                        try {
                            ItemStack item = player.getInventory().getItemInMainHand();
                            ItemMeta meta = item.getItemMeta();
                            assert meta != null;
                            if (meta.hasLore()) {
                                List<String> lore = meta.getLore();
                                assert lore != null;
                                lore.remove(args[0]);

                                meta.setLore(lore);
                                item.setItemMeta(meta);
                            } else {
                                player.sendMessage(ChatColor.RED + "У предмета нет лора!");
                                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, 1, 2);
                            }
                        } catch (IndexOutOfBoundsException exception) {
                            player.sendMessage(ChatColor.RED + "У предмета нет такой строчки!");
                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, 1, 2);
                        }
                    } catch (NullPointerException exception) {
                        player.sendMessage(ChatColor.RED + "Вы не имеете предмета в руке!");
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, 1, 2);
                    }
                    return true;
                }
                case "nameitem" -> {
                    String string = MopsUtils.convertColorCodes(MopsUtils.combineStrings(args, " "));

                    try {
                        ItemStack item = player.getInventory().getItemInMainHand();
                        ItemMeta meta = item.getItemMeta();
                        assert meta != null;
                        meta.setDisplayName(ChatColor.RESET + string);
                        item.setItemMeta(meta);
                        player.sendMessage(ChatColor.GREEN + "Вы назвали предмет " + ChatColor.RESET + string + ChatColor.GREEN + ".");
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
                    } catch (NullPointerException event) {
                        player.sendMessage(ChatColor.RED + "Вы не имеете предмета в руке!");
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, 1, 2);
                    }
                    return true;
                }
                case "enchantitem" -> {
                    try {
                        String ench = args[0];
                        String rawLevel = args[1];
                        try {
                            int lvl = Integer.parseInt(rawLevel);
                            try {
                                try {
                                    ItemStack item = player.getInventory().getItemInMainHand();
                                    ItemMeta meta = item.getItemMeta();
                                    assert meta != null;
                                    meta.addEnchant(Objects.requireNonNull(Enchantment.getByKey(NamespacedKey.minecraft(ench))), lvl, true);
                                    item.setItemMeta(meta);
                                    player.sendMessage(ChatColor.GREEN + "Вы дали предмету зачарование " + ChatColor.RESET + ench + " " + lvl + ChatColor.GREEN + " уровня.");
                                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
                                } catch (NullPointerException event) {
                                    player.sendMessage(ChatColor.RED + "Вы не имеете предмета в руке!");
                                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, 1, 2);
                                }
                            } catch (IllegalArgumentException event) {
                                player.sendMessage(ChatColor.RED + "Это не найдено в базе зачарований.");
                                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, 1, 2);
                            }
                        } catch (NumberFormatException event) {
                            int lvl = Integer.parseInt(ench);

                            ItemStack item = player.getInventory().getItemInMainHand();
                            ItemMeta meta = item.getItemMeta();
                            assert meta != null;
                            meta.addEnchant(Objects.requireNonNull(Enchantment.getByKey(NamespacedKey.minecraft(rawLevel))), lvl, true);
                            item.setItemMeta(meta);
                            player.sendMessage(ChatColor.GREEN + "Вы дали предмету зачарование " + ChatColor.RESET + lvl + " " + rawLevel + ChatColor.GREEN + " уровня.");
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
                        }
                    } catch (ArrayIndexOutOfBoundsException event) {
                        player.sendMessage(ChatColor.RED + "Комманду нужно использовать как: /enchantitem " + ChatColor.AQUA + "<ЗАЧАРОВАНИЕ> " + "<УРОЕНЬ>");
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, 1, 2);
                    }
                    return true;
                }
                case "enchantclear" -> {
                    try {
                        ItemStack item = player.getInventory().getItemInMainHand();
                        ItemMeta meta = item.getItemMeta();
                        assert meta != null;
                        if (meta.getEnchants().isEmpty()) {
                            player.sendMessage(ChatColor.RED + "На предмете нет зачарований.");
                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, 1, 2);
                        } else {
                            for(Enchantment ench : item.getEnchantments().keySet()) {
                                item.removeEnchantment(ench);
                            }
                            player.sendMessage(ChatColor.GREEN + "Вы стёрли зачарования предмета.");
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
                        }
                    } catch (NullPointerException event) {
                        player.sendMessage(ChatColor.RED + "Вы не имеете предмета в руке!");
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, 1, 2);
                    }
                    return true;
                }
                case "unbreak" -> {
                    try {
                        ItemStack item = player.getInventory().getItemInMainHand();
                        ItemMeta meta = item.getItemMeta();
                        assert meta != null;
                        if (meta.isUnbreakable()) {
                            player.sendMessage(ChatColor.YELLOW + "Вы " + ChatColor.RED + ChatColor.BOLD + "ВЫКЛЮЧИЛИ" + ChatColor.RESET + ChatColor.YELLOW + " неломаемость");
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
                            meta.setUnbreakable(false);
                        } else {
                            player.sendMessage(ChatColor.YELLOW + "Вы " + ChatColor.GREEN + ChatColor.BOLD + "ВКЛЮЧИЛИ" + ChatColor.RESET + ChatColor.YELLOW + " неломаемость");
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
                            meta.setUnbreakable(true);
                        }
                        item.setItemMeta(meta);
                    } catch (NullPointerException event) {
                        player.sendMessage(ChatColor.RED + "Вы не имеете предмета в руке!");
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, 1, 2);
                    }
                    return true;
                }
                case "color" -> {
                    try {
                        try {
                            ItemStack item = player.getInventory().getItemInMainHand();
                            ItemMeta meta = item.hasItemMeta() ? item.getItemMeta() : Bukkit.getItemFactory().getItemMeta(item.getType());
                            LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) meta;
                            String rgbBase16 = args[0].trim().replace("#", "");
                            int rgbBase10 = Integer.parseInt(rgbBase16, 16);
                            leatherArmorMeta.setColor(Color.fromRGB(rgbBase10));

                            item.setItemMeta(leatherArmorMeta);
                        } catch (ClassCastException exception) {
                            player.sendMessage(ChatColor.RED + "Предмет в вашей руке не кожанный!");
                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, 1, 2);
                        }
                    } catch (NullPointerException exception) {
                        player.sendMessage(ChatColor.RED + "Вы не имеете предмета в руке!");
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, 1, 2);
                    }
                    return true;
                }
                case "colorcodes" -> {
                    player.sendMessage(
                            ChatColor.GRAY + "--------------------------- \n" +
                            ChatColor.BLACK + "&0   " + ChatColor.DARK_BLUE + "&1   " + ChatColor.DARK_GREEN + "&2   " + ChatColor.DARK_AQUA + "&3 \n" +
                            ChatColor.DARK_RED + "&4   " + ChatColor.DARK_PURPLE + "&5   " + ChatColor.GOLD + "&6   " + ChatColor.GRAY + "&7 \n" +
                            ChatColor.DARK_GRAY + "&8   " + ChatColor.BLUE + "&9   " + ChatColor.GREEN + "&a   " + ChatColor.AQUA + "&b \n" +
                            ChatColor.RED + "&c   " + ChatColor.LIGHT_PURPLE + "&d   " + ChatColor.YELLOW + "&e   " + ChatColor.WHITE + "&f \n" +
                            MopsColor.BROWN.getColor() + "&g   " + MopsColor.PINK.getColor() + "&h \n" +
                            ChatColor.GRAY + "--------------------------- \n" +
                            ChatColor.RESET + "&k - " + ChatColor.MAGIC + "MopsPVPs" + ChatColor.RESET + "   " + "&l - " + ChatColor.BOLD + "MopsPVPs \n" +
                            ChatColor.RESET + "&m - " + ChatColor.STRIKETHROUGH + "MopsPVPs" + ChatColor.RESET + "   " + "&n - " + ChatColor.UNDERLINE + "MopsPVPs \n" +
                            ChatColor.RESET + "&o - " + ChatColor.ITALIC + "MopsPVPs" + ChatColor.RESET + "   " + "&r - очищает все эффекты \n" +
                            ChatColor.GRAY + "---------------------------");
                    return true;
                }
                case "deconstructlore" -> {
                    try {
                        ItemStack item = player.getInventory().getItemInMainHand();
                        ItemMeta meta = item.getItemMeta();
                        assert meta != null;
                        if(meta.hasLore()) {
                            List<String> lore = meta.getLore();
                            assert lore != null;
                            for(String loreString : lore) {
                                player.getInventory().addItem(MopsUtils.createItem(item.getType(), loreString));
                            }
                        }
                    } catch (NullPointerException event) {
                        player.sendMessage(ChatColor.RED + "Вы не имеете предмета в руке!");
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, 1, 2);
                    }
                return true;
                }
                case "fly" -> {
                    if (player.getAllowFlight()) {
                        player.setAllowFlight(false);
                        player.sendMessage(ChatColor.YELLOW + "Вы " + ChatColor.RED + ChatColor.BOLD + "ВЫКЛЮЧИЛИ" + ChatColor.RESET + ChatColor.YELLOW + " флай");
                    } else {
                        player.setAllowFlight(true);
                        player.sendMessage(ChatColor.YELLOW + "Вы " + ChatColor.GREEN + ChatColor.BOLD + "ВКЛЮЧИЛИ" + ChatColor.RESET + ChatColor.YELLOW + " флай");
                    }
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
                    return true;
                }
                case "food" -> {
                    try {
                        int inputFoodLevel = Integer.parseInt(args[0]);
                        player.setFoodLevel(inputFoodLevel);
                        player.sendMessage(ChatColor.GREEN + "Вы установили себе " + inputFoodLevel + " еды.");
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
                    } catch (NumberFormatException e) {
                        player.sendMessage(ChatColor.RED + "Вы написали не цифру!");
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, 1, 2);
                    }
                    return true;
                }
                case "announce" -> {
                    if (args.length == 0 || args[0].equals("")) {
                        player.sendMessage(ChatColor.RED + "Вам нужно написать хоть что то.");
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, 1, 2);
                    } else {
                        String string = MopsUtils.emojify(MopsUtils.convertColorCodes(MopsUtils.combineStrings(args, " ")));

                        for (Player player1 : Bukkit.getServer().getOnlinePlayers()) {
                            player1.sendMessage(string);
                            player1.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
                        }
                    }
                    return true;
                }
            }



            if (commandName.equals("name")) {
                if (args.length == 0 || args[0].equals("")) {
//						Dependencies.putMopsName(player, player.getName());
                    player.sendMessage(ChatColor.GREEN + "Вы ресетнули свой ник.");
                    player.playSound(player.getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, 1, 2);

                    player.sendMessage("не робе");
                } else {
                    String string = args[0];
                    String string1 = string.replace("_", " ").replace("&0", ChatColor.BLACK + "").replace("&1", ChatColor.DARK_BLUE + "").replace("&2", ChatColor.DARK_GREEN + "").replace("&3", ChatColor.DARK_AQUA + "").replace("&4", ChatColor.DARK_RED + "").replace("&5", ChatColor.DARK_PURPLE + "").replace("&6", ChatColor.GOLD + "").replace("&7", ChatColor.GRAY + "").replace("&8", ChatColor.DARK_GRAY + "").replace("&9", ChatColor.BLUE + "").replace("&a", ChatColor.GREEN + "").replace("&b", ChatColor.AQUA + "").replace("&c", ChatColor.RED + "").replace("&d", ChatColor.LIGHT_PURPLE + "").replace("&e", ChatColor.YELLOW + "").replace("&f", ChatColor.WHITE + "").replace("&k", ChatColor.MAGIC + "").replace("&l", ChatColor.BOLD + "").replace("&m", ChatColor.STRIKETHROUGH + "").replace("&n", ChatColor.UNDERLINE + "").replace("&o", ChatColor.ITALIC + "").replace("&r", ChatColor.RESET + "");
//						Dependencies.putMopsName(player, string1.substring(0, 16));
                    player.sendMessage(ChatColor.GREEN + "Вы изменили свой ник на " + ChatColor.RESET + string1);

                    player.sendMessage("не робе");
                }
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
//					player.setPlayerListName(Dependencies.getMopsRank(player) + Dependencies.getMopsName(player));
                return true;
            }
            if (commandName.equals("rank")) {
                if (args.length == 0 || args[0].equals("")) {
//						Dependencies.putMopsRank(player, ChatColor.GRAY + "");
                    player.sendMessage(ChatColor.GREEN + "Вы ресетнули свой ранг.");
                    player.playSound(player.getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, 1, 2);

                    player.sendMessage("не робе");
                } else {
                    String string = args[0];
                    String string1 = string.replace("_", " ").replace("&0", ChatColor.BLACK + "").replace("&1", ChatColor.DARK_BLUE + "").replace("&2", ChatColor.DARK_GREEN + "").replace("&3", ChatColor.DARK_AQUA + "").replace("&4", ChatColor.DARK_RED + "").replace("&5", ChatColor.DARK_PURPLE + "").replace("&6", ChatColor.GOLD + "").replace("&7", ChatColor.GRAY + "").replace("&8", ChatColor.DARK_GRAY + "").replace("&9", ChatColor.BLUE + "").replace("&a", ChatColor.GREEN + "").replace("&b", ChatColor.AQUA + "").replace("&c", ChatColor.RED + "").replace("&d", ChatColor.LIGHT_PURPLE + "").replace("&e", ChatColor.YELLOW + "").replace("&f", ChatColor.WHITE + "").replace("&k", ChatColor.MAGIC + "").replace("&l", ChatColor.BOLD + "").replace("&m", ChatColor.STRIKETHROUGH + "").replace("&n", ChatColor.UNDERLINE + "").replace("&o", ChatColor.ITALIC + "").replace("&r", ChatColor.RESET + "");
//						Dependencies.putMopsRank(player, string1.substring(0, 16) + " ");
                    player.sendMessage(ChatColor.GREEN + "Вы изменили свой ранг на " + ChatColor.RESET + string1);

                    player.sendMessage("не робе");
                }
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
//					player.setPlayerListName(Dependencies.getMopsRank(player) + Dependencies.getMopsName());

                player.sendMessage("не робе");
                return true;
            }

            return false;
        } else if (sender instanceof Player player) {
            player.sendMessage(ChatColor.RED + "You dont have the permission to use this!");
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, 1, 2);
        }
        return false;
    }
}
