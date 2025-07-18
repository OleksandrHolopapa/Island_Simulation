package com.island.map;

import com.island.models.Organism;
import com.island.utils.islandServices.InitialisationService;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Island {
    private final Location[][] locations;
    private final int length;
    private final int height;

    public Island(int length, int height) {
        this.length = length;
        this.height = height;
        locations = new Location[length][height];
        for (int i = 0; i < locations.length; i++) {
            for (int j = 0; j < locations[i].length; j++) {
                locations[i][j] = new Location();
            }
        }
    }

    public void islandInitialisation(){
        InitialisationService.initialize(locations);
    }

    public synchronized Location[][] getLocations() {
        return locations;
    }

    public int getLength() {
        return length;
    }

    public int getHeight() {
        return height;
    }

    public void showOrganismCountInLocations() {
        for (int i = 0; i < locations.length; i++) {
            for (int j = 0; j < locations[i].length; j++) {
                Location location = locations[i][j];
                System.out.println("LOCATION "+i+":"+j);
                ConcurrentHashMap<String, List<Organism>> organismsInLocation = location.getOrganismsInLocation();
                for (Map.Entry<String, List<Organism>> entry : organismsInLocation.entrySet()) {
                    System.out.print(entry.getKey()+": "+entry.getValue().size());
                    if(entry.getKey().equals("Grass")) System.out.print(", size = "+entry.getValue().getFirst().getWeight());
                    System.out.println();
                }
            }
        }
    }

    public void showIslandStatistic() {

    }
}