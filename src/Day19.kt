import java.security.InvalidParameterException

fun main() {

    fun part1(input: String): Int {
        val (workflowInput, partInput) = input.split("\n\n")

        val parts = partInput.split('\n').map { Part.of(it) }
        val sorter = Sorter(workflowInput.split('\n'))

        return parts.parallelStream().filter { sorter.process(it) }.map { it.partSum() }.toList().sum()
    }


    fun part2(input: String): Long {
        val sorter = Sorter(input.substringBefore("\n\n").split("\n"))
        val seed = PartSet(
            AttributeRange(1, 4000),
            AttributeRange(1, 4000),
            AttributeRange(1, 4000),
            AttributeRange(1, 4000)
        )

        val result = sorter.process(seed).sumOf { it.size() }
        return result
    }

    val input = readRaw("Day19")
    val testInput = readRaw("Day19_test")

    check(part1(testInput) == 19114)
    part1(input).println()

    check(part2(testInput) == 167409079868000L)
    part2(input).println()
}

class Sorter(workflowInputs: List<String>) {
    private val workflows: Map<String, Workflow>

    init {
        workflows = workflowInputs.associate {
            val workflow = Workflow(it)
            workflow.label to workflow
        }
    }

    fun process(p: Part): Boolean {
        var label = "in"
        while (!isTerminal(label)) {
            label = workflows[label]!!.transition(p)
        }

        return label == "A"
    }

    private fun isTerminal(label: String): Boolean {
        return label == "A" || label == "R"
    }

    fun process(seed: PartSet): List<PartSet> {
        val stack = mutableListOf(Pair("in", seed))
        val result = mutableListOf<PartSet>()
        while (stack.isNotEmpty()) {
            val (label, ps) = stack.removeLast()
            val workflow = workflows[label]!!
            val disjointPartSets = workflow.transition(ps)

            for ((nextLabel, nextPs) in disjointPartSets) {
                if (nextLabel == "A") {
                    result.add(nextPs)
                } else if (nextLabel != "R") {
                    stack.add(Pair(nextLabel,nextPs))
                }
            }
        }

        return result
    }
}

class Workflow(s: String) {
    val label: String
    private val steps: List<WorkflowStep>

    init {
        label = s.substringBefore('{')
        steps = s.substringAfter('{').dropLast(1).split(',').map {
            if (':' !in it) {
                UnconditionalStep(it)
            } else {
                ConditionalStep(it)
            }
        }
    }

    fun transition(p: Part): String {
        for (step in steps) {
            val candidate = step.transition(p)
            if (candidate != null) {
                return candidate
            }
        }

        throw InvalidParameterException()
    }

    fun transition(ps: PartSet): List<Pair<String, PartSet>> {
        val result = mutableListOf<Pair<String, PartSet>>()

        var cur = listOf(ps)
        for (step in steps) {
            val transitioning = mutableListOf<PartSet>()
            val notTransitioning = mutableListOf<PartSet>()
            for ((left, right) in cur.map { step.transition(it) }) {
                transitioning.addAll(left)
                notTransitioning.addAll(right)
            }

            cur = notTransitioning
            result.addAll(transitioning.map { Pair(step.label, it)  })
        }

        return result
    }
}

interface WorkflowStep {
    val label: String
    fun transition(p: Part): String?

    fun transition(ps: PartSet): Pair<List<PartSet>, List<PartSet>>
}

class UnconditionalStep(override val label: String) : WorkflowStep {
    override fun transition(p: Part): String {
        return label
    }

    override fun transition(ps: PartSet): Pair<List<PartSet>, List<PartSet>> {
        return Pair(listOf(ps), listOf())
    }
}

class ConditionalStep(s: String) : WorkflowStep {
    private val attr: Char
    private val op: (Int) -> Boolean
    private val opChar: Char
    private val threshold: Int
    override val label: String

    init {
        attr = s[0]
        label = s.substringAfter(':')
        threshold = s.drop(2).substringBefore(':').toInt()

        opChar = s[1]
        op = when (s[1]) {
            '<' -> {a: Int -> a < threshold}
            '>' -> {a: Int -> a > threshold}
            else -> throw UnsupportedOperationException()
        }
    }

    override fun transition(p: Part): String? {
        return if (evaluate(p)) label else null
    }

    override fun transition(ps: PartSet): Pair<List<PartSet>, List<PartSet>> {
        val (left, right) = ps.split(attr, threshold - (if (opChar == '<') 1 else 0))

        val willTransition = listOfNotNull(if (opChar == '<') left else right)
        val willNotTransition = listOfNotNull(if (opChar == '<') right else left)

        return Pair(willTransition, willNotTransition)
    }

    private fun evaluate(p: Part): Boolean {
        return op(p[attr])
    }
}

data class Part(val x: Int, val m: Int, val a: Int, val s: Int) {
    operator fun get(attr: Char): Int {
        return when (attr) {
            'x' -> x
            'm' -> m
            'a' -> a
            's' -> s
            else -> throw InvalidParameterException()
        }
    }

    fun partSum(): Int {
        return x + m + a + s
    }

    companion object {
        fun of(input: String): Part {
            val (x, m, a, s) = input.drop(1).dropLast(1).split(',').map {
                it.substringAfter('=').toInt()
            }

            return Part(x, m, a, s)
        }
    }
}

data class PartSet(
    val x: AttributeRange,
    val m: AttributeRange,
    val a: AttributeRange,
    val s: AttributeRange
) {

    // Left inclusive
    fun split(attr: Char, firstEndsAt: Int): Pair<PartSet?, PartSet?> {
        val r1 = this[attr].copy(end = firstEndsAt)
        val r2 = this[attr].copy(start = firstEndsAt + 1)

        val left = if (r1.start <= r1.end) this.copyWith(attr, r1) else null
        val right = if (r2.start <= r2.end) this.copyWith(attr, r2) else null
        return Pair(left, right)
    }

    fun size(): Long {
        return x.size() * m.size() * a.size() * s.size()
    }

    operator fun get(attr: Char): AttributeRange {
        return when (attr) {
            'x' -> x
            'm' -> m
            'a' -> a
            's' -> s
            else -> throw InvalidParameterException()
        }
    }

    private fun copyWith(attr: Char, a: AttributeRange): PartSet {
        return when (attr) {
            'x' -> this.copy(x = a)
            'm' -> this.copy(m = a)
            'a' -> this.copy(a = a)
            's' -> this.copy(s = a)
            else -> throw UnsupportedOperationException()
        }
    }
}

data class AttributeRange(val start: Int, val end: Int) {
    fun size(): Long {
        return end - start + 1L
    }
}
