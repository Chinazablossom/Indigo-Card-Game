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
    var playerScore = 0
    var computerScore = 0
    var playerCardsWon = mutableSetOf<Card>()
    var computerCardsWon = mutableSetOf<Card>()
    var lastWinnerIsPlayer: Boolean? = null
    var candidateCard = mutableSetOf<Card>()


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
            gameLoop()
            endGame()
        }


        private fun gameLoop() {
            do {
                println("\n${if (cardsOnTable.isEmpty()) "No cards on the table" else "${cardsOnTable.size} cards on the table, and the top card is ${cardsOnTable.last()}"}")
                if (userWantsToPlayFirst) displayPlayerPrompt() else computerPrompt()
            } while (cardsDeck.isNotEmpty() || playersCard.isNotEmpty() || computersCard.isNotEmpty())
        }

        private fun displayPlayerPrompt() {
            println("Cards in hand: ${playersCard.currentPlayerCard()}")
            playerPrompt()
        }

        private fun playerPrompt() {
            println("Choose a card to play (1-${playersCard.size}):")
            val playersChoice = readlnOrNull()
            processInput(playersChoice)
        }

        private fun computerPrompt() {
            println( computersCard)
            val randomComputerCard = computersCard.random()
            computersCard.remove(randomComputerCard)
            if (computersCard.isEmpty() && cardsDeck.isNotEmpty()) computersCard.resetCard()
            cardsOnTable.add(randomComputerCard)
            println("Computer plays $randomComputerCard")
            checkWinCondition(randomComputerCard, false)
            userWantsToPlayFirst = true

        }

        private fun computersStategy(){
            when {
                computersCard.size == 1 -> candidateCard.add(computersCard.last())

            }

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
                            in 1..playersCard.size -> {
                                val card = playersCard.elementAtOrNull(inputToInt - 1)
                                if (card != null) {
                                    playersCard.remove(card)
                                    cardsOnTable.add(card)
                                    if (playersCard.isEmpty() && cardsDeck.isNotEmpty()) playersCard.resetCard()
                                    checkWinCondition(card, true)
                                    userWantsToPlayFirst = false
                                } else {
                                    playerPrompt()
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

        private fun checkWinCondition(playedCard: Card, isPlayer: Boolean) {
            val topCard = cardsOnTable.elementAtOrNull(cardsOnTable.size - 2)
            if (topCard != null && (playedCard.suit == topCard.suit || playedCard.rank == topCard.rank)) {
                if (isPlayer) {
                    println("Player wins cards")
                    playerCardsWon.addAll(cardsOnTable)
                    playerScore += calculatePoints(cardsOnTable)
                    lastWinnerIsPlayer = true
                } else {
                    println("Computer wins cards")
                    computerCardsWon.addAll(cardsOnTable)
                    computerScore += calculatePoints(cardsOnTable)
                    lastWinnerIsPlayer = false
                }
                cardsOnTable.clear()
                displayScore()
            }
        }

        private fun calculatePoints(cards: Set<Card>): Int {
            return cards.count { it.rank in listOf(Ranks.ACE, Ranks.TEN, Ranks.JACK, Ranks.QUEEN, Ranks.KING) }
        }

        private fun displayScore() {
            println("Score: Player $playerScore - Computer $computerScore")
            println("Cards: Player ${playerCardsWon.size} - Computer ${computerCardsWon.size}")
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
            cardsDeck.removeAll(newCards.toSet())
            newCards.clear()
        }

        private fun MutableSet<Card>.currentPlayerCard(): String {
            val playersCard = StringBuilder()
            this.forEachIndexed { index, card ->
                playersCard.append("${index + 1})$card ")
            }
            return playersCard.toString()
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

        private fun endGame() {

            val remainingCards = cardsOnTable.size
            if (remainingCards > 0) {
                if (lastWinnerIsPlayer == true) {
                    playerCardsWon.addAll(cardsOnTable)
                    playerScore += calculatePoints(cardsOnTable)
                } else if (lastWinnerIsPlayer == false) {
                    computerCardsWon.addAll(cardsOnTable)
                    computerScore += calculatePoints(cardsOnTable)
                } else if (userWantsToPlayFirst) {
                    playerCardsWon.addAll(cardsOnTable)
                    playerScore += calculatePoints(cardsOnTable)
                } else {
                    computerCardsWon.addAll(cardsOnTable)
                    computerScore += calculatePoints(cardsOnTable)
                }
            }

            if (playerCardsWon.size > computerCardsWon.size) {
                playerScore += 3
            } else if (playerCardsWon.size < computerCardsWon.size) {
                computerScore += 3
            } else {
                if (userWantsToPlayFirst) {
                    playerScore += 3
                } else {
                    computerScore += 3
                }
            }

            println("\n${if (cardsOnTable.isEmpty()) "No cards on the table" else "${cardsOnTable.size} cards on the table, and the top card is ${cardsOnTable.last()}"}")
            displayScore()
            println("Game Over")
            exitProcess(0)

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