package com.example.smqpr;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.smqpr.chenxhub.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private final OkHttpClient client = new OkHttpClient();
    private TextView[] textViews = new TextView[12];
    private ImageView imageView;
    private Button followers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void run() {
        String login = getIntent().getStringExtra("login");
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        Log.d(TAG, login);
        Request request = new Request.Builder()
                .url("https://api.github.com/users/" + login)
                .build();

        textViews[0] = findViewById(R.id.name);
        textViews[1] = findViewById(R.id.shortUrl);
        textViews[2] = findViewById(R.id.status);
        textViews[3] = findViewById(R.id.gitHub);
        textViews[4] = findViewById(R.id.city);
        textViews[5] = findViewById(R.id.mail);
        textViews[6] = findViewById(R.id.site);
        textViews[7] = findViewById(R.id.overview);
        textViews[8] = findViewById(R.id.repositories);
        textViews[9] = findViewById(R.id.start);
        followers = findViewById(R.id.followers);
        textViews[11] = findViewById(R.id.following);

        imageView = findViewById(R.id.avatar);

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (final ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);

                    final JSONObject object = new JSONObject(responseBody.string());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                textViews[0].setText(object.getString("name"));
                                textViews[1].setText(object.getString("login"));
                                textViews[2].setText(object.getString("bio"));
                                if (object.getString("site_admin").equals("false")) {
                                    textViews[3].setText("user");
                                }
                                else textViews[3].setText("admin");
                                textViews[4].setText(object.getString("location"));
                                textViews[5].setText(object.getString("email"));
                                textViews[6].setText(object.getString("blog"));
                                textViews[8].setText("repositories " + object.getString("public_repos"));
                                followers.setText("followers " + object.getString("followers"));
                                textViews[11].setText("following " + object.getString("following"));

                                Glide.with(MainActivity.this)
                                        .load(object.getString("avatar_url"))
                                        .into(imageView);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressBar.setVisibility(ProgressBar.INVISIBLE);
            }

        });
    }
}
