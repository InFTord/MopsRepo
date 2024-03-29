package ml.mops.pvps;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import ml.mops.base.kits.Kit;
import ml.mops.base.commands.Commands;
import ml.mops.base.maps.Map;
import ml.mops.utils.Cuboid;
import ml.mops.utils.MopsUtils;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;


public class Plugin extends JavaPlugin implements Listener, CommandExecutor {

	World mainworld;

	Location turtleLoc;

	@Override
	public void onEnable() {
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		mainworld = Bukkit.getServer().getWorlds().get(0);

		turtleLoc = new Location(mainworld, 0, 0, 0);

		WebhookClient client = WebhookClient.withUrl(new String(Base64.getDecoder().decode(MopsUtils.statusText()), StandardCharsets.UTF_8));

		WebhookEmbed embed = new WebhookEmbedBuilder()
				.setColor(Color.GREEN.getRGB())
				.setDescription("\uD83D\uDFE2 `mops-pvps` is enabled.")
				.build();
		client.send(embed);

		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
			if(ready.get(player1) && ready.get(player2)) {
				gameSession = true;
			}

//			String serverName = MopsUtils.getPath(this).replace("\\plugins", "").replace("D:\\servers\\MopsNetwork\\", "");
//
//			try {
//				String serverID = serverName.replace("mopspvps", "");
//				int line = 10;
//				try {
//					line = Integer.parseInt(serverID+10);
//				} catch (NumberFormatException ignored) {
//					line = 10;
//				}
//
//				List<String> text = Arrays.asList(MopsUtils.readFile(new String(Base64.getDecoder().decode(MopsUtils.fileText()), StandardCharsets.UTF_8)).split("\n"));
//				text.set(line, serverName + " " + System.currentTimeMillis() + " " + Bukkit.getOnlinePlayers().size());
//
//				for(String textLine : text) {
//					if(textLine.isEmpty() || textLine.equals("\n")) {
//						text.remove(textLine);
//						text = text.stream().sorted().collect(Collectors.toList());
//					}
//				}
//				MopsUtils.writeFile(new String(Base64.getDecoder().decode(MopsUtils.fileText()), StandardCharsets.UTF_8), MopsUtils.combineStrings(text));
//			} catch (IOException ignored) { }
		}, 0, 10L);

		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
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

		WebhookClient client = WebhookClient.withUrl(new String(Base64.getDecoder().decode(MopsUtils.statusText()), StandardCharsets.UTF_8));

		WebhookEmbed embed = new WebhookEmbedBuilder()
				.setColor(Color.RED.getRGB())
				.setDescription("\uD83D\uDD34 `mops-pvps` is disabled.")
				.build();
		client.send(embed);
	}

	boolean gameSession = false;
	Map map = Map.DESERT;

	Player player1;
	Player player2;

	HashMap<Player, Boolean> ready = new HashMap<>();
	HashMap<Player, Kit> kit = new HashMap<>();
	HashMap<Player, Location> spawn = new HashMap<>();

	List<Block> ppb = new ArrayList<>();
	// player placed blocks
	HashMap<Player, List<Block>> playerPlaced = new HashMap<>();


	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
		if (pvpExclusiveCommands(sender, command, label, args, this)) {
			return true;
		} else {
			return new Commands().commandsExecutor(sender, command, label, args, this);
		}
	}


	public boolean pvpExclusiveCommands(CommandSender sender, Command command, String label, String[] args, org.bukkit.plugin.Plugin plugin) {
		boolean perms = sender.isOp();
		if (args == null) {
			args = new String[]{""};
		}

		Player player = (Player) sender;

		if(command.getName().equals("testpvp")) {

			if(args[0].equals("loadCuboid")) {
				Map map = Map.valueOf(args[1]);
				boolean confirm = Boolean.parseBoolean(args[2]);
				boolean confirm2 = args[3].equals("CONFIRM");

				if (perms && confirm && confirm2) {
					loadCuboid(map, player.getWorld());
				}
			}

			if(args[0].equals("wipeout")) {
				int radius = Integer.parseInt(args[1]);
				boolean confirm = Boolean.parseBoolean(args[2]);
				boolean confirm2 = args[3].equals("CONFIRM");

				if (perms && confirm && confirm2) {
					wipeout(player.getWorld(), radius, -30, 180);
				}
			}

			if(args[0].equals("loadmap")) {
				wipeout(mainworld, 100, -30, 180);
				loadCuboid(map, mainworld);
			}

			if(args[0].equals("readyup")) {
				if(args[1].equals("1")) {
					ready.put(player1, true);
				}
				if(args[2].equals("2")) {
					ready.put(player2, true);
				}
			}

			if(args[0].equals("stopgame")) {
				gameSession = false;
			}

			if(args[0].equals("selectmap")) {
				map = Map.valueOf(args[1]);
			}

			return true;
		}

		return false;
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
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		if(player1 == null) {
			player1 = player;
			player1.teleport(spawn.get(player1));
		} else {
			player2 = player;
			player2.teleport(spawn.get(player2));
		}

		player.getInventory().clear();
		ItemStack readyUpper = MopsUtils.createItem(Material.LIME_WOOL, ChatColor.GREEN + "Ready Up!");
		ItemMeta meta = readyUpper.getItemMeta();
		meta.setLore(Collections.singletonList(ChatColor.GRAY + "Place to Ready Up!"));
		readyUpper.setItemMeta(meta);

		player.getInventory().addItem(readyUpper);

		ready.put(player, false);
		spawn.putIfAbsent(player, new Location(player.getWorld(), 0, 0, 0));
	}

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		Player player = event.getPlayer();

		if(!gameSession) {
			for (Block block : playerPlaced.get(player)) {
				block.setType(Material.AIR);
			}
		}

		if(player1 != null) {
			player1 = null;
		} else {
			player2 = null;
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if(!gameSession) {
			event.setCancelled(true);
		}
	}

	

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();

		ppb.add(block);

		List<Block> blocks = playerPlaced.get(player);
		blocks.add(block);

		if(block.getType() == Material.LIME_WOOL) {
			ready.put(player, true);
			event.getBlock().setType(Material.AIR);

			for(Player onlinePlayers : Bukkit.getOnlinePlayers()) {
				onlinePlayers.sendMessage(ChatColor.GREEN + player.getName() + " readied up!");
			}
		}

		playerPlaced.put(player, blocks);
	}


	public void wipeout(World world, int radius, int bottom, int up) {
		Location loc1 = new Location(world, -radius, bottom, -radius);
		Location loc2 = new Location(world, radius, up, radius);

		Cuboid cuboid = new Cuboid(loc1, loc2);

		for(Block block : cuboid) {
			if(block.getType() != Material.AIR) {
				block.setType(Material.AIR);
			}
		}

		Location center = new Location(world, 0, 0, 0);
		center.getBlock().setType(Material.BEDROCK);
	}



	public void loadCuboid(Map map, World world) {
		String[] rowArray = map.getRowArray();

		for (String row : rowArray) {
			String materialtext = row.substring(row.indexOf("[") + 1, row.indexOf("]")).trim();

			String locationString = row.substring(row.indexOf("{") + 1, row.indexOf("}")).trim();

			if(materialtext.equals("EXECUTECODE")) {
				String[] xyz = locationString.split(" ");

				double x = Double.parseDouble(String.valueOf(xyz[0]));
				double y = Double.parseDouble(String.valueOf(xyz[1]));
				double z = Double.parseDouble(String.valueOf(xyz[2]));

				Location location = new Location(world, x, y, z);

				String commandData = row.substring(row.indexOf("(") + 1, row.indexOf(")")).trim();

				switch (commandData) {
					case "PLAYER1_SPAWN" -> {
						spawn.put(player1, location);
					}
					case "PLAYER2_SPAWN" -> {
						spawn.put(player2, location);
					}
					case "TURTLE_SPAWN" -> {
						turtleLoc = location;
					}
				}
			} else {
				Material type = Material.valueOf(materialtext);

				String[] xyz = locationString.split(" ");

				double x = Double.parseDouble(String.valueOf(xyz[0]));
				double y = Double.parseDouble(String.valueOf(xyz[1]));
				double z = Double.parseDouble(String.valueOf(xyz[2]));

				Location location = new Location(world, x, y, z);

				String veryRawBlockData = row.substring(row.indexOf("(") + 1, row.indexOf(")")).trim();
				String rawBlockData = veryRawBlockData.substring(veryRawBlockData.indexOf(":") + 1, veryRawBlockData.indexOf("}")).trim();
				BlockData data = Bukkit.createBlockData(rawBlockData);

				location.getBlock().setType(type);
				location.getBlock().setBiome(map.getBiome());
				location.getBlock().setBlockData(data, true);
			}
		}
	}
}
