package com.yalrguild;

import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

public class GuildUpgrade {

    private final Player player;
    private final GuildManager guildManager;
    private Guild guild;

    public GuildUpgrade(Guild guild, Player player, GuildManager guildManager) {
        this.player = player;
        this.guildManager = guildManager;
        this.guild = guildManager.getPlayerClan(player.getUniqueId());
    }


    public boolean checkUpgrade() {

        int xpNeeded = xpNeeded();

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


    public void addXp(int xp, boolean isSendMessage) {
        guild.setXp(guild.getXp() + xp);

        if (isSendMessage) {
            player.sendMessage(ChatColor.GREEN + "You earned " + xp + " XP!");
        }

        checkUpgrade();
    }


    private static int calcXpNeeded(int level, int baseXP, double exponent) {
        return (int) (baseXP * Math.pow(level, exponent));
    }


    public int xpNeeded() {
        return calcXpNeeded(guild.getLevel(), 100, 1.5);

    }
}
