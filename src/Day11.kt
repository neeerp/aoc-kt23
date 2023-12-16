import kotlin.math.abs

fun main() {

    fun findGalaxies(input: List<List<Char>>): Set<Pair<Int, Int>> {
        val galaxies: MutableSet<Pair<Int, Int>> = mutableSetOf()
        for ((i, line) in input.withIndex()) {
            for ((j, c) in line.withIndex()) {
                if (c == '#') {
                    galaxies.add(Pair(i, j))
                }
            }
        }

        return galaxies
    }

    fun findGalaxies(input: List<String>): Set<Pair<Int, Int>> {
        return findGalaxies(input.map { it.toList() })
    }

    fun transformMap(input: List<String>): List<List<Char>> {
        val result: MutableList<List<Char>> = mutableListOf()
        val m = input.size
        val n = input[0].length

        val galaxies = findGalaxies(input)

        val hDeadSpace = (0..<m).filter { it !in galaxies.map { g -> g.first } }
        val vDeadSpace = (0..<n).filter { it !in galaxies.map { g -> g.second } }

        for ((i, line) in input.withIndex()) {
            if (i in hDeadSpace) {
                result.add(MutableList(n + vDeadSpace.size) { _ -> '.' })
                result.add(MutableList(n + vDeadSpace.size) { _ -> '.' })
                continue
            }

            val cur = mutableListOf<Char>()
            for ((j, c) in line.withIndex()) {
                cur.add(c)

                if (j in vDeadSpace) {
                    cur.add(c)
                }
            }
            result.add(cur)
        }

        return result
    }

    fun manhattan(p1: Pair<Int, Int>, p2: Pair<Int, Int>): Int {
        return abs(p1.first - p2.first) + abs(p1.second - p2.second)
    }

    /**
     * Given a map of galaxies, assume that every row/col with no galaxy is duplicated.
     *
     * Find the shortest path between each pair of galaxies.
     */
    fun part1(input: List<String>): Int {
        // First, let's parse and transform the map.
        val universe = transformMap(input)
        val galaxies = findGalaxies(universe).toList()

        var d = 0
        for ((i, g1) in galaxies.withIndex()) {
            for (g2 in galaxies.subList(i, galaxies.size)) {
                d += manhattan(g1, g2)
            }
        }

        return d
    }


    fun part2(input: List<String>): Int {
        return 0
    }

    val input = readInput("Day11")
    val testInputPart1 = readInput("Day11_test")

    check(part1(testInputPart1) == 374)
    part1(input).println()

//    check(part2(testInputPart1) == 2286)
//    part2(input).println()
}
