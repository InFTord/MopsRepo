package ml.mops.network;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Party {
	public Party(Player player) {
		partyMembers.add(player);
		owner = player;
	}

	List<Player> partyMembers = new ArrayList<>();
	Player owner;

	public List<Player> getPartyMembers() {
		return partyMembers;
	}
	public void setPartyMembers(List<Player> memberList) {
		this.partyMembers = memberList;
	}

	public void addMember(Player player) {
		partyMembers.add(player);
	}
	public void removeMember(Player player) {
		partyMembers.remove(player);
	}

	public void setOwner(Player player) {
		owner = player;
	}
}