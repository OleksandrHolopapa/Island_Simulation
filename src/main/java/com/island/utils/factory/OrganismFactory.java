package com.island.utils.factory;

import com.island.models.Organism;
import com.island.models.animals.herbivorous.Horse;
import com.island.models.animals.herbivorous.Rabbit;
import com.island.models.animals.predators.Fox;
import com.island.models.animals.predators.Wolf;
import com.island.models.plants.Grass;

public class OrganismFactory {
    public static Organism getOrganism(String animalSimpleName){
        Organisms organism = Organisms.valueOf(animalSimpleName.toUpperCase());
        return switch (organism){
            case WOLF -> new Wolf();
            case FOX -> new Fox();
            case RABBIT -> new Rabbit();
            case HORSE -> new Horse();
            case GRASS -> new Grass();
        };
    }
}
