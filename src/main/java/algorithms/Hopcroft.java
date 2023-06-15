package algorithms;

import fa.DFA;
import fa.State;
import fa.WaitingSet;
import helpers.Helper;
import helpers.OutputFormat;

import java.util.*;

public class Hopcroft extends PartitionBasedMinimizationAlgorithm {

    public Hopcroft(DFA dfa){
        super(dfa);
    }

    public void minimization(){
        P.add(new HashSet<State>(dfa.getNonFinalStates()));  //1:
        P.add(new HashSet<State>(dfa.getFinalStates()));
        Set<State> M = min(dfa.getNonFinalStates(), dfa.getFinalStates() ); // 2:
        Queue<WaitingSet> waitingSet = new LinkedList<WaitingSet>();// 3:
        for(String letter : dfa.getAlphabet()){ // 4:
            waitingSet.add(new WaitingSet(M, letter));  //  5: W.push((M, l))
        }
        int i = 0;
        output.add(i+"\n"+partitionOutput()+"\n"+"(S,l) ="+"\n"+waitingSetOutput(waitingSet)+"\n"+separate());
        while(!waitingSet.isEmpty()) { // 7:
            Set<State> splitter = inverseDeltaFunction(waitingSet.peek(), dfa); //8: (S, l) = W.pop()
            String splittingOn = currentOutput(waitingSet.peek().getSet(), waitingSet.poll().getLetter());
            Set<Set<State>> toBeAdded = new HashSet<Set<State>>();
            for(Set<State> C : P) { //for all C in P for which C is split by (S, l) do
                Set<State> copy = new HashSet<State>();
                copy.addAll(C);
                List<Set<State>> split = split(C, splitter); // 10: (C1, C2) = c | (S, l)
                if(split != null) { // 11: replace C for C1 and C2 in P
                    C.removeAll(split.get(0));
                    toBeAdded.add(split.get(0));
                    Set<State> c3 = min(split.get(0), split.get(1)); //12: C3=min(C1,C2)
                    for (String letter : dfa.getAlphabet()) {  //13
                        for (WaitingSet w : waitingSet) {
                            if(w.getSet().containsAll(copy)){//14: for all (C, x) in W (where C is the same C as above) do
                                w.getSet().removeAll(c3);
                            }
                        }
                        waitingSet.add(new WaitingSet(c3, letter)); //17: W.push((C000, x))
                    }
                }
            }
            P.addAll(toBeAdded);
            output.add(++i+"\n"+partitionOutput()+"\n"+splittingOn+"\n"+waitingSetOutput(waitingSet)+"\n"+separate());
        }
        printSteps();
    }

    public Set<State> min(Set<State> set1, Set<State> set2){
        if(set1.size() > set2.size()){return  set2;}
        return set1;
    }

    Set<State> inverseDeltaFunction(WaitingSet w, DFA old){
        return DFA.inverseDeltaFunction(w.getSet(),w.getLetter(),old);
    }

    public List<Set<State>> split(Set<State> partition, Set<State> splitter){
        List<Set<State>> split = new ArrayList<Set<State>>();
        Set<State> temp = new HashSet<State>();
        temp.addAll(partition);
        temp.removeAll(splitter);
        if(temp.size()!=0 && temp.size() < partition.size()){
            split.add(new HashSet<State>(temp));
            partition.removeAll(temp);
            split.add(new HashSet<State>(partition));
            return split;
        }
        return null;
    }

    public void printSteps(){
        System.out.println("Total number of steps: "+(output.size()-1)+"\nPress Enter to continue OR Enter how many steps you would like to move forward");
        Helper.nextStep(output);
        while(!output.isEmpty()){
            int i = Helper.pressToContinue(output);
            for(int j=0; j<i; j++){
                Helper.nextStep(output);
            }
        }
    }

    private String currentOutput(Set<State> set, String letter) {
        return OutputFormat.currentOutput(set, letter);
    }

    private String waitingSetOutput(Queue<WaitingSet> waitingSet) {
        return OutputFormat.waitingSetOutput(waitingSet);
    }

    private String partitionOutput() {
        return OutputFormat.partitionOutputHopcroft(this.P);
    }

    private String separate() {
        return OutputFormat.separate(this.dfa);
    }

    public  DFA toDFA() {
        return DFA.partitionToDFA(this.P, this.dfa);
    }

}
