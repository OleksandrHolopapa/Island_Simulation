package com.island.models;

import java.util.List;

public interface Organism {
    double getWeight();
    int getMaxPopulation();

    void reproduce(List<Organism> possiblePartners);
}