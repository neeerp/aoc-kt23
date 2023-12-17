private const val b = true

fun main() {

    fun isSymmetric(lines: List<String>, lineOfSymmetry: Int): Boolean {
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

    fun findSymmetry(lines: List<List<Char>>, ignore: Int): Int {
        var lines = lines.map{ it.joinToString("") }
        for (i in 1..<lines.size) {
            if (isSymmetric(lines, i) && i != ignore) {
                return i
            }
        }

        return 0
    }

    fun findSymmetry(lines: List<List<Char>>): Int {
        return findSymmetry(lines, 0)
    }

    fun processMirror(mirrorText: String): Int {
        val horizontal = mirrorText.split("\n").map { it.toList() }
        val vertical = horizontal.transpose()

        var result = findSymmetry(vertical) + 100 * findSymmetry(horizontal)
        return result

    }

    fun smudgeMirror(mirrorText: String): Int {
        val horizontal = mirrorText.split("\n").map { it.toList() }
        val vertical = horizontal.transpose()

        val vline = findSymmetry(vertical)
        val hline = findSymmetry(horizontal)

        val result = mirrorText.indices.toList().parallelStream().map {
            if (mirrorText[it] == '\n') {
                0
            } else {
                val smudgedMirror = mirrorText.substring(0, it) +
                        (if (mirrorText[it] == '#') '.' else '#') +
                        mirrorText.substring(it + 1, mirrorText.length)

                val h = smudgedMirror.split("\n").map { it.toList() }
                val v = h.transpose()

                findSymmetry(v, vline) + findSymmetry(h, hline) * 100
            }
        }.toList().distinct().sum()
        return result
    }


    fun part1(input: String): Int {
        return input.split("\n\n").map { processMirror(it) }.sum()
    }

    fun part2(input: String): Int {
        return input.split("\n\n").map { smudgeMirror(it) }.sum()
    }

    val testInput = readRaw("Day13_test")
    val playInput = readRaw("Day13_play")
    val input = readRaw("Day13")

    check(part1(testInput) == 405)
    part2(playInput).println()
    part1(input).println()

    check(part2(testInput) == 400)
    part2(input).println()
}
