package team.GrenadesPlus;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.getspout.spoutapi.block.design.Texture;

import team.GrenadesPlus.Block.Placeable;
import team.GrenadesPlus.Enum.KeyType;
import team.GrenadesPlus.Item.Throwable;
import team.GrenadesPlus.Manager.ConfigLoader;
import team.GunsPlus.GunsPlus;

import com.griefcraft.lwc.LWC;
import com.griefcraft.lwc.LWCPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class GrenadesPlus extends JavaPlugin{
	public static final String PRE = "[Grenades+] ";
	public static final Logger log = Bukkit.getLogger();
	
	public static GrenadesPlus plugin;
	
	public static GunsPlus gunsplus;
	public static LWC lwc;
	public static WorldGuardPlugin wg;
	public static boolean useFurnaceAPI = false;
	
	public static boolean warnings = true;
	public static boolean debug = false;
	public static boolean notifications = true;
	public static boolean autoreload = true;
	public static KeyType throwType = new KeyType("G", true);
	
	public static List<Throwable> allThrowables = new ArrayList<Throwable>();
	public static List<Placeable> allPlaceables = new ArrayList<Placeable>();
	public static List<Material> transparentMaterials = new ArrayList<Material>();
	
	public static List<Texture> loadedBlockTextures = new ArrayList<Texture>();
	
	public static List<GrenadesPlusPlayer> GrenadesPlusPlayers = new ArrayList<GrenadesPlusPlayer>();
	
	@Override
	public void onDisable() {
		log.log(Level.INFO, PRE + " version " + getDescription().getVersion()
				+ " is now disabled.");
	}
	
	@Override
	public void onEnable() {
		plugin = this;
		checkForAPI();
		ConfigLoader.config();
//		new VersionChecker(this, ""); TODO: Add rss url
		
		hook();
		init();
		
		getCommand("grenades+").setExecutor(new CommandEx(this));
		getServer().getPluginManager().registerEvents(new GrenadesPlusListener(this), this);
		new ThrowBinding(this, throwType);
		
		log.log(Level.INFO, PRE + " version " + getDescription().getVersion()
				+ " is now enabled.");
	}
	
	private void init(){
		ConfigLoader.loadGeneral();
		ConfigLoader.loadThrowables();
		ConfigLoader.loadPlaceables();
		ConfigLoader.loadRecipes();
	}
	
	private void hook(){
		Plugin spout = getServer().getPluginManager().getPlugin("Spout");
		Plugin gp = getServer().getPluginManager().getPlugin("GunsPlus");
		Plugin lwcPlugin = getServer().getPluginManager().getPlugin("LWC");
		Plugin furnaceAPI = getServer().getPluginManager().getPlugin("FurnaceAPI");
		Plugin worldguard = getServer().getPluginManager().getPlugin("WorldGuardPlugin");
		if(spout != null) {
		    log.log(Level.INFO, PRE+" Plugged into Spout!");
		}else{
			log.log(Level.INFO, PRE+" disableing because Spout is missing!");
			this.setEnabled(false);
		}
		if(gp != null) {
			gunsplus = (GunsPlus) gp;
			log.log(Level.INFO, PRE+" Plugged into Guns+!");
		}
		if(lwcPlugin != null) {
			lwc = ((LWCPlugin) lwcPlugin).getLWC();
		    log.log(Level.INFO, PRE+" Plugged into LWC!");
		}
		if(furnaceAPI != null) {
			useFurnaceAPI = true;	
			log.log(Level.INFO, PRE+" Plugged into FurnaceAPI!");
		}
		if(worldguard != null) {
			wg = (WorldGuardPlugin) worldguard;
			log.log(Level.INFO, PRE+" Plugged into WorldGuard!");
		}
	}
	
	private void checkForAPI() {
		PluginManager pm = getServer().getPluginManager();
		Plugin apiPlugin = pm.getPlugin("ApiPlus");
		if (apiPlugin == null) {
			try {
				File api = new File("plugins/ApiPlus.jar");
				download( new URL("https://github.com/atlan1/ApiPlus/downloads/ApiPlus.jar"), api);
				pm.loadPlugin(api);
				pm.enablePlugin(apiPlugin);
			} catch (Exception exception) {
				log.warning(PRE+"could not download ApiPlus! Try to install it manually.");
				pm.disablePlugin(this);
			}
		}
	}

	public static void download(URL url, File file) throws IOException {
		if (!file.getParentFile().exists())
			file.getParentFile().mkdir();
		if (file.exists())
			file.delete();
		file.createNewFile();
		log.info(PRE+"Starting download of "+file.getName()+" ...");
		final InputStream in = url.openStream();
		final OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
		final byte[] buffer = new byte[1024];
		int len;
		while ((len = in.read(buffer)) >= 0) {
			out.write(buffer, 0, len);
		}
		in.close();
		out.close();
		log.info(PRE+"Download of "+file.getName()+" finished!");
	}
}
