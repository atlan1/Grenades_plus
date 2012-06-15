package team.GrenadesPlus.Util;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.block.SpoutBlock;
import org.getspout.spoutapi.inventory.SpoutItemStack;
import team.GrenadesPlus.GrenadesPlus;
import team.GrenadesPlus.Block.Placeable;
import team.GrenadesPlus.Item.Throwable;

public class ExplosiveUtils {

	public static Item throwThrowable(Inventory inv, ItemStack i, Location direction, double s){
		if(isThrowable(i)){
			Item drop = direction.getWorld().dropItem(direction, new SpoutItemStack(getThrowable(i)));
			drop.setPickupDelay(Integer.MAX_VALUE);
			drop.setVelocity(direction.getDirection().multiply(s));
			removeThrowable(inv, i);
			return drop;
		}
		return null;
	}
	
	public static List<Block> getBlocksInRange(Location l, int range, List<Block> list){
		List<Block> inrange = new ArrayList<Block>();
		for(Block b:list){
			if(b.getLocation().toVector().distance(l.toVector())<=range){
				inrange.add(b);
			}
		}
		return inrange;
	}
	
	public static void removeThrowable(Inventory i, ItemStack is){
		if(!i.contains(is)) return;
		if(is.getAmount()>1)
			is.setAmount(is.getAmount()-1);
		else
			i.remove(is);
	}
	
	public static boolean isThrowable(ItemStack i){
		for(Throwable g : GrenadesPlus.allThrowables){
			SpoutItemStack sis = new SpoutItemStack(g);
			if(sis.getDurability()==i.getDurability()&&sis.getTypeId()==i.getTypeId()){
				return true;
			}
		}
		return false;
	}
	
	public static boolean isPlaceable(ItemStack i){
		for(Placeable e : GrenadesPlus.allPlaceables){
			SpoutItemStack sis = new SpoutItemStack(e);
			if(sis.getDurability()==i.getDurability()&&sis.getTypeId()==i.getTypeId()){
				return true;
			}
		}
		return false;
	}
	
	public static boolean isPlaceable(Block b){
		for(Placeable e : GrenadesPlus.allPlaceables){
			SpoutBlock sb = (SpoutBlock) b;
			if(sb.getCustomBlock().equals(e)){
				return true;
			}
		}
		return false;
	}
	
	public static Placeable getPlaceable(Block b){
		for(Placeable e : GrenadesPlus.allPlaceables){
			SpoutBlock sb = (SpoutBlock) b;
			if(sb.getCustomBlock().equals(e)){
				return e;
			}
		}
		return null;
	}
	
	public static Throwable getThrowable(ItemStack i){
		for(Throwable g : GrenadesPlus.allThrowables){
			SpoutItemStack sis = new SpoutItemStack(g);
			if(sis.getDurability()==i.getDurability()&&sis.getTypeId()==i.getTypeId()){
				return g;
			}
		}
		return null;
	}
	
	public static Placeable getPlaceable(ItemStack i){
		for(Placeable e : GrenadesPlus.allPlaceables){
			SpoutItemStack sis = new SpoutItemStack(e);
			if(sis.getDurability()==i.getDurability()&&sis.getTypeId()==i.getTypeId()){
				return e;
			}
		}
		return null;
	}
	
	public static boolean isExplosive(ItemStack i){
		return isThrowable(i)||isPlaceable(i);
	}
}
