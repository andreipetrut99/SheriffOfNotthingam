package com.tema1.process;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

public class ScoreBoard {
    private Map<Integer, Integer> coins;
    private Map<Integer, Integer> sorted;
    private List<String> playerNames;

    public ScoreBoard(final Map<Integer, Integer> coins, final List<String> playerNames) {
        this.coins = new HashMap<Integer, Integer>(coins);
        this.playerNames = new LinkedList<String>(playerNames);
    }

    /**
     * Method which sort the players by the number of coins.
     */
    public void sortPlayers() {
       sorted = this.coins.entrySet()
               .stream()
               .sorted((Map.Entry.<Integer, Integer>comparingByValue().reversed()))
               .collect(Collectors.toMap(Map.Entry::getKey,
                       Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    /**
     * Method that prints the final Score Board.
     */
    public void printScoreBoard() {
        for (int key : sorted.keySet()) {
            System.out.println(key + " "
                    + playerNames.get(key).toUpperCase()
                    + " " + sorted.get(key));
        }
    }
}
