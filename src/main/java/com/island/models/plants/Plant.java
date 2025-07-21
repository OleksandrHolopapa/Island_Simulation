package com.island.models.plants;

import com.island.models.Organism;

import java.util.List;

public abstract class Plant implements Organism {
    protected double totalWeight;
    protected double weight;
    protected int maxPopulation;

    public Plant(double weight, int maxPopulation) {
        this.weight = weight;
        this.maxPopulation = maxPopulation;
    }

    @Override
    public void reproduce(List<Organism> possiblePartners) {
        this.weight = this.totalWeight;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public int getMaxPopulation() {
        return maxPopulation;
    }

    public void setTotalWeight(double totalWeight) {
        this.totalWeight = totalWeight;
    }

    public void setWeight(double weight) {
        this.weight = weight > 0 ? weight : 0;
    }
}