
package algorithms;

import fa.DFA;
import fa.State;

import java.util.*;

public class Watson extends Algorithm{

    HashMap<State, State> S;
    Set<Set<State>> results;


    public Watson(DFA dfa) {
        super(dfa);
        this.S = new HashMap<State,State>();
        this.results = new HashSet<Set<State>>();
    }

    public boolean equiv(State p, State q, int k){
        boolean eq;
        if(k == 0){
            return ((this.dfa.getNonFinalStates().contains(p) && this.dfa.getNonFinalStates().contains(q)) || (this.dfa.getFinalStates().contains(p) && this.dfa.getFinalStates().contains(q)));
        }
        else if(S.containsKey(p)){
            if (S.get(p).equals(q)){
                return true;
            }
        }
        else if(S.containsKey(q)){
            if (S.get(q).equals(p)){
                return true;
            }
        }
        eq = recursion(p,q,k);
        return eq;
    }

    public boolean recursion(State p, State q, int k){
        boolean eq;
        eq =((this.dfa.getNonFinalStates().contains(p) && this.dfa.getNonFinalStates().contains(q)) ||
                (this.dfa.getFinalStates().contains(p) && this.dfa.getFinalStates().contains(q)));
        S.put(p,q);
        for(String key: this.dfa.getAlphabet()){
            if(!eq){
                return false;
            }
            if (!q.getTransitionsTo().containsKey(key) && !p.getTransitionsTo().containsKey(key)) {
                continue;
            }
            if (!p.getTransitionsTo().containsKey(key) || ! q.getTransitionsTo().containsKey(key)) {
                return false;
            }
            int id1 = (p.getTransitionsTo().get(key).iterator().next());
            int id2 = (q.getTransitionsTo().get(key).iterator().next());
            eq = eq && equiv(this.dfa.getStateById(id1),this.dfa.getStateById(id2),k-1);
        }
        S.remove(p);
        return eq;
    }

    public void minimization(){
        int k = Math.max(0, dfa.partitionToDFA().length-2);
        for(int i = 0; i<dfa.partitionToDFA().length-1; i++){
            for(int j = i+1; j<dfa.partitionToDFA().length; j++){
                if(i<j){
                    S = new HashMap<State, State>();
                    if(equiv(dfa.getStateById(i), dfa.getStateById(j), k)==true){
                        System.out.println("Equiv("+dfa.getStateById(i).getId()+","+dfa.getStateById(j).getId()+") = "+true);
                        boolean added=false;
                        for(Set<State> set : results){
                            if(set.contains(dfa.getStateById(i))){
                                set.add(dfa.getStateById(j));
                                added = true;
                                break;
                            }
                        }
                        if(added == false){
                            HashSet<State> hs= new HashSet<State>();
                            hs.add(dfa.getStateById(i));
                            hs.add(dfa.getStateById(j));
                            results.add(new HashSet<State>(hs));
                        }
                    }
                    else{
                        System.out.println("Equiv("+dfa.getStateById(i).getId()+","+dfa.getStateById(j).getId()+") = "+false);
                    }
                }
            }
        }
        System.out.println(output());
    }

    public String output(){
        String string = "Equivalent states are: ";
        for(Set<State> set : results){
            String temp = "";
            for(State state : set){
                temp += state.getId()+", ";
            }
            string += "\n "+temp.substring(0, temp.length()-2);
        }
        string = "\n"+string+"\n";
        return string;
    }

    public DFA toDFA(){
        DFA dfa2 = new DFA();
        List<State> list = new ArrayList<State>();
        Map<State, Integer> dictionary = new HashMap<State, Integer>();
        int id=0;
        for(Set<State> set : results){
            for(State state : set){
                dictionary.put(state, id);
            }
            list.add(new State(id));
            id++;
        }
        for(State state : this.dfa.partitionToDFA()){
            if(!dictionary.containsKey(state)){
                dictionary.put(state, id);
                list.add(new State(id));
                id++;
            }
        }
        dfa2.setDfa(list.toArray(new State[list.size()]));
        dfa2.setAlphabet(this.dfa.getAlphabet());
        dfa2.setInitialState(dfa2.getStateById(dictionary.get(dfa.getInitialState())));
        for(State state : this.dfa.partitionToDFA()){
            if(this.dfa.getFinalStates().contains(state)){
                dfa2.getFinalStates().add(dfa2.getStateById(dictionary.get(state)));
            }
            else{
                dfa2.getNonFinalStates().add(dfa2.getStateById(dictionary.get(state)));
            }
            for(String letter : this.dfa.getAlphabet()){
                if(state.getTransitionsTo().containsKey(letter)){
                    int i = state.getTransitionsTo().get(letter).iterator().next();
                    dfa2.getStateById(dictionary.get(state)).addTransitionTo(letter,dictionary.get(dfa.getStateById(i)));
                }
                if(state.getTransitionsFrom().containsKey(letter)){
                    for(Integer in : state.getTransitionsFrom().get(letter)) {
                        dfa2.getStateById(dictionary.get(state)).addTransitionFrom(letter, dictionary.get(dfa.getStateById(in)));
                    }
                }
            }
        }
        return dfa2;
    }
}