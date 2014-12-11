package com.wyliodrin.mobileapp.realTimeGraphics;

import android.content.Context;
import android.graphics.Color;

import com.wyliodrin.mobileapp.realTimeGraphics.Point;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

public class LineGraph {

    private GraphicalView view;

    private TimeSeries dataset = new TimeSeries("Rain Fall");
    private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();

    private XYSeriesRenderer renderer = new XYSeriesRenderer(); // This will be used to customize line 1
    private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer(); // Holds a collection of XYSeriesRenderer and customizes the graph

    public LineGraph()
    {
        // Add single dataset to multiple dataset
        mDataset.addSeries(dataset);

        // Customization time for line 1!
        renderer.setColor(Color.RED);
        renderer.setPointStyle(PointStyle.CIRCLE);
        renderer.setFillPoints(true);
        renderer.setLineWidth(2);
        renderer.setDisplayChartValues(true);

        // Enable Zoom
        mRenderer.setZoomButtonsVisible(true);
        mRenderer.setXTitle("Day #");
        mRenderer.setYTitle("Degrees in Fahrenheit");
        mRenderer.setChartTitle("Temperature in New York City");
        mRenderer.setShowGrid(true);

        mRenderer.setApplyBackgroundColor(true);
        mRenderer.setBackgroundColor(Color.BLACK);
        mRenderer.setMarginsColor(Color.WHITE);

        // Add single renderer to multiple renderer
        mRenderer.addSeriesRenderer(renderer);
    }

    public GraphicalView getView(Context context)
    {
        view =  ChartFactory.getLineChartView(context, mDataset, mRenderer);
        return view;
    }

    public void addNewPoints(Point p)
    {
        dataset.add(p.getX(), p.getY());
    }

}