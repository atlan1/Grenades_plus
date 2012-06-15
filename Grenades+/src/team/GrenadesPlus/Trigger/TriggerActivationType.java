package team.GrenadesPlus.Trigger;

import java.util.ArrayList;
import java.util.List;

public enum TriggerActivationType {
	/*TODO: ONHIT(true),*/ ONTHROW(true), ONPLACE(false), ONINTERACT(false);
	
	// This boolean defines either to use a type for throwables or for placeables or for both. (t=true, p=false, b=null)
	private Boolean usage;
	
	private static List<TriggerActivationType> throwableActivationTypes = new ArrayList<TriggerActivationType>();
	private static List<TriggerActivationType> placeableActivationTypes = new ArrayList<TriggerActivationType>();
	
	private TriggerActivationType(Boolean b){
		this.usage = b;
	}
	
	private Boolean getUsage(){return usage;}
	
	static {
		for(TriggerActivationType t : values()){
			if (t.getUsage()==null) {
				throwableActivationTypes.add(t);
				placeableActivationTypes.add(t);
			} else if (!t.getUsage()) {
				placeableActivationTypes.add(t);
			} else {
				throwableActivationTypes.add(t);
			}
		}
	}
	
	public static boolean isThrowableTriggerActivationType(TriggerActivationType t){
		return throwableActivationTypes.contains(t);
	}
	
	public static boolean isPlaceableTriggerActivationType(TriggerActivationType t){
		return placeableActivationTypes.contains(t);
	}
	
	public static List<TriggerActivationType> getThrowableTriggerActivationTypes(){
		return throwableActivationTypes;
	}
	
	public static List<TriggerActivationType> getPlaceableTriggerActivationTypes(){
		return placeableActivationTypes;
	}
	
	
}
