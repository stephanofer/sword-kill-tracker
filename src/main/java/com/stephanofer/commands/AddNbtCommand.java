package com.stephanofer.commands;

import de.tr7zw.nbtapi.NBT;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AddNbtCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Este comando solo puede ser usado por jugadores.");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Uso: /addnbt <key> <value> [type]");
            sender.sendMessage(ChatColor.GRAY + "Tipos disponibles: string, int, double, float, long, boolean, byte, short");
            sender.sendMessage(ChatColor.GRAY + "Por defecto se usa 'string' si no especificas el tipo");
            return true;
        }

        Player player = (Player) sender;
        ItemStack item = player.getItemInHand();

        if (item == null || item.getType().name().contains("AIR")) {
            player.sendMessage(ChatColor.RED + "Debes tener un item en tu mano principal.");
            return true;
        }

        String key = args[0];
        String value = args[1];
        String type = args.length >= 3 ? args[2].toLowerCase() : "string";

        try {
            NBT.modify(item, nbt -> {
                switch (type) {
                    case "string":
                        nbt.setString(key, value);
                        break;
                    case "int":
                    case "integer":
                        nbt.setInteger(key, Integer.parseInt(value));
                        break;
                    case "double":
                        nbt.setDouble(key, Double.parseDouble(value));
                        break;
                    case "float":
                        nbt.setFloat(key, Float.parseFloat(value));
                        break;
                    case "long":
                        nbt.setLong(key, Long.parseLong(value));
                        break;
                    case "boolean":
                    case "bool":
                        nbt.setBoolean(key, Boolean.parseBoolean(value));
                        break;
                    case "byte":
                        nbt.setByte(key, Byte.parseByte(value));
                        break;
                    case "short":
                        nbt.setShort(key, Short.parseShort(value));
                        break;
                    default:
                        player.sendMessage(ChatColor.RED + "Tipo inválido. Usa: string, int, double, float, long, boolean, byte, short");
                        return;
                }
                
                player.sendMessage(ChatColor.GREEN + "NBT agregado exitosamente!");
                player.sendMessage(ChatColor.YELLOW + "Key: " + ChatColor.WHITE + key);
                player.sendMessage(ChatColor.YELLOW + "Value: " + ChatColor.WHITE + value);
                player.sendMessage(ChatColor.YELLOW + "Type: " + ChatColor.WHITE + type);
            });
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "Error: El valor '" + value + "' no es válido para el tipo '" + type + "'");
        }

        return true;
    }
}
