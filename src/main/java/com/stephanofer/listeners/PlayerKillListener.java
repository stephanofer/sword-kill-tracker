package com.stephanofer.listeners;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBTList;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerKillListener implements Listener {

    @EventHandler
    public void onPlayerKill(EntityDeathEvent event){
        boolean isPlayerKiller = event.getEntity().getKiller() != null && event.getEntity().getKiller() instanceof Player;
        boolean isPlayerVictim = event.getEntity() instanceof Player;

        if(!(isPlayerKiller) || !(isPlayerVictim)){
            return;
        }

        Player killer = event.getEntity().getKiller();
        Player victim = (Player) event.getEntity();
        ItemStack weapon = killer.getItemInHand();
        boolean isWeaponValid = NBT.get(weapon, nbt -> (boolean) nbt.hasTag("KillCount"));

        if (!isWeaponValid){
            return;
        }

        ItemStack trackedSword = updateSwordKillTracker(weapon, victim, killer);

        killer.setItemInHand(trackedSword);
    }

    private ItemStack updateSwordKillTracker(ItemStack sword, Player victim, Player killer) {

        NBT.modify(sword, nbt -> {
            int killCount = (int) nbt.getInteger("KillCount");
            killCount++;
            nbt.setInteger("KillCount", killCount);
            nbt.setString("LastPlayerKilled", victim.getName());
            ReadWriteNBTList<String> killDescriptions = nbt.getStringList("KillDescriptions");
            String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM"));
            String newKillDescription = "§8["+ date + "] §f"+ victim.getName() + " §ewas slain by §f" + killer.getName() + "§e.";
            if (killDescriptions.size() >= 3) {
                killDescriptions.remove(0);
            }
            killDescriptions.add(0, newKillDescription);
        });

        List<String> killDescriptionsList = NBT.get(sword, nbt -> {
            ReadWriteNBTList<String> nbtList = (ReadWriteNBTList<String>) nbt.getStringList("KillDescriptions");
            List<String> list = new ArrayList<>();
            for (String s : nbtList) {
                list.add(s);
            }
            Collections.reverse(list);
            return list;
        });


        ItemMeta meta = sword.getItemMeta();
        String messageEnchantment = "§7Kill tracker I";
        String messageKillCounter = "§6§lKills: §f" + NBT.get(sword, nbt -> (int) nbt.getInteger("KillCount"));

        List<String> newLore = new ArrayList<>();
        newLore.add(0,messageEnchantment);
        newLore.add(1,messageKillCounter);
        newLore.add(2, " ");
        newLore.add(3, "§7Latest Three Kills:");
        newLore.addAll(killDescriptionsList);
        meta.setLore(newLore);
        sword.setItemMeta(meta);
        return sword;
    }

}
