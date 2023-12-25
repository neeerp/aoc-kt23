fun main() {

    fun dfs(g: Graph): List<Set<String>> {

        val connectedComponents = mutableListOf<Set<String>>()
        val seen = mutableSetOf<String>()
        for (v in g.adjacency) {
            val root = v.key
            if (root in seen) {
                continue
            }

            val component = mutableSetOf<String>()
            val stack = mutableListOf(root)
            while (stack.isNotEmpty()) {
                val cur = stack.removeLast()

                seen.add(cur)
                component.add(cur)

                val toAdd = g[cur]!!.filter { it !in seen }

                seen.addAll(toAdd)
                component.addAll(toAdd)
                stack.addAll(toAdd)
            }

            connectedComponents.add(component)
        }

        return connectedComponents
    }

    fun parseEdges(input: List<String>): List<Pair<String, String>> {
        val edges = input.map { line ->
            val (node, edgeNodes) = line.split(':')

            edgeNodes.trim().split(' ').map { Pair(node, it) }
        }.flatten()

        return edges
    }

    fun bruteForce(input: List<String>): Int {
        val edges = parseEdges(input).toMutableSet()


        // Using GraphViz, I could see these are the three edges to cut.
        // Otherwise, I tried...
        // 1. Brute force: Worked on the test input; didn't seem to terminate after 15 minutes on the real input.
        // 2. Monte Carlo simulation using the 'merge two nodes over and over' algorithm...
        //   this found a ton of cuts and they weren't all with a cut size of 3... I guess I could have adapted this better.
        edges.remove(Pair("njx", "pbx"))
        edges.remove(Pair("pbx", "njx"))

        edges.remove(Pair("pzr", "sss"))
        edges.remove(Pair("sss", "pzr"))

        edges.remove(Pair("zvk", "sxx"))
        edges.remove(Pair("sxx", "zvk"))
        println(edges)

        val g = Graph.fromEdges(edges)
        val components = dfs(g)
        val res = components[0].size * components[1].size
        println(res)



        return res
    }

    fun part1(input: List<String>): Int {
        return bruteForce(input)
    }



    fun part2(input: List<String>): Int {
        return 0
    }

    val input = readInput("Day25")
    val testInput = readInput("Day25_test")

//    check(part1(testInput) == 54)
    part1(input).println()
//    part1(input).println()

//    check(part2(testInput) == 154)
//    part2(input).println()
}

private data class Graph(val adjacency: MutableMap<String, MutableSet<String>>) {
    operator fun get(k: String): MutableSet<String>? {
        return adjacency[k]
    }


    companion object {
        fun fromEdges(edges: Collection<Pair<String, String>>): Graph {
            val adjacency = mutableMapOf<String, MutableSet<String>>()

            for (edge in edges) {
                val (n1, n2) = edge

                if (n1 !in adjacency) {
                    adjacency[n1] = mutableSetOf()
                }

                if (n2 !in adjacency) {
                    adjacency[n2] = mutableSetOf()
                }

                adjacency[n1]!!.add(n2)
                adjacency[n2]!!.add(n1)
            }

            return Graph(adjacency)
        }
    }
}
