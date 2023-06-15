package fa;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class DfaGenerator {

    private DFA dfa;

    public DfaGenerator(String pathToFile, int numStates, Set<String> alphabet, int counter ) {
        generateDFA(numStates, alphabet,counter);
        while(true){
            if(containsUnreachableState(this.dfa)){
                generateDFA(numStates, alphabet,counter);
            }
            else{
                dfaToFile(pathToFile, numStates, alphabet, counter);
                break;
            }
        }
    }

    protected void generateDFA(int numStates, Set<String> alphabet, int counter){
        this.dfa = new DFA();
        this.dfa.setDfa(new State[numStates]);
        for (int i = 0; i < numStates; i++) { //add id to every state of dfa
            this.dfa.partitionToDFA()[i] = new State(i);
        }
        this.dfa.setAlphabet(alphabet);
        Random random = new Random();
        int number = random.nextInt(numStates);
        this.dfa.setInitialState(this.dfa.getStateById(number));
        for (int i = 0; i < numStates; i++) { //i je index aktualne generovaneho stavu
            for (String letter : alphabet) {
                number = random.nextInt(numStates);
                this.dfa.partitionToDFA()[number].addTransitionFrom(letter, this.dfa.partitionToDFA()[i]);
                this.dfa.partitionToDFA()[i].addTransitionTo(letter, this.dfa.partitionToDFA()[number]);
                this.dfa.addNonFinalState(this.dfa.partitionToDFA()[i]);
            }
        }
        int finalStatesNumber = random.nextInt(numStates - 1) + 1;
        for (int i = 0; i < finalStatesNumber; i++) {
            number = random.nextInt(numStates);
            this.dfa.addFinalState(this.dfa.partitionToDFA()[number]);
            this.dfa.getNonFinalStates().remove(this.dfa.partitionToDFA()[number]);
        }
    }

    protected void dfaToFile(String pathToFile, int numStates, Set<String> alphabet, int index){
        Scanner scanner = new Scanner(System.in);
        boolean b = true;
        String name = ".";
        while(b){
            System.out.println("enter the file name for new generated automaton:" );
            name = scanner.nextLine();
            while(!name.matches("[a-zA-Z0-9]*") || name.isEmpty()){
                System.out.println("name should contain letters and/or numbers only");
                name = scanner.nextLine();
            }
            File f = new File(pathToFile+"/"+name+".txt");
            if(!f.exists()){
                b = false;
            }
            else{
                System.out.println("file already exists, enter another NEW name");
            }
        }
        File file = new File(pathToFile +"/"+name+".txt"); //initialize File object and passing path as argument
        boolean result;
        try {
            result = file.createNewFile();  // creates a new file
            if (result) {     // test if successfully created a new file
                FileWriter fstream = new FileWriter(file);
                fstream.write(numStates + "\n");
                fstream.write("start " + dfa.getInitialState().getId() + "\n");
                for (int i = 0; i < numStates; i++) { //i is the index of state that is being generated
                    for (String letter : alphabet) {
                        State state = this.dfa.getStateById(i);
                        int integer =state.getTransitionsTo().get(letter).iterator().next();
                        fstream.write(i + " " + letter + " " +integer+ "\n");
                    }
                }
                fstream.write("final");
                for (State finalState : this.dfa.getFinalStates()) {
                    fstream.write(" " + finalState.getId());
                }
                fstream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();    //prints exception if any
        }
    }

    public static boolean containsUnreachableState(DFA dfa){
        return dfa.containsUnreachableState();
    }

    public DFA getDfa() {
        return dfa;
    }
}
