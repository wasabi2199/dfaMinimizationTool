package helpers;

import fa.DFA;
import fa.State;
import fa.WaitingSet;

import java.util.Queue;
import java.util.Set;

public class OutputFormat {

    public static String partitionOutputHopcroft(Set<Set<State>> P){
        String stringP = "  P   =  ";
        return partitionOutput(P, stringP);
    }

    public static String partitionOutputMoore(Set<Set<State>> P){
        String stringP = "";
        return partitionOutput(P, stringP);
    }

    private static String partitionOutput(Set<Set<State>> P, String stringP) {
        int counter1 = 1;
        for(Set<State> p : P){
            int counter2 = 1;
            for(State state : p){
                stringP += state.getId();
                if(counter2<p.size()) {
                    stringP += ",";
                }
                counter2++;
            }
            if(counter1<P.size()) {stringP += "|";}
            counter1++;
        }
        return stringP;
    }

    public static String waitingSetOutput(Queue<WaitingSet> w){
        String cur = "  W   = ";
        int counter = 1;
        for(WaitingSet set : w){
            cur += "(";
            int counter2 = 1;
            for(State s : set.getSet()){
                cur+= s.getId();
                if(counter2 < set.getSet().size()){
                    cur += ",";
                }
                counter2++;
            }
            if(counter <= w.size()){
                cur += "; " + set.getLetter()+") ";
            }
            counter++;
        }
        return cur;
    }

    public static String currentOutput(Set<State> current){
        String cur = "C = ";
        int counter = 1;
        for(State s : current){
            cur+= s.getId();
            if(counter < current.size()){
                cur += ",";
            }
            counter++;
        }
        return cur;
    }

    public static String currentOutput(Set<State> current, String letter){
        String string = "(S,l) = (";
        int counter = 1;
        for(State s : current){
            string += s.getId();
            if(counter < current.size()){
                string += ",";
            }
            counter++;
        }
        string += "; " + letter + ")";
        return string;
    }

    public static String separate(DFA dfa){
        int n = dfa.partitionToDFA().length;
        String separator = "__________________";
        for(int i=0; i<n; i++){
            separator += "____";
        }
        return separator;
    }

    public static String splitterOutput(Set<State> splitter){
        String s ="splitter ";
        for(State state : splitter){
            s+=state.getId();
        }
        return s;
    }
}
