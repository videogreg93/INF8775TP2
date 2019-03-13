package com.company;

public class City {
    int index;
    int revenue;
    int quantity;

    float profitability;
    double weight = -1;

    City(int index, int revenue, int quantity) {
        this.index = index;
        this.revenue = revenue;
        this.quantity = quantity;
        profitability = (float)revenue/quantity;
    }

    @Override
    public String toString() {
        return "City{" +
                "index=" + index +
                ", revenue=" + revenue +
                ", quantity=" + quantity +
                ", profitability=" + profitability +
                '}';
    }
}
