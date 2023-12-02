/**
 * The calibration document consists of lines of text.
 * Each line originally contained a calibration value that needs to be recovered.
 *
 * On each line, the calibration value may be found by combining the first and last digit, in that order, forming a two
 * digit number.
 *
 * We want the sum of all the calibration values.
 */
fun main() {

    fun part1(input: List<String>): Int {
        return input.map { find_digits(it) }.sum()
    }


    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}

fun find_digits(line: String): Int {
    var first = -1
    var last = -1
    for (c in line) {
        if (first < 0) {
            first = c.digitToIntOrNull() ?: first
        }
        last = c.digitToIntOrNull() ?: last
    }

    return first * 10 + last
}
