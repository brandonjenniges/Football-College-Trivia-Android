package com.college.football.trivia;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.college.football.trivia.game.PracticeActivity;
import com.college.football.trivia.game.StandardActivity;
import com.college.football.trivia.game.SurvivalActivity;
import com.college.football.trivia.model.College;
import com.college.football.trivia.model.Player;
import com.college.football.trivia.util.Constants;
import com.college.football.trivia.util.GameController;
import com.google.android.gms.games.Games;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class TitleScreenActivity extends BaseActivity {

    private ArrayList<Player> allPlayers = new ArrayList<>();
    private GameController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_screen);

        controller = GameController.getInstance();

        loadPlayersJsonData();
        loadCollegesJsonData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_title_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_rate:
                this.startActivity(new Intent(Intent.ACTION_VIEW, Uri
                        .parse(Constants.app_url)));
                return  true;
            case R.id.action_leaderboard:
                if (BuildConfig.DEBUG || mGoogleApiClient.isConnected()) {
                    startActivity(new Intent(getApplicationContext(),
                            LeaderboardActivity.class));
                } else {
                    mGoogleApiClient.connect();
                }
                return true;
            case R.id.action_achievements:
                if (BuildConfig.DEBUG) {
                    return true;
                }

                if (mGoogleApiClient.isConnected()) {
                    startActivityForResult(Games.Achievements.getAchievementsIntent(mGoogleApiClient),
                            5);
                } else {
                    mGoogleApiClient.connect();
                }
                return true;
            /*
            case R.id.action_about:
                Toast.makeText(getApplicationContext(), "About", Toast.LENGTH_SHORT).show();
                return true;
                */
        }

        return super.onOptionsItemSelected(item);
    }

    public void startGame(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.standardGame:
                controller.setCurrent_mode(Constants.standard_game_int);
                launchDifficultyDialog();
                break;
            case R.id.survivalGame:
                controller.setCurrent_mode(Constants.survival_game_int);
                launchDifficultyDialog();
                break;
            case R.id.practiceGame:
                controller.setCurrent_mode(Constants.practice_game_int);
                launchDifficultyDialog();
                break;
        }
    }

    public void loadPlayersJsonData() {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(getAssets().open(
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

    public void loadCollegesJsonData() {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(getAssets().open(
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

    public void parsePlayersJsonData(String json) {
        try {
            JSONObject jsonObjMain = new JSONObject(json);
            JSONArray jsonArray = jsonObjMain.getJSONArray("players");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);

                Player p = new Player(i, jsonObj.getString("firstName"), jsonObj.getString("lastName"), jsonObj.getString("proTeam"), jsonObj.getString("college"), jsonObj.getString("position"), jsonObj.getInt("jerseyNumber"), jsonObj.getInt("tier"), jsonObj.getInt("overall"));
                allPlayers.add(p);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void parseCollegesJsonData(String json) {
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

    public void launchDifficultyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Difficulty");
        builder.setItems(Constants.diffs,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        controller.setCurrent_diff(which + 1);

                        switch (which + 1) {
                            case Constants.easiest_game_int:
                                loadEasyQuestions();
                                break;
                            case Constants.normal_game_int:
                                loadNormalQuestions();
                                break;
                            case Constants.hard_game_int:
                                loadHardQuestions();
                                break;
                            case Constants.hardest_game_int:
                                loadHardestQuestions();
                                break;
                        }
                        Intent intent;

                        switch (controller.getCurrent_mode()) {
                            case Constants.standard_game_int:
                                intent = new Intent(getApplicationContext(),
                                        StandardActivity.class);
                                break;
                            case Constants.survival_game_int:
                                intent = new Intent(getApplicationContext(),
                                        SurvivalActivity.class);
                                break;
                            case Constants.practice_game_int:
                            default:
                                intent = new Intent(getApplicationContext(),
                                        PracticeActivity.class);
                                break;
                        }

                        startActivity(intent);
                    }
                }
        );
        builder.show();
    }

    public void loadEasyQuestions() {
        ArrayList<Player> tempArr = new ArrayList<>();
        for(Player p:allPlayers){
            if(p.getOverall() >= 80 && (p.getPosition().equals("QB") || p.getPosition().equals("HR") || p.getPosition().equals("WR"))){
                tempArr.add(p);
            }
        }
        Log.d("COUNT", "EASY: " + tempArr.size());
        GameController.setGamePlayers(tempArr);
    }

    public void loadNormalQuestions() {
        ArrayList<Player> tempArr = new ArrayList<>();
        for(Player p:allPlayers){
            if( p.getPosition().equals("QB") || p.getPosition().equals("HB") || p.getPosition().equals("WR")){
                tempArr.add(p);
            }
        }
        Log.d("COUNT", "STARTER: " + tempArr.size());
        GameController.setGamePlayers(tempArr);

    }

    public void loadHardQuestions() {
        ArrayList<Player> tempArr = new ArrayList<>();
        for(Player p:allPlayers){
            if(p.getOverall() >= 76){
                tempArr.add(p);
            }
        }
        Log.d("COUNT", "VETERAN: " + tempArr.size());
        GameController.setGamePlayers(tempArr);
    }

    public void loadHardestQuestions() {
        Log.d("COUNT", "ALL-PRO: " + allPlayers.size());
        GameController.setGamePlayers(allPlayers);
    }

}
