package team.GrenadesPlus.Util;

import net.minecraft.server.v1_4_R1.Packet18ArmAnimation;
import net.minecraft.server.v1_4_R1.WorldServer;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_4_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.inventory.SpoutItemStack;
import org.getspout.spoutapi.player.SpoutPlayer;

import team.GrenadesPlus.GrenadesPlus;
import team.GrenadesPlus.GrenadesPlusPlayer;
import team.GrenadesPlus.Block.Placeable;
import team.GrenadesPlus.Item.Throwable;

public class PlayerUtils {
	
	public static boolean hasPermissionToThrow(SpoutPlayer sp, Throwable t) {
		if(!sp.hasPermission("grenadesplus.throw.all")){
			if(!sp.hasPermission("grenadesplus.throw."+t.getName().replace(" ", "_"))){
				sendNotification(sp, "Permission", ChatColor.RED+"Denied", new SpoutItemStack(t), 1500);
				return false;
			}
		}
		return true;
	}
	
	public static boolean hasPermissionToPlace(SpoutPlayer sp, Placeable p) {
		if(!sp.hasPermission("grenadesplus.place.all")){
			if(!sp.hasPermission("grenadesplus.place."+p.getName().replace(" ", "_"))){
				sendNotification(sp, "Permission", ChatColor.RED+"Denied", new SpoutItemStack(p), 1500);
				return false;
			}
		}
		return true;
	}
	
	public static boolean hasPermissionForDetonator(SpoutPlayer sp) {
		if(sp.hasPermission("grenadesplus.detonator"))
			return true;
		else
			sendNotification(sp, "Permission", ChatColor.RED+"Denied", new SpoutItemStack(GrenadesPlus.detonator), 1500);
		return false;
	}
	
	public static GrenadesPlusPlayer getPlayerBySpoutPlayer(SpoutPlayer sp){
		for(GrenadesPlusPlayer gp : GrenadesPlus.GrenadesPlusPlayers){
			if(gp.getPlayer().equals(sp)){
				return gp;
			}
		}
		return null;
	}
	
	public static GrenadesPlusPlayer getPlayerByName(String sp){
		for(GrenadesPlusPlayer gp : GrenadesPlus.GrenadesPlusPlayers){
			if(gp.getPlayer().getName().equals(sp)){
				return gp;
			}
		}
		return null;
	}
	
	public static boolean hasSpoutcraft(Player p) {
		SpoutPlayer sp = SpoutManager.getPlayer(p);
		return sp.isSpoutCraftEnabled();
	}

	public static void sendNotification(SpoutPlayer sp, String title,
			String text, ItemStack icon, int duration) {
		if(!GrenadesPlus.notifications) return;
		if(title.length()>26){
			Util.warn("Too long notification. Check your gun and addition names.");
			title = title.replace(title.substring(25, title.length()-1),"");
		}
		if(text.length()>26){
			Util.warn("Too long notification. Check your gun and addition names.");
			text = text.replace(text.substring(25, text.length()-1),"");
		}
		sp.sendNotification(title, text, icon, duration);
	}
	
	public static void playArmAnimation(SpoutPlayer sp){
		((WorldServer) ((CraftPlayer)sp).getHandle().world).tracker.a(((CraftPlayer)sp).getHandle(), new Packet18ArmAnimation(((CraftPlayer)sp).getHandle(), 1));
	}
}
