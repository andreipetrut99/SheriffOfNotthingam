package com.tema1.process;

import com.tema1.goods.Goods;
import com.tema1.goods.GoodsFactory;

import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

import static com.tema1.common.Constants.MAX_LEGAL_INDEX;

public class IllegalBonus {
    private Map<Integer, List<Integer>> stand;
    private Map<Integer, List<Integer>> finalStand;
    private List<Integer> aux = new LinkedList<Integer>();
    private GoodsFactory goodsFactory = GoodsFactory.getInstance();

    public IllegalBonus(final Map<Integer, List<Integer>> stand) {
        this.stand = new HashMap<Integer, List<Integer>>(stand);
    }

    /**
     * Method that add bonus cards for illegal cards.
     * @param id of player
     * @return a list of bonus cards.
     */
    public List<Integer> addIllegalBonus(final int id) {
        aux.clear();
        for (int cardId : stand.get(id)) {
            if (cardId > MAX_LEGAL_INDEX) {
                for (Goods bonusCard : goodsFactory
                        .getGoodsById(cardId)
                        .getIllegalBonus()
                        .keySet()) {
                    for (int i = 0; i < goodsFactory
                            .getGoodsById(cardId)
                            .getIllegalBonus()
                            .get(bonusCard); i++) {
                        aux.add(bonusCard.getId());
                    }
                }
            }
        }
        return aux;
    }
}
