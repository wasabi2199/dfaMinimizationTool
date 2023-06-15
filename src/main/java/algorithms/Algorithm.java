package algorithms;

import fa.DFA;

abstract class Algorithm {
    protected DFA dfa;

    public Algorithm(DFA dfa) {
        this.dfa = dfa;
    }
    public abstract void minimization();
}
