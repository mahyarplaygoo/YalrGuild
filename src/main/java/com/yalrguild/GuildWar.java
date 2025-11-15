package com.yalrguild;

import java.util.List;
import java.util.UUID;

public class GuildWar {
    private int clanid1;
    private int clanid2;
    private Integer winner;
    private List<UUID> players;

    public GuildWar(){}
    public GuildWar(int clanid1, int clanid2, Integer winner){
        this.clanid1 = clanid1;
        this.clanid2 = clanid2;
        this.winner  = null;
    }

    public int getClanid1(){return clanid1;}
    public void setClanid1(int clanid1){this.clanid1 = clanid1;}


    public int getClanid2(){return clanid2;}
    public void setClanid2(int clanid2){this.clanid2 = clanid2;}

    public Integer getWinner(){return winner;}
    public void setWinner(Integer winner){this.winner = winner;}

    public List<UUID> getPlayers(){return players;}
    public void setPlayers(List<UUID> players){this.players = players;}
}
