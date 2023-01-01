package ml.mops.maps;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import ml.mops.base.commands.Commands;
import ml.mops.utils.MopsUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

public class Plugin extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, this);

        WebhookClient client = WebhookClient.withUrl(new String(Base64.getDecoder().decode(MopsUtils.statusText()), StandardCharsets.UTF_8));

        WebhookEmbed embed = new WebhookEmbedBuilder()
                .setColor(Color.GREEN.getRGB())
                .setDescription("\uD83D\uDFE2 `mops-maps` is turned on.")
                .build();
        client.send(embed);
    }

    @Override
    public void onDisable() {
        WebhookClient client = WebhookClient.withUrl(new String(Base64.getDecoder().decode(MopsUtils.statusText()), StandardCharsets.UTF_8));

        WebhookEmbed embed = new WebhookEmbedBuilder()
                .setColor(Color.RED.getRGB())
                .setDescription("\uD83D\uDD34 `mops-maps` is turned off.")
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
