package com.island.utils.islandServices;

import com.island.map.Location;
import com.island.models.Organism;
import com.island.models.animals.Animal;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class RebootingService {
    public static void rebootAnimalsStats(Location location){
        ConcurrentHashMap<String, List<Organism>> organismsInLocation = location.getOrganismsInLocation();
        for (List<Organism> value : organismsInLocation.values()) {
            for (Organism organism : value) {
                if(organism instanceof Animal animal) {
                    animal.getAlreadyAte().set(false);
                    animal.getAlreadyReproduced().set(false);
                    animal.getAlreadyMoved().set(false);
                }
            }
        }
        System.out.println("Reboot stop");
    }
}
