package engelbergbleyel.at.wizcore;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class Bids extends AppCompatActivity implements View.OnClickListener {

    Game game;

    //true for bid, false for score
    boolean bidOrScore;

    TextView textViewBid;
    TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bids);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

     /*   FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        game = Game.getInstance();
        bidOrScore = getIntent().getBooleanExtra("BidOrScore", false);

        createInterface();
    }

    public void createInterface() {

        tableLayout = (TableLayout) findViewById(R.id.table_bids);
        int id = 0;

        tableLayout.removeAllViews();

        TextView textView = (TextView) findViewById(R.id.txt_BidOrScore);
        if (bidOrScore)
            textView.setText(getResources().getString(R.string.general_bids) + " " + getResources().getString(R.string.general_please));
        else
            textView.setText(getResources().getString(R.string.general_tricks) + " " + getResources().getString(R.string.general_please));

        for (int i = 0; i < game.getPlayers().size(); i++) {

            TableRow tableRow = new TableRow(this);
            Button btnMinus;
            Button btnPlus;

            TextView textViewName = new TextView(this);
            textViewName.setText(game.getPlayers().get(i).getName());
            textViewName.setTextSize(18);

            /*LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            llp.setMargins(0, 0, 50, 0); // llp.setMargins(left, top, right, bottom);
            textViewName.setLayoutParams(llp);*/

            tableRow.addView(textViewName);

            btnMinus = new Button(this);
            btnMinus.setText("-");
            btnMinus.setTypeface(Typeface.DEFAULT_BOLD);
            btnMinus.setId(id);
            btnMinus.setOnClickListener(this);
            btnMinus.setBackground(getResources().getDrawable(R.drawable.button,null));

            tableRow.addView(btnMinus);

            textViewBid = new TextView(this);
            textViewBid.setGravity(Gravity.CENTER);
            textViewBid.setTextSize(18);

            if (bidOrScore)
                textViewBid.setText(String.valueOf(game.getPlayers().get(i).getBidAt(game.getRound())));
            else
                textViewBid.setText(String.valueOf(game.getPlayers().get(i).getTrickAt(game.getRound())));


            textViewBid.setId(id + 1);
            tableRow.addView(textViewBid);

            btnPlus = new Button(this);
            btnPlus.setText("+");
            btnPlus.setTypeface(Typeface.DEFAULT_BOLD);
            btnPlus.setId(id + 2);
            btnPlus.setOnClickListener(this);
            btnPlus.setBackground(getResources().getDrawable(R.drawable.button,null));
            tableRow.addView(btnPlus);

            id += 3;

            TableLayout.LayoutParams tableRowParams= new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
            tableRowParams.setMargins(0,0,0,5);
            tableRow.setLayoutParams(tableRowParams);
            tableLayout.setStretchAllColumns(true);
            tableLayout.addView(tableRow);
        }

        TextView possibleBids = (TextView) findViewById(R.id.tv_sumBidsTricks);
        possibleBids.setId(18);


        if (bidOrScore) {
            possibleBids.setText(getResources().getString(R.string.bids_sumofBids) + ": " + game.amountOfBidsForRound(game.getRound()) + "/" + game.getRound());
        } else {
            possibleBids.setText(getResources().getString(R.string.bids_sumofTricks)+ ": " + game.amountOfTricksForRound(game.getRound()) + "/" + game.getRound());

            Button button = (Button) findViewById(R.id.btn_bidsDone);
            button.setText(getResources().getString(R.string.bids_activity_calcScore));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleOnClickCalcScore(v);
                }
            });
        }

    }

    @Override
    public void onClick(View v) {

        TextView textViewNewBid = null;

        //v.setBackground(getResources().getDrawable(R.drawable.button_onclick,null));
        switch (v.getId()) {

            //player 1
            case 0: //minus
                respondToBidChange(0, false);
                break;

            case 2: //plus
                respondToBidChange(0, true);
                break;

            //player 2
            case 3://minus
                respondToBidChange(1, false);
                break;

            case 5://plus
                respondToBidChange(1, true);
                break;

            //player 3
            case 6://minus
                respondToBidChange(2, false);
                break;

            case 8://plus
                respondToBidChange(2, true);
                break;

            //player 4
            case 9://minus
                respondToBidChange(3, false);
                break;
            case 11://plus
                respondToBidChange(3, true);
                break;

            //player 5
            case 12://minus
                respondToBidChange(4, false);
                break;

            case 14://plus
                respondToBidChange(4, true);
                break;

            //player 6
            case 15://minus
                respondToBidChange(5, false);
                break;

            case 17://plus
                respondToBidChange(5, true);
                break;

            default:
                break;
        }

        if (bidOrScore) {
            //bids
            TextView textViewNewSum = (TextView) findViewById(18);
            textViewNewSum.setText(getResources().getString(R.string.bids_sumofBids)+": " + game.amountOfBidsForRound(game.getRound()) + "/" + game.getRound());
            if (game.amountOfBidsForRound(game.getRound()) == game.getRound())
                textViewNewSum.setTextColor(Color.RED);
            else
                textViewNewSum.setTextColor(Color.BLACK);
        } else {
            //tricks
            TextView textViewNewSum = (TextView) findViewById(18);
            textViewNewSum.setText(getResources().getString(R.string.bids_sumofTricks)+": " + game.amountOfTricksForRound(game.getRound()) + "/" + game.getRound());
            if (game.amountOfTricksForRound(game.getRound()) == game.getRound())
                textViewNewSum.setTextColor(getResources().getColor(R.color.green,null));
            else
                textViewNewSum.setTextColor(Color.RED);
        }
        tableLayout.refreshDrawableState();

    }

    public void respondToBidChange(int playerNumber, boolean increaseOrDecrease) {

        int textViewId = 0;
        switch (playerNumber) {
            case 0:
                textViewId = 1;
                break;
            case 1:
                textViewId = 4;
                break;
            case 2:
                textViewId = 7;
                break;
            case 3:
                textViewId = 10;
                break;
            case 4:
                textViewId = 13;
                break;
            case 5:
                textViewId = 16;
                break;
        }

        textViewBid = (TextView) findViewById(textViewId);

        if (increaseOrDecrease) {
            //erhöhen
            if (bidOrScore) {
                if (!game.getPlayers().get(playerNumber).raiseBidByOne(game.getRound()))
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.bids_youCantTricks), Toast.LENGTH_SHORT).show();
                else {
                    textViewBid.setText(String.valueOf(game.getPlayers().get(playerNumber).getBidAt(game.getRound())));
                }
            } else {
                if (game.amountOfTricksForRound(game.getRound()) == game.getRound())
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.bids_maxAmount), Toast.LENGTH_SHORT).show();
                else if (!game.getPlayers().get(playerNumber).raiseTrickByOne(game.getRound()))
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.bids_youCantTricks), Toast.LENGTH_SHORT).show();
                else {
                    textViewBid.setText(String.valueOf(game.getPlayers().get(playerNumber).getTrickAt(game.getRound())));
                }
            }

        } else {
            //verringern
            if (!game.getPlayers().get(playerNumber).lowerBidOrScoreByOne(game.getRound(), bidOrScore))
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.bids_less0), Toast.LENGTH_SHORT).show();
            else {
                if (bidOrScore)
                    textViewBid.setText(String.valueOf(game.getPlayers().get(playerNumber).getBidAt(game.getRound())));
                else
                    textViewBid.setText(String.valueOf(game.getPlayers().get(playerNumber).getTrickAt(game.getRound())));
            }
        }
    }

    public void handleOnClickDone(View view) {
        if (game.isSumBidsMustDifferFromRound()) {
            if (game.getRound() == game.amountOfBidsForRound(game.getRound())) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.configureGame_bidsNotRounds), Toast.LENGTH_SHORT).show();
            } else
                this.finish();
        } else
            this.finish();
    }

    public void handleOnClickCalcScore(View view) {

        if (game.amountOfTricksForRound(game.getRound()) == game.getRound() || bidOrScore) {
            game.calculateScore();
            game.raiseRoundByOne();
            this.finish();
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.bids_enterAll), Toast.LENGTH_SHORT).show();
        }
    }

    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.alert_backScoresheet))
                .setMessage(getResources().getString(R.string.alert_inputsLost))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        NavUtils.navigateUpTo(Bids.this,getParentActivityIntent());
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
}
