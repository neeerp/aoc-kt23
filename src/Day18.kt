import kotlin.math.abs

fun main() {

    fun shoelace(points: List<Pair<Int,Int>>): Int {
        val n = points.size
        var area = 0
        for (i in 0..<n) {
            val (xi, yi) = points[i]
            val (xip1, yip1) = points[(i+1) % n]
            area += (xip1 + xi) * (yip1 - yi)
        }
        area = abs(area) / 2

        return area
    }

    fun part1(input: List<String>): Int {
        val moves = input.map {
            var (dir, m, _) = it.split(' ')
            Pair(dir[0], m.toInt())
        }

        var pos = Pair(0, 0)
        var perimeter = 0
        val points = mutableListOf(pos)

        for ((d, m) in moves) {
            val (i, j) = pos
            pos = when (d) {
                'R' -> Pair(i, j + m)
                'L' -> Pair(i, j - m)
                'D' -> Pair(i + m, j)
                'U' -> Pair(i - m, j)
                else -> throw Exception()
            }

            perimeter += m
            points.add(pos)
        }

        val area = shoelace(points)

        // Using Pick's theorem...
        // A = interior + boundary/2 - 1
        val interior = area - perimeter/2 + 1

        return interior + perimeter
    }

    fun part2(input: List<List<Int>>): Int {
        return 0
    }

    val input = readInput("Day18")
    val testInput = readInput("Day18_test")

    check(part1(testInput) == 62)
    part1(input).println()

//    check(part2(testInput) == 94)
//    part2(input).println()
}
