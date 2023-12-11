import java.security.InvalidParameterException

fun main() {
    /**
     */
    fun part1(input: List<String>): Int {
        val directions = input[0].map {
            when(it) {
                'L' -> Direction.LEFT
                'R' -> Direction.RIGHT
                else -> {
                    throw InvalidParameterException()
                }
            }
        }
        val map = LRMap(input.drop(2))

        var location = "AAA"
        var curDir = 0
        while (location != "ZZZ") {
            location = map.get(location, directions[curDir % directions.size])

            curDir += 1
        }

        return curDir
    }


    /**
     */
    fun part2(input: List<String>): Int {
        return 0
    }

    val input = readInput("Day08")
    val testInput1 = readInput("Day08_test1")
    val testInput2 = readInput("Day08_test2")

    check(part1(testInput1) == 2)
    check(part1(testInput2) == 6)
    part1(input).println()

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
