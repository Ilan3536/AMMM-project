package edu.upc.fib.ammm;

import edu.upc.fib.ammm.utils.Globals;
import lombok.SneakyThrows;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.VectorGraphicsEncoder;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import com.opencsv.CSVReader;

public class Plot {
    @SneakyThrows
    public static void main(String[] args) {
        var timeChart = new XYChartBuilder().width(600).height(400).title("Time Chart").xAxisTitle("N").yAxisTitle("Time").build();
        var objectiveChart = new XYChartBuilder().width(600).height(400).title("Objective Chart").xAxisTitle("N").yAxisTitle("Objective").build();

        try (var reader = Files.newBufferedReader(Path.of(args[0]))) {
            try (CSVReader csvReader = new CSVReader(reader)) {
                var header = csvReader.readNext();
                var all = csvReader.readAll();

                // To import in the report
                //VectorGraphicsEncoder.saveVectorGraphic(timeChart, Path.of(Globals.OUT_DIR, "timeChart.pdf").toString(), VectorGraphicsEncoder.VectorGraphicsFormat.PDF);
                //VectorGraphicsEncoder.saveVectorGraphic(objectiveChart, Path.of(Globals.OUT_DIR, "objectiveChart.pdf").toString(), VectorGraphicsEncoder.VectorGraphicsFormat.PDF);
            }
        }

    }
}
