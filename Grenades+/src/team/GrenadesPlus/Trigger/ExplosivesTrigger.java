package team.GrenadesPlus.Trigger;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.block.Block;
import org.bukkit.entity.Item;

import team.ApiPlus.API.EffectHolder;
import team.ApiPlus.API.Trigger.Trigger;
import team.ApiPlus.API.Trigger.TriggerTask;
import team.ApiPlus.API.Trigger.TriggerType;
import team.ApiPlus.Util.Task;

public class ExplosivesTrigger implements Trigger{

	private TriggerType triggerType;
	private static Map<Block, UUID> blocksToTrigger = Collections.synchronizedMap(new HashMap<Block, UUID>());
	private static Map<Item, UUID> itemsToTrigger = Collections.synchronizedMap(new HashMap<Item, UUID>());
	private static Map<UUID, Set<TriggerTask>> runningTasks = Collections.synchronizedMap(new HashMap<UUID, Set<TriggerTask>>());
	
	public ExplosivesTrigger(TriggerType tt, EffectHolder eff) {
		triggerType = tt;
	}
	
	@Override
	public TriggerType getTriggerType() {
		return triggerType;
	}

	@Override
	public void setTriggerType(TriggerType tt) {
		triggerType = tt;
	}

	@Override
	public Task triggered(Object... args) {
		TriggerTask t = getTriggerType().triggeredTask().createTask(this, args);
		t.startTaskModel();
		return t;
	}
	
	@Override
	public Task activate(Object... args) {
		TriggerTask t = getTriggerType().activationTask().createTask(this, args);
		t.startTaskModel();
		return t;
	}
	
	public static void registerItem(Item i) {
		if(ExplosivesTrigger.itemsToTrigger.containsKey(i)){
			UUID id = getID(i);
			if(!ExplosivesTrigger.runningTasks.containsKey(id))
				ExplosivesTrigger.runningTasks.put(id, new HashSet<TriggerTask>());
		}else{
			ExplosivesTrigger.itemsToTrigger.put(i, UUID.randomUUID());
			registerItem(i);
		}
	}
	
	public static void unregisterItem(Item i) {
		if(isRegistered(i)){
			UUID id = itemsToTrigger.get(i);
			runningTasks.remove(id);
			itemsToTrigger.remove(i);
		}
	}
	
	public static void addTask(UUID id, TriggerTask t){
		if(runningTasks.containsKey(id))
			runningTasks.get(id).add(t);
	}
	
	public static void removeTask(UUID id, TriggerTask t){
		if(runningTasks.containsKey(id))
			runningTasks.get(id).remove(t);
	}
	
	public static Set<TriggerTask> getTasks(UUID id){
		if(runningTasks.containsKey(id)){
			return  runningTasks.get(id);
		}
		return null;
	}
	
	public static void stopTasks(UUID id){
		if(runningTasks.containsKey(id)){
			for(TriggerTask tt :  runningTasks.get(id)){
				tt.stopTask();
			}
		}
	}
	
	public static boolean containsTask(UUID id, TriggerTask t){
		if(runningTasks.containsKey(id))
			return runningTasks.get(id).contains(t);
		return false;
	}
	
	public static void registerBlock(Block b) {
		if(ExplosivesTrigger.blocksToTrigger.containsKey(b)){
			UUID id = getID(b);
			if(!ExplosivesTrigger.runningTasks.containsKey(id))
				ExplosivesTrigger.runningTasks.put(id, new HashSet<TriggerTask>());
		}else{
			ExplosivesTrigger.blocksToTrigger.put(b, UUID.randomUUID());
			registerBlock(b);
		}
	}
	
	public static void unregisterBlock(Block i) {
		if(isRegistered(i)){
			UUID id = blocksToTrigger.get(i);
			runningTasks.remove(id);
			blocksToTrigger.remove(i);
		}
	}
	
	public static boolean isRegistered(Block b) {
		return blocksToTrigger.containsKey(b);
	}
	
	public static boolean isRegistered(Item b) {
		return blocksToTrigger.containsKey(b);
	}
	
	public static UUID getID(Block b){
		if(blocksToTrigger.containsKey(b))
			return blocksToTrigger.get(b);
		return null;
	}
	
	public static UUID getID(Item i){
		if(itemsToTrigger.containsKey(i))
			return itemsToTrigger.get(i);
		return null;
	}
}
