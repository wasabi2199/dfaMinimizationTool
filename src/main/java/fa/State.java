
package fa;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class State {
    private int id;
    private Map<String, Set<Integer>> transitionsTo;
    private Map<String, Set<Integer>> transitionsFrom;
    private Alias alias;

    public State(int i){
        id = i;
        transitionsTo = new HashMap<String, Set<Integer>>();
        transitionsFrom = new HashMap<String, Set<Integer>>();
        alias = new Alias();
    }

    public void addTransitionTo(String letter, State state){
        if (!transitionsTo.containsKey(letter)) {
            transitionsTo.put(letter, new HashSet<Integer>());
        }
        transitionsTo.get(letter).add(state.getId());
    }
    public void addTransitionTo(String letter, int id){
        if (!transitionsTo.containsKey(letter)) {
            transitionsTo.put(letter, new HashSet<Integer>());
        }
        transitionsTo.get(letter).add(id);
    }

    public void addTransitionFrom(String letter, State state){
        if (!transitionsFrom.containsKey(letter)) {
            transitionsFrom.put(letter, new HashSet<Integer>());
        }
        transitionsFrom.get(letter).add(state.getId());
    }
    public void addTransitionFrom(String letter, int id){
        if (!transitionsFrom.containsKey(letter)) {
            transitionsFrom.put(letter, new HashSet<Integer>());
        }
        transitionsFrom.get(letter).add(id);
    }
    public int getId() {
        return id;
    }

    public Map<String, Set<Integer>> getTransitionsTo() {
        return transitionsTo;
    }

    public Map<String, Set<Integer>> getTransitionsFrom() {
        return transitionsFrom;
    }

    public State getState() {
        State s = new State(this.id);
        s.transitionsFrom = this.transitionsFrom;
        s.transitionsTo = this.transitionsTo;
        s.alias = this.alias;
        return s;
    }

    public Alias getAlias() {
        return alias;
    }

    public void setTransitionsTo(Map<String, Set<Integer>> transitionsTo) {
        this.transitionsTo = transitionsTo;
    }

    public void setTransitionsFrom(Map<String, Set<Integer>> transitionsFrom) {
        this.transitionsFrom = transitionsFrom;
    }
}
