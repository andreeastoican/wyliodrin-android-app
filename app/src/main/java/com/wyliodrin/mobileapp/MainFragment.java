package com.wyliodrin.mobileapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.wyliodrin.mobileapp.api.WylioBoard;
import com.wyliodrin.mobileapp.widgets.StepGraphWidget;

import java.util.List;
import java.util.Random;

public class MainFragment extends Fragment {

    Thread t;
    int x = 0;
    Random rand = new Random();


    public MainFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_main, container, false);

        // Simple Message
        final WylioBoard board = new WylioBoard("250a7843-9eda-4a92-942d-dba0dfe3b0a0b836cb66-0ad1-46d6-b6ba-458781a38ca08ced6536-0a8a-49b0-bbf3-b072631d0ccf", "andreea.stoican.5_random@wyliodrin.com");

        //final WylioBoard board = new WylioBoard("b883db27-a072-4481-aff2-2e5297d781c80a6febc0-6263-4118-9eeb-2fbb0530585b46a813ed-7eda-49b4-be5e-4a319c6c0da13a155792-fc2e-413f-82d9-9728ca5b829a");

        Button sendButtonOn = (Button) rootView.findViewById(R.id.buttonon);
        sendButtonOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                board.sendMessage("ledon", "ledon");
            }
        });

        Button sendButtonOff = (Button) rootView.findViewById(R.id.buttonoff);
        sendButtonOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                board.sendMessage("ledoff", "ledoff");
            }
        });

        return rootView;
    }

    public void addGraph() {

        //final GraphWidget graph = new GraphWidget(this);
        //final BarGraphWidget graph = new BarGraphWidget(this);
        final StepGraphWidget graph = new StepGraphWidget(getActivity());

        LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.graphContainer);
        layout.getLayoutParams().height = 300;
        layout.getLayoutParams().width = 200;
        layout.requestLayout();
        layout.addView(graph);

        t = new Thread() {
            @Override
            public void run() {
                super.run();

                while(true) {
                    x++;
                    double y = rand.nextDouble();

                    graph.addPoint(x, y);

                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();

    }

}
