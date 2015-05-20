package com.wyliodrin.mobileapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wyliodrin.mobileapp.widgets.SimpleButton;
import com.wyliodrin.mobileapp.widgets.GraphWidget;
import com.wyliodrin.mobileapp.widgets.SimpleSeekBar;
import com.wyliodrin.mobileapp.widgets.SimpleToggleButton;
import com.wyliodrin.mobileapp.widgets.Speedometer;
import com.wyliodrin.mobileapp.widgets.Thermometer;
import com.wyliodrin.mobileapp.widgets.Widget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewDashboardActivity extends FragmentActivity {
    ArrayList<Widget> objects;
    public SharedPreferences shPref;

    private DrawerLayout mDrawerLayout;

    private JSONObject currentBoard;
    private int boardId = -1;

    private View.OnLongClickListener widgetLongClick = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(final View view) {

            new AlertDialog.Builder(NewDashboardActivity.this).setTitle("Remove widget?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            LinearLayout layout = (LinearLayout) findViewById(R.id.widgetsContainer);
                            layout.removeView(view);
                            objects.remove(view);
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

        Button addThermometerButton = (Button) findViewById(R.id.add_thermometer_button);
        addThermometerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thermometer.showAddDialog(NewDashboardActivity.this, (LinearLayout) findViewById(R.id.widgetsContainer),
                        widgetLongClick, objects);
                mDrawerLayout.closeDrawers();
            }
        });

        Button addStepGraphButton = (Button) findViewById(R.id.add_step_graph_button);
        addStepGraphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GraphWidget.showAddDialog(NewDashboardActivity.this, (LinearLayout) findViewById(R.id.widgetsContainer),
                        widgetLongClick, GraphWidget.GraphType.StepGraph, objects);
                mDrawerLayout.closeDrawers();
            }
        });

        Button addBarGraphButton = (Button) findViewById(R.id.add_bar_graph_button);
        addBarGraphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GraphWidget.showAddDialog(NewDashboardActivity.this, (LinearLayout) findViewById(R.id.widgetsContainer),
                        widgetLongClick, GraphWidget.GraphType.BarGraph,objects);
                mDrawerLayout.closeDrawers();
            }
        });

        Button addLineGraphButton = (Button) findViewById(R.id.add_line_graph_button);
        addLineGraphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GraphWidget.showAddDialog(NewDashboardActivity.this, (LinearLayout) findViewById(R.id.widgetsContainer),
                        widgetLongClick, GraphWidget.GraphType.LineGraph, objects);
                mDrawerLayout.closeDrawers();
            }
        });

        Button addSimpleButton = (Button) findViewById(R.id.add_simple_button);
        addSimpleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SimpleButton.showAddDialog(NewDashboardActivity.this, (LinearLayout) findViewById(R.id.widgetsContainer),
                        widgetLongClick, objects);
                mDrawerLayout.closeDrawers();

            }
        });

        Button addSimpleToggleButton = (Button) findViewById(R.id.add_simple_toggle_button);
        addSimpleToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SimpleToggleButton.showAddDialog(NewDashboardActivity.this, (LinearLayout) findViewById(R.id.widgetsContainer),
                        widgetLongClick, objects);
                mDrawerLayout.closeDrawers();

            }
        });

        Button addSeekBar = (Button) findViewById(R.id.add_simple_seek_bar);
        addSeekBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SimpleSeekBar.showAddDialog(NewDashboardActivity.this, (LinearLayout) findViewById(R.id.widgetsContainer),
                        widgetLongClick, objects);
                mDrawerLayout.closeDrawers();

            }
        });

        Button addSpeedometer = (Button) findViewById(R.id.add_speedometer);
        addSpeedometer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Speedometer.showAddDialog(NewDashboardActivity.this, (LinearLayout) findViewById(R.id.widgetsContainer),
                        widgetLongClick, objects);
                mDrawerLayout.closeDrawers();
            }
        });

        Intent intent = getIntent();
        String boardString = intent.getStringExtra("board");
        boardId = intent.getIntExtra("board_id", -1);

        if(boardString != null && boardId != -1) {
            try {
                currentBoard = new JSONObject(boardString);

                JSONArray widgets = currentBoard.getJSONArray("objects");

                for (int i = 0; i < widgets.length(); i++) {
                    JSONObject widget = widgets.getJSONObject(i);
                    switch (widget.optInt("type", Widget.TYPE_NONE)) {
                        case Widget.TYPE_THERMOMETER:
                            Thermometer.addToBoard(this, (LinearLayout) findViewById(R.id.widgetsContainer), widgetLongClick, objects,
                                    widget.optInt("width", 200), widget.optInt("height", 300),
                                    widget.optDouble("maxDegree", 70), widget.optDouble("minDegree", -20));
                            break;
                        case Widget.TYPE_GRAPH:
                            GraphWidget.GraphType type = null;
                            if (widget.opt("graph_type").equals("StepGraph")) {
                                type = GraphWidget.GraphType.StepGraph;
                            } else if (widget.opt("graph_type").equals("BarGraph")) {
                                type = GraphWidget.GraphType.BarGraph;
                            } else if (widget.opt("graph_type").equals("LineGraph")) {
                                type = GraphWidget.GraphType.LineGraph;
                            }

                            GraphWidget.addToBoard(this, (LinearLayout) findViewById(R.id.widgetsContainer),widgetLongClick,
                                    objects, widget.optInt("width", 200),widget.optInt("height", 300), widget.optString("title", "Step"),
                                    type);
                            break;
                        case Widget.TYPE_BUTTON:
                            SimpleButton.addToBoard(this, (LinearLayout) findViewById(R.id.widgetsContainer) ,widgetLongClick,
                                    objects, widget.optInt("width"), widget.optInt("height"), widget.optString("text_button"));
                            break;
                        case Widget.TYPE_TOGGLE_BUTTON:
                            SimpleToggleButton.addToBoard(this, (LinearLayout) findViewById(R.id.widgetsContainer) ,widgetLongClick,
                                    objects, widget.optInt("width"), widget.optInt("height"), widget.optString("text_button_on"), widget.optString("text_button_off"));
                            break;
                        case Widget.TYPE_SEEK_BAR:
                            SimpleSeekBar.addToBoard(this, (LinearLayout) findViewById(R.id.widgetsContainer) ,widgetLongClick,
                                objects, widget.optInt("width"), widget.optInt("max_value"));
                        case Widget.TYPE_SPEEDOMETER:
                            Speedometer.addToBoard(this, (LinearLayout) findViewById(R.id.widgetsContainer), widgetLongClick,
                                    objects, widget.optInt("diameter"), widget.optInt("min_value"), widget.optInt("max_value"));
                            break;
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
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

        JSONArray list;
        try {
            list = new JSONArray(boards);
        } catch (JSONException e) {
            list = new JSONArray();
            e.printStackTrace();
        }

        if(boardId != -1) {
            try {
                list.put(boardId, obj);
            } catch (JSONException e) {
            }
        } else {
            list.put(obj);
        }
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
                EditText nameEditText = (EditText) alert_dialog_xml.findViewById(R.id.name);

                // daca se salveaza un bord existent
                if (boardId != -1) {
                    nameEditText.setText(currentBoard.optString("name"), TextView.BufferType.EDITABLE);
                }
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
