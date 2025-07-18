package com.island.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class TableService {
    private static final List<String> eatingPossibilitiesTable;

    static {
        try {
            eatingPossibilitiesTable = Files.readAllLines(Path.of("C:\\Users\\user\\IdeaProjects\\Project2.IslandSimulation\\src\\main\\resources\\eatingPossibilitiesTable.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static int getVictimPositionInTableTitle(String victim) {
        String tittle = eatingPossibilitiesTable.getFirst();
        int victimPositionInTittle = 0;
        String[] organismsInTittle = tittle.split("\\|");
        while (!organismsInTittle[victimPositionInTittle].equals(victim)) victimPositionInTittle++;
        return victimPositionInTittle;
    }

    public static int getPossibilityToEatValue(String hunter, String victim) {
        int possibilityToEatValue = 0;
        for (String string : eatingPossibilitiesTable) {
            if (string.startsWith(hunter)) {
                String[] stringSplit = string.split("\\|");
                possibilityToEatValue = Integer.parseInt(stringSplit[getVictimPositionInTableTitle(victim)].trim());
                break;
            }
        }
        return possibilityToEatValue;
    }
}