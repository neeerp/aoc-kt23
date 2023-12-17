fun main() {

    fun axisRange(direction: TiltDirection, m: Int, n: Int): IntProgression {
        return when (direction) {
            TiltDirection.NORTH -> m-1 downTo 0
            TiltDirection.SOUTH -> 0..<m
            TiltDirection.WEST -> n-1 downTo 0
            TiltDirection.EAST -> 0..<n
        }
    }

    fun nonAxisRange(direction: TiltDirection, m: Int, n: Int): IntProgression {
        return when (direction) {
            TiltDirection.NORTH -> 0..<n
            TiltDirection.SOUTH -> 0..<n
            TiltDirection.EAST -> 0..<m
            TiltDirection.WEST -> 0..<m
        }
    }

    fun axisToCoord(direction: TiltDirection, first: Int, second: Int): Pair<Int, Int> {
        return when (direction) {
            TiltDirection.NORTH -> Pair(second, first)
            TiltDirection.SOUTH -> Pair(second, first)
            TiltDirection.EAST -> Pair(first, second)
            TiltDirection.WEST -> Pair(first, second)
        }
    }

    fun nextCoord(coord: Pair<Int, Int>, direction: TiltDirection): Pair<Int, Int> {
        val (i, j) = coord
        return when (direction) {
            TiltDirection.NORTH -> Pair(i - 1, j)
            TiltDirection.SOUTH -> Pair(i + 1, j)
            TiltDirection.WEST -> Pair(i, j - 1)
            TiltDirection.EAST -> Pair(i, j + 1)
        }
    }

    fun tilt(grid: MutableList<MutableList<Char>>, direction: TiltDirection): MutableList<MutableList<Char>> {
        val m = grid.size
        val n = grid[0].size

        for (first in nonAxisRange(direction, m, n)) {
            var numRocks = 0
            var lastRock = Pair(-1, -1)
            for (second in axisRange(direction, m, n)) {
                val (i, j) = axisToCoord(direction, first, second)
                if (grid[i][j] == 'O') {
                    lastRock = if (numRocks == 0) Pair(i,j) else lastRock
                    numRocks += 1
                } else if (grid[i][j] == '.' && numRocks > 0) {
                    // Rock slide!!
                    grid[lastRock.first][lastRock.second] = '.'
                    grid[i][j] = 'O'
                    lastRock = nextCoord(lastRock, direction)
                } else if (grid[i][j] == '#') {
                    // End of Segment; reset
                    numRocks = 0
                }
            }
        }

        return grid
    }

    fun countNorthLoad(grid: MutableList<MutableList<Char>>): Int {
        var result = 0
        for (i in 0..<grid.size) {
            for (j in 0..<grid[0].size) {
                if (grid[i][j] == 'O') {
                    result += (grid.size - i)
                }
            }
        }

        return result
    }

    fun part1(input: MutableList<MutableList<Char>>): Int {
        val tiltedGrid = tilt(input, TiltDirection.NORTH)
        return countNorthLoad(tiltedGrid)
    }

    fun part2(input: MutableList<MutableList<Char>>): Int {
        val mem = mutableMapOf<String, Int>()

        var i = 1
        var found = false
        while (i < 1_000_000_000) {
            tilt(input, TiltDirection.NORTH)
            tilt(input, TiltDirection.WEST)
            tilt(input, TiltDirection.SOUTH)
            tilt(input, TiltDirection.EAST)

            if (found) {
                i += 1
                continue
            }

            val stringified = input.stringify()

            if (stringified in mem) {
                found = true
                val clen = i - mem[stringified]!!
                val cycles_remaining = (1_000_000_000 - mem[stringified]!!) % clen
                i = 1_000_000_000 - cycles_remaining
                continue
            } else {
                mem[stringified] = i
                i += 1
            }
        }

        return countNorthLoad(input)
    }

    val testInput = readInput("Day14_test").toMutableCharMatrix()
//    val playInput = readRaw("Day14_play")
    val input = readInput("Day14").toMutableCharMatrix()

    check(part1(testInput) == 136)
//    part2(playInput).println()
    part1(input).println()

    check(part2(testInput) == 64)
    part2(input).println()
}

enum class TiltDirection {
    NORTH, WEST, SOUTH, EAST
}
