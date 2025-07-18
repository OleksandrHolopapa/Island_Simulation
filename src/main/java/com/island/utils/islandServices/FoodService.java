package com.island.utils.islandServices;

import com.island.map.Location;
import com.island.models.Organism;
import com.island.models.animals.Animal;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class FoodService {
    public static void timeToEat(Location location){
        ConcurrentHashMap<String, List<Organism>> organismsInLocation = location.getOrganismsInLocation();
        for (List<Organism> organisms : organismsInLocation.values()) {
            for (Organism organism : organisms) {
                if(organism instanceof Animal animal) animal.eat(organismsInLocation);
            }
        }
        System.out.println("Feeding is over");
    }
}