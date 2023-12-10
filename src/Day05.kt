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
     * About those seeds... those are actually ranges of the form (start, length) :)
     *
     * Instead of being smart about this, I brute forced it with some parallelism and a little
     * bit more care for our memory use so we don't OOM on my lowly 64GB of RAM.
     *
     * I'm sure a smarter solution that doesn't take 2 minutes to run exists, but at least I got
     * an excuse to try some parallelism in Kotlin :D
     */
    fun part2(input: String): Long {
        val inputSegments = input.split("\n\n")
        val seeds = inputSegments[0].substringAfter("seeds:").trim().split(' ').map { it.toLong() }.chunked(2)

        val maps = inputSegments.drop(1).map { AdventMap(it) }
        return seeds.parallelStream().map { seedPair ->
            (seedPair[0]..<(seedPair[0] + seedPair[1])).minOf { chainMap(it, maps) }
        }.toList().min()
    }

    val input = readRaw("Day05")
    val testInput = readRaw("Day05_test")

    check(part1(testInput) == 35L)
    part1(input).println()

    check(part2(testInput) == 46L)
    part2(input).println()
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
