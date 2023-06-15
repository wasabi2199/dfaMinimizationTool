
package algorithms;

import fa.DFA;
import fa.State;
import helpers.Helper;
import helpers.OutputFormat;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class Moore extends PartitionBasedMinimizationAlgorithm {
    private Queue<Integer> indexes;

    public Moore(DFA dfa) {
        super(dfa);
        this.indexes = new LinkedList<Integer>();
        indexes.add(0);
    }

    public void  minimization(){
        P.add(new HashSet<State>(dfa.getNonFinalStates()));
        P.add(new HashSet<State>(dfa.getFinalStates()));
        boolean flag = true;
        Set<Set<State>> P1 = new HashSet<Set<State>>();
        int count=0;
        while(flag==true){
            P1.clear();
            for(Set<State> set : P){
                Set<State> newSet = new HashSet<State>();
                for(State state : set){
                    newSet.add(state);
                }
                P1.add(newSet);
            }
            this.output.add(((separate()+"\nC' = " + partitionOutput()+"\n"+separate())));
            for(Set<State> currentC : P1){
                for(String letter : dfa.getAlphabet()){
                    output.add(("P = "+partitionOutput()+"   l = "+letter+"   " + currentOutput(currentC)));
                    Set<State> splitter = inverseDeltaFunction(currentC, letter, dfa);
                    split( splitter);
                }
            }
            this.indexes.add(new Integer(count += P1.size()*dfa.getAlphabet().size()+1));
            if(P.size() == P1.size()){
                flag = false;
                break;
            }
        }
        printSteps();
    }

    public void split( Set<State> splitter){
        Set<Set<State>> newSet = new HashSet<Set<State>>();
        for(Set<State> partition : P){
            Set<State> tempSet = new HashSet<State>();
            tempSet.addAll(partition);
            tempSet.removeAll(splitter);
            if (!tempSet.containsAll(partition) && tempSet.size() != 0) {
                newSet.add(new HashSet<State>(tempSet));
                partition.removeAll(tempSet);
            }
            newSet.add(new HashSet<State>(partition));
        }
        P = newSet;
    }

    Set<State> inverseDeltaFunction(Set<State> states, String letter, DFA old){
        return DFA.inverseDeltaFunction(states,letter,old);
    }

    public void printSteps(){
        System.out.println("Total number of steps: "+(output.size()-indexes.size()+1)+"\nPress Enter to continue OR Enter how many steps you would like to move forward");
        int counter = 0 ;
        while(!output.isEmpty()){
            int i = Helper.pressToContinue(output);
            for(int j=0; j<i; j++){
                if(indexes.contains(counter)){
                    i++;
                }
                Helper.nextStep(output);
                counter++;
            }
        }
        System.out.println("");
    }

    private String currentOutput(Set<State> currentC) {
        return OutputFormat.currentOutput(currentC);
    }

    private String partitionOutput() {
        return OutputFormat.partitionOutputMoore(this.P);
    }

    private String separate() {
        return OutputFormat.separate(this.dfa);
    }

    public  DFA toDFA() {
        return DFA.partitionToDFA(this.P, this.dfa);
    }

}

