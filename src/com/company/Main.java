package com.company;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    static int iterations = 10;
    static String filename = "./exemplaires/WC-100-10-03.txt";

    public static void main(String[] args) {

        // Print Best Solution
        List<City> solution = getGreedy();
        System.out.println(solution);

    }

    public static List<City> getGreedy() {
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




}
