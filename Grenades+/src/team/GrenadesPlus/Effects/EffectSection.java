package team.GrenadesPlus.Effects;

import java.util.HashMap;
import java.util.Map;

import team.ApiPlus.API.PropertyHolder;
import team.ApiPlus.API.Effect.EffectTarget;

public enum EffectSection implements EffectTarget, PropertyHolder{
	EXPLOSIVELOCATION(), TARGETENTITY(), TARGETLOCATION(), GRENADIER(), GRENADIERLOCATION();
	
	private Map<String, Object> properties = new HashMap<String, Object>();
	
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
		properties = new HashMap<String, Object>(properties);
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
	
	@Override
	public void setProperty(String id, Object property) {
		addProperty(id, property);
		editProperty(id, property);
	}
}
