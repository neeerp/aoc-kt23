import kotlin.math.max

fun main() {

    fun is_symbol(c: Char): Boolean {
        return (!c.isDigit() && c != '.')
    }

    fun validate_part(input: List<String>, row: Int, start: Int, end: Int): Int {
        val number = input[row].substring(start, end).toIntOrNull() ?: 0

        // Check Sides
        for (i in (row - 1).rangeTo(row + 1)) {
            if (i < 0 || i >= input.size) {
                // Top or bottom boundary
                continue
            }

            if (start > 0 && is_symbol(input[i][start - 1])) {
                return number
            }

            if (end < input[0].length && is_symbol(input[i][end])) {
                return number
            }
        }

        // Check above and below rows
        for (j in start.rangeTo(end - 1)) {
            if (row > 0 && is_symbol(input[row - 1][j])) {
                return number
            }

            if (row < input.size - 1 && is_symbol(input[row + 1][j])) {
                return number
            }
        }

        return 0
    }

    /**
     * An engine schematic consists of visual representations of the engine.
     * We want to add up all the part numbers in the schematic... Any number
     * adjacent to a symbol, even diagonally, is a 'part number' and should
     * be included in the sum (periods are not symbols).
     */
    fun part1(input: List<String>): Int {
        /*
         * What should the strategy here look like?
         *
         * We can assume the length of each line is always the same, hence we can create a 2-d Matrix.
         *
         * We need to identify each number as we iterate. Once we've "parsed" a number, we need to check all the
         * cells around it for a symbol.
         */

        var result = 0
        for ((i, line) in input.withIndex()) {
            var start: Int? = null
            for ((j, c) in line.withIndex()) {
                if (start != null) {
                    // Building up a number
                    if (!c.isDigit()) {
                        // start - j-1 forms a number; now validate.
                        result += validate_part(input, i, start, j)
                        start = null
                    }
                } else {
                    if (c.isDigit()) {
                        start = j
                    }
                }
            }

            // Straggler
            if (start != null) {
                result += validate_part(input, i, start, line.length)
            }
        }
        return result
    }


    /**
     * TODO
     */
    fun part2(input: List<String>): Int {
        return 0
    }

    val input = readInput("Day03")
    val testInputPart1 = readInput("Day03_test")

    check(part1(testInputPart1) == 4362)
    part1(input).println()

//    check(part2(testInputPart1) == 0)
//    part2(input).println()
}
