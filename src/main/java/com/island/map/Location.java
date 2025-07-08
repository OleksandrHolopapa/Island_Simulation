package com.island.map;

import com.island.models.Organism;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Location {
    ConcurrentHashMap<String, List<Organism>> animalsInLocation = new ConcurrentHashMap<>();
}
