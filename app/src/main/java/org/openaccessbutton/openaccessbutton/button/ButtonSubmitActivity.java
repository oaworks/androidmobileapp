package org.openaccessbutton.openaccessbutton.button;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.openaccessbutton.openaccessbutton.MainActivity;
import org.openaccessbutton.openaccessbutton.R;
import org.openaccessbutton.openaccessbutton.api.API;

/**
 * Submit paywalled articles to the OAB
 */
public class ButtonSubmitActivity extends Activity {
    private double mLatitude;
    private double mLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button_submit);

        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        // If we've been launched with the right intent, show the paywall form
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleShare(intent);
            }
        } else {
            // Redirect back to MainActivity
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }

        setupButton();
    }

    void handleShare(Intent intent) {
        // Preset the URL in the form from the intent data
        String url = intent.getStringExtra(Intent.EXTRA_TEXT);
        EditText urlView = (EditText) findViewById(R.id.articleUrl);
        urlView.setText(url);

        // Attempt to get the last known location from Android. If we can get it, prefill that
        // into the form
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            mLatitude = location.getLatitude();
            mLongitude = location.getLongitude();

            ((EditText) findViewById(R.id.location)).setText(Double.toString(mLatitude) + ", " + Double.toString(mLongitude));
        }
    }

    // Handle button submits by posting data to the OAB API
    protected void setupButton() {
        Button button = (Button) findViewById(R.id.buttonButton);

        final Context context = this;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = ((EditText) findViewById(R.id.articleUrl)).getText().toString();
                String location = ((EditText) findViewById(R.id.location)).getText().toString();
                String doi = ((EditText) findViewById(R.id.doi)).getText().toString();
                String description = ((EditText) findViewById(R.id.description)).getText().toString();
                String usecase = ((EditText) findViewById(R.id.usecase)).getText().toString();

                API.blockedRequest(new API.Callback() {
                    @Override
                    public void onComplete() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, getString(R.string.buttonSubmitted), Toast.LENGTH_LONG).show();
                                finish();
                            }
                        });
                    }
                }, context, url, location, doi, description, usecase);

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.button_submit, menu);
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
