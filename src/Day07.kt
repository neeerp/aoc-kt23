fun main() {

    /**
     * Sort the poker hands and multiply their bid.
     */
    fun part1(input: List<String>): Int {
        var hands = input.map { it.split(' ') }.map { Hand(it[0], it[1].toInt()) }
        hands = hands.sortedWith(Hand::compareTo)

        return hands.withIndex().sumOf { (it.index+1) * it.value.bid }
    }


    /**
     */
    fun part2(input: List<String>): Int {
        return 0
    }

    val input = readInput("Day07")
    val testInput = readInput("Day07_test")

    check(part1(testInput) == 6459)
    part1(input).println()

//    check(part2(testInput) == 71503)
//    part2(input).println()
}

class Hand(val hand: String, val bid: Int) {
    val handType: HandType
    init {
        val counts = hand.groupingBy { it }.eachCount().values.groupingBy { it }.eachCount()

        handType = if (counts.contains(5)) {
            HandType.FIVE_OF_A_KIND
        } else if (counts.contains(4)) {
            HandType.FOUR_OF_A_KIND
        } else if (counts.contains(3) && counts.contains(2)) {
            HandType.FULL_HOUSE
        } else if (counts.contains(3)) {
            HandType.THREE_OF_A_KIND
        } else if (counts.getOrElse(2) { 0 } == 2) {
            HandType.TWO_PAIR
        } else if (counts.contains(2)) {
            HandType.ONE_PAIR
        } else {
            HandType.HIGH_CARD
        }
    }
}


fun cardToVal(card: Char): Int {
    return when (card) {
        'A' -> {
            14
        }
        'K' -> {
            13
        }
        'Q' -> {
            12
        }
        'J' -> {
            11
        }
        'T' -> {
            10
        }
        else -> {
            card.digitToInt()
        }
    }
}
fun Hand.compareTo(other: Hand): Int {
    // Better hand wins
    if (this.handType < other.handType) {
        return 1
    } else if (this.handType > other.handType) {
        return -1
    }

    // Ordered high card wins
    for ((a,b) in this.hand.zip(other.hand)) {
        if (cardToVal(a) > cardToVal(b)) {
            return 1
        } else if (cardToVal(a) < cardToVal(b)) {
            return - 1
        }
    }

    return 0
}

enum class HandType {
    FIVE_OF_A_KIND,
    FOUR_OF_A_KIND,
    FULL_HOUSE,
    THREE_OF_A_KIND,
    TWO_PAIR,
    ONE_PAIR,
    HIGH_CARD
}
