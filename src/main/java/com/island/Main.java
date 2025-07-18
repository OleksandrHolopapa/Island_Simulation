package com.island;

import com.island.engine.IslandEngine;
import com.island.map.Island;
import com.island.map.Location;
import com.island.utils.islandServices.*;

public class Main {
    public static void main(String[] args) {
        Island island = new Island(2,2);
        island.islandInitialisation();
        island.showOrganismCountInLocations();

        //Запуск багатопоточності
        IslandEngine engine = new IslandEngine(island);
        engine.startSimulation();

        //Запуск багатопоточності може давати помилки. Чому?
        //Після роботи кожного сервісу отримуємо повідомлення про завершення його роботи
        //4 клітинки - 4 повідомлення для одного сервісу
        //В багатопоточному варіанті не завжди по чотири повідомлення, іноді по 3 чи 2 повідомлення на сервіс
        //Не можу зрозуміти, чому

        //Наступні цикли запускають процеси на острові в одному потоці. Все працює правильно
        /*for (Location[] location : island.getLocations()) {
            for (Location currentLocation : location) {
                FoodService.timeToEat(currentLocation);
            }
        }

        for (Location[] location : island.getLocations()) {
            for (Location currentLocation : location) {
                DeathService.deathByStarvation(currentLocation);
            }
        }

        for (Location[] location : island.getLocations()) {
            for (Location currentLocation : location) {
                ReproduceService.timeToReproduce(currentLocation);
            }
        }

        for (int i = 0; i < island.getLocations().length; i++) {
            for (int j = 0; j < island.getLocations()[i].length; j++) {
                Location location = island.getLocations()[i][j];
                RelocationService.startRelocation(island, location, i, j);
            }
        }

        for (Location[] location : island.getLocations()) {
            for (Location currentLocation : location) {
                RebootingService.rebootAnimalsStats(currentLocation);
            }
        }*/

        System.out.println("After--------------------------------------------------------------------");
        island.showOrganismCountInLocations();
    }
}