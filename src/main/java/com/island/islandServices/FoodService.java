package com.island.islandServices;

import com.island.map.Location;
import com.island.models.Organism;
import com.island.models.animals.Animal;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FoodService implements Service {
    public void run(Location[][] locations) {
        try (ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())) {
            for (Location[] locationArray : locations) {
                for (Location location : locationArray) {
                    executor.submit(()->timeToEat(location));
                }
            }
            executor.shutdown();
            boolean awaited = executor.awaitTermination(1, TimeUnit.SECONDS);
            System.out.println("FoodService finished: "+awaited);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void timeToEat(Location location){
        ConcurrentHashMap<String, List<Organism>> organismsInLocation = location.getOrganismsInLocation();
        for (List<Organism> organisms : organismsInLocation.values()) {
            for (Organism organism : organisms) {
                if(organism instanceof Animal animal) animal.eat(organismsInLocation);
            }
        }
    }
}