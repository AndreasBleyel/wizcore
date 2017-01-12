package engelbergbleyel.at.wizcore;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class About extends AppCompatActivity {

    private final String EMAIL = "andy-b23@gmx.at";
    private final String SUBJECT = "WizCore ";
    private final String TEXT = "Hi Paula and Andreas,";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, getResources().getString(R.string.about_spend), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                /* Create the Intent */
                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

                /* Fill it with Data */
                emailIntent.setType("plain/text");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, EMAIL);
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, SUBJECT);
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, TEXT);

                /* Send it off to the Activity-Chooser */
                startActivity(Intent.createChooser(emailIntent, getResources().getString(R.string.about_text)));
            }
        });
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
