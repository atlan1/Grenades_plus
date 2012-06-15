package team.GrenadesPlus.Item;

import org.bukkit.plugin.Plugin;

import team.ApiPlus.API.Type.ItemTypeEffectPlusProperty;
import team.GrenadesPlus.Util.Explosive;

public class Throwable extends ItemTypeEffectPlusProperty implements Explosive{

	public Throwable(Plugin plugin, String name, String texture) {
		super(plugin, name, texture);
	}

	@Override
	public <T> void performEffects(T... arg0) {
		System.out.print("BEEP");
	}

	
}
