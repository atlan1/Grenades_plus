package team.GrenadesPlus.Util;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import team.ApiPlus.API.Operator;
import team.GrenadesPlus.Block.Placeable;
import team.GrenadesPlus.Item.Detonator;
import team.GrenadesPlus.Item.Throwable;

public abstract class Grenadier implements Operator{
	
	private ArrayList<Block> assignedToDetonator = new ArrayList<Block>();
	
	public void assignBlock(Block b){
		if(!assignedToDetonator.contains(b))
			assignedToDetonator.add(b);
	}
	
	public void removeBlock(Block b){
		if(assignedToDetonator.contains(b))
			assignedToDetonator.remove(b);
	}
	
	@SuppressWarnings("unchecked")
	public List<Block> getAssignedBlocks(){
		for(Block b : (ArrayList<Block>)assignedToDetonator.clone())
			if(!ExplosiveUtils.isPlaceable(b)){
				assignedToDetonator.remove(b);
			}
		return assignedToDetonator;
	}
	
	public abstract void Throw(Throwable t);
	
	public abstract void Place(Placeable p, Block b, BlockFace bf);
	
	public abstract void Detonate(Detonator d);
	
}
