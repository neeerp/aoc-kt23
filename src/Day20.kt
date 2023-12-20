fun main() {

    fun part1(input: List<String>): Long {
        val network = MachineNetwork(input)
        for (times in 1..1000) {
            network.pushButton(true)
        }

        return network.signalProduct()
    }


    fun part2(input: List<String>): Long {
        var count = 0L
        val network = MachineNetwork(input)
        while (true) {
            val started = network.pushButton(false)
            count += 1
            if (started) break
        }
        return count
    }

    val input = readInput("Day20")
    val testInput1 = readInput("Day20_test1")
    val testInput2 = readInput("Day20_test2")

    check(part1(testInput1) == 32000000L)
    check(part1(testInput2) == 11687500L)
    part1(input).println()

    // This never terminates; just look at the console.
    // In my input, there's four nodes that lead into a conjunction that then turns the 'rx' node on.
    // I'm printing out the iteration at which each turns on.
    // The answer turns out to be the product of the iteration # they turn on at.
    part2(input).println()
}

private interface Network {
    fun send(signals: List<Signal>)
    fun pushButton(test: Boolean): Boolean
}

private class MachineNetwork(input: List<String>) : Network {
    // TODO: Maybe encapsulate counting
    private var hi = 0L
    private var lo = 0L
    private var iters = 0L

    private val START_SIGNAL = Signal("btn", "broadcaster", SignalStrength.LO)

    private val signalQueue: ArrayDeque<Signal> = ArrayDeque(32)
    private val nodes: Map<String, Node>

    init {
        nodes = input.associate {
            val node: Node = when (it[0]) {
                '%' -> FlipFlopNode(it, this)
                '&' -> ConjunctionNode(it, this)
                'b' -> BroadcastNode(it, this)
                else -> throw UnsupportedOperationException()
            }
            node.label to node
        }

        for (n1 in nodes.values) {
            for (label in n1.getDestinations()) {
                nodes[label]?.registerUpstream(n1)
            }
        }
    }

    fun signalProduct(): Long {
//        println("hi: ${hi}; lo: ${lo}")
        return hi * lo
    }

    override fun send(signals: List<Signal>) {
        signalQueue.addAll(signals)
    }

    override fun pushButton(test: Boolean): Boolean {
        iters += 1
        signalQueue.add(START_SIGNAL)

        while (signalQueue.isNotEmpty()) {
            val signal = signalQueue.removeFirst()

            if (signal.dst == "mg" && signal.strength == SignalStrength.HI) {
                println("Signal to &mg: ${signal} at iter #${iters}")
            }

            if (!test && signal.dst == "rx" && signal.strength == SignalStrength.LO) {
                return true
            }

            if (signal.strength == SignalStrength.LO) {
                lo += 1
            } else {
                hi += 1
            }

            nodes[signal.dst]?.process(signal)
        }

        return false
    }

}

private interface Node {
    val label: String
    val network: Network
    fun process(signal: Signal)

    fun registerUpstream(n: Node)

    fun getDestinations(): List<String>
}

private class BroadcastNode(input: String, override val network: Network): Node {
    override val label: String
    private val destinations: List<String>

    init {
        val (node, labels) = input.split(" -> ")
        destinations = labels.split(", ")
        label = node
    }

    override fun process(signal: Signal) {
        val signals = destinations.map { Signal(label, it, signal.strength) }
        network.send(signals)
    }

    override fun registerUpstream(n: Node) {}

    override fun getDestinations(): List<String> {
        return destinations
    }
}

private class ConjunctionNode(input: String, override val network: Network): Node {
    override val label: String

    private val destinations: List<String>
    private val upstreams = mutableMapOf<String, SignalStrength>()

    init {
        val (node, labels) = input.split(" -> ")
        destinations = labels.split(", ")
        label = node.drop(1)
    }

    override fun process(signal: Signal) {
        upstreams[signal.src] = signal.strength
        val signals = destinations.map { Signal(label, it, getStrength()) }
        network.send(signals)
    }

    private fun getStrength(): SignalStrength {
        return if (upstreams.values.all { it == SignalStrength.HI }) SignalStrength.LO else SignalStrength.HI
    }


    override fun registerUpstream(n: Node) {
        upstreams[n.label] = SignalStrength.LO
    }

    override fun getDestinations(): List<String> {
        return destinations
    }
}

private class FlipFlopNode(input: String, override val network: Network): Node {
    override val label: String
    private val destinations: List<String>

    private enum class State {
        ON, OFF
    }
    private var state: State = State.OFF


    init {
        val (node, labels) = input.split(" -> ")
        destinations = labels.split(", ")
        label = node.drop(1)
    }

    override fun process(signal: Signal) {
        if (signal.strength == SignalStrength.HI) {
            return
        }

        val strength = flipStateAndReturnStrength()

        val signals = destinations.map { Signal(label, it, strength) }
        network.send(signals)
    }

    override fun registerUpstream(n: Node) {}

    override fun getDestinations(): List<String> {
        return destinations
    }

    private fun flipStateAndReturnStrength(): SignalStrength {
        return when (state) {
            State.ON -> {
                state = State.OFF
                SignalStrength.LO
            }
            State.OFF -> {
                state = State.ON
                SignalStrength.HI
            }
        }
    }
}

private data class Signal(val src: String, val dst: String, val strength: SignalStrength)

private enum class SignalStrength {
    LO,
    HI
}
