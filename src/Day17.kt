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

    fun part1(input: List<List<Int>>): Int {
        val m = input.size
        val n = input[0].size

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
                for (k in 0..<3) {
                    coords = nextTile(coords, dir)
                    if (!isPointInBounds(coords, m, n)) {
                        continue
                    }

                    val (i, j) = coords
                    cost += input[i][j]
                    q.add(HeapNode(cost, coords, dir))
                }
            }
        }

        return -1
    }

    fun part2(input: List<List<Int>>): Int {
        return 0
    }

    val input = readInput("Day17").toMutableIntMatrix()
    val testInput = readInput("Day17_test").toMutableIntMatrix()

//    println(part1(testInput))
    check(part1(testInput) == 102)
    part1(input).println()

//    check(part2(testInput) == 51)
//    part2(input).println()
}
