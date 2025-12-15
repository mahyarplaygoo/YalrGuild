package com.yalrguild.commands;

import com.yalrguild.GUI.ClanInfoGUI;
import com.yalrguild.GUI.ClanListGUI;
import com.yalrguild.Guild;
import com.yalrguild.GuildManager;

import com.yalrguild.GuildMember;
import com.yalrguild.GuildWar;
import org.bukkit.command.*;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Clancommands implements CommandExecutor, TabCompleter {
    private final GuildManager guildManager;
    private GuildMember guildMember;
    private final Set<UUID> clanChatMode;
    private GuildWar guildWar;

    public Clancommands(GuildManager guildManager, GuildMember guildMember, Set<UUID> clanChatMode){
        this.guildManager = guildManager;
        this.guildMember = guildMember;
        this.clanChatMode = clanChatMode;

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be executed by players!");
            return true;
        }

        Player player = (Player) sender;
        Guild playerClan = guildManager.getPlayerClan(player.getUniqueId());
        if (args.length == 0) {
            help(player);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "create":
                if(args.length >= 3){
                    String name = args[1];
                    String tag = args[2];
                    if(guildManager.createClan(player, name, tag)){
                        player.sendMessage(ChatColor.GREEN + "Clan created successfully!");

                    }else{
                        player.sendMessage(ChatColor.RED + "Failed to create clan. you may already be in a clan or the name/tag is taken.");
                    }
                }else{
                    player.sendMessage(ChatColor.RED + "Usage: /clan create <name> [tag]");
                }
                break;
            case "delete":

                if(playerClan != null){
                    if(guildManager.deleteClan(player, playerClan.getId())){
                        player.sendMessage(ChatColor.GREEN + "Clan deleted successfully!");

                    }else{
                        player.sendMessage(ChatColor.RED + "You must be the owner to delete the clan!");

                    }
                }else{
                    player.sendMessage(ChatColor.RED + "You are not in a clan!");

                }
                break;
            case "invite":
                if(args.length >= 2){
                    if(guildManager.invitePlayer(player, args[1])){
                        player.sendMessage(ChatColor.GREEN + "player invited successfully!");

                    }else{
                        player.sendMessage(ChatColor.RED + "Failed to invite player. Check your permissions or if the player exists");

                    }
                }else{
                    player.sendMessage(ChatColor.RED + "Usage: /clan invite <player>");
                }
                break;
            case "kick":
                if(args.length == 2){
                    if(guildManager.kickPlayer(player, args[1])){
                        player.sendMessage(ChatColor.RED + "Player " + ChatColor.GOLD + args[1] + "kicked successfully");
                    }else{
                        player.sendMessage(ChatColor.RED + "Failed to kick player. Check your permissions.");
                    }
                }else{
                    player.sendMessage(ChatColor.RED + "Usage: /clan kick <player>");
                }
                break;
            case "promote":
                if(args.length == 2){
                    if(guildManager.promote(player, args[1])){
                        player.sendMessage(ChatColor.GREEN + "Player " + ChatColor.GOLD + args[1] + "promoted successfully");

                    }else{
                        player.sendMessage(ChatColor.RED + "Failed to promote player.");

                    }
                }else{
                    player.sendMessage(ChatColor.RED + "Usage: /clan promote <player>");
                }
                break;
            case "demote":
                if(args.length == 2){
                    if(guildManager.demote(player, args[1])){
                        player.sendMessage(ChatColor.GREEN + "Player " + ChatColor.GOLD + args[1] + "demoted successfully");

                    }else{
                        player.sendMessage(ChatColor.RED + "Failed to demote player.");

                    }
                }else{
                    player.sendMessage(ChatColor.RED + "Usage: /clan demote <player>");
                }
                break;
            case "info":
                Guild playerGuild = guildManager.getPlayerClan(player.getUniqueId());
                if(playerClan != null){

                    new ClanInfoGUI(guildManager, player).open();
                }else{
                    player.sendMessage(ChatColor.RED + "You are not in a clan!");
                }
                break;
            case "chat":
                UUID uuid = player.getUniqueId();
                if (clanChatMode.contains(uuid)) {
                    clanChatMode.remove(uuid);
                    player.sendMessage(ChatColor.RED + "Clan chat disabled.");
                } else {
                    clanChatMode.add(uuid);
                    player.sendMessage(ChatColor.GREEN + "Clan chat enabled. All messages will go to your guild.");
                }
                return true;

            case "list":
                new ClanListGUI(guildManager, player).open();
                break;
            case "leave":
                player.sendMessage(ChatColor.RED + "This Commands not active");
                break;
            case "war":
                player.sendMessage(ChatColor.RED + "This Commands not active");
                break;
            case "upgrade":
                player.sendMessage(ChatColor.RED + "This Commands not active");
                break;
            case "help":
                help(player);
                break;
            default:
                player.sendMessage(ChatColor.RED + "Unknown command! Use /clan for help.");
                break;
        }

        return true;
    }

    private void help(Player player) {
        player.sendMessage(ChatColor.GOLD + "=== Clan Commands ===");
        player.sendMessage(ChatColor.YELLOW + "/clan create <name> <tag> - Create a new clan");
        player.sendMessage(ChatColor.YELLOW + "/clan delete - Delete your clan");
        player.sendMessage(ChatColor.YELLOW + "/clan invite <player> - Invite a player");
        player.sendMessage(ChatColor.YELLOW + "/clan kick <player> - Kick a player");
        player.sendMessage(ChatColor.YELLOW + "/clan promote <player> - Promote a player");
        player.sendMessage(ChatColor.YELLOW + "/clan demote <player> - Demote a player");
        player.sendMessage(ChatColor.YELLOW + "/clan info - Show clan info");
        player.sendMessage(ChatColor.YELLOW + "/clan list - List all clans");
        player.sendMessage(ChatColor.YELLOW + "/clan leave - Leave your clan");
        player.sendMessage(ChatColor.YELLOW + "/clan war <clan> - Declare war on a clan");
        player.sendMessage(ChatColor.YELLOW + "/clan upgrade - Upgrade your clan");
    }
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("create");
            completions.add("delete");
            completions.add("invite");
            completions.add("kick");
            completions.add("promote");
            completions.add("demote");
            completions.add("info");
            completions.add("chat");
            completions.add("list");
            completions.add("leave");
            completions.add("war");
            completions.add("upgrade");


        }


        String lastWord = args[args.length - 1].toLowerCase();
        completions.removeIf(s -> !s.toLowerCase().startsWith(lastWord));

        return completions;
    }
}
