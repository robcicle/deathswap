package dev.robcicle.deathswap.commands;

import dev.robcicle.deathswap.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class DeathSwap extends BukkitRunnable implements CommandExecutor {
    
    private final Main plugin;
    private float timer = 60;
    private float seconds;
    private boolean running = false;
    
    private Player p1;
    private Player p2;
    
    public DeathSwap(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("swap").setExecutor(this);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (args.length != 3 || running) {
            player.sendMessage("The command must be {/swap PlayerName PlayerName2 CountdownTime} or else it won't work.");
            return true;
        }
        
        p1 = Bukkit.getPlayer(args[0]);
        p2 = Bukkit.getPlayer(args[1]);
        
        if (p1 == null || p2 == null || p1.equals(p2)) {
            player.sendMessage("Invalid players specified.");
            return true;
        }
        
        try {
            timer = Float.parseFloat(args[2]) * 60;
        } catch (NumberFormatException e) {
            player.sendMessage(args[2] + " is not a valid number.");
            return true;
        }
        
        if (timer < 120) {
            player.sendMessage("Your timer must be more than 2 minutes.");
            return true;
        }
        
        StartGame();
        return true;
    }
    
    private void StartGame() {
        running = true;
        seconds = timer;
        Bukkit.broadcast(Component.text("§lYour timer has now started, Good Luck!"));
        runTaskTimer(plugin, 0, 20); // Schedule the task to run every second
    }
    
    @Override
    public void run() {
        if (!running) return;
        
        if (p1.isDead() || p2.isDead()) {
            EndGame();
            return;
        }
        
        seconds -= 1;
        if (seconds <= 0) {
            seconds = timer;
            Bukkit.broadcast(Component.text(ChatColor.DARK_RED + "" + ChatColor.BOLD + "SWAPPING!"));
            SwapPlayers();
        } else if (seconds <= 10) {
            Bukkit.broadcast(Component.text(ChatColor.DARK_RED + "" + ChatColor.BOLD + "SWAPPING IN " + (int) seconds + "!"));
        }
    }
    
    private void EndGame() {
        running = false;
        Player winner = p1.isDead() ? p2 : p1;
        Bukkit.broadcast(Component.text(ChatColor.GREEN + "" + ChatColor.BOLD + winner.getName() + " is the winner of" + ChatColor.RED + "" + ChatColor.BOLD + " Death Swap" + ChatColor.GREEN + "!"));
        winner.showTitle(
            Title.title(
                Component.text(ChatColor.GREEN + "" + ChatColor.BOLD + "Winner!"),
                Component.text(ChatColor.GREEN + "" + ChatColor.BOLD + winner.getName() + " is the Winner!")
            )
        );
        CelebrateWin(winner);
    }
    
    private void CelebrateWin(Player winner) {
        Location loc = winner.getLocation();
        World world = winner.getWorld();
        int diameter = 4;
        int fireworkAmount = 10;

        for (int i = 0; i < fireworkAmount; i++) {
            Location newLocation = loc.add(new Vector(Math.random() - 0.5, 0, Math.random() - 0.5).multiply(diameter));
            world.spawnEntity(newLocation, EntityType.FIREWORK_ROCKET);
        }
        
        winner.playSound(winner.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 0);
    }
    
    private void SwapPlayers() {
        Location l1 = p1.getLocation();
        Location l2 = p2.getLocation();
        
        p1.teleport(l2);
        p1.playSound(l2, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 0);
        p2.teleport(l1);
        p2.playSound(l1, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 0);
    }

    public boolean GetRunning() {
        return running;
    }

    public void StopGame() {
        running = false;
    }
}