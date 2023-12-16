import kotlin.math.min

fun main() {

    fun backtrack(groups: MutableList<String>, damaged: MutableList<Int>, mem: MutableMap<Pair<String,String>, Long>): Long {
        // Try to place damaged[0] at every possible location in the group.

        //MEMO
        var groupsm = groups.joinToString(" ").trim()
        var damagedm = damaged.joinToString(",")
        var keym = Pair(groupsm, damagedm)
        if (keym in mem) {
            return mem[keym]!!
        }
        ///

        // We've supposedly placed all of damaged; are there any damaged left?
        if (damaged.isEmpty()) {
            if (groups.flatMap { it.toList() }.any { it == '#' }) {
                mem[keym] = 0
                return 0
            } else {
                mem[keym] = 1
                return 1
            }
        }

        // We are out of springs but haven't accounted for all the damaged ones
        if (groups.isEmpty()) {
            mem[keym] = 0
            return 0
        }

        val cur = groups.removeLast()
        if (cur.isBlank()) {
            val retval = backtrack(groups, damaged, mem)
            groups.add(cur)
            mem[keym] = retval
            return retval
        }

        val dmg = damaged.removeLast()

        // We must place something here
        if (cur[0] == '#') {
            if (cur.length < dmg || cur.length > dmg && cur[dmg] == '#') {
                groups.add(cur)
                damaged.add(dmg)
                mem[keym] = 0
                return 0
            }

            groups.add(cur.substring(min(dmg + 1, cur.length)))
            val retval = backtrack(groups, damaged, mem)

            groups.removeLast()
            groups.add(cur)
            damaged.add(dmg)
            mem[keym] = retval
            return retval
        }

        // We can't fit anything so let's drop the string and move on
        if (cur.length < dmg) {
            damaged.add(dmg)

            var retval = 0L
            if ('#' !in cur) {
                retval += backtrack(groups, damaged, mem)
            }

            groups.add(cur)
            mem[keym] = retval
            return retval
        }

        if (cur.length == dmg) {
            var retval = 0L
            retval += backtrack(groups, damaged, mem)
            damaged.add(dmg)
            if ('#' !in cur) {
                retval += backtrack(groups, damaged, mem)
            }
            groups.add(cur)
            mem[keym] = retval
            return retval
        }

        var retval = 0L
        if (cur[dmg] == '?') {
            groups.add(cur.substring(min(dmg + 1, cur.length)))
            retval += backtrack(groups, damaged, mem)
            groups.removeLast()
        }

        damaged.add(dmg)
        groups.add(cur.substring(min(1, cur.length)))
        retval += backtrack(groups, damaged, mem)
        groups.removeLast()

        groups.add(cur)
        mem[keym] = retval
        return retval
    }


    fun foo(row: String): Long {
        val springs = row.substringBefore(' ')
        val damaged = row.substringAfter(' ').split(',').map { it.toInt() }.reversed().toMutableList()

        val groups = springs.split("\\.+".toRegex()).reversed().toMutableList()
        val result = backtrack(groups, damaged, mutableMapOf())

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
        return result.toInt()
    }

    fun multiplyLine(line: String): String {
        val springs = List(5) { _ -> line.substringBefore(' ') }.joinToString("?")
        val damaged = List(5) { _ -> line.substringAfter(' ') }.joinToString(",")

        return "${springs} ${damaged}"
    }

    fun part2(input: List<String>): Long {
        val multiplied = input.map { multiplyLine(it) }
        val result = multiplied.parallelStream().map {
            val res = foo(it)

            res
        }.toList().sum()

        return result
    }



    val input = readInput("Day12")
    val testInput = readInput("Day12_test")
    val playInput = readInput("Day12_play")

    part1(playInput).println()


    check(part1(testInput) == 21)
    part1(input).println()

    check(part2(testInput) == 525152L)
    part2(input).println()
}
