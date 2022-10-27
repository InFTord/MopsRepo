package ml.mops.utils;

import net.minecraft.server.level.EntityPlayer;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import net.minecraft.server.network.PlayerConnection;
import org.bukkit.entity.Player;

public class HandlePlayer {
	EntityPlayer entityPlayer;

	public HandlePlayer(Player player) {
		entityPlayer = ((CraftPlayer) player).getHandle();
	}

	public PlayerConnection getPlayerConnection() {
		return entityPlayer.b;
	}
}
