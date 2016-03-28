package com.college.football.trivia.Model;

import com.college.football.trivia.Util.Constants;
import com.college.football.trivia.Util.QuestionLoader;

import java.util.ArrayList;

public class Player {
    private static Player[] allPlayers;
    private static Player[] rookiePlayers;
    private static Player[] starterPlayers;
    private static Player[] veteranPlayers;
    private int id;
    private String first_name;
    private String last_name;
    private String team;
    private String college;
    private String position;
    private int jersey_num;
    private int college_tier;
    private int overall;
    public Player(int id, String first_name, String last_name, String team,
                  String college, String position, int jersey_num,
                  int college_tier, int overall) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.team = team;
        this.college = college;
        this.position = position;
        this.jersey_num = jersey_num;
        this.college_tier = college_tier;
        this.overall = overall;
    }

    public static Player[] getAllPlayers() {
        if (allPlayers == null) {
            QuestionLoader questionLoader = new QuestionLoader();
            ArrayList<Player> players = questionLoader.hardestQuestions();
            allPlayers = players.toArray(new Player[players.size()]);
        }
        return allPlayers;
    }

    public static Player[] getRookiePlayers() {
        if (rookiePlayers == null) {
            QuestionLoader questionLoader = new QuestionLoader();
            ArrayList<Player> players = questionLoader.easyQuestions();
            rookiePlayers = players.toArray(new Player[players.size()]);
        }
        return rookiePlayers;
    }

    public static Player[] getStarterPlayers() {
        if (starterPlayers == null) {
            QuestionLoader questionLoader = new QuestionLoader();
            ArrayList<Player> players = questionLoader.normalQuestions();
            starterPlayers = players.toArray(new Player[players.size()]);
        }
        return starterPlayers;
    }

    public static Player[] getVeteranPlayers() {
        if (veteranPlayers == null) {
            QuestionLoader questionLoader = new QuestionLoader();
            ArrayList<Player> players = questionLoader.hardQuestions();
            veteranPlayers = players.toArray(new Player[players.size()]);
        }
        return veteranPlayers;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getJersey_num() {
        return jersey_num;
    }

    public void setJersey_num(int jersey_num) {
        this.jersey_num = jersey_num;
    }

    public int getCollege_tier() {
        return college_tier;
    }

    public void setCollege_tier(int college_tier) {
        this.college_tier = college_tier;
    }

    public int getOverall() {
        return overall;
    }

    public void setOverall(int overall) {
        this.overall = overall;
    }



    @Override
    public String toString() {
        return "Player [id=" + id + ", first_name=" + first_name
                + ", last_name=" + last_name + ", team=" + team + ", college="
                + college + ", position=" + position + ", jersey_num="
                + jersey_num + ", college_tier="
                + college_tier + "]";
    }

    public static Player[] getPlayers(Game.Difficulty difficulty) {
        if (difficulty == Game.Difficulty.Rookie) {
            return Player.getRookiePlayers();
        } else if (difficulty == Game.Difficulty.Starter) {
            return Player.getStarterPlayers();
        } else if (difficulty == Game.Difficulty.Veteran) {
            return Player.getVeteranPlayers();
        } else {
            return Player.getAllPlayers();
        }
    }
}