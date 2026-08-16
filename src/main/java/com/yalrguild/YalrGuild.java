package com.yalrguild;

import com.yalrguild.commands.Clancommands;
import com.yalrguild.database.DatabaseManager;
import com.yalrguild.listener.ClanChatListener;
import com.yalrguild.listener.InfoGUIListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class YalrGuild extends JavaPlugin {

    private static YalrGuild instance;

    private final Set<UUID> clanChatMode = new HashSet<>();
    private DatabaseManager databaseManager;



    private GuildManager guildManager;
    private GuildMember guildMember;
    private GuildUpgrade guildUpgrade;

    @Override
    public void onEnable() {
        instance = this;

        // Load database
        this.databaseManager = new DatabaseManager();
        databaseManager.initialize();

        // Create guild upgrade
        this.guildUpgrade = new GuildUpgrade(guildManager);

        // Create guild manager
        this.guildManager = new GuildManager(databaseManager, guildUpgrade);

        // Setup command
        Clancommands clanCommand = new Clancommands(guildManager, guildMember, clanChatMode);
        getCommand("clan").setExecutor(clanCommand);
        getCommand("clan").setTabCompleter(clanCommand);

        // Register listeners
        getServer().getPluginManager().registerEvents(new InfoGUIListener(guildManager), this);
        getServer().getPluginManager().registerEvents(new ClanChatListener(guildManager, clanChatMode), this);

        getLogger().info("YalrGuild plugin enabled!");
    }

    @Override
    public void onDisable() {
        if (databaseManager != null) {
            databaseManager.closeConnection();
        }
        getLogger().info("YalrGuild plugin disabled!");
    }

    public static YalrGuild getInstance() {
        return instance;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public GuildManager getGuildManager() {
        return guildManager;
    }
}
