package com.tema1.strategies;

import com.tema1.goods.Goods;
import com.tema1.goods.GoodsFactory;
import com.tema1.goods.GoodsType;
import com.tema1.process.Bag;

import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

import static com.tema1.common.Constants.MAX_CARDS_IN_BAG;
import static com.tema1.common.Constants.MAX_LEGAL_INDEX;

public class Basic {
    protected int penalty;
    private int highestFrequency = 0;
    private int highestFrequencyKey = -1;
    private int highestProfitKey = -1;
    private int highestProfit = 0;
    private int profit = 0;
    protected boolean liar = false;
    private boolean onlyIllegalCards = true;
    protected List<Integer> hand;
    private List<Integer> cardsForStand = new LinkedList<Integer>();
    private List<Integer> confiscatedCards = new LinkedList<Integer>();;
    private Map<Integer, Integer> frequency = new HashMap<Integer, Integer>();
    private GoodsFactory goodsFactory = GoodsFactory.getInstance();

    public Basic() {
        this.hand = null;
    }

    public Basic(final List<Integer> hand) {
        this.hand = new LinkedList<Integer>(hand);
    }
    /**
     * Preparing the bag for inspection following the rules of Basic Strategy.
     * @return the bag
     */
    public Bag getBag() {
        Bag bag = new Bag();
        for (int card : hand) {
            frequency.put(card, frequency.getOrDefault(card, 0) + 1);
            if (goodsFactory.getGoodsById(card).getType() == GoodsType.Legal) {
                onlyIllegalCards = false;
            }
        }

        for (Integer key : frequency.keySet()) {
             if (!onlyIllegalCards) {
                 if (key <= MAX_LEGAL_INDEX) {
                     if (frequency.get(key) > highestFrequency) {
                         highestFrequency = frequency.get(key);
                         highestFrequencyKey = key;
                     } else if (frequency.get(key) == highestFrequency) {
                         if (goodsFactory.getGoodsById(key).getProfit()
                                 > goodsFactory.getGoodsById(highestFrequencyKey).getProfit()) {
                             highestFrequency = frequency.get(key);
                             highestFrequencyKey = key;
                         } else if (goodsFactory.getGoodsById(key).getProfit()
                                 == goodsFactory.getGoodsById(highestFrequencyKey).getProfit()) {
                             if (key > highestFrequencyKey) {
                                 highestFrequency = frequency.get(key);
                                 highestFrequencyKey = key;
                             }
                         }
                     }
                 }
             } else {
                 if (goodsFactory.getGoodsById(key).getProfit() > highestProfit) {
                     highestProfitKey = key;
                     highestProfit = goodsFactory.getGoodsById(key).getProfit();
                 }
             }
         }

        if (onlyIllegalCards) {
            bag.addCard(goodsFactory.getGoodsById(highestProfitKey));
            bag.setLabel(0);
            bag.setBribe(0);
        } else {
            for (int i = 0; i < highestFrequency; i++) {
                if (bag.getCards().size() < MAX_CARDS_IN_BAG) {
                    bag.addCard(goodsFactory.getGoodsById(highestFrequencyKey));
                }
            }
            bag.setLabel(highestFrequencyKey);
            bag.setBribe(0);
        }
        return bag;
    }


    /**
     * Inspecting the bag and see if a player is a liar or not.
     * @param bag
     * @return confiscatedCards with penalty as a last element.
     */
    public void inspectBag(final Bag bag) {
        penalty = 0;
        confiscatedCards.clear();
        cardsForStand.clear();
        liar = false;
        for (Goods card : bag.getCards()) {
            if (card.getId() != bag.getLabel()) {
                liar = true;
                penalty += card.getPenalty();
                confiscatedCards.add(card.getId());
            } else {
                cardsForStand.add(card.getId());
            }
        }
        if (penalty == 0) {
            penalty = bag.getCards().size() * (goodsFactory
                    .getGoodsById(bag.getLabel())).getPenalty();
        }
    }


    /**
     * Getter for a Confiscated Cards.
     * @return a list of confiscated cards ids.
     */
    public List<Integer> getConfiscatedCards() {
        return confiscatedCards;
    }


    /**
     * Getter for Cards which will be added to stand.
     * @return a list of cards ids.
     */
    public List<Integer> getCardsForStand() {
        return cardsForStand;
    }

    /**
     * Getter for the penalty.
     * @return penalty.
     */
    public int getPenalty() {
        return penalty;
    }

    /**
     * Check if a player is lying or not.
     * @return boolean liar.
     */
    public boolean isLiar() {
        return liar;
    }

}
