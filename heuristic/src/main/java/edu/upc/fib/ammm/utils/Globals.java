package edu.upc.fib.ammm.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Globals {
    public static String PROBLEMS_DIR = "../opl";
    public static boolean ENABLE_ILP = true;
    public static String OUT_DIR = "";
    public static int MAX_ILP_PROBLEM_NUMBER = 48; // 0 means none

    static {
        PROBLEMS_DIR = System.getenv().getOrDefault("PROBLEMS_DIR", PROBLEMS_DIR);
        ENABLE_ILP = Boolean.parseBoolean(System.getenv().getOrDefault("ENABLE_ILP", String.valueOf(ENABLE_ILP)));
        OUT_DIR = System.getenv().getOrDefault("OUT_DIR", OUT_DIR);
        MAX_ILP_PROBLEM_NUMBER = Integer.parseInt(System.getenv().getOrDefault("MAX_ILP_PROBLEM_NUMBER", String.valueOf(MAX_ILP_PROBLEM_NUMBER)));

        log.info("Running with global configuration (can be overridden via env variables)");
        log.info("PROBLEMS_DIR={}", PROBLEMS_DIR);
        log.info("ENABLE_ILP={}", ENABLE_ILP);
        log.info("OUT_DIR={}", OUT_DIR);
        log.info("MAX_ILP_PROBLEM_NUMBER={}", MAX_ILP_PROBLEM_NUMBER);
    }
}
