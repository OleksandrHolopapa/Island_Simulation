package com.island.engine;

import com.island.islandServices.*;
import com.island.map.Island;
import com.island.map.Location;

import java.util.concurrent.*;

public class IslandEngine {
    private int reproducePeriod = 2;
    private final Island island;
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> future;

    public IslandEngine(Island island) {
        this.island = island;
    }

    public void startSimulation(){
        island.startService(new InitialisationService());
        island.showOrganismCountInLocations();
        future = executorService.scheduleWithFixedDelay(this::runCircle, 0, 500, TimeUnit.MILLISECONDS);
        try {
            //після 5 хв симуляція зупиниться
            //погана ідея
            Thread.sleep(60_000);
            future.cancel(false);
            executorService.shutdown();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void runCircle(){
        int period = reproducePeriod;
        island.startService(new FoodService());
        island.startService(new DeathService());
        //розмноження раз на два цикли
        if(period == 1) island.startService(new ReproduceService());
        island.startService(new RelocationService());
        island.startService(new RebootingService());
        island.showIslandStatistic();
        //if(period==3) {future.cancel(false); executorService.shutdown(); Thread.currentThread().interrupt();}
        changeReproductionPeriod();
        System.out.println("======================================================================================");
    }

    private void changeReproductionPeriod(){
        reproducePeriod--;
        if(reproducePeriod==0) reproducePeriod = 2;
    }
}