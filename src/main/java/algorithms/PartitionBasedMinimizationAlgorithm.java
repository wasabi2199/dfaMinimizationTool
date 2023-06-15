package algorithms;

import fa.DFA;
import fa.State;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

abstract class PartitionBasedMinimizationAlgorithm extends Algorithm {
    protected Set<Set<State>> P;
    protected Queue<String> output;

    public PartitionBasedMinimizationAlgorithm(DFA dfa) {
        super(dfa);
        this.P = new HashSet<Set<State>>();
        this.output = new LinkedList<String>();
    }
}
