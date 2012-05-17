package team.GrenadesPlus.Manager;

import java.io.File;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import team.GrenadesPlus.GrenadesPlus;
import team.GrenadesPlus.Util.Util;

public class ConfigLoader {
	
	public static File generalFile;
	public static File grenadesFile;
	public static File recipeFile;
	public static FileConfiguration recipeConfig;
	public static FileConfiguration grenadesConfig;
	public static FileConfiguration generalConfig;

	
	public static void config() {
		grenadesFile = new File(GrenadesPlus.plugin.getDataFolder(), "grenades.yml");
		recipeFile = new File(GrenadesPlus.plugin.getDataFolder(), "recipes.yml");
		generalFile = new File(GrenadesPlus.plugin.getDataFolder(), "general.yml");
		try {
			firstRun();
		} catch (Exception e) {}
		grenadesConfig = new YamlConfiguration();
		recipeConfig = new YamlConfiguration();
		generalConfig = new YamlConfiguration();
		try {
			grenadesConfig.load(grenadesFile);
			recipeConfig.load(recipeFile);
			generalConfig.load(generalFile);
		} catch (Exception e) {}
	}

	private static void firstRun() {
		if(FileManager.create(grenadesFile))
			FileManager.copy(GrenadesPlus.plugin.getResource("grenades.yml"), ConfigLoader.grenadesFile);
		if(FileManager.create(recipeFile))
			FileManager.copy(GrenadesPlus.plugin.getResource("recipes.yml"), ConfigLoader.recipeFile);
		if(FileManager.create(generalFile))
			FileManager.copy(GrenadesPlus.plugin.getResource("general.yml"), ConfigLoader.generalFile);
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
