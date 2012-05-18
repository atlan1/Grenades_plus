package team.GrenadesPlus.Item;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.material.item.GenericCustomItem;

import team.GrenadesPlus.Enum.Effect;
import team.GrenadesPlus.Util.EffectHolder;

public class Throwable extends GenericCustomItem implements EffectHolder{

	private List<Effect> effects = new ArrayList<Effect>();
	
	public Throwable(Plugin plugin, String name, String texture) {
		super(plugin, name, texture);
	}

	@Override
	public List<Effect> getEffects() {
		return effects;
	}
	
	public void setEffects(List<Effect> eff){
		effects = eff;
	}

	@Override
	public void performEffects() {
		
	}
}
