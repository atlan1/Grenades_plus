package team.GrenadesPlus.Manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.inventory.SpoutItemStack;

import team.GrenadesPlus.Block.Placeable;
import team.GrenadesPlus.Enum.Effect;
import team.GrenadesPlus.Enum.EffectSection;
import team.GrenadesPlus.Enum.EffectType;
import team.GrenadesPlus.Enum.KeyType;
import team.GrenadesPlus.Enum.Trigger;
import team.GrenadesPlus.Item.Throwable;
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
    
    public static List<Trigger> parseTriggers(String path){
    	List<Trigger> triggers = new ArrayList<Trigger>();
    	boolean throwable = false;
    	if(path.startsWith("Throwable"))
    		throwable = true;
    	if(!ConfigLoader.explosivesConfig.isConfigurationSection(path)||ConfigLoader.explosivesConfig.getConfigurationSection(path).getKeys(false).isEmpty()) return triggers;
    	for(String node: ConfigLoader.explosivesConfig.getConfigurationSection(path).getKeys(false)){
    		Trigger t = Trigger.valueOf(node.toUpperCase());
    		if(throwable){
    			if(!Trigger.isThrowableTrigger(t))
    				continue;
    		}else{
    			if(!Trigger.isPlaceableTrigger(t))
    				continue;
    		}
    		switch(t){
	    		case ONHIT:
	    			t = Trigger.ONHIT();
	    			break;
	    		case TIME:
	    			t = Trigger.TIME(ConfigLoader.explosivesConfig.getInt(path+"."+node));
	    			break;
	    		case DETONATOR:
	    			t = Trigger.DETONATOR();
	    			break;
	    		case REDSTONE:
	    			t = Trigger.REDSTONE(ConfigLoader.explosivesConfig.getBoolean(path+"."+node));
	    			break;
	    		case SHOCK:
	    			t = Trigger.SHOCK(ConfigLoader.explosivesConfig.getInt(path+"."+node));
	    			break;
    		}
    		triggers.add(t);
    	}
    	return triggers;
    }
    
    public static List<Effect> parseEffects(String path){
    	List<Effect> effects = new ArrayList<Effect>();
    	if(!ConfigLoader.explosivesConfig.isConfigurationSection(path)||ConfigLoader.explosivesConfig.getConfigurationSection(path).getKeys(false).isEmpty()) return effects;
    	for(String effectsection: ConfigLoader.explosivesConfig.getConfigurationSection(path).getKeys(false)){
    		EffectSection effsec = EffectSection.valueOf(effectsection.toUpperCase());
    		setSectionArguments(path+"."+effectsection, effsec);
    		for(String effecttype : ConfigLoader.explosivesConfig.getConfigurationSection(path+"."+effectsection).getKeys(false)){
    			if(effecttype.toUpperCase().equalsIgnoreCase("arguments")) continue;
    			EffectType efftyp = EffectType.valueOf(effecttype.toUpperCase());
    			if(Util.isAllowedInEffectSection(efftyp, effsec)){
    				effects.add(buildEffect(efftyp, effsec, path+"."+effectsection+"."+effecttype));
    			}
    		}
    	}
    	return effects;
    }
    
    private static void setSectionArguments(String path ,EffectSection e){
    	Map<String, Object> map = new HashMap<String, Object>();
    	ConfigurationSection cs = ConfigLoader.explosivesConfig.getConfigurationSection(path+".arguments");
    	if(cs==null) return;
    	switch(e) {
    		case FLIGHTPATH:
    			if(cs.getInt("length")!=0)
    				map.put("LENGTH", (Integer)cs.getInt("length"));
    			else return;
    			break;
    		default:
    			if(cs.getInt("radius")!=0)
    				map.put("RADIUS", (Integer)cs.getInt("radius"));
    			else return;
    			break;
    	}
    	e.setProperties(map);
    }
    
    private static Effect buildEffect(EffectType efftyp, EffectSection es, String path){
    		Effect e = new Effect(efftyp, es);
    		switch(efftyp){
		    	case EXPLOSION:
		    		e.addProperty("SIZE", ConfigLoader.explosivesConfig.getInt(path+".size"));
		    		break;
		    	case LIGHTNING:
		    		break;
		    	case SMOKE:
		    		e.addProperty("DENSITY", ConfigLoader.explosivesConfig.getInt(path+".density"));
		    		break;
		    	case FIRE:
		    		if(es.equals(EffectSection.TARGETENTITIES)||es.equals(EffectSection.THROWER)||es.equals(EffectSection.LAYER))
		    			e.addProperty("DURATION", ConfigLoader.explosivesConfig.getInt(path+".duration"));
		    		else
		    			e.addProperty("STRENGTH", ConfigLoader.explosivesConfig.getInt(path+".strength"));
		    		break;
		    	case PUSH:
		    		e.addProperty("SPEED", ConfigLoader.explosivesConfig.getDouble(path+".speed"));
		    		break;
		    	case DRAW:
		    		e.addProperty("SPEED", ConfigLoader.explosivesConfig.getDouble(path+".speed"));
		    		break;
		    	case POTION:
		    		e.addProperty("ID", ConfigLoader.explosivesConfig.getInt(path+".id"));
		    		e.addProperty("DURATION", ConfigLoader.explosivesConfig.getInt(path+".duration"));
		    		e.addProperty("STRENGTH", ConfigLoader.explosivesConfig.getInt(path+".strength"));
		    		break;
		    	case SPAWN:
		    		e.addProperty("ENTITY", ConfigLoader.explosivesConfig.getString(path+".entity"));
		    		break;
		    	case PLACE:
		    		e.addProperty("BLOCK", ConfigLoader.explosivesConfig.getString(path+".block"));
		    		break;
		    	case BREAK:
		    		e.addProperty("POTENCY", ConfigLoader.explosivesConfig.getDouble(path+".potency"));
		    		break;
    	}
    	return e;
    }
}
