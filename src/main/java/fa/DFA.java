package fa;

import java.util.*;

public class DFA {
    State dfa[];
    State initialState;
    Set<String> alphabet;
    Set<State> finalStates;
    Set<State> nonFinalStates;

    public DFA(){
        this.finalStates = new HashSet<State>();
        this.nonFinalStates = new HashSet<State>();
        this.alphabet = new HashSet<String>();
    }

    public DFA(State[] dfa, State initialState, Set<String> alphabet,
               Set<State> finalStates, Set<State> nonFinalStates) {
        this.dfa = dfa;
        this.initialState = initialState;
        this.alphabet = alphabet;
        this.finalStates = finalStates;
        this.nonFinalStates = nonFinalStates;
    }

    public DFA copy(){
        DFA copy = new DFA();
        copy.setAlphabet(new HashSet<String>(getAlphabet()));
        copy.setDfa(new State[partitionToDFA().length]);
        for(int i = 0; i< partitionToDFA().length; i++){
            copy.partitionToDFA()[i] = new State(i);
            if(getFinalStates().contains(partitionToDFA()[i])){
                copy.getFinalStates().add(copy.partitionToDFA()[i]);
            }
            else{
                copy.getNonFinalStates().add(copy.partitionToDFA()[i]);
            }
            if(i==getInitialState().getId()){
                copy.setInitialState(copy.partitionToDFA()[i]);
            }
            for(String letter : getAlphabet()){
                if(partitionToDFA()[i].getTransitionsFrom().containsKey(letter)){
                    copy.partitionToDFA()[i].getTransitionsFrom().put(letter,new HashSet<Integer>(partitionToDFA()[i].getTransitionsFrom().get(letter)));
                }
                if(partitionToDFA()[i].getTransitionsTo().containsKey(letter)){
                    copy.partitionToDFA()[i].getTransitionsTo().put(letter,new HashSet<Integer>(partitionToDFA()[i].getTransitionsTo().get(letter)));
                }
            }
        }
        return copy;
    }

    public void addFinalState(State s) {
        this.finalStates.add(s);
    }

    public void addNonFinalState(State s) {
        this.nonFinalStates.add(s);
    }

    public void addToAlphabet(String s){
        this.alphabet.add(s);
    }

    public State[] partitionToDFA() {
        return this.dfa;
    }

    public void setDfa(State[] dfa) {
        this.dfa = dfa;
    }

    public State getInitialState() {
        return initialState;
    }

    public void setInitialState(State state) {
        this.initialState=(state);
    }

    public Set<String> getAlphabet() {
        return alphabet;
    }

    public void setAlphabet(Set<String> alphabet) {
        this.alphabet = alphabet;
    }

    public Set<State> getFinalStates() {
        return finalStates;
    }

    public void setFinalStates(Set<State> finalStates) {
        this.finalStates = finalStates;
    }

    public Set<State> getNonFinalStates() {
        return nonFinalStates;
    }

    public State getStateById(int id){
        return this.dfa[id];
    }

    public void setNonFinalStates(Set<State> nonFinalStates) {
        this.nonFinalStates = nonFinalStates;
    }

    public static Set<State> inverseDeltaFunction(Set<State> states, String letter, DFA dfa){
        Set<State> set = new HashSet<State>();
        Set<Integer> allIds = new HashSet<Integer>();
        for (State state : states) {
            try {
                allIds.addAll(state.getTransitionsFrom().get(letter));
            } catch (Exception e) {}
        }
        for (Integer i : allIds) {
            set.add(dfa.getStateById(i));
        }
        return set;
    }

    public static DFA partitionToDFA(Set<Set<State>> partition, DFA old) {
        DFA dfa = new DFA();
        dfa.setAlphabet(old.getAlphabet());
        dfa.setDfa(new State[partition.size()]);
        Map<State, Integer> dictionary = new HashMap<State, Integer>();
        int id = 0;
        for (Set<State> set : partition) {
            dfa.partitionToDFA()[id] = new State(id);
            for (State state : set) {
                dictionary.put(state, id);//stav stareho automatu, id v novom
            }
            id++;
        }
        id=0;
        for (Set<State> set : partition) {
            if(set.contains(old.getInitialState())){
                dfa.setInitialState(dfa.getStateById(id));
            }
            for (State state : set) {
                if (old.getFinalStates().contains(state)) {
                    dfa.getFinalStates().add(dfa.partitionToDFA()[id]);
                } else {
                    dfa.getNonFinalStates().add(dfa.partitionToDFA()[id]);
                }
                for (String key : old.getAlphabet()) {
                    if (state.getTransitionsTo().containsKey(key)) {
                        Set<Integer> ID = state.getTransitionsTo().get(key); // ID contains ids in original dfa
                        for (int i : ID) {
                            int newID = dictionary.get(old.getStateById(i));
                            dfa.partitionToDFA()[id].addTransitionTo(key, newID); //put(letter, id in new dfa)
                            dfa.partitionToDFA()[newID].addTransitionFrom(key, dfa.partitionToDFA()[id]);
                        }
                    }
                }
            }
            id++;
        }
        return dfa;
    }

    public boolean containsUnreachableState(){
        Set<State> checked = new HashSet<State>();
        Stack<State> waiting = new Stack<State>();
        State current = this.getInitialState();
        if(current.getTransitionsTo()==null){return true;}
        checked.add(current);
        for(Map.Entry<String, Set<Integer>> entry : current.getTransitionsTo().entrySet()){
            for(Integer id : entry.getValue()){
                waiting.add(dfa[id]);
            }
        }
        while (!waiting.isEmpty()){
            current = waiting.pop();
            checked.add(current);
            for(Map.Entry<String, Set<Integer>> entry :current.getTransitionsTo().entrySet()){
                for(Integer id : entry.getValue()){
                    if(!checked.contains(dfa[id]) && !waiting.contains(dfa[id])){
                        waiting.add(dfa[id]);
                    }
                }
            }
        }
        if(checked.containsAll(Arrays.asList(dfa))){
            return false;
        }
        return true;
    }

    public void printDFA(){
        System.out.println("initial state: "+this.getInitialState().getId());
        String finalStates = "final states: ";
        String nonFinalStates = "nonfinal states: ";
        for(int i = 0; i< partitionToDFA().length; i++){
            if(getFinalStates().contains(partitionToDFA()[i])){
                finalStates += partitionToDFA()[i].getId()+", ";
            }
            else{
                nonFinalStates += partitionToDFA()[i].getId()+", ";
            }
        }
        System.out.println(finalStates.substring(0,finalStates.length()-2));
        System.out.println(nonFinalStates.substring(0,nonFinalStates.length()-2));
        for(State state : partitionToDFA()){
            if(state.getAlias().isEnabled() == true){
                System.out.println("id "+state.getId()+"   "+state.getAlias().getName());
            }
            else{
                System.out.println("id "+state.getId());
            }
            for(String s : getAlphabet()){
                if(state.getTransitionsTo().containsKey(s)){
                   String output = "  transition on: "+ s +"  to: ";
                   for(Integer i : state.getTransitionsTo().get(s)){
                       output += partitionToDFA()[i].getId() + ", ";
                   }
                    System.out.println(output.substring(0, output.length()-2));
                }
            }
        }
        System.out.println("\n\n");
    }
}
