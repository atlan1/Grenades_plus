package team.GrenadesPlus.API.Event;

import org.bukkit.entity.Item;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import team.GrenadesPlus.Util.Grenadier;
import team.GrenadesPlus.Item.Throwable;

public class ThrowEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
    
	private Grenadier grenadier;
	private Throwable throwable;
	private Item item;
	
    public ThrowEvent(Grenadier t, Throwable ta, Item i) {
    	grenadier = t;
    	throwable = ta;
    	item = i;
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
    
    public Throwable getThrowable(){
    	return throwable;
    }
    
    public Item getItem(){
    	return item;
    }
	    
}
