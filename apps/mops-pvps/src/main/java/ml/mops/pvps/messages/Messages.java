package ml.mops.pvps.messages;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Sound;

public interface Messages {
	public TextComponent messageComponent();
	public Sound messageSound();
}