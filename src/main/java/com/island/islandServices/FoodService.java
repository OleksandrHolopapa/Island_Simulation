package com.island.islandServices;

import com.island.map.Location;
import com.island.models.Organism;
import com.island.models.animals.Animal;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FoodService implements Service {
    public void run(Location[][] locations) {
        try (ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())) {
            Arrays.stream(locations)
                    .forEach(locationArray -> Arrays.stream(locationArray)
                            .forEach(location -> executor.submit(() -> timeToEat(location))));
            executor.shutdown();
            if (!executor.awaitTermination(10, TimeUnit.SECONDS))
                throw new NotEnoughTimeToProcessException("FoodService");
        } catch (NotEnoughTimeToProcessException e) {
            System.err.println(e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void timeToEat(Location location) {
        ConcurrentHashMap<String, List<Organism>> organismsInLocation = location.getOrganismsInLocation();
        for (List<Organism> organisms : organismsInLocation.values()) {
            for (Organism organism : organisms) {
                if (organism instanceof Animal animal) animal.eat(organismsInLocation);
            }
        }
    }
}