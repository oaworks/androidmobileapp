package org.openaccessbutton.openaccessbutton.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import org.openaccessbutton.openaccessbutton.MainActivity;
import org.openaccessbutton.openaccessbutton.R;

public class MenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Bind our buttons to launch the relevant things when clicked
        bindFragmentLaunchingButton(R.id.doResearchButton, 2, this);
        bindFragmentLaunchingButton(R.id.infoHubButton, 0, this);
        bindFragmentLaunchingButton(R.id.mapButton, 3, this);
        bindFragmentLaunchingButton(R.id.moreOnAppButton, 1, this);
    }

    /**
     * Adds a click listener to a button that launches MainActivity and opens a specific fragment
     * whenever the button is pressed
     */
    protected void bindFragmentLaunchingButton(int buttonId, final int fragmentId, final Context context) {
        Button btn = (Button) findViewById(buttonId);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent k = new Intent(context, MainActivity.class);
                k.putExtra("fragmentNo", fragmentId);
                startActivity(k);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}