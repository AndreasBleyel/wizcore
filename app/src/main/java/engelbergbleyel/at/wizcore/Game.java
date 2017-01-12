package engelbergbleyel.at.wizcore;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by andi on 30.11.16.
 */

public class Game {

    //class attributes
    private int round;
    private int amountOfPlayers;
    private int amountOfRounds;
    private ArrayList<Player> players;
    private boolean sumBidsMustDifferFromRound;
    private boolean startNewGame;

    //singleton
    private static Game ourInstance = new Game();

    public static Game getInstance() {
        return ourInstance;
    }

    private Game() {
        this.round = 1;
        players = new ArrayList<>();
        amountOfPlayers = 0;
        startNewGame = false;
        sumBidsMustDifferFromRound = true;
    }

    //getter
    public boolean getSumBids(){
        return sumBidsMustDifferFromRound;
    }

    public int getRound() {
        return round;
    }

    public int getAmountOfPlayers() {
        return amountOfPlayers;
    }

    public int getAmountOfRounds() {
        return amountOfRounds;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public boolean isStartNewGame() {
        return startNewGame;
    }


    //setter
    public void setSumBidsMustDifferFromRound(boolean sumBidsMustDifferFromRound) {
        this.sumBidsMustDifferFromRound = sumBidsMustDifferFromRound;
    }

    //class methods
    public void raiseRoundByOne() {
        round++;
    }

    public void addPlayerToGame(Player player) {
        players.add(player);
        amountOfPlayers++;
        setAmountOfRounds();
    }

    public void delPlayerFromGame(int playerPosition) {
        //del player
        players.remove(playerPosition);
        amountOfPlayers--;
        setAmountOfRounds();
    }

    private void setAmountOfRounds() {
        switch (amountOfPlayers) {
            case 0:
            case 1:
            case 2:
                break;
            case 3:
                amountOfRounds = 3;
                break;
            case 4:
                amountOfRounds = 3;
                break;
            case 5:
                amountOfRounds = 3;
                break;
            case 6:
                amountOfRounds = 3;
                break;
        }
    }

    public boolean isSumBidsMustDifferFromRound() {
        return sumBidsMustDifferFromRound;
    }

    public int amountOfBidsForRound(int round) {
        Log.i("a", "Round Game: " + round);
        int sum = 0;
        for (Player player : players) {
            sum += player.getBidAt(round);
        }
        return sum;
    }

    public int amountOfTricksForRound(int round) {
        int sum = 0;
        for (Player player : players) {
            sum += player.getTrickAt(round);
        }
        return sum;
    }

    public boolean gameOver() {
        if (round > amountOfRounds) return true;
        else return false;
    }

    public void calculateScore() {
        for (Player player : players) {
            player.calcScore(round);
        }
    }

    private void calcPositions() {
        Collections.sort(players);
        Collections.reverse(players);
    }

    public void endGame(boolean aborted, Context context) {
        if(!aborted){
            calcPositions();
            for(Player temp : players){
                if(temp.isDbPlayer()){
                    DBHelper mydb = new DBHelper(context);
                    mydb.updateAllTimeScore(temp.getId(),temp.getScore());

                    Cursor res = mydb.getHighscore(temp.getId());
                    res.moveToFirst();
                    if(temp.getScore() > res.getInt(res.getColumnIndex(DBHelper.PLAYERS_COLUMN_HIGHSCORE))){
                        mydb.updateHighScore(temp.getId(),temp.getScore());
                    }
                }
            }
        }
        startNewGame = true;
    }

    public void resetGame() {
        this.round = 1;
        players = new ArrayList<>();
        amountOfPlayers = 0;
        startNewGame = false;
    }
}