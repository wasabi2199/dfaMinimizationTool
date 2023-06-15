package fa;

import java.util.*;

public class NFA {
    private State nfa[];
    private Set<State> initialStates;
    private Set<String> alphabet;
    private State finalState;
    private Set<State> nonFinalStates;

    public NFA(){
        this.initialStates =new HashSet<State>();
        this.nonFinalStates = new HashSet<State>();
        this.alphabet = new HashSet<String>();
    }

    public NFA(State[] dfa, Set<State> initialStates, Set<String> alphabet, State finalState, Set<State> nonFinalStates) {
        this.nfa = dfa;
        this.initialStates = initialStates;
        this.alphabet = alphabet;
        this.finalState = finalState;
        this.nonFinalStates = nonFinalStates;
    }

    public State[] getNfa() {
        return nfa;
    }

    public void setNfa(State[] nfa) {
        this.nfa = nfa;
    }

    public Set<State> getInitialStates() {
        return initialStates;
    }

    public void setInitialStates(Set<State> initialStates) {
        this.initialStates = initialStates;
    }

    public Set<String> getAlphabet() {
        return alphabet;
    }

    public void setAlphabet(Set<String> alphabet) {
        this.alphabet = alphabet;
    }

    public State getFinalState() {
        return finalState;
    }

    public void setFinalState(State finalState) {
        this.finalState = finalState;
    }

    public Set<State> getNonFinalStates() {
        return nonFinalStates;
    }

    public void setNonFinalStates(Set<State> nonFinalStates) {
        this.nonFinalStates = nonFinalStates;
    }

    public State getStateById(int id){
        return this.nfa[id];
    }

    public void printNFA(){
        System.out.println("final state: "+getFinalState().getId());

        String initialStates = "initial states: ";
        String nonFinalStates = "nonfinal states: ";
        for(int i=0; i<getNfa().length; i++){
            if(getInitialStates().contains(getNfa()[i])){
                initialStates += getNfa()[i].getId()+", ";
            }
            else if(getNonFinalStates().contains(getNfa()[i])){
                nonFinalStates += getNfa()[i].getId()+", ";
            }
        }
        System.out.println(initialStates.substring(0,initialStates.length()-2));
        System.out.println(nonFinalStates.substring(0,nonFinalStates.length()-2));
        for(State state : getNfa()){
            System.out.println("id "+state.getId());
            for(String s : getAlphabet()){
                if(state.getTransitionsTo().containsKey(s)){
                    String output = "  transition on: "+ s +"  to: ";
                    for(Integer i : state.getTransitionsTo().get(s)){
                        output += getNfa()[i].getId() + ", ";
                    }
                    System.out.println(output.substring(0, output.length()-2));
                }
            }
        }
        System.out.println("\n\n");
    }
}
