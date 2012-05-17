package team.old.GrenadesPack;

import java.util.LinkedList;
import java.util.List;



import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.inventory.SpoutItemStack;

public class MaterialParser
{
	public static GrenadesPack plugin;
	
	public MaterialParser(GrenadesPack p){
		plugin = p;
	}
    public static List<ItemStack> parseItems(String s)
    {
        List<ItemStack> result = new LinkedList<ItemStack>();
        
        String[] items = s.split(",");
        for (String item : items)
        {
            ItemStack mat = parseItem(item.trim());
            if (mat != null)
                result.add(mat);
        }
        
        return result;
    }
    
    public static ItemStack parseItem(String item)
    {
        if (item == null || item.equals(""))
            return null;
        
        String[] parts = item.split(":");
        if (parts.length == 1)
            return singleItem(parts[0]);
        if (parts.length == 2)
            return withDurability(parts[0], parts[1]);
        
        return null;
    }
    
    private static ItemStack singleItem(String item)
    {
    	SpoutItemStack custom = null;
        Material m = getMaterial(item);
        if(m==null){
			for(int i = 0;i<plugin.allGrenades.size();i++){
				if(plugin.allGrenades.get(i).getName().toString().equals(item)){
					custom = new SpoutItemStack(plugin.allGrenades.get(i));
				}
			}
			for(int i = 0;i<plugin.allPlaceables.size();i++){
				if(plugin.allPlaceables.get(i).getName().toString().equals(item)){
					custom = new SpoutItemStack(plugin.allPlaceables.get(i));
				}
			}
			for(int i = 0;i<plugin.allDetonators.size();i++){
				if(plugin.allDetonators.get(i).getName().toString().equals(item)){
					custom = new SpoutItemStack(plugin.allDetonators.get(i));
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
    
    private static ItemStack withDurability(String item, String durab)
    {
    	Material m = getMaterial(item);
        if (m == null)
            return null;
        SpoutItemStack sis = new SpoutItemStack(new ItemStack(m));
        if(durab.matches("[0-9]+")){
        	sis.setDurability(Short.parseShort(durab));
        }
        
        return sis;
    }
    
    private static Material getMaterial(String item)
    {
        if (item.matches("[0-9]*"))
            return Material.getMaterial(Integer.parseInt(item));
        
        return Material.getMaterial(item.toUpperCase());
    }
}

