package team.GrenadesPlus.Trigger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.entity.CraftItem;
import org.bukkit.entity.Item;

import team.ApiPlus.API.EffectHolder;
import team.ApiPlus.API.PropertyHolder;
import team.ApiPlus.API.Trigger.Trigger;
import team.ApiPlus.API.Trigger.TriggerTask;
import team.ApiPlus.API.Trigger.TriggerTaskModel;
import team.ApiPlus.API.Trigger.TriggerType;
import team.ApiPlus.Util.Task;
import team.GrenadesPlus.GrenadesPlus;
import team.GrenadesPlus.Util.Explosive;
import team.GrenadesPlus.Util.Grenadier;
import team.GrenadesPlus.Util.Util;

public enum ExplosiveTriggerType implements PropertyHolder, TriggerType{


	ONHIT(true), DETONATOR(false), SHOCK(false), TIME(null), REDSTONE(false);
	
	// This boolean defines either to use a trigger for throwables or for placeables or for both. (t=true, p=false, b=null)
	private Boolean usage;
	
	private static List<ExplosiveTriggerType> throwableTriggers = new ArrayList<ExplosiveTriggerType>();
	private static List<ExplosiveTriggerType> placeableTriggers = new ArrayList<ExplosiveTriggerType>();
	
	private TriggerActivationType tat;
	private Trigger t;
	
	private TriggerTaskModel activation;
	private TriggerTaskModel triggered = new TriggerTaskModel(GrenadesPlus.plugin,  "delayed", 1, Explosive.class, Grenadier.class, Location.class){
		public void run(Task t){
			Explosive e = (Explosive) t.getArg(0);
			Grenadier g = (Grenadier) t.getArg(1);
			Location target = (Location) t.getArg(2);
			if(e instanceof EffectHolder){
				((EffectHolder) e).performEffects(e, g, target);
			}
		}
	};
	
	private Map<String, Object> properties = new HashMap<String, Object>();
	
	private ExplosiveTriggerType(Boolean b){
		usage = b;
	}
	

	@Override
	public TriggerTaskModel activationTask() {
		return activation;
	}

	@Override
	public TriggerTaskModel triggeredTask() {
		return triggered;
	}
	
	public static ExplosiveTriggerType ONHIT(Trigger t, TriggerActivationType tat){
		ExplosiveTriggerType tt = ExplosiveTriggerType.ONHIT;
		tt.tat = tat;
		tt.t = t;
		tt.activation = new TriggerTaskModel(GrenadesPlus.plugin, "repeating", 10, Explosive.class, Grenadier.class, Item.class){
			public void run(Task t) {
				Explosive e = (Explosive) t.getArg(0);
				Grenadier g = (Grenadier) t.getArg(1);
				Item i = (Item)t.getArg(2);
				CraftItem ci = (CraftItem) i;
				Location n, o, s, w, itemLoc = i.getLocation();
				n = i.getLocation().getBlock().getRelative(BlockFace.NORTH).getLocation();
				o = i.getLocation().getBlock().getRelative(BlockFace.EAST).getLocation();
				s = i.getLocation().getBlock().getRelative(BlockFace.SOUTH).getLocation();
				w = i.getLocation().getBlock().getRelative(BlockFace.WEST).getLocation();
				if(i.isDead()||(!Util.isTransparent(n.getBlock())&&Util.distanceSmallerThan(itemLoc, n, 0.1d))||(!Util.isTransparent(o.getBlock())&&Util.distanceSmallerThan(itemLoc, o, 0.1d))||(!Util.isTransparent(w.getBlock())&&Util.distanceSmallerThan(itemLoc, w, 0.1d))||(!Util.isTransparent(s.getBlock())&&Util.distanceSmallerThan(itemLoc, s, 0.1d))||ci.getHandle().onGround){
					((TriggerTask)t).getTrigger().triggered(e, g, itemLoc);
					i.remove();
					t.stopTask();
				}
			}
		};
		return tt;
	}
	
	public static ExplosiveTriggerType DETONATOR(Trigger t, TriggerActivationType tat){
		ExplosiveTriggerType tt = ExplosiveTriggerType.DETONATOR;
		tt.tat = tat;
		tt.t = t;
		tt.activation = new TriggerTaskModel(GrenadesPlus.plugin, "delayed", 0, Explosive.class, Grenadier.class, List.class){
			@SuppressWarnings("unchecked")
			public void run(Task t) {
				Explosive e = (Explosive) t.getArg(0);
				Grenadier g = (Grenadier) t.getArg(1);
				List<Block> list = (List<Block>)t.getArg(2);
				if(g.isDetonated()){
					g.setDetonated(false);
					for(Block b : list){
						((TriggerTask) t).getTrigger().triggered(e, g, b);
					}
				}
			}
		};
		return tt;
	}
	
	public static ExplosiveTriggerType SHOCK(Trigger t, TriggerActivationType tat, int[]rad){
		ExplosiveTriggerType tt = ExplosiveTriggerType.SHOCK;
		tt.addProperty("RADIUSX", rad[0]);
		tt.addProperty("RADIUSY", rad[1]);
		tt.addProperty("RADIUSZ", rad[2]);
		tt.tat = tat;
		tt.t = t;
		tt.activation = new TriggerTaskModel(GrenadesPlus.plugin, "repeating", 10, Explosive.class, Grenadier.class, Block.class){
			public void run(Task t) {
				Location l = (Location) t.getArg(2);
				Explosive e = (Explosive) t.getArg(0);
				Grenadier g = (Grenadier) t.getArg(1);
				if(!Util.getNearbyEntities(l, ((Integer)((PropertyHolder) ((TriggerTask) t).getTrigger().getTriggerType()).getProperty("RADIUSX")), ((Integer)((PropertyHolder) ((TriggerTask) t).getTrigger().getTriggerType()).getProperty("RADIUSY")), ((Integer)((PropertyHolder) ((TriggerTask) t).getTrigger().getTriggerType()).getProperty("RADIUSZ"))).isEmpty()){
					((TriggerTask) t).getTrigger().triggered(e, g, l);
					t.stopTask();
				}
			}
		};
		return tt;
	}
	
	public static ExplosiveTriggerType TIME(Trigger t, TriggerActivationType tat, int time){
		ExplosiveTriggerType tt = ExplosiveTriggerType.TIME;
		tt.addProperty("TIME", time);
		tt.tat = tat;
		tt.t = t;
		tt.activation = new TriggerTaskModel(GrenadesPlus.plugin, "delayed", time, Explosive.class, Grenadier.class, Item.class, Block.class){
			public void run(Task t) {
				Explosive e = (Explosive) t.getArg(0);
				Grenadier g = (Grenadier) t.getArg(1);
				Item i =  (Item)t.getArg(2);
				Block b =  (Block)t.getArg(3);
				((TriggerTask) t).getTrigger().triggered(e, g, i!=null?i.getLocation():b.getLocation());
				t.stopTask();
			}
		};
		return tt;
	}
	
	public static ExplosiveTriggerType REDSTONE(Trigger t, TriggerActivationType tat, boolean active){
		ExplosiveTriggerType tt = ExplosiveTriggerType.REDSTONE;
		tt.addProperty("ACTIVE", active);
		tt.tat = tat;
		tt.t = t;
		tt.activation = new TriggerTaskModel(GrenadesPlus.plugin, "repeating", 10, Explosive.class, Grenadier.class, Block.class){
			public void run(Task t) { 
				Block b = (Block) t.getArg(2);
				Explosive e = (Explosive) t.getArg(0);
				Grenadier g = (Grenadier) t.getArg(1);
				if(((Boolean)((PropertyHolder) ((TriggerTask) t).getTrigger().getTriggerType()).getProperty("ACTIVE")&&b.isBlockPowered())||(!(Boolean)((PropertyHolder) ((TriggerTask) t).getTrigger().getTriggerType()).getProperty("ACTIVE")&&!b.isBlockPowered())){
					((TriggerTask) t).getTrigger().triggered(e, g, b.getLocation());
					t.stopTask();
				}
			}
		};
		return tt;
	}
	
	private Boolean getUsage(){
		return usage;
	}

	public TriggerActivationType getTriggerActivationType(){
		return this.tat;
	}
	
	static {
		for(ExplosiveTriggerType t : values()){
			if (t.getUsage()==null) {
				throwableTriggers.add(t);
				placeableTriggers.add(t);
			} else if (!t.getUsage()) {
				placeableTriggers.add(t);
			} else {
				throwableTriggers.add(t);
			}
		}
	}
	
	public static boolean isThrowableTrigger(ExplosiveTriggerType t){
		return throwableTriggers.contains(t);
	}
	
	public static boolean isPlaceableTrigger(ExplosiveTriggerType t){
		return placeableTriggers.contains(t);
	}
	
	public static List<ExplosiveTriggerType> getThrowableTriggers(){
		return throwableTriggers;
	}
	
	public static List<ExplosiveTriggerType> getPlaceableTriggers(){
		return placeableTriggers;
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
		this.properties = new HashMap<String, Object>(properties);
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


	public Trigger getTrigger() {
		return t;
	}


	public void setTrigger(Trigger trigger) {
		this.t = trigger;
	}
	
}
