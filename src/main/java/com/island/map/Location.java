package com.island.map;

import com.island.models.Organism;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Location {
    private final ConcurrentHashMap<String, List<Organism>> OrganismsInLocation = new ConcurrentHashMap<>();

    public ConcurrentHashMap<String, List<Organism>> getOrganismsInLocation() {
        return OrganismsInLocation;
    }
}