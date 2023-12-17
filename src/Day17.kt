import java.util.PriorityQueue

fun main() {

    fun nextTile(coords: Pair<Int, Int>, direction: BeamDirection): Pair<Int, Int> {
        val (i, j) = coords
        return when(direction) {
            BeamDirection.RIGHT -> Pair(i, j+1)
            BeamDirection.LEFT -> Pair(i, j-1)
            BeamDirection.UP -> Pair(i-1, j)
            BeamDirection.DOWN -> Pair(i+1, j)
        }
    }

    fun validDirections(direction: BeamDirection): List<BeamDirection> {
        return when (direction) {
            BeamDirection.UP, BeamDirection.DOWN -> listOf(BeamDirection.LEFT, BeamDirection.RIGHT)
            BeamDirection.LEFT, BeamDirection.RIGHT -> listOf(BeamDirection.UP, BeamDirection.DOWN)
        }
    }

    class HeapNode(val cost: Int, val coords: Pair<Int, Int>, val direction: BeamDirection) {
        override fun toString(): String {
            return ("${this.coords}: ${this.cost}  // ${this.direction}")
        }
    }

    fun traverse(grid: List<List<Int>>, minChain: Int, maxChain: Int): Int {
        val m = grid.size
        val n = grid[0].size

        val q = PriorityQueue {t1: HeapNode, t2: HeapNode -> t1.cost - t2.cost}
        val seen = mutableSetOf<Pair<Pair<Int, Int>, BeamDirection>>()

        q.add(HeapNode(0, Pair(0,0), BeamDirection.DOWN))
        q.add(HeapNode(0, Pair(0,0), BeamDirection.RIGHT))

        while (!q.isEmpty()) {
            val cur = q.poll()
            val seenKey = Pair(cur.coords, cur.direction)
            if (seenKey in seen) {
                continue
            }
            seen.add(seenKey)

            if (cur.coords == Pair(m-1,n-1)) {
                return cur.cost
            }


            for (dir in validDirections(cur.direction)) {
                var coords = cur.coords
                var cost = cur.cost
                for (k in 1..maxChain) {
                    coords = nextTile(coords, dir)
                    if (!isPointInBounds(coords, m, n)) {
                        continue
                    }

                    val (i, j) = coords
                    cost += grid[i][j]

                    if (k >= minChain) {
                        q.add(HeapNode(cost, coords, dir))
                    }
                }
            }
        }

        return -1
    }

    fun part1(input: List<List<Int>>): Int {
        return traverse(input, 0, 3)
    }

    fun part2(input: List<List<Int>>): Int {
        return traverse(input, 4, 10)
    }

    val input = readInput("Day17").toMutableIntMatrix()
    val testInput = readInput("Day17_test").toMutableIntMatrix()

    println(part1(testInput))
    check(part1(testInput) == 102)
    part1(input).println()

    check(part2(testInput) == 94)
    part2(input).println()
}
