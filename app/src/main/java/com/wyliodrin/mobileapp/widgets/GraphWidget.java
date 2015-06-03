package com.wyliodrin.mobileapp.widgets;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.androidplot.util.PixelUtils;
import com.androidplot.xy.BarFormatter;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.StepFormatter;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.XYSeriesFormatter;
import com.androidplot.xy.XYStepMode;
import com.wyliodrin.mobileapp.R;
import com.wyliodrin.mobileapp.api.WylioMessage;


import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by andreea on 12/10/14.
 */
public class GraphWidget extends XYPlot implements InputDataWidget {

    private String title;
    private int width;
    private int height;
    private GraphType currentType;
    private String label;

    public static enum GraphType {
        StepGraph,
        BarGraph,
        LineGraph
    }

    static int x_bar_graph = 0;
    int maxNumberOfPoints = 10;

    private class GraphWidgetDataSeries implements XYSeries {

        List<Number> X;
        List<Number> Y;

        GraphWidgetDataSeries() {
            X = new ArrayList<Number>();
            Y = new ArrayList<Number>();
        }

        public void addPoint(double x, double y) {
            X.add(x);
            Y.add(y);
            if(X.size() > maxNumberOfPoints) {
                X.remove(0);
                Y.remove(0);
            }

            GraphWidget.this.redraw();
        }

        @Override
        public int size() {
            return X.size();
        }

        @Override
        public Number getX(int index) {
            return X.get(index);
        }

        @Override
        public Number getY(int index) {
            return Y.get(index);
        }

        @Override
        public String getTitle() {
            return "my series";
        }
    }

    private GraphWidgetDataSeries series;

    public GraphWidget(Context context, AttributeSet attrs, GraphType graphType) {
        super(context, attrs);

        series = new GraphWidgetDataSeries();
        XYSeriesFormatter formatter = null;

        if (graphType.equals(GraphType.StepGraph)) {
            StepFormatter stepFormatter = new StepFormatter(Color.argb(100, 0, 200, 0), Color.rgb(0, 80, 0));
            stepFormatter.getLinePaint().setStrokeWidth(3);
            stepFormatter.getLinePaint().setStrokeJoin(Paint.Join.ROUND);

            formatter = stepFormatter;
        } else if(graphType.equals(GraphType.BarGraph)) {
            BarFormatter barFormatter = new BarFormatter(Color.argb(100, 0, 200, 0), Color.rgb(0, 80, 0));
            barFormatter.getLinePaint().setStrokeWidth(3);
            barFormatter.getLinePaint().setStrokeJoin(Paint.Join.ROUND);

            formatter = barFormatter;
        } else if(graphType.equals(GraphType.LineGraph)) {
            LineAndPointFormatter lineFormatter = new LineAndPointFormatter(Color.rgb(0, 0, 200), null, null, null);
            lineFormatter.getLinePaint().setStrokeWidth(3);
            lineFormatter.getLinePaint().setStrokeJoin(Paint.Join.ROUND);
            formatter = lineFormatter;
        }


        setDomainStepMode(XYStepMode.SUBDIVIDE);
        setDomainStepValue(5);

        setRangeStepMode(XYStepMode.SUBDIVIDE);
        setRangeStepValue(10);

        setRangeValueFormat(new DecimalFormat("###.#"));

        // uncomment this line to freeze the range boundaries:
        setRangeBoundaries(-1, 2, BoundaryMode.FIXED);

        // create a dash effect for domain and range grid lines:
        DashPathEffect dashFx = new DashPathEffect(
                new float[] {PixelUtils.dpToPix(3), PixelUtils.dpToPix(3)}, 0);
        getGraphWidget().getDomainGridLinePaint().setPathEffect(dashFx);
        getGraphWidget().getRangeGridLinePaint().setPathEffect(dashFx);


//        setRenderMode(RenderMode.USE_BACKGROUND_THREAD);
        //setTitle("Titlu test");

        // without decimals
        this.getGraphWidget().setDomainValueFormat(new DecimalFormat("0"));
        this.addSeries(series, formatter);
    }

    private void setProperties(String title, int width, int height, GraphType currentType) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.currentType = currentType;
    }

    public static void showAddDialog(final Activity activity, final LinearLayout layout, final View.OnLongClickListener onLongClick,
                                     final GraphType graphType, final ArrayList<Widget> objects) {

        ScrollView scroll = new ScrollView(activity);
        scroll.setBackgroundColor(android.R.color.transparent);
        scroll.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setTitle("Choose the graph properties");

        LayoutInflater inflater= LayoutInflater.from(activity);
        final View alert_dialog_xml =inflater.inflate(R.layout.alert_dialog_graph, null);
        alertDialogBuilder.setView(alert_dialog_xml);

        alertDialogBuilder.setPositiveButton("Done", null);

        alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
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
                        String width = "";
                        String height = "";
                        String title = "";
                        String label = "";

                        EditText widthEditText = (EditText) alert_dialog_xml.findViewById(R.id.width);
                        if (widthEditText != null) {
                            width = widthEditText.getText().toString();

                            if (width.isEmpty())
                                widthEditText.setError("Width is required");
                        }

                        EditText heightEditText = (EditText) alert_dialog_xml.findViewById(R.id.height);
                        if (heightEditText != null) {
                            height = heightEditText.getText().toString();

                            if (height.isEmpty())
                                heightEditText.setError("Height is required");
                        }

                        EditText titleEditText = (EditText) alert_dialog_xml.findViewById(R.id.title);
                        if (titleEditText != null) {
                            title = titleEditText.getText().toString();

                            if (title.isEmpty())
                                titleEditText.setError("Title is required");
                        }

                        EditText labelEditText = (EditText) alert_dialog_xml.findViewById(R.id.label);
                        if (labelEditText != null) {
                            label = labelEditText.getText().toString();

                            if (label.isEmpty())
                                labelEditText.setError("Label is required");
                        }

                        if (!width.isEmpty() && !height.isEmpty() && !title.isEmpty() && !label.isEmpty()) {

                            addToBoard(activity, layout, onLongClick, objects,
                                    Integer.parseInt(width), Integer.parseInt(height),
                                    title, graphType, label);
                            alertDialog.dismiss();
                        }
                    }
                });
            }
        });

        alertDialog.show();
    }

    public static void addToBoard(Activity activity, LinearLayout layout, OnLongClickListener onLongClick, ArrayList<Widget> objects,
                                  int width, int height, String title, GraphType graphType, String label) {
        // add the thermometer
        final GraphWidget graph = new GraphWidget(activity, graphType);

        graph.setLayoutParams(new LinearLayout.LayoutParams(width, height));
        graph.setOnLongClickListener(onLongClick);
        graph.setProperties(title, width, height, graphType);
        graph.setTitle(title);
        graph.setLabel(label);

        layout.addView(graph);
        objects.add(graph);

        Thread thread_bar = new Thread() {
            @Override
            public void run() {
                super.run();

                while(true) {
                    x_bar_graph++;
                    double y = new Random().nextDouble();

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

    public GraphWidget(Context context, GraphType type) {
        this(context, null, type);
    }

    @Override
    public void addData(WylioMessage message) {
        // get data from message and call addPoint
    }

    public void addPoint(double x, double y) {
        series.addPoint(x, y);
    }

    @Override
    public JSONObject toJson() {
        JSONObject obj=new JSONObject();
        try {
            obj.put("type", TYPE_GRAPH);
            obj.put("graph_type", currentType);
            obj.put("title", title);
            obj.put("width", width);
            obj.put("height", height);
            obj.put("label", label);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    @Override
    public void setLabel(String label) {
        this.label = label;
    }

}
