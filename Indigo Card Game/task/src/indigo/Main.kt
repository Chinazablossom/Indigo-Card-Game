package indigo

import java.lang.NumberFormatException
import kotlin.system.exitProcess


fun main() {
    IndigoCarGame()
}


class IndigoCarGame {
    val cards = mutableSetOf<String>()
    val cardsDeck = mutableSetOf<String>()


    init {
        generateAllCards()
        prompt()
    }

    private fun prompt() {
        do {
            println("Choose an action (reset, shuffle, get, exit):")
            val userAction = readln().trim()
            when (userAction.lowercase()) {
                "reset" -> resetCards()
                "shuffle" -> shuffleCards()
                "get" -> getCards()
                "exit" -> exit()
                else -> {
                    println("Wrong action.")
                }

            }

        } while (true)
    }

    private fun resetCards() {
        cardsDeck.addAll(cards)
        cards.clear()
        println("Card deck is reset.")
    }

    private fun shuffleCards() {
        cardsDeck.shuffled()
        println("Card deck is shuffled.")
    }

    private fun getCards() {
        try {
            println("Number of cards:")
            var counter = 0
            val newCards = mutableListOf<String>()

            val numberOfCard = readln().toInt()
            validateUserInputIsInt(numberOfCard)

            loop@ for (i in cardsDeck) {
                counter++
                cards.add(i)
                newCards.add(i)
                if (counter == numberOfCard)
                    break@loop
            }
            counter = 0

            cardsDeck.removeAll(cards)
            println(newCards.shuffled().joinToString(" "))

            newCards.clear()

        } catch (e: NumberFormatException) {
            println("Invalid number of cards.")
        } catch (e: Exception) {
            println(e.message)
        }


    }

    private fun generateAllCards(): MutableSet<String> {
        Ranks.values().forEach { rank ->
            Suits.values().forEach { suit ->
                val card = Card(rank, suit)
                cardsDeck.add("${card.rank.tag}${card.suit.icon}")
            }
        }
        return cardsDeck.shuffled().toMutableSet()

    }

    private fun exit() {
        println("Bye")
        exitProcess(0)
    }

    private fun validateUserInputIsInt(number: Any) {
        if (number is Int && number !in 1..52 && cardsDeck.size <= 52) throw Exception("Invalid number of cards.")
        if (number is Int && number > cardsDeck.size) throw Exception("The remaining cards are insufficient to meet the request.")
        if (number !is Int) throw Exception("Invalid number of cards.")
    }

    inner class Card(val rank: Ranks, val suit: Suits)

    enum class Suits(val icon: String) {
        DIAMONDS("♦"), HEARTS("♥"), SPADES("♠"), CLUBS("♣")
    }

    enum class Ranks(val tag: String) {
        ACE("A"), TWO("2"), THREE("3"), FOUR("4"),
        FIVE("5"), SIX("6"), SEVEN("7"), EIGHT("8"),
        NINE("9"), TEN("10"), JACK("J"), QUEEN("Q"), KING("K")
    }


}