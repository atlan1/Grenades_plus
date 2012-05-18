package team.GrenadesPlus;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.getspout.spoutapi.player.SpoutPlayer;

import team.GrenadesPlus.Util.GrenadesUtils;
import team.GrenadesPlus.Util.PlayerUtils;

public class GrenadesPlusListener implements Listener{

	public GrenadesPlus plugin;

	public GrenadesPlusListener(GrenadesPlus instance) {
		plugin = instance;
	}
	
	@EventHandler
	public void onRightClick(PlayerInteractEvent e){
		if(e.getAction().equals(Action.RIGHT_CLICK_AIR)||e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
			if(!PlayerUtils.hasSpoutcraft(e.getPlayer())) return;
			SpoutPlayer sp = (SpoutPlayer) e.getPlayer();
			if(GrenadesUtils.isGrenade(sp.getItemInHand())){
				
			}else if(GrenadesUtils.isExplosive(sp.getItemInHand())){
				
			}
		}
	}
	
	@EventHandler
	public void onLeftClick(PlayerInteractEvent e){
		if(e.getAction().equals(Action.LEFT_CLICK_AIR)||e.getAction().equals(Action.LEFT_CLICK_BLOCK)){
			if(!PlayerUtils.hasSpoutcraft(e.getPlayer())) return;
			SpoutPlayer sp = (SpoutPlayer) e.getPlayer();
			if(GrenadesUtils.isGrenade(sp.getItemInHand())){
				
			}else if(GrenadesUtils.isExplosive(sp.getItemInHand())){
				
			}
		}
	}
}
