package com.island.utils.islandServices;

import com.island.map.Location;
import com.island.models.Organism;
import com.island.models.animals.Animal;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DeathService {
    public static void deathByStarvation(Location location){
        ConcurrentHashMap<String, List<Organism>> organismsInLocation = location.getOrganismsInLocation();
        for (List<Organism> organisms : organismsInLocation.values()) {
            for (int i = 0; i < organisms.size(); i++) {
                Organism organism = organisms.get(i);
                if((organism instanceof Animal animal)&&(animal.getWeight()<=0)) {
                    organisms.remove(animal);
                    i--;
                }
            }
        }
        System.out.println("DeathService stop");
    }
}