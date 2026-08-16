package com.yalrguild;

import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

public class GuildUpgrade {

    private final GuildManager guildManager;

    public GuildUpgrade(GuildManager guildManager) {
        this.guildManager = guildManager;
    }

    public boolean addXp(Player player, int xp, boolean sendMessage) {
        Guild guild = guildManager.getPlayerClan(player.getUniqueId());
        if (guild == null) return false;

        guild.setXp(guild.getXp() + xp);

        if (sendMessage) {
            player.sendMessage(ChatColor.GREEN + "You earned " + xp + " XP!");
        }

        return checkUpgrade(player, guild);
    }

    private boolean checkUpgrade(Player player, Guild guild) {
        int xpNeeded = xpNeeded(guild.getLevel());

        if (guild.getXp() >= xpNeeded) {
            guild.setLevel(guild.getLevel() + 1);

            player.sendMessage(ChatColor.GREEN + "-----------------------------------------");
            player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD +
                    "Level UP! → New Level: " + guild.getLevel());
            player.sendMessage(ChatColor.BLUE + "XP: " + guild.getXp() + "/" + xpNeeded);
            player.sendMessage(ChatColor.GREEN + "-----------------------------------------");

            return true;
        }

        return false;
    }

    private static int calcXpNeeded(int level, int baseXP, double exponent) {
        return (int) (baseXP * Math.pow(level, exponent));
    }

    public int xpNeeded(int level) {
        return calcXpNeeded(level, 100, 1.5);
    }
}
