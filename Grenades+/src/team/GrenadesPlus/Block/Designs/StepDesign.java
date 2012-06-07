package team.GrenadesPlus.Block.Designs;

import org.bukkit.block.BlockFace;
import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.block.design.GenericCuboidBlockDesign;
import org.getspout.spoutapi.block.design.Texture;

public class StepDesign extends GenericCuboidBlockDesign{
	
	private static float[] computeDesignRotation(BlockFace face){
		switch(face){
			case NORTH:
				return new float[]{0, 0, .5f, 1, 1, 1};
			case SOUTH:
				return new float[]{0, 0, 0, 1, 1, .5f};
			case WEST:
				return new float[]{.5f, 0, 0, 1, 1, 1};
			case EAST:
				return new float[]{0, 0, 0, .5f, 1, 1};
			case UP:
				return new float[]{0, .5f, 0, 1, 1, 1};
			case DOWN:
				return new float[]{0, 0, 0, .5f, .5f, .5f};
		}
		return new float[]{0, 0, 0, .5f, .5f, .5f};
	}

	public StepDesign(Plugin plugin, Texture texture, int[] textureId, BlockFace face, boolean attach) {
		this(plugin, texture, textureId, attach?computeDesignRotation(face):new float[]{0, 0, 0, .5f, .5f, .5f});
	}
	
	public StepDesign(Plugin plugin, Texture texture, int[] textureId, float[] coords){
		super(plugin, texture, textureId, coords[0], coords[1], coords[2], coords[3], coords[4], coords[5]);
	}

}
