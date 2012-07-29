package team.GrenadesPlus;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import team.GrenadesPlus.Block.Placeable;

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
		}else if(args[0].equalsIgnoreCase("credits")) {
			sender.sendMessage(ChatColor.GREEN+"Grenades+ by "+ChatColor.MAGIC+plugin.getDescription().getAuthors().toString());
		}else if(args[0].equalsIgnoreCase("reload")) {
			if(!sender.hasPermission("grenadesplus.command.reload")) return false;
			plugin.reload();
		}else if(args[0].equalsIgnoreCase("list")) {
			if(!sender.hasPermission("grenadesplus.command.list")) return false;
			sender.sendMessage(ChatColor.BLUE + "---Placeable Explosives----");
			for(Placeable p : GrenadesPlus.allPlaceables) {
				sender.sendMessage(ChatColor.GRAY + "- "+ p.getName());
			}
			sender.sendMessage(ChatColor.BLUE + "---Throwable Explosives----");
			for(team.GrenadesPlus.Item.Throwable p : GrenadesPlus.allThrowables) {
				sender.sendMessage(ChatColor.GRAY + "- "+ p.getName());
			}
		}
		return false;
	}

}
