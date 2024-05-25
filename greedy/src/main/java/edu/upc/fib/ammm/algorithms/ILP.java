package edu.upc.fib.ammm.algorithms;

import edu.upc.fib.ammm.model.PerformanceData;
import edu.upc.fib.ammm.model.Problem;
import edu.upc.fib.ammm.model.Solution;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

// Wrapper for OPL CPLEX run
@Slf4j
public class ILP {
    final String model = Path.of("../opl/project.mod").toAbsolutePath().toString();
    final String OPLRUN_MACOS = "/Applications/CPLEX_Studio2211/opl/bin/x86-64_osx/oplrun";
    final String OPLRUN_WINDOWS = "C:\\Program Files\\IBM\\ILOG\\CPLEX_Studio2211\\opl\\bin\\x64_win64\\oplrun.exe";
    final String OPLRUN_LINUX = "oplrun"; // Should be in PATH
    final String OPLRUN;

    public ILP() {
        var os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            OPLRUN = OPLRUN_WINDOWS;
        } else if (os.contains("mac")) {
            OPLRUN = OPLRUN_MACOS;
        } else {
            OPLRUN = OPLRUN_LINUX;
        }

    }

    private String runOpl(String[] args) throws IOException {
        var fullArgs = new ArrayList<>(Arrays.asList(args));
        fullArgs.addFirst(OPLRUN);
        var processBuilder = new ProcessBuilder(fullArgs.toArray(String[]::new));

        try {
            var process = processBuilder.start();
            var sb = new StringBuilder();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                reader.lines().forEach(s -> {
                    sb.append(s);
                    sb.append("\n");
                });
            }

            int exitCode = process.waitFor();
            return sb.toString();
        } catch (IOException | InterruptedException e) {
            log.error("Error running CPLEX", e);
            Thread.currentThread().interrupt();  // Restore the interrupted status
        }
        return null;
    }

    public PerformanceData solve(Problem problem) {
        try {
            var out = runOpl(new String[]{model, problem.getFilePath()});
            System.out.println(out);
            var solution = new Solution(problem.getBox());
            // TODO parse out
            solution.setCost(0);
            float time = 0;

            return new PerformanceData("ILP", solution, time);
        } catch (Exception e) {
            log.error("Error while running CPLEX on {} {}", model, problem.getFilePath(), e);
        }
        return null;
    }
}
