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

    private void createTables() throws SQLException {

        String createClanTable =
                "CREATE TABLE IF NOT EXISTS clan (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name TEXT NOT NULL UNIQUE," +
                        "owner TEXT NOT NULL," +
                        "tag TEXT NOT NULL UNIQUE," +
                        "level INTEGER DEFAULT 1," +
                        "xp INTEGER DEFAULT 0," +
                        "createat DATETIME DEFAULT CURRENT_TIMESTAMP" +
                        ")";

        String createClanMemberTable =
                "CREATE TABLE IF NOT EXISTS clan_member(" +
                        "uuid TEXT NOT NULL," +
                        "clan_id INTEGER NOT NULL," +
                        "rank TEXT DEFAULT 'MEMBER'," +
                        "joined_at DATETIME DEFAULT CURRENT_TIMESTAMP," +
                        "PRIMARY KEY (uuid, clan_id)," +
                        "FOREIGN KEY (clan_id) REFERENCES clan(id) ON DELETE CASCADE" +
                        ")";

        String createClanWarTable =
                "CREATE TABLE IF NOT EXISTS clan_war (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "clanid1 INTEGER NOT NULL," +
                        "clanid2 INTEGER NOT NULL," +
                        "winner INTEGER," +
                        "players TEXT," +
                        "FOREIGN KEY (clanid1) REFERENCES clan(id)," +
                        "FOREIGN KEY (clanid2) REFERENCES clan(id)" +
                        ")";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createClanTable);
            stmt.execute(createClanMemberTable);
            stmt.execute(createClanWarTable);
        }
    }

    public void createClan(Guild clan) throws SQLException {
        String sql = "INSERT INTO clan (name, owner, tag, level, xp, createat) VALUES (?, ?, ?, ?, ?, ?)";
        try(PreparedStatement pstmt = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){
            pstmt.setString(1, clan.getName());
            pstmt.setString(2, clan.getOwner().toString());
            pstmt.setString(3, clan.getTag());
            pstmt.setInt(4,clan.getLevel());
            pstmt.setInt(5, clan.getXp());
            pstmt.setTimestamp(6, new Timestamp(clan.getCreateAt().getTime()));
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if(rs.next()){
                clan.setId(rs.getInt(1));
            }
        }
    }

    public Guild getClanById(int id) throws SQLException{
        String sql = "SELECT * FROM clan WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)){
         pstmt.setInt(1, id);
         ResultSet rs = pstmt.executeQuery();
         if(rs.next()){
             return resultSetToClan(rs);
         }

        }
        return null;
    }
    public Guild getClanByName(String name) throws SQLException{
        String sql = "SELECT * FROM clan WHERE name = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                return resultSetToClan(rs);
            }
        }
        return null;
    }

    public List<Guild> getAllClans() throws SQLException{
        List<Guild> clans = new ArrayList<>();
        String sql = "SELECT * FROM clan ORDER BY level DESC, xp DESC";
        try(Statement stmt = connection.createStatement();){
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
                clans.add(resultSetToClan(rs));
            }
        }
        return clans;
    }

    public void deleteClan(int clanId) throws SQLException{
        String sql = "DELETE FROM clan WHERE id = ?;";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setInt(1, clanId);
            pstmt.executeUpdate();
        }

    }
    public void updateClan(Guild clan) throws SQLException{
        String sql = "UPDATE clan SET name = ?, owner = ?, tag = ?, level = ?, xp = ?, WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1, clan.getName());
            pstmt.setString(2, clan.getOwner().toString());
            pstmt.setString(3, clan.getTag());
            pstmt.setInt(4, clan.getLevel());
            pstmt.setInt(5, clan.getXp());
            pstmt.setInt(6, clan.getId());
            pstmt.executeUpdate();

        }
    }
    public Guild resultSetToClan(ResultSet rs) throws SQLException{
        Guild clan = new Guild();
        clan.setId(rs.getInt("id"));
        clan.setName(rs.getString("name"));
        clan.setOwner(UUID.fromString(rs.getString("owner")));
        clan.setTag(rs.getString("tag"));
        clan.setLevel(rs.getInt("level"));
        clan.setXp(rs.getInt("xp"));
        clan.setCreateAt(rs.getTimestamp("createat"));
        return clan;
    }

    public void addClanMember(GuildMember member) throws SQLException{
        String sql = "INSERT INTO clan_member (uuid, clan_id, rank, joined_at) VALUES(?, ?, ?, ?)";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1, member.getUUID().toString());
            pstmt.setInt(2, member.getClanid());
            pstmt.setString(3,member.getRank());
            pstmt.setTimestamp(4, new Timestamp(member.getJoinedAt().getTime()));
            pstmt.executeUpdate();
        }
    }
    public void removeClanMember(UUID uuid, int clanId) throws SQLException{
        String sql = "DELETE FROM clan_member WHERE uuid = ? AND clan_id = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1,uuid.toString());
            pstmt.setInt(2,clanId);
            pstmt.executeUpdate();
        }
    }
    public void updateMemberRank(UUID uuid, int clanId, String rank) throws SQLException{
        String sql = "UPDATE clan_member SET rank = ? WHERE uuid = ? AND clan_id = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1, rank);
            pstmt.setString(2, uuid.toString());
            pstmt.setInt(3, clanId);
            pstmt.executeUpdate();
        }

    }
    public GuildMember getClanMember(UUID uuid, int clanId) throws SQLException{
        String sql = "SELECT * FROM clan_member WHERE uuid= ? AND clan_id = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1,uuid.toString());
            pstmt.setInt(2, clanId);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                return resultSetToClanMember(rs);
            }
        }
        return null;
    }

    public List<GuildMember> getClanMembers(int clanId)throws SQLException{
        List<GuildMember> members = new ArrayList<>();
        String sql = "SELECT * FROM clan_member WHERE clan_id = ?";

        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setInt(1, clanId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                members.add(resultSetToClanMember(rs));
            }
        }
        return members;
    }
    public Guild getPlayerClan(UUID playerUuid) throws SQLException{
        String sql = "SELECT c.* FROM clan c JOIN clan_member cm ON c.id = cm.clan_id WHERE cm.uuid = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1, playerUuid.toString());
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                return resultSetToClan(rs);
            }
        }
        return null;
    }

    private GuildMember resultSetToClanMember(ResultSet rs) throws SQLException{
        GuildMember member = new GuildMember();
        member.setUUID(UUID.fromString(rs.getString("uuid")));
        member.setClanid(rs.getInt("clan_id"));
        member.setRank(rs.getString("rank"));
        member.setJoinedAt(rs.getTimestamp("joined_at"));
        return member;
    }
    public int createClanWar(int clanId1, int clanId2) {
        String sql = "INSERT INTO clan_war (clanid1, clanid2, players) VALUES (?, ?, ?)";
        int generatedWarId = -1;

        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, clanId1);
            pstmt.setInt(2, clanId2);
            pstmt.setString(3, "");

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {

                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedWarId = generatedKeys.getInt(1);
                    } else {
                        System.err.println("Creating clan war failed, no ID obtained.");
                    }
                }
            } else {
                System.err.println("Creating clan war failed, no rows affected.");
            }
        } catch (SQLException e) {
            System.err.println("Error creating clan war: " + e.getMessage());
            e.printStackTrace();
        }
        return generatedWarId;
    }
    public Map<String, Object> getClanWar(int warId) {
        String sql = "SELECT clanid1, clanid2, winner, players FROM clan_war WHERE id = ?";
        Map<String, Object> warDetails = null;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, warId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    warDetails = new HashMap<>();
                    warDetails.put("clanid1", rs.getInt("clanid1"));
                    warDetails.put("clanid2", rs.getInt("clanid2"));

                    int winnerId = rs.getInt("winner");
                    if (!rs.wasNull()) {
                        warDetails.put("winner", winnerId);
                    } else {
                        warDetails.put("winner", null);
                    }
                    // فعلاً players را به صورت رشته خام برمی‌گردانیم
                    String playersText = rs.getString("players");
                    warDetails.put("players", parsePlayersFromText(playersText));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving clan war with ID " + warId + ": " + e.getMessage());
            e.printStackTrace();
        }
        return warDetails;
    }

        public void closeConnection(){
            try{
                if(connection != null && !connection.isClosed()){
                    connection.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
}
