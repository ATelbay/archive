package com.example.smqpr.a2hi_tech;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.DataPermission;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static long back_pressed;

    private ListView listView;
    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_sources:
                    setTitle("Test");
                    SourcesFragment sourcesfragment = new SourcesFragment();
                    loadFragment(sourcesfragment);
                    return true;
                case R.id.navigation_news:
                    setTitle("News");
                    Fragment fragment = new NewsFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_settings:
                    setTitle("My Account");
                    SettingsFragment settingsFragment = new SettingsFragment();
                    loadFragment(settingsFragment);
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        android.support.v4.app.FragmentTransaction testFragmentTransaction = getSupportFragmentManager().beginTransaction();
        testFragmentTransaction.replace(R.id.frame, fragment, "ChatFragment");
        testFragmentTransaction.commit();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DataQueryBuilder queryBuilder = DataQueryBuilder.create()
                .setPageSize(99);

        listView = findViewById(R.id.recyclerView);

        mTextMessage = findViewById(R.id.message);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        NewsFragment newsFragment = new NewsFragment();
        loadFragment(newsFragment);
        setTitle("Test");

        Backendless.Data.of( Post.class ).find( queryBuilder,
                new AsyncCallback<List<Post>>(){
                    @Override
                    public void handleResponse( List<Post> foundPosts )
                    {
                        Log.d("TAG###########", String.valueOf(foundPosts));
                        // the "foundPost" collection now contains instances of the Post class.
                        // each instance represents an object stored on the server.
                    }
                    @Override
                    public void handleFault( BackendlessFault fault )
                    {
                        // an error has occurred, the error code can be retrieved with fault.getCode()
                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_logout) {

            new AlertDialog.Builder(this)
                    .setMessage(R.string.action_logout)
                    .setPositiveButton("Да" , new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(MainActivity.this, "!!!!", Toast.LENGTH_SHORT).show();
                        }
                    }).
                    setNegativeButton("Нет" , new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(MainActivity.this, "!", Toast.LENGTH_SHORT).show();
                        }
                    }).create().show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
//            listView.setVisibility(View.VISIBLE);
//            mTextMessage.setVisibility(View.GONE);
            super.onBackPressed();
        } else {
            getFragmentManager().popBackStack();
        }

    }

}
