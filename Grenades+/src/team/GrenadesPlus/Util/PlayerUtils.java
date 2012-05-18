package team.GrenadesPlus.Util;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockIterator;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.player.SpoutPlayer;

import team.GrenadesPlus.GrenadesPlus;
import team.GrenadesPlus.GrenadesPlusPlayer;

public class PlayerUtils {
	

	public static GrenadesPlusPlayer getPlayerBySpoutPlayer(SpoutPlayer sp){
		for(GrenadesPlusPlayer gp : GrenadesPlus.GrenadesPlusPlayers){
			if(gp.getPlayer().equals(sp)){
				return gp;
			}
		}
		return null;
	}
	
	public static GrenadesPlusPlayer getPlayerByName(String sp){
		for(GrenadesPlusPlayer gp : GrenadesPlus.GrenadesPlusPlayers){
			if(gp.getPlayer().getName().equals(sp)){
				return gp;
			}
		}
		return null;
	}
	
	public static Location getTargetBlock(SpoutPlayer sp, int range){
		BlockIterator bitr = new BlockIterator(sp, range);
		Location loc = null;
		int i = 0;
		while(bitr.hasNext()){
			if(i==range){
				loc = bitr.next().getLocation();
			}
			i++;
		}
		return loc;
	}
	
	public static boolean hasSpoutcraft(Player p) {
		SpoutPlayer sp = SpoutManager.getPlayer(p);
		if (sp.isSpoutCraftEnabled()) {
			return true;
		}
		return false;
	}

	public static void sendNotification(SpoutPlayer sp, String title,
			String text, ItemStack icon, int duration) {
		try {
			sp.sendNotification(title, text, icon, duration);
		} catch (Exception e) {
		}
	}
}
