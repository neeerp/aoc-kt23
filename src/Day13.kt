private const val b = true

fun main() {

    fun isSymmetric(lines: List<String>, lineOfSymmetry: Int): Boolean {
        if (lineOfSymmetry == 0) {
            return false
        }

        var l = lineOfSymmetry - 1
        var r = lineOfSymmetry

        while (l >= 0 && r < lines.size) {
            if (lines[l] != lines[r]) {
                return false
            }

            l -= 1
            r += 1
        }
        return true
    }

    fun findSymmetry(lines: List<List<Char>>): Int {
        var lines = lines.map{ it.joinToString("") }
        for (i in 0..<lines.size) {
            if (isSymmetric(lines, i)) {
                return i
            }
        }

        return 0
    }

    fun processMirror(mirrorText: String): Int {
        val horizontal = mirrorText.split("\n").map { it.toList() }
        val vertical = horizontal.transpose()

        var result = findSymmetry(vertical) + 100 * findSymmetry(horizontal)
        return result

    }


    fun part1(input: String): Int {
        return input.split("\n\n").map { processMirror(it) }.sum()
    }

    fun part2(input: String): Int {
        return 0
    }

    val testInput = readRaw("Day13_test")
    val playInput = readRaw("Day13_play")
    val input = readRaw("Day13")

    check(part1(testInput) == 405)
//    part1(playInput).println()
    part1(input).println()

//    check(part2(testInput) == 2)
//    part2(input).println()
}
