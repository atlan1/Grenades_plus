package team.GrenadesPlus.Trigger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.getspout.spoutapi.inventory.SpoutItemStack;
import team.ApiPlus.API.Trigger.TriggerTask;
import team.ApiPlus.Util.Task;
import team.GrenadesPlus.GrenadesPlus;
import team.GrenadesPlus.API.Event.DetonateEvent;
import team.GrenadesPlus.API.Event.ThrowEvent;
import team.GrenadesPlus.Block.Placeable;
import team.GrenadesPlus.Manager.DBManager;
import team.GrenadesPlus.Util.ExplosiveUtils;
import team.GrenadesPlus.Util.Grenadier;
import team.GrenadesPlus.GrenadesPlusPlayer;
import team.GrenadesPlus.Util.PlayerUtils;

public class TriggerListener implements Listener {
	
	private GrenadesPlus plugin;
	
	public TriggerListener(GrenadesPlus p){
		plugin = p;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@SuppressWarnings("unchecked")
	@EventHandler
	public void onDetonate(DetonateEvent e){
		boolean exploded = false;
		Map<Placeable, List<Block>> ps = new HashMap<Placeable, List<Block>>();
		for(Block b : e.getBlockList()){
			if(ExplosiveUtils.isPlaceable(b)){
				Placeable p = ExplosiveUtils.getPlaceable(b);
				if(!ps.containsKey(p))
					ps.put(p, new ArrayList<Block>());
				ps.get(p).add(b);
			}else{
				e.getGrenadier().removeBlock(b);
			}
		}
		for(Placeable p : new HashSet<Placeable>(ps.keySet())){
			List<ExplosivesTrigger> triggers = ((ArrayList<ExplosivesTrigger>)p.getProperty("TRIGGERS"));
			for(ExplosivesTrigger t : triggers){
				if(((ExplosiveTriggerType) t.getTriggerType()).equals(ExplosiveTriggerType.DETONATOR)){
					exploded = true;
					t.activate(p, e.getGrenadier(), ps.get(p));
					for(Block b : ps.get(p))
						e.getGrenadier().removeBlock(b);
				}
			}
		}
		if(exploded&&(e.getGrenadier() instanceof GrenadesPlusPlayer))
			PlayerUtils.sendNotification(((GrenadesPlusPlayer)e.getGrenadier()).getPlayer(), "Achievment get!", "Behave like a creeper!", new SpoutItemStack(GrenadesPlus.detonator), 2000);
	}

	@SuppressWarnings("unchecked")
	@EventHandler
	public void onThrow(ThrowEvent e){
		List<ExplosivesTrigger> triggers = ((ArrayList<ExplosivesTrigger>)e.getThrowable().getProperty("TRIGGERS"));
		for(ExplosivesTrigger t : triggers){
			ExplosiveTriggerType et = (ExplosiveTriggerType)t.getTriggerType();
			if(et.getTriggerActivationType().equals(TriggerActivationType.ONTHROW)){
				switch(et){
					case TIME:
						registerDelayedTrigger(e.getItem(), t.activate(e.getThrowable(), e.getGrenadier(), e.getItem(), null));
						break;
					case ONHIT:
						t.activate(e.getThrowable(), e.getGrenadier(), e.getItem());
						break;
				case DETONATOR:
					break;
				case REDSTONE:
					break;
				case SHOCK:
					break;
				default:
					break;
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void onPlace(Placeable p, Grenadier g, Block b){
		List<ExplosivesTrigger> triggers = ((ArrayList<ExplosivesTrigger>)p.getProperty("TRIGGERS"));
		for(ExplosivesTrigger t : triggers){
			ExplosiveTriggerType et = (ExplosiveTriggerType)t.getTriggerType();
			if(et.getTriggerActivationType().equals(TriggerActivationType.ONPLACE)){
				switch(et){
					case DETONATOR:
						break;
					case TIME:
						registerDelayedTrigger(b, t.activate(p, g, null, b));
						break;
					default:
						t.activate(p, g, b);
						break;
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void onInteract(Placeable p, Grenadier g, Block b){
		List<ExplosivesTrigger> triggers = ((ArrayList<ExplosivesTrigger>)p.getProperty("TRIGGERS"));
		for(ExplosivesTrigger t : triggers){
			ExplosiveTriggerType et = (ExplosiveTriggerType)t.getTriggerType();
			if(et.getTriggerActivationType().equals(TriggerActivationType.ONINTERACT)){
				switch(et){
					case DETONATOR:
						break;
					case TIME:
						registerDelayedTrigger(b, t.activate(p, g, null, b));
						break;
					default:
						t.activate(p, g, b);
						break;
				}
			}
		}
	}
	
	@EventHandler
	public static void onPlaceableDestroyedByExplosion(EntityExplodeEvent e){
		for(Block b:e.blockList()){
			if(ExplosiveUtils.isPlaceable(b)){
				DBManager.deleteBlock(b);
			}
		}
	}
	
	public static void registerDelayedTrigger(Item i, Task t) {
		ExplosivesTrigger.registerItem(i);
		UUID id = ExplosivesTrigger.getID(i);
		ExplosivesTrigger.addTask(id, (TriggerTask)t);
	}
	
	public static void registerDelayedTrigger(Block i, Task t) {
		ExplosivesTrigger.registerBlock(i);
		UUID id = ExplosivesTrigger.getID(i);
		ExplosivesTrigger.addTask(id, (TriggerTask)t);
	}
}
