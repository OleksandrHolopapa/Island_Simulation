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

public class DeathService implements Service {
    public void run(Location[][] locations) {
        try (ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())) {
            Arrays.stream(locations)
                    .forEach(locationArray -> Arrays.stream(locationArray)
                            .forEach(location -> executor.submit(() -> deathByStarvation(location))));
            executor.shutdown();
            if (!executor.awaitTermination(10, TimeUnit.SECONDS))
                throw new NotEnoughTimeToProcessException("DeathService");
        } catch (NotEnoughTimeToProcessException e) {
            System.err.println(e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void deathByStarvation(Location location) {
        ConcurrentHashMap<String, List<Organism>> organismsInLocation = location.getOrganismsInLocation();
        for (List<Organism> organisms : organismsInLocation.values()) {
            for (int i = organisms.size() - 1; i >= 0; i--) {
                Organism organism = organisms.get(i);
                if ((organism instanceof Animal animal) && (animal.getWeight() <= 0)) {
                    organisms.remove(animal);
                }
            }
        }
    }
}