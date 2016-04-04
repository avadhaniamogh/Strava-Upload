
package example.android.com.amogha.stravaauthentication;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by AMOGH A on 29-Mar-16.
 */
public class StravaAuthenticationAccessTokenGetTask extends AsyncTask<String, Integer, String> {

    private Context mContext;
    private MainActivity mActivity;
    private JSONObject athlete;

    public StravaAuthenticationAccessTokenGetTask(Context context, MainActivity activity) {
        mContext = context;
        mActivity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        String code = params[0];
        String urlString = "https://www.strava.com/oauth/token?client_id=" + MainActivity.CLIENT_ID
                + "&client_secret=" + MainActivity.CLIENT_SECRET + "&code=" + code;

        StringBuilder result = new StringBuilder();

        URL url;
        HttpURLConnection connection = null;

        try {
            url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            int status = connection.getResponseCode();
            InputStream inputStream = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(
                    inputStream));
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

        } catch (java.io.IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return result.toString();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        String access_token = parseResult(result);
        mActivity.setStravaAccessToken(access_token);
        mActivity.setAthlete(athlete);
    }

    private String parseResult(String result) {
        String access_token = "";
        try {
            JSONObject jsonObject = new JSONObject(result);
            if (jsonObject.has("access_token")) {
                access_token = jsonObject.getString("access_token");
                if (jsonObject.has("athlete")) {
                    athlete = jsonObject.getJSONObject("athlete");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return access_token;
    }
}
