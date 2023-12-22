import java.security.InvalidParameterException
import kotlin.math.max
import kotlin.math.min

fun main() {

    fun part1(input: List<String>): Int {
        val suspendedBricks = input.map{ Brick.of(it) }.sortedByDescending { - it.minZ() }
        val settledBricks = mutableListOf<Brick>()
        val maxHeights = mutableMapOf<Pair<Int, Int>, Int>()
        val grid = mutableMapOf<Triple<Int, Int, Int>, Brick>()

        for (brick in suspendedBricks) {
            // Settle the bricks
            val settleHeight = brick.occupies().maxOf {
                val (x, y, _) = it
                (maxHeights[Pair(x, y)] ?: 0) + 1
            }
            val settledBrick = brick.settled(settleHeight)

            settledBricks.add(brick.settled(settleHeight))
            settledBrick.occupies().forEach {
                val (x, y, z) = it
                maxHeights[Pair(x, y)] = z
                grid[it] = settledBrick
            }
        }

        for (brick in settledBricks) {
            // Determine what bricks are being supported.
            brick.cubesAbove().forEach {
                grid[it]?.let { it1 -> brick.registerSupportFor(it1) }
            }
        }

        val result = settledBricks.count {
            it.isNotLoadBearing()
        }

        return result
    }



    fun part2(input: List<String>): Long {
        return 0L
    }

    val input = readInput("Day22")
    val testInput = readInput("Day22_test")

    check(part1(testInput) == 5)
    part1(input).println()

//    check(part2(testInput) == 167409079868000L)
//    part2(input).println()
}

data class Brick(val a: Triple<Int, Int, Int>, val b: Triple<Int, Int, Int>) {
    private val orientation: Orientation
    private val ax = a.first
    private val ay = a.second
    private val az = a.third

    private val bx = b.first
    private val by = b.second
    private val bz = b.third

    private val supportedBy = mutableSetOf<Brick>()
    private val supporting = mutableSetOf<Brick>()

    init {
        if (ax != bx) {
            orientation = Orientation.X
        } else if (ay != by) {
            orientation = Orientation.Y
        } else if (az != bz) {
            orientation = Orientation.Z
        } else if (a == b) {
            orientation = Orientation.Z
        } else {
            println(a)
            println(b)
            throw InvalidParameterException()
        }
    }

    companion object {
        fun of(s: String): Brick {
            val (left, right) = s.split('~')
            val (ax, ay, az) = left.split(',').map { it.toInt() }
            val (bx, by, bz) = right.split(',').map { it.toInt() }

            return Brick(Triple(ax, ay, az), Triple(bx, by, bz))
        }
    }

    fun registerSupportFor(other: Brick) {
        supporting.add(other)
        other.supportedBy.add(this)
    }

    fun isNotLoadBearing(): Boolean {
        return supporting.isEmpty() || supporting.all {
            it.supportedBy.size > 1
        }
    }


    fun minX(): Int {
        return min(ax, bx)
    }

    fun minY(): Int {
        return min(ay, by)
    }

    fun minZ(): Int {
        return min(az, bz)
    }

    fun maxX(): Int {
        return max(ax, bx)
    }

    fun maxY(): Int {
        return max(ay, by)
    }

    fun maxZ(): Int {
        return max(az, bz)
    }


    fun occupies(): List<Triple<Int, Int, Int>> {
        return when (orientation) {
            Orientation.X -> (minX()..maxX()).map { Triple(it, ay, az) }
            Orientation.Y -> (minY()..maxY()).map { Triple(ax, it, az) }
            Orientation.Z -> (minZ()..maxZ()).map { Triple(ax, ay, it) }
        }
    }

    fun cubesAbove(): List<Triple<Int, Int, Int>> {
        return when (orientation) {
            Orientation.X, Orientation.Y ->  {
                occupies().map { it.copy(third = it.third + 1) }
            }
            Orientation.Z -> {
                listOf(Triple(ax, ay, maxZ() + 1))
            }
        }

    }

    fun settled(height: Int): Brick {
        return when (orientation) {
            Orientation.X, Orientation.Y -> Brick(Triple(ax, ay, height), Triple(bx, by, height))
            Orientation.Z ->  {
                val lo = height
                val hi = maxZ() - minZ() + height

                val azNew: Int
                val bzNew: Int
                if (az == maxZ()) {
                    azNew = hi
                    bzNew = lo
                } else {
                    azNew = lo
                    bzNew = hi
                }

                Brick(Triple(ax, ay, azNew), Triple(bx, by, bzNew))
            }
        }
    }
}

enum class Orientation {
    X, Y, Z
}
