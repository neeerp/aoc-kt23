import java.util.ArrayList
import kotlin.math.pow

fun main() {

    /**
     * Each card has two lists of numbers separated by a '|':
     *  - A list of winning numbers
     *  - A list of numbers you have
     *
     * The first match gives you one point; every match thereafter doubles your points.
     *
     * Return the sum of all the cards.
     */
    fun part1(input: List<String>): Int {
        val result = input.sumOf { line ->
            val pair = line.substringAfter(':').split('|')

            val winningNumbers = pair[0].trim().split(' ').filter { it.toIntOrNull() != null }.toSet()
            val myNumbers = pair[1]!!.trim().split(' ').filter { it.toIntOrNull() != null }.toSet()

            val intersect = winningNumbers.intersect(myNumbers)

            if (intersect.isNotEmpty()) 2.0.pow(intersect.size - 1).toInt() else 0
        }
        return result
    }

    fun numberOfMatches(card: String): Int {
        val pair = card.substringAfter(':').split('|')

        val winningNumbers = pair[0].trim().split(' ').filter { it.toIntOrNull() != null }.toSet()
        val myNumbers = pair[1]!!.trim().split(' ').filter { it.toIntOrNull() != null }.toSet()

        val intersect = winningNumbers.intersect(myNumbers)
        return intersect.size
    }


    /**
     * Scratch cards let you win scratch cards equal to the number of winning numbers you have.
     * You win that many cards below you.
     * If card 10 has 5 matching numbers, you win one copy of cards 11, 12, 13, 14, and 15.
     */
    fun part2(input: List<String>): Int {
        var cards = Array(input.size) { _ -> 1 }
        for ((num, card) in input.withIndex()) {
            val matches = numberOfMatches(card)
            for (i in 1..matches) {
                cards[num + i] += cards[num]
            }
        }

        return cards.sum()
    }

    val input = readInput("Day04")
    val testInput = readInput("Day04_test")

    check(part1(testInput) == 13)
    part1(input).println()

    check(part2(testInput) == 30)
    part2(input).println()
}
