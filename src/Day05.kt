fun main() {

    fun chainMap(value: Long, maps: List<AdventMap>): Long {
        var result = value
        for (map in maps) {
            result = map.map(result)
        }

        return result
    }

    /**
     * You've got a list of seeds, and then a number of maps starting at seeds
     * to (something) and ending at (something else) to locations.
     *
     * Return the minimum location any given seed is mapped to.
     */
    fun part1(input: String): Long {
        val inputSegments = input.split("\n\n")
        val seeds = inputSegments[0].substringAfter("seeds:").trim().split(' ').map { it.toLong() }


        val maps = inputSegments.drop(1).map { AdventMap(it) }
        return seeds.map { chainMap(it, maps) }.min()
    }



    /**
     */
    fun part2(input: String): Long {
        return 0
    }

    val input = readRaw("Day05")
    val testInput = readRaw("Day05_test")

    check(part1(testInput) == 35L)
    part1(input).println()

//    check(part2(testInput) == 30)
//    part2(input).println()
}

class AdventMap(input: String) {
    private val mappings = input.lines().drop(1).map {
        val items = it.split(' ')
        AdventMapping(items[0].toLong(), items[1].toLong(), items[2].toLong())
    }

    fun map(value: Long): Long {
        return mappings.firstNotNullOfOrNull {
            it.map(value)
        } ?: value
    }

}

class AdventMapping(private val dst: Long, private val src: Long, private val len: Long) {
    fun map(value: Long): Long? {
        if (value >= src && value < src + len) {
            return dst + (value - src)
        }
        return null
    }
}
