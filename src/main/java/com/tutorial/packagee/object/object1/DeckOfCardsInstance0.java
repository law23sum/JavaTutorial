package com.tutorial.packagee.object.object1;

import com.tutorial.packagee.object.object1.deck.Card;
import com.tutorial.packagee.object.object1.deck.Deck;

public class DeckOfCardsInstance0 {
    public static void main(String[] args) {
        Deck deck = new Deck();
        for (int suit = Card.DIAMONDS; suit <= Card.SPADES; suit++) {
            for (int rank = Card.ACE; rank <= Card.KING; rank++) {
                Card card = deck.getCard(suit, rank);
                System.out.format("%s of %s%n",
                        Card.rankToString(card.getRank()),
                        Card.suitToString(card.getSuit()));
            }
        }
    }
}
