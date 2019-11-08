package com.tema1.process;

import com.tema1.goods.GoodsFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Profit {
    private int profit = 0;
    private Map<Integer, List<Integer>> stand;
    private GoodsFactory goodsFactory = GoodsFactory.getInstance();

    public Profit(final Map<Integer, List<Integer>> stand) {
        this.stand = new HashMap<Integer, List<Integer>>(stand);
    }

    /**
     * Getter for profit of a player.
     * @param id of player.
     * @return profit for respective player.
     */
    public int getProfit(final int id) {
        profit = 0;
        for (int cardId : stand.get(id)) {
            profit += goodsFactory.getGoodsById(cardId).getProfit();
        }
        return profit;
    }
}
