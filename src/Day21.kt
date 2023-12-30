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

    fun adjacent(grid: List<List<Char>>, point: Pair<Int, Int>): List<Pair<Int, Int>> {
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

    fun prettyPrint(grid: List<List<Char>>, evenSeen: Set<Pair<Int, Int>>, oddSeen: Set<Pair<Int, Int>>) {
        val grid = grid.toMutable()
        for ((i, j) in evenSeen) {
            grid[i][j] = 'E'
        }

        for ((i, j) in oddSeen) {
            grid[i][j] = 'O'
        }
        println(grid.stringify())
        println("Grid size: ${grid.size} x ${grid[0].size}")

    }

    fun part1(grid: List<List<Char>>, steps: Long, start: Pair<Int, Int>, debug: Boolean = false): Pair<Int, Int> {
        // Game plan:
        // Perform a BFS, but alternate which 'seen' set we append to.
        val evenSeen = mutableSetOf<Pair<Int, Int>>()
        val evenNext = mutableListOf(start)

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

        if (debug) {
            prettyPrint(grid, evenSeen, oddSeen)
            println("Odds: ${oddSeen.size} Evens: ${evenSeen.size}")

            // Parity information
            if (grid.size == 131 && grid[0].size == 131) {
                if (Pair(65, 65) in evenSeen || Pair(0, 0) in evenSeen || Pair(0, 130) in evenSeen || Pair(130, 0) in evenSeen || Pair(130, 130) in evenSeen) {
                    println("This grid's parity is Even")
                } else {
                    println("This grid's parity is Odd")
                }
            }
        }

        return Pair(evenSeen.size, oddSeen.size)
    }

    fun sum1ToN(n: Long): Long {
        return n * (n + 1) / 2
    }


    fun part2(grid: List<List<Char>>): Long {

        // We wish to compute...
        // - Result on the start square
        // - Result on adjacent ('odd') squares
        // - Result on Top/Bottom/Left/Right end squares
        // - Result on Top Left/Top Right/Bottom Left/ Bottom Right big/small squares



        // 26501300 is divisible by 131 (the length of the square input grid);
        // 26501300 / 131 = 202300
        // This means on the x/y axes, we complete 202300 full maps. If the center/start is 'even',
        // this implies the incomplete tips will be even as well.

        val STEPS = 26501365
        val FULL_MAPS = (STEPS - 65) / 131
//        val SIZE = 131

        val (evenMap, oddMap) = part1(grid, 300L, Pair(65, 65), false).toList().map { it.toLong() };

        val topMid = part1(grid, 130, Pair(130, 65), false).first.toLong()
        val rightMid = part1(grid, 130, Pair(65, 0), false).first.toLong()
        val bottomMid = part1(grid, 130, Pair(0, 65), false).first.toLong()
        val leftMid = part1(grid, 130, Pair(65, 130), false).first.toLong()


        val bottomRightBig = part1(grid, 195, Pair(0, 0), false).second.toLong()
        val bottomLeftBig = part1(grid, 195, Pair(0, 130), false).second.toLong()
        val topRightBig = part1(grid, 195, Pair(130, 0), false).second.toLong()
        val topLeftBig = part1(grid, 195, Pair(130, 130), false).second.toLong()

        val bottomRightSmall = part1(grid, 65, Pair(0, 0), false).first.toLong()
        val bottomLeftSmall = part1(grid, 65, Pair(0, 130), false).first.toLong()
        val topRightSmall = part1(grid, 65, Pair(130, 0), false).first.toLong()
        val topLeftSmall = part1(grid, 65, Pair(130, 130), false).first.toLong()

        val inner = oddMap * ((FULL_MAPS - 1) / 2 * 2 + 1L).let { it * it } +
                evenMap * (FULL_MAPS / 2 * 2L).let { it * it }
        val small = listOf(bottomRightSmall, bottomLeftSmall, topRightSmall, topLeftSmall).sum().times(FULL_MAPS)
        val big = listOf(bottomRightBig, bottomLeftBig, topRightBig, topLeftBig).sum().times(FULL_MAPS - 1)
        val mid = listOf(leftMid, rightMid, bottomMid, topMid).sum()

        val result = inner + small + big + mid
        return result
    }

    val input = readInput("Day21").toMutableCharMatrix()
    val testInput = readInput("Day21_test").toMutableCharMatrix()

    val originalInputStart = findStart(input)
    input[originalInputStart.first][originalInputStart.second] = '.'

    val testInputStart = findStart(testInput)
    testInput[testInputStart.first][testInputStart.second] = '.'

    check(part1(testInput, 6L, testInputStart, false).first == 16)
    part1(input, 64L, Pair(65,65)).println()
    part2(input).println()
}

enum class Parity {
    EVEN, ODD
}
