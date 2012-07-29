package team.GrenadesPlus.Util;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class PlaceableData {
	private int id;
	private String owner;
	private boolean interacted;
	private Location loc;
	
	public PlaceableData(int id, String name, int x, int y, int z, String world, boolean inter) {
		this.id = id;
		this.owner = name;
		setInteracted(inter);
		setLoc(new Location(Bukkit.getWorld(world), x, y, z));
	}

	public String getOwner() {
		return owner;
	}

	public boolean isInteracted() {
		return interacted;
	}

	public void setInteracted(boolean interacted) {
		this.interacted = interacted;
	}

	public Location getLoc() {
		return loc;
	}

	public void setLoc(Location loc) {
		this.loc = loc;
	}

	public int getId() {
		return id;
	}
}
