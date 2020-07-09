package com.example.smqpr.tictactoes;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private Button[][] buttons = new Button[3][3];
    private Button restartButton;
    private int[][] marks = new int[3][3];

    private boolean turn = true;
    private boolean win = false;
    private boolean draw = false;

    private Button finish;
    private Button results;
    private int playerId;
    private TextView p1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        p1 = findViewById(R.id.p1);

        finish = findViewById(R.id.finish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        results = findViewById(R.id.results);
        results.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainActivity.this, ScoreActivity.class);
                startActivity(intent1);
            }
        });

        Intent intent = getIntent();

        playerId = intent.getIntExtra("playerId", -1);

        if (playerId == -1) {
            Toast.makeText(this, "Some error occurred, please repeat after time", Toast.LENGTH_SHORT).show();
            finish();
        }

        AuthDao dao = AuthDao.getInstance(new DBHelper(this));

        Log.d(TAG, "onCreate: " + dao.select());

        String n1 = dao.getUser(playerId).getPlayer();
        p1.setText(n1);

        buttons[0][0] = findViewById(R.id.button1);
        buttons[0][1] = findViewById(R.id.button4);
        buttons[0][2] = findViewById(R.id.button7);
        buttons[1][0] = findViewById(R.id.button2);
        buttons[1][1] = findViewById(R.id.button5);
        buttons[1][2] = findViewById(R.id.button8);
        buttons[2][0] = findViewById(R.id.button3);
        buttons[2][1] = findViewById(R.id.button6);
        buttons[2][2] = findViewById(R.id.button9);

        restartButton = findViewById(R.id.restartButton);
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });
        turns();
    }

    private void refresh() {
        for (int i = 0; i < marks.length; i++) {
            for (int j = 0; j < marks.length; j++) {
                marks[i][j] = 0;
                buttons[i][j].setText(" ");
                buttons[i][j].setEnabled(true);
                buttons[i][j].setBackground(getResources().getDrawable(R.color.trans));
                turn = true;
            }

        }
    }

    public boolean isTurnOfX() {
        return turn;
    }

    public void turns() {
        final AuthDao dao = AuthDao.getInstance(new DBHelper(this));
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                final int finalI = i;
                final int finalJ = j;
                Button currentButton = buttons[i][j];
                currentButton.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onClick(View v) {
                        Button e = (Button) v;
                        String currentValue;
                        if (isTurnOfX()) {
                            marks[finalI][finalJ] = 1;
                            currentValue = " ";
                            buttons[finalI][finalJ].setBackground(getResources().getDrawable(R.drawable.cross));
                            turn = false;
                        } else {
                            marks[finalI][finalJ] = -1;
                            currentValue = " ";
                            buttons[finalI][finalJ].setBackground(getResources().getDrawable(R.drawable.nought));
                            turn = true;
                        }
                        e.setText(currentValue);
                        e.setEnabled(false);
                        if (winCondition()) {
                            if (isWinX()) {
                                Toast.makeText(MainActivity.this, "Player X has Won", Toast.LENGTH_SHORT).show();
                                dao.increaseScore(playerId);
                            } else {
                                Toast.makeText(MainActivity.this, "Player O has Won", Toast.LENGTH_SHORT).show();
                                dao.increaseScore(1);
                            }
                        }
                        if (drawCondition()) {
                            Toast.makeText(MainActivity.this, "It is Draw", Toast.LENGTH_SHORT).show();
                        }

                    }

                });
            }
        }
    }

    public boolean winCondition() {
        if (marks[0][0] == marks[1][1] && marks[1][1] == marks[2][2] && (marks[0][0] == 1 || marks[0][0] == -1))
            win = true;

        else if (marks[0][0] == marks[0][1] && marks[0][1] == marks[0][2] && (marks[0][0] == 1 || marks[0][0] == -1))
            win = true;

        else if (marks[1][0] == marks[1][1] && marks[1][1] == marks[1][2] && (marks[1][0] == 1 || marks[1][0] == -1))
            win = true;

        else if (marks[2][0] == marks[2][1] && marks[2][1] == marks[2][2] && (marks[2][0] == 1 || marks[2][0] == -1))
            win = true;

        else if (marks[0][0] == marks[1][0] && marks[1][0] == marks[2][0] && (marks[0][0] == 1 || marks[0][0] == -1))
            win = true;

        else if (marks[0][1] == marks[1][1] && marks[1][1] == marks[2][1] && (marks[0][1] == 1 || marks[0][1] == -1))
            win = true;

        else if (marks[0][2] == marks[1][2] && marks[1][2] == marks[2][2] && (marks[2][2] == 1 || marks[2][2] == -1))
            win = true;

        else if (marks[0][2] == marks[1][1] && marks[1][1] == marks[2][0] && (marks[0][2] == 1 || marks[0][2] == -1))
            win = true;

        else win = false;

        if (win) {
            for (int i = 0; i < buttons.length; i++) {
                for (int j = 0; j < buttons[i].length; j++) {
                    buttons[i][j].setEnabled(false);
                }

            }
        }

        return win;
    }

    public boolean drawCondition() {
        int counter = 0;//counts how many places are locked
        if (!winCondition()) {
            for (int i = 0; i < marks.length; i++) {
                for (int j = 0; j < marks[i].length; j++) {
                    if (marks[i][j] == -1 || marks[i][j] == 1) counter++;
                }
            }
        }
        if (counter == 9) draw = true;
        else draw = false;
        return draw;
    }

    public boolean isWinX() {
        if (marks[0][0] == marks[1][1] && marks[1][1] == marks[2][2] && marks[0][0] == 1)
            return true;

        else if (marks[0][0] == marks[0][1] && marks[0][1] == marks[0][2] && marks[0][0] == 1)
            return true;

        else if (marks[1][0] == marks[1][1] && marks[1][1] == marks[1][2] && marks[1][0] == 1)
            return true;

        else if (marks[2][0] == marks[2][1] && marks[2][1] == marks[2][2] && marks[2][0] == 1)
            return true;

        else if (marks[0][0] == marks[1][0] && marks[1][0] == marks[2][0] && marks[0][0] == 1)
            return true;

        else if (marks[0][1] == marks[1][1] && marks[1][1] == marks[2][1] && marks[0][1] == 1)
            return true;

        else if (marks[0][2] == marks[1][2] && marks[1][2] == marks[2][2] && marks[2][2] == 1)
            return true;

        else if (marks[0][2] == marks[1][1] && marks[1][1] == marks[2][0] && marks[0][2] == 1)
            return true;

        else return false;
    }


}
