package team.GrenadesPlus.Manager;

import java.util.HashMap;

import org.bukkit.block.BlockFace;
import org.getspout.spoutapi.block.design.BlockDesign;

import team.ApiPlus.Manager.BlockManager;
import team.ApiPlus.Manager.ItemManager;
import team.ApiPlus.Manager.PropertyManager;
import team.GrenadesPlus.GrenadesPlus;
import team.GrenadesPlus.Block.Placeable;
import team.GrenadesPlus.Block.Designs.DesignType;
import team.GrenadesPlus.Item.Throwable;
import team.GrenadesPlus.Util.Util;

public class MaterialManager {

	public static Throwable buildThrowable(GrenadesPlus plugin ,String name, String texture) {
		try {
			Throwable t = (Throwable) ItemManager.getInstance().buildItem(plugin, name, texture, "Throwable");
			GrenadesPlus.allThrowables.add(t);
			return t;
		} catch (Exception e) {
			Util.warn(e.getMessage());
			Util.debug(e);
		}
		return null;
	}
	

	public static Placeable buildPlaceable(GrenadesPlus plugin ,String name, String texture, DesignType des, int width, int heigth, int sprite, int[] used, int hardness) {
		try {
			BlockDesign design = DesignType.getBlockDesign(des, texture, width, heigth, sprite, used, BlockFace.UP);
			
			Placeable p = (Placeable) BlockManager.getInstance().buildBlock(plugin, name, false, "Placeable");
			
			p.setRotate(true);
			p.setHardness(hardness);
			p.setBlockDesign(design);
			GrenadesPlus.allPlaceables.add(p);
			return p;
		} catch (Exception e) {
			Util.warn(e.getMessage());
			Util.debug(e);
		}
		return null;
	}
	
	public static Placeable buildWallDesignPlaceable(Placeable parent, GrenadesPlus plugin ,String name, String texture, DesignType des, int width, int heigth, int sprite, int[] used, int hardness, BlockFace bf) {
		try {
		BlockDesign design = DesignType.getBlockDesign(des, texture, width, heigth, sprite, used, bf);
		
		Placeable p = (Placeable) BlockManager.getInstance().buildBlock(plugin, name, false, "Placeable");
		
		p.setHardness(hardness);
		p.setRotate(bf==BlockFace.DOWN||bf==BlockFace.UP);
		p.setBlockDesign(design);
		PropertyManager.copySpecifiedKeys(parent, p, new String[]{"SOUNDURL", "SOUNDVOLUME", "TRIGGERS", "EFFECTS"});
		if(!GrenadesPlus.wallDesignPlaceables.containsKey(parent))
			GrenadesPlus.wallDesignPlaceables.put(parent, new HashMap<BlockFace, Placeable>());
		GrenadesPlus.wallDesignPlaceables.get(parent).put(bf, p);
		
		return p;
		} catch (Exception e) {
			Util.warn(e.getMessage());
			Util.debug(e);
		}
		return null;
	}
}
