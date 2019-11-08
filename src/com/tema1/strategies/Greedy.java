package com.tema1.strategies;

import com.tema1.goods.GoodsFactory;
import com.tema1.process.Bag;

import java.util.LinkedList;
import java.util.List;

import static com.tema1.common.Constants.MAX_CARDS_IN_BAG;
import static com.tema1.common.Constants.MAX_LEGAL_INDEX;

public class Greedy extends Basic {
    private int roundNumber;
    private boolean letGo = false;
    private int coins;
    private Bag bag;
    private int highestProfitIllegalCard;
    private GoodsFactory goodsFactory = GoodsFactory.getInstance();

    public Greedy() {
        super();
        this.hand = null;
        this.roundNumber = 0;
    }

    public Greedy(final List<Integer> hand, final int roundNumber, final int coins) {
        super(hand);
        this.hand = new LinkedList<Integer>(hand);
        this.roundNumber = roundNumber;
        this.coins = coins;
    }

    /**
     * Getting the bag for inspection by basic strategy and then checking if it
     * is a oven round and adding highest profit illegal card in the bag if there
     * is space for it.
     * @return bag
     */
    public Bag getGreedyBag() {
        bag = super.getBag();
        List<Integer> auxiliarHand = new LinkedList<Integer>(hand);

        for (int i = 0; i < bag.getCardsIds().size(); i++) {
            if (auxiliarHand.contains(bag.getCardsIds().get(i))) {
                auxiliarHand.remove(bag.getCardsIds().get(i));
            }
        }

        if (this.roundNumber % 2 == 0) {
            highestProfitIllegalCard = 0;
            if (bag.getCards().size() < MAX_CARDS_IN_BAG) {
                for (Integer cardId : auxiliarHand) {
                    if (cardId > MAX_LEGAL_INDEX
                            && goodsFactory.getGoodsById(cardId).getProfit()
                            > goodsFactory.getGoodsById(highestProfitIllegalCard).getProfit()) {
                        highestProfitIllegalCard = cardId;
                    }
                }
                if (highestProfitIllegalCard != 0
                        && (coins - goodsFactory
                                .getGoodsById(highestProfitIllegalCard)
                                .getPenalty()) > 0) {
                    bag.addCard(goodsFactory.getGoodsById(highestProfitIllegalCard));
                }
            }
        }
        return bag;
    }

    /**
     * Inspecting the bag if there is no bribe and take the bribe if exists.
     * @param bagToInspect
     */
    public void inspect(final Bag bagToInspect) {
        letGo = false;
        if (bagToInspect.getBribe() != 0) {
            letGo = true;
        } else {
            super.inspectBag(bagToInspect);
        }
    }

    /**
     * Check if boolean letGo is true or false.
     * @return true or false
     */
    public boolean isLetGo() {
        if (letGo) {
            return true;
        }
        return false;
    }
}
