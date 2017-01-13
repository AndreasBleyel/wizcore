package engelbergbleyel.at.wizcore;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class Launch extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        SharedPreferences sharedPref = this.getSharedPreferences("tut",Context.MODE_PRIVATE);
        boolean tut = sharedPref.getBoolean("tut",true);

        if (tut) {
            startActivity(new Intent(this, StartTutorial.class));
        } else {
            startActivity(new Intent(this, MainMenu.class));
        }
    }
}
