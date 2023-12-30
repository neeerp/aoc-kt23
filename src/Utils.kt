import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.io.path.readText

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readLines()

fun readRaw(name: String) = Path("src/$name.txt").readText()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)


fun Collection<Int>.product(): Int = this.reduce { product: Int, element: Int -> product * element }

fun <T>List<List<T>>.transpose(): List<List<T>> {
    return (this[0].indices).map { i -> (this.indices).map { j -> this[j][i]} }
}

fun List<String>.toMutableCharMatrix(): MutableList<MutableList<Char>> {
    return this.map { it.toMutableList() }.toMutableList()
}

fun List<List<Char>>.toMutable(): MutableList<MutableList<Char>> {
    return this.map { it.toMutableList() }.toMutableList()
}

fun List<String>.toMutableIntMatrix(): MutableList<MutableList<Int>> {
    return this.map { line -> line.map { it.toString().toInt() }.toMutableList() }.toMutableList()
}

fun List<List<Char>>.stringify(): String {
    return this.map { line -> line.joinToString("") }.joinToString("\n")
}

fun isPointInBounds(coords: Pair<Int, Int>, m: Int, n: Int): Boolean {
    val (i, j) = coords
    if (i < 0 || i >= m) {
        return false
    }

    if (j < 0 || j >= n) {
        return false
    }

    return true
}
