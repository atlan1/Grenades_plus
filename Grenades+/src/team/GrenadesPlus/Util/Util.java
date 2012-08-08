package team.GrenadesPlus.Util;

import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockIterator;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.inventory.SpoutItemStack;
import org.getspout.spoutapi.material.item.GenericCustomItem;
import org.getspout.spoutapi.sound.SoundManager;

import team.ApiPlus.API.Effect.EffectType;
import team.ApiPlus.API.Effect.EntityEffect;
import team.ApiPlus.Util.Utils;
import team.GrenadesPlus.GrenadesPlus;
import team.GrenadesPlus.Block.Placeable;
import team.GrenadesPlus.Effects.EffectTargetImpl;
import team.GrenadesPlus.Manager.ConfigLoader;
import team.GrenadesPlus.Item.Throwable;

public class Util {
	
	public static void printIDs() {
		if (ConfigLoader.generalConfig.getBoolean("id-info-throwables", true)) {
			GrenadesPlus.log.log(Level.INFO, GrenadesPlus.PRE
					+ " ------------  ID's of throwable explosives: -----------------");
			if (GrenadesPlus.allThrowables.isEmpty())
				GrenadesPlus.log.log(Level.INFO, "EMPTY");
			for (Throwable gun : GrenadesPlus.allThrowables) {
				GrenadesPlus.log.log(Level.INFO, "ID of " + gun.getName() + ": "
						+ new SpoutItemStack((GenericCustomItem)gun).getTypeId() + ":"
						+ new SpoutItemStack((GenericCustomItem)gun).getDurability());
			}
		}
		if (ConfigLoader.generalConfig.getBoolean("id-info-placeables", true)) {
			GrenadesPlus.log.log(Level.INFO, GrenadesPlus.PRE
					+ " ------------  ID's of placeable explosives: -----------------");
			if (GrenadesPlus.allPlaceables.isEmpty())
				GrenadesPlus.log.log(Level.INFO, "EMPTY");
			for (Placeable gun : GrenadesPlus.allPlaceables) {
				GrenadesPlus.log.log(Level.INFO, "ID of " + gun.getName() + ": "
						+ new SpoutItemStack(gun).getTypeId() + ":"
						+ new SpoutItemStack(gun).getDurability());
			}
		}
		if (GrenadesPlus.detonator != null) {
			GrenadesPlus.log.info(GrenadesPlus.PRE+"----------- ID of the Detonator: ----------");
			GrenadesPlus.log.info(new SpoutItemStack(GrenadesPlus.detonator).getTypeId()+":"+new SpoutItemStack(GrenadesPlus.detonator).getDurability());
		}
	}
	public static boolean distanceSmallerThan(final Location l1, final Location l2, double distance){
		return l1.toVector().distance(l2.toVector())<distance;
	}
	
	public static boolean distanceBiggerThan(final Location l1, final Location l2, double distance){
		if(l1.toVector().distance(l2.toVector())>distance){
			return true;
		}
		return false;
	}
	
	public static boolean isGrenadesPlusMaterial(String name) {
		for (int i = 0; i < GrenadesPlus.allThrowables.size(); i++) {
			if (GrenadesPlus.allThrowables.get(i).getName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		for (int i = 0; i < GrenadesPlus.allPlaceables.size(); i++) {
			if (GrenadesPlus.allPlaceables.get(i).getName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

	public static Object getGrenadesPlusMaterial(String name) {
		Object cm = null;
		if(!isGrenadesPlusMaterial(name)) return cm;
		for (int i = 0; i < GrenadesPlus.allThrowables.size(); i++) {
			if (GrenadesPlus.allThrowables.get(i).getName().equalsIgnoreCase(name)) {
				cm = GrenadesPlus.allThrowables.get(i);
				return cm;
			}
		}
		for (int i = 0; i < GrenadesPlus.allPlaceables.size(); i++) {
			if (GrenadesPlus.allPlaceables.get(i).getName().equalsIgnoreCase(name)) {
				cm = GrenadesPlus.allPlaceables.get(i);
				return cm;
			}
		}
		return cm;
	}
	
	public static boolean containsCustomItems(List<ItemStack> items){
		for(ItemStack i : items){
			if(isCustomItem(i)){
				return true;
			}
		}
		return false;
	}
	
	public static boolean isCustomItem(ItemStack item){
		return new SpoutItemStack(item).isCustomItem();
	}
	
	public static Block getBlockInSight(Location l, int blockIndex, int maxradius){
		BlockIterator bi = new BlockIterator(l.getWorld(), l.toVector(), l.getDirection(), 0d, maxradius);
		Block b = null;
		for(int i = 0; i<blockIndex; i++){
			if(bi.hasNext()){
				b =  bi.next(); 
			}else break;
		}
		return b;
	}

	public static void warn(String msg) {
		if (GrenadesPlus.warnings) {
			GrenadesPlus.log.warning(GrenadesPlus.PRE + " " + msg);
		}
	}

	public static void info(String msg) {
		GrenadesPlus.log.info(GrenadesPlus.PRE + " " + msg);
	}
	
	public static void debug(Exception e){
		if(GrenadesPlus.debug){
			GrenadesPlus.log.info(GrenadesPlus.PRE + "[Debug] " + e.getMessage());
			e.printStackTrace();
		}
	}

	public static Projectile launchProjectile(Class<? extends Projectile> c,
			Location from, Location to, float speed) {
		Projectile e = from.getWorld().spawn(from, c);
		e.setVelocity(to.toVector().multiply(speed));
		Bukkit.getPluginManager().callEvent(new ProjectileLaunchEvent(e));
		return e;
	}

	public static boolean canSee(final Location observer, final Location observed, int range) {
		Location o = observer.clone();
		Location w = observed.clone();
		if(o.toVector().distance(w.toVector())>range) return false;
		BlockIterator bitr = new BlockIterator(Utils.setLookingAt(o, w), 0, range);
		while (bitr.hasNext()) {
			Block b = bitr.next();
			if(b.equals(w.getBlock())) return true;
			if (!Utils.isTransparent(b)) {
				break;
			}
		}
		return false;
	}

	public static void playCustomSound(GrenadesPlus plugin, Location l, String url,
			int volume) {
		SoundManager SM = SpoutManager.getSoundManager();
		SM.playGlobalCustomSoundEffect(plugin, url, false, l, 40, volume);
	}

	public static boolean isAllowedWithEffectTarget(EffectType efftyp, EffectTargetImpl efftar) {
		switch(efftar.getType()){
			case EXPLOSIVELOCATION:
				if(efftyp.getEffectClass().isAssignableFrom(EntityEffect.class)){
					return false;
				}else return true;
			case TARGETENTITY:
				return true;
			case TARGETLOCATION:
				if(efftyp.getEffectClass().isAssignableFrom(EntityEffect.class)){
					return false;
				}else return true;
			case GRENADIER:
				return true;
			case GRENADIERLOCATION:
				if(efftyp.getEffectClass().isAssignableFrom(EntityEffect.class)){
					return false;
				}else return true;
		}
		
		return false;
	}
}
