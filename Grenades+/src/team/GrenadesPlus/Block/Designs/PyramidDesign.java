package team.GrenadesPlus.Block.Designs;

import org.bukkit.block.BlockFace;
import org.getspout.spoutapi.block.design.GenericBlockDesign;
import org.getspout.spoutapi.block.design.Quad;
import org.getspout.spoutapi.block.design.SubTexture;
import org.getspout.spoutapi.block.design.Texture;

import team.GrenadesPlus.GrenadesPlus;

public class PyramidDesign extends GenericBlockDesign{

	public PyramidDesign(GrenadesPlus plugin, Texture texture, int[] textureId, BlockFace face, boolean attach) {
		setBoundingBox(0, 0, 0, 1, 1, 1);
		setRenderPass(0);
		setQuadNumber(10);
		setTexture(plugin, texture);
		SubTexture baseTex = texture.getSubTexture(textureId[0]);
		SubTexture tri1Tex = texture.getSubTexture(textureId[1]);
		SubTexture tri2Tex = texture.getSubTexture(textureId[2]);
		SubTexture tri3Tex = texture.getSubTexture(textureId[3]);
		SubTexture tri4Tex = texture.getSubTexture(textureId[4]);
		
		Quad base = new Quad(0, baseTex);
		Quad tri1 = new Quad(1, tri1Tex);
		Quad tri2 = new Quad(2, tri2Tex);
		Quad tri3 = new Quad(3, tri3Tex);
		Quad tri4 = new Quad(4, tri4Tex);
		Quad _base = new Quad(5, baseTex);
		Quad _tri1 = new Quad(6, tri1Tex);
		Quad _tri2 = new Quad(7, tri2Tex);
		Quad _tri3 = new Quad(8, tri3Tex);
		Quad _tri4 = new Quad(9, tri4Tex);
		
		if(face==null) face = BlockFace.UP;
		switch(face){
			case NORTH:
				base.addVertex(0, 1, 1, 1);
				base.addVertex(1, 1, 0, 1);
				base.addVertex(2, 1, 0, 0);
				base.addVertex(3, 1, 1, 0);
				_base.addVertex(0, 1, 1, 0);
				_base.addVertex(1, 1, 0, 0);
				_base.addVertex(2, 1, 0, 1);
				_base.addVertex(3, 1, 1, 1);
				
				tri1.addVertex(0, 0, .5f, .5f);
				tri1.addVertex(1, 1, 1, 0);
				tri1.addVertex(2, 1, 0, 0);
				tri1.addVertex(3, 0, .5f, .5f);
				_tri1.addVertex(0, 0, .5f, .5f);
				_tri1.addVertex(1, 1, 0, 0);
				_tri1.addVertex(2, 1, 1, 0);
				_tri1.addVertex(3, 0, .5f, .5f);
				
				tri2.addVertex(0, 0, .5f, .5f);
				tri2.addVertex(1, 1, 1, 1);
				tri2.addVertex(2, 1, 1, 0);
				tri2.addVertex(3, 0, .5f, .5f);
				_tri2.addVertex(0, 0, .5f, .5f);
				_tri2.addVertex(1, 1, 1, 0);
				_tri2.addVertex(2, 1, 1, 1);
				_tri2.addVertex(3, 0, .5f, .5f);
				
				tri3.addVertex(0, 0, .5f, .5f);
				tri3.addVertex(1, 1, 0, 1);
				tri3.addVertex(2, 1, 1, 1);
				tri3.addVertex(3, 0, .5f, .5f);
				_tri3.addVertex(0, 0, .5f, .5f);
				_tri3.addVertex(1, 1, 1, 1);
				_tri3.addVertex(2, 1, 0, 1);
				_tri3.addVertex(3, 0, .5f, .5f);
				
				tri4.addVertex(0, 0, .5f, .5f);
				tri4.addVertex(1, 1, 0, 0);
				tri4.addVertex(2, 1, 0, 1);
				tri4.addVertex(3, 0, .5f, .5f);
				_tri4.addVertex(0, 0, .5f, .5f);
				_tri4.addVertex(1, 1, 0, 1);
				_tri4.addVertex(2, 1, 0, 0);
				_tri4.addVertex(3, 0, .5f, .5f);
				break;
			case EAST:
				base.addVertex(0, 0, 1, 1);
				base.addVertex(1, 0, 0, 1);
				base.addVertex(2, 1, 0, 1);
				base.addVertex(3, 1, 1, 1);
				_base.addVertex(0, 1, 1, 1);
				_base.addVertex(1, 1, 0, 1);
				_base.addVertex(2, 0, 0, 1);
				_base.addVertex(3, 0, 1, 1);
			
				tri1.addVertex(0, .5f, .5f, 0);
				tri1.addVertex(1, 1, 1, 1);
				tri1.addVertex(2, 1, 0, 1);
				tri1.addVertex(3, .5f, .5f, 0);
				_tri1.addVertex(0, .5f, .5f, 0);
				_tri1.addVertex(1, 1, 0, 1);
				_tri1.addVertex(2, 1, 1, 1);
				_tri1.addVertex(3, .5f, .5f, 0);
				
				tri2.addVertex(0, .5f, .5f, 0);
				tri2.addVertex(1, 0, 1, 1);
				tri2.addVertex(2, 1, 1, 1);
				tri2.addVertex(3, .5f, .5f, 0);
				_tri2.addVertex(0, .5f, .5f, 0);
				_tri2.addVertex(1, 1, 1, 1);
				_tri2.addVertex(2, 0, 1, 1);
				_tri2.addVertex(3, .5f, .5f, 0);
				
				tri3.addVertex(0, .5f, .5f, 0);
				tri3.addVertex(1, 0, 0, 1);
				tri3.addVertex(2, 0, 1, 1);
				tri3.addVertex(3, .5f, .5f, 0);
				_tri3.addVertex(0, .5f, .5f, 0);
				_tri3.addVertex(1, 0, 1, 1);
				_tri3.addVertex(2, 0, 0, 1);
				_tri3.addVertex(3, .5f, .5f, 0);
				
				tri4.addVertex(0, .5f, .5f, 0);
				tri4.addVertex(1, 1, 0, 1);
				tri4.addVertex(2, 0, 0, 1);
				tri4.addVertex(3, .5f, .5f, 0);
				_tri4.addVertex(0, .5f, .5f, 0);
				_tri4.addVertex(1, 0, 0, 1);
				_tri4.addVertex(2, 1, 0, 1);
				_tri4.addVertex(3, .5f, .5f, 0);
				break;
			case SOUTH:
				base.addVertex(0, 0, 1, 0);
				base.addVertex(1, 0, 0, 0);
				base.addVertex(2, 0, 0, 1);
				base.addVertex(3, 0, 1, 1);
				_base.addVertex(0, 0, 1, 1);
				_base.addVertex(1, 0, 0, 1);
				_base.addVertex(2, 0, 0, 0);
				_base.addVertex(3, 0, 1, 0);
				
				tri1.addVertex(0, 1, .5f, .5f);
				tri1.addVertex(1, 0, 1, 1);
				tri1.addVertex(2, 0, 0, 1);
				tri1.addVertex(3, 1, .5f, .5f);
				_tri1.addVertex(0, 1, .5f, .5f);
				_tri1.addVertex(1, 0, 0, 1);
				_tri1.addVertex(2, 0, 1, 1);
				_tri1.addVertex(3, 1, .5f, .5f);
				
				tri2.addVertex(0, 1, .5f, .5f);
				tri2.addVertex(1, 0, 1, 0);
				tri2.addVertex(2, 0, 1, 1);
				tri2.addVertex(3, 1, .5f, .5f);
				_tri2.addVertex(0, 1, .5f, .5f);
				_tri2.addVertex(1, 0, 1, 1);
				_tri2.addVertex(2, 0, 1, 0);
				_tri2.addVertex(3, 1, .5f, .5f);
				
				tri3.addVertex(0, 1, .5f, .5f);
				tri3.addVertex(1, 0, 0, 0);
				tri3.addVertex(2, 0, 1, 0);
				tri3.addVertex(3, 1, .5f, .5f);
				_tri3.addVertex(0, 1, .5f, .5f);
				_tri3.addVertex(1, 0, 1, 0);
				_tri3.addVertex(2, 0, 0, 0);
				_tri3.addVertex(3, 1, .5f, .5f);
				
				tri4.addVertex(0, 1, .5f, .5f);
				tri4.addVertex(1, 0, 0, 1);
				tri4.addVertex(2, 0, 0, 0);
				tri4.addVertex(3, 1, .5f, .5f);
				_tri4.addVertex(0, 1, .5f, .5f);
				_tri4.addVertex(1, 0, 0, 0);
				_tri4.addVertex(2, 0, 0, 1);
				_tri4.addVertex(3, 1, .5f, .5f);
				break;
			case WEST:
				base.addVertex(0, 0, 1, 0);
				base.addVertex(1, 0, 0, 0);
				base.addVertex(2, 1, 0, 0);
				base.addVertex(3, 1, 1, 0);
				_base.addVertex(0, 1, 1, 0);
				_base.addVertex(1, 1, 0, 0);
				_base.addVertex(2, 0, 0, 0);
				_base.addVertex(3, 0, 1, 0);
				
				tri1.addVertex(0, .5f, .5f, 1);
				tri1.addVertex(1, 0, 1, 0);
				tri1.addVertex(2, 0, 0, 0);
				tri1.addVertex(3, .5f, .5f, 1);
				_tri1.addVertex(0, .5f, .5f, 1);
				_tri1.addVertex(1, 0, 0, 0);	
				_tri1.addVertex(2, 0, 1, 0);
				_tri1.addVertex(3, .5f, .5f, 1);
				
				tri2.addVertex(0, .5f, .5f, 1);
				tri2.addVertex(1, 1, 1, 0);
				tri2.addVertex(2, 0, 1, 0);
				tri2.addVertex(3, .5f, .5f, 1);
				_tri2.addVertex(0, .5f, .5f, 1);
				_tri2.addVertex(1, 0, 1, 0);
				_tri2.addVertex(2, 1, 1, 0);
				_tri2.addVertex(3, .5f, .5f, 1);
				
				tri3.addVertex(0, .5f, .5f, 1);
				tri3.addVertex(1, 1, 0, 0);
				tri3.addVertex(2, 1, 1, 0);
				tri3.addVertex(3, .5f, .5f, 1);
				_tri3.addVertex(0, .5f, .5f, 1);
				_tri3.addVertex(1, 1, 1, 0);
				_tri3.addVertex(2, 1, 0, 0);
				_tri3.addVertex(3, .5f, .5f, 1);
				
				tri4.addVertex(0, .5f, .5f, 1);
				tri4.addVertex(1, 0, 0, 0);
				tri4.addVertex(2, 1, 0, 0);
				tri4.addVertex(3, .5f, .5f, 1);
				_tri4.addVertex(0, .5f, .5f, 1);
				_tri4.addVertex(1, 1, 0, 0);
				_tri4.addVertex(2, 0, 0, 0);
				_tri4.addVertex(3, .5f, .5f, 1);
				break;
			case DOWN:
				base.addVertex(0, 1, 1, 0);
				base.addVertex(1, 0, 1, 0);
				base.addVertex(2, 0, 1, 1);
				base.addVertex(3, 1, 1, 1);
				_base.addVertex(0, 1, 1, 1);
				_base.addVertex(1, 0, 1, 1);
				_base.addVertex(2, 0, 1, 0);
				_base.addVertex(3, 1, 1, 0);
				
				tri1.addVertex(0, .5f, 0, .5f);
				tri1.addVertex(1, 0, 1, 0);
				tri1.addVertex(2, 1, 1, 0);
				tri1.addVertex(3, .5f, 0, .5f);
				_tri1.addVertex(0, .5f, 0, .5f);
				_tri1.addVertex(1, 1, 1, 0);
				_tri1.addVertex(2, 0, 1, 0);
				_tri1.addVertex(3, .5f, 0, .5f);
				
				tri2.addVertex(0, .5f, 0, .5f);
				tri2.addVertex(1, 0, 1, 1);
				tri2.addVertex(2, 0, 1, 0);
				tri2.addVertex(3, .5f, 0, .5f);
				_tri2.addVertex(0, .5f, 0, .5f);
				_tri2.addVertex(1, 0, 1, 0);
				_tri2.addVertex(2, 0, 1, 1);
				_tri2.addVertex(3, .5f, 0, .5f);
				
				tri3.addVertex(0, .5f, 0, .5f);
				tri3.addVertex(1, 1, 1, 1);
				tri3.addVertex(2, 0, 1, 1);
				tri3.addVertex(3, .5f, 0, .5f);
				_tri3.addVertex(0, .5f, 0, .5f);
				_tri3.addVertex(1, 0, 1, 1);
				_tri3.addVertex(2, 1, 1, 1);
				_tri3.addVertex(3, .5f, 0, .5f);
				
				tri4.addVertex(0, .5f, 0, .5f);
				tri4.addVertex(1, 1, 1, 0);
				tri4.addVertex(2, 1, 1, 1);
				tri4.addVertex(3, .5f, 0, .5f);
				_tri4.addVertex(0, .5f, 0, .5f);
				_tri4.addVertex(1, 1, 1, 1);
				_tri4.addVertex(2, 1, 1, 0);
				_tri4.addVertex(3, .5f, 0, .5f);
				break;
			default:
				base.addVertex(0, 1, 0, 0);
				base.addVertex(1, 1, 0, 1);
				base.addVertex(2, 0, 0, 1);
				base.addVertex(3, 0, 0, 0);
				_base.addVertex(0, 0, 0, 0);
				_base.addVertex(1, 0, 0, 1);
				_base.addVertex(2, 1, 0, 1);
				_base.addVertex(3, 1, 0, 0);
				
				tri1.addVertex(0, .5f, 1, .5f);
				tri1.addVertex(1, 1, 0, 0);
				tri1.addVertex(2, 0, 0, 0);
				tri1.addVertex(3, .5f, 1, .5f);
				_tri1.addVertex(0, .5f, 1, .5f);
				_tri1.addVertex(1, 0, 0, 0);
				_tri1.addVertex(2, 1, 0, 0);
				_tri1.addVertex(3, .5f, 1, .5f);
				
				tri2.addVertex(0, .5f, 1, .5f);
				tri2.addVertex(1, 1, 0, 1);
				tri2.addVertex(2, 1, 0, 0);
				tri2.addVertex(3, .5f, 1, .5f);
				_tri2.addVertex(0, .5f, 1, .5f);
				_tri2.addVertex(1, 1, 0, 0);
				_tri2.addVertex(2, 1, 0, 1);
				_tri2.addVertex(3, .5f, 1, .5f);
				
				tri3.addVertex(0, .5f, 1, .5f);
				tri3.addVertex(1, 0, 0, 1);
				tri3.addVertex(2, 1, 0, 1);
				tri3.addVertex(3, .5f, 1, .5f);
				_tri3.addVertex(0, .5f, 1, .5f);
				_tri3.addVertex(1, 1, 0, 1);
				_tri3.addVertex(2, 0, 0, 1);
				_tri3.addVertex(3, .5f, 1, .5f);
				
				tri4.addVertex(0, .5f, 1, .5f);
				tri4.addVertex(1, 0, 0, 0);
				tri4.addVertex(2, 0, 0, 1);
				tri4.addVertex(3, .5f, 1, .5f);
				_tri4.addVertex(0, .5f, 1, .5f);
				_tri4.addVertex(1, 0, 0, 1);
				_tri4.addVertex(2, 0, 0, 0);
				_tri4.addVertex(3, .5f, 1, .5f);
				break;
		}
		setQuad(base).
		setQuad(tri1).
		setQuad(tri2).
		setQuad(tri3).
		setQuad(tri4).
		setQuad(_base).
		setQuad(_tri1).
		setQuad(_tri2).
		setQuad(_tri3).
		setQuad(_tri4);
	}
}
