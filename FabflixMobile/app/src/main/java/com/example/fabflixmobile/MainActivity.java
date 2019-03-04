package com.example.fabflixmobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.IOException;
import java.net.URL;
import java.security.cert.Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;

public class MainActivity extends AppCompatActivity {
    private CookieManager cookieManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void userLogin(View view) {
        //String username = ((EditText) findViewById(R.id.userText)).getText().toString();
        //String password = ((EditText) findViewById(R.id.passText)).getText().toString();
        String url = "https://10.0.2.2:8443/project1/api/login";

        final Intent goToSearchPage = new Intent(this, SearchActivity.class);

        final RequestQueue queue = NetworkManager.sharedManager(this).queue;

        final StringRequest loginRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("login.success", response);
//                        ((TextView) findViewById(R.id.httpResponse)).setText(response);
                        // Add the request to the RequestQueue.
//                        queue.add(afterLoginRequest);
                        startActivity(goToSearchPage);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("login.error", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                // Post request form data
                final Map<String, String> params = new HashMap<String, String>();
                String username = ((EditText) findViewById(R.id.userText)).getText().toString();
                String password = ((EditText) findViewById(R.id.passText)).getText().toString();


                params.put("username", username);
                params.put("password", password);

                return params;
            }
        };

        queue.add(loginRequest);
    }
}