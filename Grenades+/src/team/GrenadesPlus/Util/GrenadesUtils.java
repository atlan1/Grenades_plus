package team.GrenadesPlus.Util;

import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.inventory.SpoutItemStack;

import team.GrenadesPlus.GrenadesPlus;
import team.GrenadesPlus.Block.Explosive;
import team.GrenadesPlus.Item.Grenade;

public class GrenadesUtils {

	
	public static boolean isGrenade(ItemStack i){
		for(Grenade g : GrenadesPlus.allGrenades){
			SpoutItemStack sis = new SpoutItemStack(g);
			if(sis.getDurability()==i.getDurability()&&sis.getTypeId()==i.getTypeId()){
				return true;
			}
		}
		return false;
	}
	
	public static boolean isExplosive(ItemStack i){
		for(Explosive e : GrenadesPlus.allExplosives){
			SpoutItemStack sis = new SpoutItemStack(e);
			if(sis.getDurability()==i.getDurability()&&sis.getTypeId()==i.getTypeId()){
				return true;
			}
		}
		return false;
	}
	
	public static Grenade getGrenade(ItemStack i){
		for(Grenade g : GrenadesPlus.allGrenades){
			SpoutItemStack sis = new SpoutItemStack(g);
			if(sis.getDurability()==i.getDurability()&&sis.getTypeId()==i.getTypeId()){
				return g;
			}
		}
		return null;
	}
	
	public static Explosive getExplosive(ItemStack i){
		for(Explosive e : GrenadesPlus.allExplosives){
			SpoutItemStack sis = new SpoutItemStack(e);
			if(sis.getDurability()==i.getDurability()&&sis.getTypeId()==i.getTypeId()){
				return e;
			}
		}
		return null;
	}
}
