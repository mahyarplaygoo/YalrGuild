package com.yalrguild.listener;

import com.yalrguild.GuildManager;
import com.yalrguild.GuildMember;
import com.yalrguild.GUI.ClanInfoGUI;
import com.yalrguild.Guild;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class InfoGUIListener implements Listener {

    private final GuildManager guildManager;

    public InfoGUIListener(GuildManager guildManager) {
        this.guildManager = guildManager;
    }

    @EventHandler
    public void onClickInfoInventory(InventoryClickEvent event) {

        if (!event.getView().getTitle().equals("§6§l§ Clan Info")) return;
        event.setCancelled(true);

        ItemStack item = event.getCurrentItem();
        if (item == null || item.getType() == Material.AIR) return;

        Player player = (Player) event.getWhoClicked();

        switch (item.getType()) {
            case PLAYER_HEAD -> {

                Guild guild = guildManager.getPlayerClan(player.getUniqueId());
                if (guild == null) return;
                new ClanInfoGUI(guildManager, player).openMemberList();
            }
            case RED_BED -> {
                Guild guild = guildManager.getPlayerClan(player.getUniqueId());
                if (guild == null) {
                    player.sendMessage("§cYou are not in a guild!");
                    return;
                }

                boolean success = guildManager.leaveClan(player);
                if (success) {
                    player.sendMessage("§aYou have left the guild: " + guild.getName());
                    player.closeInventory();
                } else {
                    player.sendMessage("§cYou cannot leave the guild as the owner!");
                }
            }
        }

    }

    @EventHandler
    public void onClickMemberList(InventoryClickEvent event) {

        if (!event.getView().getTitle().equals("§eMember List")) return;
        event.setCancelled(true);

        ItemStack item = event.getCurrentItem();
        if (item == null || item.getType() == Material.AIR) return;

        Player player = (Player) event.getWhoClicked();
        Guild guild = guildManager.getPlayerClan(player.getUniqueId());
        if (guild == null) return;


        if (item.getType() == Material.PLAYER_HEAD &&
                item.hasItemMeta() &&
                item.getItemMeta() instanceof SkullMeta skullMeta) {

            OfflinePlayer clickedPlayer = skullMeta.getOwningPlayer();
            if (clickedPlayer == null) return;

            GuildMember clickedMember = guildManager
                    .getClanMembers(guild.getId())
                    .stream()
                    .filter(m -> m.getUUID().equals(clickedPlayer.getUniqueId()))
                    .findFirst()
                    .orElse(null);

            if (clickedMember == null) return;

            switch (event.getClick()) {

                case RIGHT -> {


                    if (clickedMember.getUUID().equals(player.getUniqueId())) {
                        player.sendMessage("§cYou cannot kick yourself!");
                        return;
                    }


                    guildManager.kickPlayer(player, clickedMember.getUUID().toString());

                    player.sendMessage("§a" + clickedPlayer.getName() + " has been kicked from the guild!");

                    Player target = Bukkit.getPlayer(clickedMember.getUUID());
                    if (target != null) {
                        target.sendMessage("§cYou have been kicked from the guild by " + player.getName());
                    }


                    new ClanInfoGUI(guildManager, player).openMemberList();
                }

                case LEFT -> {
                    player.sendMessage("§eYou clicked on " + clickedPlayer.getName());
                }
            }
        }
    }
}
