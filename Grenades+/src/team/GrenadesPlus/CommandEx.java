package team.GrenadesPlus;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandEx implements CommandExecutor{
	private GrenadesPlus plugin;
	
	public CommandEx(GrenadesPlus gunsPlus) {
		plugin = gunsPlus;
		help[0] = (ChatColor.GOLD + "------Grenades+ Version:" + plugin.getDescription().getVersion() + "-------");
		help[1] = (ChatColor.BLUE + "-   /Grenades+ Credits   ~  Displays Credits of Grenades+");
		help[2] = (ChatColor.BLUE + "-   /Grenades+ Reload    ~  Reloads Config");
		help[3] = (ChatColor.BLUE + "-   /Grenades+ List      ~  Lists all Loaded Items");
	}
	String[] help = new String[4];

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args[0].equalsIgnoreCase("help")) {
			sender.sendMessage(help);
			return true;
		}
		return false;
	}

}
