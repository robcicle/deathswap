package me.robcicle.deathswap.commands;

import me.robcicle.deathswap.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StopSwap implements CommandExecutor{
	
	private Main plugin;
	private DeathSwap swap;
	
	public StopSwap(Main plugin){
		this.plugin = plugin;
		swap = plugin.deathSwap;
		plugin.getCommand("stopswap").setExecutor(this);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		
		Player p = (Player) sender;
		
		if (!swap.running) {
			p.sendMessage("There isn't currently a swap occuring.");
			return true;
		}
		else if (swap.running) {
			swap.running = false;
			Bukkit.broadcastMessage("Swap has been stopped!");
			return true;
		}
		return true;
	}

}
