import kotlin.math.abs

fun main() {

    fun manhattan(p1: Pair<Long, Long>, p2: Pair<Long, Long>): Long {
        return abs(p1.first - p2.first) + abs(p1.second - p2.second)
    }

    fun findAllPairDistances(pairs: List<Pair<Long, Long>>): Long {
        var d = 0L
        for ((i, g1) in pairs.withIndex()) {
            for (g2 in pairs.subList(i, pairs.size)) {
                d += manhattan(g1, g2)
            }
        }

        return d
    }

    fun findGalaxies(input: List<List<Char>>): MutableList<Pair<Long, Long>> {
        val galaxies: MutableList<Pair<Long, Long>> = mutableListOf()
        for ((i, line) in input.withIndex()) {
            for ((j, c) in line.withIndex()) {
                if (c == '#') {
                    galaxies.add(Pair(i.toLong(), j.toLong()))
                }
            }
        }

        return galaxies
    }

    fun findGalaxies(input: List<String>): MutableList<Pair<Long, Long>> {
        return findGalaxies(input.map { it.toList() })
    }

    fun findSparseGalaxies(input: List<String>, age: Long): List<Pair<Long, Long>> {
        val m = input.size
        val n = input[0].length

        var galaxies = findGalaxies(input).sortedByDescending { it.first }.reversed().toMutableList()

        val hDeadSpace = (0L..<m).filter { it !in galaxies.map { g -> g.first } }
        val vDeadSpace = (0L..<n).filter { it !in galaxies.map { g -> g.second } }


        // Incorporate row dead space
        var deadSpace = 0
        for ((idx, g) in galaxies.withIndex()) {
            while (deadSpace < hDeadSpace.size && hDeadSpace[deadSpace] < g.first) {
                deadSpace += 1
            }

            galaxies[idx] = Pair(g.first + ((age - 1) * deadSpace), g.second)
        }


        // Incorporate row dead space
        galaxies = galaxies.sortedByDescending { it.second }.reversed().toMutableList()
        deadSpace = 0
        for ((idx, g) in galaxies.withIndex()) {
            while (deadSpace < vDeadSpace.size && vDeadSpace[deadSpace] < g.second) {
                deadSpace += 1
            }

            galaxies[idx] = Pair(g.first, g.second + ((age - 1) * deadSpace))
        }

        return galaxies
    }


    /**
     * Same deal... only this time, dead space accounts for 1_000_000 rows/cols instead of 2.
     */
    fun part2(input: List<String>, age: Long): Long {
        val galaxies = findSparseGalaxies(input, age)
        val d = findAllPairDistances(galaxies)
        return d
    }

    /**
     * Given a map of galaxies, assume that every row/col with no galaxy is duplicated.
     *
     * Find the shortest path between each pair of galaxies.
     */
    fun part1(input: List<String>): Long {
        return part2(input, 2)
    }


    val input = readInput("Day11")
    val testInput = readInput("Day11_test")

    check(part1(testInput) == 374L)
    part1(input).println()

    check(part2(testInput, 2L) == 374L)
    check(part2(testInput, 10L) == 1030L)
    check(part2(testInput, 100L) == 8410L)
    check(part2(input, 2) == 9403026L)
    part2(input, 1000000L).println()
}
