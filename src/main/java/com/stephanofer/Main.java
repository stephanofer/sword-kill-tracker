package com.stephanofer;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    @Override
    public void onEnable(){
        getLogger().info("Starting this plugin");
    }

    @Override
    public void onDisable(){
        getLogger().info("Disabling this plugin");
    }
}