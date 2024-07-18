package indigo

import kotlin.system.exitProcess
import kotlin.text.StringBuilder


fun main() {
    `Indigo Card Game`()
}

class `Indigo Card Game` {
    val playersCard = mutableSetOf<Card>()
    val computersCard = mutableSetOf<Card>()
    val cardsDeck = mutableSetOf<Card>()
    val cardsOnTable = mutableSetOf<Card>()
    var userWantsToPlayFirst = false


    init {
        println(javaClass.simpleName)
        generateAllCards()
        prompt()
    }

    private fun prompt() {
        println("Play first?")
        val userWantsToPlayFirstInput = readln()
        when (userWantsToPlayFirstInput) {
            "yes" -> userWantsToPlayFirst = true
            "no" -> userWantsToPlayFirst = false
            else -> prompt()
        }
        Game()

    }

    private inner class Game() {

        init {
            getAllPlayersCard()
            println("Initial cards on the table: ${cardsOnTable.joinToString(" ")}")

            do {
                println("\n${cardsOnTable.size} cards on the table, and the top card is ${cardsOnTable.last()}")
                if (userWantsToPlayFirst) displayPlayerPrompt() else computerPrompt()

            } while (cardsOnTable.size != 52)
            exit()
        }

        private fun displayPlayerPrompt() {
            println("Cards in hand: ${playersCard.printPlayerCard()}")
            playerPrompt()
        }

        private fun playerPrompt() {
            println("Choose a card to play (1-${playersCard.size}):")
            val playersChoice = readlnOrNull()
            processInput(playersChoice)
        }

        private fun computerPrompt() {

            val randomNumsList = MutableList(computersCard.size) { it }
            val randomNum = randomNumsList.random()
            val randomComputerCard = computersCard.elementAt(randomNum)
            randomNumsList.remove(randomNum)

            computersCard.remove(randomComputerCard)
            if (computersCard.isEmpty() && cardsDeck.isNotEmpty()) computersCard.resetCard()
            cardsOnTable.add(randomComputerCard)
            println("Computer plays $randomComputerCard")
            userWantsToPlayFirst = true
        }

        private fun processInput(input: Any?) {
            val inputToString = input as? String
            when {
                inputToString == null -> playerPrompt()
                inputToString.equals("exit", true) -> exit()
                inputToString.isBlank() -> playerPrompt()
                else -> {
                    try {
                        val inputToInt = inputToString.toInt()
                        when (inputToInt) {
                            in 1..6 -> {
                                val card = playersCard.elementAtOrNull(inputToInt - 1)
                                if (card != null) {
                                    playersCard.remove(card)
                                    if (playersCard.isEmpty() && cardsDeck.isNotEmpty()) playersCard.resetCard()
                                    cardsOnTable.add(card)
                                    userWantsToPlayFirst = false
                                } else {
                                    displayPlayerPrompt()
                                }
                            }

                            else -> playerPrompt()
                        }
                    } catch (e: NumberFormatException) {
                        playerPrompt()
                    }
                }
            }
        }

        private fun getAllPlayersCard() {
            initialCardsOnTable()
            playersCard.resetCard()
            computersCard.resetCard()
        }

        private fun MutableSet<Card>.resetCard() {
            var counter = 0
            val newCards = mutableListOf<Card>()
            val numberOfCard = 6
            loop@ for (i in cardsDeck.shuffled()) {
                counter++
                this.add(i)
                newCards.add(i)
                if (counter == numberOfCard)
                    break@loop
            }
            counter = 0
            cardsDeck.removeAll(newCards.toSet())
            newCards.clear()
        }

        private fun MutableSet<Card>.printPlayerCard(): String {
            val playersCard = StringBuilder()
            this.forEachIndexed { index, card ->
                playersCard.append("${index + 1})$card ")
            }
            return playersCard.toString()

        }

    }

    private fun generateAllCards(): MutableSet<Card> {
        Ranks.values().forEach { rank ->
            Suits.values().forEach { suit ->
                val card = Card(rank, suit)
                cardsDeck.add(card)
            }
        }
        return cardsDeck
    }

    private fun initialCardsOnTable(): MutableSet<Card> {
        var count = 0
        exit@ for (card in cardsDeck.shuffled()) {
            count++
            cardsOnTable.add(card)
            if (count == 4) break@exit
        }
        cardsDeck.removeAll(cardsOnTable)
        return cardsOnTable

    }

    private fun exit() {
        println(
            if (cardsDeck.isEmpty())
                "${cardsOnTable.size} cards on the table, and the top card is ${cardsOnTable.last()}\nGame Over"
            else "Game Over"
        )
        exitProcess(0)
    }


    inner class Card(val rank: Ranks, val suit: Suits) {
        override fun toString() = "${rank.tag}${suit.icon}"
    }

    enum class Suits(val icon: String) {
        DIAMONDS("♦"), HEARTS("♥"), SPADES("♠"), CLUBS("♣")
    }

    enum class Ranks(val tag: String) {
        ACE("A"), TWO("2"), THREE("3"), FOUR("4"),
        FIVE("5"), SIX("6"), SEVEN("7"), EIGHT("8"),
        NINE("9"), TEN("10"), JACK("J"), QUEEN("Q"), KING("K")
    }


}