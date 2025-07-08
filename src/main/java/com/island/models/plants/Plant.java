package com.island.models.plants;

import com.island.models.Organism;

public abstract class Plant implements Organism {
    protected double weight;
    protected int maxPopulation;

    public Plant(double weight, int maxPopulation) {
        this.weight = weight;
        this.maxPopulation = maxPopulation;
    }

}