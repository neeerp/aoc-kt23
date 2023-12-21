import java.security.InvalidParameterException

fun main() {

    fun findStart(grid: MutableList<MutableList<Char>>): Pair<Int, Int> {
        for ((i, line) in grid.withIndex()) {
            for ((j, c) in line.withIndex()) {
                if (c == 'S') {
                    return Pair(i, j)
                }
            }
        }
        throw InvalidParameterException()
    }

    fun adjacent(grid: MutableList<MutableList<Char>>, point: Pair<Int, Int>): List<Pair<Int, Int>> {
        val (i, j) = point
        val adjacent = mutableListOf<Pair<Int, Int>>()

        if (i > 0 && grid[i-1][j] != '#') {
            adjacent.add(Pair(i-1, j))
        }

        if (i < grid.size - 1 && grid[i+1][j] != '#') {
            adjacent.add(Pair(i+1, j))
        }

        if (j > 0 && grid[i][j-1] != '#') {
            adjacent.add(Pair(i, j - 1))
        }

        if (j < grid[0].size - 1 && grid[i][j+1] != '#') {
            adjacent.add(Pair(i, j + 1))
        }

        return adjacent
    }

    fun prettyPrint(grid: MutableList<MutableList<Char>>, evenSeen: Set<Pair<Int, Int>>, oddSeen: Set<Pair<Int, Int>>) {
        for ((i, j) in evenSeen) {
            grid[i][j] = 'E'
        }

        for ((i, j) in oddSeen) {
            grid[i][j] = 'O'
        }
        println(grid.stringify())
    }

    fun part1(grid: MutableList<MutableList<Char>>, steps: Long): Int {
        // Game plan:
        // Perform a BFS, but alternate which 'seen' set we append to.
        val evenSeen = mutableSetOf<Pair<Int, Int>>()
        val evenNext = mutableListOf(findStart(grid))

        val oddSeen = mutableSetOf<Pair<Int, Int>>()
        val oddNext = mutableListOf<Pair<Int, Int>>()
        var parity: Parity = Parity.EVEN

        var remaining = steps
        while (remaining >= 0L && (evenNext.isNotEmpty() || oddNext.isNotEmpty())) {
            var curSeen: MutableSet<Pair<Int, Int>>
            var curNext: MutableList<Pair<Int, Int>>
            var nextSeen: MutableSet<Pair<Int, Int>>
            var nextNext: MutableList<Pair<Int, Int>>

            when(parity) {
                Parity.EVEN -> {
                    curNext = evenNext
                    curSeen = evenSeen
                    nextNext = oddNext
                    nextSeen = oddSeen
                }
                Parity.ODD -> {
                    curNext = oddNext
                    curSeen = oddSeen
                    nextNext = evenNext
                    nextSeen = evenSeen
                }
            }

            while (curNext.isNotEmpty()) {
                val cur = curNext.removeLast()
                if (cur in curSeen) {
                    continue
                }
                curSeen.add(cur)

                val adjacentPoints = adjacent(grid, cur)
                for (point in adjacentPoints) {
                    if (point !in nextSeen) {
                        nextNext.add(point)
                    }
                }
            }

            parity = when(parity) {
                Parity.ODD -> Parity.EVEN
                Parity.EVEN -> Parity.ODD
            }
            remaining -= 1
        }

        val result = when(parity) {
            Parity.ODD -> evenSeen.size
            Parity.EVEN -> oddSeen.size
        }

        prettyPrint(grid, evenSeen, oddSeen)

        return result
    }


    fun part2(grid: MutableList<MutableList<Char>>): Long {
        return 0L
    }

    val input = readInput("Day21").toMutableCharMatrix()
    val testInput = readInput("Day21_test").toMutableCharMatrix()

    check(part1(testInput, 6L) == 16)

    part1(input, 64L).println()

//    check(part2(testInput) == 167409079868000L)
//    part2(input).println()
}

enum class Parity {
    EVEN, ODD
}
