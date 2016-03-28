package com.college.football.trivia.Model;

public class College {
    private int id;
    private String name;
    private int tier;

    public College(int id, String name, int tier) {
        this.id = id;
        this.name = name;
        this.tier = tier;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTier() {
        return tier;
    }

    public void setTier(int tier) {
        this.tier = tier;
    }

    @Override
    public String toString() {
        return "College [id=" + id + ", name=" + name + ", tier=" + tier + "]";
    }

}
