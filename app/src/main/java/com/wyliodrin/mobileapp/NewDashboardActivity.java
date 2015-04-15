package com.wyliodrin.mobileapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.wyliodrin.mobileapp.widgets.SimpleButton;
import com.wyliodrin.mobileapp.widgets.GraphWidget;
import com.wyliodrin.mobileapp.widgets.Thermometer;
import com.wyliodrin.mobileapp.widgets.Widget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

public class NewDashboardActivity extends FragmentActivity {
    Random rand = new Random();
    ArrayList<Widget> objects;
    public SharedPreferences shPref;

    private DrawerLayout mDrawerLayout;

    private Button addStepGraphButton;
    private Button addLineGraphButton;
    private Button addBarGraphButton;
    private Button addSimpleButton;
    private Button addThermometerButton;

    private View.OnLongClickListener widgetLongClick = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(final View view) {

            new AlertDialog.Builder(NewDashboardActivity.this).setTitle("Remove widget?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            LinearLayout layout = (LinearLayout) findViewById(R.id.widgetsContainer);
                            layout.removeView(view);

                            // TODO remove from list
                        }
                    }).setNegativeButton("No", null).show();

            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_dashboard);

        objects = new ArrayList<Widget>();
        shPref = getSharedPreferences("dashboard", MODE_PRIVATE);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        addThermometerButton = (Button) findViewById(R.id.add_thermometer_button);
        addThermometerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thermometer.showAddDialog(NewDashboardActivity.this, (LinearLayout) findViewById(R.id.widgetsContainer), widgetLongClick, objects);
                mDrawerLayout.closeDrawers();
            }
        });

        addStepGraphButton = (Button) findViewById(R.id.add_step_graph_button);
        addStepGraphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GraphWidget.showAddDialog(NewDashboardActivity.this, (LinearLayout) findViewById(R.id.widgetsContainer), widgetLongClick, GraphWidget.GraphType.StepGraph);
                mDrawerLayout.closeDrawers();
            }
        });

        addBarGraphButton = (Button) findViewById(R.id.add_bar_graph_button);
        addBarGraphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GraphWidget.showAddDialog(NewDashboardActivity.this, (LinearLayout) findViewById(R.id.widgetsContainer), widgetLongClick, GraphWidget.GraphType.BarGraph);
                mDrawerLayout.closeDrawers();
            }
        });

        addLineGraphButton = (Button) findViewById(R.id.add_line_graph_button);
        addLineGraphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GraphWidget.showAddDialog(NewDashboardActivity.this, (LinearLayout) findViewById(R.id.widgetsContainer), widgetLongClick, GraphWidget.GraphType.LineGraph);
                mDrawerLayout.closeDrawers();
            }
        });

        addSimpleButton = (Button) findViewById(R.id.add_simple_button);
        addSimpleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SimpleButton.showAddDialog(NewDashboardActivity.this, (LinearLayout) findViewById(R.id.widgetsContainer), widgetLongClick);
                mDrawerLayout.closeDrawers();

            }
        });

        Fragment fragment = new MainFragment();
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment).commit();

            mDrawerLayout.closeDrawer((LinearLayout) findViewById(R.id.drawerLayout));
        }

    }

    public void saveBoard(String name) {
        JSONArray array = new JSONArray();

        for(Widget widget : objects) {
            array.put(widget.toJson());
        }

        JSONObject obj = new JSONObject();
        try {
            obj.put("name", name);
            obj.put("objects", array);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String boards = shPref.getString("boards", "");

        JSONArray list = null;
        try {
            list = new JSONArray(boards);
        } catch (JSONException e) {
            list = new JSONArray();
            e.printStackTrace();
        }
        list.put(obj);
        shPref.edit().putString("boards", list.toString()).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.save_board:

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NewDashboardActivity.this);
                alertDialogBuilder.setTitle("Dashboard name");

                LayoutInflater inflater = LayoutInflater.from(NewDashboardActivity.this);
                final View alert_dialog_xml = inflater.inflate(R.layout.alert_dialog_dashboard_name, null);
                alertDialogBuilder.setView(alert_dialog_xml);

                alertDialogBuilder
                        .setMessage("Choose dashboard name")
                        .setPositiveButton("Save", null)
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

                    @Override
                    public void onShow(DialogInterface dialogInterface) {

                        Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        b.setOnClickListener(new View.OnClickListener() {

                            public void onClick(View v) {

                                EditText nameEditText = (EditText) alert_dialog_xml.findViewById(R.id.name);
                                String name = null;
                                if (nameEditText != null) {
                                    name = nameEditText.getText().toString();

                                    if (name.isEmpty())
                                        nameEditText.setError("Name is required");
                                }

                                if (!name.isEmpty()) {
                                    saveBoard(name);
                                    Toast.makeText(NewDashboardActivity.this, "Dashboard " + name + " saved", Toast.LENGTH_LONG).show();
                                    alertDialog.dismiss();
                                }
                            }
                        });
                    }
                });

                alertDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
