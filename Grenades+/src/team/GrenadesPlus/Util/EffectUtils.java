package team.GrenadesPlus.Util;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import team.ApiPlus.API.Effect.Effect;
import team.ApiPlus.API.Effect.EntityEffect;
import team.ApiPlus.API.Effect.LocationEffect;
import team.ApiPlus.API.Effect.SphereEffect;
import team.ApiPlus.API.Effect.Default.MoveEffect;
import team.ApiPlus.Util.Utils;
import team.GrenadesPlus.Effects.EffectSection;

public class EffectUtils {
	
	private static Location[] switchLocation(Effect e, Location[] grenadier, Location[] target, Location[] ex){
		Location[] ret = null;
		switch ((EffectSection)e.getEffectTarget()) {
			case TARGETLOCATION:
				ret = target;
				break;
			case GRENADIERLOCATION:
				ret = grenadier;
				break;
			case TARGETENTITY:
				ret = target;
				break;
			case GRENADIER:
				ret = grenadier;
				break;
			case EXPLOSIVELOCATION:
				ret = ex;
				break;
		}
		return ret;
	}
	
	private static LivingEntity[] switchEntity(Effect e, LivingEntity[] grenadier, LivingEntity[] targets){
		switch((EffectSection)e.getEffectTarget()){
			case TARGETENTITY:
				return targets;
			case GRENADIER:
				return grenadier;
			default:
				return null;
		}	
	}
	
	private static Location[] getTargetLocations(LivingEntity[] livs) {
		Location[] locs = new Location[livs.length];
		for(int i=0;i<livs.length;i++)
			locs[i] = livs[i].getLocation();
		return locs;
	}
	
	private static LivingEntity[] getTargetEntities(Location ex, int radius) {
		List<Entity> ents = new ArrayList<Entity>(Utils.getNearbyEntities(ex, radius, radius, radius));
		List<LivingEntity> livs = new ArrayList<LivingEntity>();
		for(Entity e : ents)
			if(e instanceof LivingEntity)
				livs.add((LivingEntity) e);
		return livs.toArray(new LivingEntity[livs.size()]);
	}
	
	public static void performLocationEffect(LocationEffect e, Location grenadier, Location ex) {
		int radius = (Integer)((EffectSection) e.getEffectTarget()).getProperty("RADIUS");
		Location[] switched = switchLocation(e, new Location[]{grenadier}, getTargetLocations(getTargetEntities(ex, radius)), new Location[]{ex});
		for(Location s:switched)
			new SphereEffect(s, radius, e).start();
	}

	public static void performEntityEffect(EntityEffect e, LivingEntity grenadier, Location ex) {
		int radius = (Integer)((EffectSection) e.getEffectTarget()).getProperty("RADIUS");
		List<LivingEntity> targets = Arrays.asList(getTargetEntities(ex, radius));
		if(targets.contains(grenadier)&&(EffectSection)e.getEffectTarget() != EffectSection.GRENADIER) return;
		LivingEntity[] les = switchEntity(e, new LivingEntity[]{grenadier}, (LivingEntity[]) targets.toArray());
		if(e instanceof MoveEffect){
			for(LivingEntity l:les)
				e.performEffect(l, grenadier.getEyeLocation()); 
		}else
			for(LivingEntity l:les)
				e.performEffect(l);
	}
}
