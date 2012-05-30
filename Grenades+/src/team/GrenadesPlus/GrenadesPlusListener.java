package team.GrenadesPlus;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.getspout.spoutapi.event.spout.SpoutCraftEnableEvent;
import org.getspout.spoutapi.player.SpoutPlayer;

import team.GrenadesPlus.Util.PlayerUtils;


public class GrenadesPlusListener implements Listener{

	public GrenadesPlus plugin;

	public GrenadesPlusListener(GrenadesPlus instance) {
		plugin = instance;
	}
	
	@EventHandler
	public void onSpoutCraftEnable(SpoutCraftEnableEvent e) {
		SpoutPlayer sp = e.getPlayer();
		GrenadesPlusPlayer gp = new GrenadesPlusPlayer(sp);
		GrenadesPlus.GrenadesPlusPlayers.add(gp);
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e){
		if(PlayerUtils.hasSpoutcraft(e.getPlayer()))
			GrenadesPlus.GrenadesPlusPlayers.remove((SpoutPlayer)e.getPlayer());
	}
	
//	@EventHandler
//	public void onRightClick(PlayerInteractEvent e){
//		if(e.getAction().equals(Action.RIGHT_CLICK_AIR)||e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
//			if(!PlayerUtils.hasSpoutcraft(e.getPlayer())) return;
//			SpoutPlayer sp = (SpoutPlayer) e.getPlayer();
//			if(ExplosiveUtils.isThrowable(sp.getItemInHand())){
//				
//			}
//		}
//	}
//	
//	@EventHandler
//	public void onLeftClick(PlayerInteractEvent e){
//		if(e.getAction().equals(Action.LEFT_CLICK_AIR)||e.getAction().equals(Action.LEFT_CLICK_BLOCK)){
//			if(!PlayerUtils.hasSpoutcraft(e.getPlayer())) return;
//			SpoutPlayer sp = (SpoutPlayer) e.getPlayer();
//			if(ExplosiveUtils.isThrowable(sp.getItemInHand())){
//				
//			}
//		}
//	}
}
