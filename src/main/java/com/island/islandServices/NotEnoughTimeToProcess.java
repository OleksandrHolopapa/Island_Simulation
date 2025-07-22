package com.island.islandServices;

public class NotEnoughTimeToProcess extends RuntimeException {
    public NotEnoughTimeToProcess(String message) {
        super("The "+message+" class threads did not have enough time to complete the tasks.");
    }
}
