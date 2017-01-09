package engelbergbleyel.at.wizcore;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PlayerDetail extends AppCompatActivity implements View.OnClickListener {

    DBHelper mydb;
    EditText name;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

/*        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mydb = new DBHelper(this);
        name = (EditText) findViewById(R.id.editTxt_playerNameDBValue);
        id = getIntent().getIntExtra("id", 0);
        displayDetails();
    }

    public void displayDetails() {
        TextView highscore = (TextView) findViewById(R.id.tv_playerHighscoreDBValue);
        TextView alltimescore = (TextView) findViewById(R.id.tv_playerAllTimeDBValue);

        Button delPlayer = (Button) findViewById(R.id.btn_delPlayerDB);
        Button updatePlayer = (Button) findViewById(R.id.btn_updatePlayerDB);
        Button savePlayer = (Button) findViewById(R.id.btn_savePlayerDB);

        if (id != -1) {
            Cursor rs = mydb.getData(id);
            rs.moveToFirst();

            delPlayer.setVisibility(View.VISIBLE);
            updatePlayer.setVisibility(View.VISIBLE);
            name.setText(rs.getString(rs.getColumnIndex(DBHelper.PLAYERS_COLUMN_NAME)));
            highscore.setText(rs.getString(rs.getColumnIndex(DBHelper.PLAYERS_COLUMN_HIGHSCORE)));
            alltimescore.setText(rs.getString(rs.getColumnIndex(DBHelper.PLAYERS_COLUMN_ALLTIMESCORE)));

            if (!rs.isClosed()) {
                rs.close();
            }
        } else {
            savePlayer.setVisibility(View.VISIBLE);
            name.setHint(getResources().getString(R.string.configureGame_hintName));
            highscore.setText("0");
            alltimescore.setText("0");
        }

    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpTo(this, getParentActivityIntent());
    }

    @Override
    public void onClick(View view) {
        Button button = (Button) view;

        switch (button.getId()) {
            case R.id.btn_updatePlayerDB:
                if (name.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Please enter a Name", Toast.LENGTH_SHORT).show();
                } else {
                    mydb.updateContact(id, name.getText().toString());
                    Toast.makeText(this, "Player updated", Toast.LENGTH_SHORT).show();
                    NavUtils.navigateUpTo(this,getParentActivityIntent());
                }
                break;

            case R.id.btn_delPlayerDB:
                new AlertDialog.Builder(this)
                        .setTitle("Delete Player?")
                        .setMessage("Scores will be lost!")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                mydb.deleteContact(id);
                                Toast.makeText(getApplicationContext(), "Player deleted", Toast.LENGTH_SHORT).show();
                                NavUtils.navigateUpTo(PlayerDetail.this,getParentActivityIntent());
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

                break;

            case R.id.btn_savePlayerDB:
                if (name.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Please enter a Name", Toast.LENGTH_SHORT).show();
                } else {
                    mydb.insertContact(name.getText().toString());
                    Toast.makeText(this, "Player saved", Toast.LENGTH_SHORT).show();
                    NavUtils.navigateUpTo(this,getParentActivityIntent());
                }
            default:
                break;
        }

    }
}
