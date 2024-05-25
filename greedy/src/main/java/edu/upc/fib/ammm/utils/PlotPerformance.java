package edu.upc.fib.ammm.utils;

import edu.upc.fib.ammm.model.PerformanceData;
import edu.upc.fib.ammm.model.Problem;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
public class PlotPerformance {
    final Map<Problem, List<PerformanceData>> results;

    public PlotPerformance(Map<Problem, List<PerformanceData>> results) {
        this.results = results;
    }

    public void plot() {
        log.info("PLOTTING");
        results.forEach((p, performances) -> {
            log.info("Problem: {}", p);
            performances.forEach(data -> {
                log.info("{}", data);
            });
        });
    }
}
