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

    fun getNumberAt(input: List<String>, i: Int, j: Int): Int {
        var start = j
        var end = j

        var k = j
        while (k >= 0 && input[i][k].isDigit()) {
            start = k--
        }

        k = j
        while (k < input[0].length && input[i][k].isDigit()) {
            end = k++
        }

        return input[i].substring(start, end+1).toInt()
    }

    fun validateCandidate(candidate: Pair<Int, Int>, input: List<String>): Int {
        val numbers: ArrayList<Int> = arrayListOf()
        val (i, j)  = candidate

        // Check the sides
        if (j > 0 && input[i][j-1].isDigit()) {
            numbers.add(getNumberAt(input, i, j-1))
        }

        if (j < input[0].length - 1 && input[i][j+1].isDigit()) {
            numbers.add(getNumberAt(input, i, j+1))
        }

        // Check Above
        if (i > 0) {
            if (input[i-1][j].isDigit()) {
                numbers.add(getNumberAt(input, i-1, j))
            } else {
                if (j > 0 && input[i-1][j-1].isDigit()) {
                    numbers.add(getNumberAt(input, i-1, j-1))
                }

                if (j < input[0].length - 1 && input[i-1][j+1].isDigit()) {
                    numbers.add(getNumberAt(input, i-1, j+1))
                }
            }
        }

        // Check below
        if (i < input.size - 1) {
            if (input[i+1][j].isDigit()) {
                numbers.add(getNumberAt(input, i+1, j))
            } else {
                if (j > 0 && input[i+1][j-1].isDigit()) {
                    numbers.add(getNumberAt(input, i+1, j-1))
                }

                if (j < input[0].length - 1 && input[i+1][j+1].isDigit()) {
                    numbers.add(getNumberAt(input, i+1, j+1))
                }
            }
        }

        return if (numbers.size == 2) numbers[0] * numbers[1] else 0
    }


    /**
     * A gear is any '*' that is adjacent to exactly two part numbers.
     * Its gear ratio is the reuslt of multiplying those two numbers together.
     *
     * Find the gear ratio of every gear and add them up.
     */
    fun part2(input: List<String>): Int {
        // Find all the stars
        val candidates = mutableSetOf<Pair<Int, Int>>()
        for ((i, line) in input.withIndex()) {
            for ((j, c) in line.withIndex()) {
                if (c == '*') {
                    candidates.add(Pair(i, j))
                }
            }
        }

        return candidates.sumOf { validateCandidate(it, input) }
    }

    val input = readInput("Day03")
    val testInput = readInput("Day03_test")

    check(part1(testInput) == 4362)
    part1(input).println()

    check(part2(testInput) == 467835)
    part2(input).println()
}
