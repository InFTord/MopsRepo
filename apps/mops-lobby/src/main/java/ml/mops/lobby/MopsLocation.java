package ml.mops.lobby;

import ml.mops.base.maps.MapType;
import ml.mops.utils.MopsColor;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum MopsLocation {
    LOBBY(ChatColor.YELLOW + "Main Lobby"),
    PVP(ChatColor.YELLOW + "MopsPVPs"),
    KITHUB(ChatColor.AQUA + "Kits"),
    LOBBY2(ChatColor.YELLOW + "Secondary Lobby"),
    WOOLBATTLE(ChatColor.LIGHT_PURPLE + "WoolBattle"),
    FISHERMAN(ChatColor.DARK_AQUA + "Fisherman's Shack"),
    FISHERMANBASEMENT(ChatColor.DARK_AQUA + "Fisherman's Basement"),
    MISSIONS(ChatColor.DARK_GREEN + "Missions"),
    LOBBY3(ChatColor.BLUE + "Other Lobby"),
    THEATER(MopsColor.BROWN.getColor() + "Theater"),
    MARKET(ChatColor.GREEN + "Market"),
    CIRCUS(ChatColor.RED + "Circus"),
    THREEDOGS(ChatColor.YELLOW + "Dog Wall"),
    SALESMAN(ChatColor.BLUE + "Salesman's Shop"),
    SALESMANSECRET(ChatColor.BLUE + "..."),
    CORNSECRET(ChatColor.GOLD + "" + ChatColor.BOLD + "corn"),
    ADMINROOM(ChatColor.AQUA + "admin room"),
    BACKSTAGE(MopsColor.BROWN.getColor() + "Backstage"),
    LONELYPIGEON(ChatColor.AQUA + "Pigeon's Workshop-Hole"),
    SENKOVIKROOM(ChatColor.AQUA + "Senkovik's Room");

    final String locationStatus;

    MopsLocation (String locationStatus) {
        this.locationStatus = locationStatus;
    }

    public String getLocationStatus() {
        return locationStatus;
    }
}
