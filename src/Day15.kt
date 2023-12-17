fun main() {

    fun computeHash(s: String): Int {
        var cv = 0
        for (c in s) {
            cv += c.code
            cv = (cv * 17) % 256
        }

        return cv
    }

    class HolidayHashMap() {
        private val buckets = arrayOfNulls<HolidayHashNode>(256)

        fun computeFocusingPower(): Int {
            var total = 0
            for (i in 0..<256) {
                var cur = buckets[i]
                var slot = 1
                while (cur != null) {
                    total += (i + 1) * slot * cur.v
                    slot += 1
                    cur = cur.next
                }
            }

            return total
        }

        operator fun get(k: String): Int? {
            val hash = computeHash(k)

            var head = buckets[hash]
            while (head != null) {
                if (head.k == k) {
                    return head.v
                }
                head = head.next
            }
            return null
        }

        operator fun set(k: String, v: Int) {
            val hash = computeHash(k)
            val newNode = HolidayHashNode(k, v)

            var cur = buckets[hash]
            if (cur == null) {
                buckets[hash] = newNode
                return
            }

            while (cur!!.next != null) {
                if (cur!!.k == k) {
                    break
                }

                cur = cur!!.next
            }

            if (cur!!.k == k) {
                cur!!.v = v
                return
            }

            cur!!.next = newNode
            newNode.prev = cur
        }

        fun delete(k: String) {
            val hash = computeHash(k)

            var cur = buckets[hash]

            while (cur != null) {
                if (cur!!.k == k) {
                    break
                }
                cur = cur!!.next
            }

            if (cur == null) {
                return
            }

            if (cur!!.prev == null) {
                // Edge case: Head
                buckets[hash] = cur!!.next
                if (cur!!.next != null) {
                    cur!!.next!!.prev = null
                }
                return
            }  else if (cur!!.next == null) {
                cur!!.prev!!.next = cur!!.next
            } else {
                cur!!.prev!!.next = cur!!.next
                cur!!.next!!.prev = cur!!.prev
            }

            return
        }
    }

    fun part1(input: String): Int {
        return input.split(',').sumOf { computeHash(it) }
    }

    fun part2(input: String): Int {
        val holidyHashMap = HolidayHashMap()

        input.split(',').forEach {
            if (it.last() == '-') {
                // DELETE
                val label = it.substringBefore('-')
                holidyHashMap.delete(label)
            } else {
                val label = it.substringBefore('=')
                val value = it.substringAfter('=').toInt()
                holidyHashMap[label] = value
            }
        }
        return holidyHashMap.computeFocusingPower()
    }

    // test if implementation meets criteria from the description, like:
    val input = readRaw("Day15")
    val testInput = readRaw("Day15_test")

    check(computeHash("HASH") == 52)
    check(part1(testInput) == 1320)
    part1(input).println()

    check(part2(testInput) == 145)
    part2(input).println()
}

data class HolidayHashNode(val k: String, var v: Int) {
    var next: HolidayHashNode? = null
    var prev: HolidayHashNode? = null
}
