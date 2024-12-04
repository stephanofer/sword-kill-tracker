package com.stephanofer.listeners;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
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

        if (weapon.getType() != Material.DIAMOND_SWORD){
            return;
        }

        ItemStack trackedSword = updateSwordKillTracker(weapon, victim);

        killer.setItemInHand(trackedSword);
    }

    private ItemStack updateSwordKillTracker(ItemStack sword, Player victim) {


        List<String> nbtKeys = NBT.get(sword, nbt -> (List<String>) new ArrayList<String>(nbt.getKeys()));
        for (String key : nbtKeys) {
            Bukkit.broadcastMessage("NBT Key: " + key + ", Value: " + NBT.get(sword, nbt -> (String) nbt.getString(key)));
        }

        NBT.modify(sword, nbt -> {
            int killCount = (int) nbt.getOrDefault("KillCount", 0);
            killCount++;
            nbt.setInteger("KillCount", killCount);
            nbt.setString("LastPlayerKilled", victim.getName());
        });

//        ItemStack trackedSword = nbtSword.getItem();
        ItemMeta meta = sword.getItemMeta();

        List<String> newLore = new ArrayList<>();

        newLore.add("§6Kills: " + NBT.get(sword, nbt -> (int) nbt.getInteger("KillCount")));
        newLore.add("§6Última Muerte: " + NBT.get(sword, nbt -> (String) nbt.getString("LastPlayerKilled")));

        meta.setLore(newLore);

        sword.setItemMeta(meta);

        return sword;
    }

}
