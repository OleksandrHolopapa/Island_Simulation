package com.island.utils.islandServices;

import com.island.models.Organism;

import com.island.map.Location;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ReproduceService {
    public synchronized static void timeToReproduce(Location location){
        ConcurrentHashMap<String, List<Organism>> organismsInLocation = location.getOrganismsInLocation();
        for (List<Organism> organisms : organismsInLocation.values()) {
            for (int i = 0; i < organisms.size(); i++) {
                organisms.get(i).reproduce(organisms);
            }
        }
        System.out.println("Reproduce stop");
    }
}