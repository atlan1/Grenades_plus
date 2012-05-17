package team.old.GrenadesPack;

import java.util.ArrayList;
import java.util.HashMap;


import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.block.SpoutBlock;
import org.getspout.spoutapi.inventory.SpoutItemStack;
import org.getspout.spoutapi.material.item.GenericCustomItem;
import org.getspout.spoutapi.player.SpoutPlayer;

public class Grenade extends GenericCustomItem {	
	
	public GrenadesPack plugin;
	private float speedmulti;
	private ArrayList<Effect> effects;
	private int radius;
	private String sound;
	private ArrayList<Trigger> triggers;
	
	public Grenade(Plugin p, String name, String texture, String soundUrl, float s,int r ,ArrayList<Effect>e,ArrayList<Trigger> tr, GrenadesPack grenpack) {
		super(p, name, texture);
		plugin = grenpack;
		speedmulti = s;
		effects = e;
		radius = r;
		triggers = tr;
		sound = soundUrl;
	}

	@Override
	public boolean onItemInteract(SpoutPlayer sp, SpoutBlock sb, BlockFace bf){
		if(plugin.grenadierSafeStatus.containsKey(sp)&&plugin.grenadierSafeStatus.get(sp).containsKey(this)&&plugin.grenadierSafeStatus.get(sp).get(this).equals(true)
				&&!(plugin.thrownGrenades.containsKey(sp)&&plugin.thrownGrenades.get(sp).contains(this))){
			throwGrenade(this.getSpeed(), sp);
			removeGrenadeFromInv(sp);
			plugin.grenadierSafeStatus.get(sp).remove(this);
		}else{
			if(plugin.thrownGrenades.containsKey(sp)&&plugin.thrownGrenades.get(sp).contains(this))return false;
			if(!plugin.grenadierSafeStatus.containsKey(sp)){
				HashMap<Grenade, Boolean> hm = new HashMap<Grenade, Boolean>();
				hm.put(this, true);
				plugin.grenadierSafeStatus.put(sp,hm );
			}else{
				plugin.grenadierSafeStatus.get(sp).put(this, true);
			}
			this.updateLocation(sp, sp);
			if(plugin.util.hasTrigger(triggers, 0))
				this.runTimer(sp);
		}
		return true;
	}

	private void throwGrenade(float speedy, SpoutPlayer sp) {
		ItemStack stack = new SpoutItemStack(this);
		Item item = sp.getWorld().dropItem(plugin.util.getHandLocation(sp), stack);
		item.setVelocity(sp.getLocation().getDirection());
		item.setVelocity(item.getVelocity().multiply(speedy));
		plugin.thrownList.add(item);
		if(!plugin.thrownItems.containsKey(sp)){
			HashMap<Grenade, Item> hm = new HashMap<Grenade, Item>();
			hm.put(this, item);
			plugin.thrownItems.put(sp,hm );
		}else{
			plugin.thrownItems.get(sp).put(this, item);
		}
		if(!plugin.thrownGrenades.containsKey(sp)){
			ArrayList<Grenade> al = new ArrayList<Grenade>();
			al.add(this);
			plugin.thrownGrenades.put(sp,al );
		}else{
			plugin.thrownGrenades.get(sp).add(this);
		}
		this.updateLocation(item, sp);
		if(plugin.util.hasTrigger(triggers, 2)&&plugin.util.getTrigger(triggers, 2).getArgs().get(0).equals(true))
				this.checkOnHit(item, sp);	
	}
	
	public void updateLocation(Entity e, SpoutPlayer sp){
		if(plugin.updateTasks.containsKey(sp)&&plugin.updateTasks.get(sp).containsKey(this)&&plugin.updateTasks.get(sp).get(this)!=null){
			plugin.updateTasks.get(sp).get(this).stop();
			plugin.updateTasks.get(sp).remove(this);
		}
		Task t = new Task(plugin, e, sp, this){
			public void run(){
				Grenade g = (Grenade) this.getArg(2);
				Entity e = (Entity) this.getArg(0);
				SpoutPlayer sp = (SpoutPlayer) this.getArg(1);	
				if(plugin.explosionLocsG.containsKey(sp)&&plugin.explosionLocsG.get(sp).containsKey(g)){
					plugin.explosionLocsG.get(sp).put(g, e.getLocation());
				}else{
					HashMap<Grenade, Location> hm = new HashMap<Grenade, Location>();
					hm.put(g, e.getLocation());
					plugin.explosionLocsG.put(sp,hm );
				}
			}
		};
		t.startRepeating(5);
		
		if(!plugin.updateTasks.containsKey(sp)){
			HashMap<Grenade, Task> hm = new HashMap<Grenade, Task>();
			hm.put(this, t);
			plugin.updateTasks.put(sp,hm );
		}else {
			plugin.updateTasks.get(sp).put(this, t);
		}
		
	}
	
	public void checkOnHit(Entity e, SpoutPlayer sp){
		Task t = new Task(plugin, e, sp, this){
			public void run(){
				Entity ent = (Entity) this.getArg(0);
				SpoutPlayer sp = (SpoutPlayer) this.getArg(1);
				Grenade g = (Grenade) this.getArg(2);
				Location l = ent.getLocation();
				Block b = l.getBlock();
				if( (!(b.getRelative(BlockFace.DOWN).isEmpty()||b.getRelative(BlockFace.DOWN).isLiquid()))||
					(!(b.getRelative(BlockFace.UP).isEmpty()||b.getRelative(BlockFace.UP).isLiquid()))||
					(!(b.getRelative(BlockFace.NORTH).isEmpty()||b.getRelative(BlockFace.NORTH).isLiquid()))||
					(!(b.getRelative(BlockFace.SOUTH).isEmpty()||b.getRelative(BlockFace.SOUTH).isLiquid()))||
					(!(b.getRelative(BlockFace.EAST).isEmpty()||b.getRelative(BlockFace.EAST).isLiquid()))||
					(!(b.getRelative(BlockFace.WEST).isEmpty()||b.getRelative(BlockFace.WEST).isLiquid()))){
					if(plugin.explosionLocsG.containsKey(sp)&&plugin.explosionLocsG.get(sp).containsKey(g)&&plugin.explosionLocsG.get(sp).get(g)!=null){
						plugin.util.performEffects(effects, plugin.explosionLocsG.get(sp).get(g), radius);
						if(sound != null){
							plugin.util.playCustomSound(sp, sound);
						}
					}
					
					cleanData(sp, g);
				}
			}
		};
		t.startRepeating(5);
		if(!plugin.onhitTasks.containsKey(sp)){
			HashMap<Grenade, Task> hm = new HashMap<Grenade, Task>();
			hm.put(this, t);
			plugin.onhitTasks.put(sp,hm );
		}else {
			plugin.onhitTasks.get(sp).put(this, t);
		}
	}
	
	private void removeGrenadeFromInv(SpoutPlayer sp){
		ItemStack i = sp.getItemInHand();
		if(i.getAmount()>1){
			i.setAmount(i.getAmount()-1);
			sp.setItemInHand(i);
		}else{
			sp.setItemInHand(null);
		}
	}
	
	private void runTimer(SpoutPlayer sp){
		Task perform1 = new Task(plugin, sp, this){
			public void run(){
					SpoutPlayer sp =  (SpoutPlayer) this.getArg(0);
					Grenade g = (Grenade) this.getArg(1);
					if(plugin.explosionLocsG.containsKey(sp)&&plugin.explosionLocsG.get(sp).containsKey(g)&&plugin.explosionLocsG.get(sp).get(g)!=null){
						plugin.util.performEffects(effects,plugin.explosionLocsG.get(sp).get(g), radius);
						if(sound != null){
							plugin.util.playCustomSound(sp, sound);
						}
					}
					
					cleanData(sp , g);
			}
		};
		int time = 24000;
		for(Trigger t : triggers){
			if(t.getId()==0)
				time = ((Integer)(t.getArgs().get(0))).intValue();
		}
		perform1.startDelayed(time);

	}
	
	public void cleanData(SpoutPlayer sp, Grenade g){
		if(plugin.updateTasks.containsKey(sp)&&plugin.updateTasks.get(sp).containsKey(g)){
			plugin.updateTasks.get(sp).get(g).stop();
			plugin.updateTasks.get(sp).remove(g);
		}
		if(plugin.onhitTasks.containsKey(sp)&&plugin.onhitTasks.get(sp).containsKey(g)){
			plugin.onhitTasks.get(sp).get(g).stop();
			plugin.onhitTasks.get(sp).remove(g);
		}
		if(plugin.thrownItems.containsKey(sp)&&plugin.thrownItems.get(sp).containsKey(g)){
			plugin.thrownItems.get(sp).get(g).remove();
			plugin.thrownItems.get(sp).remove(g);
		}
		if(plugin.thrownGrenades.containsKey(sp)&&plugin.thrownGrenades.get(sp).contains(g)){
			plugin.thrownGrenades.get(sp).remove(g);
		}
		if(plugin.explosionLocsG.containsKey(sp)&&plugin.explosionLocsG.get(sp).containsKey(g)){
			plugin.explosionLocsG.get(sp).remove(g);
		}
		if(plugin.grenadierSafeStatus.containsKey(sp)&&plugin.grenadierSafeStatus.get(sp).containsKey(g)&&plugin.grenadierSafeStatus.get(sp).get(g).equals(true)){
			plugin.grenadierSafeStatus.get(sp).remove(g);
		}
	}

	public float getSpeed() {
		return speedmulti;
	}

	public void setSpeed(float speedmulti) {
		this.speedmulti = speedmulti;
	}


	public ArrayList<Effect> getEffects() {
		return effects;
	}

	public void setEffects(ArrayList<Effect> effects) {
		this.effects = effects;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public ArrayList<Trigger> getTriggers() {
		return triggers;
	}

	public void setTriggers(ArrayList<Trigger> triggers) {
		this.triggers = triggers;
	}
}
