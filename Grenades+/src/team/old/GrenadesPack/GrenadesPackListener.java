package team.old.GrenadesPack;



import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Set;


import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;


public class GrenadesPackListener implements Listener{
	public GrenadesPack plugin;
	
	public GrenadesPackListener(GrenadesPack gp){
		plugin = gp;
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e){
		if(plugin.placeableData.containsKey(e.getPlayer())){
			Set<Entry<Placeable, ArrayList<Block>>> entry = plugin.placeableData.get(e.getPlayer()).entrySet();
			for(Entry<?, ?> entr : entry){
				@SuppressWarnings("unchecked")
				ArrayList<Block> block = (ArrayList<Block>)entr.getValue();
				for(Block b : block){
					b.setTypeId(0);
				}
			}
			plugin.placeableData.remove(e.getPlayer());
		}
	}
	
	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent e ){
		if(plugin.thrownList.contains(e.getItem())){
			e.setCancelled(true);
		}
	}
}
	
