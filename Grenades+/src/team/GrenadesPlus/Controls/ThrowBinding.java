package team.GrenadesPlus.Controls;

import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.event.input.KeyBindingEvent;
import org.getspout.spoutapi.gui.ScreenType;
import org.getspout.spoutapi.keyboard.BindingExecutionDelegate;

import team.GrenadesPlus.GrenadesPlus;
import team.GrenadesPlus.GrenadesPlusPlayer;
import team.GrenadesPlus.Util.ExplosiveUtils;
import team.GrenadesPlus.Util.PlayerUtils;

public class ThrowBinding implements BindingExecutionDelegate{
	
	private GrenadesPlus plugin;
	private KeyType keytype;
	
	public  ThrowBinding(GrenadesPlus p, KeyType kt){
		plugin = p;
		keytype = kt;
		SpoutManager.getKeyBindingManager().registerBinding("Throw", kt.getKey(), "If pressed grenades will be thrown.", this, plugin);
	}
	
	@Override
	public void keyPressed(KeyBindingEvent e) {		
		if(e.getScreenType().equals(ScreenType.GAME_SCREEN)){
			GrenadesPlusPlayer gpp = PlayerUtils.getPlayerBySpoutPlayer(e.getPlayer());
			if(ExplosiveUtils.isThrowable(gpp.getPlayer().getItemInHand())&&!keytype.isHoldKey()){
				PlayerUtils.playArmAnimation(e.getPlayer());
				gpp.Throw(ExplosiveUtils.getThrowable(gpp.getPlayer().getItemInHand()));
			}
		}
	}

	@Override
	public void keyReleased(KeyBindingEvent e) {
		if(e.getScreenType().equals(ScreenType.GAME_SCREEN)){
			GrenadesPlusPlayer gpp = PlayerUtils.getPlayerBySpoutPlayer(e.getPlayer());
			if(ExplosiveUtils.isThrowable(gpp.getPlayer().getItemInHand())&&keytype.isHoldKey()){
				PlayerUtils.playArmAnimation(e.getPlayer());
				gpp.Throw(ExplosiveUtils.getThrowable(gpp.getPlayer().getItemInHand()));
			}
		}
	}

}
