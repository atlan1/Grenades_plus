package team.GrenadesPlus;

import net.minecraft.server.Packet18ArmAnimation;

import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.event.input.KeyBindingEvent;
import org.getspout.spoutapi.gui.ScreenType;
import org.getspout.spoutapi.keyboard.BindingExecutionDelegate;

import team.GrenadesPlus.Enum.KeyType;
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
			Packet18ArmAnimation pa = new Packet18ArmAnimation();
			CraftPlayer cp = (CraftPlayer) gpp.getPlayer();
			cp.getHandle().netServerHandler.sendPacket(pa);
			if(ExplosiveUtils.isThrowable(gpp.getPlayer().getItemInHand())&&!keytype.isHoldKey()){
				gpp.Throw(ExplosiveUtils.getThrowable(gpp.getPlayer().getItemInHand()));
			}
		}
	}

	@Override
	public void keyReleased(KeyBindingEvent e) {
		if(e.getScreenType().equals(ScreenType.GAME_SCREEN)){
			GrenadesPlusPlayer gpp = PlayerUtils.getPlayerBySpoutPlayer(e.getPlayer());
			if(ExplosiveUtils.isThrowable(gpp.getPlayer().getItemInHand())&&keytype.isHoldKey()){
				gpp.Throw(ExplosiveUtils.getThrowable(gpp.getPlayer().getItemInHand()));
			}
		}
	}

}
