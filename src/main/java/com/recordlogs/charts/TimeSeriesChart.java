package com.recordlogs.charts;

import com.recordlogs.model.Measurement;
import com.recordlogs.model.SourceData;
import de.gsi.chart.XYChart;
import de.gsi.chart.axes.AxisLabelOverlapPolicy;
import de.gsi.chart.axes.spi.DefaultNumericAxis;
import de.gsi.chart.plugins.DataPointTooltip;
import de.gsi.chart.plugins.Panner;
import de.gsi.chart.plugins.ParameterMeasurements;
import de.gsi.chart.plugins.Zoomer;
import de.gsi.chart.renderer.ErrorStyle;
import de.gsi.chart.renderer.LineStyle;
import de.gsi.chart.renderer.spi.ErrorDataSetRenderer;
import de.gsi.chart.ui.geometry.Side;
import de.gsi.dataset.spi.DoubleDataSet;
import javafx.application.Platform;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class TimeSeriesChart {

    public static XYChart getTimeSeriesChart() {
        final DefaultNumericAxis xAxis = new DefaultNumericAxis("time");
        xAxis.setOverlapPolicy(AxisLabelOverlapPolicy.SKIP_ALT);
        final DefaultNumericAxis yAxis = new DefaultNumericAxis("measurement");

        XYChart chart = new XYChart(xAxis, yAxis);
        chart.legendVisibleProperty().set(true);
        //chart.setLegendSide(Side.RIGHT);
        chart.getPlugins().add(new Zoomer());
        //chart.getPlugins().add(new EditAxis());
        chart.getPlugins().add(new DataPointTooltip());
        chart.getPlugins().add(new ParameterMeasurements());
        chart.getPlugins().add(new Panner());
        chart.setAnimated(false); // set them false to make the plot faster

        xAxis.setAutoRangeRounding(false);
        xAxis.setTimeAxis(true);
        yAxis.setAutoRangeRounding(true);
        chart.setLegend(new CustomLegend());
        return chart;
    }
    public static DoubleDataSet createDatasetForCase(String activeCase) {
        final DoubleDataSet dataset = new DoubleDataSet("case " + activeCase);
        dataset.setStyle("strokeColor=" + getRandomColor() + "; strokeWidth=2");
        dataset.autoNotification().set(false);
        dataset.clearData();
        dataset.autoNotification().set(true);
        Platform.runLater(() -> dataset.fireInvalidated(null));
        return dataset;
    }
    public static ErrorDataSetRenderer getDatasetsPerCase(SourceData sourceData, List<String> activeCasesList, String activeMeasurement) {
        Map<String, DoubleDataSet> datasetsPerCase = new HashMap<>();
        Set<String> activeCases = new HashSet<>(activeCasesList);
        activeCases.forEach(activeCase -> datasetsPerCase.put(activeCase, createDatasetForCase(activeCase)));
        sourceData.getMeasurements()
                .stream()
                .filter(measurement -> activeCases.contains(measurement.getCaseID()))
                .forEach(measurement -> {
                    datasetsPerCase.get(measurement.getCaseID())
                            .add(getSeconds(measurement), getValue(measurement, activeMeasurement));
                });
        final ErrorDataSetRenderer errorRenderer = new ErrorDataSetRenderer();
        errorRenderer.errorStyleProperty().set(ErrorStyle.NONE);
        errorRenderer.getDatasets().addAll(datasetsPerCase.values());
        return errorRenderer;
    }
    public static String getRandomColor() {
        final Random random = new Random();
        final String[] letters = "0123456789ABCDEF".split("");
        String color = "#";
        for (int i = 0; i < 6; i++) {
            color += letters[Math.round(random.nextFloat() * 15)];
        }
        return color;
    }
    private static long getSeconds(Measurement measurement) {
        return measurement.getTimestamp().getTime() / 1000;
    }

    private static double getValue(Measurement measurement, String selectedMeasurement) {
        try {
            return Double.parseDouble(measurement.getMeasurementValue().get(selectedMeasurement));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
