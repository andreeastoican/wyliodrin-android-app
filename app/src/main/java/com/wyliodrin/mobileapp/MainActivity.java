package com.wyliodrin.mobileapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.wyliodrin.mobileapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends ActionBarActivity {

    public SharedPreferences shPref;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton addButton = (ImageButton) findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewDashboardActivity.class);
                startActivity(intent);
            }
        });

        shPref = getSharedPreferences("dashboard", MODE_PRIVATE);

        updateBoardsList();
    }

    private void updateBoardsList() {
        ListView dashboardList = (ListView) findViewById(R.id.dashboard_list);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        String boards = shPref.getString("boards", "");
        JSONArray boardList = null;
        try {
            boardList = new JSONArray(boards);
        } catch (JSONException e) {
            boardList = new JSONArray();
            e.printStackTrace();
        }

        for (int i = 0; i < boardList.length(); i++) {
            try {
                JSONObject board = boardList.getJSONObject(i);
                adapter.add(board.optString("name", ""));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        dashboardList.setAdapter(adapter);

        final JSONArray finalBoardList = boardList;
        dashboardList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, NewDashboardActivity.class);

                try {
                    JSONObject board = finalBoardList.getJSONObject(i);
                    intent.putExtra("board", board.toString());
                    intent.putExtra("board_id", i);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateBoardsList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
