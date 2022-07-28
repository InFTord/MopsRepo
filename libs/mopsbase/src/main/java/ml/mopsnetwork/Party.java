package ml.mopsbase;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Party {
	List<Player> partyMembers = new ArrayList<>();
	Player owner = null;


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

	public void createParty(Player player) {
		partyMembers.add(player);
		owner = player;
	}
}