
package algorithms;

import fa.DFA;
import fa.NFA;
import fa.State;

import java.util.*;

public class Brzozowski extends Algorithm{
    Map<Integer, String> dictionary;
    Map<Set<State>, Integer> nfaSetToDfaId;
    DFA minimized;

    public Brzozowski(DFA dfa) {
        super(dfa);
    }

    public void minimization() {
        this.dictionary = new HashMap<Integer, String>();
        NFA rev1 = reversal(this.dfa);
        System.out.println("I. Reversal ");
        rev1.printNFA();

        DFA det1 = determinization(rev1);
        renameStates(det1, nfaSetToDfaId,1);
        System.out.println("II. Determinization ");

        det1.printDFA();
        NFA rev2 = reversal(det1);
        System.out.println("III. Reversal ");
        rev2.printNFA();

        minimized = determinization(rev2);
        renameStates(minimized, nfaSetToDfaId,2);
        System.out.println("IV. Determinization ");
        minimized.printDFA();

    }

    public NFA reversal(DFA dfaInput) {
        State[] nfaStates = new State[dfaInput.partitionToDFA().length];
        Set<State> initialStates = new HashSet<State>();
        State finalState = new State(0);
        Set<State> nonFinalStates = new HashSet<State>();
        int counter = 0;
        for (State state : dfaInput.partitionToDFA()) {
            State currentNewNfaState = new State(counter);
            currentNewNfaState.setTransitionsFrom(state.getTransitionsTo());
            currentNewNfaState.setTransitionsTo(state.getTransitionsFrom());
            if (dfaInput.getFinalStates().contains(dfaInput.partitionToDFA()[counter])) {
                initialStates.add(currentNewNfaState);
            }
            if (dfaInput.getInitialState().equals(dfaInput.partitionToDFA()[counter])) {
                finalState = currentNewNfaState;
            } else {
                nonFinalStates.add(currentNewNfaState);
            }
            nfaStates[counter] = currentNewNfaState;
            counter++;
        }
        System.out.println("Reversal: ");
        NFA nfa = new NFA(nfaStates, initialStates, dfaInput.getAlphabet(), finalState, nonFinalStates);
        return nfa;
    }

    public DFA determinization(NFA nfaInput) {
        this.nfaSetToDfaId = new HashMap<Set<State>, Integer>();
        Queue<Set<State>> nfaSet = new LinkedList<Set<State>>();
        Map<Integer, State> newDfaStates = new HashMap<Integer, State>();
        Set<State> newFinalStatesDFA = new HashSet<State>();
        Set<State> newNonfinalStatesDFA = new HashSet<State>();
        State nfaFinalState = nfaInput.getFinalState();
        newDfaStates.put(0, new State(0));
        nfaSet.add(nfaInput.getInitialStates());
        nfaSetToDfaId.put(nfaInput.getInitialStates(), 0);
        if (nfaInput.getInitialStates().contains(nfaInput.getFinalState())) {
            newFinalStatesDFA.add(newDfaStates.get(0));
        } else {
            newNonfinalStatesDFA.add(newDfaStates.get(0));
        }
        int counter = 1;
        int currId = 0;
        State currState = new State(0);
        Set<State> currSet = new HashSet<State>();
        for (int i = 0; i < counter; i++) {
            currSet = nfaSet.poll();
            for (String letter : nfaInput.getAlphabet()) {
                Set<State> set = deltaFunction(currSet, letter, nfaInput);
                if (!nfaSetToDfaId.containsKey(set)) {
                    currId = nfaSetToDfaId.size();
                    nfaSetToDfaId.put(set, currId);
                    nfaSet.add(set);
                    currState = new State(currId);
                    newDfaStates.put(currId, currState);
                } else {
                    currId = nfaSetToDfaId.get(set);
                    currState = newDfaStates.get(currId);
                }
                newDfaStates.get(i).addTransitionTo(letter, currId);
                currState.addTransitionFrom(letter, i);
                if (set.contains(nfaFinalState)) {
                    newFinalStatesDFA.add(currState);
                } else {
                    newNonfinalStatesDFA.add(currState);
                }
            }
            counter = nfaSetToDfaId.size();
        }
        DFA dfa1 = new DFA(newDfaStates.values().toArray(new State[nfaSetToDfaId.size()]), newDfaStates.get(0), nfaInput.getAlphabet(), newFinalStatesDFA, newNonfinalStatesDFA);
        return dfa1;
    }

    protected Set<State> deltaFunction(Set<State> states, String letter, NFA nfa) {
        Set<State> set = new HashSet<State>();
        Set<Integer> allIds = new HashSet<Integer>();
        for (State state : states) {
            try {
                allIds.addAll(state.getTransitionsTo().get(letter));
            } catch (Exception e) {
            }
        }
        for (Integer i : allIds) {
            set.add(nfa.getStateById(i));
        }
        return set;
    }

    protected void renameStates(DFA dfa1, Map<Set<State>, Integer> dictionary, int reverse) {
        Map<Integer, Set<State>> dictionary2 = new HashMap<Integer, Set<State>>();
        for (Map.Entry entry : dictionary.entrySet()) {
            dictionary2.put((Integer) entry.getValue(), (Set<State>) entry.getKey());
        }
        for (State state : dfa1.partitionToDFA()) {
            state.getAlias().setEnabled(true);
            Set<State> set = dictionary2.get(state.getId());
            String name = "{";
            for (State s : set) {
                if (reverse == 1) {
                    name += s.getId() + ", ";
                } else {
                    name += this.dictionary.get(s.getId()) + ", ";
                }
            }
            if (name.length() > 2) {
                name = name.substring(0, name.length() - 2) + "}";
            } else {
                name += "}";
            }
            if (dfa1.getInitialState().equals(state)) {
                state.getAlias().setName("->" + name);
            } else if (dfa1.getFinalStates().contains(state)) {
                state.getAlias().setName("*" + name);
            } else {
                state.getAlias().setName(name);
            }
            if (reverse == 1) {
                this.dictionary.put(state.getId(), name);
            }
        }
    }

    public DFA toDFA() {
        return this.minimized;
    }
}

