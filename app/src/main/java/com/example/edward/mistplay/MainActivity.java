package com.example.edward.mistplay;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.edward.mistplay.HttpUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void searchGame(View view) {
        final TextView tv = (TextView) findViewById(R.id.searchgame_name);
        final TextView gamesList = (TextView) findViewById(R.id.textViewGames);
        HttpUtils.post("/search?term=" + tv.getText().toString() + "&index=10", new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                tv.setText("");
                gamesList.setText("Title | Category | Rating \n");
                JSONArray jsonGamesArray = null;
                try {
                    jsonGamesArray = response.getJSONObject("data").getJSONArray("games");
                    for (int i = 0; i < jsonGamesArray.length(); i++) {
                        try {
                            String gameTitle = jsonGamesArray.getJSONObject(i).getString("tile");
                            String gameCategory = jsonGamesArray.getJSONObject(i).getString("category");
                            String gameRating = jsonGamesArray.getJSONObject(i).getString("rating");
                            gamesList.append(gameTitle + " | ");
                            gamesList.append(gameCategory + " | ");
                            gamesList.append(gameRating + "\n");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            }
        });
    }
}
