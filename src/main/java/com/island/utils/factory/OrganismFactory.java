package com.island.utils.factory;

import com.island.models.Organism;
import com.island.models.animals.herbivorous.*;
import com.island.models.animals.predators.*;
import com.island.models.plants.Grass;

public class OrganismFactory {
    public static Organism getOrganism(String animalSimpleName) {
        Organisms organism = Organisms.valueOf(animalSimpleName.toUpperCase());
        return switch (organism) {
            case WOLF -> new Wolf();
            case FOX -> new Fox();
            case RABBIT -> new Rabbit();
            case HORSE -> new Horse();
            case GRASS -> new Grass();
            case BOA -> new Boa();
            case BEAR -> new Bear();
            case EAGLE -> new Eagle();
            case MOUSE -> new Mouse();
            case DEER -> new Deer();
            case BOAR -> new Boar();
            case BUFFALO -> new Buffalo();
            case SHEEP -> new Sheep();
            case DUCK -> new Duck();
            case GOAT -> new Goat();
            case CATERPILLAR -> new Caterpillar();
        };
    }
}
