import kotlin.math.min

fun main() {

    fun backtrack(groups: MutableList<String>, damaged: MutableList<Int>): Int {
        // Try to place damaged[0] at every possible location in the group.
//        println(groups)
//        println(damaged)

        // We've supposedly placed all of damaged; are there any damaged left?
        if (damaged.isEmpty()) {
            if (groups.flatMap { it.toList() }.any { it == '#' }) {
                return 0
            } else {
//                println("foo 1")
                return 1
            }
        }

        // We are out of springs but haven't accounted for all the damaged ones
        if (groups.isEmpty()) {
            return 0
        }

        val cur = groups.removeLast()
        if (cur.isBlank()) {
            val retval = backtrack(groups, damaged)
            groups.add(cur)
//            println("bar ${retval}")
            return retval
        }

        val dmg = damaged.removeLast()

        // We must place something here
        if (cur[0] == '#') {
            if (cur.length < dmg || cur.length > dmg && cur[dmg] == '#') {
                groups.add(cur)
                damaged.add(dmg)
                return 0
            }

            groups.add(cur.substring(min(dmg + 1, cur.length)))
            val retval = backtrack(groups, damaged)

            groups.removeLast()
            groups.add(cur)
            damaged.add(dmg)
//            println("baz ${retval}")
            return retval
        }

        // We can't fit anything so let's drop the string and move on
        if (cur.length < dmg) {
            damaged.add(dmg)

            var retval = 0
            if ('#' !in cur) {
                retval += backtrack(groups, damaged)
            }

            groups.add(cur)
//            println("qux ${retval}")
            return retval
        }

        if (cur.length == dmg) {
            var retval = 0
            retval += backtrack(groups, damaged)
            damaged.add(dmg)
            if ('#' !in cur) {
                retval += backtrack(groups, damaged)
            }
            groups.add(cur)
//            println("ol ${retval}")
            return retval
        }

        var retval = 0
        if (cur[dmg] == '?') {
            groups.add(cur.substring(min(dmg + 1, cur.length)))
            retval += backtrack(groups, damaged)
            groups.removeLast()
        }

        damaged.add(dmg)
        groups.add(cur.substring(min(1, cur.length)))
        retval += backtrack(groups, damaged)
        groups.removeLast()

        groups.add(cur)
//        println("blep ${retval}")
        return retval
    }


    fun foo(row: String): Int {
        val springs = row.substringBefore(' ')
        val damaged = row.substringAfter(' ').split(',').map { it.toInt() }.reversed().toMutableList()

        val groups = springs.split("\\.+".toRegex()).reversed().toMutableList()
        val result = backtrack(groups, damaged)

        println(row)
        println(result)
        println()
//        println()
        return result
    }

    /**
     * Springs are operational ('.'), damaged ('#'), or unknown ('?').
     *
     * We're also given a list of numbers indicating the size of each contiguous group of
     * damaged springs.
     *
     * Determine how many different arrangements of damaged and operational springs are possible in each row.
     */
    fun part1(input: List<String>): Int {
        val result = input.sumOf { foo(it) }
        return result
    }

    fun part2(input: List<String>): Int {
        return 0
    }



    val input = readInput("Day12")
    val testInput = readInput("Day12_test")
    val playInput = readInput("Day12_play")

    part1(playInput).println()


    check(part1(testInput) == 21)
    part1(input).println()

//    check(part2(testInput, 2L) == 374L)
//    part2(input, 1000000L).println()
}
