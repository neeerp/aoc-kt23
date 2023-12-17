fun main() {

    fun tileFrom(c: Char): MirrorTile {
        return when (c) {
            '/' -> MirrorTile.SLASH
            '\\' -> MirrorTile.BACKSLASH
            '|' -> MirrorTile.VERTICAL
            '-' -> MirrorTile.HORIZONTAL
            else -> MirrorTile.EMPTY
        }
    }

    fun split(beam: BeamDirection, tile: MirrorTile): List<BeamDirection> {
        return when (tile) {
            MirrorTile.VERTICAL -> when(beam) {
                BeamDirection.LEFT, BeamDirection.RIGHT -> listOf(BeamDirection.UP, BeamDirection.DOWN)
                BeamDirection.UP, BeamDirection.DOWN -> listOf(beam)
            }

            MirrorTile.HORIZONTAL -> when(beam) {
                BeamDirection.LEFT, BeamDirection.RIGHT -> listOf(beam)
                BeamDirection.UP, BeamDirection.DOWN -> listOf(BeamDirection.LEFT, BeamDirection.RIGHT)
            }

            else -> throw Exception()
        }
    }

    fun reflect(beam: BeamDirection, tile: MirrorTile): BeamDirection {
        return when (tile) {
            MirrorTile.SLASH -> when(beam) {
                BeamDirection.LEFT -> BeamDirection.DOWN
                BeamDirection.RIGHT -> BeamDirection.UP
                BeamDirection.UP -> BeamDirection.RIGHT
                BeamDirection.DOWN -> BeamDirection.LEFT
            }

            MirrorTile.BACKSLASH -> when(beam) {
                BeamDirection.LEFT -> BeamDirection.UP
                BeamDirection.RIGHT -> BeamDirection.DOWN
                BeamDirection.UP -> BeamDirection.LEFT
                BeamDirection.DOWN -> BeamDirection.RIGHT
            }

            else -> throw Exception()
        }
    }

    fun newDirections(beam: BeamDirection, tile: MirrorTile): List<BeamDirection> {
        return when (tile) {
            MirrorTile.EMPTY -> listOf(beam)
            MirrorTile.SLASH, MirrorTile.BACKSLASH -> listOf(reflect(beam, tile))
            MirrorTile.HORIZONTAL, MirrorTile.VERTICAL -> split(beam, tile)
        }
    }

    fun nextTile(coords: Pair<Int, Int>, direction: BeamDirection): Pair<Int, Int> {
        val (i, j) = coords
        return when(direction) {
            BeamDirection.RIGHT -> Pair(i, j+1)
            BeamDirection.LEFT -> Pair(i, j-1)
            BeamDirection.UP -> Pair(i-1, j)
            BeamDirection.DOWN -> Pair(i+1, j)
        }
    }

    fun isValid(coords: Pair<Int, Int>, m: Int, n: Int): Boolean {
        val (i, j) = coords
        if (i < 0 || i >= m) {
            return false
        }

        if (j < 0 || j >= n) {
            return false
        }

        return true
    }

    fun isEnergized(coords: Pair<Int, Int>, energized: Set<Pair<Pair<Int, Int>, BeamDirection>>): Boolean {
        return Pair(coords, BeamDirection.UP) in energized ||
            Pair(coords, BeamDirection.DOWN) in energized ||
            Pair(coords, BeamDirection.LEFT) in energized ||
            Pair(coords, BeamDirection.RIGHT) in energized
    }

    fun energize(grid: List<List<Char>>, start: Pair<Int, Int>, startDirection: BeamDirection): Int {
        val m = grid.size
        val n = grid[0].size
        val beams = mutableListOf(Pair(start, startDirection))

        val energized = mutableSetOf<Pair<Pair<Int,Int>, BeamDirection>>()

        while (!beams.isEmpty()) {
            val cur = beams.removeLast()
            val (coords, direction) = cur
            energized.add(cur)

            val (i, j) = coords
            val tile = tileFrom(grid[i][j])

            val dirs = newDirections(direction, tile)
            for (dir in dirs) {
                val newCoords = nextTile(coords, dir)

                if (!isValid(newCoords, m, n) || Pair(newCoords, dir) in energized) {
                    continue
                }

                beams.add(Pair(newCoords, dir))
            }
        }

        var count = 0
        for (i in 0..<m) {
            for (j in 0..<n) {
                if (isEnergized(Pair(i,j), energized)) {
                    count += 1
                }
            }
        }

        return count
    }


    fun part1(input: List<List<Char>>): Int {
        return energize(input, Pair(0,0), BeamDirection.RIGHT)
    }

    fun part2(input: List<List<Char>>): Int {
        val candidates = mutableListOf<Int>()
        val m = input.size
        val n = input[0].size

        for (i in 0..<m) {
            candidates.add(energize(input, Pair(i, 0), BeamDirection.RIGHT))
            candidates.add(energize(input, Pair(i, n-1), BeamDirection.LEFT))
        }

        for (j in 0..<n) {
            candidates.add(energize(input, Pair(0, j), BeamDirection.DOWN))
            candidates.add(energize(input, Pair(m-1, j), BeamDirection.UP))
        }

        return candidates.max()
    }

    val input = readInput("Day16").toMutableCharMatrix()
    val testInput = readInput("Day16_test").toMutableCharMatrix()

    check(part1(testInput) == 46)
    part1(input).println()

    check(part2(testInput) == 51)
    part2(input).println()
}

enum class MirrorTile {
    SLASH, BACKSLASH, EMPTY, VERTICAL, HORIZONTAL;

}
enum class BeamDirection {
    UP,DOWN,LEFT,RIGHT
}

