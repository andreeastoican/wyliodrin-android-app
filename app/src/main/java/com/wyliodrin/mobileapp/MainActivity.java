package com.wyliodrin.mobileapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wyliodrin.mobileapp.widgets.BarGraphWidget;
import com.wyliodrin.mobileapp.widgets.LineAndPointGraphWidget;
import com.wyliodrin.mobileapp.widgets.StepGraphWidget;

import java.util.Random;


public class MainActivity extends ActionBarActivity {

    Thread thread_bar;
    Thread thread_step;
    int x_bar_graph = 0;
    int x_step_graph = 0;
    Random rand = new Random();

    //private String[] widgetTitles;
    private DrawerLayout mDrawerLayout;
    //private ListView mDrawerList;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

       // widgetTitles = new String[] {"Moon", "Sun"};
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //mDrawerList = (ListView) findViewById(R.id.left_drawer);
        button1 = (Button) findViewById(R.id.button_elem1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletedTextView(R.id.text1);
                addStepGraph();
            }
        });

        button2 = (Button) findViewById(R.id.button_elem2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletedTextView(R.id.text2);
                addBarGraph();
            }
        });

        button3 = (Button) findViewById(R.id.button_elem3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletedTextView(R.id.text3);
                addLineGraph();
            }
        });

        button4 = (Button) findViewById(R.id.button_elem4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletedTextView(R.id.text4);

                Button myButton = new Button(MainActivity.this);
                myButton.setText("Push Me");
                

                LinearLayout layout = (LinearLayout) findViewById(R.id.graphContainer4);
                layout.getLayoutParams().height = 300;
                layout.getLayoutParams().width = 200;
                layout.requestLayout();
                layout.addView(myButton);

            }
        });

        // Set the adapter for the list view
        //mDrawerList.setAdapter(new ArrayAdapter<String>(this,
          //      R.layout.drawer_elem, widgetTitles));

        //mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

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

    public void deletedTextView(int id) {
        TextView textView = (TextView) findViewById(id);
        textView.setVisibility(View.GONE);
    }

    public void addStepGraph() {

        //final GraphWidget graph = new GraphWidget(this);
        //final BarGraphWidget graph = new BarGraphWidget(this);
        final StepGraphWidget graph = new StepGraphWidget(MainActivity.this);

        LinearLayout layout = (LinearLayout) findViewById(R.id.graphContainer);
        layout.getLayoutParams().height = 300;
        layout.getLayoutParams().width = 200;
        layout.requestLayout();
        layout.addView(graph);

        thread_step = new Thread() {
            @Override
            public void run() {
                super.run();

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
        thread_step.start();

    }

    public void addBarGraph() {

        //final GraphWidget graph = new GraphWidget(this);
        final BarGraphWidget graph = new BarGraphWidget(this);
        //final StepGraphWidget graph = new StepGraphWidget(MainActivity.this);

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
