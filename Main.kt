package indigo

import kotlin.random.Random
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    /*val suit= arrayOf(Suit.ACE,Suit.TWO,Suit.THREE,Suit.FOUR,Suit.FIVE,Suit.SIX,Suit.SEVEN,Suit.EIGHT,
    Suit.NINE,Suit.TEN,Suit.JACK,Suit.QUEEN,Suit.KING)
    println(suit.joinToString(" "))

    val rank= arrayOf(Rank.DIAMOND,Rank.SPADE,Rank.HEART,Rank.CLUBS)
    println(rank.joinToString(" "))


   val deck= suit.flatMap { element ->rank.map {element.toString()+it.toString()} }
println(deck.joinToString(separator = " ").toCharArray())*/

//    Deck().askOption()
    GameStart().gamestarts()
}


open class GameStart : gameLogic() {
    var initialDeck = deck
    var initial_cards =
        initialDeck.take(4).also { repeat(4) { initialDeck.removeFirst() } }
    var cards_on_table = mutableListOf<Card>()
    var cards_in_hand_computer: MutableList<Card> = mutableListOf()
    var cards_in_hand: MutableList<Card> = mutableListOf()
    var play_first: String = ""
    var wonCardsUser: MutableList<Card> = mutableListOf()
    var wonCardsComputer: MutableList<Card> = mutableListOf()
    var wonLast: String = ""

    open fun gamestarts() {
        println("Indigo Card Game")
        println("Play first?")
        play_first = readln()
        while (play_first != "yes" && play_first != "no" && play_first != "YES" && play_first != "NO") {
            println("Play first?")
            play_first = readln()
        }

        println(
            "Initial cards on the table: ${
                initial_cards.joinToString(
                    separator = " "
                )
            }"
        )
        cards_on_table.addAll(initial_cards)
        if (play_first == "yes" || play_first == "YES") {
            cards_in_hand = initialDeck.take(6).toMutableList()
                .also { repeat(6) { initialDeck.removeFirst() } }
        } else {
            cards_in_hand_computer = initialDeck.take(6).toMutableList()
                .also { repeat(6) { initialDeck.removeFirst() } }
        }
        if (play_first == "yes" || play_first == "YES") {
            User().userTurn()
        } else {
            Computer().computerTurn()
        }
    }


    inner class Computer : computerAI() {


        fun computerTurn() {
            if (cards_in_hand_computer.size > 0) {
//                val randomIndex = Random.nextInt(cards_in_hand_computer.size)
//                var randomCard = cards_in_hand_computer[randomIndex]
                if (cards_on_table.size == 0) {
                    for (i in 0 until cards_in_hand_computer.size) {
                        print("${cards_in_hand_computer[i]} ")
                    }
                    println()
                    val cartToPlay =
                        noCardsOnTable(cards_on_table, cards_in_hand_computer)
                    println("Computer plays ${cartToPlay}")
                    cards_in_hand_computer.remove(cartToPlay)
                    cards_on_table.add(cartToPlay!!)
                } else {
                    val cardToPlay =
                        CardsOnTableNoCandidate(cards_on_table,cards_in_hand_computer)
                    println("${cards_on_table.size} cards on the table, and the top card is ${cards_on_table.last()}")
                    for (i in 0 until cards_in_hand_computer.size) {
                        print("${cards_in_hand_computer[i]} ")
                    }
                    println()
                    println("Computer plays ${cardToPlay}")
                    if (winsCardsOnTableComputer(
                            cardToPlay!!,
                            cards_in_hand_computer,
                            wonCardsComputer,
                            cards_on_table
                        )
                    ) {
                        sumPoints(wonCardsUser, wonCardsComputer, play_first)
                        wonLast = "computer"
                    } else {
                        cards_in_hand_computer.remove(cardToPlay)
                        cards_on_table.add(cardToPlay!!)
                    }

                }

                User().userTurn()
            } else {
                if (initialDeck.size > 0) {
                    cards_in_hand_computer =
                        initialDeck.take(6).toMutableList()
                            .also { repeat(6) { initialDeck.removeFirst() } }
                    computerTurn()
                } else {
                    println("${cards_on_table.size} cards on the table, and the top card is ${cards_on_table.last()}")
                    dealCardsOnTableWhenNoCardsLeft(
                        play_first,
                        cards_on_table,
                        wonCardsUser,
                        wonCardsComputer,
                        wonLast
                    )
                    exitProcess(0)
                }
            }
        }
    }

    inner class User() {

        fun userTurn() {

            if (cards_in_hand.size > 0) {
                if (cards_on_table.size == 0) {
                    print("Cards in hand: ")
                    for (i in 0 until cards_in_hand.size) {
                        print("${i + 1})${cards_in_hand[i]} ")
                    }
                    println("")
                    var card_to_play: String?
                    do {
                        println("Choose a card to play (1-${cards_in_hand.size}):")
                        card_to_play = readlnOrNull()
                        if (card_to_play == "exit") {
                            println("Game Over")
                            exitProcess(0)
                        }
                    } while (card_to_play == null || card_to_play.toIntOrNull() == null || card_to_play.toInt() !in 1..cards_in_hand.size)
                    cards_on_table.add(cards_in_hand[card_to_play.toInt() - 1])
                    cards_in_hand.remove(cards_in_hand[card_to_play.toInt() - 1])
                } else {
                    println("${cards_on_table.size} cards on the table, and the top card is ${cards_on_table.last()}")
                    print("Cards in hand: ")
                    for (i in 0 until cards_in_hand.size) {
                        print("${i + 1})${cards_in_hand[i]} ")
                    }
                    println("")
                    var card_to_play: String?
                    do {
                        println("Choose a card to play (1-${cards_in_hand.size}):")
                        card_to_play = readlnOrNull()
                        if (card_to_play == "exit") {
                            println("Game Over")
                            exitProcess(0)
                        }
                    } while (card_to_play == null || card_to_play.toIntOrNull() == null || card_to_play.toInt() !in 1..cards_in_hand.size)
                    if (winsCardsOnTableUser(
                            card_to_play.toInt(),
                            cards_in_hand,
                            wonCardsUser,
                            cards_on_table
                        )
                    ) {
                        sumPoints(wonCardsUser, wonCardsComputer, play_first)
                        wonLast = "player"
                    } else {
                        cards_on_table.add(cards_in_hand[card_to_play.toInt() - 1])
                        cards_in_hand.remove(cards_in_hand[card_to_play.toInt() - 1])
                    }
                }

                Computer().computerTurn()
            } else {
                if (initialDeck.size > 0) {
                    cards_in_hand =
                        initialDeck.take(6).toMutableList()
                            .also { repeat(6) { initialDeck.removeFirst() } }
                    userTurn()
                } else {
//                    println("${cards_on_table.size} cards on the table, and the top card is ${cards_on_table.last()}")
                    dealCardsOnTableWhenNoCardsLeft(
                        play_first,
                        cards_on_table,
                        wonCardsUser,
                        wonCardsComputer,
                        wonLast
                    )
                    exitProcess(0)
                }
            }
        }

    }
}

open class gameLogic() : Deck() {

    fun winsCardsOnTableUser(
        cardToPlay: Int,
        cardsOnHand: MutableList<Card>,
        wonCards: MutableList<Card>, cardsOnTable: MutableList<Card>
    ): Boolean {
        return if (cardsOnHand[cardToPlay - 1].suit == cardsOnTable.last().suit || cardsOnHand[cardToPlay - 1].rank == cardsOnTable.last().rank) {
            println("Player wins cards")
            cardsOnTable.add(cardsOnHand[cardToPlay - 1])
            cardsOnHand.remove(cardsOnHand[cardToPlay - 1])
            wonCards.addAll(cardsOnTable)
            cardsOnTable.clear()
            true
        } else {
            false
        }
    }

    fun winsCardsOnTableComputer(
        cardToPlay: Card,
        cardsOnHand: MutableList<Card>,
        wonCards: MutableList<Card>, cardsOnTable: MutableList<Card>
    ): Boolean {
        return if (cardToPlay.suit == cardsOnTable.last().suit || cardToPlay.rank == cardsOnTable.last().rank) {
            println("Computer wins cards")
            cardsOnTable.add(cardToPlay)
            cardsOnHand.remove(cardToPlay)
            wonCards.addAll(cardsOnTable)
            cardsOnTable.clear()
            true
        } else {
            false
        }
    }

    open fun sumPoints(
        wonCardsUser: MutableList<Card>,
        wonCardsComputer: MutableList<Card>,
        playedFirst: String
    ) {
        var temppointsUser = 0
        var temppointsComputer = 0

        for (i in wonCardsUser) {
            if ("A10JQK".contains(i.suit, false)) {
                temppointsUser++
            }
        }
        for (i in wonCardsComputer) {
            if ("A10JQK".contains(i.suit, false)) {
                temppointsComputer++
            }
        }
        println("Score: Player $temppointsUser - Computer $temppointsComputer")
        println("Cards: Player ${wonCardsUser.size} - Computer ${wonCardsComputer.size}")
        println()
        println("No cards on the table")
    }

    open fun sumLastPoints(
        wonCardsUser: MutableList<Card>,
        wonCardsComputer: MutableList<Card>,
        playedFirst: String
    ) {
        var pointsUser = 0
        var pointsComputer = 0
        if (wonCardsUser.size == wonCardsComputer.size) {
            if (playedFirst == "yes" || playedFirst == "YES") {
                pointsUser += 3
            } else {
                pointsComputer += 3
            }
        } else {
            if (wonCardsUser.size > wonCardsComputer.size) pointsUser += 3 else pointsComputer += 3
        }


        for (i in wonCardsUser) {
            if ("A10JQK".contains(i.suit, false)) {
                pointsUser++
            }
        }
        for (i in wonCardsComputer) {
            if ("A10JQK".contains(i.suit, false)) {
                pointsComputer++
            }
        }

        println("Score: Player $pointsUser - Computer $pointsComputer")
        println("Cards: Player ${wonCardsUser.size} - Computer ${wonCardsComputer.size}")
        println("Game Over")
    }

    fun dealCardsOnTableWhenNoCardsLeft(
        playedFirst: String,
        cardsOnTable: MutableList<Card>,
        wonCardsUser: MutableList<Card>,
        wonCardsComputer: MutableList<Card>,
        wonLast: String
    ) {
        if (wonLast == "player") {
            wonCardsUser.addAll(cardsOnTable)
        } else if (wonLast == "computer") {
            wonCardsComputer.addAll(cardsOnTable)
        } else {
            if (playedFirst == "yes" || playedFirst == "YES") {
                wonCardsUser.addAll(cardsOnTable)
            }

            if (playedFirst == "no" || playedFirst == "NO") {
                wonCardsComputer.addAll(cardsOnTable)
            }
        }

        sumLastPoints(wonCardsUser, wonCardsComputer, playedFirst)
    }

}

open class computerAI() : Deck() {

    fun oneCardInHand(
        cardsInHandComputer: MutableList<Card>,
        cardsOnTable: MutableList<Card>
    ): Card {
        if (cardsInHandComputer.size == 1) {
            return cardsInHandComputer[0]
        }
        return cardsInHandComputer[Random.nextInt(
            0,
            cardsInHandComputer.size - 1
        )]
    }

    fun oneCandidateCard(
        cardsInHandComputer: MutableList<Card>,
        cardsOnTable: MutableList<Card>
    ): Card? {
        val candidateCards = cardsInHandComputer.filter { it.suit == cardsOnTable.last().suit || it.rank == cardsOnTable.last().rank }

        return when {
            candidateCards.size >= 2 -> {
                val sameSuitCards = candidateCards.filter { it.suit == cardsOnTable.last().suit }
                if (sameSuitCards.isNotEmpty()) {
                    sameSuitCards.random()
                } else {
                    candidateCards.filter { it.rank == cardsOnTable.last().rank }.random()
                }
            }
            candidateCards.size == 1 -> candidateCards.first()
            else -> null
        }
    }
    fun noCardsOnTable(
        cardsOnTable: MutableList<Card>,
        cardsInHandComputer: MutableList<Card>
    ): Card? {
        var cardToPlay: Card? = null
        if(cardsOnTable.size==0) {
            val cardsBySuit = cardsInHandComputer.groupBy { it.suit }
            // Find a suit that repeats
            val suitToPlay = cardsBySuit.entries.find { it.value.size > 1 }?.value
            // Otherwise, find a rank that repeats
            val cardsByRank = cardsInHandComputer.groupBy { it.rank }
            val rankToPlay = cardsByRank.entries.find { it.value.size > 1 }?.value
            if (rankToPlay!=null) {
                // If there are cards in hand with the same suit, throw one of them at random
                cardToPlay = rankToPlay.shuffled().first()
            }
            else {
                // Find cards with the same rank
                if (suitToPlay!=null) {
                    // If there are no cards in hand with the same suit but there are cards with the same rank, throw one of them at random
                    cardToPlay = suitToPlay.shuffled().first()
                } else {
                    cardToPlay = cardsInHandComputer.shuffled().first()
                }
            }
        }
        else{
            var cardToPlay: Card? = null
        }

        // If there are no cards in hand with the same suit or rank, throw any card at random

        return cardToPlay
    }

    fun CardsOnTableNoCandidate(
        cardsOnTable: MutableList<Card>,
        cardsInHandComputer: MutableList<Card>
    ): Card? {
        var cardToPlay: Card? = oneCandidateCard(cardsInHandComputer,cardsOnTable)
                if(cardToPlay==null) {

                    val cardsBySuit = cardsInHandComputer.groupBy { it.suit }

                    // Find a suit that repeats
                    val suitToPlay = cardsBySuit.entries.find { it.value.size > 1 }?.value

                    // If a suit was found, choose a random card of that suit
                    if (suitToPlay != null) {
                        cardToPlay = suitToPlay.random()
                    }
                    else{
                    // Otherwise, find a rank that repeats
                    val cardsByRank = cardsInHandComputer.groupBy { it.rank }
                    val rankToPlay = cardsByRank.entries.find { it.value.size > 1 }?.value

                    // If a rank was found, choose a random card of that rank
                    if (rankToPlay != null) {
                        cardToPlay =  rankToPlay.random()
                    }
                    else {
                        // Otherwise, choose a random card
                        cardToPlay = cardsInHandComputer.randomOrNull()
                    }
                    }
                }

        return cardToPlay
        }

    }



enum class Suit(val symbol: String) {
    ACE("A"),
    TWO("2"),
    THREE("3"),
    FOUR("4"),
    FIVE("5"),
    SIX("6"),
    SEVEN("7"),
    EIGHT("8"),
    NINE("9"),
    TEN("10"),
    JACK("J"),
    QUEEN("Q"),
    KING("K");

    override fun toString(): String {
        return symbol
    }
}

enum class Rank(val symbol: String) {
    DIAMOND("♦"),
    HEART("♥"),
    SPADE("♠"),
    CLUBS("♣");

    override fun toString(): String {
        return symbol.toString()
    }
}

data class Card(val suit: String, val rank: String) {
    override fun toString(): String {
        return suit + rank
    }
}


open class Deck(
    var deck: MutableList<Card> = mutableListOf<Card>
        (
        Card(Suit.ACE.symbol, Rank.DIAMOND.symbol),
        Card(Suit.ACE.symbol, Rank.HEART.symbol),
        Card(Suit.ACE.symbol, Rank.SPADE.symbol),
        Card(Suit.ACE.symbol, Rank.CLUBS.symbol),
        Card(Suit.TWO.symbol, Rank.DIAMOND.symbol),
        Card(Suit.TWO.symbol, Rank.HEART.symbol),
        Card(Suit.TWO.symbol, Rank.SPADE.symbol),
        Card(Suit.TWO.symbol, Rank.CLUBS.symbol),
        Card(Suit.THREE.symbol, Rank.DIAMOND.symbol),
        Card(Suit.THREE.symbol, Rank.HEART.symbol),
        Card(Suit.THREE.symbol, Rank.SPADE.symbol),
        Card(Suit.THREE.symbol, Rank.CLUBS.symbol),
        Card(Suit.FOUR.symbol, Rank.DIAMOND.symbol),
        Card(Suit.FOUR.symbol, Rank.HEART.symbol),
        Card(Suit.FOUR.symbol, Rank.SPADE.symbol),
        Card(Suit.FOUR.symbol, Rank.CLUBS.symbol),
        Card(Suit.FIVE.symbol, Rank.DIAMOND.symbol),
        Card(Suit.FIVE.symbol, Rank.HEART.symbol),
        Card(Suit.FIVE.symbol, Rank.SPADE.symbol),
        Card(Suit.FIVE.symbol, Rank.CLUBS.symbol),
        Card(Suit.SIX.symbol, Rank.DIAMOND.symbol),
        Card(Suit.SIX.symbol, Rank.HEART.symbol),
        Card(Suit.SIX.symbol, Rank.SPADE.symbol),
        Card(Suit.SIX.symbol, Rank.CLUBS.symbol),
        Card(Suit.SEVEN.symbol, Rank.DIAMOND.symbol),
        Card(Suit.SEVEN.symbol, Rank.HEART.symbol),
        Card(Suit.SEVEN.symbol, Rank.SPADE.symbol),
        Card(Suit.SEVEN.symbol, Rank.CLUBS.symbol),
        Card(Suit.EIGHT.symbol, Rank.DIAMOND.symbol),
        Card(Suit.EIGHT.symbol, Rank.HEART.symbol),
        Card(Suit.EIGHT.symbol, Rank.SPADE.symbol),
        Card(Suit.EIGHT.symbol, Rank.CLUBS.symbol),
        Card(Suit.NINE.symbol, Rank.DIAMOND.symbol),
        Card(Suit.NINE.symbol, Rank.HEART.symbol),
        Card(Suit.NINE.symbol, Rank.SPADE.symbol),
        Card(Suit.NINE.symbol, Rank.CLUBS.symbol),
        Card(Suit.TEN.symbol, Rank.DIAMOND.symbol),
        Card(Suit.TEN.symbol, Rank.HEART.symbol),
        Card(Suit.TEN.symbol, Rank.SPADE.symbol),
        Card(Suit.TEN.symbol, Rank.CLUBS.symbol),
        Card(Suit.JACK.symbol, Rank.DIAMOND.symbol),
        Card(Suit.JACK.symbol, Rank.HEART.symbol),
        Card(Suit.JACK.symbol, Rank.SPADE.symbol),
        Card(Suit.JACK.symbol, Rank.CLUBS.symbol),
        Card(Suit.QUEEN.symbol, Rank.DIAMOND.symbol),
        Card(Suit.QUEEN.symbol, Rank.HEART.symbol),
        Card(Suit.QUEEN.symbol, Rank.SPADE.symbol),
        Card(Suit.QUEEN.symbol, Rank.CLUBS.symbol),
        Card(Suit.KING.symbol, Rank.DIAMOND.symbol),
        Card(Suit.KING.symbol, Rank.HEART.symbol),
        Card(Suit.KING.symbol, Rank.SPADE.symbol),
        Card(Suit.KING.symbol, Rank.CLUBS.symbol)

    )
) {


    fun askOption() {
        println("Choose an action (reset, shuffle, get, exit):")
        var option = readln()
        when (option) {

            "reset" -> reset()
            "shuffle" -> shuffle()
            "get" -> getcards()
            "exit" -> exit()
            "print" -> print()
            else -> println("Wrong action.").also { askOption() }
        }
    }

    fun print() {
        println(deck)
        askOption()
    }

    fun reset() {
        val newdeck = Deck().deck
        deck = newdeck
        println("Card deck is reset.")
        askOption()
    }

    fun shuffle() {
        deck = deck.shuffled().toMutableList()
        println("Card deck is shuffled.")
        askOption()
    }

    fun getcards() {
        println("Number of cards:")
        val numberOfCards = readln().toIntOrNull()

        if (numberOfCards != null) {
            if (numberOfCards in 1..52) {
                if (deck.size >= numberOfCards) {
                    var cardsRemoved = ""
                    repeat(numberOfCards) {
                        cardsRemoved += "${deck.removeFirst()} "

                    }
                    println(cardsRemoved)
                    askOption()
                } else {
                    println("The remaining cards are insufficient to meet the request.")
                    askOption()
                }
            } else {
                println("Invalid number of cards.")
                askOption()
            }
        } else {
            println("Invalid number of cards.")
            askOption()
        }
    }

    fun exit() {
        println("Bye")
        exitProcess(0)
    }

}
