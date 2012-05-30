package team.GrenadesPlus.Block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.block.design.BlockDesign;
import org.getspout.spoutapi.material.block.GenericCustomBlock;

import team.GrenadesPlus.Enum.Effect;
import team.GrenadesPlus.Util.EffectHolder;
import team.GrenadesPlus.Util.PropertyHolder;

public class Placeable extends GenericCustomBlock implements EffectHolder, PropertyHolder{

	private List<Effect> effects = new ArrayList<Effect>();
	private Map<String, Object> properties = new HashMap<String, Object>();
	
	public Placeable(Plugin plugin, String name, int blockId, int metadata, BlockDesign design, int hardness) {
		super(plugin, name, blockId, metadata, design);
		this.setRotate(true);
		this.setHardness(hardness);
		this.setOpaque(false);
	}
	
	@Override
	public void onBlockPlace(World w, int x, int y, int z, LivingEntity le) {
		
	}
	
	@Override
	public void performEffects() {
		
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
