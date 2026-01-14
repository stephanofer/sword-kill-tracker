package com.stephanofer.commands;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.NBTType;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class DebugItemCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Este comando solo puede ser usado por jugadores.");
            return true;
        }

        Player player = (Player) sender;
        ItemStack item = player.getItemInHand();

        if (item == null || item.getType().name().contains("AIR")) {
            player.sendMessage(ChatColor.RED + "Debes tener un item en tu mano principal.");
            return true;
        }

        player.sendMessage(ChatColor.GOLD + "========== DEBUG ITEM ==========");
        player.sendMessage(ChatColor.YELLOW + "Item: " + ChatColor.WHITE + item.getType().name());
        player.sendMessage(ChatColor.YELLOW + "Cantidad: " + ChatColor.WHITE + item.getAmount());
        player.sendMessage("");

        // Mostrar NBT en formato plano (SNBT)
        String snbt = NBT.get(item, nbt -> {
            return nbt.toString();
        });
        player.sendMessage(ChatColor.AQUA + "NBT Plano (SNBT):");
        player.sendMessage(ChatColor.GRAY + snbt);
        player.sendMessage("");

        // Mostrar NBT formateado y legible
        player.sendMessage(ChatColor.GREEN + "NBT Formateado:");
        NBT.get(item, nbt -> {
            Set<String> keys = nbt.getKeys();
            if (keys.isEmpty()) {
                player.sendMessage(ChatColor.GRAY + "  (Sin datos NBT personalizados)");
            } else {
                for (String key : keys) {
                    NBTType type = nbt.getType(key);
                    Object value = getFormattedValue(nbt, key, type);
                    player.sendMessage(ChatColor.YELLOW + "  " + key + ChatColor.GRAY + " (" + type + ")" + ChatColor.WHITE + ": " + value);
                }
            }
            return null;
        });

        player.sendMessage(ChatColor.GOLD + "================================");
        return true;
    }

    private Object getFormattedValue(de.tr7zw.nbtapi.iface.ReadableNBT nbt, String key, NBTType type) {
        switch (type) {
            case NBTTagString:
                return "\"" + nbt.getString(key) + "\"";
            case NBTTagByte:
                return nbt.getByte(key) + "b";
            case NBTTagShort:
                return nbt.getShort(key) + "s";
            case NBTTagInt:
                return nbt.getInteger(key);
            case NBTTagLong:
                return nbt.getLong(key) + "L";
            case NBTTagFloat:
                return nbt.getFloat(key) + "f";
            case NBTTagDouble:
                return nbt.getDouble(key) + "d";
            case NBTTagByteArray:
                return "[B;" + nbt.getByteArray(key).length + " bytes]";
            case NBTTagIntArray:
                return "[I;" + nbt.getIntArray(key).length + " ints]";
            case NBTTagLongArray:
                return "[L;" + nbt.getLongArray(key).length + " longs]";
            case NBTTagCompound:
                return nbt.getCompound(key).toString();
            case NBTTagList:
                return nbt.getType(key) + " (size: " + nbt.getKeys().size() + ")";
            default:
                return nbt.toString();
        }
    }
}
