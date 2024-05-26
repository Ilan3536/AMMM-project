package edu.upc.fib.ammm;

import edu.upc.fib.ammm.utils.Globals;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.VectorGraphicsEncoder;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import com.opencsv.CSVReader;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.markers.SeriesMarkers;

@Slf4j
public class Plot {
    @SneakyThrows
    public static void main(String[] args) {
        var timeChart = new XYChartBuilder().theme(Styler.ChartTheme.GGPlot2).width(800).height(800).title("Time Chart").xAxisTitle("N").yAxisTitle("Time").build();
        var objectiveChart = new XYChartBuilder().theme(Styler.ChartTheme.GGPlot2).width(800).height(800).title("Objective Chart").xAxisTitle("N").yAxisTitle("Objective").build();

        timeChart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideS);
        objectiveChart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideS);

        int reduceFontBy = 4;
        var font = objectiveChart.getStyler().getLegendFont();
        var attributes = new HashMap<TextAttribute, Object>();
        attributes.put(TextAttribute.SIZE, font.getSize() - reduceFontBy);
        var newFont = font.deriveFont(attributes);

        objectiveChart.getStyler().setLegendFont(newFont);
        timeChart.getStyler().setLegendFont(newFont);

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
                    for (int i = 2; i < header.length; i += 2) {
                        var objective = Double.valueOf(entry[i+1]);
                        if (objective > bestObjective) {
                            bestObjective = objective;
                        }
                    }

                    ns.add(Double.valueOf(entry[1]));
                    for (int i = 2; i < header.length; i += 2) {
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
                    series.setLineWidth(1f);
                }

                for (var algo: objectives.entrySet()) {
                    var series = objectiveChart.addSeries(algo.getKey(), ns, algo.getValue());
                    series.setMarker(SeriesMarkers.NONE);
                    series.setSmooth(true);
                    series.setLineWidth(1f);
                }

                // To import in the report
                var timeChartPath = args[0] + ".timeChart.pdf";
                VectorGraphicsEncoder.saveVectorGraphic(timeChart, Path.of(Globals.OUT_DIR, timeChartPath).toString(), VectorGraphicsEncoder.VectorGraphicsFormat.PDF);
                log.info("Output {}", timeChartPath);
                var objectiveChartPath = args[0] + ".objectiveChart.pdf";
                VectorGraphicsEncoder.saveVectorGraphic(objectiveChart, Path.of(Globals.OUT_DIR, objectiveChartPath).toString(), VectorGraphicsEncoder.VectorGraphicsFormat.PDF);
                log.info("Output {}", objectiveChartPath);
            }
        }

    }
}
