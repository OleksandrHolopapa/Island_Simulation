package com.island.map;

import com.island.models.Organism;
import com.island.islandServices.Service;
import com.island.models.animals.Animal;
import com.island.utils.factory.OrganismFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Island {
    private final Location[][] locations;

    public Island(int length, int height) {
        locations = new Location[length][height];
        for (int i = 0; i < locations.length; i++) {
            for (int j = 0; j < locations[i].length; j++) {
                locations[i][j] = new Location();
            }
        }
    }

    public synchronized Location[][] getLocations() {
        return locations;
    }

    private Map<String, Integer> getPopulationOfDifferentOrganismsOnIsland() {
        Map<String, Integer> organismsOnIsland = new HashMap<>();
        for (Location[] location : locations) {
            for (Location currentLocation : location) {
                ConcurrentHashMap<String, List<Organism>> organismsInLocation = currentLocation.getOrganismsInLocation();
                for (Map.Entry<String, List<Organism>> entry : organismsInLocation.entrySet()) {
                    if (organismsOnIsland.containsKey(entry.getKey())) {
                        Integer i = organismsOnIsland.get(entry.getKey());
                        i += entry.getValue().size();
                        organismsOnIsland.put(entry.getKey(), i);
                    } else organismsOnIsland.put(entry.getKey(), entry.getValue().size());
                }
            }
        }
        return organismsOnIsland;
    }

    public void showIslandStatistic() {
        Map<String, Integer> organismOnIsland = getPopulationOfDifferentOrganismsOnIsland();
        for (Map.Entry<String, Integer> entry : organismOnIsland.entrySet()) {
            if (OrganismFactory.getOrganism(entry.getKey()) instanceof Animal)
                System.out.printf("  %-13s%8d\n", entry.getKey(), entry.getValue());
        }
    }

    public void startService(Service service) {
        service.run(locations);
    }
}