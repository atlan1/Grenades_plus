package team.GrenadesPlus.Manager;

import org.getspout.spoutapi.block.design.BlockDesign;

import team.GrenadesPlus.GrenadesPlus;
import team.GrenadesPlus.Block.Placeable;
import team.GrenadesPlus.Enum.DesignType;
import team.GrenadesPlus.Item.Throwable;
import team.GrenadesPlus.Util.EffectHolder;
import team.GrenadesPlus.Util.PropertyHolder;
import team.GrenadesPlus.Util.Util;

public class MaterialManager {

	public static Throwable buildNewThrowable(GrenadesPlus plugin ,String name, String texture) {
		Throwable t = new Throwable(plugin, name, texture);
		GrenadesPlus.allThrowables.add(t);
		return t;
	}
	

	public static Placeable buildNewPlaceable(GrenadesPlus plugin ,String name, String texture, DesignType des, int width, int heigth, int sprite, int[] used, int hardness) {
		BlockDesign design = Util.getBlockDesign(des, texture, width, heigth, sprite, used);
		Placeable p = new Placeable(plugin, name, 46, 0,  design, hardness);
		GrenadesPlus.allPlaceables.add(p);
		return p;
	}
	
	public static void copyProperties(PropertyHolder input, PropertyHolder result){
		result.setProperties(input.getProperties());
	}
	
	public static void copyEffects(EffectHolder input, EffectHolder result){
		result.setEffects(input.getEffects());
	}
}
