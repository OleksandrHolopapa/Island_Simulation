package com.island.models.animals;

import com.island.map.Location;
import com.island.models.Organism;
import com.island.models.plants.Plant;
import com.island.utils.TableService;
import com.island.utils.factory.OrganismFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class Animal implements Organism {
    protected double weight;
    protected int maxPopulation;
    protected int maxSpeedOfMovement;
    protected double maxCanEat;
    protected Gender gender;
    protected AtomicBoolean alreadyMoved = new AtomicBoolean(false);
    protected boolean alreadyReproduced = false;

    public Animal(double weight, int maxPopulation, int maxSpeedOfMovement, double maxCanEat) {
        this.weight = weight;
        this.maxPopulation = maxPopulation;
        this.maxSpeedOfMovement = maxSpeedOfMovement;
        this.maxCanEat = maxCanEat;
        this.gender = chooseGender();
    }

    public void eat(ConcurrentHashMap<String, List<Organism>> possibleVictims) {
        for (Map.Entry<String, List<Organism>> entry : possibleVictims.entrySet()) {
            String victimSimpleName = entry.getKey();
            int possibilityToEatValue = TableService.getPossibilityToEatValue(this.getClass().getSimpleName(), victimSimpleName);
            //Перевіряємо, чи тварина може з'їсти організм-жертву та, чи є кого їсти
            if ((possibilityToEatValue > 0) && (!entry.getValue().isEmpty())) {
                //визначаємо успіх полювання
                if (ThreadLocalRandom.current().nextInt(101) <= possibilityToEatValue) {
                    Organism victim = entry.getValue().getLast();
                    this.weight += Math.min(this.maxCanEat, victim.getWeight());
                    //Якщо жертва - рослина, то вона втрачає масу, якщо тварина - видаляємо.
                    if (victim instanceof Plant plant) {
                        plant.setWeight(plant.getWeight() - this.maxCanEat);
                    } else {entry.getValue().removeLast();}
                }
                break;
            }
        }
        //постійна втрата ваги тварини
        this.weight -= 1.2 * maxCanEat;
    }

    @Override
    public void reproduce(List<Organism> possiblePartners) {
        if(!this.alreadyReproduced) {
            this.alreadyReproduced = true;
            for (int i = 0; i < possiblePartners.size(); i++) {
                Animal possiblePartner = (Animal) possiblePartners.get(i);
                if((!possiblePartner.alreadyReproduced) && (this.gender!=possiblePartner.gender) && (possiblePartners.size() < this.maxPopulation)) {
                    possiblePartner.alreadyReproduced = true;
                    Animal animalChild = (Animal) OrganismFactory.getOrganism(this.getClass().getSimpleName());
                    animalChild.alreadyReproduced = true;
                    possiblePartners.add(animalChild);
                    break;
                }
            }
        }
    }

    public void move(Location[][] locations, Location location, int currentX, int currentY) {
        if((!this.alreadyMoved.get()) && (this.maxSpeedOfMovement > 0)) {
            int distance = ThreadLocalRandom.current().nextInt(this.maxSpeedOfMovement);
            int destinationX = currentX;
            int destinationY = currentY;
            switch (ThreadLocalRandom.current().nextInt(4)) {
                //нові значення не повинні виходити за розмір масиву
                case 0 -> destinationY = Math.max(0, currentY - distance);
                case 1 -> destinationX = Math.min(locations.length-1, currentX + distance);
                case 2 -> destinationY = Math.min(locations[currentX].length-1, currentY + distance);
                case 3 -> destinationX = Math.max(0, currentX - distance);
            }
            this.alreadyMoved.set(true);
            //Перевірка зміни локації
            if((currentX!=destinationX) || (currentY!=destinationY)) {
                goToNewLocation(this, location, destinationX, destinationY, locations);
            }
        }
    }

    private void goToNewLocation(Organism organism, Location currentLocation, int destinationX, int destinationY, Location[][] locations) {
        String organismSimpleName = organism.getClass().getSimpleName();
        ConcurrentHashMap<String, List<Organism>> organismsInCurrentLocation = currentLocation.getOrganismsInLocation();
        ConcurrentHashMap<String, List<Organism>> organismsInNewLocation = locations[destinationX][destinationY].getOrganismsInLocation();
        List<Organism> organisms = organismsInNewLocation.get(organismSimpleName);
        //Перевіряємо, чи в локації призначення є ідентичні тварини.
        if(organisms == null) {
            organisms = new ArrayList<>();
            organisms.add(organism);
            organismsInNewLocation.put(organismSimpleName, organisms);
            organismsInCurrentLocation.get(organismSimpleName).remove(organism);
        }
        // Якщо ідентичних тварин максимум, переміщення не відбувається
        else if (organisms.size() < organism.getMaxPopulation()) {
            organisms.add(organism);
            organismsInCurrentLocation.get(organismSimpleName).remove(organism);
        }
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public int getMaxPopulation() {
        return maxPopulation;
    }

    private Gender chooseGender(){
        return ThreadLocalRandom.current().nextInt(2) == 0? Gender.MALE:Gender.FEMALE;
    }

    public AtomicBoolean getAlreadyMoved() {
        return alreadyMoved;
    }

    public void setAlreadyReproduced(boolean alreadyReproduced) {
        this.alreadyReproduced = alreadyReproduced;
    }

    enum Gender {
        MALE,
        FEMALE
    }
}