package engelbergbleyel.at.wizcore;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class EditPlayers extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_players);
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


        Button button = (Button) findViewById(R.id.btn_addPlayerDB);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PlayerDetail.class);
                intent.putExtra("id", -1);
                startActivity(intent);
            }
        });
        buildTable();
    }

    public void buildTable() {

        TableLayout tableLayout = (TableLayout) findViewById(R.id.tbl_playerListEdit);
        DBHelper mydb = new DBHelper(this);

        Cursor rs = mydb.getAll();
        int counter = 0;

        if (rs != null) {
            if (rs.moveToFirst()) {
                do {
                    final TableRow tableRow = new TableRow(this);
                    TextView all = new TextView(this);
                    TextView name = new TextView(this);
                    TextView score = new TextView(this);

                    String nam = rs.getString(rs.getColumnIndex(DBHelper.PLAYERS_COLUMN_NAME));
                    String high = rs.getString(rs.getColumnIndex(DBHelper.PLAYERS_COLUMN_HIGHSCORE));
                    String alltime = rs.getString(rs.getColumnIndex(DBHelper.PLAYERS_COLUMN_ALLTIMESCORE));

                    name.setText(nam);
                    name.setTextSize(20);

                    all.setText(alltime);
                    all.setTextSize(20);

                    score.setText(high);
                    score.setTextSize(20);

                    name.setPadding(10, 2, 2, 2);
                    if (counter % 2 != 0) {
                        /*name.setTextColor(Color.BLACK);
                        all.setTextColor(Color.BLACK);
                        score.setTextColor(Color.BLACK);*/

                        tableRow.setBackgroundColor(Color.LTGRAY);

                    }
                    counter++;

                    tableRow.addView(name);
                    tableRow.addView(all);
                    tableRow.addView(score);
                    tableRow.setId(rs.getInt(rs.getColumnIndex(DBHelper.PLAYERS_COLUMN_ID)));
                    tableRow.isClickable();
                    tableRow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getApplicationContext(), PlayerDetail.class);
                            intent.putExtra("id", tableRow.getId());
                            startActivity(intent);
                        }
                    });

                    /*TableLayout.LayoutParams tableRowParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
                    tableRowParams.setMargins(0, 0, 0, 10);

                    tableRow.setLayoutParams(tableRowParams);*/

                    tableLayout.addView(tableRow);

                } while (rs.moveToNext());
            }
        }
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpTo(this, getParentActivityIntent());
    }

}
