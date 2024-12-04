package com.stephanofer;

import com.stephanofer.commands.MainCommand;
import com.stephanofer.listeners.PlayerKillListener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    @Override
    public void onEnable(){
        registerEvents();
        registerCommand();
        getLogger().info("Starting this plugin");
    }

    @Override
    public void onDisable(){
        getLogger().info("Disabling this plugin");
    }

    public void registerCommand(){
        getCommand("swordkilltracker").setExecutor(new MainCommand());
    }

    public void registerEvents(){
        getServer().getPluginManager().registerEvents(new PlayerKillListener(), this);
    }
}