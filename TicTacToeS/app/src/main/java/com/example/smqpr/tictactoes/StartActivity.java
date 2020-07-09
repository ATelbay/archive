package com.example.smqpr.tictactoes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class StartActivity extends AppCompatActivity {

    private static final String TAG = StartActivity.class.getSimpleName();
    private Button start;
    private Button score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        final AuthDao dao = AuthDao.getInstance(new DBHelper(this));

        start = findViewById(R.id.start);

        start.setOnClickListener(new View.OnClickListener()


        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, MainActivity.class);

                final EditText p1 = findViewById(R.id.editText1);

                String n1 = p1.getText().toString();

                dao.insertIfNotExists(new User(n1, 0));

                Log.d(TAG, String.format("onClick: %s, %d", n1, dao.getId(n1)));

                intent.putExtra("playerId", dao.getId(n1));

                startActivity(intent);
            }
        });
    }
}
