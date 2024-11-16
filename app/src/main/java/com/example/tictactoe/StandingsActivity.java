package com.example.tictactoe;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class StandingsActivity extends AppCompatActivity {

    private ArrayAdapter<String> adapter;
    private ListView standingsList;

    private String names = "PlayerNames";
    private SharedPreferences playerNames;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standings);

        playerNames = getSharedPreferences(names, 0);
        editor = playerNames.edit();

        standingsList = findViewById(R.id.standingsList);
    }

    protected void onStart() {

        super.onStart();

        //gets all the entries in the standingsPrefs SharedPreferences
        Map<String, ?> allEntries = playerNames.getAll();

        //initialize array to the size of SharedPreferences found
        ArrayList<ScoreBoard> playerNamesList = new ArrayList<>();

        int count = 0;
        for (Map.Entry<String, ?> entry : allEntries.entrySet())
        {
            ScoreBoard scoreEntry = new ScoreBoard(entry.getKey().toString(),Integer.parseInt(entry.getValue().toString()));
            playerNamesList.add(scoreEntry);
            count++;
        }

        Collections.sort(playerNamesList);

        ArrayList<String> tempStringArray = new ArrayList<>();
        for(int i=0;i<playerNamesList.size();i++)
        {
            tempStringArray.add(playerNamesList.get(i).names + " has " + playerNamesList.get(i).scores + " Win(s)");
        }

        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,tempStringArray);
        standingsList.setAdapter(adapter);


        Button clearData = findViewById(R.id.clearData);
        clearData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.clear();
            }
        });
    }
}

class ScoreBoard implements Comparable<ScoreBoard> {

    public String names;
    public int scores;

    public ScoreBoard(String name, int score) {

        names = name;
        scores = score;
    }

    private int getScores() {
        return scores;
    }

    @Override
    public int compareTo(ScoreBoard scoreBoard) {

        int compareScore = (scoreBoard).getScores();

        //highest scores on top, descending order
        return compareScore - this.scores;
    }
}


