package team.GrenadesPlus.Util;

import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.inventory.SpoutItemStack;

import team.GrenadesPlus.GrenadesPlus;
import team.GrenadesPlus.Block.Placeable;
import team.GrenadesPlus.Item.Throwable;

public class ExplosiveUtils {

	
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
