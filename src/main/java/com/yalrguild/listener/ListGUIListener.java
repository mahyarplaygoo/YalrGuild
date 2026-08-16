package com.yalrguild.listener;



import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;


public class ListGUIListener implements Listener {


    @EventHandler
    public void onClickInfoInventory(InventoryClickEvent event) {

        if (!event.getView().getTitle().equals("§6§l§ Clan List")) return;
        event.setCancelled(true);

    }
}
