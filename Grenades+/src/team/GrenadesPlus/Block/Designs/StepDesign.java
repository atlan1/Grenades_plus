package team.GrenadesPlus.Block.Designs;

import org.bukkit.block.BlockFace;
import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.block.design.GenericCuboidBlockDesign;
import org.getspout.spoutapi.block.design.Texture;

public class StepDesign extends GenericCuboidBlockDesign{
	
	private static float[] computeDesignRotation(BlockFace face){
		float[] fs = null;
		switch(face){
			case NORTH:
				fs= new float[]{.5f, 0, 0, 1, 1, 1};
				break;
			case SOUTH:
				fs= new float[]{0, 0, 0, .5f, 1, 1};
				break;
			case WEST:
				fs= new float[]{0, 0, 0, 1, 1, .5f};
				break;
			case EAST:
				fs= new float[]{0, 0, .5f, 1, 1, 1};
				break;
			case DOWN:
				fs= new float[]{0, .5f, 0, 1, 1, 1};
				break;
			default:
				fs= new float[]{0, 0, 0, 1, .5f, 1};
				break;
		}
		return fs;				
	}

	public StepDesign(Plugin plugin, Texture texture, int[] textureId, BlockFace face, boolean attach) {
		this(plugin, texture, textureId, attach?computeDesignRotation(face):new float[]{0, 0, 0, 1, .5f, 1});
	}
	
	public StepDesign(Plugin plugin, Texture texture, int[] textureId, float[] coords){
		super(plugin, texture, textureId, coords[0], coords[1], coords[2], coords[3], coords[4], coords[5]);
	}

}
