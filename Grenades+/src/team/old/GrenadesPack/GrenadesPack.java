package team.old.GrenadesPack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;


import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.block.design.BlockDesign;
import org.getspout.spoutapi.block.design.GenericCubeBlockDesign;
import org.getspout.spoutapi.inventory.SpoutItemStack;
import org.getspout.spoutapi.inventory.SpoutShapedRecipe;
import org.getspout.spoutapi.inventory.SpoutShapelessRecipe;
import org.getspout.spoutapi.material.CustomBlock;
import org.getspout.spoutapi.material.CustomItem;
import org.getspout.spoutapi.material.MaterialData;


public class GrenadesPack extends JavaPlugin{

	public static GrenadesPack plugin;
	public Util util;
	
	public HashMap<Player, ArrayList<Grenade>> thrownGrenades = new HashMap<Player, ArrayList<Grenade>>();
	public HashMap<Player, HashMap<Grenade, Item>> thrownItems = new HashMap<Player, HashMap<Grenade, Item>>();
	public HashMap<Player, HashMap<Grenade, Location>> explosionLocsG = new HashMap<Player, HashMap<Grenade, Location>>();
	public HashMap<Player, HashMap<Placeable, ArrayList<Block>>> placeableData = new HashMap<Player, HashMap<Placeable, ArrayList<Block>>>();
	public HashMap<Player, HashMap<Grenade, ArrayList<Item>>> throwableData = new HashMap<Player, HashMap<Grenade, ArrayList<Item>>>();
	public HashMap<Player, HashMap<Grenade, Task>> updateTasks = new HashMap<Player , HashMap<Grenade, Task>>();
	public HashMap<Player, HashMap<Grenade, Task>> onhitTasks = new HashMap<Player , HashMap<Grenade, Task>>();
	public HashMap<Player, HashMap<Grenade, Boolean>> grenadierSafeStatus = new HashMap<Player, HashMap<Grenade, Boolean>>();
	public ArrayList<Grenade> allGrenades = new ArrayList<Grenade>();
	public ArrayList<Placeable> allPlaceables = new ArrayList<Placeable>();
	public ArrayList<Detonator> allDetonators = new ArrayList<Detonator>();
	public ArrayList<Item> thrownList = new ArrayList<Item>();
	
	public File grenadeFile;
	public FileConfiguration grenadeConfig;
	public File recipeFile;
	public FileConfiguration recipeConfig;
	public File generalFile;
	public FileConfiguration generalConfig;
	public File detonatorFile;
	public FileConfiguration detonatorConfig;
	 
	public String PREFIX = "[GrenadesPack]";
	public final GrenadesPackListener gpp = new GrenadesPackListener(this);
	public final Logger log = Logger.getLogger("Minecraft");
	
	
	@Override
	public void onDisable() {
		PluginDescriptionFile pdfFile= getDescription();
		log.info("["+pdfFile.getName() + "] version " + pdfFile.getVersion()+ " is now disabled.");
	}
	
	@Override
	public void onEnable() {
		PluginDescriptionFile pdfFile= getDescription();
		
		util = new Util(this);
		
		config();
		
		loadDetonators();
		
		loadGrenades();
		
		loadRecipes();
		
		getServer().getPluginManager().registerEvents(gpp, this);
		
		if(generalConfig.getBoolean("id-info-grenades")==true){
			log.info("["+pdfFile.getName()+"] ----------  ID's of the grenades: ------------");
			for(int i=0;i<allGrenades.size();i++){
				SpoutItemStack sis = new SpoutItemStack(allGrenades.get(i));
				log.info("ID of "+allGrenades.get(i).getName()+": "+sis.getTypeId()+":"+ sis.getDurability());
			}
			for(int i=0;i<allPlaceables.size();i++){
				SpoutItemStack sis = new SpoutItemStack(allPlaceables.get(i));
				log.info("ID of "+allPlaceables.get(i).getName()+": "+sis.getTypeId()+":"+sis.getDurability());
			}
		}
		
		if(generalConfig.getBoolean("id-info-detonators")==true){
			log.info("["+pdfFile.getName()+"] ----------  ID's of the detonators: ------------");
			for(int i=0;i<allDetonators.size();i++){
				SpoutItemStack sis = new SpoutItemStack(allDetonators.get(i));
				log.info("ID of "+allDetonators.get(i).getName()+": "+sis.getTypeId()+":"+sis.getDurability());
			}
		}
		
		
		
		log.info("["+pdfFile.getName() + "] version " + pdfFile.getVersion()+ " is now enabled.");
	}
	
	
	public Effect getEffects(String path, int type){
		ConfigurationSection cs = grenadeConfig.getConfigurationSection(path);
		Object[] effectsArray = cs.getKeys(false).toArray();
		Effect e = new Effect(type);
		for(Object o : effectsArray){
			if(o.toString().toLowerCase().equals("explosion")&&(type==0||type==2)){
				e.addValue(o.toString().toLowerCase(), 0, grenadeConfig.getInt(path+"."+(String)o+".size"));
			}else if(o.toString().toLowerCase().equals("lightning")&&(type==0||type==2)){
				e.addEffect(o.toString().toLowerCase());
			}else if(o.toString().toLowerCase().equals("smoke")&&(type==0||type==2)){
				e.addValue(o.toString().toLowerCase(), 0, grenadeConfig.getInt(path+"."+(String)o+".density"));
			}else if(o.toString().toLowerCase().equals("spawn")&&(type==0||type==2)){
				e.addValue(o.toString().toLowerCase(), 0, grenadeConfig.getString(path+"."+(String)o+".mob"));
			}else if(o.toString().toLowerCase().equals("push")&&type==1){
				e.addValue(o.toString().toLowerCase(), 0, grenadeConfig.getDouble(path+"."+(String)o+".speed"));
			}else if(o.toString().toLowerCase().equals("fire")){
				if(type==1){
					e.addValue(o.toString().toLowerCase(), 1, grenadeConfig.getInt(path+"."+(String)o+".duration"));
				}else{
					e.addValue(o.toString().toLowerCase(), 0, grenadeConfig.getInt(path+"."+(String)o+".strength"));
				}
			}else if(o.toString().toLowerCase().equals("draw")&&type==1){
				e.addValue(o.toString().toLowerCase(), 0, grenadeConfig.getDouble(path+"."+(String)o+".speed"));
			}else if(o.toString().toLowerCase().equals("place")&&(type==0||type==2)){
				e.addValue(o.toString().toLowerCase(), 0, MaterialParser.parseItem(grenadeConfig.getString(path+"."+(String)o+".block")).getTypeId());
			}else if(o.toString().toLowerCase().equals("break")&&(type==0||type==2)){
				e.addValue(o.toString().toLowerCase(), 0, grenadeConfig.getInt(path+"."+(String)o+".potency"));
			}else if(o.toString().toLowerCase().startsWith("potion")&&type==1){
				String[] p = o.toString().split("_");
				if(p.length<2){
					if(generalConfig.getBoolean("show-warnings"))
					log.info(PREFIX+" Can't find potion effect id. Skipping effect!");
					continue;
				}
				e.addValue(o.toString().toLowerCase(), 0, Integer.parseInt(p[1]));
				e.addValue(o.toString().toLowerCase(), 1, grenadeConfig.getInt(path+"."+(String)o+".strength"));
				e.addValue(o.toString().toLowerCase(), 2, grenadeConfig.getInt(path+"."+(String)o+".duration"));
			}else{
				if(generalConfig.getBoolean("show-warnings"))
				log.info(PREFIX+" Effect "+o.toString()+" does not exist!");
			}
		}
		return e;
	}
	
	//loading grenade items
	public void loadGrenades(){
		loadItemSection("Throwable");
		loadItemSection("Placeable");
		
		//OUTPUT
		if(generalConfig.getBoolean("loaded-grenades")==true){
			log.info("["+this.getDescription().getName()+"] ------------- Grenades loaded: --------------");
			for(int k=0;k<allGrenades.size();k++){
				System.out.print("- "+allGrenades.get(k).getName());
			}
			for(int k=0;k<allPlaceables.size();k++){
				System.out.print("- "+allPlaceables.get(k).getName());
			}
		}
	}
	
	//loading detonator items
		public void loadDetonators(){
			Set<String> detonatorKeys = detonatorConfig.getKeys(false);
			Object[] detonatorArray =  detonatorKeys.toArray();
			for(int i = 0;i<detonatorArray.length;i++){
				if((String) detonatorArray[i]+".texture"==null||!detonatorConfig.isString((String) detonatorArray[i]+".texture")){
					if(generalConfig.getBoolean("show-warnings"))
					log.info(PREFIX+" Can't find texture url for "+detonatorArray[i]+"! Skipping!");
					continue;
				}
				if((String) detonatorArray[i]+".range"==null||!detonatorConfig.isInt((String) detonatorArray[i]+".range")){
					if(generalConfig.getBoolean("show-warnings"))
					log.info(PREFIX+" Can't find range value for "+detonatorArray[i]+"! Skipping!");
					continue;
				}
				if((String) detonatorArray[i]+".frequency"==null||!detonatorConfig.isInt((String) detonatorArray[i]+".frequency")){
					log.info(PREFIX+" Can't find frequency value for "+detonatorArray[i]+"! Skipping!");
					continue;
				}
				Detonator a = new Detonator(this, detonatorArray[i].toString(), detonatorConfig.getString((String) detonatorArray[i]+".texture"), detonatorConfig.getInt((String) detonatorArray[i]+".range"),detonatorConfig.getInt((String) detonatorArray[i]+".frequency"), this);
				allDetonators.add(a);
			}
			if(generalConfig.getBoolean("loaded-detonators")==true){
				log.info(PREFIX+"-------------- Detonators loaded: ---------------");
				for(int k=0;k<allDetonators.size();k++){
					System.out.print("- "+allDetonators.get(k).getName());
				}
			}
		}
		
	
	private void loadItemSection(String s){
		ConfigurationSection cs = grenadeConfig.isConfigurationSection(s)?grenadeConfig.getConfigurationSection(s):null;
		Object[] grenadeArray = null;
		if(cs!=null){
			Set<String> grenadeKeys = cs.getKeys(false);
			grenadeArray =  grenadeKeys.toArray();
			
			for(int i = 0;i<grenadeArray.length;i++){
				String path=s+"."+grenadeArray[i];
				//TEXTURE
				String tex = grenadeConfig.isString((String) path+".texture")?grenadeConfig.getString(path+".texture"):null;
				if(tex==null) {
					if(generalConfig.getBoolean("show-warnings"))
					log.info(PREFIX+" Can't find texture url of "+grenadeArray[i]+"! Skipping!");
					continue;
				}
				//RADIUS
				int radius = grenadeConfig.isInt(path+".radius")?grenadeConfig.getInt(path+".radius"):0;
				//TRIGGERS AND SPECIAL DATA
				ArrayList<Trigger> trigger = new ArrayList<Trigger>();
				float speed = 1.0f;
				BlockDesign blockD = null;
				float hardness = MaterialData.stone.getHardness();
				int type = 0;
				//sound
				String sound = grenadeConfig.isString((String) path+".sound")?grenadeConfig.getString(path+".sound"):null;
				if(sound==null) {
					if(generalConfig.getBoolean("show-warnings"))
					log.info(PREFIX+" Can't find sound url of "+grenadeArray[i]+"!");
				}
				if(s.equalsIgnoreCase("throwable")){
					trigger = loadTriggers(path);
					//SPEED
					speed = grenadeConfig.isDouble((String)path+".speed-multiply")? (float) grenadeConfig.getDouble((String)path+".speed-multiply"):(float) 1.0;
					
				}else if(s.equalsIgnoreCase("placeable")){
					trigger = loadTriggers(path);
					//DESIGN
					String design= grenadeConfig.isString((String)path+".design")?  grenadeConfig.getString((String)path+".design"):"cube";
					blockD = generateDesign(design, tex);
					//HARDNESS
					hardness = (float) (grenadeConfig.isDouble(path+".hardness")?grenadeConfig.getDouble(path+".hardness"):MaterialData.stone.getHardness());
					//TYPE
					String typeString = (grenadeConfig.isString(path+".type")?grenadeConfig.getString(path+".type"):"place");
					if(typeString.equals("place")){
						type = 0;
					}else if(typeString.equals("click")){
						type = 1;
					}else{
						if(generalConfig.getBoolean("show-warnings"))
						log.info(PREFIX+" Unknown placeable type "+typeString+"!  Defaulting !");
					}
				}
				//EFFECTS
				ArrayList<Effect> effects = new ArrayList<Effect>();
				
				ConfigurationSection effcs = grenadeConfig.getConfigurationSection(path+".effects");
				if(effcs==null){
					if(generalConfig.getBoolean("show-warnings"))
					log.info(PREFIX+" Can't find any effects for the "+grenadeArray[i]+"!");
				}else{
					Object[] effectsArray = effcs.getKeys(false).toArray();
					
					for(Object o : effectsArray){
						log.info(o.toString());
						Effect e = null;
						if(o.toString().equalsIgnoreCase("location")){
							e = getEffects(path+".effects.location", 0);
						}else if(o.toString().equalsIgnoreCase("entityInRange")){
							e = getEffects(path+".effects.entityInRange", 1);
						}else if(o.toString().equalsIgnoreCase("blocksInRange")){
							e = getEffects(path+".effects.blocksInRange", 2);
						}
						if(e==null){
							if(generalConfig.getBoolean("show-warnings"))
							log.info(PREFIX+" Could not load effect "+o.toString()+" in "+grenadeArray[i]+"! Skipping!");
							continue;
						}
						effects.add(e);
					}
				}
				//CREATING
				if(s.equalsIgnoreCase("throwable")){
					Grenade g = new Grenade(this, grenadeArray[i].toString(), tex,sound, speed,radius, effects, trigger,  this);
					allGrenades.add(g);
				}else if(s.equalsIgnoreCase("placeable")){
					Placeable p = new Placeable(this, grenadeArray[i].toString(), blockD,sound, hardness, radius, type, effects, trigger, this);
					allPlaceables.add(p);
				}
				
			}
		}else{
			if(generalConfig.getBoolean("show-warnings"))
			log.info(PREFIX+"Can't find any "+s+" Grenades!");
		}
	}
	
	public BlockDesign generateDesign(String type, String url){
		BlockDesign bd = null;
		if(type.equalsIgnoreCase("cube")){
			bd = new GenericCubeBlockDesign(this, url, 1);
		}else{
			if(generalConfig.getBoolean("show-warnings"))
			log.info(PREFIX+" There is no design called "+type+"! ");
		}
		
		return bd;
	}
	
	public ArrayList<Trigger>loadTriggers(String s){
		String path = s+".trigger";
		ArrayList<Trigger> tr = new ArrayList<Trigger>();
		ConfigurationSection cs = grenadeConfig.getConfigurationSection(path);
		Set<String> triggerKeys = null;
		Object[] triggerArray=null;
		if(cs==null){
			if(generalConfig.getBoolean("show-warnings"))
			log.info(PREFIX+" Can't find trigger section!");
		}else{
			triggerKeys = cs.getKeys(false);
			triggerArray = triggerKeys.toArray();
		}
		if(triggerArray.length!=0&&triggerArray!=null){
			for(Object o : triggerArray) {
				Object obst = new Object();
				String newPath = path+"."+o.toString();
				if(o.toString().equalsIgnoreCase("time")){
					if(grenadeConfig.isInt(newPath)){
						obst=grenadeConfig.getInt(newPath);
						tr.add(new Trigger(this, o.toString().toLowerCase(), obst));
					}
				}else if(o.toString().equalsIgnoreCase("onhit")){
					if(grenadeConfig.isBoolean(newPath)){
						obst=grenadeConfig.getBoolean(newPath);
						tr.add(new Trigger(this, o.toString().toLowerCase(), obst));
					}
				} else if(o.toString().equalsIgnoreCase("shock")){
					if(grenadeConfig.isInt(newPath)){
						obst=grenadeConfig.getInt(newPath);
						tr.add(new Trigger(this, o.toString().toLowerCase(), obst));
					}
				}else if(o.toString().equalsIgnoreCase("detonator")){
					if(grenadeConfig.isString(newPath)){
						obst=grenadeConfig.getString(newPath);
						tr.add(new Trigger(this,o.toString().toLowerCase(), obst));
					}
				}else{
					if(generalConfig.getBoolean("show-warnings"))
					log.info(PREFIX+" The trigger type "+o.toString()+" of your grenade is not valid!");
				}
				
			}
			return tr;
		}
		return null;
	}
	
	//loading recipes
	public void loadRecipes(){
		Set<String> recipeKeys = recipeConfig.getKeys(false);
		Object[] recipeArray =  recipeKeys.toArray();
		for(int i = 0;i<recipeArray.length;i++){
			CustomItem r = null;
			CustomBlock b = null;
			for(int k = 0;k<allGrenades.size();k++){
				if(allGrenades.get(k).getName().equals(recipeArray[i].toString()))
					r=allGrenades.get(k);
			}
			for(int k = 0;k<allPlaceables.size();k++){
				if(allPlaceables.get(k).getName().equals(recipeArray[i].toString()))
					b=allPlaceables.get(k);
			}
			for(int k = 0;k<allDetonators.size();k++){
				if(allDetonators.get(k).getName().equals(recipeArray[i].toString()))
					r=allDetonators.get(k);
			}
			if(r==null&&b==null){
				if(generalConfig.getBoolean("show-warnings"))
				log.warning("Could not load recipe for "+recipeArray[i]+", skipping!");
				continue;
			}
			SpoutItemStack result = new SpoutItemStack(r==null?b:r, recipeConfig.getInt(recipeArray[i]+".amount"));
			if(recipeConfig.getString(recipeArray[i]+".type").equalsIgnoreCase("shaped")){
				SpoutShapedRecipe ssr = new SpoutShapedRecipe(result);
				
				char[] chars = {'a','b','c','d','e','f','g','h','i'};
				int charcounter = 0;
				
				String s =recipeConfig.getString(recipeArray[i]+".ingredients");
				
				List<ItemStack> lis = MaterialParser.parseItems(s);
				for(int l=0;l<9;l++){
					if(lis.get(l).getTypeId()==0)chars[l]=' ';
				}
				String r1 = chars[0]+""+chars[1]+""+chars[2];
				String r2 = chars[3]+""+chars[4]+""+chars[5];
				String r3 = chars[6]+""+chars[7]+""+chars[8];
				ssr.shape(r1, r2, r3);
			
				for(int l=0;l<lis.size();l++){
					if(chars[charcounter]==' '){
						charcounter++;
						continue;
					}
					ssr.setIngredient(chars[charcounter],MaterialData.getMaterial(lis.get(l).getTypeId(),(short) lis.get(l).getData().getData())); 
					charcounter++;
				}
				
				
				SpoutManager.getMaterialManager().registerSpoutRecipe(ssr);
				
			}else if(recipeConfig.getString(recipeArray[i]+".type").equalsIgnoreCase("shapeless")){
				SpoutShapelessRecipe ssl = new SpoutShapelessRecipe(result);
				String s =recipeConfig.getString(recipeArray[i]+".ingredients");
				List<ItemStack> lis = MaterialParser.parseItems(s);
				for(int l=0;l<lis.size();l++){
					ssl.addIngredient(MaterialData.getMaterial(lis.get(l).getTypeId(),(short) lis.get(l).getData().getData())); 
				}
				SpoutManager.getMaterialManager().registerSpoutRecipe(ssl);
			}
		}
	}
	
	
	//loading the config....
	 public void config(){
		 grenadeFile = new File(getDataFolder(), "grenades.yml");
		 recipeFile = new File(getDataFolder(), "recipes.yml");
		 generalFile = new File(getDataFolder(), "general.yml");
		 detonatorFile = new File(getDataFolder(), "detonators.yml");
		 try {
		        firstRun();
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		 grenadeConfig = new YamlConfiguration();
		 recipeConfig = new YamlConfiguration();
		 generalConfig = new YamlConfiguration();
		 detonatorConfig = new YamlConfiguration();
		 loadYamls();
	 }
	 
	
	private void firstRun() {
	    if(!grenadeFile.exists()){
	    	grenadeFile.getParentFile().mkdirs();
	        copy(getResource("grenades.yml"), grenadeFile);
	    }
	    if(!recipeFile.exists()){
	    	recipeFile.getParentFile().mkdirs();
	        copy(getResource("recipes.yml"), recipeFile);
	    }
	    if(!generalFile.exists()){
	    	generalFile.getParentFile().mkdirs();
	        copy(getResource("general.yml"),generalFile);
	    }
	    if(!detonatorFile.exists()){
	    	detonatorFile.getParentFile().mkdirs();
	        copy(getResource("detonators.yml"), detonatorFile);
	    }
	}
	private void copy(InputStream in, File file) {
	    try {
	        OutputStream out = new FileOutputStream(file);
	        byte[] buf = new byte[1024];
	        int len;
	        while((len=in.read(buf))>0){
	            out.write(buf,0,len);
	        }
	        out.close();
	        in.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public void loadYamls() {
	    try {
	    	grenadeConfig.load(grenadeFile);
	        recipeConfig.load(recipeFile);
	        generalConfig.load(generalFile);
	        detonatorConfig.load(detonatorFile);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

}
