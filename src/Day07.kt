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
        var hands = input.map { it.split(' ') }.map { Hand(it[0], it[1].toInt()) }
        hands = hands.sortedWith(Hand::compareToWithJoker)

        val result = hands.withIndex().sumOf { (it.index+1) * it.value.bid }
        return result
    }

    val input = readInput("Day07")
    val testInput = readInput("Day07_test")

    check(part1(testInput) == 6440)
    part1(input).println()

    check(part2(testInput) == 5905)
    part2(input).println()
}

fun deriveHandType(hand: String): HandType {
    val counts = hand.groupingBy { it }.eachCount().values.groupingBy { it }.eachCount()

    return if (counts.contains(5)) {
        HandType.FIVE_OF_A_KIND
    } else if (counts.contains(4)) {
        HandType.FOUR_OF_A_KIND
    } else if (counts.contains(3) && counts.contains(2)) {
        HandType.FULL_HOUSE
    } else if (counts.contains(3)) {
        HandType.THREE_OF_A_KIND
    } else if (counts.getOrDefault(2, 0) == 2) {
        HandType.TWO_PAIR
    } else if (counts.contains(2)) {
        HandType.ONE_PAIR
    } else {
        HandType.HIGH_CARD
    }
}

// 3 + 2
// 3 + 1j + 1 NO
// 2 + 2 + 1j
// 2 + 1 + 2j NO


// 2 + 2 + 1
// 2 + 1 + 1 + 1j NO

fun deriveHandTypeWithJoker(hand: String): HandType {
    val counts = hand.groupingBy { it }.eachCount()

    val jokers = counts.getOrDefault('J', 0)
    val countsWithoutJoker = counts.filter { it.key != 'J' }.values.groupingBy { it }.eachCount()

    return if ((countsWithoutJoker.keys.maxOrNull() ?: 0) + jokers == 5) {
        HandType.FIVE_OF_A_KIND
    } else if (countsWithoutJoker.keys.max() + jokers == 4) {
        HandType.FOUR_OF_A_KIND
    } else if (
        (countsWithoutJoker.contains(3) && countsWithoutJoker.contains(2)) ||
        (countsWithoutJoker.getOrDefault(2, 0) == 2 && jokers == 1)) {
        HandType.FULL_HOUSE
    } else if (countsWithoutJoker.keys.max() + jokers == 3) {
        HandType.THREE_OF_A_KIND
    } else if ((countsWithoutJoker.getOrDefault(2,0)  == 2)) {
        HandType.TWO_PAIR
    } else if (countsWithoutJoker.keys.max() + jokers == 2) {
        HandType.ONE_PAIR
    } else {
        HandType.HIGH_CARD
    }
}

class Hand(val hand: String, val bid: Int) {
    val handType = deriveHandType(hand)
    val jokerHandType = deriveHandTypeWithJoker(hand)
}


fun cardToVal(card: Char, joker: Boolean = false): Int {
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
            if (joker) 1 else 11
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

fun Hand.compareToWithJoker(other: Hand): Int {
    // Better hand wins
    if (this.jokerHandType < other.jokerHandType) {
        return 1
    } else if (this.jokerHandType > other.jokerHandType) {
        return -1
    }

    // Ordered high card wins
    for ((a,b) in this.hand.zip(other.hand)) {
        if (cardToVal(a, true) > cardToVal(b, true)) {
            return 1
        } else if (cardToVal(a, true) < cardToVal(b, true)) {
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
