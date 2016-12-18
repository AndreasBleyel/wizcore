package engelbergbleyel.at.wizcore;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class EditPlayers extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "MESSAGE";
    private ListView obj;
    DBHelper mydb;
    Button addPlayerDB;

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

        mydb = new DBHelper(this);
        ArrayList array_list = mydb.getAllContacts();
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, array_list);

        final ArrayList<Integer> array_ids = mydb.getAllIds();

        for (Object temp:array_list){
           Log.i("a","arraylist: "+temp.toString());
        }
        for (Object temp:array_ids){
           Log.i("a","arrayids: "+temp.toString());
        }

        obj = (ListView) findViewById(R.id.lv_players);
        obj.setAdapter(arrayAdapter);
        obj.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                int id_To_Search = array_ids.get(arg2);
                Log.i("a","ITEM LV: "+arg0.getId()+" "+arg1.getId()+" "+arg2+" "+arg3);
                Log.i("a","id: " +id_To_Search);

                Bundle dataBundle = new Bundle();
                dataBundle.putInt("id", id_To_Search);

                Intent intent = new Intent(getApplicationContext(), DisplayPlayer.class);

                intent.putExtras(dataBundle);
                startActivity(intent);
            }
        });

        addPlayerDB = (Button) findViewById(R.id.btn_addPlayerDB);
        addPlayerDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DisplayPlayer.class);
                Bundle dataBundle = new Bundle();
                dataBundle.putInt("id", 0);
                intent.putExtras(dataBundle);
                startActivity(intent);
            }
        });
    }

}
