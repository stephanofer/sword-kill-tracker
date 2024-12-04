package com.stephanofer.commands;

import de.tr7zw.nbtapi.NBT;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class MainCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {

        if (args.length != 3){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c!Ups! &fRecuerda usar &a/swordkilltracker give <ITEM> <Jugador>"));
            return true;
        }

            String identifier = args[0];
            String inputMaterial = args[1].toUpperCase();
            Player player = Bukkit.getPlayer(args[2]);
            Material material;

            boolean isPlayerValid = player != null;

            if (identifier.equalsIgnoreCase("give")){
                if(!isPlayerValid){
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c!Ups¡ &fEl jugador &c"+ args[2] + " &fno esta conectado."));
                    return true;
                }

                try{
                    material = Material.valueOf(inputMaterial);
                }catch (IllegalArgumentException e){
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c!Ups¡ &fEl Item &c"+ inputMaterial + " &Fno valido."));
                    return true;
                }

                ItemStack itemCreated = createItem(material);

                player.getInventory().addItem(itemCreated);

                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a!Exito¡ &fSe entrego correctamete el item al jugador &c"+ player.getName()));

            } else{
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c!Ups! &fRecuerda usar &a/swordkilltracker give <item> <Jugador>"));
            }


        return true;
    }

    public ItemStack createItem(Material material){
        ItemStack item = new ItemStack(material);

        NBT.modify(item, nbt -> {
            nbt.setInteger("KillCount", 0);
        });

        return item;
    }
}
