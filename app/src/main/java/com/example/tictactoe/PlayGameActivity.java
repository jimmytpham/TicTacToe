package com.example.tictactoe;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class PlayGameActivity extends AppCompatActivity implements View.OnClickListener {

    private Button[] buttons = new Button[9];
    Button button0, button1, button2, button3, button4, button5, button6, button7, button8;
    Button reset;
    TextView player1Name, player2Name, Turn, player1WinScore, player2WinScore;
    boolean playerOneTurn;
    int roundCount, player1Wins, player2Wins;

    //player 1 = 0
    //player 2 = 1
    //empty = -1
    int[] filledPositions = {-1, -1, -1, -1, -1, -1, -1, -1, -1};


    //SharedPreference variables for saving data
    private String names = "PlayerNames";
    private SharedPreferences playerNames;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        // assign textViews
        player1Name = (TextView) findViewById(R.id.player1name);
        player2Name = (TextView) findViewById(R.id.player2name);
        player1Name.setText(GameData.getInstance().nameOne);
        player2Name.setText(GameData.getInstance().nameTwo);

        player1WinScore = (TextView) findViewById(R.id.player1WinScore);
        player2WinScore = (TextView) findViewById(R.id.player2WinScore);

        Turn = (TextView) findViewById(R.id.turn);
        //start on player 1 turn
        Turn.setText("It's " + player1Name.getText().toString() + "'s Turn!");


        //assign buttons and onClickListener
        for (int i = 0; i < buttons.length; i++) {
            String buttonID = "button" + i;
            int resourceID = getResources().getIdentifier(buttonID, "id", getPackageName());
            buttons[i] = (Button) findViewById(resourceID);
            buttons[i].setOnClickListener(this);
        }

        reset = findViewById(R.id.reset);
        reset.setOnClickListener(view -> restartGame());

        playerNames = getSharedPreferences(names, 0);
        editor = playerNames.edit();


        //set round count and player wins to 0
        roundCount = 0;
        player1Wins = 0;
        player2Wins = 0;
        playerOneTurn = true;
    }

    @Override
    public void onClick(View view) {

        //set button click
        String buttonID = view.getResources().getResourceEntryName(view.getId());
        int buttonClicked = Integer.parseInt(buttonID.substring(buttonID.length() - 1, buttonID.length()));

        //if button is already clicked, do nothing
        if (filledPositions[buttonClicked] != -1) {
            return;
        }
        if (playerOneTurn) {
            ((Button) view).setText("X");
            ((Button) view).setTextColor(Color.parseColor("#1700FF"));
            filledPositions[buttonClicked] = 0; //player 1
            Turn.setText("It's " + player2Name.getText().toString() + "'s Turn!");
        } else {
            ((Button) view).setText("O");
            ((Button) view).setTextColor(Color.parseColor("#FF0000"));
            filledPositions[buttonClicked] = 1; //player 1
            Turn.setText("It's " + player1Name.getText().toString() + "'s Turn!");
        }

        roundCount++;


        //check for winner
        if (checkWinner()) {
            if (playerOneTurn) {
                playerOneWins();
            } else {
                playerTwoWins();
            }
        } else if (roundCount == 9) {
            draw();
        } else {
            playerOneTurn = !playerOneTurn;
        }
        if (!playerOneTurn && player2Name.getText().toString().equals("CPU")){
            cpuAsync();
        }
    }

    private boolean checkWinner() {
        boolean winnerResult = false;

        //Assigning all the possible ways to win in an Array
        int[][] winningPosition = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, //rows
                {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, //columns
                {0, 4, 8}, {2, 4, 6}}; //diagonals

        //Getting all the winning positions
        for (int[] WinningPosition : winningPosition) {
            if (filledPositions[WinningPosition[0]] == filledPositions[WinningPosition[1]]
                    && filledPositions[WinningPosition[1]] == filledPositions[WinningPosition[2]]
                    && filledPositions[WinningPosition[0]] != -1) {
                //Returning true for win
                winnerResult = true;
            }
        }
        return winnerResult;
    }

        //updates which player wins
    private void playerOneWins() {
        player1Wins++;
        Toast.makeText(this, player1Name.getText().toString() + " Wins!", Toast.LENGTH_SHORT).show();
        updatePoints();
        playAgain();
    }

    private void playerTwoWins() {
        player2Wins++;
        Toast.makeText(this, player2Name.getText().toString() + " Wins!", Toast.LENGTH_SHORT).show();
        updatePoints();
        playAgain();
    }

    private void draw() {
        Toast.makeText(this, "Draw", Toast.LENGTH_SHORT).show();
        playAgain();
    }

    private void updatePoints() {
        player1WinScore.setText("Wins: " + player1Wins);
        player2WinScore.setText("Wins: " + player2Wins);
    }


    private void playAgain() {
        //set current player back to 1 and round count to 0
        playerOneTurn = true;
        roundCount = 0;

        //change buttons back to empty
        for (int i = 0; i < buttons.length; i++) {
            filledPositions[i] = -1;
            buttons[i].setText("");
        }

        Turn.setText("It's " + player1Name.getText().toString() + "'s Turn");
    }

    private void restartGame() {
        //reset score
        player1Wins = 0;
        player2Wins = 0;
        updatePoints();
        playAgain();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //for screen rotations
        outState.putInt("roundCount", roundCount);
        outState.putInt("player1Wins", player1Wins);
        outState.putInt("player2Wins", player2Wins);
        outState.putBoolean("playerOneTurn", playerOneTurn);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        roundCount = savedInstanceState.getInt("roundCount");
        player1Wins = savedInstanceState.getInt("player1Wins");
        player2Wins = savedInstanceState.getInt("player2Wins");
        playerOneTurn = savedInstanceState.getBoolean("playerOneTurn");
    }

    public void cpuMove() {

        button0 = findViewById(R.id.button0);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button5 = findViewById(R.id.button5);
        button6 = findViewById(R.id.button6);
        button7 = findViewById(R.id.button7);
        button8 = findViewById(R.id.button8);

        //generates random value
        int randomNumber = new Random().nextInt(filledPositions.length);

        //get the value from the array
        int buttonClick = filledPositions[randomNumber];

        //check if the button is already used
        if (buttonClick == -1) {
            if (randomNumber == 0) {
                button0.setText("O");
                filledPositions[0] = 1; //player 2
                Turn.setText("It's " + player1Name.getText().toString() + "'s Turn!");
            }
            if (randomNumber == 1) {
                button1.setText("O");
                filledPositions[1] = 1; //player 2
                Turn.setText("It's " + player1Name.getText().toString() + "'s Turn!");
            }
            if (randomNumber == 2) {
                button2.setText("O");
                filledPositions[2] = 1; //player 2
                Turn.setText("It's " + player1Name.getText().toString() + "'s Turn!");
            }
            if (randomNumber == 3) {
                button3.setText("O");
                filledPositions[3] = 1; //player 2
                Turn.setText("It's " + player1Name.getText().toString() + "'s Turn!");
            }
            if (randomNumber == 4) {
                button4.setText("O");
                filledPositions[4] = 1; //player 2
                Turn.setText("It's " + player1Name.getText().toString() + "'s Turn!");
            }
            if (randomNumber == 5) {
                button5.setText("O");
                filledPositions[5] = 1; //player 2
                Turn.setText("It's " + player1Name.getText().toString() + "'s Turn!");
            }
            if (randomNumber == 6) {
                button6.setText("O");
                filledPositions[6] = 1; //player 2
                Turn.setText("It's " + player1Name.getText().toString() + "'s Turn!");
            }
            if (randomNumber == 7) {
                button7.setText("O");
                filledPositions[7] = 1; //player 2
                Turn.setText("It's " + player1Name.getText().toString() + "'s Turn!");
            }
            if (randomNumber == 8) {
                button8.setText("O");
                filledPositions[8] = 1; //player 2
                Turn.setText("It's " + player1Name.getText().toString() + "'s Turn!");
            }
        } else {
            cpuMove();
        }
        if (checkWinner()) {
            if (playerOneTurn) {
                playerOneWins();
            } else {
                playerTwoWins();
            }
        }
        playerOneTurn = true;
    }
    //Runs the background thread for the CPU's moves
    private void cpuAsync()
    {
        new CPUAsync(this).execute("","");
    }
}

//background Async
class CPUAsync extends AsyncTask<String, String, String>
{
    private Context context;

    public CPUAsync(Context context)
    {
        this.context = context;
        PlayGameActivity cont = (PlayGameActivity) context;
        cont.cpuMove();
    }

    @Override
    protected String doInBackground(String... strings)
    {
        return null;
    }
}
