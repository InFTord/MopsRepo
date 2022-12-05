package ml.mops.pvps;

import ml.mops.base.commands.AdminUtils;
import ml.mops.base.commands.Commands;
import ml.mops.base.commands.PlayerEssentials;
import ml.mops.utils.MopsUtils;
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

	@Override
	public void onEnable() {
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		World mainworld = Bukkit.getServer().getWorlds().get(0);
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
			player.sendMessage("command sent");

			if(args[0].equals("loadCuboid")) {
				player.sendMessage("load cuboid");

				Map map = Map.valueOf(args[1]);
				boolean confirm = Boolean.parseBoolean(args[2]);
				boolean confirm2 = args[3].equals("CONFIRM");

				player.sendMessage("parsed");

				if (perms && confirm && confirm2) {

					player.sendMessage("conditions met");

					InputStream stream = getResource(map.getFileName());

					player.sendMessage("file found");

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

						player.sendMessage("string read");
						player.sendMessage(ChatColor.YELLOW + stringBuilder.toString().substring(0, 50));

						rowArray = stringBuilder.toString().split("\n");;

					} catch (Exception ignored) { }

						for (String row : rowArray) {
							Material type = Material.valueOf(row.substring(row.indexOf("[") + 1, row.indexOf("]")).trim());

							String locationString = row.substring(row.indexOf("{") + 1, row.indexOf("}")).trim();
							String[] xyz = locationString.split(" ");

							double x = Double.parseDouble(String.valueOf(xyz[0]));
							double y = Double.parseDouble(String.valueOf(xyz[1]));
							double z = Double.parseDouble(String.valueOf(xyz[2]));

							Location location = new Location(player.getWorld(), x, y, z);

							String veryRawBlockData = row.substring(row.indexOf("(") + 1, row.indexOf(")")).trim();
							String rawBlockData = veryRawBlockData.substring(row.indexOf("{") + 1, row.indexOf("}")).trim();
							BlockData data = Bukkit.createBlockData(rawBlockData);

							location.getBlock().setType(type);
							location.getBlock().setBlockData(data, true);

							if(Boolean.parseBoolean(args[4])) {
								player.sendMessage(ChatColor.RED + "" + type + " " + x + " " + y + " " + z + " " + rawBlockData);
							}
						}
				}
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


	@Override
	public @Nullable InputStream getResource(@NotNull String filename) {
		return super.getResource(filename);
	}
}
