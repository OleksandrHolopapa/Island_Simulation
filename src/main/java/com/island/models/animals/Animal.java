package com.island.models.animals;

import com.island.map.Island;
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
    protected AtomicBoolean alreadyAte = new AtomicBoolean(false);
    protected AtomicBoolean alreadyMoved = new AtomicBoolean(false);
    protected AtomicBoolean alreadyReproduced = new AtomicBoolean(false);

    public Animal(double weight, int maxPopulation, int maxSpeedOfMovement, double maxCanEat) {
        this.weight = weight;
        this.maxPopulation = maxPopulation;
        this.maxSpeedOfMovement = maxSpeedOfMovement;
        this.maxCanEat = maxCanEat;
        this.gender = chooseGender();
    }

    public void eat(ConcurrentHashMap<String, List<Organism>> possibleVictims) {
        if(!this.alreadyAte.get()) {
            for (Map.Entry<String, List<Organism>> entry : possibleVictims.entrySet()) {
                String victimSimpleName = entry.getKey();
                int possibilityToEatValue = TableService.getPossibilityToEatValue(this.getClass().getSimpleName(), victimSimpleName);
                //Перевіряємо, чи тварина може з'їсти організм-жертву та, чи є кого їсти
                if ((possibilityToEatValue > 0) && (!entry.getValue().isEmpty())) {
                    alreadyAte.set(true);
                    //визначаємо успіх полювання
                    if (ThreadLocalRandom.current().nextInt(101) <= possibilityToEatValue) {
                        Organism victim = entry.getValue().getLast();
                        this.weight += Math.min(this.maxCanEat, victim.getWeight());
                        //Якщо жертва - рослина, то вона втрачає масу, якщо тварина - видаляємо.
                        if ((victim instanceof Plant plant)) {
                            plant.setWeight(plant.getWeight() - this.maxCanEat);
                            //System.out.println("Plant weight "+plant.getWeight()+" from "+plant.getTotalWeight()+" ate "+this.maxCanEat);
                        } else entry.getValue().removeLast();
                    }
                    break;
                }
            }
            //постійна втрата ваги
            this.weight -= 1.2 * maxCanEat;
        }
    }

    @Override
    public void reproduce(List<Organism> possiblePartners) {
        if(!this.alreadyReproduced.get()) {
            this.alreadyReproduced.set(true);
            for (int i = 0; i < possiblePartners.size(); i++) {
                Animal possiblePartner = (Animal) possiblePartners.get(i);
                if((!possiblePartner.alreadyReproduced.get()) && (this.gender!=possiblePartner.gender) && (possiblePartners.size() < this.maxPopulation)) {
                    possiblePartner.alreadyReproduced.set(true);
                    Animal animalChild = (Animal) OrganismFactory.getOrganism(this.getClass().getSimpleName());
                    animalChild.alreadyReproduced.set(true);
                    possiblePartners.add(animalChild);
                    break;
                }
            }
        }
    }

    public void move(Location location, int currentX, int currentY, Island island) {
        if((!this.alreadyMoved.get()) && (this.maxSpeedOfMovement > 0)) {
            int distance = ThreadLocalRandom.current().nextInt(this.maxSpeedOfMovement);
            int destinationX = currentX;
            int destinationY = currentY;
            switch (ThreadLocalRandom.current().nextInt(4)) {
                //нові значення не повинні виходити за розмір масиву
                case 0 -> destinationY = Math.max(0, currentY - distance);
                case 1 -> destinationX = Math.min(island.getLength()-1, currentX + distance);
                case 2 -> destinationY = Math.min(island.getHeight()-1, currentY + distance);
                case 3 -> destinationX = Math.max(0, currentX - distance);
            }
            this.alreadyMoved.set(true);
            //Перевірка зміни локації
            if((currentX!=destinationX) || (currentY!=destinationY)) {
            goToNewLocation(this, location, destinationX, destinationY, island);
            }
        }
    }

    private void goToNewLocation(Organism organism, Location location, int destinationX, int destinationY, Island island) {
        String simpleName = organism.getClass().getSimpleName();
        ConcurrentHashMap<String, List<Organism>> organismsInCurrentLocation = location.getOrganismsInLocation();
        ConcurrentHashMap<String, List<Organism>> organismsInNewLocation = island.getLocations()[destinationX][destinationY].getOrganismsInLocation();
        List<Organism> organisms = organismsInNewLocation.get(simpleName);
        //Перевіряємо, чи в локації призначення є ідентичні тварини.
        // Якщо ідентичних тварин максимум, переміщення не відбувається
        if(organisms == null) {
            organisms = new ArrayList<>();
            organisms.add(organism);
            organismsInNewLocation.put(simpleName, organisms);
            organismsInCurrentLocation.get(simpleName).remove(organism);
        }
        else if (organisms.size() < organism.getMaxPopulation()) {
            organisms.add(organism);
            organismsInCurrentLocation.get(simpleName).remove(organism);
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

    public AtomicBoolean getAlreadyAte() {
        return alreadyAte;
    }

    public AtomicBoolean getAlreadyMoved() {
        return alreadyMoved;
    }

    public AtomicBoolean getAlreadyReproduced() {
        return alreadyReproduced;
    }

    enum Gender {
        MALE,
        FEMALE
    }
}