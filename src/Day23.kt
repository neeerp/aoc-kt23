import java.security.InvalidParameterException
import kotlin.math.max


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

    fun movesFromPart2(point: Pair<Int, Int>, grid: List<List<Char>>, visited: Set<Pair<Int, Int>>): MutableList<Pair<Int, Int>> {
        val candidates = mutableListOf<Pair<Int, Int>>()

        val (i, j) = point
        when (grid[i][j]) {
            PATH, UP, DOWN, LEFT, RIGHT -> {
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
            FOREST -> listOf<Pair<Int, Int>>()
        }

        return candidates.filter { it !in visited }.toMutableList()
    }

    fun dfsIterative(grid: List<List<Char>>, start: Pair<Int, Int>, goal: Pair<Int, Int>, debug: Boolean): Int {

        // Each entry on the stack should be a node and the number of branches remaining...
        // i.e. the candidate list.
        val visited = mutableSetOf<Pair<Int, Int>>(start)
        val stack = mutableListOf(Pair(start, movesFromPart2(start, grid, visited)))
        var best = 0
        while (stack.isNotEmpty()) {
            val (cur, curPending) = stack.removeLast()
            if (cur == goal) {
                if (debug) {
                    println("#########################################################")
                    println("We made it to ${cur}!!")
                    println("Length: ${visited.size + 1}")
                    println("#########################################################")
                    println()
                }
                best = max(visited.size - 1, best)
                visited.remove(cur)
                continue
            }

            if (curPending.isEmpty()) {
                visited.remove(cur)
                continue
            }

            val next = curPending.removeLast()
            visited.add(next)
            stack.add(Pair(cur, curPending))

            val candidates = movesFromPart2(next, grid, visited)
            stack.add(Pair(next, candidates))
        }

        return best
    }

    fun dfs(grid: List<List<Char>>, cur: Pair<Int, Int>, goal: Pair<Int, Int>, visited: MutableSet<Pair<Int, Int>>, part: Int): Int {
        if (cur == goal) {
            println("#########################################################")
            println("We made it to ${cur}!!")
            println("Length: ${visited.size + 1}")
            println("#########################################################")
            println()
            return visited.size
        }

        val children: List<Pair<Int, Int>>
        if (part == 1) {
            children = movesFrom(cur, grid, visited)
        } else {
            children = movesFromPart2(cur, grid, visited)
        }

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
            val res: Int
            if (part == 1) {
                res = dfs(grid, child, goal, visited, 1)
            } else {
                res = dfs(grid, child, goal, visited, 2)
            }
            best = max(best, res)
        }


        visited.remove(cur)
        return best
    }

    fun findJunction(point: Pair<Int, Int>, grid: List<List<Char>>, visited: MutableSet<Pair<Int, Int>>): Pair<Pair<Int, Int>, Int> {
        var cur = point
        var count = 1
        visited.add(cur)

        var candidates = movesFromPart2(cur, grid, visited)
        while (candidates.size == 1) {
            cur = candidates.removeLast()
            visited.add(cur)
            candidates = movesFromPart2(cur, grid, visited)
            count += 1
        }

        return Pair(cur, count)
    }

    fun truncate(grid: List<List<Char>>): Map<Pair<Int, Int>, List<Pair<Pair<Int, Int>, Int>>> {
        val junctions = mutableMapOf<Pair<Int, Int>, List<Pair<Pair<Int, Int>, Int>>> ()
        grid.indices.forEach { i -> grid[0].indices.forEach { j ->
            val candidates = movesFromPart2(Pair(i, j), grid, setOf())
            if (candidates.size == 1 || candidates.size > 2) {
                val links = candidates.map { findJunction(it, grid, mutableSetOf(Pair(i, j))) }
                junctions[Pair(i, j)] = links
            }
        } }

        return junctions
    }


    fun part1(input: List<String>): Int {
        val start = Pair(0, findFirstPath(input[0]))
        val goal = Pair(input.size - 1, findFirstPath(input.last()))

        val grid = input.toMutableCharMatrix()
        val visited = mutableSetOf<Pair<Int, Int>>()

        val res = dfs(grid, start, goal, visited, 1)
        println(res)
        return res
    }



    fun dfs(graph: Map<Pair<Int, Int>, List<Pair<Pair<Int, Int>, Int>>>, start: Pair<Int, Int>, goal: Pair<Int, Int>, debug: Boolean = false): Int {
        val visited = mutableMapOf<Pair<Int, Int>, Int>()
        visited[start] = 0

        val stack = mutableListOf(Pair(start, graph[start]!!.toMutableList()))
        var best = 0
        while (stack.isNotEmpty()) {
            val (cur, curPending) = stack.removeLast()
            if (cur == goal) {
                val length = visited.values.sum()
                if (debug) {
                    println("#########################################################")
                    println("We made it to ${cur}!!")
                    println("Length: ${length}")
                    println("#########################################################")
                    println()
                }
                best = max(length, best)
                visited.remove(cur)
                continue
            }

            if (curPending.isEmpty()) {
                visited.remove(cur)
                continue
            }

            val (next, length) = curPending.removeLast()
            stack.add(Pair(cur, curPending))

            visited[next] = length
            val candidates: MutableList<Pair<Pair<Int, Int>, Int>> = graph[next]!!.filter { it.first !in visited }.toMutableList()
            stack.add(Pair(next, candidates))
        }

        return best

    }
    fun part2(input: List<String>): Int {
        val graph = truncate(input.toMutableCharMatrix())
        val start = Pair(0, findFirstPath(input[0]))
        val goal = Pair(input.size - 1, findFirstPath(input.last()))

        val result = dfs(graph, start, goal)
        println(result)
        return result
    }

    val input = readInput("Day23")
    val testInput = readInput("Day23_test")

//    check(part1(testInput) == 94)
//    part1(input).println()

//    check(part2(testInput) == 154)
    part2(input).println()
}
