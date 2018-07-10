package de.ngloader.multiinventory.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import de.ngloader.multiinventory.MultiInventory;

public class CommandMultiInventory implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length > 0) {
			switch (args[0]) {
			case "reload":
				MultiInventory.getInstance().loadConfigs();
				sender.sendMessage("§8[§4M§culti§4I§cnv] §7Configs §asuccessful §7reloaded§8.");
				return true;

//			case "inv":
//			case "inventory":
//				if(args.length > 1) {
//					if(sender instanceof Player) {
//						switch(args[1]) {
//						case "del":
//						case "delete":
//							
//							return true;
//
//						case "create":
//							
//							return true;
//
//						case "load":
//							
//							return true;
//						}
//						sender.sendMessage("§8[§4M§culti§4I§cnv] §7/§emultiinv inventory §7<§ecreate§8|§edelete§8|§eload§7>§8.");
//						return true;
//					} else
//						sender.sendMessage("§8[§4M§culti§4I§cnv] §7You must be a §cplayer§8.");
//				} else
//					sender.sendMessage("§8[§4M§culti§4I§cnv] §7/§emultiinv inventory §7<§ecreate§8|§edelete§8§eload§7>§8.");
//				return true;

				default:
					sender.sendMessage("§8[§4M§culti§4I§cnv] §7/§emultiinv §7<§ereload§7>§8.");
					break;
			}
		} else
			sender.sendMessage("§8[§4M§culti§4I§cnv] §7/§emultiinv §7<§ereload§7>§8.");
		return true;
	}
}