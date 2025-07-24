package com.island.islandServices;

public class NotEnoughTimeToProcessException extends RuntimeException {
    public NotEnoughTimeToProcessException(String message) {
        super("The "+message+" class threads did not have enough time to complete the tasks.");
    }
}
