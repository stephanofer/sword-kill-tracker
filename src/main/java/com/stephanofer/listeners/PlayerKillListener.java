package com.stephanofer.listeners;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBTList;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import think.rpgitems.api.RPGItems;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PlayerKillListener implements Listener {

    @EventHandler
    public void onPlayerKill(EntityDeathEvent event) {
        if (event.getEntity().getKiller() == null || !(event.getEntity() instanceof Player)) {
            return;
        }

        Player killer = event.getEntity().getKiller();
        Player victim = (Player) event.getEntity();
        ItemStack weapon = killer.getItemInHand();

        if (weapon == null || weapon.getType() == Material.AIR) {
            return;
        }

        if (RPGItems.toRPGItem(weapon) != null) {
            return;
        }

        if (!isKillTrackerWeapon(weapon.getType())) {
            return;
        }

        updateWeaponKillTracker(weapon, victim, killer);
    }

    /**
     * Verifica si el tipo de material es una espada, hacha o arco
     */
    private boolean isKillTrackerWeapon(Material type) {
        String name = type.name();
        return name.endsWith("_SWORD") || name.endsWith("_AXE") || type == Material.BOW;
    }

    private void updateWeaponKillTracker(ItemStack weapon, Player victim, Player killer) {
        NBT.modify(weapon, nbt -> {
            // Si no tiene KillCount, es la primera kill - inicializar
            int currentKills = nbt.hasTag("KillCount") ? nbt.getInteger("KillCount") : 0;
            int newKillCount = currentKills + 1;
            
            nbt.setInteger("KillCount", newKillCount);
            nbt.setString("LastPlayerKilled", victim.getName());

            ReadWriteNBTList<String> killDescriptions = nbt.getStringList("KillDescriptions");
            String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM"));
            String newKillDescription = "§8[" + date + "] §f" + victim.getName() + " §ewas slain by §f" + killer.getName() + "§e.";

            // Mantener solo las últimas 3 kills
            if (killDescriptions.size() >= 3) {
                killDescriptions.remove(killDescriptions.size() - 1);
            }
            killDescriptions.add(0, newKillDescription);

            // Actualizar el lore del item
            nbt.modifyMeta((readOnlyNbt, meta) -> {
                List<String> killDescriptionsList = new ArrayList<>();
                for (String s : readOnlyNbt.getStringList("KillDescriptions")) {
                    killDescriptionsList.add(s);
                }

                String messageKillCounter = "§6§lKills: §f" + newKillCount;

                List<String> newLore = new ArrayList<>();
                newLore.add(messageKillCounter);
                newLore.add(" ");
                newLore.add("§7Latest Three Kills:");
                newLore.addAll(killDescriptionsList);
                meta.setLore(newLore);
            });
        });
    }

}
