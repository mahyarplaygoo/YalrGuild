package com.yalrguild.database;

import com.yalrguild.Guild;
import com.yalrguild.GuildMember;
import com.yalrguild.GuildWar;

import java.sql.*;
import java.util.*;


public class DatabaseManager{
    private Connection connection;

    public void initialize(){
        try{
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:plugins/GuildsPlugin/Sqlite/clans.db");
            createTables();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    private void createTables() throws SQLException{
        String createClanTable = "CREATE TABLE IF NOT EXISTS clan (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL UNIQUE," +
                "owner TEXT NOT NULL," +
                "tag TEXT NOT NULL UNIQUE," +
                "level INTEGER DEFAULT 1," +
                "xp INTEGER DELAULT 0," +
                "createat DATETIME DEFAULT CURRENT_TIMESTAMP";
        String createClanMemberTable = "CREATE TABLE IF NOT EXISTS clan_member(" +
                "uuid TEXT NOT NULL," +
                "clan_id INTEGER NOT NULL," +
                "rank TEXT DEFAULT 'MEMBER'," +
                "joined_at DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "PRIMARY KEY (uuid, clan_id)," +
                "FOREIGN KEY (clan_id) REFERENCES clan(id) ON DELETE CASCADE";

        String createClanWarTable = "CREATE TABLE IF NOT EXISTS clan_war (" +
                "clanid1 INTEGER NOT NULL," +
                "clanid2 INTEGER NOT NULL," +
                "winner INTEGER," +
                "players TEXT," +
                "FOREIGN KEY (clanid1) REFERENCES clan(id)," +
                "FOREIGN KEY (clanid2) REFERENCES clan(id))";


        try(Statement stmt = connection.createStatement()){
            stmt.execute(createClanTable);
            stmt.execute(createClanMemberTable);
            stmt.execute(createClanWarTable);
        }
    }
}
