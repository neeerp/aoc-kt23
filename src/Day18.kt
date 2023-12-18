import kotlin.math.abs

fun main() {

    fun shoelace(points: List<Pair<Long,Long>>): Long {
        val n = points.size
        var area = 0L
        for (i in 0..<n) {
            val (xi, yi) = points[i]
            val (xip1, yip1) = points[(i+1) % n]
            area += (xip1 + xi) * (yip1 - yi)
        }
        area = abs(area) / 2L

        return area
    }

    fun computeNumberOfPoints(moves: List<Pair<Char,Long>>): Long {
        var pos = Pair(0L, 0L)
        var perimeter = 0L
        val points = mutableListOf(pos)

        for ((d, m) in moves) {
            val (i, j) = pos
            pos = when (d) {
                'R', '0' -> Pair(i, j + m)
                'L', '2' -> Pair(i, j - m)
                'D', '1' -> Pair(i + m, j)
                'U', '3' -> Pair(i - m, j)
                else -> throw Exception()
            }

            perimeter += m
            points.add(pos)
        }

        val area = shoelace(points)

        // Using Pick's theorem...
        // A = interior + boundary/2 - 1
        val interior = area - perimeter/2L + 1L

        return interior + perimeter
    }

    fun part1(input: List<String>): Long {
        val moves = input.map {
            var (dir, m, _) = it.split(' ')
            Pair(dir[0], m.toLong())
        }

        return computeNumberOfPoints(moves)
    }


    fun part2(input: List<String>): Long {
        val moves = input.map {
            var (_, temphex) = it.split('#')
            val hex = temphex.dropLast(1)
            val d = hex.last()
            val m = hex.dropLast(1).toLong(radix = 16)
            Pair(d, m)
        }

        return computeNumberOfPoints(moves)
    }

    val input = readInput("Day18")
    val testInput = readInput("Day18_test")

    check(part1(testInput) == 62L)
    part1(input).println()

    check(part2(testInput) == 952408144115L)
    part2(input).println()
}
