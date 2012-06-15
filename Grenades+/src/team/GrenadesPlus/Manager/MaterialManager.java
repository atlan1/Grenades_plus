package team.GrenadesPlus.Manager;

import org.getspout.spoutapi.block.design.BlockDesign;

import team.ApiPlus.Manager.BlockManager;
import team.ApiPlus.Manager.ItemManager;
import team.GrenadesPlus.GrenadesPlus;
import team.GrenadesPlus.Block.Placeable;
import team.GrenadesPlus.Block.Designs.DesignType;
import team.GrenadesPlus.Item.Throwable;
import team.GrenadesPlus.Util.Util;

public class MaterialManager {

	public static Throwable buildNewThrowable(GrenadesPlus plugin ,String name, String texture) {
		Throwable t = null;
		try {
			t = (Throwable) ItemManager.getInstance().buildItem(plugin, name, texture, "Throwable");
		} catch (Exception e) {
			Util.warn(e.getMessage());
			Util.debug(e);
		}
		GrenadesPlus.allThrowables.add(t);
		return t;
	}
	

	public static Placeable buildNewPlaceable(GrenadesPlus plugin ,String name, String texture, DesignType des, int width, int heigth, int sprite, int[] used, int hardness) {
		BlockDesign design = Util.getBlockDesign(des, texture, width, heigth, sprite, used);
		Placeable p = null;
		try {
			p = (Placeable) BlockManager.getInstance().buildBlock(plugin, name, false, "Placeable");
		} catch (Exception e) {
			Util.warn(e.getMessage());
			Util.debug(e);
		}
		p.setRotate(true);
		p.setHardness(hardness);
		p.setBlockDesign(design);
		GrenadesPlus.allPlaceables.add(p);
		return p;
	}
}
