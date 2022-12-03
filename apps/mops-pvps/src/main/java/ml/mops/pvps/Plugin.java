package ml.mops.pvps;

import ml.mops.base.commands.Commands;
import ml.mops.utils.MopsUtils;
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
		return new Commands().commandsExecutor(sender, command, label, args, this);
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
}
