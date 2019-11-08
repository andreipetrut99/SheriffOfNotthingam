package com.tema1.process;

import com.tema1.goods.GoodsFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tema1.common.Constants.NUMBER_OF_ILLEGAL_GOODS;

public class KingQueenBonus {
    private boolean foundQueen, foundKing;
    private int max, second, frequency, kingID, queenID;
    private Map<Integer, List<Integer>> stand = new HashMap<Integer, List<Integer>>();
    private Map<Integer, Integer> bonus = new HashMap<Integer, Integer>();

    public KingQueenBonus(final Map<Integer, List<Integer>> stand) {
        this.stand.putAll(stand);
    }

    /**
     * Method which calculates the King and Queen bonus for each card.
     */
    public void kingQueenBonus() {
        GoodsFactory goodsFactory = GoodsFactory.getInstance();
        for (int i = 0; i < goodsFactory.getAllGoods().size() - NUMBER_OF_ILLEGAL_GOODS; i++) {
            max = 0;
            second = 0;
            foundKing = false;
            foundQueen = false;

            for (int key : stand.keySet()) {
                frequency = 0;
                for (int cardID : stand.get(key)) {
                    if (cardID == i) {
                        frequency++;
                    }
                }
                if (frequency > max) {
                    max = frequency;
                    kingID = key;
                    foundKing = true;
                }
            }
            max = 0;
            for (int key : stand.keySet()) {
                if (key != kingID) {
                    frequency = 0;
                    for (int cardID : stand.get(key)) {
                        if (cardID == i) {
                            frequency++;
                        }
                    }
                    if (frequency > max) {
                        max = frequency;
                        queenID = key;
                        foundQueen = true;
                    }
                }
            }


            if (foundKing) {
                if (bonus.containsKey(kingID)) {
                    bonus.put(kingID, bonus.get(kingID) + goodsFactory.getKingBonus(i));
                } else {
                    bonus.put(kingID, goodsFactory.getKingBonus(i));
                }
            }

            if (foundQueen) {
                if (bonus.containsKey(queenID)) {
                    bonus.put(queenID, bonus.get(queenID) + goodsFactory.getQueenBonus(i));
                } else {
                    bonus.put(queenID, goodsFactory.getQueenBonus(i));
                }
            }
        }
    }

    /**
     * Getter for bonus after checking if a player is Queen or King.
     * @param playerId which is to be checked
     * @return the integer bonus if player is Queen or King or 0 otherwise
     */
    public int getBonus(final int playerId) {
        if (bonus.containsKey(playerId)) {
            return bonus.get(playerId);
        }

        return 0;
    }
}
