package com.company;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    static int iterations = 10;
    static String filename = "./exemplaires/WC-100-100-03.txt";

    public static void main(String[] args) {

        // Print Best Solution
        //List<City> solution = getGreedy();
        //System.out.println(solution);
        //getProgDynam();
        List<City> solution2 = getHeuristic();
        System.out.println(solution2);

    }

    private static List<City> getGreedy() {
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
            revenues[i] = greedy.getTotalRevenue(solution);
        }
        // Find best solution
        int maxIndex = 0;
        for (int i = 0; i < revenues.length; i++) {
            maxIndex = revenues[i] > revenues[maxIndex] ? i : maxIndex;
        }
        return allSolutions.get(maxIndex);
    }

    private static List<City> getProgDynam() {
        ProgDynam progDynam = new ProgDynam();
        List<City> cities = new ArrayList<>();
        try {
            progDynam.readTextFile(filename);
        } catch (IOException e) {
            e.printStackTrace();
            return cities;
        }
        progDynam.calculate();
        return cities;
    }

    private static List<City> getHeuristic() {
        Heuristic heuristic = new Heuristic();
        List<City> cities = new ArrayList<>();
        try {
            cities = heuristic.readTextFile(filename);
        } catch (IOException e) {
            e.printStackTrace();
            return cities;
        }
        List<City> solution = getGreedy();
        System.out.println(solution);
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

}
