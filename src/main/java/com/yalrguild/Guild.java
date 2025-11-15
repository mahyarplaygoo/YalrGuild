package com.yalrguild;


import java.util.UUID;
import java.util.Date;


public class Guild {
    private int id;
    private UUID owner;
    private String name;
    private String tag;
    private int xp;
    private int level;
    private Date createAt;
    public Guild(){}
    public Guild(String name, String tag, UUID owner){
        this.name = name;
        this.owner = owner;
        this.tag = tag;
        this.level = 1;
        this.xp = 0;
        this.createAt = new Date();
    }


    public int getId(){return id;}
    public void setId(int id){this.id = id; }

    public String getName() {return name;}
    public void setName(String name){this.name = name; }

    public UUID getOwner(){return owner;}
    public void setOwner(UUID owner) {this.owner = owner;}

    public String getTag(){return tag;}
    public void setTag(String tag){this.tag = tag;}

    public int getLevel(){return level;}
    public void setLevel(int level){this.level = level;}

    public int getXp(){return xp;}
    public void setXp(int xp){this.xp = xp;}

    public Date getCreateAt(){return createAt;}
    public void setCreateAt(Date createAt){this.createAt = createAt;}


}