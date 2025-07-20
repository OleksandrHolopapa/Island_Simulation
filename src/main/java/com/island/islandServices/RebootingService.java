package com.island.islandServices;

import com.island.map.Location;
import com.island.models.Organism;
import com.island.models.animals.Animal;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class RebootingService implements Service {
    public void run(Location[][] locations) {
        try (ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())) {
            for (Location[] locationArray : locations) {
                for (Location location : locationArray) {
                    executor.submit(()->rebootAnimalsStats(location));
                }
            }
            executor.shutdown();
            if(!executor.awaitTermination(3, TimeUnit.SECONDS)) System.out.println("RebootingService needs more time!!!");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void rebootAnimalsStats(Location location){
        ConcurrentHashMap<String, List<Organism>> organismsInLocation = location.getOrganismsInLocation();
        for (List<Organism> value : organismsInLocation.values()) {
            for (Organism organism : value) {
                if(organism instanceof Animal animal) {
                    animal.setAlreadyReproduced(false);
                    animal.getAlreadyMoved().set(false);
                }
            }
        }
    }
}