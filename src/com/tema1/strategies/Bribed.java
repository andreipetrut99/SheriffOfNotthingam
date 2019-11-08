package com.tema1.strategies;

import com.tema1.process.Bag;
import com.tema1.goods.GoodsType;
import com.tema1.goods.GoodsFactory;
import com.tema1.common.ProfitComparator;

import java.util.List;
import java.util.LinkedList;

import static com.tema1.common.Constants.MAX_BRIBE;
import static com.tema1.common.Constants.MIN_BRIBE;
import static com.tema1.common.Constants.MAX_CARDS_IN_BAG;
import static com.tema1.common.Constants.TWO_CARDS;

public class Bribed extends Basic {
    private int coins;
    private Bag bag;
    private List<Integer> hand;
    private GoodsFactory goodsFactory = GoodsFactory.getInstance();
    private ProfitComparator profitComparator = new ProfitComparator();

    public Bribed() {
        this.hand = null;
    }
    public Bribed(final List<Integer> hand, final int coins) {
        super(hand);
        this.hand = new LinkedList<Integer>(hand);
        this.coins = coins;
    }

    /**
     * Getting the Bag by Bribed Player rules.
     * @return bag.
     */
    public Bag getBribeBag() {
        int auxCoins = coins;
        int noIllegalCards = 0;
        bag = new Bag();
        hand.sort(profitComparator);

        // If there are only Legal Cards OR
        // the player doesn't have money
        // he will get the bag by Basic's Strategy
        if (goodsFactory.getGoodsById(hand.get(0)).getType() == GoodsType.Legal
                || coins <= MIN_BRIBE) {
            return super.getBag();
        }

        for (Integer card : hand) {
            if ((auxCoins - goodsFactory.getGoodsById(card).getPenalty()) > 0
                    && bag.getCards().size() < MAX_CARDS_IN_BAG) {
                auxCoins -= goodsFactory.getGoodsById(card).getPenalty();
                bag.addCard(goodsFactory.getGoodsById(card));
                if (goodsFactory.getGoodsById(card).getType() == GoodsType.Illegal) {
                    noIllegalCards++;
                }
            }
        }
        if (noIllegalCards > 0 && noIllegalCards <= TWO_CARDS) {
            bag.setBribe(MIN_BRIBE);
        } else if (noIllegalCards > TWO_CARDS) {
            bag.setBribe(MAX_BRIBE);
        }

        bag.setLabel(0);
        return bag;
    }
}
