package team.GrenadesPlus.Item;

import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.block.design.BlockDesign;
import org.getspout.spoutapi.material.block.GenericCustomBlock;


public class Explosive extends GenericCustomBlock implements Placeable{

	public Explosive(Plugin plugin, String name, int blockId, int metadata,
			BlockDesign design) {
		super(plugin, name, blockId, metadata, design);
	}

	@Override
	public boolean onPlace() {
		return false;
	}
}
