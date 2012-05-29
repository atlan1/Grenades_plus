package team.GrenadesPlus.Manager;

import team.GrenadesPlus.GrenadesPlus;
import team.GrenadesPlus.Item.Throwable;
import team.GrenadesPlus.Util.EffectHolder;
import team.GrenadesPlus.Util.PropertyHolder;

public class MaterialManager {

	public static Throwable buildNewThrowable(GrenadesPlus plugin ,String name, String texture) {
		Throwable t = new Throwable(plugin, name, texture);
		GrenadesPlus.allThrowables.add(t);
		return t;
	}
	
	public static void copyProperties(PropertyHolder input, PropertyHolder result){
		result.setProperties(input.getProperties());
	}
	
	public static void copyEffects(EffectHolder input, EffectHolder result){
		result.setEffects(input.getEffects());
	}
}
