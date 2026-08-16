package com.yalrguild.GUI;


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

public class ClanInfoGUI {

    private final Player player;
    private final GuildManager guildManager;
    private final Guild guild;

    public ClanInfoGUI(GuildManager guildManager, Player player) {
        this.guildManager = guildManager;
        this.player = player;
        this.guild = guildManager.getPlayerClan(player.getUniqueId());
    }

    public void open() {
        if (guild == null) {
            player.sendMessage(ChatColor.RED + "You are not in a clan!");
            return;
        }

        Inventory inv = Bukkit.createInventory(null, 54, "§6§l§ Clan Info");

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

        ItemStack infoitem = createItem(Material.GOLDEN_APPLE,
                "§6§l" + guild.getName(),
                Arrays.asList(
                        "§7Tag : §e" + guild.getTag(),
                        "§7Level : §a" + guild.getLevel(),
                        "§7Xp : §b" + guild.getXp(),
                        "§7Created At : §f" + guild.getCreateAt(),
                        "§7Owner : §f" + getPlayerName(guild.getOwner())
                ));

        gui.setItem(4, infoitem);


        ItemStack members = createItem(Material.PLAYER_HEAD, "§b§lMembers",
                Arrays.asList("§7Left Click to Show Clan Members"));
        gui.setItem(21, members);


        ItemStack leave = createItem(Material.RED_BED, "§b§lLeave",
                Arrays.asList("" +
                        "§7Left Click to leave"
                ));
        gui.setItem(20, leave);
    }

    private String getPlayerName(UUID uuid) {
        String name = Bukkit.getOfflinePlayer(uuid).getName();
        return name != null ? name : "Unknown";
    }


    public void openMemberList() {

        Inventory inv = Bukkit.createInventory(null, 54, "§eMember List");

        for (GuildMember gm : guildManager.getClanMembers(guild.getId())) {
            OfflinePlayer op = Bukkit.getOfflinePlayer(gm.getUUID());

            ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) head.getItemMeta();

            ArrayList<String> loreList = new ArrayList<>();
            loreList.add("§7Rank : §e" + gm.getRank());
            loreList.add("§7Joined at : §e" + gm.getJoinedAt());
            loreList.add("");
            loreList.add("§cRight-click to remove this player");

            meta.setOwningPlayer(op);
            meta.setDisplayName("§e" + op.getName());
            meta.setLore(loreList);
            head.setItemMeta(meta);

            inv.addItem(head);
        }

        player.openInventory(inv);
    }
}
