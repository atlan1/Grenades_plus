package team.old.GrenadesPack;

import java.util.ArrayList;



public class Trigger {

	
	public GrenadesPack plugin;
	private final int id;
	private ArrayList<Object> args = new ArrayList<Object>();
	
	public Trigger(GrenadesPack p , String name, Object...o){
		plugin = p;
		if(name.equalsIgnoreCase("time")){
			id=0;
			args.add(o[0]);
		}else if(name.equalsIgnoreCase("detonator")){
			id=1;
			args.add(o[0]);
		}else if(name.equalsIgnoreCase("onhit")){
			id=2;
			args.add(o[0]);
		}else if(name.equalsIgnoreCase("shock")){
			id=3;
			args.add(o[0]);
		}else{
			id = 4;
			plugin.log.info(plugin.PREFIX+" Unknown trigger type "+name+"!");
		}
	}


	public int getId() {
		return id;
	}
	
	public ArrayList<Object> getArgs() {
		return args;
	}
}
