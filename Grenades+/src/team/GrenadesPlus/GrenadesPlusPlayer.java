package team.GrenadesPlus;

import org.bukkit.Location;
import org.getspout.spoutapi.player.SpoutPlayer;

import team.GrenadesPlus.Block.Placeable;
import team.GrenadesPlus.Item.Throwable;
import team.GrenadesPlus.Util.Layer;
import team.GrenadesPlus.Util.Thrower;

public class GrenadesPlusPlayer implements Thrower, Layer{

	private SpoutPlayer player;
	
	public GrenadesPlusPlayer(SpoutPlayer sp){
		player = sp;
	}
	
	public SpoutPlayer getPlayer(){
		return player;
	}
	
	public Location getLocation(){
		return player.getLocation();
	}

	@Override
	public void Throw(Throwable t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Lay(Placeable p) {
		// TODO Auto-generated method stub
		
	}
}
