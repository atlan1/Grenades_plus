package team.GrenadesPlus.Util;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;

import team.ApiPlus.API.Operator;
import team.GrenadesPlus.Block.Placeable;
import team.GrenadesPlus.Item.Detonator;
import team.GrenadesPlus.Item.Throwable;

public abstract class Grenadier implements Operator{
	
	public List<Block> assignedToDetonator = new ArrayList<Block>();
	public boolean detonated = false;
	
	public void assignBlock(Block b){
		if(!assignedToDetonator.contains(b))
			assignedToDetonator.add(b);
	}
	
	public void removeBlock(Block b){
		if(assignedToDetonator.contains(b))
			assignedToDetonator.remove(b);
	}

	public void setDetonated(boolean d){
		detonated = d;
	}
	
	public boolean isDetonated(){
		return detonated;
	}
	
	public abstract void Throw(Throwable t);
	
	public abstract void Place(Placeable p);
	
	public abstract void Detonate(Detonator d);
	
	public abstract Location getLocation();
}
