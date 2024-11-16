package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onStart() {
        super.onStart();

        Resources resources = getResources();
        String [] main = resources.getStringArray(R.array.mainMenu);

        ListView mainMenu = findViewById(R.id.mainMenu);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,main);
        mainMenu.setAdapter(adapter);

        mainMenu.setOnItemClickListener((adapterView, view, i, l) -> {
            String items = adapter.getItem(i);
            openActivity(items);
        });
    }

    private void openActivity (String activity){

        Intent intent;

        //switch statement for each activity
        switch (activity){

            case "Enter Names":
                intent = new Intent(this,EnterNamesActivity.class);
                startActivity(intent);
                break;

            case "Play Game":
                if (GameData.getInstance().nameOne.toString().equals("")) {
                    Toast.makeText(this, "You must enter a name for Player One", Toast.LENGTH_SHORT).show();
                    return;
                } else
                intent = new Intent(this,PlayGameActivity.class);
                startActivity(intent);
                break;

            case "Standings":
                intent = new Intent(this,StandingsActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }
}