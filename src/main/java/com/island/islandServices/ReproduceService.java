package com.island.islandServices;

import com.island.models.Organism;

import com.island.map.Location;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ReproduceService implements Service {
    public void run(Location[][] locations) {
        try (ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())) {
            for (Location[] locationArray : locations) {
                for (Location location : locationArray) {
                    executor.submit(()->timeToReproduce(location));
                }
            }
            executor.shutdown();
            if(!executor.awaitTermination(3, TimeUnit.SECONDS)) System.out.println("ReproduceService needs more time!!!");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    private void timeToReproduce(Location location) {
        ConcurrentHashMap<String, List<Organism>> organismsInLocation = location.getOrganismsInLocation();
        for (List<Organism> organisms : organismsInLocation.values()) {
            for (int i = 0; i < organisms.size(); i++) {
                organisms.get(i).reproduce(organisms);
            }
        }
    }
}