package dev.robcicle.deathswap;

import dev.robcicle.deathswap.commands.DeathSwap;
import dev.robcicle.deathswap.commands.StopSwap;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
    
    public DeathSwap deathSwap;
    public StopSwap stopSwap;

    @Override
    public void onEnable() {
        deathSwap = new DeathSwap(this);
        deathSwap.runTaskTimer(this, 0, 20);
        
        stopSwap = new StopSwap(this);
    }
}