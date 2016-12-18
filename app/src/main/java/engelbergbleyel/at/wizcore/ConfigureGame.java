package engelbergbleyel.at.wizcore;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
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
import android.widget.ToggleButton;

import java.util.ArrayList;

public class ConfigureGame extends AppCompatActivity {

    public ArrayList<Player> playerArrayList = new ArrayList<Player>();
    Game game;
    PlayerAdapter playerAdapter; //notwendig???
    private ListView playerPool;
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

        Switch toggle = (Switch) findViewById(R.id.sw_bidNotRounds);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)  game.setSumBidsMustDifferFromRound(true);
                else            game.setSumBidsMustDifferFromRound(false);
            }
        });

        game = Game.getInstance();
        if(game.isStartNewGame()){
            game.resetGame();
        }else if (game.getAmountOfPlayers() != 0){
            displayPlayersInList();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        game = Game.getInstance();
        if(game.isStartNewGame()){
            game.resetGame();
        }else if (game.getAmountOfPlayers() != 0){
            displayPlayersInList();
        }

        final ListView listNewPlayers = (ListView) findViewById(R.id.listView_playerAdded);
        listNewPlayers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Log.i("HelloListView", "You clicked Item: " + i + " at position:" + l);
                playerArrayList.remove(i);
                playerAdapter = new PlayerAdapter(getApplicationContext(), playerArrayList);
                game.delPlayerFromGame(i);
                displayPlayersInList();
                Toast.makeText(getApplicationContext(), "Player deleted", Toast.LENGTH_SHORT).show();

                if (playerArrayList.size() < 3) {
                    Button button = (Button) findViewById(R.id.btn_startGame);
                    button.setEnabled(false);
                }
            }
        });

    }


    public void handleOnClickBtnAddPlayer(View view) {
        //adds Player with Name entered in InputField txt_enterName and raises amountPlayer by 1

        if (checkInputFieldHasValue()) {
            playerAdapter = new PlayerAdapter(this, playerArrayList);
            Player player = new Player(getNameFromInputField());
            playerAdapter.add(player);
            game.addPlayerToGame(player);

            //Log.i("a","PlayeR: "+player.toString()+"bids "+player.getBidAt(1) );

            displayPlayersInList();
            setInputFieldBack();
            if (playerArrayList.size() > 2) {
                Button button = (Button) findViewById(R.id.btn_startGame);
                button.setEnabled(true);
            }
            if (playerArrayList.size() == 6) {
                setAddPlayerButtonToFull();
                setInputFieldBack();
                disableInputField();
                Toast.makeText(this, "Maximum amount of Players reached", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please enter a Name", Toast.LENGTH_SHORT).show();
        }
    }

    public String getNameFromInputField() {
        //returns enteredName from InputField txt_enterName

        EditText editText = (EditText) findViewById(R.id.txt_enterName);
        return editText.getText().toString();
    }

    public void setAddPlayerButtonToFull() {
        Button button = (Button) findViewById(R.id.btn_addNewPlayer);
        button.setText("I'm full");
        button.setEnabled(false);
    }

    public void disableInputField() {
        EditText editText = (EditText) findViewById(R.id.txt_enterName);
        editText.setHint("Invitation only");
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
        editText.setHint("Enter Player Name");
    }

    public void displayPlayersInList() {
        //display the Names in the ListView listPlayers

        ListView listView = (ListView) findViewById(R.id.listView_playerAdded);
        listView.setAdapter(playerAdapter);
        playerAdapter.notifyDataSetChanged();

        mydb = new DBHelper(this);
        ArrayList array_list = mydb.getAllContacts();
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, array_list);
        playerPool = (ListView) findViewById(R.id.listView_playerPool);
        playerPool.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
    }

    public void handleOnClickBtnStartGame(View view) {
        for(Player player : game.getPlayers()) player.initArrays(game.getAmountOfRounds());
        startActivity(new Intent(this, Scoresheet.class));
    }

}
