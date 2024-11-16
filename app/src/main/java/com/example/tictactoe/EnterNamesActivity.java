package com.example.tictactoe;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class EnterNamesActivity extends AppCompatActivity {

    Button saveName;
    EditText playerNameOne, playerNameTwo;

    private  String names = "PlayerNames";
    private SharedPreferences playerNames;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_names);

        saveName= findViewById(R.id.saveNames);
        playerNameOne = findViewById(R.id.playerOneName);
        playerNameTwo = findViewById(R.id.playerTwoName);


        playerNames = getSharedPreferences(names, 0);
        editor = playerNames.edit();

        //setPlayers();

        saveName.setOnClickListener(view -> {

            saveNames();
            onBackPressed();
     });
    }

    private void saveNames() {

        String playerOne = playerNameOne.getText().toString();
        String playerTwo = playerNameTwo.getText().toString();

        if (playerOne.equals("")) {

            Toast.makeText(this, "You must enter a name for Player One", Toast.LENGTH_SHORT).show();
        } else {

            checkPlayers(playerOne, playerTwo);
        }
    }

    //2 names entered = 2 player game
    //1 name entered = cpu as 2nd player
    private void checkPlayers(String nameOne, String nameTwo){

        int numberOfPlayers = 0;

        if(!nameOne.equals("")){
            if (!playerNames.contains(nameOne)){

            editor.putInt(nameOne, 0);
            editor.commit();
            }
            GameData.getInstance().setNameOne(nameOne);
            numberOfPlayers++;
        }

        if(!nameTwo.equals("")){
            if (!playerNames.contains(nameTwo)){

                editor.putInt(nameTwo, 0);
                editor.commit();
            }
            GameData.getInstance().setNameTwo(nameTwo);
            numberOfPlayers++;
        }
        if (nameTwo.equals("")){
            if (!playerNames.contains(nameTwo)){

                editor.putInt("CPU", 0);
                editor.commit();
            }
            GameData.getInstance().setNameTwo("CPU");
        }
        GameData.getInstance().setNumberOfPlayers(numberOfPlayers);

        //Lets the user know what time of game they will be playing
        if(numberOfPlayers >= 2)
        {
            Toast.makeText(this, "Two Player Game", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Single Player Game", Toast.LENGTH_SHORT).show();
        }
    }
}

