package vg.civcraft.mc.bettershards.command.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import vg.civcraft.mc.bettershards.BetterShardsAPI;
import vg.civcraft.mc.bettershards.BetterShardsPlugin;
import vg.civcraft.mc.bettershards.PortalsManager;
import vg.civcraft.mc.bettershards.misc.Grid;
import vg.civcraft.mc.bettershards.misc.Grid.GridLocation;
import vg.civcraft.mc.bettershards.portal.Portal;
import vg.civcraft.mc.bettershards.portal.PortalType;
import vg.civcraft.mc.bettershards.portal.portals.CuboidPortal;
import vg.civcraft.mc.civmodcore.command.PlayerCommand;

public class CreatePortal extends PlayerCommand {

	private PortalsManager pm = BetterShardsAPI.getPortalsManager();
	public CreatePortal(String name) {
		super(name);
		setIdentifier("bsc");
		setDescription("Creates a portal from the selection made.");
		setUsage("/bsc <name>, <PortalType>");
		setArguments(1,2);
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Need to be instance of player.");
			return true;
		}
		Player p = (Player) sender;
		Grid g = BetterShardsPlugin.getInstance().getPlayerGrid(p);
		if (g.getMissingSelection() == GridLocation.NOSELECTION) 
			return sendPlayerMessage(p, ChatColor.RED + "You have not selected any points.", true);
		else if (g.getMissingSelection() == GridLocation.RIGHTSELECTION)
			return sendPlayerMessage(p, ChatColor.RED + "Your secondary selection has not been chosen.", true);
		else if (g.getMissingSelection() == GridLocation.LEFTSELECTION)
			return sendPlayerMessage(p, ChatColor.RED + "Your primary selection has not been chosen.", true);
		int selection = -1;
		try {
			selection = Integer.parseInt(args[1]);
		} catch (Exception e) {
			PortalType.sendPlayerErrorMessage(p);
			return true;
		}
		PortalType type = PortalType.fromOrdeal(selection);
		if (type == null) {
			PortalType.sendPlayerErrorMessage(p);
			return true;
		}
		Portal portal = null;
		switch(type.ordinal()) {
		case 0:
			portal = new CuboidPortal(args[0], g.getFocusedLocation(), g.getXRadius(), g.getYRadius(), g.getZRadius(), true);
		}
		if (pm.getPortal(args[0]) != null) 
			return sendPlayerMessage(p, ChatColor.RED + "That portal name already exists.", true);
		pm.createPortal(portal);
		String m = ChatColor.GREEN + "You have successfully created the portal " + args[0] + ".\n"
				+ "To add a connection use the command /bsj <main portal> <connection>";
		return sendPlayerMessage(p, m, true);
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String[] args) {
		return null;
	}

}
