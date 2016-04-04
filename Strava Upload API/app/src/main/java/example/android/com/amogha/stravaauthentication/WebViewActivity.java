
package example.android.com.amogha.stravaauthentication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends AppCompatActivity {

    String urlString = "https://www.strava.com/oauth/authorize?client_id="
            + MainActivity.CLIENT_ID
            + "&response_type=code&redirect_uri=http://xero.bike&scope=write&state=mystate&approval_prompt=force";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WebView webView = new WebView(this);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("http://xero.bike")) {
                    if (url.contains("error=access_denied")) {

                    } else {
                        if (url.contains("code=")) {
                            String code = url.substring(url.indexOf("code=") + 5);
                            Log.d("Strava Auth Result", "Strava Auth code = " + code);
                            Intent intent = new Intent();
                            intent.putExtra("Code", code);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }
                }
                view.loadUrl(url);
                return true;
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.loadUrl(urlString);
        setContentView(webView);

    }
}
