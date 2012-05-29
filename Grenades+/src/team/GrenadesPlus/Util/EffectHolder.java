package team.GrenadesPlus.Util;

import java.util.List;

import team.GrenadesPlus.Enum.Effect;

public interface EffectHolder {

	public List<Effect> getEffects();
	public void setEffects(List<Effect> effects);
	public void performEffects();
}
