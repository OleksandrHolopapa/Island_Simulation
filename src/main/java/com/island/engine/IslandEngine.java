package com.island.engine;

import com.island.islandServices.*;
import com.island.map.Island;
import com.island.map.Location;
import com.island.models.Organism;
import com.island.models.animals.Animal;

import java.util.List;
import java.util.concurrent.*;

public class IslandEngine {
    private int currentDay = 1;
    private int reproducePeriod = 2;
    private final Island island;
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> future;

    public IslandEngine(Island island) {
        this.island = island;
    }

    public void startSimulation(){
        island.startService(new InitialisationService());
        future = executorService.scheduleWithFixedDelay(this::runCircle, 0, 500, TimeUnit.MILLISECONDS);
    }

    private void runCircle(){
        System.out.println("========= DAY №"+(currentDay++)+" ==========");
        island.startService(new FoodService());
        island.startService(new DeathService());
        //розмноження раз на два цикли
        if(reproducePeriod == 1) island.startService(new ReproduceService());
        island.startService(new RelocationService());
        island.startService(new RebootingService());
        island.showIslandStatistic();
        stopSimulation();
        if(stopSimulation()) {
            future.cancel(false); executorService.shutdown();
            System.out.println("All the animals died!!!");
        }
        changeReproductionPeriod();
    }

    private void changeReproductionPeriod(){
        reproducePeriod--;
        if(reproducePeriod==0) reproducePeriod = 2;
    }

    private boolean stopSimulation() {
        boolean simulationStopped = true;
        for (Location[] locations : island.getLocations()) {
            for (Location location : locations) {
                ConcurrentHashMap<String, List<Organism>> organismsInLocation = location.getOrganismsInLocation();
                for (List<Organism> value : organismsInLocation.values()) {
                    if((!value.isEmpty()) && (value.getLast() instanceof Animal)) simulationStopped = false;
                }
            }
        }
        return simulationStopped;
    }
}