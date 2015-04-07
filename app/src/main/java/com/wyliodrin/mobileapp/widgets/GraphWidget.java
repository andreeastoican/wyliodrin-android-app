package com.wyliodrin.mobileapp.widgets;

import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.androidplot.ui.Formatter;
import com.androidplot.util.PixelUtils;
import com.androidplot.xy.BarFormatter;
import com.androidplot.xy.BarRenderer;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.StepFormatter;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.XYSeriesFormatter;
import com.androidplot.xy.XYStepMode;
import com.wyliodrin.mobileapp.api.WylioMessage;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by andreea on 12/10/14.
 */
public class GraphWidget extends XYPlot implements InputDataWidget {

    public static enum GraphType {
        StepGraph,
        BarGraph,
        LineGraph
    }

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
        setTitle("Titlu test");

        // without decimals
        this.getGraphWidget().setDomainValueFormat(new DecimalFormat("0"));
        this.addSeries(series, formatter);
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

}
