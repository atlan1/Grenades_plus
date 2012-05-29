package team.GrenadesPlus.Enum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import team.GrenadesPlus.Util.PropertyHolder;

public enum Trigger implements PropertyHolder{

	ONHIT(true), DETONATOR(false), SHOCK(false), TIME(null), REDSTONE(false);
	
	// This boolean defines either to use a trigger for throwables or for placeables or for both. (t=true, p=false, b=null)
	private Boolean usage;
	
	private static List<Trigger> throwableTriggers = new ArrayList<Trigger>();
	private static List<Trigger> placeableTriggers = new ArrayList<Trigger>();
	
	private Map<String, Object> properties = new HashMap<String, Object>();
	
	private Trigger(Boolean b){
		usage = b;
	}
	
	private boolean getUsage(){
		return usage;
	}
	
	static {
		for(Trigger t : values()){
			if(t.getUsage())
				throwableTriggers.add(t);
			else if(!t.getUsage())
				placeableTriggers.add(t);
			else{
				throwableTriggers.add(t);
				placeableTriggers.add(t);
			}
		}
	}
	
	public boolean isThrowableTrigger(Trigger t){
		return throwableTriggers.contains(t);
	}
	
	public boolean isPlaceableTrigger(Trigger t){
		return placeableTriggers.contains(t);
	}
	
	public List<Trigger> getThrowableTriggers(){
		return throwableTriggers;
	}
	
	public List<Trigger> getPlaceableTriggers(){
		return placeableTriggers;
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
