package com.wyliodrin.mobileapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity {

    private SharedPreferences shPref;
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
        final ListView dashboardList = (ListView) findViewById(R.id.dashboard_list);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        String boards = shPref.getString("boards", "");
        JSONArray boardList;
        try {
            boardList = new JSONArray(boards);
        } catch (JSONException e) {
            boardList = new JSONArray();
//            e.printStackTrace();
        }

        for (int i = 0; i < boardList.length(); i++) {
            try {
                JSONObject board = boardList.getJSONObject(i);
                adapter.add(board.optString("name", ""));
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
//                e.printStackTrace();
            }
        }

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

        dashboardList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, final View view, final int i, long l) {

                new AlertDialog.Builder(MainActivity.this).setTitle("Remove dashboard?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i1) {

                                String boards = shPref.getString("boards", "");
                                JSONArray boardList;
                                try {
                                    boardList = new JSONArray(boards);
                                } catch (JSONException e) {
                                    boardList = new JSONArray();
                                    e.printStackTrace();
                                }


                                Object toRemove = adapter.getItem(i);
                                adapter.remove((String) toRemove);
                                adapter.notifyDataSetChanged();

                                String name = "";
                                List<JSONObject> list = new ArrayList<JSONObject>();
                                Log.d("befeore for",boardList.length()+"");
                                for (int pos = 0; pos < boardList.length(); pos++) {
                                    try {
                                        if (pos != i) {
                                            list.add(boardList.getJSONObject(pos));
                                        } else {
                                            name = boardList.getJSONObject(pos).optString("name");
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                Log.d("after for", list.size()+"");
                                JSONArray jsArray = new JSONArray(list);
                                shPref.edit().putString("boards", jsArray.toString()).commit();
                                dashboardList.setAdapter(adapter);

                                Toast.makeText(MainActivity.this, "Dashboard " + name + " removed", Toast.LENGTH_LONG).show();
                            }
                        }).setNegativeButton("No", null).show();

                return true;
            }
        });

        dashboardList.setAdapter(adapter);

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
