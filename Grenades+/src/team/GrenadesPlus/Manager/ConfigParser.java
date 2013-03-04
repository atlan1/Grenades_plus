package team.GrenadesPlus.Manager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.inventory.SpoutItemStack;
import team.ApiPlus.API.EffectHolder;
import team.ApiPlus.API.Effect.Effect;
import team.ApiPlus.API.Effect.EffectType;
import team.ApiPlus.API.Property.NumberProperty;
import team.ApiPlus.Manager.EffectManager;
import team.GrenadesPlus.Block.Placeable;
import team.GrenadesPlus.Controls.KeyType;
import team.GrenadesPlus.Effects.EffectTargetType;
import team.GrenadesPlus.Effects.EffectTargetImpl;
import team.GrenadesPlus.Item.Throwable;
import team.GrenadesPlus.Trigger.ExplosiveTriggerType;
import team.GrenadesPlus.Trigger.ExplosivesTrigger;
import team.GrenadesPlus.Trigger.TriggerActivationType;
import team.GrenadesPlus.Util.Util;
import team.GrenadesPlus.GrenadesPlus;

public class ConfigParser {

	public static List<ItemStack> parseItems(String s){
        List<ItemStack> result = new LinkedList<ItemStack>();
        
        String[] items = s.split(",");
        for (String item : items){
            ItemStack mat = parseItem(item.trim());
            if (mat != null)
                result.add(mat);
        }
        
        return result;
    }
    
    public static ItemStack parseItem(String item){
        if (item == null || item.equals(""))
            return null;
        
        String[] parts = item.split(":");
        if (parts.length == 1)
            return singleItem(parts[0]);
        if (parts.length == 2)
            return withDurability(parts[0], parts[1]);
        if(parts.length == 3)
        	return withAmount(parts[0], parts[1], parts[2]);
        
        return null;
    }
    
    private static ItemStack singleItem(String item){
    	SpoutItemStack custom = null;
        Material m = getMaterial(item);
        if(m==null){
        	for(Throwable a: GrenadesPlus.allThrowables){
				if(a.getName().toString().equals(item)){
					custom = new SpoutItemStack(a);
				}
			}
        	for(Placeable a: GrenadesPlus.allPlaceables){
				if(a.getName().toString().equals(item)){
					custom = new SpoutItemStack(a);
				}
			}
        }
        if(custom==null){
        	if(m==null){
        		return null;
        	}else{
        		return new ItemStack(m);
        	}
        }else{
        	return new SpoutItemStack(custom);
        }
    }
    
    private static ItemStack withDurability(String item, String durab){
    	Material m = getMaterial(item);
        if (m == null)
            return null;
        SpoutItemStack sis = new SpoutItemStack(new ItemStack(m));
        if(durab.matches("[0-9]+")){
        	sis.setDurability(Short.parseShort(durab));
        }
        
        return sis;
    }
    
    private static ItemStack withAmount(String item, String durab, String amount){
    	Material m = getMaterial(item);
        if (m == null)
            return null;
        SpoutItemStack sis = new SpoutItemStack(new ItemStack(m, Integer.parseInt(amount)));
        if(durab.matches("[0-9]+")){
        	sis.setDurability(Short.parseShort(durab));
        }
        
        return sis;
    }
    
    private static Material getMaterial(String item){
        if (item.matches("[0-9]*"))
            return Material.getMaterial(Integer.parseInt(item));
        
        return Material.getMaterial(item.toUpperCase());
    }
    
    public static KeyType parseKeyType(String string) throws Exception{
    	boolean hold = false;
    	String keyname = "";
    	if(string.endsWith("_")){
    		hold = true;
    		string = string.replace("_", "");
    	}
    	if(string.matches("[0-9a-zA-Z]*")){
    		keyname = string;
    	}else throw new Exception(" Key contains invalid characters: "+ string);
    	return new KeyType(keyname, hold);
    }
    
    public static int[] parseIntArray(FileConfiguration f, String node){
    	String[] s = f.getString(node, "0").split(",");
    	int[] ia = new int[s.length];
    	for(int i=0;i<ia.length;i++){
    		ia[i] = Integer.parseInt(s[i].trim());
    	}
    	return ia;
    }
    
    public static String[] parseStringArray(FileConfiguration f, String node){
    	String[] s = f.getString(node, "null").split(",");
    	String[] array = new String[s.length];
    	for(int i=0;i<array.length;i++){
    		array[i] = s[i].trim();
    	}
    	return array;
    }
    
    public static List<ExplosivesTrigger> parseTriggers(String path,EffectHolder effectHolder){
    	List<ExplosivesTrigger> triggers = new ArrayList<ExplosivesTrigger>();
    	boolean throwable = false;
    	if(path.startsWith("Throwable"))
    		throwable = true;
    	if(!ConfigLoader.explosivesConfig.isConfigurationSection(path)||ConfigLoader.explosivesConfig.getConfigurationSection(path).getKeys(false).isEmpty()) return triggers;
    	for(String node: ConfigLoader.explosivesConfig.getConfigurationSection(path).getKeys(false)){
    		ExplosiveTriggerType t = ExplosiveTriggerType.valueOf(node.toUpperCase());
    		if(throwable){
    			if(!ExplosiveTriggerType.isThrowableTrigger(t))
    				continue;
    		}else{
    			if(!ExplosiveTriggerType.isPlaceableTrigger(t))
    				continue;
    		}
    		TriggerActivationType tat = TriggerActivationType.valueOf(ConfigLoader.explosivesConfig.getString(path+"."+node+".activation").toUpperCase());
    		ExplosivesTrigger trig = new ExplosivesTrigger(t, effectHolder); 
        	ArrayList<Map<?, ?>> args = new ArrayList<Map<?,?>>(ConfigLoader.explosivesConfig.getMapList(path+"."+node+".args"));
    		switch(t){
	    		case ONHIT:
	    			t = ExplosiveTriggerType.ONHIT(trig, tat);
	    			break;
	    		case TIME:
	    			t = ExplosiveTriggerType.TIME(trig, tat, (Integer) searchKeyInMapList(args, "time").get("time"));
	    			break;
	    		case DETONATOR:
	    			t = ExplosiveTriggerType.DETONATOR(trig, tat);
	    			break;
	    		case REDSTONE:
	    			t = ExplosiveTriggerType.REDSTONE(trig, tat, (Boolean) searchKeyInMapList(args, "powered").get("powered"));
	    			break;
	    		case SHOCK:
	    			t = ExplosiveTriggerType.SHOCK(trig, tat, new int[]{(Integer) searchKeyInMapList(args, "radiusX").get("radiusX"), (Integer) searchKeyInMapList(args, "radiusY").get("radiusY"), (Integer) searchKeyInMapList(args, "radiusZ").get("radiusZ")});
	    			break;
    		}
    		triggers.add(trig);
    	}
    	return triggers;
    }
    
    public static List<Effect> parseEffects(String path) throws Exception{
    	List<Effect> effects = new ArrayList<Effect>();
    	if(!ConfigLoader.explosivesConfig.isConfigurationSection(path)||ConfigLoader.explosivesConfig.getConfigurationSection(path).getKeys(false).isEmpty()) return effects;
    	for(String effect_: ConfigLoader.explosivesConfig.getConfigurationSection(path).getKeys(false)){
    		try{
	    		String newpath=path+"."+effect_;
	    		String type =  ConfigLoader.explosivesConfig.getString(newpath+".type");
	    		EffectType efftyp = EffectType.valueOf(type.toUpperCase());
	    		EffectTargetImpl efftar = buildEffectTarget(newpath+".target");
	    		if(Util.isAllowedWithEffectTarget(efftyp, efftar))
	    			effects.add(buildEffect(efftar, efftyp, newpath));
	    		else throw new Exception("The effect type "+efftyp.toString().toLowerCase()+" is not allowed to have the target "+efftar);
    		}catch(Exception e){
    			Util.warn("Something went wrong during loading effect "+effect_+":");
    			Util.warn(e.getMessage());
    			Util.debug(e);
    			continue;
    		}
    	}
    	return effects;
    }
    
    private static EffectTargetImpl buildEffectTarget(String path) throws Exception {
    	ArrayList<Map<?, ?>> args = new ArrayList<Map<?,?>>(ConfigLoader.explosivesConfig.getMapList(path+".args"));
    	EffectTargetType ett =  EffectTargetType.valueOf(ConfigLoader.explosivesConfig.getString(path+".type").toUpperCase());
    	EffectTargetImpl efftar = new EffectTargetImpl(ett);
    	if(args.isEmpty()||args==null) return efftar;
    	Map<?, ?> map = searchKeyInMapList(args, "radius");
    	if(map==null)
    		throw new Exception("Argument 'radius' missing!");
    	switch(efftar.getType()){
	    	case TARGETLOCATION:
	    		efftar.addProperty("RADIUS", new NumberProperty((Integer)searchKeyInMapList(args, "radius").get("radius")));
	    		break;
	    	case EXPLOSIVELOCATION:
	    		efftar.addProperty("RADIUS", new NumberProperty((Integer)searchKeyInMapList(args, "radius").get("radius")));
	    		break;
	    	case GRENADIERLOCATION:
	    		efftar.addProperty("RADIUS", new NumberProperty((Integer)searchKeyInMapList(args, "radius").get("radius")));
	    		break;
	    	case TARGETENTITY:
	    		efftar.addProperty("RADIUS", new NumberProperty((Integer)searchKeyInMapList(args, "radius").get("radius")));
	    		break;
		case GRENADIER:
			break;
		default:
			break;
    	}
    	return efftar;
    }
    
    private static Effect buildEffect(EffectTargetImpl es, EffectType t, String path) throws Exception{
    	Effect e = null;
    	ArrayList<Map<?, ?>> args = new ArrayList<Map<?,?>>(ConfigLoader.explosivesConfig.getMapList(path+".args"));
    	if(args.isEmpty()){
    		throw new Exception("Arguments missing for effect type "+t+"!");
    	}
    		switch(t){
		    	case EXPLOSION:
		    		e =  EffectManager.getInstance().buildEffect(t.getEffectName(), searchKeyInMapList(args, "size").get("size")); 
		    		break;
		    	case MOVE:
		    		e =  EffectManager.getInstance().buildEffect(t.getEffectName(), searchKeyInMapList(args, "speed").get("speed"), searchKeyInMapList(args, "direction").get("direction"));
		    		break;
		    	case POTION:
		    		e =  EffectManager.getInstance().buildEffect(t.getEffectName(), searchKeyInMapList(args, "id").get("id"), searchKeyInMapList(args, "duration").get("duration"), searchKeyInMapList(args, "strength").get("strength") );
		    		break;
		    	case SPAWN:
		    		e =  EffectManager.getInstance().buildEffect(t.getEffectName(), searchKeyInMapList(args, "entity").get("entity"));
		    		break;
		    	case PLACE:
		    		e =  EffectManager.getInstance().buildEffect(t.getEffectName(), parseItem((String)searchKeyInMapList(args, "block").get("block")).getType());
		    		break;
		    	case BREAK:
		    		e =  EffectManager.getInstance().buildEffect(t.getEffectName(), searchKeyInMapList(args, "potency").get("potency"));
		    		break;
		    	case PARTICLE:
		    		e =  EffectManager.getInstance().buildEffect(t.getEffectName(), searchKeyInMapList(args, "type").get("type"), searchKeyInMapList(args, "amount").get("amount"), searchKeyInMapList(args, "gravity").get("gravity"), searchKeyInMapList(args, "max-age").get("max-age"), searchKeyInMapList(args, "scale").get("scale"));
		    		break;
		    	case BURN:
		    		e =  EffectManager.getInstance().buildEffect(t.getEffectName(), searchKeyInMapList(args, "duration").get("duration"));
		    		break;
		    	default:
		    		e =  EffectManager.getInstance().buildEffect(t.getEffectName()); 
		    		break;
    	}
    		e.setEffectTarget(es);
    	return e;
    }
    
    private static Map<?, ?> searchKeyInMapList(List<Map<?,?>> maplist, String key){
    	for(Map<?, ?> map : maplist){
    		if(map.containsKey(key)){
    			return map;
    		}
    	}
    	return null;
    }
}
