package com.example.smqpr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.smqpr.chenxhub.R;

import okhttp3.OkHttpClient;

public class StartActivity extends AppCompatActivity {

    private static final String TAG = StartActivity.class.getSimpleName();
    private final OkHttpClient client = new OkHttpClient();
    private EditText login;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        button = findViewById(R.id.button);
        login = findViewById(R.id.login);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                Log.d(TAG, String.valueOf(login.getText()));
                intent.putExtra("login", String.valueOf(login.getText()));
                startActivity(intent);
            }
        });
    }
}
