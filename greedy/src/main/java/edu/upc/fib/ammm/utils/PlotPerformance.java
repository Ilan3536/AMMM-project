package edu.upc.fib.ammm.utils;

import edu.upc.fib.ammm.model.PerformanceData;
import edu.upc.fib.ammm.model.Problem;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
public class PlotPerformance {
    final Map<Problem, List<PerformanceData>> results;

    public PlotPerformance(Map<Problem, List<PerformanceData>> results) {
        this.results = results;
    }

    public void plot() {
        var timestamp = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT).replace(":", "_");

        try (PrintWriter out = new PrintWriter(Globals.OUT_DIR + "perf_" + timestamp + ".csv")) {
            out.print(csv());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public String csv() {
        var csv = new StringBuilder();
        var header = new ArrayList<>(List.of("Problem", "n"));

        results.entrySet().iterator().next().getValue().forEach(data -> {
            var algorithm = data.name().replace(',',';');
            header.add(algorithm + " Time");
            header.add(algorithm + " Objective");
        });

        csv.append(String.join(",", header));
        csv.append("\n");

        for (var entry : results.entrySet()) {
            var problem = entry.getKey();
            var row = new ArrayList<>(List.of(problem.getFilePath().replace(',', ' '), String.valueOf(problem.getN())));

            for (var data : entry.getValue()) {
                row.add(String.valueOf(data.time()));
                row.add(String.valueOf(data.solution().getCost()));
            }
            csv.append(String.join(",", row));
            csv.append("\n");
        }

        return csv.toString();
    }
}
