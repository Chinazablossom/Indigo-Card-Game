package indigo


fun main() {
    IndigoCarGame()
}


class IndigoCarGame {


    init {
        println(Ranks.values().joinToString(" ") { it.tag } )
        println(Suits.values().joinToString(" ") { it.icon })
        println(getCards().shuffled().joinToString(" "))

    }


    private fun getCards(): List<String> {
        val allCard = mutableListOf<String>()

        Ranks.values().forEach { rank ->
            Suits.values().forEach { suit ->
                if (Ranks.values().contains(rank) && Suits.values().contains(suit)){
                    val card = Card(rank, suit)
                    allCard.add("${card.rank.tag}${card.suit.icon}")
                }
            }

        }
        return allCard

    }

    inner class Card(val rank: Ranks,val suit: Suits)


    enum class Suits(val icon: String) {
        DIAMONDS("♦"), HEARTS("♥"), SPADES("♠"), CLUBS("♣")
    }

    enum class Ranks(val tag: String) {
        ACE("A"), TWO("2"), THREE("3"), FOUR("4"),
        FIVE("5"), SIX("6"), SEVEN("7"), EIGHT("8"),
        NINE("9"), TEN("10"), JACK("J"), QUEEN("Q"), KING("K")
    }

}