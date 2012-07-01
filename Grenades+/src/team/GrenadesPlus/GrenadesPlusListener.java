package team.GrenadesPlus;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.block.SpoutBlock;
import org.getspout.spoutapi.event.spout.SpoutCraftEnableEvent;
import org.getspout.spoutapi.inventory.SpoutItemStack;
import org.getspout.spoutapi.player.SpoutPlayer;

import team.ApiPlus.Util.Task;
import team.GrenadesPlus.Block.Placeable;
import team.GrenadesPlus.Block.Designs.DesignType;
import team.GrenadesPlus.Util.ExplosiveUtils;
import team.GrenadesPlus.Util.PlayerUtils;

public class GrenadesPlusListener implements Listener{

	public GrenadesPlus plugin;

	public GrenadesPlusListener(GrenadesPlus instance) {
		plugin = instance;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onPlayerJoin(SpoutCraftEnableEvent e) {
		SpoutPlayer sp = e.getPlayer();
		GrenadesPlusPlayer gp = new GrenadesPlusPlayer(sp);
		GrenadesPlus.GrenadesPlusPlayers.add(gp);
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e){
		if(PlayerUtils.hasSpoutcraft(e.getPlayer()))
			GrenadesPlus.GrenadesPlusPlayers.remove((SpoutPlayer)e.getPlayer());
	}
	
	@EventHandler
	public void onWallBlockPlace(PlayerInteractEvent e){
		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)&&ExplosiveUtils.isPlaceable(new SpoutItemStack(e.getPlayer().getItemInHand()))){
			Placeable p = ExplosiveUtils.getPlaceable(e.getItem());
			if(((DesignType)p.getProperty("DESIGN")).isAttaching()&&!e.getBlockFace().equals(BlockFace.UP)){
				Task t = new Task(plugin,p, e.getClickedBlock(), e.getBlockFace()){
					public void run(){
						Block b = (Block)this.getArg(1);
						BlockFace bf = (BlockFace)this.getArg(2);
						Placeable p = (Placeable)this.getArg(0);
						SpoutBlock sb = (SpoutBlock)b.getRelative(bf);
						if (b.getType() == Material.SNOW) {
							sb = sb.getRelative(0, -1, 0);
						}
						Placeable newP = GrenadesPlus.wallDesignPlaceables.get(p).get(bf);
						sb.setTypeIdAndData(newP.getBlockId(), (byte)(newP.getBlockData()), true);
						SpoutManager.getMaterialManager().overrideBlock(sb, newP);
					}
				};
				t.startTaskDelayed(1);
			}
		}
	}
	
	@EventHandler
	public void onPlayerRightClickWithDetonator(PlayerInteractEvent e){
		GrenadesPlusPlayer gp = PlayerUtils.getPlayerBySpoutPlayer((SpoutPlayer) e.getPlayer());
		if(ExplosiveUtils.isDetonator(gp.getPlayer().getItemInHand())&&e.getAction().equals(Action.RIGHT_CLICK_BLOCK)||e.getAction().equals(Action.RIGHT_CLICK_AIR)){
			if(gp.getPlayer().isSneaking()){
				if(!gp.getAssignedBlocks().isEmpty()){
					gp.Detonate(GrenadesPlus.detonator);
					PlayerUtils.sendNotification(gp.getPlayer(), "Achievment get!", "Behave like a creeper!", new SpoutItemStack(GrenadesPlus.detonator), 2000);
				}
			}else if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)&&ExplosiveUtils.isPlaceable(e.getClickedBlock())){
				gp.assignBlock(e.getClickedBlock());
				PlayerUtils.sendNotification(gp.getPlayer(), "Assigned a "+ExplosiveUtils.getPlaceable(e.getClickedBlock()).getPlaceableName()+" block!", "You can now detonate it!", new SpoutItemStack(((SpoutBlock)e.getClickedBlock()).getCustomBlock()), 2000);
			}
		}
	}
}
