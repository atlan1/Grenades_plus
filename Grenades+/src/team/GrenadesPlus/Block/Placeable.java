package team.GrenadesPlus.Block;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.block.design.BlockDesign;
import org.getspout.spoutapi.material.block.GenericCustomBlock;

import team.GrenadesPlus.Enum.Effect;
import team.GrenadesPlus.Util.EffectHolder;

public class Placeable extends GenericCustomBlock implements EffectHolder{

	private List<Effect> effects = new ArrayList<Effect>();
	
	public Placeable(Plugin plugin, String name, int blockId, int metadata,
			BlockDesign design) {
		super(plugin, name, blockId, metadata, design);
	}
	
	@Override
	public void onBlockPlace(World w, int x, int y, int z, LivingEntity le) {
		
	}
	
	@Override
	public List<Effect> getEffects() {
		return effects;
	}
	
	public void setEffects(List<Effect> eff){
		effects = eff;
	}
	@Override
	public void performEffects() {
		
	}
}
