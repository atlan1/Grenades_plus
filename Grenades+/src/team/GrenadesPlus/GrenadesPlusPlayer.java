package team.GrenadesPlus;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.block.SpoutBlock;
import org.getspout.spoutapi.player.SpoutPlayer;

import team.ApiPlus.API.Property.NumberProperty;
import team.ApiPlus.API.Property.ObjectProperty;
import team.ApiPlus.Util.Task;
import team.ApiPlus.Util.Utils;
import team.GrenadesPlus.API.Event.DetonateEvent;
import team.GrenadesPlus.API.Event.ThrowEvent;
import team.GrenadesPlus.Block.Placeable;
import team.GrenadesPlus.Block.Designs.DesignType;
import team.GrenadesPlus.Item.Detonator;
import team.GrenadesPlus.Item.Throwable;
import team.GrenadesPlus.Util.ExplosiveUtils;
import team.GrenadesPlus.Util.LivingGrenadier;
import team.GrenadesPlus.Util.PlayerUtils;

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
		return getPlayer();
	}
	
	@Override
	public Location getLocation(){
		return getPlayer().getLocation();
	}

	@Override
	public void Throw(Throwable t) {
		if(!PlayerUtils.hasPermissionToThrow(getPlayer(), t)) return;
		Item i = ExplosiveUtils.throwThrowable(getPlayer().getInventory(), getPlayer().getItemInHand(), Utils.getHandLocation(getPlayer()), ((NumberProperty)t.getProperty("SPEED")).getValue().doubleValue()+(getPlayer().isSprinting()?.5d:0));
		ThrowEvent event = new ThrowEvent(this, t, i);
		Bukkit.getPluginManager().callEvent(event);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void Place(Placeable p, Block b, BlockFace bf) {
		if(!PlayerUtils.hasPermissionToPlace(getPlayer(), p)) return;
		if(bf.equals(BlockFace.UP)){
			SpoutBlock sb = (SpoutBlock)b;
			sb.setTypeIdAndData(p.getBlockId(), (byte)(p.getBlockData()), true);
			SpoutManager.getMaterialManager().overrideBlock(sb, p);
		}else if(((ObjectProperty<DesignType>)p.getProperty("DESIGN")).getValue().isAttaching()&&!bf.equals(BlockFace.UP)){
			Task t = new Task(GrenadesPlus.plugin,p, b, bf){
				public void run(){
					BlockFace bf = (BlockFace)this.getArg(2);
					Placeable p = (Placeable)this.getArg(0);
					SpoutBlock sb = (SpoutBlock)this.getArg(1);
					Placeable newP = GrenadesPlus.wallDesignPlaceables.get(p).get(bf);
					sb.setTypeIdAndData(newP.getBlockId(), (byte)(newP.getBlockData()), true);
					SpoutManager.getMaterialManager().overrideBlock(sb, newP);
				}
			};
			t.startTask();
		}
		p.onBlockPlace(b.getWorld(), b.getX(), b.getY(), b.getZ(), getPlayer());
	}

	@Override
	public void Detonate(Detonator d) {
		DetonateEvent event = new DetonateEvent(this, ExplosiveUtils.getBlocksInRange(getLocation(), ((NumberProperty)d.getProperty("RANGE")).getValue().intValue(), this.getAssignedBlocks()));
		Bukkit.getPluginManager().callEvent(event);
	}


}
