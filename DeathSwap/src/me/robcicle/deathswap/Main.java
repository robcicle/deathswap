package me.robcicle.deathswap;

import me.robcicle.deathswap.commands.*;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener{
	
	public DeathSwap deathSwap;
	StopSwap stopSwap;

	@Override
	public void onEnable() {
		deathSwap = new DeathSwap(this);
		
		deathSwap.runTaskTimer(this, 0, 1 * 20);
		
		new StopSwap(this);
	}
	
}
