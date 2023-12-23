import java.security.InvalidParameterException
import kotlin.math.max
import kotlin.math.min


fun main() {
    val PATH = '.'
    val FOREST = '#'
    val UP = '^'
    val DOWN = 'v'
    val LEFT = '<'
    val RIGHT = '>'

    fun findFirstPath(line: String): Int {
        for ((i, c) in line.withIndex()) {
            if (c == PATH) {
                return i
            }
        }

        throw InvalidParameterException()
    }

    fun isValid(point: Pair<Int, Int>, grid: List<List<Char>>): Boolean {
        val (i, j) = point

        if (i < 0 || i > grid.size - 1) {
            return false
        }

        if (j < 0 || j > grid[0].size - 1) {
            return false
        }

        return grid[i][j] != FOREST
    }

    fun movesFrom(point: Pair<Int, Int>, grid: List<List<Char>>, visited: Set<Pair<Int, Int>>): List<Pair<Int, Int>> {
        val candidates = mutableListOf<Pair<Int, Int>>()

        val (i, j) = point
        when (grid[i][j]) {
            PATH -> {
                if (isValid(Pair(i - 1, j), grid)) {
                    candidates.add(Pair(i-1, j))
                }

                if (isValid(Pair(i + 1, j), grid)) {
                    candidates.add(Pair(i+1, j))
                }

                if (isValid(Pair(i, j - 1), grid)) {
                    candidates.add(Pair(i, j - 1))
                }

                if (isValid(Pair(i, j + 1), grid)) {
                    candidates.add(Pair(i, j + 1))
                }
            }
            UP -> {
                if (isValid(Pair(i - 1, j), grid)) {
                    candidates.add(Pair(i-1, j))
                }
            }

            DOWN -> {
                if (isValid(Pair(i + 1, j), grid)) {
                    candidates.add(Pair(i+1, j))
                }
            }

            LEFT -> {
                if (isValid(Pair(i, j - 1), grid)) {
                    candidates.add(Pair(i, j - 1))
                }
            }

            RIGHT -> {
                if (isValid(Pair(i, j + 1), grid)) {
                    candidates.add(Pair(i, j + 1))
                }
            }

            FOREST -> listOf<Pair<Int, Int>>()
        }

        return candidates.filter { it !in visited }
    }

    fun dfs(grid: List<List<Char>>, cur: Pair<Int, Int>, goal: Pair<Int, Int>, visited: MutableSet<Pair<Int, Int>>): Int {
        if (cur == goal) {
            println("#########################################################")
            println("We made it to ${cur}!!")
            println("Length: ${visited.size + 1}")
            println("#########################################################")
            println()
            return visited.size
        }

        val children = movesFrom(cur, grid, visited)
        if (children.isEmpty()) {
            println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%")
            println("No candidates found at ${cur}!!")
            println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%")
            println()
            return 0
        }

        println("${cur}; Candidates include ${children};")
        visited.add(cur)
        println("The path consists of ${visited}")
        println()
        var best = 0
        for (child in children) {
            val res = dfs(grid, child, goal, visited)
            best = max(best, res)
        }


        visited.remove(cur)
        return best
    }



    fun part1(input: List<String>): Int {
        val start = Pair(0, findFirstPath(input[0]))
        val goal = Pair(input.size - 1, findFirstPath(input.last()))

        val grid = input.toMutableCharMatrix()
        val visited = mutableSetOf<Pair<Int, Int>>()

        val res = dfs(grid, start, goal, visited)
        println(res)
        return res
    }



    fun part2(input: List<String>): Int {
        return 0
    }

    val input = readInput("Day23")
    val testInput = readInput("Day23_test")

    check(part1(testInput) == 94)
    part1(input).println()

//    check(part2(testInput) == 7)
//    part2(input).println()
}
