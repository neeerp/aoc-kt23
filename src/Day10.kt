import java.security.InvalidParameterException

fun main() {

    /**
     */
    fun part1(input: List<String>): Int {
        val grid = Grid(input.map { it.toList() }, TileFactory())
        val start = grid.findStart()


        var (a, b) = start.connections().toList()
        var aPrev = start
        var bPrev = start

        var counter = 1
        while (!a.equals(b) && !a.next(aPrev)!!.equals(b)) {
            counter += 1

            val aTemp = a.next(aPrev)!!
            val bTemp = b.next(bPrev)!!

            aPrev = a
            bPrev = b
            a = aTemp
            b = bTemp
        }
        return counter
    }


    /**
     * What's the fewest number of cubes that could have been in the bag for each game?
     *
     * Return their product.
     */
    fun part2(input: List<String>): Int {

        return 0
    }

    val input = readInput("Day10")

    val test1 = readInput("Day10_test1")
    val test2 = readInput("Day10_test2")
    val test3 = readInput("Day10_test3")
    val test4 = readInput("Day10_test4")

    check(part1(test1) == 4)
    check(part1(test2) == 4)

    check(part1(test3) == 8)
    check(part1(test4) == 8)
    part1(input).println()

//    check(part2(testInputPart1) == 2286)
//    part2(input).println()
}

class TileFactory {
    fun tile(t: Char, coord: Coord, grid: Grid): Tile {
        return when(t) {
            '|' -> VerticalPipe(coord, grid)
            '-' -> HorizontalPipe(coord, grid)
            'J' -> UpLeftBendPipe(coord, grid)
            'L' -> UpRightBendPipe(coord, grid)
            '7' -> DownLeftBendPipe(coord, grid)
            'F' -> DownRightBendPipe(coord, grid)
            'S' -> StartingTile(coord, grid)
            else -> EmptyTile(coord, grid)

        }
    }
}

data class Coord(val i: Int, val j: Int) {
    fun equals(other: Coord): Boolean {
        return i == other.i && j == other.j
    }
}

abstract class Tile(private val coord: Coord, protected val grid: Grid) {
    protected val i: Int = coord.i
    protected val j: Int = coord.j

    abstract fun connections(): Collection<Tile>

    fun equals(other: Tile): Boolean {
        return coord.equals(other.coord)
    }

    fun next(prev: Tile): Tile? {
        for (tile in connections()) {
            if (!prev.equals(tile)) {
                return tile
            }
        }

        return null
    }

    override fun toString(): String {
        return "${this.javaClass} :: (${i}, ${j})"
    }
}

class VerticalPipe(coord: Coord, grid: Grid) : Tile(coord, grid) {
    override fun connections(): Collection<Tile> {
        val l: MutableList<Tile> = mutableListOf()
        val a = grid[i + 1, j]
        val b = grid[i - 1, j]
        if (a != null) l.add(a)
        if (b != null) l.add(b)
        return l
    }
}

class HorizontalPipe(coord: Coord, grid: Grid) : Tile(coord, grid) {
    override fun connections(): Collection<Tile> {
        val l: MutableList<Tile> = mutableListOf()
        val a = grid[i, j + 1]
        val b = grid[i, j - 1]
        if (a != null) l.add(a)
        if (b != null) l.add(b)
        return l
    }
}

class UpLeftBendPipe(coord: Coord, grid: Grid) : Tile(coord, grid) {
    override fun connections(): Collection<Tile> {
        val l: MutableList<Tile> = mutableListOf()
        val a = grid[i - 1, j]
        val b = grid[i, j - 1]
        if (a != null) l.add(a)
        if (b != null) l.add(b)
        return l
    }
}

class UpRightBendPipe(coord: Coord, grid: Grid) : Tile(coord, grid) {
    override fun connections(): Collection<Tile> {
        val l: MutableList<Tile> = mutableListOf()
        val a = grid[i - 1, j]
        val b = grid[i, j + 1]
        if (a != null) l.add(a)
        if (b != null) l.add(b)
        return l
    }
}

class DownRightBendPipe(coord: Coord, grid: Grid) : Tile(coord, grid) {
    override fun connections(): Collection<Tile> {
        val l: MutableList<Tile> = mutableListOf()
        val a = grid[i + 1, j]
        val b = grid[i, j + 1]
        if (a != null) l.add(a)
        if (b != null) l.add(b)
        return l
    }
}

class DownLeftBendPipe(coord: Coord, grid: Grid) : Tile(coord, grid) {
    override fun connections(): Collection<Tile> {
        val l: MutableList<Tile> = mutableListOf()
        val a = grid[i + 1, j]
        val b = grid[i, j - 1]
        if (a != null) l.add(a)
        if (b != null) l.add(b)
        return l
    }
}

class EmptyTile(coord: Coord, grid: Grid) : Tile(coord, grid) {
    override fun connections(): Collection<Tile> {
        return emptyList()
    }
}

class StartingTile(coord: Coord, grid: Grid) : Tile(coord, grid) {
    override fun connections(): Collection<Tile> {
        val candidates = listOf(
            grid[i + 1, j],
            grid[i - 1, j],
            grid[i, j + 1],
            grid[i, j - 1],
        )

        return candidates.filterNotNull().filter {
            it.connections().any { adj -> adj.equals(this) }
        }
    }
}

class Grid(private val grid: List<List<Char>>, private val tileFactory: TileFactory) {
    private val n = grid.size
    private val m = grid[0].size

    operator fun get(i: Int, j: Int): Tile? {
        if (i < 0 || i > m - 1) {
            return null
        }

        if (j < 0 || j > n - 1) {
            return null
        }

        return tileFactory.tile(grid[i][j], Coord(i, j), this)
    }
    operator fun get(coord: Coord): Tile? {
        return this[coord.i, coord.j]
    }

    fun findStart(): Tile {
        for ((i, line) in grid.withIndex()) {
            for ((j, c) in line.withIndex()) {
                if (c == 'S') return this[i,j]!!
            }
        }

        throw InvalidParameterException()
    }
}