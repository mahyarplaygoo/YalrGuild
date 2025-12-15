package com.yalrguild.GUI;
import org.bukkit.inventory.*;
import com.yalrguild.Guild;
import com.yalrguild.GuildManager;
import com.yalrguild.GuildMember;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.ArrayList;

public class ClanListGUI {
    private final GuildManager guildManager;

    private final Player player;
    private final Guild guild;

    public ClanListGUI(GuildManager guildManager, Player player) {
        this.guildManager = guildManager;
        this.player = player;
        this.guild = guildManager.getPlayerClan(player.getUniqueId());
    }

    public void open() {

        Inventory inv = Bukkit.createInventory(null, 54, "§6§l§ Clan List");
        fillBackground(inv);
        addClanInfoItem(inv);

        player.openInventory(inv);
    }


    private ItemStack createItem(Material material, String displayname, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayname);
        if (lore != null) meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }


    private void fillBackground(Inventory gui) {
        ItemStack background = createItem(Material.BLACK_STAINED_GLASS_PANE, " ", null);
        for (int i = 0; i < gui.getSize(); i++) {
            gui.setItem(i, background);
        }
    }


    private void addClanInfoItem(Inventory gui) {

        List<Guild> clans = guildManager.getAllClans();
        if (clans == null) return;

        int slot = 10;

        for (Guild clan : clans) {

            if (slot >= 44) break;

            ItemStack item = new ItemStack(Material.SHIELD);
            ItemMeta meta = item.getItemMeta();

            meta.setDisplayName("§a" + clan.getName() + " §7[" + clan.getTag() + "]");

            List<GuildMember> membersList = guildManager.getClanMembers(clan.getId());
            int members = membersList == null ? 0 : membersList.size();

            List<String> lore = new ArrayList<>();
            lore.add("§7Level: §e" + clan.getLevel());
            lore.add("§7Members: §e" + members);

            OfflinePlayer owner = Bukkit.getOfflinePlayer(clan.getOwner());
            lore.add("§7Owner: §f" + (owner.getName() != null ? owner.getName() : "Unknown"));

            meta.setLore(lore);
            item.setItemMeta(meta);

            gui.setItem(slot, item);
            slot++;


            if ((slot + 1) % 9 == 0) {
                slot += 2;
            }
        }
    }
}

