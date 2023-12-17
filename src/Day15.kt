fun main() {
    fun computeHash(s: String): Int {
        var cv = 0
        for (c in s) {
            cv += c.code
            cv = (cv * 17) % 256
        }

        return cv
    }

    fun part1(input: String): Int {
        return input.split(',').sumOf { computeHash(it) }
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val input = readRaw("Day15")
    val testInput = readRaw("Day15_test")

    check(computeHash("HASH") == 52)
    check(part1(testInput) == 1320)

//    check(part2(testInput) == 281)

    part1(input).println()
//    part2(input).println()
}
