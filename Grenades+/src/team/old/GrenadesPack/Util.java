package team.old.GrenadesPack;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.server.MobEffect;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.material.MaterialData;
import org.getspout.spoutapi.player.SpoutPlayer;
import org.getspout.spoutapi.sound.SoundManager;

import team.old.GrenadesPack.GrenadesPack;


public class Util {

	public GrenadesPack plugin;
	public Util(GrenadesPack grenadesPack) {
		plugin = grenadesPack;
	}
	
	public void playCustomSound(Player player, String url)
	{
		SoundManager SM = SpoutManager.getSoundManager();
		SpoutPlayer sp = SpoutManager.getPlayer(player);
		SM.playCustomSoundEffect(plugin, sp, url, false, sp.getLocation() ,25, 50);
		SM.playGlobalCustomSoundEffect(plugin, url, false, sp.getLocation(), 100, 100);
	}
	
	public Location getHandLocation(Player p){
	    Location loc = p.getLocation().clone();
	    
	    double a = loc.getYaw() / 180D * Math.PI + Math.PI / 2;
	    double l = Math.sqrt(0.8D * 0.8D + 0.4D * 0.4D);
	    
	    loc.setX(loc.getX() + l * Math.cos(a) - 0.8D * Math.sin(a));
	    loc.setY(loc.getY() + p.getEyeHeight() - 0.2D);
	    loc.setZ(loc.getZ() + l * Math.sin(a) + 0.8D * Math.cos(a));
	    return loc;
	}
	
	public void performEffects(ArrayList<Effect> h, Location loc, int range){
		Entity orb = loc.getWorld().spawn(loc, ExperienceOrb.class);
		List<Entity> le = orb.getNearbyEntities(range, range, range);
		orb.remove();
		ArrayList<LivingEntity> livings = new ArrayList<LivingEntity>();
		for (Entity e : le) {
			if (e instanceof LivingEntity) {
				livings.add((LivingEntity) e);
			}
		}
		for(Effect eff : h){
			switch(eff.getType()){
				case 0:
					if(eff.hasEffect("explosion")){
						loc.getWorld().createExplosion(loc, (Integer) eff.getEffectValues("explosion").get(0));
					}
					if(eff.hasEffect("lightning")){
						loc.getWorld().strikeLightning(loc);
					}
					if(eff.hasEffect("smoke")){
						loc.getWorld().playEffect(loc, org.bukkit.Effect.SMOKE, (Integer) eff.getEffectValues("smoke").get(0));
					}
					if(eff.hasEffect("spawn")){
						Location l1 = loc;
						l1.setY(loc.getY()+1);
//						loc.getWorld().spawnCreature(l1, CreatureType.valueOf((String) eff.getEffectValues("spawn").get(0)));
					}
					if(eff.hasEffect("fire")){
						loc.getWorld().playEffect(loc, org.bukkit.Effect.MOBSPAWNER_FLAMES, (Integer) eff.getEffectValues("fire").get(0));
					}
					if(eff.hasEffect("place")){
						Block b = loc.getBlock();
						if(b.isEmpty()||b.isLiquid())b.setTypeId((Integer) eff.getEffectValues("place").get(0));
					}
					if(eff.hasEffect("break")){
						if(MaterialData.getBlock(loc.getBlock().getRelative(BlockFace.DOWN).getTypeId()).getHardness()<(Integer) eff.getEffectValues("break").get(0)){
							loc.getBlock().setTypeId(0);
						}
					}
					
					break;
				case 1:
					if(le!=null){
						if(eff.hasEffect("fire")){
							for(LivingEntity IAmAlive:livings){
								IAmAlive.setFireTicks((Integer) eff.getEffectValues("fire").get(1));
							}
						}
						if(eff.hasEffect("push")){
							for(LivingEntity IAmAlive : livings){
								Location lookingat = lookAt(loc, IAmAlive.getLocation());
								Vector v = lookingat.getDirection();
								v.multiply((Double) eff.getEffectValues("push").get(0));
								IAmAlive.setVelocity(v);
							}
							
						}
						if(eff.hasEffect("draw")){
							for(LivingEntity IAmAlive : livings){
								Location lookingat = lookAt(loc, IAmAlive.getLocation());
								Vector v = lookingat.getDirection();
								v.multiply((Double) eff.getEffectValues("draw").get(0)*-1);
								IAmAlive.setVelocity(v);
							}
						}
						for(int i=0;i<19;i++){
							if(eff.hasEffect("potion_"+i)){
								if(i==14||i==16)continue;
								for(LivingEntity IAmAlive:livings){
									plugin.log.info(""+IAmAlive.toString());
									CraftLivingEntity cle = (CraftLivingEntity)IAmAlive;
									cle.getHandle().addEffect(new MobEffect((Integer)eff.getEffectValues("potion_"+i).get(0), (Integer)eff.getEffectValues("potion_"+i).get(2), (Integer)eff.getEffectValues("potion_"+i).get(1)));
								}
									
							}
						}
					}
					break;
				case 2:
					if(eff.hasEffect("fire")){
						for(Block b: getSphere(loc, range)){
							b.getWorld().playEffect(b.getLocation(), org.bukkit.Effect.MOBSPAWNER_FLAMES, (Integer) eff.getEffectValues("fire").get(0));
						}
					}
					if(eff.hasEffect("explosion")){
						for(Block b: getSphere(loc, range))
							
							b.getWorld().createExplosion(b.getLocation(), (Integer) eff.getEffectValues("explosion").get(0));
						}
					}	
					if(eff.hasEffect("lightning")){
						for(Block b: getSphere(loc, range)){
								b.getWorld().strikeLightning(b.getLocation());
						}
					}	
					if(eff.hasEffect("smoke")){
						for(Block b:getSphere(loc, range)){
							b.getWorld().playEffect(b.getLocation(), org.bukkit.Effect.SMOKE, (Integer) eff.getEffectValues("smoke").get(0));
						}
					}	
					if(eff.hasEffect("spawn")){
//						for(Block b: getSphere(loc, range)){
//							if(b.isEmpty()||b.isLiquid())
//							b.getWorld().spawnCreature(loc, CreatureType.valueOf((String) eff.getEffectValues("spawn").get(0)));
//						}
					}	
					if(eff.hasEffect("place")){
						for(Block b: getSphere(loc, range)){
							if(b.isEmpty()||b.isLiquid())
							b.setTypeId((Integer) eff.getEffectValues("place").get(0));
						}
					}
					if(eff.hasEffect("break")){
						for(Block b: getSphere(loc, range)){
							if(MaterialData.getBlock(b.getTypeId()).getHardness()<(Integer) eff.getEffectValues("break").get(0)){
								b.setTypeId(0);
						}
					}
					break;
					
			}
			
			
		}
	}
	
//	public ArrayList<Player> getServerPlayer(){
//		ArrayList<Player> players = new ArrayList<Player>();
//		Player[] onP = plugin.getServer().getOnlinePlayers();
//		for(int i = 0; i<onP.length;i++){
//			players.add(onP[i]);
//		}
//		return players;
//	}
	
	public boolean hasTrigger(ArrayList<Trigger> tr, int id){
		for(Trigger t : tr){
			if(t.getId()==id){
				return true;
			}
		}
		return false;
	}
	
	public Trigger getTrigger(ArrayList<Trigger> tr, int id){
		for(Trigger t : tr){
			if(t.getId()==id){
				return t;
			}
		}
		return null;
	}

	
	//CODE from Bergerkiller
	public static Location lookAt(Location loc, Location lookat) {
        loc = loc.clone();

        double dx = lookat.getX() - loc.getX();
        double dy = lookat.getY() - loc.getY();
        double dz = lookat.getZ() - loc.getZ();

        if (dx != 0) {
            if (dx < 0) {
                loc.setYaw((float) (1.5 * Math.PI));
            } else {
                loc.setYaw((float) (0.5 * Math.PI));
            }
            loc.setYaw((float) loc.getYaw() - (float) Math.atan(dz / dx));
        } else if (dz < 0) {
            loc.setYaw((float) Math.PI);
        }

        double dxz = Math.sqrt(Math.pow(dx, 2) + Math.pow(dz, 2));

        loc.setPitch((float) -Math.atan(dy / dxz));

        loc.setYaw(-loc.getYaw() * 180f / (float) Math.PI);
        loc.setPitch(loc.getPitch() * 180f / (float) Math.PI);

        return loc;
    }
	
	public Set<Block> getBlockRadius(Block center, int radius){
		 Set<Block> blocks = new HashSet<Block>();
		    double accuracy = 0.1 * Math.PI;
		    int dx, dz;
		    for(int rad=1;rad<radius;rad++){
		    	 for (double d = 0; d < 2 * Math.PI; d += accuracy) {
			        dx = (int) (rad * Math.cos(d));
			        dz = (int) (rad * Math.sin(d));
			        blocks.add(center.getRelative(dx, 0, dz));
			    }
		    }
		   
		    return blocks;
		    
	}
	
	public List<Block> getSphere(Location center, double radius) {
		 
		List<Block> blockList = new ArrayList<Block>();
		
	    radius += 0.5;
	    //Get the square of the radius.
	    final double radSquare = Math.pow(2, radius);
	 
	    //Get the mathematical ceiling of radius.
	    final int radCeil = (int) Math.ceil(radius);
	 
	    //Get the center's coords.
	    final double centerX = center.getX();
	    final double centerY = center.getY();
	    final double centerZ = center.getZ();
	 
	    //Loop through all points inside of a sphere described by the given parameters.
	    for(double x = centerX - radCeil; x <= centerX + radCeil; x++) {
	 
	        for(double y = centerY - radCeil; y <= centerY + radCeil; y++) {
	 
	            for(double z = centerZ - radCeil; z <= centerZ + radCeil; z++) {
	 
	                //Get the square of the distance between the sphere center and the current point.
	                double distSquare = Math.pow(2, x - centerX) + Math.pow(2,y - centerY) + Math.pow(2,z - centerZ);
	 
	                //If the square of the distance to the current point is greater than the square of the radius, skip forward to the next point.
	                if (distSquare > radSquare) {
	 
	                    continue;
	                }
	 
	                Location currPoint = new Location(center.getWorld(), x, y, z);
	                //Add the block of this point to the list
	                blockList.add(currPoint.getBlock());
	            }
	        }
	    }
	    return blockList;
	}
	
	//CODE
}
