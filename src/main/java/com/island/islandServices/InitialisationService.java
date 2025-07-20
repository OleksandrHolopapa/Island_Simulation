package com.island.islandServices;

import com.island.map.Location;
import com.island.models.Organism;
import com.island.models.plants.Plant;
import com.island.utils.factory.Organisms;
import com.island.utils.factory.OrganismFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class InitialisationService implements Service {
    public void run(Location[][] locations) {
        try (ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())) {
            for (Location[] locationArray : locations) {
                for (Location location : locationArray) {
                    executor.submit(()->addOrganismsToLocation(location));
                    //System.out.println("DONE");
                }
            }
            executor.shutdown();
            boolean awaited = executor.awaitTermination(1, TimeUnit.SECONDS);
            System.out.println("Initialisation finished: "+awaited);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void addOrganismsToLocation(Location location) {
        ConcurrentHashMap<String, List<Organism>> organismsInLocation = new ConcurrentHashMap<>();
        for (Organisms organismEnumValue : Organisms.values()) {
            //З ймовірністю 60% організм буде в локації
            if(organismIsPresentInLocation(60)){
                Organism organism = OrganismFactory.getOrganism(organismEnumValue.toString());
                List<Organism> identicalOrganismsInLocation = getIdenticalOrganismsList(organism, organismEnumValue);
                organismsInLocation.put(organism.getClass().getSimpleName(), identicalOrganismsInLocation);
            }
        }
        location.getOrganismsInLocation().putAll(organismsInLocation);
    }

    private List<Organism> getIdenticalOrganismsList(Organism organism, Organisms organismEnumValue){
        List<Organism> identicalOrganismsInLocation = new ArrayList<>();
        identicalOrganismsInLocation.add(organism);
        int numberOfIdenticalOrganismsInLocation = getNumberOfIdenticalOrganismsInLocation(organism.getMaxPopulation());
        //Рослина в списку завжди одна, збільшується тільки її маса
        if(organism instanceof Plant plant) {
            double plantWeight = numberOfIdenticalOrganismsInLocation * plant.getWeight();
            plant.setTotalWeight(plantWeight);
            plant.setWeight(plantWeight);
        }
        else {
            for (int i = 1; i < numberOfIdenticalOrganismsInLocation; i++) {
                identicalOrganismsInLocation.add(OrganismFactory.getOrganism(organismEnumValue.toString()));
            }
        }
        return identicalOrganismsInLocation;
    }

    private boolean organismIsPresentInLocation(int opportunityToBePresent){
        return ThreadLocalRandom.current().nextInt(101) <= opportunityToBePresent;
    }

    private int getNumberOfIdenticalOrganismsInLocation(int maxPopulation){
        return ThreadLocalRandom.current().nextInt(1, maxPopulation+1);
    }
}