package team.GrenadesPlus.Manager;

import java.io.File;
import java.util.List;

import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.inventory.SpoutItemStack;
import org.getspout.spoutapi.material.CustomBlock;
import org.getspout.spoutapi.material.CustomItem;

import team.ApiPlus.API.Effect.Effect;
import team.ApiPlus.Manager.ItemManager;
import team.ApiPlus.Manager.RecipeManager;
import team.ApiPlus.Util.FileUtil;
import team.GrenadesPlus.GrenadesPlus;
import team.GrenadesPlus.Block.Placeable;
import team.GrenadesPlus.Block.Designs.DesignType;
import team.GrenadesPlus.Trigger.ExplosivesTrigger;
import team.GrenadesPlus.Util.Util;
import team.GrenadesPlus.Manager.ConfigParser;
import team.GrenadesPlus.Item.Detonator;
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
		if(FileUtil.create(explosivesFile))
			FileUtil.copy(GrenadesPlus.plugin.getResource("explosives.yml"), ConfigLoader.explosivesFile);
		if(FileUtil.create(recipeFile))
			FileUtil.copy(GrenadesPlus.plugin.getResource("recipes.yml"), ConfigLoader.recipeFile);
		if(FileUtil.create(generalFile))
			FileUtil.copy(GrenadesPlus.plugin.getResource("general.yml"), ConfigLoader.generalFile);
	}
	
	public static void loadRecipes(){
		Object[] recipeKeys = recipeConfig.getKeys(false).toArray();
		for(Object key : recipeKeys){
			try{
				if (!key.toString().equalsIgnoreCase("name")) {
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
					RecipeManager.RecipeType type = RecipeManager.RecipeType.valueOf(recipeConfig.getString(key+".type").toUpperCase());
					RecipeManager.addRecipe(type, ingredients, result);
				}
			}catch (Exception e) {
				Util.warn("Config Error: " + e.getMessage());
				Util.debug(e);
			}
		}
	}
	
	public static void loadThrowables(){
		if(explosivesConfig.getConfigurationSection("Throwable")==null)
			return;
		Object[] keys = explosivesConfig.getConfigurationSection("Throwable").getKeys(false).toArray();
		for(Object key : keys){
			try{
				String path = "Throwable."+key;
				YamlConfiguration defaultConfig = new YamlConfiguration();
				defaultConfig.load(GrenadesPlus.plugin.getResource("explosives.yml"));
				for(String node : defaultConfig.getConfigurationSection("Throwable.Grenade").getKeys(false)){
					if(explosivesConfig.get(path+"."+node)==null){
						Util.warn( "The node '"+node+"' in the throwable explosive "+key+" is missing or invalid!");
					}
				}
				
				String name = key.toString();
				String texture = explosivesConfig.getString(path+".texture");
				String sound = explosivesConfig.getString(path+".sound.url");
				int soundvolume = explosivesConfig.getInt(path+".sound.volume");
				int radius = explosivesConfig.getInt(path+".radius");
				float speed = explosivesConfig.getInt(path+".speed");
				
				List<Effect> effects = ConfigParser.parseEffects(path.toString()+".effects");
				
				Throwable t = MaterialManager.buildThrowable(GrenadesPlus.plugin, name, texture);
				//triggers has to be loaded after creation because they need the throwable as effectholder parameter
				List<ExplosivesTrigger> triggers = ConfigParser.parseTriggers(path.toString()+".triggers", t);
				
				t.addProperty("SOUNDURL", sound);
				t.addProperty("RADIUS", radius);
				t.addProperty("SOUNDVOLUME", soundvolume);
				t.addProperty("SPEED", speed);
				t.addProperty("TRIGGERS", triggers);
				t.addProperty("EFFECTS", effects);
				
			}catch (Exception e) {
				Util.warn("Config Error: " + e.getMessage());
				Util.debug(e);
			}
		}
	}
	
	public static void loadPlaceables(){
		if(explosivesConfig.getConfigurationSection("Placeable")==null)
			return;
		Object[] keys = explosivesConfig.getConfigurationSection("Placeable").getKeys(false).toArray();
		for(Object key : keys){
			try{
				String path = "Placeable."+key;
				YamlConfiguration defaultConfig = new YamlConfiguration();
				defaultConfig.load(GrenadesPlus.plugin.getResource("explosives.yml"));
				for(String node : defaultConfig.getConfigurationSection("Placeable.C4").getKeys(false)){
					if(explosivesConfig.get(path+"."+node)==null){
						Util.warn( "The node '"+node+"' in the placeable explosive "+key+" is missing or invalid!");
					}
				}
				String name = key.toString();
				String textureUrl = explosivesConfig.getString(path+".design.texture.url");
				DesignType design = DesignType.valueOf(explosivesConfig.getString(path+".design.type", "cube").toUpperCase());
				design.setAttaching(explosivesConfig.getBoolean(path+".design.attach-to-walls", false));
				int width = explosivesConfig.getInt(path+".design.texture.width", 16);
				int heigth = explosivesConfig.getInt(path+".design.texture.heigth", 16);
				int sprite = explosivesConfig.getInt(path+".design.texture.sprite", 16);
				int hardness = explosivesConfig.getInt(path+".hardness");
				int[]  used = ConfigParser.parseIntArray(explosivesConfig, path+".design.texture.used-ids");
				String sound = explosivesConfig.getString(path+".sound.url");
				int soundvolume = explosivesConfig.getInt(path+".sound.volume");
				
				List<Effect> effects = ConfigParser.parseEffects(path.toString()+".effects");
				
				Placeable t = MaterialManager.buildPlaceable(GrenadesPlus.plugin, name, textureUrl, design, width, heigth, sprite, used, hardness);
				
				List<ExplosivesTrigger> triggers = ConfigParser.parseTriggers(path.toString()+".triggers", t);
				
				t.addProperty("SOUNDURL", sound);
				t.addProperty("SOUNDVOLUME", soundvolume);
				t.addProperty("TRIGGERS", triggers);
				t.addProperty("DESIGN", design);
				t.addProperty("EFFECTS", effects);
				
				if(design.isAttaching()){
					for(BlockFace bf : BlockFace.values()){
						if(bf.equals(BlockFace.DOWN)||bf.equals(BlockFace.EAST)||bf.equals(BlockFace.NORTH)||bf.equals(BlockFace.SOUTH)||bf.equals(BlockFace.WEST)){
							MaterialManager.buildWallDesignPlaceable(t, GrenadesPlus.plugin, name+"_"+bf.toString(), textureUrl, design, width, heigth, sprite, used, hardness, bf);
						}
					}
				}
			}catch (Exception e) {
				Util.warn("Config Error: " + e.getMessage());
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
			
			String z = ConfigLoader.generalConfig.getString("throw", "G_");
			GrenadesPlus.throwType = ConfigParser.parseKeyType(z);
			if(GrenadesPlus.throwType==null) throw new Exception(" Could not parse throw key!");

			loadDetonator();
			
		} catch (Exception e) {
			Util.warn( "Config Error: " + e.getMessage());
			Util.debug(e);}
	}
	
	private static void loadDetonator(){
		String tex = ConfigLoader.generalConfig.getString("detonator.texture");
		String name = ConfigLoader.generalConfig.getString("detonator.name", "Detonator");
		int range = ConfigLoader.generalConfig.getInt("detonator.range", 30);
		try {
			Detonator deto = (Detonator) ItemManager.getInstance().buildItem(GrenadesPlus.plugin, name, tex, "Detonator");
			deto.addProperty("RANGE", range);
			GrenadesPlus.detonator = deto;
		} catch (Exception e) {
			Util.warn(e.getMessage());
			Util.debug(e);
		}
	}

	public static boolean modify(FileConfiguration con) {
		String name = con.getString("Name");
		if(name.equalsIgnoreCase("explosives")) {
			ConfigLoader.explosivesConfig = con;
			ConfigLoader.loadThrowables();
			ConfigLoader.loadPlaceables();
			return true;
		}
		if(name.equalsIgnoreCase("recipes")) {
			ConfigLoader.recipeConfig = con;
			ConfigLoader.loadRecipes();
			return true;
		}
		return false;
	}
	
}
