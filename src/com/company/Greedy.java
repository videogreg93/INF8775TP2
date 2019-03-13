package com.company;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

class Greedy {

    private int totalCitys;
    private int totalQuantity;

    List<City> Calculate(List<City> cities) {
        calculateWeights(cities);
        List<City> solution = new ArrayList<>();
        while (!cities.isEmpty() && !isSolution(solution)) {
            City choice = getChoice(cities);
            if (legalChoice(choice, solution)) {
                solution.add(choice);
            }
        }
        return solution;
    }

    /**
     * Adapted from https://en.wikipedia.org/wiki/Fitness_proportionate_selection#Java_%E2%80%93_linear_O(n)_version
     *
     * @param cities list to choose from
     * @return choice
     */
    private City getChoice(List<City> cities) {
        // calculate the total weight
        double weight_sum = 0;
        for (City city : cities) {
            weight_sum += city.weight;
        }
        // get a random value
        double value = new Random().nextDouble() * weight_sum;
        // locate the random value based on the weights
        for (int i = 0; i < cities.size(); i++) {
            value -= cities.get(i).weight;
            if (value <= 0) return cities.remove(i);
        }
        // when rounding errors occur, we return the last item's index
        return cities.remove(cities.size() - 1);
    }

    private void calculateWeights(List<City> cities) {
        int totalProfitability = 0;
        for (City city : cities) {
            totalProfitability += city.profitability;
        }
        for (City city : cities) {
            city.weight = city.profitability / totalProfitability;
        }
    }

    private boolean legalChoice(City choice, List<City> solution) {
        int total = getTotalQuantity(solution);
        total += choice.quantity;
        return total <= totalQuantity;
    }

    private boolean isSolution(List<City> solution) {
        int total = getTotalQuantity(solution);
        return total == totalQuantity;
    }

    public List<City> readTextFile(String fileName) throws IOException {
        Path path = Paths.get(fileName);
        List<String> allLines = Files.readAllLines(path, StandardCharsets.UTF_8);
        totalCitys = Integer.parseInt(allLines.remove(0));
        totalQuantity = Integer.parseInt(allLines.remove(allLines.size() - 1));
        List<City> cities = new ArrayList<>();
        for (String line : allLines) {
            String[] parameters = line.split("\\s+");
            cities.add(new City(Integer.parseInt(parameters[1]), Integer.parseInt(parameters[2]), Integer.parseInt(parameters[3])));
        }
        return cities;
    }

    public int getTotalQuantity(List<City> cities) {
        int total = 0;
        for (City city : cities) {
            total += city.quantity;
        }
        return total;
    }

    public int getTotalRevenue(List<City> solution) {
        int total = 0;
        for (City city : solution) {
            total += city.revenue;
        }
        return total;
    }
}
