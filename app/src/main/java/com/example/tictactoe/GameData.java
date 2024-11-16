package com.example.tictactoe;

public class GameData {

    public String nameOne = "";
    public String nameTwo = "";
    public int numberOfPlayers = 0;
    private static GameData instance;

    public void setNameOne(String name)
    {
        nameOne = name;
    }

    public void setNameTwo(String name)
    {
        nameTwo = name;
    }

    public void setNumberOfPlayers(int i)
    {
        numberOfPlayers = i;
    }

    static GameData getInstance(){

        if( instance == null )
        {
            instance = new GameData();
        }
        return instance;
    }
}
