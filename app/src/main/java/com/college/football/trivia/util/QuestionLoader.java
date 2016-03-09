package com.college.football.trivia.Util;

import android.content.Context;

import com.college.football.trivia.Model.College;
import com.college.football.trivia.Model.Player;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class QuestionLoader {
    private static Context context;

    private static ArrayList<Player> allPlayers;

    public static void loadData(Context context) {
        QuestionLoader.context = context;
        loadPlayersJsonData();
        loadCollegesJsonData();
    }

    private static void loadPlayersJsonData() {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(context.getAssets().open(
                    Constants.PLAYERS_JSON_FILE)));
            String temp;
            while ((temp = br.readLine()) != null)
                sb.append(temp);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        parsePlayersJsonData(sb.toString());
    }

    private static void loadCollegesJsonData() {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(context.getAssets().open(
                    Constants.COLLEGE_JSON_FILE)));
            String temp;
            while ((temp = br.readLine()) != null)
                sb.append(temp);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        parseCollegesJsonData(sb.toString());
    }

    public static void parsePlayersJsonData(String json) {
        ArrayList<Player> allPlayers = new ArrayList<>();
        try {
            JSONObject jsonObjMain = new JSONObject(json);
            JSONArray jsonArray = jsonObjMain.getJSONArray("players");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);

                Player p = new Player(i, jsonObj.getString("firstName"), jsonObj.getString("lastName"), jsonObj.getString("proTeam"), jsonObj.getString("college"), jsonObj.getString("position"), jsonObj.getInt("jerseyNumber"), jsonObj.getInt("tier"), jsonObj.getInt("overall"));
                allPlayers.add(p);
            }
            QuestionLoader.allPlayers = allPlayers;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void parseCollegesJsonData(String json) {
        ArrayList<College> temp1 = new ArrayList<>();
        ArrayList<College> temp2 = new ArrayList<>();
        ArrayList<College> temp3 = new ArrayList<>();
        try {
            JSONObject jsonObjMain = new JSONObject(json);
            JSONArray jsonArray = jsonObjMain.getJSONArray("colleges");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                College c = new College(i, jsonObj.getString("college"), jsonObj.getInt("tier"));

                switch (c.getTier()) {
                    case 1:
                        temp1.add(c);
                        break;
                    case 2:
                        temp2.add(c);
                        break;
                    case 3:
                        temp3.add(c);
                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        GameController.setTier1(temp1);
        GameController.setTier2(temp2);
        GameController.setTier3(temp3);
    }

    public ArrayList<Player> easyQuestions() {
        ArrayList<Player> tempArr = new ArrayList<>();
        for(Player p:allPlayers){
            if(p.getOverall() >= 80 && (p.getPosition().equals("QB") || p.getPosition().equals("HR") || p.getPosition().equals("WR"))){
                tempArr.add(p);
            }
        }
        return tempArr;
    }

    public ArrayList<Player> normalQuestions() {
        ArrayList<Player> tempArr = new ArrayList<>();
        for(Player p:allPlayers){
            if( p.getPosition().equals("QB") || p.getPosition().equals("HB") || p.getPosition().equals("WR")){
                tempArr.add(p);
            }
        }
        return tempArr;
    }

    public ArrayList<Player> hardQuestions() {
        ArrayList<Player> tempArr = new ArrayList<>();
        for(Player p:allPlayers){
            if(p.getOverall() >= 76){
                tempArr.add(p);
            }
        }
        return tempArr;
    }

    public ArrayList<Player> hardestQuestions() {
        return allPlayers;
    }
}
