import kotlin.math.max

fun main() {

    val NUM_RED = 12
    val NUM_GREEN = 13
    val NUM_BLUE = 14

    val RED = "red"
    val GREEN = "green"
    val BLUE = "blue"


    fun isSegmentValid(segment: String, red: Int, green: Int, blue: Int): Boolean {
        val cubes = segment.split(", ").associateBy(
            { it.substringAfter(' ') },
            { it.substringBefore(' ').toInt() }
        )

        if ((cubes[RED] ?: 0) > red) {
            return false
        }

        if ((cubes[BLUE] ?: 0) > blue) {
            return false
        }

        if ((cubes[GREEN] ?: 0) > green) {
            return false
        }

        return true
    }

    fun parseGame(game: String): Int {
        val gameId = game.substringBefore(':').substringAfter(' ').toInt()
        val segments = game.substringAfter(':').split(';').map{it.trim()}

        return if (segments.all { isSegmentValid(it, NUM_RED, NUM_GREEN, NUM_BLUE) }) gameId else 0
    }

    fun findMinimums(game: String): Int {
        val segments = game.substringAfter(':').split(';', ',').map{it.trim()}
        val colours = mutableMapOf(RED to 0, GREEN to 0, BLUE to 0)

        segments.forEach{
            val count = it.substringBefore(' ').toInt()!!
            val colour = it.substringAfter(' ')

            colours[colour] = max(colours[colour]!!, count)
        }

        return colours[RED]!! * colours[GREEN]!! * colours[BLUE]!!
    }




    /**
     * You play several games and record the information from each.
     *
     * Each game is listed with its ID ('Game N:') followed by a semicolon-separated list of
     * subsets of cubes revealed from the bag (e.g. 3 blue, 4 red; 1 red, 2 green; 2 green).
     *
     * Return the sum of ID numbers of valid games if the bag contained only
     * 12 red cubes, 13 green cubes, and 14 blue cubes.
     *
     */
    fun part1(input: List<String>): Int {
        return input.map{parseGame(it)}.sum()
    }


    /**
     * What's the fewest number of cubes that could have been in the bag for each game?
     *
     * Return their product.
     */
    fun part2(input: List<String>): Int {
        return input.map{findMinimums(it)}.sum()
    }

    val input = readInput("Day02")
    val testInputPart1 = readInput("Day02_part1_test")

    check(part1(testInputPart1) == 8)
    part1(input).println()

    check(part2(testInputPart1) == 2286)
    part2(input).println()
}
