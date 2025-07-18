package com.island.utils.islandServices;

import com.island.map.Island;
import com.island.map.Location;
import com.island.models.Organism;
import com.island.models.animals.Animal;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class RelocationService {
    public static void startRelocation(Island island, Location location, int currentX, int currentY){
        ConcurrentHashMap<String, List<Organism>> organismsInLocation = location.getOrganismsInLocation();
        for (List<Organism> value : organismsInLocation.values()) {
            for (int i = value.size()-1; i >= 0; i--) {
                if(value.get(i) instanceof Animal animal) animal.move(location, currentX, currentY, island);
            }
        }
        System.out.println("Relocate stop");
    }
}
