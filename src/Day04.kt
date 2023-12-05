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
        println(result)
        return result
    }


    /**
     */
    fun part2(input: List<String>): Int {
        return 0
    }

    val input = readInput("Day04")
    val testInput = readInput("Day04_test")

    check(part1(testInput) == 13)
    part1(input).println()

//    check(part2(testInput) == 467835)
//    part2(input).println()
}
