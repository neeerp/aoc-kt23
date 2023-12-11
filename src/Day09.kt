fun main() {

    fun differences(nums: List<Int>): List<Int> {
        return nums.zip(nums.drop(1)).map { it.second - it.first }
    }

    fun computeDifferences(nums: List<Int>): List<List<Int>> {
        val diffs = mutableListOf(nums)

        while (diffs.last().any { it != 0 }) {
            diffs.add(differences(diffs.last()))
        }

        return diffs
    }

    fun extrapolate(diffs: List<List<Int>>): Int {
        val final = mutableListOf(0)

        for (list in diffs.asReversed().drop(1)) {
            final.add(final.last() + list.last())
        }

        return final.last()
    }

    fun part1(input: List<String>): Int {
        return input.sumOf {
            val diffs = computeDifferences(it.split(' ').map { it.toInt() })
            extrapolate(diffs)
        }
    }


    fun part2(input: List<String>): Int {
        return 0
    }

    val testInput = readInput("Day09_test")
    val input = readInput("Day09")

    check(part1(testInput) == 114)
    part1(input).println()

//    check(part2(testInput) == 281)
//    part2(input).println()
}
