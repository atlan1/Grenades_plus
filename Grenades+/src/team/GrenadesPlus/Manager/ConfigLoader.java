package team.GrenadesPlus.Manager;

import java.io.File;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.inventory.SpoutItemStack;
import org.getspout.spoutapi.material.CustomBlock;
import org.getspout.spoutapi.material.CustomItem;

import team.GrenadesPlus.GrenadesPlus;
import team.GrenadesPlus.Enum.Effect;
import team.GrenadesPlus.Enum.Trigger;
import team.GrenadesPlus.Util.Util;
import team.GrenadesPlus.Item.Throwable;

public class ConfigLoader {
	
	public static File generalFile;
	public static File explosivesFile;
	public static File recipeFile;
	public static FileConfiguration recipeConfig;
	public static FileConfiguration explosivesConfig;
	public static FileConfiguration generalConfig;

	
	public static void config() {
		explosivesFile = new File(GrenadesPlus.plugin.getDataFolder(), "explosives.yml");
		recipeFile = new File(GrenadesPlus.plugin.getDataFolder(), "recipes.yml");
		generalFile = new File(GrenadesPlus.plugin.getDataFolder(), "general.yml");
		try {
			firstRun();
		} catch (Exception e) {}
		explosivesConfig = new YamlConfiguration();
		recipeConfig = new YamlConfiguration();
		generalConfig = new YamlConfiguration();
		try {
			explosivesConfig.load(explosivesFile);
			recipeConfig.load(recipeFile);
			generalConfig.load(generalFile);
		} catch (Exception e) {}
	}

	private static void firstRun() {
		if(FileManager.create(explosivesFile))
			FileManager.copy(GrenadesPlus.plugin.getResource("explosives.yml"), ConfigLoader.explosivesFile);
		if(FileManager.create(recipeFile))
			FileManager.copy(GrenadesPlus.plugin.getResource("recipes.yml"), ConfigLoader.recipeFile);
		if(FileManager.create(generalFile))
			FileManager.copy(GrenadesPlus.plugin.getResource("general.yml"), ConfigLoader.generalFile);
	}
	
	public static void loadRecipes(){
		Object[] recipeKeys = recipeConfig.getKeys(false).toArray();
		for(Object key : recipeKeys){
			try{
				YamlConfiguration defaultConfig = new YamlConfiguration();
				defaultConfig.load(GrenadesPlus.plugin.getResource("recipes.yml"));
				for(String node : defaultConfig.getConfigurationSection("Grenade").getKeys(false)){
					if(recipeConfig.get(key+"."+node)==null){
						Util.warn( "The node '"+node+"' in the recipe for "+key+" is missing or invalid!");
					}
				}
				
				Object cm = null;
				if(Util.isGrenadesPlusMaterial(key.toString()))cm = Util.getGrenadesPlusMaterial(key.toString());
				else throw new Exception(GrenadesPlus.PRE + " Recipe output not found: "+key+"! Skipping!");
				int amount = recipeConfig.getInt(key.toString()+".amount");
				SpoutItemStack result = null;
				if(cm instanceof CustomItem){
					CustomItem ci = (CustomItem)cm;
					result = new SpoutItemStack(ci, amount);
				}
				else if(cm instanceof CustomBlock){
					CustomBlock cb = (CustomBlock) cm;
					result = new SpoutItemStack(cb, amount);
				}
				List<ItemStack> ingredients = ConfigParser.parseItems(recipeConfig.getString(key+".ingredients"));
				team.GrenadesPlus.Manager.RecipeManager.RecipeType type = team.GrenadesPlus.Manager.RecipeManager.RecipeType.valueOf(recipeConfig.getString(key+".type").toUpperCase());
				RecipeManager.addRecipe(type, ingredients, result);
			}catch (Exception e) {
				Util.warn("Config Error:" + e.getMessage());
				Util.debug(e);
			}
		}
	}
	
	public static void loadThrowables(){
		if(explosivesConfig.getConfigurationSection("Throwable")==null)
			return;
		Object[] keys = explosivesConfig.getConfigurationSection("throwables").getKeys(false).toArray();
		for(Object key : keys){
			try{
				YamlConfiguration defaultConfig = new YamlConfiguration();
				defaultConfig.load(GrenadesPlus.plugin.getResource("explosives.yml"));
				for(String node : defaultConfig.getConfigurationSection("Grenade").getKeys(false)){
					if(explosivesConfig.get("Throwable."+key+"."+node)==null){
						Util.warn( "The node '"+node+"' in the throwable explosive "+key+" is missing or invalid!");
					}
				}
				
				String name = key.toString();
				String texture = explosivesConfig.getString(key+".texture");
				String sound = explosivesConfig.getString(key+".sound.url");
				int soundvolume = explosivesConfig.getInt(key+".sound.volume");
				float speed = explosivesConfig.getInt(key+".speed");
				List<Trigger> triggers = ConfigParser.parseTriggers("Throwable"+key.toString()+".triggers");
				List<Effect> effects = ConfigParser.parseEffects("Throwable."+key.toString()+".effects");
				
				Throwable t = MaterialManager.buildNewThrowable(GrenadesPlus.plugin, name, texture);
				t.addProperty("SOUNDURL", sound);
				t.addProperty("SOUNDVOLUME", soundvolume);
				t.addProperty("SPEED", speed);
				t.addProperty("TRIGGERS", triggers);
				t.setEffects(effects);
				
			}catch (Exception e) {
				Util.warn("Config Error:" + e.getMessage());
				Util.debug(e);
			}
		}
	}

	public static void loadGeneral() {
		try {
			YamlConfiguration defaultConfig = new YamlConfiguration();
			defaultConfig.load(GrenadesPlus.plugin.getResource("general.yml"));
			for(String node : defaultConfig.getKeys(true)){
				if(ConfigLoader.generalConfig.get(node)==null)
					Util.warn("The node '"+node+"' in general.yml is missing or invalid! Defaulting!");
			}
			
			GrenadesPlus.warnings = ConfigLoader.generalConfig.getBoolean("show-warnings", true);
			GrenadesPlus.debug = ConfigLoader.generalConfig.getBoolean("show-debug", false);
			GrenadesPlus.notifications = ConfigLoader.generalConfig.getBoolean("show-notifications", true);
			
			List<ItemStack> il = ConfigParser.parseItems(ConfigLoader.generalConfig.getString("transparent-materials"));
			for(int m=0;m<il.size();m++){
				GrenadesPlus.transparentMaterials.add(il.get(m).getType());
			}

		} catch (Exception e) {
			Util.warn( "Config Error:" + e.getMessage());
			Util.debug(e);}
	}
	
}
