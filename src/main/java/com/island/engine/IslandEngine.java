package com.island.engine;

import com.island.map.Island;
import com.island.map.Location;
import com.island.utils.islandServices.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class IslandEngine {
    private final Island island;

    public IslandEngine(Island island) {
        this.island = island;
    }

    public void startSimulation(){
        //TODO
        runCircle();
    }

    public void runCircle(){
        try(ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())) {
            for (int i = 0; i < island.getLength(); i++) {
                for (int j = 0; j < island.getHeight(); j++) {
                    int currentX = i;
                    int currentY = j;
                    Location location = island.getLocations()[i][j];
                    service.submit(
                            ()->{
                                FoodService.timeToEat(location);
                                DeathService.deathByStarvation(location);
                                ReproduceService.timeToReproduce(location);
                                RelocationService.startRelocation(island, location, currentX, currentY);
                                RebootingService.rebootAnimalsStats(location);
                            }
                            );
                    System.out.println("location "+i+":"+j+" in work...");
                }
            }
            service.shutdown();
            System.out.println("Всі потоки виконалися: "+service.awaitTermination(2, TimeUnit.SECONDS));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}