package com.island.islandServices;

import com.island.map.Location;
import com.island.models.Organism;
import com.island.models.animals.Animal;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class RelocationService implements Service {
    public void run(Location[][] locations) {
        try (ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())) {
            for (int i = 0; i < locations.length; i++) {
                for (int j = 0; j < locations[i].length; j++) {
                    int x = i;
                    int y = j;
                    Location location = locations[i][j];
                    executor.submit(() -> startRelocation(locations, location, x, y));
                }
            }
            executor.shutdown();
            if (!executor.awaitTermination(10, TimeUnit.SECONDS))
                throw new NotEnoughTimeToProcessException("RelocationService");
        } catch (NotEnoughTimeToProcessException e) {
            System.err.println(e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private synchronized void startRelocation(Location[][] locations, Location currentLocation, int currentX, int currentY) {
        ConcurrentHashMap<String, List<Organism>> organismsInLocation = currentLocation.getOrganismsInLocation();
        for (List<Organism> value : organismsInLocation.values()) {
            for (int i = value.size() - 1; i >= 0; i--) {
                if (value.get(i) instanceof Animal animal) animal.move(locations, currentLocation, currentX, currentY);
            }
        }
    }
}