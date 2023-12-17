fun main() {

    fun tiltNorth(grid: MutableList<MutableList<Char>>): MutableList<MutableList<Char>> {
        for (j in 0..<grid[0].size) {
            var numRocks = 0
            var lastRock = -1
            for (i in (grid.size - 1) downTo 0) {
                if (grid[i][j] == 'O') {
                    lastRock = if (numRocks == 0) i else lastRock
                    numRocks += 1
                } else if (grid[i][j] == '.' && numRocks > 0) {
                    // Rock slide!!
                    grid[lastRock][j] = '.'
                    grid[i][j] = 'O'
                    lastRock -= 1
                } else if (grid[i][j] == '#') {
                    // End of Segment; reset
                    numRocks = 0
                    lastRock = -1
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
        val tiltedGrid = tiltNorth(input)
        return countNorthLoad(tiltedGrid)
    }

    fun part2(input: MutableList<MutableList<Char>>): Int {
        return 0
    }

    val testInput = readInput("Day14_test").toMutableCharMatrix()
//    val playInput = readRaw("Day14_play")
    val input = readInput("Day14").toMutableCharMatrix()

    check(part1(testInput) == 136)
//    part2(playInput).println()
    part1(input).println()

//    check(part2(testInput) == 400)
//    part2(input).println()
}
