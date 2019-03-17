package com.company;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {
    static int iterations = 10;
    static int[] SIZES = new int[]{100, 1000, 10000};
    static int[] SERIES = new int[]{10, 100, 1000};
    static int EXAMPLES = 10;


//    static String filename = "./exemplaires/WC-100-100-03.txt";

    public static void main(String[] args) {

        // Print Best Solution
//        List<City> solution = getGreedy();
//        System.out.println(solution);
//        getProgDynam();
//        List<City> solution2 = getHeuristic();
//        System.out.println(solution2);
        generateCSVFile(ALGO.HEURISTIC);
    }

    private static List<City> getGreedy(String filename) {
        Greedy greedy = new Greedy();
        List<City> cities = new ArrayList<>();
        try {
            cities = greedy.readTextFile(filename);
        } catch (IOException e) {
            e.printStackTrace();
            return cities;
        }
        List<List<City>> allSolutions = new ArrayList<>();
        int[] revenues = new int[iterations];
        for (int i = 0; i < iterations; i++) {
            List<City> solution = greedy.Calculate(cities);
            allSolutions.add(solution);
            revenues[i] = Greedy.getTotalRevenue(solution);
        }
        // Find best solution
        int maxIndex = 0;
        for (int i = 0; i < revenues.length; i++) {
            maxIndex = revenues[i] > revenues[maxIndex] ? i : maxIndex;
        }
        return allSolutions.get(maxIndex);
    }

    private static int getProgDynam(String filename) {
        ProgDynam progDynam = new ProgDynam();
        try {
            progDynam.readTextFile(filename);
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
        return progDynam.calculate();
    }

    private static List<City> getHeuristic(String filename) {
        Heuristic heuristic = new Heuristic();
        List<City> cities = new ArrayList<>();
        try {
            cities = heuristic.readTextFile(filename);
        } catch (IOException e) {
            e.printStackTrace();
            return cities;
        }
        List<City> solution = getGreedy(filename);
        while (true) {
            List<City> neighbors = heuristic.findNeighbors(cities, solution);
            if (neighbors.size() > 0) {
                City bestNeighbor = heuristic.findBestNeighbors(neighbors);
                solution.remove(0);
                solution.add(bestNeighbor);
            } else {
                break;
            }
        }
        return solution;
    }

    private static void generateCSVFile(ALGO algo) {
        ArrayList<String> lines = new ArrayList<>();
        String resultsFilename = "";
        switch (algo) {
            case GREEDY:
                lines = generateGreedyCSVFile();
                resultsFilename = "results-greedy.csv";
                break;
            case HEURISTIC:
                lines = generateHeuristicCSVFile();
                resultsFilename = "results-heuristic.csv";
                break;
            case PROG_DYNA:
                break;
        }


        try {
            Path file = Paths.get(resultsFilename);
            Files.write(file, lines, Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<String> generateGreedyCSVFile() {
        ArrayList<String> lines = new ArrayList<>();
        lines.add("Size, Serie, Average Revenue, Average Execution Time");
        for (int size : SIZES) {
            for (int serie : SERIES) {
                // Get average of examples 1 through 10
                float averageRevenue = 0;
                double averageExecutionTime = 0;
                for (int example = 1; example <= EXAMPLES; example++) {
                    long startTime = System.nanoTime();
                    String filename = "./exemplaires/WC-" + size + "-" + serie + "-" + String.format("%02d", example) + ".txt";
                    long endTime = System.nanoTime();
                    long timeElapsed = endTime - startTime;
                    averageRevenue += Greedy.getTotalRevenue(getGreedy(filename));
                    averageExecutionTime += timeElapsed;
                }
                averageRevenue /= EXAMPLES;
                averageExecutionTime /= EXAMPLES;
                // convert to milliseconds
                averageExecutionTime /= 1000000.0;
                lines.add(size + "," + serie + "," + averageRevenue + "," + averageExecutionTime);
            }
        }

        return lines;
    }

    private static ArrayList<String> generateHeuristicCSVFile() {
        ArrayList<String> lines = new ArrayList<>();
        lines.add("Size, Serie, Average Revenue, Average Execution Time");
        for (int size : SIZES) {
            for (int serie : SERIES) {
                // Get average of examples 1 through 10
                float averageRevenue = 0;
                double averageExecutionTime = 0;
                for (int example = 1; example <= EXAMPLES; example++) {
                    long startTime = System.nanoTime();
                    String filename = "./exemplaires/WC-" + size + "-" + serie + "-" + String.format("%02d", example) + ".txt";
                    long endTime = System.nanoTime();
                    long timeElapsed = endTime - startTime;
                    averageRevenue += Greedy.getTotalRevenue(getHeuristic(filename));
                    averageExecutionTime += timeElapsed;
                }
                averageRevenue /= EXAMPLES;
                averageExecutionTime /= EXAMPLES;
                // convert to milliseconds
                averageExecutionTime /= 1000000.0;
                lines.add(size + "," + serie + "," + averageRevenue + "," + averageExecutionTime);
            }
        }

        return lines;
    }

    enum ALGO {
        GREEDY,
        PROG_DYNA,
        HEURISTIC
    }


}
