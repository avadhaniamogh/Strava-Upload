
package example.android.com.amogha.stravaauthentication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private Button login;
    public String stravaCode;
    public static int CLIENT_ID;// Your Client id
    public static String CLIENT_SECRET = "";//Your Client Secret
    String access_token = null;
    String id = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        login = (Button) findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                startActivityForResult(intent, 999);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 999) {
            if (resultCode == RESULT_OK) {
                stravaCode = data.getStringExtra("Code");
                Log.d("Strava Auth Result", "Strava Auth code = " + stravaCode);
                fetchStravaAccessToken(stravaCode);
            }
        }

    }

    private void fetchStravaAccessToken(String code) {
        StravaAuthenticationAccessTokenGetTask task = new StravaAuthenticationAccessTokenGetTask(
                this, MainActivity.this);
        String[] params = new String[1];
        params[0] = code;
        task.execute(params);
    }

    public void setStravaAccessToken(String token) {
        access_token = token;
        Log.d("StravaAuth", "Strava access token: " + access_token);
        StravaUploadTask uploadTask = new StravaUploadTask(MainActivity.this);
        String[] params = new String[1];
        params[0] = access_token;
        uploadTask.execute(params);
    }

    public void setAthlete(JSONObject object) {
        JSONObject athlete = object;
        Log.d("StravaAuth", "Strava athlete: " + athlete);
    }

    public void setId(String id) {
        this.id = id;
    }
}
