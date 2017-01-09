package engelbergbleyel.at.wizcore;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Scoresheet extends AppCompatActivity {

    Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoresheet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        game = Game.getInstance();

        Log.i("a", "Round onCreate: " + game.getRound());
        createTable();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.i("a", "START ON RESUME");
        game = Game.getInstance();
        /*for (Player player : game.getPlayers()) {
            Log.i("a", "Player: " + player.toString());
        }*/
        createTable();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Abort Game")
                .setMessage("and go back to MainScreen?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        game.endGame(true,getApplicationContext());
                        NavUtils.navigateUpTo(Scoresheet.this,getParentActivityIntent());
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void handleOnClickButtonTricks(View view) {
        Intent intent = new Intent(this, Bids.class);
        intent.putExtra("BidOrScore", false);

        Button button = (Button) findViewById(R.id.btn_Tricks);
        button.setEnabled(false);

        startActivity(intent);
    }

    public void handleOnClickButtonBids(View view) {
        Intent intent = new Intent(this, Bids.class);
        intent.putExtra("BidOrScore", true);

        Button button = (Button) findViewById(R.id.btn_Tricks);
        button.setEnabled(true);

        startActivity(intent);
    }

    public void handleOnClickButtonNewGame(View view) {
        startActivity(new Intent(this, ConfigureGame.class));
    }

    public void handleOnClickButtonMain(View view){
        startActivity(new Intent(this, MainActivity.class));
    }

    public void createTable() {

        TextView roundNumber = (TextView) findViewById(R.id.tv_Round);
        TextView bids = (TextView) findViewById(R.id.tv_Bids);

        TextView play1name = (TextView) findViewById(R.id.tv_play1);
        TextView play1point = (TextView) findViewById(R.id.tv_play1Point);
        TextView play1bid = (TextView) findViewById(R.id.tv_play1Bid);

        TextView play2name = (TextView) findViewById(R.id.tv_play2);
        TextView play2point = (TextView) findViewById(R.id.tv_play2Point);
        TextView play2bid = (TextView) findViewById(R.id.tv_play2Bid);

        TextView play3name = (TextView) findViewById(R.id.tv_play3);
        TextView play3point = (TextView) findViewById(R.id.tv_play3Point);
        TextView play3bid = (TextView) findViewById(R.id.tv_play3Bid);

        TextView play4name = (TextView) findViewById(R.id.tv_play4);
        TextView play4point = (TextView) findViewById(R.id.tv_play4Point);
        TextView play4bid = (TextView) findViewById(R.id.tv_play4Bid);

        TextView play5name = (TextView) findViewById(R.id.tv_play5);
        TextView play5point = (TextView) findViewById(R.id.tv_play5Point);
        TextView play5bid = (TextView) findViewById(R.id.tv_play5Bid);

        TextView play6name = (TextView) findViewById(R.id.tv_play6);
        TextView play6point = (TextView) findViewById(R.id.tv_play6Point);
        TextView play6bid = (TextView) findViewById(R.id.tv_play6Bid);

        if (game.gameOver()) {
            Button bidsButton = (Button) findViewById(R.id.btn_Bids);
            Button tricksButton = (Button) findViewById(R.id.btn_Tricks);
            Button newGame = (Button) findViewById(R.id.btn_newGame);
            newGame.setVisibility(View.VISIBLE);
            Button main = (Button)findViewById(R.id.btn_backMain);
            main.setVisibility(View.VISIBLE);
            bidsButton.setEnabled(false);
            tricksButton.setEnabled(false);

            game.endGame(false,this);

            bids.setText(getResources().getString(R.string.scoreSheet_gameOver));
            roundNumber.setText("Round: " + Integer.toString(game.getRound()-1) + "/" + game.getAmountOfRounds());

            play1name.setText(game.getPlayers().get(0).getName());
            play1name.setTextColor(Color.rgb(255,215,0));
            play1point.setText(getResources().getString(R.string.scoreSheet_first) + ": " + game.getPlayers().get(0).getScore());
            play1bid.setVisibility(View.INVISIBLE);

            play2name.setText(game.getPlayers().get(1).getName());
            play2name.setTextColor(Color.rgb(192,192,192));
            play2point.setText(getResources().getString(R.string.scoreSheet_second) + ": " + game.getPlayers().get(1).getScore());
            play2bid.setVisibility(View.INVISIBLE);

            play3name.setText(game.getPlayers().get(2).getName());
            play3name.setTextColor(Color.rgb(205, 127, 50));
            play3point.setText(getResources().getString(R.string.scoreSheet_third) + ": " + game.getPlayers().get(2).getScore());
            play3bid.setVisibility(View.INVISIBLE);

            if (game.getPlayers().size() > 3) {
                play4name.setText(game.getPlayers().get(3).getName());
                play4name.setVisibility(View.VISIBLE);
                play4point.setText(getResources().getString(R.string.general_points) + ": " + game.getPlayers().get(3).getScore());
                play4point.setVisibility(View.VISIBLE);
                play4bid.setVisibility(View.INVISIBLE);

                if (game.getPlayers().size() > 4) {
                    play5name.setText(game.getPlayers().get(4).getName());
                    play5name.setVisibility(View.VISIBLE);
                    play5point.setText(getResources().getString(R.string.general_points) + ": " + game.getPlayers().get(4).getScore());
                    play5point.setVisibility(View.VISIBLE);
                    play5bid.setVisibility(View.INVISIBLE);
                    if (game.getPlayers().size() > 5) {
                        play6name.setText(game.getPlayers().get(5).getName());
                        play6name.setVisibility(View.VISIBLE);
                        play6point.setText(getResources().getString(R.string.general_points) + ": " + game.getPlayers().get(5).getScore());
                        play6point.setVisibility(View.VISIBLE);
                        play6bid.setVisibility(View.INVISIBLE);
                    }
                }
                View divider = findViewById(R.id.div_bottom);
                divider.setVisibility(View.VISIBLE);
            }


        } else {

            roundNumber.setText("Round: " + game.getRound() + "/" + game.getAmountOfRounds());
            Log.i("a", "Round Scoresheet: " + game.getRound());
            bids.setText(getResources().getString(R.string.general_bids) + ": " + game.amountOfBidsForRound(game.getRound()) + "/" + game.getRound());

            play1name.setText(game.getPlayers().get(0).getName());
            play1point.setText(getResources().getString(R.string.general_points) + ": " + game.getPlayers().get(0).getScore());
            play1bid.setText(getResources().getString(R.string.general_bids) + ": " + game.getPlayers().get(0).getBidAt(game.getRound()));

            play2name.setText(game.getPlayers().get(1).getName());
            play2point.setText(getResources().getString(R.string.general_points) + ": " + game.getPlayers().get(1).getScore());
            play2bid.setText(getResources().getString(R.string.general_bids) + ": " + game.getPlayers().get(1).getBidAt(game.getRound()));

            play3name.setText(game.getPlayers().get(2).getName());
            play3point.setText(getResources().getString(R.string.general_points) + ": " + game.getPlayers().get(2).getScore());
            play3bid.setText(getResources().getString(R.string.general_bids) + ": " + game.getPlayers().get(2).getBidAt(game.getRound()));


            if (game.getPlayers().size() > 3) {
                play4name.setText(game.getPlayers().get(3).getName());
                play4name.setVisibility(View.VISIBLE);
                play4point.setText(getResources().getString(R.string.general_points) + ": " + game.getPlayers().get(3).getScore());
                play4point.setVisibility(View.VISIBLE);
                play4bid.setText(getResources().getString(R.string.general_bids) + ": " + game.getPlayers().get(3).getBidAt(game.getRound()));
                play4bid.setVisibility(View.VISIBLE);

                if (game.getPlayers().size() > 4) {
                    play5name.setText(game.getPlayers().get(4).getName());
                    play5name.setVisibility(View.VISIBLE);
                    play5point.setText(getResources().getString(R.string.general_points) + ": " + game.getPlayers().get(4).getScore());
                    play5point.setVisibility(View.VISIBLE);
                    play5bid.setText(getResources().getString(R.string.general_bids) + ": " + game.getPlayers().get(4).getBidAt(game.getRound()));
                    play5bid.setVisibility(View.VISIBLE);
                    if (game.getPlayers().size() > 5) {
                        play6name.setText(game.getPlayers().get(5).getName());
                        play6name.setVisibility(View.VISIBLE);
                        play6point.setText(getResources().getString(R.string.general_points) + ": " + game.getPlayers().get(5).getScore());
                        play6point.setVisibility(View.VISIBLE);
                        play6bid.setText(getResources().getString(R.string.general_bids) + ": " + game.getPlayers().get(5).getBidAt(game.getRound()));
                        play6bid.setVisibility(View.VISIBLE);
                    }
                }
                View divider = findViewById(R.id.div_bottom);
                divider.setVisibility(View.VISIBLE);
            }


        }
    }

}
