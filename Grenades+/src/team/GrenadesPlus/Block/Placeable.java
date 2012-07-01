package team.GrenadesPlus.Block;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.player.SpoutPlayer;
import team.ApiPlus.API.Type.BlockTypeEffectPlusProperty;
import team.GrenadesPlus.Trigger.TriggerListener;
import team.GrenadesPlus.Util.Explosive;
import team.GrenadesPlus.Util.ExplosiveUtils;
import team.GrenadesPlus.Util.Grenadier;
import team.GrenadesPlus.Util.PlayerUtils;

public class Placeable extends BlockTypeEffectPlusProperty implements Explosive{

	public Placeable(Plugin plugin, String name,  boolean isOpaque) {
		super(plugin, name, isOpaque);
	}
	
	@Override
	public void onBlockPlace(World w, int x, int y, int z, LivingEntity le) {
		if(le instanceof SpoutPlayer){
			TriggerListener.onPlace(this, PlayerUtils.getPlayerBySpoutPlayer((SpoutPlayer)le), new Location(w, x, y, z).getBlock());
		}
	}
	
	@Override
	public boolean onBlockInteract(World world, int x, int y, int z, SpoutPlayer player) {
		TriggerListener.onInteract(this, PlayerUtils.getPlayerBySpoutPlayer(player), new Location(world, x, y, z).getBlock());
		return true;
	}
	
	@Override
	public void performEffects(Object... args) {
		System.out.print("BEEP");
		ExplosiveUtils.performEffects((Grenadier)args[0], (Explosive)args[1], (Location)args[2]);
	}
	
	public String getPlaceableName() {
		return ExplosiveUtils.getPlaceableName(this);
	}
	
}
