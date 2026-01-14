package com.stephanofer;

import com.stephanofer.commands.AddNbtCommand;
import com.stephanofer.commands.DebugItemCommand;
import com.stephanofer.commands.MainCommand;
import com.stephanofer.listeners.PlayerKillListener;

import think.rpgitems.api.RPGItems;
import think.rpgitems.item.RPGItem;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    @Override
    public void onEnable(){
        if (!checkDependencies()) {
            return; 
        }
        
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
        getCommand("debugitem").setExecutor(new DebugItemCommand());
        getCommand("addnbt").setExecutor(new AddNbtCommand());
    }

    public void registerEvents(){
        getServer().getPluginManager().registerEvents(new PlayerKillListener(), this);
    }

    private boolean checkDependencies() {
        // Verificar RPGItems
        if (!Bukkit.getPluginManager().isPluginEnabled("RPGItems")) {
            getLogger().severe("RPGItems no encontrado - deshabilitando plugin");
            Bukkit.getPluginManager().disablePlugin(this);
            return false;
        }
        getLogger().info("RPGItems detectado");

        // Verificar NBT-API
        // NBT-API se carga como dependencia, verificamos que esté disponible
        try {
            Class.forName("de.tr7zw.nbtapi.NBT");
            getLogger().info("NBT-API detectado");
        } catch (ClassNotFoundException e) {
            getLogger().severe("NBT-API no encontrado - deshabilitando plugin");
            Bukkit.getPluginManager().disablePlugin(this);
            return false;
        };

        return true;
    }
}