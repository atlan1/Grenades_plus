package team.GrenadesPlus.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.material.item.GenericCustomItem;

import team.GrenadesPlus.Enum.Effect;
import team.GrenadesPlus.Util.EffectHolder;
import team.GrenadesPlus.Util.PropertyHolder;

public class Throwable extends GenericCustomItem implements EffectHolder, PropertyHolder{

	private List<Effect> effects = new ArrayList<Effect>();
	private Map<String, Object> properties = new HashMap<String, Object>();
	
	public Throwable(Plugin plugin, String name, String texture) {
		super(plugin, name, texture);
	}

	@Override
	public List<Effect> getEffects() {
		return effects;
	}
	
	@Override
	public void setEffects(List<Effect> eff){
		effects = eff;
	}

	@Override
	public void performEffects() {
		
	}

	@Override
	public Object getProperty(String id) {
		return properties.get(id);
	}

	@Override
	public void addProperty(String id, Object property) {
		if(!properties.containsKey(id))
			properties.put(id, property);
	}

	@Override
	public Map<String, Object> getProperties() {
		return properties;
	}

	@Override
	public void setProperties(Map<String, Object> properties) {
		this.properties = new HashMap<String, Object>(properties);
	}

	@Override
	public void removeProperty(String id) {
		if(properties.containsKey(id))
			properties.remove(id);
	}

	@Override
	public void editProperty(String id, Object property) {
		if(properties.containsKey(id))
			properties.put(id, property);
	}
}
