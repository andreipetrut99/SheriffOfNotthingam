package com.tema1.process;

import com.tema1.goods.GoodsFactory;
import com.tema1.strategies.Basic;
import com.tema1.strategies.Bribed;
import com.tema1.strategies.Greedy;

import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

import static com.tema1.common.Constants.SUP_INDEX;
import static com.tema1.common.Constants.MIN_BASIC_COINS;
import static com.tema1.common.Constants.INF_INDEX;

public class Round {
    private int infIndex;
    private int supIndex;
    private int roundNumber;
    private int sheriffIndex;
    private int merchantIndex;
    private List<String> players;
    private List<Integer> assetsIds;
    private List<Integer> hand;
    private List<Integer> toAddToAssetsIds = new LinkedList<Integer>();
    private Map<Integer, Integer> coins;
    private static Map<Integer, List<Integer>>
            cardsForStand = new HashMap<Integer, List<Integer>>();
    private Bag bag = new Bag();
    private GoodsFactory goodsFactory = GoodsFactory.getInstance();

    public Round(final List<String> players, final List<Integer> assetsIds,
                 final Map<Integer, Integer> money, final int roundNumber) {
        this.players = new LinkedList<String>(players);
        this.assetsIds = new LinkedList<Integer>(assetsIds);
        this.coins = new HashMap<Integer, Integer>(money);
        this.roundNumber = roundNumber;
    }

    /**
     * Starting the round and play the players.size() number of sub-rounds.
     */
    public void startRound() {
        for (int i = 0; i < players.size(); i++) {
            HashMap<Integer, Integer> bribes = new HashMap<Integer, Integer>();
            sheriffIndex = i;

            outerloop:
            for (int j = 0; j < players.size(); j++) {
                if (j != i) {
                    merchantIndex = j;
                } else if (j + 1 < players.size()) {
                    j++;
                    merchantIndex = j;
                } else {
                    break;
                }

                hand = new LinkedList<Integer>(assetsIds.subList(INF_INDEX, SUP_INDEX));
                assetsIds.subList(INF_INDEX, SUP_INDEX).clear();

                switch (players.get(merchantIndex)) {
                    case "basic":
                        Basic basic = new Basic(hand);
                        bag = basic.getBag();
                        break;
                    case "greedy":
                        Greedy greedy = new Greedy(hand, roundNumber, coins.get(merchantIndex));
                        bag = greedy.getGreedyBag();
                        break;
                    case "bribed":
                        Bribed bribe = new Bribed(hand, coins.get(merchantIndex));
                        bag = bribe.getBribeBag();
                        break;
                    default:
                        System.out.println("Error: Could not find the type of player.");
                        break;
                }

                // Inspection
                switch (players.get(sheriffIndex)) {
                    case "basic":
                        Basic basic = new Basic();
                        if (coins.get(sheriffIndex) < MIN_BASIC_COINS) {
                            break outerloop;
                        }
                        basic.inspectBag(bag);
                        if (!basic.isLiar()) {
                            putPenalties(sheriffIndex, merchantIndex, basic.getPenalty());
                        } else {
                            putPenalties(merchantIndex, sheriffIndex, basic.getPenalty());
                        }
                        toAddToAssetsIds.addAll(basic.getConfiscatedCards());
                        addCardsForStand(merchantIndex, basic.getCardsForStand());
                        break;

                    case "greedy":
                        Greedy greedy = new Greedy();
                        greedy.inspect(bag);
                        if (greedy.isLetGo()) {
                            putPenalties(merchantIndex, sheriffIndex, bag.getBribe());
                            addCardsForStand(merchantIndex, bag.getCardsIds());
                        } else {
                            if (coins.get(sheriffIndex) < MIN_BASIC_COINS) {
                                addCardsForStand(merchantIndex, bag.getCardsIds());
                                break outerloop;
                            }
                            if (!greedy.isLiar()) {
                                putPenalties(sheriffIndex, merchantIndex, greedy.getPenalty());
                            } else {
                                putPenalties(merchantIndex, sheriffIndex, greedy.getPenalty());
                            }
                            addCardsForStand(merchantIndex, greedy.getCardsForStand());
                            toAddToAssetsIds.addAll(greedy.getConfiscatedCards());
                        }
                        break;

                    case "bribed":
                        Bribed bribed = new Bribed();

                        if (sheriffIndex == 0) {
                            infIndex = players.size() - 1;
                        } else {
                            infIndex = sheriffIndex - 1;
                        }

                        if (sheriffIndex == players.size() - 1) {
                            supIndex = 0;
                        } else {
                            supIndex = sheriffIndex + 1;
                        }
                        if (coins.get(sheriffIndex) < MIN_BASIC_COINS) {
                            addCardsForStand(merchantIndex, bag.getCardsIds());
                            if (merchantIndex != infIndex && merchantIndex != supIndex) {
                                bribes.put(merchantIndex, bag.getBribe());
                            }
                            break;
                        }

                        if ((merchantIndex == infIndex || merchantIndex == supIndex)
                                && coins.get(sheriffIndex) >= MIN_BASIC_COINS) {
                            bribed.inspectBag(bag);
                            if (!bribed.isLiar()) {
                                putPenalties(sheriffIndex, merchantIndex, bribed.getPenalty());
                            } else {
                                putPenalties(merchantIndex, sheriffIndex, bribed.getPenalty());
                            }
                            toAddToAssetsIds.addAll(bribed.getConfiscatedCards());
                            addCardsForStand(merchantIndex, bribed.getCardsForStand());
                            break;
                        } else {
                            addCardsForStand(merchantIndex, bag.getCardsIds());
                            bribes.put(merchantIndex, bag.getBribe()); // stores the bribes for later
                        }
                        break;

                    default:
                        System.out.println("Error: Could not find the type of player.");
                }
            }

            // If bribed player can take some bribes, he will take them after inspection
            for (int merchant : bribes.keySet()) {
                putPenalties(merchant, sheriffIndex, bribes.get(merchant));
            }
        }
    }

    private void addCardsForStand(final int playerId, final List<Integer> cardsToAdd) {
        if (cardsForStand.containsKey(playerId)) {
            List<Integer> aux = new LinkedList<Integer>(cardsForStand.get(playerId));
            aux.addAll(cardsToAdd);
            cardsForStand.put(playerId, aux);
        } else {
            cardsForStand.put(playerId, cardsToAdd);
        }
    }

    private void putPenalties(final int idPenalized, final int idRewarded, final int coinsNumber) {
        coins.put(idPenalized, coins.get(idPenalized)
                - coinsNumber);
        coins.put(idRewarded, coins.get(idRewarded)
                + coinsNumber);
    }

    /**
     * Getter for confiscated cards. They will be added to assetsIds.
     * @return a list of confiscated cards.
     */
    public List<Integer> getConfiscatedCards() {
        return toAddToAssetsIds;
    }

    /**
     * Getter for the remaining cards in assetsIds after n sub-rounds.
     * @return a list of assets ids.
     */
    public List<Integer> getAssetsIds() {
        return assetsIds;
    }

    /**
     * Getter for cards which passed the inspection.
     * @return cards to be added to Stand.
     */
    public Map<Integer, List<Integer>> getCardsForStand() {
        return cardsForStand;
    }

    /**
     * Getter for remaining coins of each player after this round.
     * @return a map of Player Id and his Coins.
     */
    public Map<Integer, Integer> getRemainingCoins() {
        return coins;
    }
}
