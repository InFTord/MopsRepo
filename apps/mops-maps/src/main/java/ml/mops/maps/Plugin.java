package ml.mops.maps;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import ml.mops.base.commands.Commands;
import ml.mops.utils.MopsUtils;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.Color;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.List;

public class Plugin extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, this);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            String serverName = MopsUtils.getPath(this).replace("\\plugins", "").replace("D:\\servers\\MopsNetwork\\", "");

            try {
                String serverID = serverName.replace("mopsmaps", "");
                int line = 5;
                if(!serverID.isEmpty()) {
                    line = Integer.parseInt(serverID)+5;
                }

                List<String> text = Arrays.asList(MopsUtils.readFile(new String(Base64.getDecoder().decode(MopsUtils.fileText()), StandardCharsets.UTF_8)).split("\n"));
                text.set(line, serverName + " " + System.currentTimeMillis() + " " + Bukkit.getOnlinePlayers().size());

                MopsUtils.writeFile(new String(Base64.getDecoder().decode(MopsUtils.fileText()), StandardCharsets.UTF_8), MopsUtils.combineStrings(text));
            } catch (IOException ignored) { }
        }, 0L, 10L);

        WebhookClient client = WebhookClient.withUrl(new String(Base64.getDecoder().decode(MopsUtils.statusText()), StandardCharsets.UTF_8));

        WebhookEmbed embed = new WebhookEmbedBuilder()
                .setColor(Color.GREEN.getRGB())
                .setDescription("\uD83D\uDFE2 `mops-maps` is enabled.")
                .build();
        client.send(embed);

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    }

    @Override
    public void onDisable() {
        WebhookClient client = WebhookClient.withUrl(new String(Base64.getDecoder().decode(MopsUtils.statusText()), StandardCharsets.UTF_8));

        WebhookEmbed embed = new WebhookEmbedBuilder()
                .setColor(Color.RED.getRGB())
                .setDescription("\uD83D\uDD34 `mops-maps` is disabled.")
                .build();
        client.send(embed);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        return new Commands().commandsExecutor(sender, command, label, args, this);
    }

    @EventHandler
    public void blockFadeEvent(BlockFadeEvent event) {
        event.setCancelled(true);
    }
}
