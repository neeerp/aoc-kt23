import java.security.InvalidParameterException

fun main() {

    fun part1(input: String): Long {
        val (workflowInput, partInput) = input.split("\n\n")

        val parts = partInput.split('\n').map { Part.of(it) }
        val sorter = Sorter(workflowInput.split('\n'))

        return parts.parallelStream().filter { sorter.process(it) }.map { it.partSum() }.toList().sum()
    }


    fun part2(input: String): Long {
        return 0L
    }

    val input = readRaw("Day19")
    val testInput = readRaw("Day19_test")

    check(part1(testInput) == 19114L)
    part1(input).println()

//    check(part2(testInput) == 167409079868000L)
//    part2(input).println()
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
}

interface WorkflowStep {
    fun transition(p: Part): String?
}

class UnconditionalStep(private val label: String) : WorkflowStep {
    override fun transition(p: Part): String {
        return label
    }
}

class ConditionalStep(s: String) : WorkflowStep {
    private val attr: Char
    private val op: (Long) -> Boolean
    private val threshold: Long
    private val label: String

    init {
        attr = s[0]
        label = s.substringAfter(':')
        threshold = s.drop(2).substringBefore(':').toLong()
        op = when (s[1]) {
            '<' -> {a: Long -> a < threshold}
            '>' -> {a: Long -> a > threshold}
            else -> throw UnsupportedOperationException()
        }
    }

    override fun transition(p: Part): String? {
        return if (evaluate(p)) label else null
    }

    private fun evaluate(p: Part): Boolean {
        return op(p[attr])
    }
}

data class Part(val x: Long, val m: Long, val a: Long, val s: Long) {
    operator fun get(attr: Char): Long {
        return when (attr) {
            'x' -> x
            'm' -> m
            'a' -> a
            's' -> s
            else -> throw InvalidParameterException()
        }
    }

    fun partSum(): Long {
        return x + m + a + s
    }

    companion object {
        fun of(s: String): Part {
            val (x, m, a, s) = s.drop(1).dropLast(1).split(',').map {
                it.substringAfter('=').toLong()
            }

            return Part(x, m, a, s)
        }
    }
}
