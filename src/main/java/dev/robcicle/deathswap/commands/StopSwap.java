package dev.robcicle.deathswap.commands;

import dev.robcicle.deathswap.Main;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StopSwap implements CommandExecutor {
    
    private final DeathSwap swap;
    
    public StopSwap(Main plugin) {
        this.swap = plugin.deathSwap;
        plugin.getCommand("stopswap").setExecutor(this);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (!swap.running) {
            player.sendMessage("There isn't currently a swap occurring.");
        } else {
            swap.running = false;
            Bukkit.broadcast(Component.text("Swap has been stopped!"));
        }
        return true;
    }
}