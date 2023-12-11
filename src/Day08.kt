import java.security.InvalidParameterException

fun main() {
    fun parseDirections(line: String): List<Direction> {
        return line.map {
            when(it) {
                'L' -> Direction.LEFT
                'R' -> Direction.RIGHT
                else -> {
                    throw InvalidParameterException()
                }
            }
        }
    }


    /**
     */
    fun part1(input: List<String>): Int {
        val directions = parseDirections(input[0])
        val map = LRMap(input.drop(2))

        var location = "AAA"
        var curDir = 0
        while (location != "ZZZ") {
            location = map.get(location, directions[curDir % directions.size])

            curDir += 1
        }

        return curDir
    }

    fun gcd(num1: Long, num2: Long): Long {
        var a = num1
        var b = num2
        while (b > 0) {
            val temp = b
            b = a % b
            a = temp
        }
        return a
    }

    fun lcm(a: Long, b: Long): Long {
        return (a * (b / gcd(a,b)))
    }

    fun lcm(input: List<Long>): Long {
        var result = input[0]
        for (i in 1..<input.size) {
            result = lcm(result, input[i])
        }
        return result
    }

    /**
     */
    fun part2(input: List<String>): Long {
        val directions = parseDirections(input[0])
        val map = LRMap(input.drop(2))

        val locations = map.keys().filter { it.endsWith('A') }

        val steps = locations.map { location ->
            var cur = location

            var step = 0L

            // After testing, it seems each cycle has only one terminal location
            while (!cur.endsWith('Z')) {
                val curDir = directions[(step % directions.size).toInt()]
                cur = map.get(cur, curDir)

                step++
            }
            step
        }

        return lcm(steps)
    }

    val input = readInput("Day08")
    val testInput1 = readInput("Day08_test1")
    val testInput2 = readInput("Day08_test2")

    check(part1(testInput1) == 2)
    check(part1(testInput2) == 6)
    part1(input).println()

    val testInput3 = readInput("Day08_test3")
    check(part2(testInput3) == 6L)
    part2(input).println()

}

class LRMap(lines: List<String>) {
    private val mapping = lines.associate {
        val entry = LRMapEntry(it)
        entry.key to entry
    }

    fun get(key: String, dir: Direction): String {
        val entry = mapping[key]!!
        return when (dir) {
            Direction.LEFT -> entry.left
            Direction.RIGHT -> entry.right
        }
    }

    fun keys(): Set<String> {
        return mapping.keys
    }
}

enum class Direction {
    LEFT, RIGHT
}

class LRMapEntry(line: String) {
    val key: String
    val left: String
    val right: String

    init {
        val match = Regex("(\\w+) = \\((\\w+), (\\w+)\\)").find(line)!!
        val (k, l, r) = match.destructured
        key = k; left = l; right = r


    }
}
