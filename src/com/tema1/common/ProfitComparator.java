package com.tema1.common;

import com.tema1.goods.GoodsFactory;

import java.util.Comparator;

public class ProfitComparator implements Comparator<Integer> {
    private GoodsFactory goodsFactory = GoodsFactory.getInstance();

    @Override
    /**
     * Method that compares profit of two cards.
     */
    public int compare(final Integer card1, final Integer card2) {
        if (goodsFactory.getGoodsById(card2).getProfit()
                == goodsFactory.getGoodsById(card1).getProfit()) {
            return card2 - card1;
        }
        return goodsFactory.getGoodsById(card2).getProfit()
                - goodsFactory.getGoodsById(card1).getProfit();
    }
}
