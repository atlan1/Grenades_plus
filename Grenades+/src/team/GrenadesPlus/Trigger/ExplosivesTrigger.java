package team.GrenadesPlus.Trigger;

import team.ApiPlus.API.EffectHolder;
import team.ApiPlus.API.Trigger.Trigger;
import team.ApiPlus.API.Trigger.TriggerType;

public class ExplosivesTrigger implements Trigger{

	private TriggerType triggerType;
	
	public ExplosivesTrigger(TriggerType tt, EffectHolder eff) {
		triggerType = tt;
	}
	
	@Override
	public TriggerType getTriggerType() {
		return triggerType;
	}

	@Override
	public void setTriggerType(TriggerType tt) {
		triggerType = tt;
	}

	@Override
	public void triggered(Object... args) {
		getTriggerType().triggeredTask().createTask(this, args).startTaskModel();
	}
	
	@Override
	public void activate(Object... args) {
		getTriggerType().activationTask().createTask(this, args).startTaskModel();
	}
}
