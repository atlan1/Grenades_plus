package team.GrenadesPlus.Trigger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import team.GrenadesPlus.GrenadesPlus;
import team.GrenadesPlus.API.Event.DetonateEvent;
import team.GrenadesPlus.API.Event.ThrowEvent;
import team.GrenadesPlus.Block.Placeable;
import team.GrenadesPlus.Util.ExplosiveUtils;
import team.GrenadesPlus.Util.Grenadier;

public class TriggerListener implements Listener {
	
	private GrenadesPlus plugin;
	
	public TriggerListener(GrenadesPlus p){
		plugin = p;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@SuppressWarnings("unchecked")
	@EventHandler
	public void onDetonate(DetonateEvent e){
		Map<Placeable, List<Block>> ps = new HashMap<Placeable, List<Block>>();
		for(Block b : e.getBlockList()){
			if(ExplosiveUtils.isPlaceable(b)){
				Placeable p = ExplosiveUtils.getPlaceable(b);
				if(!ps.containsKey(p))
					ps.put(p, new ArrayList<Block>());
				ps.get(p).add(b);
			}
		}
		for(Placeable p : new HashSet<Placeable>(ps.keySet())){
			List<ExplosivesTrigger> triggers = ((ArrayList<ExplosivesTrigger>)p.getProperty("TRIGGERS"));
			for(ExplosivesTrigger t : triggers){
				if(((ExplosiveTriggerType) t.getTriggerType()).equals(ExplosiveTriggerType.DETONATOR)){
					t.activate(p, e.getGrenadier(), ps.get(p));
				}
			}
		}
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
						t.activate(e.getThrowable(), e.getGrenadier(), e.getItem(), null);
						break;
					case ONHIT:
						t.activate(e.getThrowable(), e.getGrenadier(), e.getItem());
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
						t.activate(p, g, null, b);
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
			if(et.getTriggerActivationType().equals(TriggerActivationType.ONPLACE)){
				switch(et){
					case DETONATOR:
						break;
					case TIME:
						t.activate(p, g, null, b);
						break;
					default:
						t.activate(p, g, b);
						break;
				}
			}
		}
	}
}
