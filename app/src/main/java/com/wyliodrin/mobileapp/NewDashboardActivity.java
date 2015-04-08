package com.wyliodrin.mobileapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.wyliodrin.mobileapp.widgets.SimpleButton;
import com.wyliodrin.mobileapp.widgets.GraphWidget;
import com.wyliodrin.mobileapp.widgets.Thermometer;

import java.util.Random;

public class NewDashboardActivity extends FragmentActivity {
    Random rand = new Random();
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

        shPref = getSharedPreferences("dashboard", MODE_PRIVATE);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        addThermometerButton = (Button) findViewById(R.id.add_thermometer_button);
        addThermometerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thermometer.showAddDialog(NewDashboardActivity.this, (LinearLayout) findViewById(R.id.widgetsContainer), widgetLongClick);
                mDrawerLayout.closeDrawers();
            }
        });

        addStepGraphButton = (Button) findViewById(R.id.add_step_graph_button);
        addStepGraphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addStepGraph();
                mDrawerLayout.closeDrawers();
            }
        });

        addBarGraphButton = (Button) findViewById(R.id.add_bar_graph_button);
        addBarGraphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBarGraph();
                mDrawerLayout.closeDrawers();
            }
        });

        addLineGraphButton = (Button) findViewById(R.id.add_line_graph_button);
        addLineGraphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addLineGraph();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void addStepGraph() {
        GraphWidget.showAddDialog(NewDashboardActivity.this, (LinearLayout) findViewById(R.id.widgetsContainer), widgetLongClick, GraphWidget.GraphType.StepGraph);
    }

    public void addBarGraph() {
        GraphWidget.showAddDialog(NewDashboardActivity.this, (LinearLayout) findViewById(R.id.widgetsContainer), widgetLongClick, GraphWidget.GraphType.BarGraph);
    }

    public void addLineGraph() {
        GraphWidget.showAddDialog(NewDashboardActivity.this, (LinearLayout) findViewById(R.id.widgetsContainer), widgetLongClick, GraphWidget.GraphType.LineGraph);
    }
}
