package team.old.GrenadesPack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.block.design.BlockDesign;
import org.getspout.spoutapi.material.block.GenericCustomBlock;
import org.getspout.spoutapi.player.SpoutPlayer;

public class Placeable extends GenericCustomBlock {

	GrenadesPack plugin;
	private int radius;
	private ArrayList<Effect> effects;
	private ArrayList<Trigger> triggers;
	private int frequency;
	private int type = 0;
	private String sound;
	public Placeable(Plugin pl, String name,
			BlockDesign bd, String s,float hardness, int r, int t, ArrayList<Effect> eff, ArrayList<Trigger> tr,  GrenadesPack gp) {
		super(pl, name, false, bd);
		plugin = gp;
		this.setHardness(hardness);
		effects = eff;
		radius = r;
		type = t;
		triggers = tr;
		sound = s;
		if(plugin.util.hasTrigger(triggers, 1)){
			Trigger trigg = plugin.util.getTrigger(triggers, 1);
			String nameofdet = (String) trigg.getArgs().get(0);
			int f = -1;
			for(Detonator d : plugin.allDetonators){
				if(d.getName().equalsIgnoreCase(nameofdet)){
					f = d.getFrequency();
				}
			}
			frequency = f;
		}
	}
	
//	EVENTS
	@Override
	public void onBlockClicked(World w, int x, int y, int z,
			SpoutPlayer sp) {
			if(this.getType()==1){
				Location l = new Location(w,x,y,z);
				if(plugin.util.hasTrigger(triggers, 0))
					this.runTimer(sp,  l);
				if(plugin.util.hasTrigger(triggers, 3)){
					this.checkShock(sp, l);
				}
			}
	}
	
	@Override
	public void onBlockPlace(World w, int x, int y, int z, LivingEntity le){
		if(le instanceof SpoutPlayer){
			SpoutPlayer sp = (SpoutPlayer)le;
			Location l = new Location(w,x,y,z);
			if(!plugin.placeableData.containsKey(sp)){
				HashMap<Placeable, ArrayList<Block>> hm1 = new HashMap<Placeable,ArrayList<Block>>();
				ArrayList<Block> block = new ArrayList<Block>();
				block.add(l.getBlock());
				hm1.put(this, block);
				plugin.placeableData.put(sp, hm1 );
				plugin.log.info("added everything");
			}else if(plugin.placeableData.containsKey(sp)&&!plugin.placeableData.get(sp).containsKey(this)){
				ArrayList<Block> block = new ArrayList<Block>();
				block.add(l.getBlock());
				plugin.placeableData.get(sp).put(this, block);
				plugin.log.info("added placeable and block");
			}else if(plugin.placeableData.containsKey(sp)&&plugin.placeableData.get(sp).containsKey(this)){
				plugin.placeableData.get(sp).get(this).add(l.getBlock());
				plugin.log.info("added block");
			}
			if(this.getType()==0){	
				if(plugin.util.hasTrigger(triggers, 3)){
					this.checkShock(sp, l);
				}
				if(plugin.util.hasTrigger(triggers, 0)){
					this.runTimer(sp,  l);
				}
			}
		}
		
	}
	

	@Override
	public void onBlockDestroyed(World w, int x, int y, int z,
			LivingEntity le) {
		Location l = new Location(w,x,y,z);
			for(Player s : plugin.getServer().getOnlinePlayers()){
				if(s instanceof SpoutPlayer){
					SpoutPlayer ss = (SpoutPlayer) s;
					if(plugin.placeableData.containsKey(ss)&&plugin.placeableData.get(ss).containsKey(this)){
						ArrayList<Block> blocks =  plugin.placeableData.get(ss).get(this);
						for(int i=0;i<blocks.size();i++){
							if(l.getBlock()==blocks.get(i)){
								cleanData(ss, this, l);
							}
						}
					}
				}
		}
		
		
	}

	@Override
	public boolean onBlockInteract(World w, int x, int y, int z,
			SpoutPlayer sp){
		return true;
	}


	public Task checkShock(SpoutPlayer sp, Location l){
		Task t = new Task(plugin, l, sp, plugin.util.getTrigger(triggers, 3).getArgs().get(0), this){
			public void run(){
				Location loc = (Location) this.getArg(0);
				SpoutPlayer sp = (SpoutPlayer) this.getArg(1);
				int r = this.getIntArg(2);
				Placeable p = (Placeable) this.getArg(3);
				List<LivingEntity> lents = new ArrayList<LivingEntity>();
				Entity eo = loc.getWorld().spawn(loc, ExperienceOrb.class);
				List<Entity> ents = eo.getNearbyEntities(r, r, r);
				eo.remove();
					for(Entity e : ents ){
						if(e instanceof LivingEntity&&!e.equals(sp)){
							lents.add((LivingEntity)e);
						}
					}
				if(!lents.isEmpty()){
					if(plugin.placeableData.containsKey(sp)&&plugin.placeableData.get(sp).containsKey(p)&&plugin.placeableData.get(sp).get(p).contains(loc.getBlock())){
						if(!plugin.placeableData.get(sp).get(p).get(plugin.placeableData.get(sp).get(p).indexOf(loc.getBlock())).isEmpty()&&!plugin.placeableData.get(sp).get(p).get(plugin.placeableData.get(sp).get(p).indexOf(loc.getBlock())).isLiquid()){
							plugin.util.performEffects(effects,loc, radius);
							if(sound != null){
								plugin.util.playCustomSound(sp, sound);
							}
						}
						
					}
					cleanData(sp , p, loc );
					this.stop();
				}	
			}
		};
		t.startRepeating(5);
		return t;
	}
	
	public Task runTimer(SpoutPlayer sp, Location l){
		Task perform1 = new Task(plugin, sp,l ,  this){
			public void run(){
					SpoutPlayer sp =  (SpoutPlayer) this.getArg(0);
					Location l = (Location) this.getArg(1);
					Placeable p = (Placeable) this.getArg(2);
					if(plugin.placeableData.containsKey(sp)&&plugin.placeableData.get(sp).containsKey(p)&&plugin.placeableData.get(sp).get(p).contains(l.getBlock())){
						if(!plugin.placeableData.get(sp).get(p).get(plugin.placeableData.get(sp).get(p).indexOf(l.getBlock())).isEmpty()&&!plugin.placeableData.get(sp).get(p).get(plugin.placeableData.get(sp).get(p).indexOf(l.getBlock())).isLiquid()){
							plugin.util.performEffects(effects,l, radius);
							if(sound != null){
								plugin.util.playCustomSound(sp, sound);
							}
						}
						
					}
					cleanData(sp , p, l );
			}
		};
		int time = 24000;
		for(Trigger t : triggers){
			if(t.getId()==0)
				time = (Integer)t.getArgs().get(0);
		}
		perform1.startDelayed(time);
		return perform1;

	}
	
	public void cleanData(SpoutPlayer sp, Placeable g, Location l){
		if(plugin.placeableData.containsKey(sp)&&plugin.placeableData.get(sp).containsKey(g)&&plugin.placeableData.get(sp).get(g).contains(l.getBlock())){
			if(!plugin.placeableData.get(sp).get(g).get(plugin.placeableData.get(sp).get(g).indexOf(l.getBlock())).isEmpty()&&!!plugin.placeableData.get(sp).get(g).get(plugin.placeableData.get(sp).get(g).indexOf(l.getBlock())).isLiquid()){
				plugin.placeableData.get(sp).get(g).remove(l.getBlock());
			}else{
				l.getBlock().setTypeId(0);
				plugin.placeableData.get(sp).get(g).remove(l.getBlock());
			}
			
		}
	}
	
	//GETTERS AND SETTERS

	public int getRange() {
		return radius;
	}

	public void setRange(int range) {
		this.radius = range;
	}

	public ArrayList<Effect> getEffects() {
		return effects;
	}

	public void setEffects(ArrayList<Effect> effects) {
		this.effects = effects;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public String getSound() {
		return sound;
	}

	public void setSound(String sound) {
		this.sound = sound;
	}
}
