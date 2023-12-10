fun main() {
    fun isWinnable(wait: Int, t: Int, d: Int): Boolean {
        val remaining = t - wait
        return remaining * wait > d
    }

    fun findMargin(t: Int, d: Int): Int {
        var margin = 0
        for (wait in 0..<t) {
            if (isWinnable(wait, t, d)) {
                margin++
            }
        }
        return margin
    }

    /**
     * You're in several races. You've got t seconds to beat d distance.
     * Each second you wait increases your speed by 1 unit per second.
     *
     * Return the product of the margin for the seconds you can spend
     * waiting in each race.
     */
    fun part1(input: List<String>): Int {
        val times = input[0].substringAfter(':').trim().split("\\s+".toRegex()).map{it.toInt()}
        val dists = input[1].substringAfter(':').trim().split("\\s+".toRegex()).map{it.toInt()}

        val result = times.zip(dists).map { findMargin(it.first, it.second) }.product()
        return result
    }


    /**
     */
    fun part2(input: List<String>): Int {
        return 0
    }

    val input = readInput("Day06")
    val testInput = readInput("Day06_test")

    check(part1(testInput) == 288)
    part1(input).println()

//    check(part2(testInput) == 46L)
//    part2(input).println()
}
