package team.GrenadesPlus;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.block.SpoutBlock;
import org.getspout.spoutapi.event.spout.SpoutCraftEnableEvent;
import org.getspout.spoutapi.inventory.SpoutItemStack;
import org.getspout.spoutapi.player.SpoutPlayer;

import team.GrenadesPlus.Block.Placeable;
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
		PlayerUtils.sendNotification(sp, ChatColor.BLUE+"Grenades+", ChatColor.AQUA+"by Atlan1, SirTyler", new ItemStack(Material.POTION), 2000);
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e){
		if(PlayerUtils.hasSpoutcraft(e.getPlayer())&&PlayerUtils.getPlayerBySpoutPlayer((SpoutPlayer)e.getPlayer())!=null){
			GrenadesPlus.GrenadesPlusPlayers.remove(PlayerUtils.getPlayerBySpoutPlayer((SpoutPlayer)e.getPlayer()));
		}
	}
	
	@EventHandler
	public void onWallBlockPlace(PlayerInteractEvent e){
		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)&&ExplosiveUtils.isPlaceable(new SpoutItemStack(e.getPlayer().getItemInHand()))){
			GrenadesPlusPlayer gp = PlayerUtils.getPlayerBySpoutPlayer((SpoutPlayer)e.getPlayer());
			Placeable p = ExplosiveUtils.getPlaceable(e.getItem());
			gp.Place(p, e.getClickedBlock().getRelative(e.getBlockFace()), e.getBlockFace());
		}
	}
	
	@EventHandler
	public void onPlayerRightClickWithDetonator(PlayerInteractEvent e){
		if(!PlayerUtils.hasSpoutcraft(e.getPlayer())){
			return;
		}
		GrenadesPlusPlayer gp = PlayerUtils.getPlayerBySpoutPlayer((SpoutPlayer) e.getPlayer());
		if(ExplosiveUtils.isDetonator(gp.getPlayer().getItemInHand())&&e.getAction().equals(Action.RIGHT_CLICK_BLOCK)||e.getAction().equals(Action.RIGHT_CLICK_AIR)){
			if(!PlayerUtils.hasPermissionForDetonator(gp.getPlayer())) return;
			if(gp.getPlayer().isSneaking()){
				if(!gp.getAssignedBlocks().isEmpty()){
					gp.Detonate(GrenadesPlus.detonator);
				}
			}else if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)&&ExplosiveUtils.isPlaceable(e.getClickedBlock())&&!gp.getAssignedBlocks().contains(e.getClickedBlock())){
				gp.assignBlock(e.getClickedBlock());
				PlayerUtils.sendNotification(gp.getPlayer(), "Assigned a block of", ExplosiveUtils.getPlaceable(e.getClickedBlock()).getPlaceableName(), new SpoutItemStack(((SpoutBlock)e.getClickedBlock()).getCustomBlock()), 2000);
			}
		}
	}
}
