package com.tema1.process;

import com.tema1.goods.Goods;

import java.util.LinkedList;
import java.util.List;

public class Bag {
    private List<Goods> cards = new LinkedList<Goods>();
    private int bribe;
    private int label;

    /**
     * Adding a card to the bag.
     * @param card
     */
    public void addCard(final Goods card) {
        this.cards.add(card);
    }

    /**
     * Getting the list of cards stored in the bag.
     * @return cards
     */
    public List<Goods> getCards() {
        return cards;
    }

    /**
     * Getter for cards ids.
     * @return a list of cards ids.
     */
    public List<Integer> getCardsIds() {
        List<Integer> cardsIds = new LinkedList<Integer>();
        for (Goods card : cards) {
            cardsIds.add(card.getId());
        }
        return cardsIds;
    }

    /**
     * Setting the bribe for the Sheriff.
     * @param bribe
     */
    public void setBribe(final int bribe) {
        this.bribe = bribe;
    }

    /**
     * Getting the bribe for the Sheriff.
     * @return bribe
     */
    public int getBribe() {
        return bribe;
    }

    /**
     * Declaring the ID of type of goods.
     * @param label
     */
    public void setLabel(final int label) {
        this.label = label;
    }

    /**
     * Getting the type of goods.
     * @return
     */
    public int getLabel() {
        return label;
    }
}
