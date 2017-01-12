package engelbergbleyel.at.wizcore;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;

public class ConfigureGame extends AppCompatActivity {

    Game game;
    ArrayAdapter tempPlayersAdapter;
    ArrayList<String> tempPlayers;

    ArrayList<String> dbPlayersNames;
    ArrayList<Integer> dbPlayersIds;
    private ListView dbPlayersListView;
    DBHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_game);
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
        tempPlayers = new ArrayList<>();

        Switch toggle = (Switch) findViewById(R.id.sw_bidNotRounds);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) game.setSumBidsMustDifferFromRound(true);
                else game.setSumBidsMustDifferFromRound(false);
            }
        });


        if (game.isStartNewGame()) {
            game.resetGame();
        } else if (game.getAmountOfPlayers() != 0) {
            displayTempPlayersInList();
        }


        final ListView listNewPlayers = (ListView) findViewById(R.id.listView_playerAdded);
        listNewPlayers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Log.i("HelloListView", "You clicked Item: " + i + " at position:" + l);

                tempPlayers.remove(i);
                game.delPlayerFromGame(i);

                displayTempPlayersInList();

                //Toast.makeText(getApplicationContext(), getResources().getString(R.string.general_player)+" "+getResources().getString(R.string.general_deleted), Toast.LENGTH_SHORT).show();

                setInputFieldBack();
                enableAddPlayerButton();
                enableInputField();
                if (tempPlayers.size() < 3) {
                    Button button = (Button) findViewById(R.id.btn_startGame);
                    button.setEnabled(false);
                }
            }
        });


        final ListView listPlayersDb = (ListView) findViewById(R.id.listView_playerPool);
        listPlayersDb.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Log.i("HelloListView", "You clicked Item: " + i + " at position:" + l);

                if (! tempPlayers.contains(dbPlayersNames.get(i))) {
                    Player player = new Player(dbPlayersNames.get(i), true, dbPlayersIds.get(i));

                    tempPlayers.add(player.getName());
                    game.addPlayerToGame(player);

                    displayTempPlayersInList();

                    setInputFieldBack();
                    if (tempPlayers.size() > 2) {
                        Button button = (Button) findViewById(R.id.btn_startGame);
                        button.setEnabled(true);
                    }
                    if (tempPlayers.size() == 6) {
                        setAddPlayerButtonToFull();
                        setInputFieldBack();
                        disableInputField();
                        Toast.makeText(getApplicationContext(), "Maximum amount of Players reached", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Player already in game", Toast.LENGTH_SHORT).show();
                }


            }
        });

        mydb = new DBHelper(this);

        if (mydb.numberOfRows() != 0) {
            displayDbPlayerinList();
        }

        Log.i("a", "ON CREATE");
    }

    public void handleOnClickBtnAddPlayer(View view) {
        //adds Player with Name entered in InputField txt_enterName and raises amountPlayer by 1

        if (checkInputFieldHasValue()) {
            if (! tempPlayers.contains(getNameFromInputField())) {

                Player player = new Player(getNameFromInputField(), false, -1);

                tempPlayers.add(player.getName());
                game.addPlayerToGame(player);

                displayTempPlayersInList();

                setInputFieldBack();
                if (tempPlayers.size() > 2) {
                    Button button = (Button) findViewById(R.id.btn_startGame);
                    button.setEnabled(true);
                }
                if (tempPlayers.size() == 6) {
                    setAddPlayerButtonToFull();
                    setInputFieldBack();
                    disableInputField();
                    Toast.makeText(this, getResources().getString(R.string.configureGame_maxReached), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.configureGame_alreadyIn), Toast.LENGTH_SHORT).show();

            }
        } else {
            Toast.makeText(this, getResources().getString(R.string.configureGame_enterName), Toast.LENGTH_SHORT).show();
        }
    }

    public void displayTempPlayersInList() {
        //display the Names in the ListView listPlayers
        ListView listView = (ListView) findViewById(R.id.listView_playerAdded);
        tempPlayersAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, tempPlayers);
        listView.setAdapter(tempPlayersAdapter);
        tempPlayersAdapter.notifyDataSetChanged();
    }

    public void displayDbPlayerinList() {
        //mydb = new DBHelper(this);
        dbPlayersNames = mydb.getAllContacts();
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, dbPlayersNames);

        dbPlayersListView = (ListView) findViewById(R.id.listView_playerPool);
        dbPlayersListView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();

        dbPlayersIds = mydb.getAllIds();
        //for (Integer x : dbPlayersIds)
        //    Log.i("a", "IDS: " + x + ", ");
    }

    public String getNameFromInputField() {
        //returns enteredName from InputField txt_enterName

        EditText editText = (EditText) findViewById(R.id.txt_enterName);
        return editText.getText().toString();
    }

    public void setAddPlayerButtonToFull() {
        Button button = (Button) findViewById(R.id.btn_addNewPlayer);
        button.setText(getResources().getString(R.string.configureGame_full));
        button.setEnabled(false);
    }

    public void enableAddPlayerButton() {
        Button button = (Button) findViewById(R.id.btn_addNewPlayer);
        button.setText(getResources().getString(R.string.configureGame_addPlayer));
        button.setEnabled(true);
    }

    public void enableInputField() {
        EditText editText = (EditText) findViewById(R.id.txt_enterName);
        editText.setHint(getResources().getString(R.string.configureGame_playerName));
        editText.setEnabled(true);
    }

    public void disableInputField() {
        EditText editText = (EditText) findViewById(R.id.txt_enterName);
        editText.setHint(getResources().getString(R.string.configureGame_invitation));
        editText.setEnabled(false);
    }

    public boolean checkInputFieldHasValue() {
        //checks if a name is entered

        EditText editText = (EditText) findViewById(R.id.txt_enterName);

        if (editText.getText().toString().isEmpty())
            return false;
        else
            return true;
    }

    public void setInputFieldBack() {
        //sets InputField back to "Enter Name" and empty

        EditText editText = (EditText) findViewById(R.id.txt_enterName);
        editText.setText("");
        editText.setHint(getResources().getString(R.string.configureGame_playerName));
    }


    public void handleOnClickBtnStartGame(View view) {
        for (Player player : game.getPlayers()) player.initArrays(game.getAmountOfRounds());
        startActivity(new Intent(this, Scoresheet.class));
    }

    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.alert_backMain))
                .setMessage(getResources().getString(R.string.alert_inputsLost))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        game.resetGame();
                        NavUtils.navigateUpTo(ConfigureGame.this,getParentActivityIntent());
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
