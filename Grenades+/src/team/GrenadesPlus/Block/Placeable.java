package team.GrenadesPlus.Block;

import java.sql.SQLException;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.player.SpoutPlayer;

import team.ApiPlus.API.Property.NumberProperty;
import team.ApiPlus.API.Property.StringProperty;
import team.ApiPlus.API.Type.BlockTypeEffectPlusProperty;
import team.GrenadesPlus.GrenadesPlus;
import team.GrenadesPlus.Manager.DBManager;
import team.GrenadesPlus.Trigger.ExplosivesTrigger;
import team.GrenadesPlus.Trigger.TriggerListener;
import team.GrenadesPlus.Util.Explosive;
import team.GrenadesPlus.Util.ExplosiveUtils;
import team.GrenadesPlus.Util.Grenadier;
import team.GrenadesPlus.Util.PlaceableData;
import team.GrenadesPlus.Util.PlayerUtils;
import team.GrenadesPlus.Util.Util;

public class Placeable extends BlockTypeEffectPlusProperty implements Explosive{

	public Placeable(Plugin plugin, String name,  boolean isOpaque) {
		super(plugin, name, isOpaque);
	}
	
	@Override
	public void onBlockPlace(World w, int x, int y, int z, LivingEntity le) {
		if(le instanceof SpoutPlayer){
			try {
				DBManager.insertBlock(((SpoutPlayer)le).getName(), new Location(w, x, y, z).getBlock(), false);
			} catch (SQLException e) {
				Util.warn(e.getMessage());
				Util.debug(e);
			}
			TriggerListener.onPlace(this, PlayerUtils.getPlayerBySpoutPlayer((SpoutPlayer)le), new Location(w, x, y, z).getBlock());
		}
	}
	
	@Override
	public boolean onBlockInteract(World world, int x, int y, int z, SpoutPlayer player) {
		Block b = new Location(world , x, y, z).getBlock();
		PlaceableData pd = DBManager.getData(b);
		pd.setInteracted(!pd.isInteracted());
		try {
			DBManager.updateBlock(pd, false);
		} catch (SQLException e) {
			Util.warn(e.getMessage());
			Util.debug(e);
		}
		if(pd.isInteracted()){
			TriggerListener.onInteract(this, PlayerUtils.getPlayerBySpoutPlayer(player), new Location(world, x, y, z).getBlock());
		}else{
			ExplosivesTrigger.stopTasks(ExplosivesTrigger.getID(b));
			ExplosivesTrigger.unregisterBlock(b);
		}
		return true;
	}
	
	@Override
	public void onBlockDestroyed(World world, int x, int y, int z, LivingEntity living){
		DBManager.deleteBlock(new Location(world, x, y, z).getBlock());
	}
	
	@Override
	public void performEffects(Object... args) {
		Location ex = (Location) args[2];
		Util.playCustomSound(GrenadesPlus.plugin, (Location)args[2],((StringProperty)getProperty("SOUNDURL")).getValue(), ((NumberProperty)getProperty("SOUNDVOLUME")).getValue().intValue());
		onBlockDestroyed(ex.getWorld(), ex.getBlockX(), ex.getBlockY(), ex.getBlockZ(), null);
		ex.getBlock().setTypeId(0, true);
		ExplosiveUtils.performEffects((Grenadier)args[0], (Explosive)args[1], (Location)args[2]);
	}
	
	public String getPlaceableName() {
		return ExplosiveUtils.getPlaceableName(this);
	}
	
}
