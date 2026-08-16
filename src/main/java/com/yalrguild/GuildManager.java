package com.yalrguild;

import com.yalrguild.database.DatabaseManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import org.bukkit.ChatColor;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.md_5.bungee.api.chat.*;


public class GuildManager {
    private DatabaseManager databaseManager;
    private GuildUpgrade guildUpgrade;
    public Map<UUID, Integer> pendingInvites = new HashMap<>();

    public GuildManager(DatabaseManager databaseManager, GuildUpgrade guildUpgrade){

        this.databaseManager = databaseManager;
        this.guildUpgrade = guildUpgrade;
    }

    public boolean createClan(Player owner, String name, String tag){
        try{
            if(getPlayerClan(owner.getUniqueId()) != null) {
                return false;
            }

            if(databaseManager.getClanByName(name) != null){
                return false;
            }
            Guild clan = new Guild(name, tag, owner.getUniqueId());
            databaseManager.createClan(clan);

            GuildMember member = new GuildMember(owner.getUniqueId(), "OWNER" ,clan.getId());
            databaseManager.addClanMember(member);

            return true;



        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }


    public boolean deleteClan(Player player, int clanId){
        try{
            Guild clan = databaseManager.getClanById(clanId);
            if (clan == null || !clan.getOwner().equals(player.getUniqueId())){
                return false;
            }
            databaseManager.deleteClan(clanId);
            return true;

        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    public boolean invitePlayer(Player inviter, String targetPlayerName) {
        try {
            Guild clan = getPlayerClan(inviter.getUniqueId());
            if (clan == null) return false;
            GuildMember inviterMember = databaseManager.getClanMember(inviter.getUniqueId(), clan.getId());
            if (!inviterMember.getRank().equals("OWNER") && !inviterMember.getRank().equals("OFFICER")) return false;
            Player target = Bukkit.getPlayer(targetPlayerName);
            if(target == null || getPlayerClan(target.getUniqueId()) != null) return false;


            // pending
            pendingInvites.put(target.getUniqueId(), clan.getId());

            TextComponent accept = new TextComponent("Accept Invite");
            accept.setColor(ChatColor.YELLOW);
            accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/guild accept"));
            target.spigot().sendMessage(new TextComponent(ChatColor.YELLOW + "Your Invited to " + clan.getName() + " Clan"), accept);
            return true;


        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean acceptInvite(Player player){
        try{
            if (!pendingInvites.containsKey(player.getUniqueId())){
                player.sendMessage(ChatColor.RED + "You don't have any Invite");
                return false;
            }
            int clanId = pendingInvites.get(player.getUniqueId());

            if (getPlayerClan(player.getUniqueId()) != null){
                player.sendMessage(ChatColor.RED + "You are in Clan");
                return false;
            }

            GuildMember newMember = new GuildMember(player.getUniqueId(), "MEMBER", clanId)
            databaseManager.addClanMember(newMember);

            pendingInvites.remove(player.getUniqueId());
            player.sendMessage(ChatColor.GREEN + "You have successfully joined the clan");

            return true;
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    public boolean kickPlayer(Player kicker, String targetPlayerName){
        try{
            Guild clan = getPlayerClan(kicker.getUniqueId());
            if(clan == null){
                return false;
            }
            GuildMember kickerMember = databaseManager.getClanMember(kicker.getUniqueId(), clan.getId());
            Player target = Bukkit.getPlayer(targetPlayerName);

            if (target == null){
                return false;
            }
            GuildMember targetMember = databaseManager.getClanMember(target.getUniqueId(), clan.getId());
            if (targetMember == null){
                return false;
            }

            // Check permissions
            if(kickerMember.getRank().equals("MEMBER") ||
                    (kickerMember.getRank().equals("OFFICER") && targetMember.getRank().equals("OWNER"))){
                return false;
            }
            databaseManager.removeClanMember(target.getUniqueId(), clan.getId());
            return true;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    private boolean changeRank(Player changer, String targetPlayerName, boolean promote){
        try{
            Guild clan = getPlayerClan(changer.getUniqueId());
            if(clan == null){
                return false;
            }
            GuildMember changerMember = databaseManager.getClanMember(changer.getUniqueId(), clan.getId());
            if(!changerMember.getRank().equals("OWNER")){
                return false;
            }
            Player target = Bukkit.getPlayer(targetPlayerName);
            if (target == null){
                return false;
            }
            GuildMember targetMember = databaseManager.getClanMember(target.getUniqueId(), clan.getId());
            if(targetMember == null){
                return false;
            }
            String newRank;

            if(promote){
                switch (targetMember.getRank()){
                    case "MEMBER": newRank = "OFFICER"; break;
                    case "OFFICER": newRank = "OWNER"; break;
                    default: return false;
                }
            }else{
                switch (targetMember.getRank()){
                    case "OWNER": newRank = "OFFICER"; break;
                    case "OFFICER": newRank = "MEMBER"; break;
                    default: return false;
                }
            }
            databaseManager.updateMemberRank(target.getUniqueId(), clan.getId(), newRank);
            return true;

        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    public boolean promote(Player promoter, String targetPlayerName){
        return changeRank(promoter, targetPlayerName, true);

    }
    public boolean demote(Player demoter, String targetPlayerName){
        return changeRank(demoter, targetPlayerName, false);
    }
    public Guild getPlayerClan(UUID playerUUID){
        try{
            return databaseManager.getPlayerClan(playerUUID);

        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public  List<Guild> getAllClans(){
        try{
            return databaseManager.getAllClans();
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }
    public List<GuildMember> getClanMembers(int clanId){
        try{
            return databaseManager.getClanMembers(clanId);

        }catch (SQLException e){
            return null;
        }

    }
    public boolean leaveClan(Player player){
        try{
            Guild clan = getPlayerClan(player.getUniqueId());
            if(clan == null){
                return false;
            }
            GuildMember member = databaseManager.getClanMember(player.getUniqueId(), clan.getId());
            if (member.getRank().equals("OWNER")){
                // Owner can't leave
                return false;
            }
            databaseManager.removeClanMember(player.getUniqueId(), clan.getId());
            return true;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    public boolean upgradeClan(Player player){
        try{
            Guild clan = getPlayerClan(player.getUniqueId());
            if(clan == null){
                return false;
            }
            GuildMember member = databaseManager.getClanMember(player.getUniqueId(), clan.getId());
            if(!member.getRank().equals("OWNER")){
                return false;
            }
            // example
            clan.setLevel(clan.getLevel() + 1);
            databaseManager.updateClan(clan);
            return true;


        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }


}
