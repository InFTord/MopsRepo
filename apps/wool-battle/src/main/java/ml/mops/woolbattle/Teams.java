package ml.mops.woolbattle;

import org.bukkit.Color;

public enum Teams {
	RED(Color.fromRGB(255, 10, 10), "&4", "RED", 1),
	YELLOW(Color.fromRGB(255, 207, 36), "&e", "YELLOW", 2),
	GREEN(Color.fromRGB(105, 255, 82), "&a", "GREEN", 3),
	BLUE(Color.fromRGB(47, 247, 227), "&b", "BLUE", 4),
	SPECTATOR(Color.fromRGB(170, 170, 170), "&7", "SPECTATOR", 0); //на будущие

	final String getColorString;
	final Color getLeatherColor;
	final String getName;
	final Integer getNumber;

	Teams (Color getLeatherColor, String getColorString, String getName, Integer getNumber) {
		this.getColorString = getColorString;
		this.getLeatherColor = getLeatherColor;
		this.getName = getName;
		this.getNumber = getNumber;
	}
}
