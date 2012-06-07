package team.GrenadesPlus.Enum;

import java.util.HashMap;
import java.util.Map;

import team.ApiPlus.API.Effect.Effect;
import team.ApiPlus.API.Effect.EffectSection;
import team.ApiPlus.API.Effect.EffectType;

public class ExplosiveEffect implements Effect{

	private ExplosiveEffectType effecttype;
	private ExplosiveEffectSection effectsection;
	private HashMap<String, Object> properties = new HashMap<String, Object>();
	
	public ExplosiveEffect(ExplosiveEffectType et, ExplosiveEffectSection es){
		setEffectType(et);
		setEffectSection(es);
	}

	public ExplosiveEffectSection getEffectSection() {
		return effectsection;
	}

	public void setEffectSection(EffectSection effectsection) {
		this.effectsection = (ExplosiveEffectSection) effectsection;
	}

	public ExplosiveEffectType getEffectType() {
		return effecttype;
	}

	public void setEffectType(EffectType effecttype) {
		this.effecttype = (ExplosiveEffectType) effecttype;
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
