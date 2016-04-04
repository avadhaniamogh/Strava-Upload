
package example.android.com.amogha.stravaauthentication;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.RelativeLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by AMOGH A on 31-Mar-16.
 */
public class StravaUploadGPXTask extends AsyncTask<String, Integer, String> {

    private MainActivity mActivity;
    private static final MediaType MEDIA_TYPE_GPX = MediaType.parse("application/gpx+xml");
    private final OkHttpClient client = new OkHttpClient();

    public StravaUploadGPXTask(MainActivity activity) {
        mActivity = activity;
    }

    @Override
    protected String doInBackground(String... params) {
        File sdCard = Environment.getExternalStorageDirectory();
        String filePathName = sdCard.getAbsolutePath() + "/your/file/name";// Your file path name
        String urlString = "https://www.strava.com/api/v3/uploads?activity_type=ride&data_type=gpx";
        String result = "";
        String access_token = params[0];
        File file = new File(filePathName);
        try {
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", "sample.gpx",
                            RequestBody.create(MEDIA_TYPE_GPX, file))
                    .build();

            Request request = new Request.Builder()
                    .header("Authorization", "Bearer " + access_token)
                    .url(urlString)
                    .post(requestBody)
                    .build();

            Response response = client.newCall(request).execute();
            if (!response.isSuccessful())
                throw new IOException("Unexpected code " + response);

            result = response.body().string();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        String id = parseResult(result);
    }

    private String parseResult(String result) {
        String id = "";
        try {
            JSONObject jsonObject = new JSONObject(result);
            if (jsonObject.has("id")) {
                id = jsonObject.getString("id");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return id;
    }
}
