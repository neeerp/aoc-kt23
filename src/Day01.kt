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

    val VALID_STRINGS = mapOf(
        "1" to 1, "one" to 1,
        "2" to 2, "two" to 2,
        "3" to 3, "three" to 3,
        "4" to 4, "four" to 4,
        "5" to 5, "five" to 5,
        "6" to 6, "six" to 6,
        "7" to 7, "seven" to 7,
        "8" to 8, "eight" to 8,
        "9" to 9, "nine" to 9,
        "0" to 0, "zero" to 0
    )

    fun find_substrings(line: String): Int {
        val first = VALID_STRINGS[line.findAnyOf(VALID_STRINGS.keys)?.second]
        val last = VALID_STRINGS[(line.findLastAnyOf(VALID_STRINGS.keys)?.second)]

        if (first == null || last == null) {
            throw IllegalArgumentException()
        }

        return first * 10 + last
    }

    fun part1(input: List<String>): Int {
        return input.map { find_digits(it) }.sum()
    }


    fun part2(input: List<String>): Int {
        return input.map { find_substrings(it) }.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInputPart1 = readInput("Day01_part1_test")
    check(part1(testInputPart1) == 142)

    val testInputPart2 = readInput("Day01_part2_test")
    check(part2(testInputPart2) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
