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
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
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
}
