package team.old.GrenadesPack;

import java.util.ArrayList;
import java.util.List;


import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.block.SpoutBlock;
import org.getspout.spoutapi.material.item.GenericCustomItem;
import org.getspout.spoutapi.player.SpoutPlayer;

public class Detonator extends GenericCustomItem{

	public GrenadesPack plugin;
	private int range;
	private int frequency;
	
	public Detonator(Plugin pl, String name, String texture, int r, int f,  GrenadesPack gp) {
		super(pl, name, texture);
		plugin = gp;
		setRange(r);
		frequency = f;
	}
	
	
	@Override
	public boolean onItemInteract(SpoutPlayer sp, SpoutBlock sb, BlockFace bf){
		List<Placeable> list = new ArrayList<Placeable>();
		for(Placeable pl : plugin.allPlaceables){
			if(pl.getFrequency()==frequency){
				list.add(pl);
			}
		}
		for(Placeable pa : list){
			for(Player s : plugin.getServer().getOnlinePlayers()){
				if(s instanceof SpoutPlayer){
					SpoutPlayer ss = (SpoutPlayer) s;
					if(plugin.placeableData.containsKey(ss)&&plugin.placeableData.get(ss).containsKey(pa)&&!(plugin.placeableData.get(ss).get(pa).isEmpty())){
						ArrayList<Block> blocks =  plugin.placeableData.get(ss).get(pa);
						for(int i=0;i<blocks.size();i++){
							if(blocks.get(i).isEmpty()&&blocks.get(i).isLiquid()){
								continue;
							}
							Location playerLoc = sp.getLocation();
							Location blockLoc = blocks.get(i).getLocation();
							double distance = blockLoc.distance(playerLoc);
							if(distance<=range){
								if(plugin.placeableData.get(ss).get(pa).contains(blocks.get(i))){
									if(!plugin.placeableData.get(ss).get(pa).get(plugin.placeableData.get(ss).get(pa).indexOf(blocks.get(i))).isEmpty()&&!plugin.placeableData.get(ss).get(pa).get(plugin.placeableData.get(ss).get(pa).indexOf(blocks.get(i))).isLiquid()){
											plugin.util.performEffects(pa.getEffects(), blockLoc, pa.getRange());
											if(pa.getSound()!= null){
												plugin.util.playCustomSound(sp, pa.getSound());
											}
									}
								
								}
								pa.cleanData(ss, pa, blockLoc);
							}
						}
					}
				}
			}
		}
		return true;
	}
	

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

}
