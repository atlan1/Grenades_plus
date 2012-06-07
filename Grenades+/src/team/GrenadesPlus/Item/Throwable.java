package team.GrenadesPlus.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.material.item.GenericCustomItem;

import team.ApiPlus.API.Effect.Effect;
import team.ApiPlus.API.Effect.EffectHolder;
import team.ApiPlus.API.PropertyHolder;

public class Throwable extends GenericCustomItem implements EffectHolder, PropertyHolder{

	private Map<String, Object> properties = new HashMap<String, Object>();
	
	public Throwable(Plugin plugin, String name, String texture) {
		super(plugin, name, texture);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Effect> getEffects() {
		return (List<Effect>) getProperty("EFFECTS");
	}
	
	@Override
	public void setEffects(List<Effect> eff){
		editProperty("EFFECTS", eff);
		addProperty("EFFECTS", eff);
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
