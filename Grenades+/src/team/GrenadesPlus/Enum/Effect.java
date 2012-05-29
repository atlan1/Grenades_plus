package team.GrenadesPlus.Enum;

import java.util.HashMap;
import java.util.Map;

import team.GrenadesPlus.Util.PropertyHolder;


public class Effect implements PropertyHolder{

	private EffectType effecttype;
	private EffectSection effectsection;
	private HashMap<String, Object> properties = new HashMap<String, Object>();
	
	public Effect(EffectType et, EffectSection es){
		setEffecttype(et);
		setEffectsection(es);
	}

	public EffectSection getEffectsection() {
		return effectsection;
	}

	public void setEffectsection(EffectSection effectsection) {
		this.effectsection = effectsection;
	}

	public EffectType getEffecttype() {
		return effecttype;
	}

	public void setEffecttype(EffectType effecttype) {
		this.effecttype = effecttype;
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
}
