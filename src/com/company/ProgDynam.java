package com.company;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProgDynam {

    private int totalCities;
    private int totalQuantity;
    private int[][] D;

    public List<City> readTextFile(String fileName) throws IOException {
        Path path = Paths.get(fileName);
        List<String> allLines = Files.readAllLines(path, StandardCharsets.UTF_8);
        totalCities = Integer.parseInt(allLines.remove(0));
        totalQuantity = Integer.parseInt(allLines.remove(allLines.size() - 1));
        try {
            D = new int[totalCities][totalQuantity + 1];
        } catch (OutOfMemoryError e) {
            System.out.print(e);
            return null;
        }
        List<City> cities = new ArrayList<>();
        for (String line : allLines) {
            String[] parameters = line.split("\\s+");
            cities.add(new City(Integer.parseInt(parameters[parameters.length - 3]), Integer.parseInt(parameters[parameters.length - 2]), Integer.parseInt(parameters[parameters.length - 1])));
        }
        return cities;
    }

    public List<City> calculate(List<City> cities) {
        buildArray(cities);
        return getSolution(cities);
    }

    private void buildArray(List<City> cities) {
        for (int i = 0; i < totalCities; i++) {
            for (int j = 0; j < totalQuantity + 1; j++) {
                D[i][j] = 0;
                if (i == 0) {
                    if (cities.get(i).quantity <= j) {
                        D[i][j] = cities.get(i).revenue;
                    }
                } else if (cities.get(i).quantity <= j) {
                    if (D[i - 1][j] > cities.get(i).revenue + D[i - 1][j - cities.get(i).quantity]) {
                        D[i][j] = D[i - 1][j];
                    } else {
                        D[i][j] = (cities.get(i).revenue + D[i - 1][j - cities.get(i).quantity]);
                    }
                } else {
                    D[i][j] = D[i - 1][j];
                }
            }
        }
    }

    private List<City> getSolution(List<City> cities) {
        List<City> solution = new ArrayList<City>();
        int currentQuantity = 0;
        int index = totalQuantity;
        for (int i = totalCities - 1; i >= 0; i--) {
            if (i == 0) {
                if (cities.get(i).quantity + currentQuantity <= totalQuantity) {
                    solution.add(cities.get(i));
                    currentQuantity += cities.get(i).quantity;
                    index -= cities.get(i).revenue;
                }
            } else if (D[i][index] != D[i - 1][index]) {
                if (cities.get(i).quantity + currentQuantity <= totalQuantity) {
                    solution.add(cities.get(i));
                    currentQuantity += cities.get(i).quantity;
                    index -= cities.get(i).quantity;
                }
            }
            if (currentQuantity == totalQuantity) {
                break;
            }
        }
        return solution;
    }
}
