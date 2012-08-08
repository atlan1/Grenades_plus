package team.GrenadesPlus.Block.Designs;

import org.bukkit.block.BlockFace;
import org.getspout.spoutapi.block.design.BlockDesign;
import org.getspout.spoutapi.block.design.GenericCubeBlockDesign;
import org.getspout.spoutapi.block.design.Texture;

import team.GrenadesPlus.GrenadesPlus;

public enum DesignType {

	CUBE(6), PYRAMID(5), STEP(6);
	
	boolean attach = false;
	int faces = 0;
	
	private DesignType(int faces){
		this.faces = faces;
	}
	
	public int getFaces(){
		return faces;
	}
	
	public boolean isAttaching(){
		return attach;
	}
	
	public void setAttaching(boolean a){
		this.attach = a;
	}

	public static BlockDesign getBlockDesign(DesignType des, String texture, int width, int heigth, int sprite, int[] usedIds, BlockFace bf) throws Exception {
		if(usedIds.length<des.getFaces()) throw new Exception("Too low used texture ids, for design "+des.name().toLowerCase()+"; req. number of ids: "+des.getFaces());
		if(width%2!=0||heigth%2!=0||sprite%2!=0) throw new Exception("With, Heigth and Sprite size of a texture must be multiples of 2!");
			switch(des){
				case CUBE:
					Texture tex = new Texture(GrenadesPlus.plugin, texture, width, heigth, sprite);
					return new GenericCubeBlockDesign(GrenadesPlus.plugin, tex, usedIds);
				case PYRAMID:
					Texture tex2 = new Texture(GrenadesPlus.plugin, texture, width, heigth, sprite);
					return new PyramidDesign(GrenadesPlus.plugin, tex2, usedIds, bf, des.isAttaching());
				case STEP:
					Texture tex1 = new Texture(GrenadesPlus.plugin, texture, width, heigth, sprite);
					return new StepDesign(GrenadesPlus.plugin, tex1, usedIds, bf, des.isAttaching());
			}
			return null;
		}
}
