package me.robcicle.deathswap.commands;

import me.robcicle.deathswap.Main;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.bukkit.Location;
import org.bukkit.Sound;

public class DeathSwap extends BukkitRunnable implements CommandExecutor{
	
	private Main plugin;
	public float timer = 60;
	public float seconds;
	public boolean running = false;
	
	public Player p1;
	public Player p2;
	
	public DeathSwap(Main plugin){
		this.plugin = plugin;
		plugin.getCommand("swap").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		
		Player p = (Player) sender;
		
		float timerInput;
		
		if (args.length == 3 && !running) {
			
			if (Bukkit.getPlayer(args[0]).isOnline()) {
				p1 = Bukkit.getPlayer(args[0]);
			}
			else {
				p.sendMessage(args[0] + " is not currently a valid player.");
				return true;
			}
			
			if (Bukkit.getPlayer(args[1]).isOnline()) {
				p2 = Bukkit.getPlayer(args[1]);
				if (p1 == p2) {
					p.sendMessage(args[0] + " and " + args[1] + " are the same person.");
					return true;
				}
			}
			else {
				p.sendMessage(args[1] + " is not currently a valid player.");
				return true;
			}
			
			try {
				timerInput = Float.parseFloat(args[2]);
			} catch (NumberFormatException nfe){
				p.sendMessage(args[2] + " is not a number.");
				return true;
			}
			
			if (timerInput < 1) {
				p.sendMessage("Your timer must be more than 2 minutes.");
				return true;
			}

			running = true;
			
			timer = timerInput * 60;
			seconds = timer;
			Bukkit.broadcastMessage(ChatColor.BOLD + "Your timer has now started, Good Luck!");
			return true;
		}
		else if (args.length != 3 && !running) {
			p.sendMessage("The command must be {/swap PlayerName PlayerName2 CountdownTime} or else it won't work.");
			return true;
		}
		else if (running) {
			p.sendMessage("A swap is already happening");
			return true;
		}
		else {
			p.sendMessage("An error has occured and tbh I'm not sure what it is. Sorry.");
			return true;
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		
		if (running) {
			if (p1.isDead()) {
				running = false;
				Bukkit.broadcastMessage(ChatColor.GREEN + "" + ChatColor.BOLD + p2.getName() + ChatColor.GREEN + "" + ChatColor.BOLD + " is the winner of" + ChatColor.RED + "" + ChatColor.BOLD + " Death Swap" + ChatColor.GREEN + "!");
				p1.sendTitle(ChatColor.GREEN + "" + ChatColor.BOLD + "Winner!", ChatColor.GREEN + "" + ChatColor.BOLD + p2.getName() + " is the Winner!");
				p2.sendTitle(ChatColor.GREEN + "" + ChatColor.BOLD + "Winner!", ChatColor.GREEN + "" + ChatColor.BOLD + p2.getName() + " is the Winner!");
				Win(p2);
			}
			else if (p2.isDead()) {
				running = false;
				Bukkit.broadcastMessage(ChatColor.GREEN + "" + ChatColor.BOLD + p1.getName() + ChatColor.GREEN + "" + ChatColor.BOLD + " is the winner of" + ChatColor.RED + "" + ChatColor.BOLD + " Death Swap" + ChatColor.GREEN + "!");
				p1.sendTitle(ChatColor.GREEN + "" + ChatColor.BOLD + "Winner!", ChatColor.GREEN + "" + ChatColor.BOLD + p1.getName() + " is the Winner!");
				p2.sendTitle(ChatColor.GREEN + "" + ChatColor.BOLD + "Winner!", ChatColor.GREEN + "" + ChatColor.BOLD + p1.getName() + " is the Winner!");
				Win(p1);
			}
			
			seconds -= 1;
			if (seconds == 10)
				Bukkit.broadcastMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "SWAPPING IN 10!");
			else if (seconds == 5)
				Bukkit.broadcastMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "SWAPPING IN 5!");
			else if (seconds == 4)
				Bukkit.broadcastMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "SWAPPING IN 4!");
			else if (seconds == 3)
				Bukkit.broadcastMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "SWAPPING IN 3!");
			else if (seconds == 2)
				Bukkit.broadcastMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "SWAPPING IN 2!");
			else if (seconds == 1)
				Bukkit.broadcastMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "SWAPPING IN 1!");	
			else if (seconds <= 0) {
				seconds = timer;
				Bukkit.broadcastMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "SWAPPING!");
				Swap();
			}
			
		}
		
	}
	
	private void Win(Player pWin) {
		
		Location loc = pWin.getLocation();
		World w = pWin.getWorld();
		int diameter = 4; //Diameter of the circle centered on loc
		int fireworkAmount = 10;

		for (int i = 0; i < fireworkAmount; i++)
		{
		    Location newLocation = loc.add(new Vector(Math.random()-0.5, 0, Math.random()-0.5).multiply(diameter));
		    w.spawnEntity(newLocation, EntityType.FIREWORK);
		}
		
		pWin.playSound(pWin.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 0);
	}
	
	private void Swap() {		
		Location l1 = p1.getLocation();
		Location l2 = p2.getLocation();
		
		p1.teleport(l2);
		p1.playSound(p1.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 0);
		p2.teleport(l1);
		p2.playSound(p2.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 0);
	}
}
