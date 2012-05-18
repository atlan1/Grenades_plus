package team.GrenadesPlus.Item;

import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.material.item.GenericCustomItem;

import team.GrenadesPlus.Util.Throwable;

public class Grenade extends GenericCustomItem implements Throwable{

	public Grenade(Plugin plugin, String name, String texture) {
		super(plugin, name, texture);
	}

	@Override
	public boolean onThrow() {
		return false;
	}

}
