package com.island;

import com.island.engine.IslandEngine;
import com.island.map.Island;

public class Main {
    public static void main(String[] args) {
        Island island = new Island(100, 20);
        IslandEngine engine = new IslandEngine(island);
        engine.startSimulation();
    }
}