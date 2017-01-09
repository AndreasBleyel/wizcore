package engelbergbleyel.at.wizcore;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayPlayer extends AppCompatActivity implements View.OnClickListener {

    int from_Where_I_Am_Coming = 0;
    private DBHelper mydb;

    EditText name;
    TextView highscore;
    TextView alltimescore;
    Button delPlayer;
    Button addPlayer;

    int id_To_Update = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_player);
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

        name = (EditText) findViewById(R.id.editTxt_playerNameDBValue);
        highscore = (TextView) findViewById(R.id.tv_playerHighscoreDBValue);
        alltimescore = (TextView) findViewById(R.id.tv_playerAllTimeDBValue);
        delPlayer = (Button) findViewById(R.id.btn_delPlayerDB);
        addPlayer = (Button) findViewById(R.id.btn_savePlayerDB);
        mydb = new DBHelper(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int value = extras.getInt("id");

            if (value > 0) {
                //means this is the view part not the add contact part.
                Cursor rs = mydb.getData(value);
                id_To_Update = value;
                rs.moveToFirst();

                String nam = rs.getString(rs.getColumnIndex(DBHelper.PLAYERS_COLUMN_NAME));
                String high = rs.getString(rs.getColumnIndex(DBHelper.PLAYERS_COLUMN_HIGHSCORE));
                //Log.i("a", "high: " + high + " id: " + id_To_Update);
                String alltime = rs.getString(rs.getColumnIndex(DBHelper.PLAYERS_COLUMN_ALLTIMESCORE));

                if (!rs.isClosed()) {
                    rs.close();
                }

                addPlayer.setEnabled(false);

                name.setText(" "+nam);
                name.setFocusable(false);
                name.setClickable(false);

                highscore.setText(" "+high);
                highscore.setFocusable(false);
                highscore.setClickable(false);

                alltimescore.setText(" "+alltime);
                alltimescore.setFocusable(false);
                alltimescore.setClickable(false);

            }
        }
    }

    public void run(View view) {
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            int value = extras.getInt("id");
            Log.i("a","Value: "+value);

            if (value > 0) {
                if (mydb.updateContact(id_To_Update, name.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), EditPlayers.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "not Updated", Toast.LENGTH_SHORT).show();
                }
            } else {
                delPlayer.setEnabled(false);
                if (mydb.insertContact(name.getText().toString() /*Integer.parseInt(highscore.getText().toString())*/ )) {
                    Toast.makeText(getApplicationContext(), "done",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "not done",
                            Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(getApplicationContext(), EditPlayers.class);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onClick(View view) {

        delPlayer = (Button) view;

        switch (delPlayer.getId()) {
            case R.id.btn_delPlayerDB:
                Log.i("a", "after delete: " + mydb.deleteContact(id_To_Update));
                Intent intent = new Intent(getApplicationContext(), EditPlayers.class);
                startActivity(intent);
                break;
            default:
                break;
        }

    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpTo(this,getParentActivityIntent());
    }
}
