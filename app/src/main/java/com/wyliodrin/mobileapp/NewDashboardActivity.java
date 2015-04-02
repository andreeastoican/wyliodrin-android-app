package com.wyliodrin.mobileapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.wyliodrin.mobileapp.widgets.BarGraphWidget;
import com.wyliodrin.mobileapp.widgets.LineAndPointGraphWidget;
import com.wyliodrin.mobileapp.widgets.StepGraphWidget;
import com.wyliodrin.mobileapp.widgets.Thermometer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class NewDashboardActivity extends FragmentActivity {

    Thread thread_bar;
    Thread thread_step;
    int x_bar_graph = 0;
    int x_step_graph = 0;
    Random rand = new Random();
    static List<Button> addedButtons;
    static int i=0;

    //Thread-uri
    ArrayList<Thread> threadArrayList;

    static int i_stepG = 0;
    ArrayList<StepGraphWidget> stepGraphWidgetArrayList;

    private DrawerLayout mDrawerLayout;

    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private Button addThermometerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_dashboard);

        addedButtons = new ArrayList<Button>();
        threadArrayList = new ArrayList<Thread>();
        stepGraphWidgetArrayList = new ArrayList<StepGraphWidget>();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        addThermometerButton = (Button) findViewById(R.id.add_thermometer_button);
        addThermometerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // set the parameters

                ScrollView scroll = new ScrollView(getApplicationContext());
                scroll.setBackgroundColor(android.R.color.transparent);
                scroll.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NewDashboardActivity.this);
                alertDialogBuilder.setTitle("Choose the thermometer properties");

                LayoutInflater inflater= LayoutInflater.from(getApplicationContext());
                final View alert_dialog_xml =inflater.inflate(R.layout.alert_dialog_properties, null);
                alertDialogBuilder.setView(alert_dialog_xml);

                alertDialogBuilder.setNegativeButton("Done",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {

                        String width="";
                        String height="";
                        String minDegree="";
                        String maxDegree="";

                        EditText widthEditText = (EditText) alert_dialog_xml.findViewById(R.id.thermometer_width);
                        if(widthEditText != null) {
                            width = widthEditText.getText().toString();
                        }

                        EditText heightEditText = (EditText) alert_dialog_xml.findViewById(R.id.thermometer_height);
                        if (heightEditText != null) {
                            height = heightEditText.getText().toString();
                        }

                        EditText minDegreeEditText = (EditText) alert_dialog_xml.findViewById(R.id.thermometer_min);
                        if (minDegreeEditText != null) {
                            minDegree = minDegreeEditText.getText().toString();
                        }

                        EditText maxDegreeEditText = (EditText) alert_dialog_xml.findViewById(R.id.thermometer_max);
                        if(maxDegreeEditText != null) {
                            maxDegree = maxDegreeEditText.getText().toString();
                        }

                        dialog.cancel();

                        // add the thermometer
                        Thermometer thermometer = new Thermometer(getApplicationContext());
                        thermometer.setMax(Float.parseFloat(maxDegree));
                        thermometer.setMin(Float.parseFloat(minDegree));

                        thermometer.setLimits(Float.parseFloat(minDegree), Float.parseFloat(maxDegree));
                        thermometer.setValue(40);

                        LinearLayout layout = (LinearLayout) findViewById(R.id.thermometer_layout);
                        layout.addView(thermometer);

                    }
                });

                alertDialogBuilder.setNeutralButton("Cancel",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                mDrawerLayout.closeDrawers();

            }
        });

        button1 = (Button) findViewById(R.id.button_elem1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addStepGraph();
                mDrawerLayout.closeDrawers();
            }
        });

        button2 = (Button) findViewById(R.id.button_elem2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBarGraph();
                mDrawerLayout.closeDrawers();
            }
        });

        button3 = (Button) findViewById(R.id.button_elem3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addLineGraph();
                mDrawerLayout.closeDrawers();
            }
        });

        button4 = (Button) findViewById(R.id.button_elem4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Button myButton = new Button(NewDashboardActivity.this);
                myButton.setText("Push Me");
                addedButtons.add(i, myButton);

                LinearLayout layout = (LinearLayout) findViewById(R.id.graphContainer4);
                layout.getLayoutParams().height = 300;
                layout.getLayoutParams().width = 200;
                layout.requestLayout();
                layout.addView(addedButtons.get(i));

                mDrawerLayout.closeDrawers();

                if(i == 0) {
                    addedButtons.get(i).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getApplicationContext(), "Ai apasat pe buton 1",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                i++;

            }
        });

        button5 = (Button) findViewById(R.id.button_elem5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Button myButton = new Button(NewDashboardActivity.this);
                EditText editText = new EditText(NewDashboardActivity.this);

                LinearLayout layout = (LinearLayout) findViewById(R.id.graphContainer4);
                layout.getLayoutParams().height = 300;
                layout.getLayoutParams().width = 200;
                layout.requestLayout();
                layout.addView(editText);

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
        i=0;
        i_stepG=0;
    }

    public void addStepGraph() {
        final StepGraphWidget graph = new StepGraphWidget(NewDashboardActivity.this);
        stepGraphWidgetArrayList.add(i_stepG, graph);

        LinearLayout layout = (LinearLayout) findViewById(R.id.graphContainer1);
        //graph.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        layout.getLayoutParams().height = 300;
        layout.getLayoutParams().width = 200;
        layout.requestLayout();

        layout.addView(stepGraphWidgetArrayList.get(i_stepG));

        thread_step = new Thread() {
            @Override
            public void run() {
                super.run();

                x_step_graph = 0;
                while(true) {
                    x_step_graph++;
                    double y = rand.nextDouble();

                    graph.addPoint(x_step_graph, y);

                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        threadArrayList.add(i_stepG, thread_step);
        threadArrayList.get(i_stepG).start();
        //thread_step.start();
        i_stepG++;

    }

    public void addBarGraph() {

        final BarGraphWidget graph = new BarGraphWidget(this);

        LinearLayout layout = (LinearLayout) findViewById(R.id.graphContainer2);
        layout.getLayoutParams().height = 300;
        layout.getLayoutParams().width = 200;
        layout.requestLayout();
        layout.addView(graph);

        thread_bar = new Thread() {
            @Override
            public void run() {
                super.run();

                while(true) {
                    x_bar_graph++;
                    double y = rand.nextDouble();

                    graph.addPoint(x_bar_graph, y);

                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread_bar.start();

    }

    public void addLineGraph() {

        final LineAndPointGraphWidget graph = new LineAndPointGraphWidget(this);

        LinearLayout layout = (LinearLayout) findViewById(R.id.graphContainer3);
        layout.getLayoutParams().height = 300;
        layout.getLayoutParams().width = 200;
        layout.requestLayout();
        layout.addView(graph);

        thread_bar = new Thread() {
            @Override
            public void run() {
                super.run();

                while(true) {
                    x_bar_graph++;
                    double y = rand.nextDouble();

                    graph.addPoint(x_bar_graph, y);

                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread_bar.start();

    }
}
