package edu.upc.fib.ammm;

import edu.upc.fib.ammm.utils.Globals;
import lombok.SneakyThrows;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.VectorGraphicsEncoder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

import com.opencsv.CSVReader;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.markers.SeriesMarkers;

public class Plot {
    @SneakyThrows
    public static void main(String[] args) {
        var timeChart = new XYChartBuilder().theme(Styler.ChartTheme.GGPlot2).width(800).height(800).title("Time Chart").xAxisTitle("N").yAxisTitle("Time").build();
        var objectiveChart = new CategoryChartBuilder().theme(Styler.ChartTheme.GGPlot2).width(800).height(800).title("Objective Chart").xAxisTitle("N").yAxisTitle("Objective").build();

        timeChart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideS);
        objectiveChart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideS);

        //timeChart.getStyler().setYAxisLogarithmic(true);
        //timeChart.setYAxisTitle("log(Time)");
        //objectiveChart.getStyler().setOverlapped(true);
        //objectiveChart.getStyler().setYAxisLogarithmic(true);
        objectiveChart.getStyler().setXAxisMaxLabelCount(20);

        try (var reader = Files.newBufferedReader(Path.of(args[0]))) {
            try (CSVReader csvReader = new CSVReader(reader)) {
                var header = csvReader.readNext();
                var all = csvReader.readAll();

                all.sort(Comparator.comparingInt(p -> Integer.parseInt(p[1])));

                var ns = new ArrayList<Double>();

                var times = new LinkedHashMap<String,ArrayList<Double>>();
                var objectives = new LinkedHashMap<String,ArrayList<Double>>();

                // skip problem and n
                for (int i = 2; i < header.length; i += 2) {
                    times.put(header[i], new ArrayList<>());
                    objectives.put(header[i+1], new ArrayList<>());
                }

                for (var entry : all) {
                    double bestObjective = 0.0;
                    for (int i = 2; i < entry.length; i += 2) {
                        var objective = Double.valueOf(entry[i+1]);
                        if (objective > bestObjective) {
                            bestObjective = objective;
                        }
                    }

                    ns.add(Double.valueOf(entry[1]));
                    for (int i = 2; i < entry.length; i += 2) {
                        var time = Double.valueOf(entry[i]);
                        var objective = Double.valueOf(entry[i+1]);

                        // Fix for log scale
                        if (time == 0) {
                            time = 0.1;
                        }

                        times.get(header[i]).add(time);
                        objectives.get(header[i+1]).add(objective);
                    }
                }

                for (var algo: times.entrySet()) {
                    var series = timeChart.addSeries(algo.getKey(), ns, algo.getValue());
                    series.setMarker(SeriesMarkers.NONE);
                    series.setSmooth(true);
                }

                for (var algo: objectives.entrySet()) {
                    var series = objectiveChart.addSeries(algo.getKey(), ns, algo.getValue());
                    series.setMarker(SeriesMarkers.NONE);
                    //series.setSmooth(true);
                }

                // To import in the report
                VectorGraphicsEncoder.saveVectorGraphic(timeChart, Path.of(Globals.OUT_DIR, "timeChart.pdf").toString(), VectorGraphicsEncoder.VectorGraphicsFormat.PDF);
                VectorGraphicsEncoder.saveVectorGraphic(objectiveChart, Path.of(Globals.OUT_DIR, "objectiveChart.pdf").toString(), VectorGraphicsEncoder.VectorGraphicsFormat.PDF);
            }
        }

    }
}
