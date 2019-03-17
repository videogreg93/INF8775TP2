package com.company;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {
    static int iterations = 30;
    static int[] SIZES = new int[]{100, 1000, 10000};
    static int[] SERIES = new int[]{10, 100, 1000};
    static int EXAMPLES = 10;


//    static String filename = "./exemplaires/WC-100-100-03.txt";

    public static void main(String[] args) {
        String algorithm = args[0];
        String filename = args[1];
        boolean showSolution = Boolean.parseBoolean(args[2]);
        boolean showExecutionTime = Boolean.parseBoolean(args[3]);
        launchAlgorithm(algorithm, filename, showSolution, showExecutionTime);
        //generateCSVFile(ALGO.GREEDY);
        //generateCSVFile(ALGO.HEURISTIC);
    }

    private static void launchAlgorithm(String algorithme, String filename, boolean showSolution, boolean showExecutionTime) {
        List<City> solution = null;
        long startTime = System.nanoTime();
        switch (algorithme) {
            case "glouton":
                solution = getGreedy(filename);
                break;
            case "progdyn":
                getProgDynam(filename); // TODO generate progDynam solution
                break;
            case "local":
                solution = getHeuristic(filename);
                break;
        }
        long endTime = System.nanoTime();
        double totalTime = (endTime - startTime)/1000000.0;
        int totalRevenue = Greedy.getTotalRevenue(solution);
        System.out.println("Total Revenue: " + totalRevenue);
        if (showSolution)
            System.out.println("Solution: " + printSolution(solution));
        if (showExecutionTime)
            System.out.println("Execution Time: " + totalTime);
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
                    averageRevenue += Greedy.getTotalRevenue(getGreedy(filename));
                    long endTime = System.nanoTime();
                    long timeElapsed = endTime - startTime;
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
                    averageRevenue += Greedy.getTotalRevenue(getHeuristic(filename));
                    long endTime = System.nanoTime();
                    long timeElapsed = endTime - startTime;
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

    private static String printSolution(List<City> solution) {
        String string = "";
        for (City city : solution) {
            string += city.index;
            string += " ";
        }
        return string;
    }

    enum ALGO {
        GREEDY,
        PROG_DYNA,
        HEURISTIC
    }


}
