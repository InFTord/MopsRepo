package ml.mops.pvps;

import ml.mops.base.Kit;
import ml.mops.base.commands.AdminUtils;
import ml.mops.base.commands.Commands;
import ml.mops.base.commands.PlayerEssentials;
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
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class Plugin extends JavaPlugin implements Listener, CommandExecutor {

	World mainworld;

	Location location1;
	Location location2;

	Location turtleLocation;

	@Override
	public void onEnable() {
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		mainworld = Bukkit.getServer().getWorlds().get(0);

		location1 = new Location(mainworld, 0, 0, 0);
		location2 = new Location(mainworld, 0, 0, 0);

		turtleLocation = new Location(mainworld, 0, 0, 0);
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
	}

	boolean gameSession = false;
	Map map = Map.DESERT;

	Player player1;
	Player player2;

	boolean ready1 = false;
	boolean ready2 = false;

	Kit player1kit = null;
	Kit player2kit = null;


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
				boolean confirm = Boolean.parseBoolean(args[1]);
				boolean confirm2 = args[2].equals("CONFIRM");

				if (perms && confirm && confirm2) {
					wipeout(player.getWorld());
				}
			}

			if(args[0].equals("startgame")) {
				gameSession = true;

				wipeout(mainworld);
				loadCuboid(map, mainworld);
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
		if(player1 == null) {
			player1 = event.getPlayer();
		} else {
			player2 = event.getPlayer();
		}
	}

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		if(player1 != null) {
			player1 = null;
		} else {
			player2 = null;
		}
	}




	public void wipeout(World world) {
		Location loc1 = new Location(world, -400, -63, -400);
		Location loc2 = new Location(world, 400, 319, 400);

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
		InputStream stream = getResource(map.getFileName());
		String[] rowArray = new String[] {""};

		try {
			assert stream != null;
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(stream));

			StringBuilder stringBuilder = new StringBuilder();

			String inputLine;
			while ((inputLine = bufferedReader.readLine()) != null) {
				stringBuilder.append(inputLine);
				stringBuilder.append(System.lineSeparator());
			}
			bufferedReader.close();

			rowArray = stringBuilder.toString().split("\n");;

		} catch (Exception ignored) { }

		for (String row : rowArray) {
			Material type = Material.valueOf(row.substring(row.indexOf("[") + 1, row.indexOf("]")).trim());

			String locationString = row.substring(row.indexOf("{") + 1, row.indexOf("}")).trim();
			String[] xyz = locationString.split(" ");

			double x = Double.parseDouble(String.valueOf(xyz[0]));
			double y = Double.parseDouble(String.valueOf(xyz[1]));
			double z = Double.parseDouble(String.valueOf(xyz[2]));

			Location location = new Location(world, x, y, z);

			String veryRawBlockData = row.substring(row.indexOf("(") + 1, row.indexOf(")")).trim();
			String rawBlockData = veryRawBlockData.substring(veryRawBlockData.indexOf(":") + 1, veryRawBlockData.indexOf("}")).trim();
			BlockData data = Bukkit.createBlockData(rawBlockData);

			location.getBlock().setType(type);
			location.getBlock().setBlockData(data, true);
		}
	}


	@Override
	public @Nullable InputStream getResource(@NotNull String filename) {
		return super.getResource(filename);
	}
}
