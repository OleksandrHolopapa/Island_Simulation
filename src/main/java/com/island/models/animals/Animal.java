package com.island.models.animals;

import com.island.models.Organism;

import java.util.concurrent.ThreadLocalRandom;

public abstract class Animal implements Organism {
    protected double weight;
    protected int maxPopulation;
    protected int maxSpeedOfMovement;
    protected double maxCanEat;
    protected boolean moved = false;

    public Animal(double weight, int maxPopulation, int maxSpeedOfMovement, double maxCanEat) {
        this.weight = weight;
        this.maxPopulation = maxPopulation;
        this.maxSpeedOfMovement = maxSpeedOfMovement;
        this.maxCanEat = maxCanEat;
    }


}