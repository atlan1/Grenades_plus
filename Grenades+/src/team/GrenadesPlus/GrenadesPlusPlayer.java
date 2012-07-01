package team.GrenadesPlus;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.getspout.spoutapi.player.SpoutPlayer;
import team.ApiPlus.Util.Utils;
import team.GrenadesPlus.API.Event.DetonateEvent;
import team.GrenadesPlus.API.Event.ThrowEvent;
import team.GrenadesPlus.Block.Placeable;
import team.GrenadesPlus.Item.Detonator;
import team.GrenadesPlus.Item.Throwable;
import team.GrenadesPlus.Util.ExplosiveUtils;
import team.GrenadesPlus.Util.LivingGrenadier;

public class GrenadesPlusPlayer extends LivingGrenadier{

	private SpoutPlayer player;
	
	public GrenadesPlusPlayer(SpoutPlayer sp){
		player = sp;
	}
	
	public SpoutPlayer getPlayer(){
		return player;
	}
	
	@Override
	public LivingEntity getLivingEntity() {
		return player;
	}
	
	@Override
	public Location getLocation(){
		return player.getLocation();
	}

	@Override
	public void Throw(Throwable t) {
		Item i = ExplosiveUtils.throwThrowable(player.getInventory(), player.getItemInHand(), Utils.getHandLocation(player), ((Number)t.getProperty("SPEED")).doubleValue()+(player.isSprinting()?.5d:0));
		ThrowEvent event = new ThrowEvent(this, t, i);
		Bukkit.getPluginManager().callEvent(event);
	}

	@Override
	public void Place(Placeable p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Detonate(Detonator d) {
		DetonateEvent event = new DetonateEvent(this, ExplosiveUtils.getBlocksInRange(getLocation(), ((Number)d.getProperty("RANGE")).intValue(), this.getAssignedBlocks()));
		Bukkit.getPluginManager().callEvent(event);
		this.getAssignedBlocks().clear();
	}


}
