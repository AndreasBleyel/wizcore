package engelbergbleyel.at.wizcore;

/**
 * Created by andi on 30.11.16.
 */

public class Player implements Comparable<Player>{

    //class attributes
    private String name;
    private int score;
    private int position;
    private int[] bids;
    private int[] tricks;
    private boolean isDbPlayer;
    private int id;

    public Player(String name, boolean isDbPlayer, int id) {
        this.name = name;
        this.score = 0;
        this.isDbPlayer = isDbPlayer;
        this.id = id;
    }

    //getter
    public String getName() {
        return name;
    }

    public int getBidAt(int round) {
        return bids[round - 1];
    }

    public int getTrickAt(int round) {
        return tricks[round - 1];
    }

    public int getScore() {
        return score;
    }

    public int getPosition() { return position; }

    public boolean isDbPlayer() { return isDbPlayer; }

    public int getId() { return id; }

    //setter


    public void setPosition(int position) { this.position = position; }

    //class methods
    public boolean raiseBidByOne(int round) {
        if (bids[round - 1] < round) {
            bids[round - 1]++;
            return true;
        } else
            return false;
    }

    public boolean raiseTrickByOne(int round) {
        if (tricks[round - 1] < round) {
            tricks[round - 1]++;
            return true;
        } else
            return false;
    }

    public boolean lowerBidOrScoreByOne(int round, boolean bidOrScore) {
        if (bidOrScore && bids[round - 1] > 0) {
            bids[round - 1]--;
            return true;
        } else if (!bidOrScore && tricks[round - 1] > 0) {
            tricks[round - 1]--;
            return true;
        } else {
            return false;
        }
    }

    public void calcScore(int round) {
        if (getBidAt(round) == getTrickAt(round))
            score += 20 + (getBidAt(round) * 10);
        else {
            int points;
            points = getTrickAt(round) - getBidAt(round);

            if (points > 0)
                points = points * 10 * -1;
            else
                points *= 10;

            score += points;
        }

    }

    public void initArrays(int amountOfRounds) {
        bids = new int[amountOfRounds];
        tricks = new int[amountOfRounds];
        for (int i = 0; i < bids.length - 1; i++) {
            bids[i] = 0;
            tricks[i] = 0;
        }
    }


    @Override
    public String toString() {
        String temp;

        temp = "BIDS: ";
        for (int i : bids)
            temp = temp + Integer.toString(i);

        temp += " || TRICKS: ";
        for (int i : tricks)
            temp = temp + Integer.toString(i);

        return " " + name + "\nScore: " + score + " " + temp/*+"\nRounds: "+getAmountOfRounds()*/;
    }

    @Override
    public int compareTo(Player o) {
        if (score < o.score)
            return -1;
        else if (score == o.score)
            return 0;
        else
            return 1;
    }
}
