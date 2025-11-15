package com.yalrguild;

import java.util.Date;
import java.util.UUID;

public class GuildMember {
    private UUID uuid;
    private Date joinedAt;
    private String rank;
    private int clanid;

    public GuildMember(){}

    public GuildMember(UUID uuid, String rank, int clanid){
        this.rank = rank;
        this.clanid = clanid;
        this.uuid = uuid;
        this.joinedAt = new Date();
    }

    public  UUID getUUID(){return uuid;}
    public void setUUID(UUID uuid){this.uuid = uuid;}

    public String getRank(){return rank;}
    public void setRank(String rank){this.rank = rank;}

    public int getClanid(){return clanid;}
    public void setClanid(int clanid){this.clanid = clanid;}

    public Date getJoinedAt(){return joinedAt;}
    public void setJoinedAt(Date joinedAt){this.joinedAt = joinedAt;}
}
