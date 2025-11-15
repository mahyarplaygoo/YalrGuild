package com.yalrguild.commands;

import org.bukkit.command.*;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Clancommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be executed by players!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            help(player);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "create":
                break;
            case "delete":
                break;
            case "invite":
                break;
            case "kick":
                break;
            case "promote":
                break;
            case "demote":
                break;
            case "info":
                break;
            case "list":
                break;
            case "leave":
                break;
            case "war":
                break;
            case "upgrade":
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
}
