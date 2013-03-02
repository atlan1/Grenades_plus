package team.GrenadesPlus.Util;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.block.SpoutBlock;
import org.getspout.spoutapi.inventory.SpoutItemStack;

import team.ApiPlus.API.PropertyHolder;
import team.ApiPlus.API.Effect.Effect;
import team.ApiPlus.API.Effect.EffectType;
import team.ApiPlus.API.Effect.EntityEffect;
import team.ApiPlus.API.Effect.LocationEffect;
import team.GrenadesPlus.GrenadesPlus;
import team.GrenadesPlus.Block.Placeable;
import team.GrenadesPlus.Item.Throwable;
import team.GrenadesPlus.Block.Designs.DesignType;;

public class ExplosiveUtils {
	
	public static String getPlaceableName(Placeable p){
		if(isParentPlaceable(p))
			return p.getName();
		else if(isWallPlaceable(p))
			return getParentPlaceable(p).getName();
		else
			return "I am unknown";
	}
	
	public static boolean isParentPlaceable(Placeable p){
		return GrenadesPlus.allPlaceables.contains(p);
	}
	
	public static boolean isWallPlaceable(Placeable p) {
		for(Placeable t : GrenadesPlus.wallDesignPlaceables.keySet()){
			if(GrenadesPlus.wallDesignPlaceables.get(t).values().contains(p))
				return true;
		}
		return false;
	}
	
	public static Placeable getParentPlaceable(Placeable p){
		if(! isWallPlaceable(p)) return null;
		for(Placeable t : GrenadesPlus.wallDesignPlaceables.keySet()){
			if(GrenadesPlus.wallDesignPlaceables.get(t).values().contains(p))
				return t;
		}
		return null;
	}

	public static Item throwThrowable(Inventory inv, ItemStack i, Location direction, double s){
		if(isThrowable(i)){
			Item drop = direction.getWorld().dropItem(direction, new SpoutItemStack(getThrowable(i)));
			drop.setPickupDelay(Integer.MAX_VALUE);
			drop.setVelocity(direction.getDirection().multiply(s));
			removeThrowable(inv, i);
			return drop;
		}
		return null;
	}
	
	public static List<Block> getBlocksInRange(Location l, int range, List<Block> list){
		List<Block> inrange = new ArrayList<Block>();
		for(Block b:list){
			if(b.getLocation().toVector().distance(l.toVector())<=range){
				inrange.add(b);
			}
		}
		return inrange;
	}
	
	public static void removeThrowable(Inventory i, ItemStack is){
		if(!i.contains(is)) return;
		if(is.getAmount()>1)
			is.setAmount(is.getAmount()-1);
		else
			i.remove(is);
	}
	
	public static boolean isDetonator(ItemStack i){
		if(i == null) return false;
		SpoutItemStack sis = new SpoutItemStack(GrenadesPlus.detonator);
		if(i.getDurability()==sis.getDurability()&&i.getTypeId()==sis.getTypeId()){
			return true;
		}
		return false;
	}
	
	public static boolean isThrowable(ItemStack i){
		if(i == null) return false;
		for(Throwable g : GrenadesPlus.allThrowables){
			SpoutItemStack sis = new SpoutItemStack(g);
			if(sis.getDurability()==i.getDurability()&&sis.getTypeId()==i.getTypeId()){
				return true;
			}
		}
		return false;
	}
	
	public static boolean isPlaceable(ItemStack i){
		if(i == null) return false;
		for(Placeable e : GrenadesPlus.allPlaceables){
			SpoutItemStack sis = new SpoutItemStack(e);
			if(sis.getDurability()==i.getDurability()&&sis.getTypeId()==i.getTypeId()){
				return true;
			}
			if(((DesignType)e.getProperty("DESIGN")).isAttaching())
				for(Placeable w : GrenadesPlus.wallDesignPlaceables.get(e).values()){
					SpoutItemStack sis2 = new SpoutItemStack(w);
					if(sis2.getDurability()==i.getDurability()&&sis2.getTypeId()==i.getTypeId()){
						return true;
					}
				}
		}
		return false;
	}
	
	public static boolean isPlaceable(Block b){
		if(b == null) return false;
		for(Placeable e : GrenadesPlus.allPlaceables){
			SpoutBlock sb = (SpoutBlock) b;
			if(sb.getCustomBlock()!=null&&sb.getCustomBlock().equals(e)){
				return true;
			}
			if(((DesignType)e.getProperty("DESIGN")).isAttaching())
				for(Placeable w : GrenadesPlus.wallDesignPlaceables.get(e).values()){
					SpoutBlock sb2 = (SpoutBlock) b;
					if(sb2.getCustomBlock()!=null&&sb2.getCustomBlock().equals(w)){
						return true;
					}
				}
		}
		return false;
	}
	
	public static Placeable getPlaceable(Block b){
		if(b == null) return null;
		for(Placeable e : GrenadesPlus.allPlaceables){
			SpoutBlock sb = (SpoutBlock) b;
			if(sb.getCustomBlock().equals(e)){
				return e;
			}
			for(Placeable w : GrenadesPlus.wallDesignPlaceables.get(e).values()){
				SpoutBlock sb2 = (SpoutBlock) b;
				if(sb2.getCustomBlock().equals(w)){
					return w;
				}
			}
		}
		return null;
	}
	
	public static Throwable getThrowable(ItemStack i){
		for(Throwable g : GrenadesPlus.allThrowables){
			SpoutItemStack sis = new SpoutItemStack(g);
			if(sis.getDurability()==i.getDurability()&&sis.getTypeId()==i.getTypeId()){
				return g;
			}
		}
		return null;
	}
	
	public static Placeable getPlaceable(ItemStack i){
		if(i == null) return null;
		for(Placeable e : GrenadesPlus.allPlaceables){
			SpoutItemStack sis = new SpoutItemStack(e);
			if(sis.getDurability()==i.getDurability()&&sis.getTypeId()==i.getTypeId()){
				return e;
			}
			for(Placeable w : GrenadesPlus.wallDesignPlaceables.get(e).values()){
				SpoutItemStack sis2 = new SpoutItemStack(w);
				if(sis2.getDurability()==i.getDurability()&&sis2.getTypeId()==i.getTypeId()){
					return w;
				}
			}
		}
		return null;
	}
	
	public static boolean isExplosive(ItemStack i){
		return isThrowable(i)||isPlaceable(i);
	}
	
	@SuppressWarnings("unchecked")
	public static void performEffects(Grenadier g, Explosive e, Location explosive){
		List<Effect> effects = (List<Effect>) ((PropertyHolder)e).getProperty("EFFECTS");
		LivingEntity grenadierEntity = null;
		Location grenadierLoc = null;
		if(g!=null&&g instanceof LivingGrenadier)
			grenadierEntity = ((LivingGrenadier) g).getLivingEntity();
		if(g!=null)
			grenadierLoc = g.getLocation();
		else 
			grenadierLoc = explosive;
		for(Effect eff : effects){
			switch(EffectType.getType(eff.getClass())){
				case EXPLOSION:
					EffectUtils.performLocationEffect((LocationEffect) eff, grenadierLoc, explosive);
					break;
				case LIGHTNING:
					EffectUtils.performLocationEffect((LocationEffect) eff, grenadierLoc, explosive);
					break;
				case POTION:
					EffectUtils.performEntityEffect((EntityEffect) eff, grenadierEntity, explosive);
					break;
				case PLACE:
					EffectUtils.performLocationEffect((LocationEffect) eff, grenadierLoc, explosive);
					break;
				case BREAK:
					EffectUtils.performLocationEffect((LocationEffect) eff, grenadierLoc, explosive);
					break;
				case PARTICLE:
					EffectUtils.performLocationEffect((LocationEffect) eff, grenadierLoc, explosive);
					break;
				case MOVE:
					EffectUtils.performEntityEffect((EntityEffect) eff, grenadierEntity, explosive);
					break;
				case BURN:
					EffectUtils.performEntityEffect((EntityEffect) eff, grenadierEntity, explosive);
					break;
			case CUSTOM:
				break;
			case SPAWN:
				break;
			default:
				break;
			}
		}
	}
	
}
