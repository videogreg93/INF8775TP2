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
    List<City> cities = new ArrayList<>();
    private int[][] D;

    void readTextFile(String fileName) throws IOException {
        Path path = Paths.get(fileName);
        List<String> allLines = Files.readAllLines(path, StandardCharsets.UTF_8);
        totalCities = Integer.parseInt(allLines.remove(0));
        totalQuantity = Integer.parseInt(allLines.remove(allLines.size() - 1));
        D = new int[(totalCities + 1)][(totalQuantity + 1)];
        for (int i = 0; i < totalCities + 1; i++) {
            for (int j = 0; j < totalQuantity + 1; j++) {
                if (i == 0 || j == 0) {
                    D[i][j] = 0;
                } else {
                    D[i][j] = -1;
                }
            }
        }

        // City 0 is an empty city
        cities.add(new City(0,0,0));

        for (String line : allLines) {
            String[] parameters = line.split("\\s+");
            cities.add(new City(Integer.parseInt(parameters[1]), Integer.parseInt(parameters[2]), Integer.parseInt(parameters[3])));
        }
    }

    int calculate() {
        // Initialize array with limit values
//        initializeArray();
        // The Solution we want is found at D[totalCities,totalQuantity]
        return getValueAt(totalCities, totalQuantity);
    }

    private void initializeArray() {
        int totalRevenue = 0;
        int totalQuantity = 0;
        for (City city : cities) {
            int i = city.index;
            totalRevenue += city.revenue;
            totalQuantity += city.quantity;
            for (int j = totalQuantity; j < this.totalQuantity; j++) {
                D[i][j] = totalRevenue;
            }
        }
    }

    private int getValueAt(int i, int j) {
        if (i <= 0 || j <= 0 || i > totalCities || j > totalQuantity) {
            return 0;
        }
        if (D[i][j] != -1) {
            return D[i][j];
        }
        D[i][j] = Math.max(cities.get(i).revenue + getValueAt(i - 1, j - cities.get(i).quantity), getValueAt(i - 1, j));
        return D[i][j];
    }
}
