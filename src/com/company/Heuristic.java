package com.company;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Heuristic {
    private int totalCities;
    private int totalQuantity;


    public List<City> readTextFile(String fileName) throws IOException {
        Path path = Paths.get(fileName);
        List<String> allLines = Files.readAllLines(path, StandardCharsets.UTF_8);
        totalCities = Integer.parseInt(allLines.remove(0));
        totalQuantity = Integer.parseInt(allLines.remove(allLines.size() - 1));
        List<City> cities = new ArrayList<>();
        for (String line : allLines) {
            String[] parameters = line.split("\\s+");
            cities.add(new City(Integer.parseInt(parameters[parameters.length-3]), Integer.parseInt(parameters[parameters.length-2]), Integer.parseInt(parameters[parameters.length-1])));        }
        return cities;
    }

    public List<City> findNeighbors(List<City> cities, List<City> solution) {
        List<City> neighbors = new ArrayList<City>();
        int totalQuantityTmp = 0;
        for (City city : solution) {
            totalQuantityTmp += city.quantity;
        }
        for (City city : cities) {
            if (!solution.contains(city)) {
                if (totalQuantityTmp + city.quantity - solution.get(0).quantity <= totalQuantity) {
                    if (city.revenue > solution.get(0).revenue) {
                        neighbors.add(city);
                    }
                }
            }
        }
        return neighbors;
    }

    public City findBestNeighbors(List<City> neighbors) {
        City bestNeighbor = neighbors.get(0);
        for (City city : neighbors) {
            if (city.revenue > bestNeighbor.revenue) {
                bestNeighbor = city;
            }
        }
        return  bestNeighbor;
    }
}
