package engelbergbleyel.at.wizcore;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ViewHighscore extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_highscore);
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

        buildTable();
    }

    public void buildTable() {

        TableLayout tableLayout = (TableLayout) findViewById(R.id.tbl_playerListHs);
        Integer position = 1;
        DBHelper mydb = new DBHelper(this);

        Cursor rs = mydb.getHighscore(-1);

        if (rs != null) {
            if (rs.moveToFirst()) {
                do {
                    TableRow tableRow = new TableRow(this);
                    TextView pos = new TextView(this);
                    TextView name = new TextView(this);
                    TextView score = new TextView(this);

                    String nam = rs.getString(rs.getColumnIndex(DBHelper.PLAYERS_COLUMN_NAME));
                    String high = rs.getString(rs.getColumnIndex(DBHelper.PLAYERS_COLUMN_HIGHSCORE));

                    switch (position) {
                        case 1:
                            pos.setTextColor(Color.rgb(255, 215, 0));
                            break;
                        case 2:
                            pos.setTextColor(Color.rgb(192, 192, 192));
                            break;
                        case 3:
                            pos.setTextColor(Color.rgb(205, 127, 50));
                            break;
                        default:
                            break;
                    }
                    pos.setText(position.toString());
                    pos.setTextSize(20);

                    name.setText(nam);
                    name.setTextSize(20);

                    score.setText(high);
                    score.setTextSize(20);

                    tableRow.addView(pos);
                    tableRow.addView(name);
                    tableRow.addView(score);

                    TableLayout.LayoutParams tableRowParams= new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
                    tableRowParams.setMargins(0, 0, 0, 10);
                    tableRow.setLayoutParams(tableRowParams);

                    tableLayout.addView(tableRow);

                    position++;
                } while (rs.moveToNext());
            }
        }
    }


    @Override
    public void onBackPressed() {
        NavUtils.navigateUpTo(this, getParentActivityIntent());
    }
}

