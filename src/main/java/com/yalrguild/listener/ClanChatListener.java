package com.yalrguild.listener;

import com.yalrguild.Guild;
import com.yalrguild.GuildMember;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import com.yalrguild.GuildManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.lang.foreign.PaddingLayout;
import java.util.Set;
import java.util.UUID;

public class ClanChatListener implements Listener {
    private GuildManager guildManager;
    private final Set<UUID> clanChatMode;

    public ClanChatListener(GuildManager guildManager, Set<UUID> clanChatMode) {
        this.guildManager = guildManager;
        this.clanChatMode = clanChatMode;


    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();

        Guild clan = guildManager.getPlayerClan(player.getUniqueId());
        if(clan != null){
            String format = ChatColor.GRAY + "" + player.getName() + " ["+clan.getTag() +"]" +": " + ChatColor.WHITE + "%2$s";
            event.setFormat(format);
        }else{
            String format = ChatColor.GRAY + "" + player.getName() + ": " + ChatColor.WHITE + "%2$s";
            event.setFormat(format);
        }
        if(!clanChatMode.contains(player.getUniqueId()))return;
        event.setCancelled(true);
        // clan
        Guild guild = guildManager.getPlayerClan(player.getUniqueId());
        if(guild == null){
            player.sendMessage(ChatColor.RED + "You are not in a clan!");
            clanChatMode.remove(player.getUniqueId());
            return;
        }
        String message = ChatColor.YELLOW + "[Clan] -> " + player.getName() + ChatColor.RED + " ["+ guild.getTag() +"]" + ChatColor.WHITE +": " + event.getMessage();
        for (GuildMember gm : guildManager.getClanMembers(guild.getId())){
            Player p = Bukkit.getPlayer(gm.getUUID());
            if (p != null) p.sendMessage(message);
        }




    }
}
