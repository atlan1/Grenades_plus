package team.GrenadesPlus.Item;

import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import team.ApiPlus.API.Type.ItemTypeEffectPlusProperty;
import team.GrenadesPlus.GrenadesPlus;
import team.GrenadesPlus.Util.Explosive;
import team.GrenadesPlus.Util.ExplosiveUtils;
import team.GrenadesPlus.Util.Grenadier;
import team.GrenadesPlus.Util.Util;

public class Throwable extends ItemTypeEffectPlusProperty implements Explosive{

	public Throwable(Plugin plugin, String name, String texture) {
		super(plugin, name, texture);
	}

	@Override
	public void performEffects(Object... args) {
		Util.playCustomSound(GrenadesPlus.plugin, (Location)args[2],(String)getProperty("SOUNDURL"), (Integer)getProperty("SOUNDVOLUME"));
		ExplosiveUtils.performEffects((Grenadier)args[0], (Explosive)args[1], (Location)args[2]);
	}

	
}
