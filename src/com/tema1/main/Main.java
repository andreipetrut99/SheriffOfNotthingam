package com.tema1.main;

import com.tema1.process.Round;
import com.tema1.process.Profit;
import com.tema1.common.Constants;
import com.tema1.process.ScoreBoard;
import com.tema1.process.IllegalBonus;
import com.tema1.process.KingQueenBonus;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.LinkedList;

import static com.tema1.common.Constants.INITIAL_MONEY;

public final class Main {
    private Main() {
        // just to trick checkstyle
    }

    public static void main(final String[] args) {
        GameInputLoader gameInputLoader = new GameInputLoader(args[0], args[1]);
        GameInput gameInput = gameInputLoader.load();

        int rounds = gameInput.getRounds();
        List<Integer> assetsIds = gameInput.getAssetIds();
        List<String> names = gameInput.getPlayerNames();
        Map<Integer, List<Integer>> stand = new HashMap<Integer, List<Integer>>();
        Map<Integer, Integer> coins = new HashMap<Integer, Integer>();

        for (int j = 0; j < names.size(); j++) {
            coins.put(j, INITIAL_MONEY);
        }

        for (int i = 1; i <= rounds && i <= Constants.MAX_ROUNDS; i++) {
            Round round = new Round(names, assetsIds, coins, i);
            round.startRound();
            assetsIds = new LinkedList<Integer>(round.getAssetsIds());
            assetsIds.addAll(round.getConfiscatedCards());

            stand.putAll(round.getCardsForStand());
            coins = new HashMap<Integer, Integer>(round.getRemainingCoins());

            for (int j = 0; j < names.size(); j++) {
                coins.put(j, coins.get(j));
            }
        }

        IllegalBonus illegalBonus = new IllegalBonus(stand);

        for (int j = 0; j < names.size(); j++) {
            List<Integer> aux = new LinkedList<Integer>(stand.get(j));
            if (illegalBonus.addIllegalBonus(j) != null) {
                aux.addAll(illegalBonus.addIllegalBonus(j));
                stand.put(j, aux);
            }
        }

        Profit profit = new Profit(stand);
        KingQueenBonus kingQueenBonus = new KingQueenBonus(stand);
        kingQueenBonus.kingQueenBonus();

        for (int j = 0; j < names.size(); j++) {
            coins.put(j, coins.get(j)
                    + profit.getProfit(j)
                    + kingQueenBonus.getBonus(j));
        }

        ScoreBoard sc = new ScoreBoard(coins, names);
        sc.sortPlayers();
        sc.printScoreBoard();
    }
}
