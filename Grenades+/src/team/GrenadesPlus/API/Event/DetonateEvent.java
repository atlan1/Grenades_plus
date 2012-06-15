package team.GrenadesPlus.API.Event;

import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import team.GrenadesPlus.Util.Grenadier;

public class DetonateEvent extends Event{

	private static final HandlerList handlers = new HandlerList();
    
	private Grenadier grenadier;
	private List<Block> blockList;
	
    public DetonateEvent(Grenadier t, List<Block> blockList) {
    	this.grenadier = t;
    	this.blockList = blockList;
    }
    
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
    
    public Grenadier getGrenadier(){
    	return grenadier;
    }
   
    public List<Block> getBlockList(){
    	return blockList;
    }
	   
	
}
