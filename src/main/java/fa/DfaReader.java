package fa;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class DfaReader {

    private DFA dfa;

    public DfaReader() {
        this.dfa = new DFA();
    }

    public void readDFA(){
        readDFA(selectFile());
    }

    public void readDFA(String path){
        try {
            Scanner faReader = new Scanner(new File(path));
            int n = Integer.parseInt(faReader.nextLine());
            this.dfa.setDfa(new State[n]);
            for (int i = 0; i < n; i++) { //add id to every state of dfa
                this.dfa.partitionToDFA()[i] = new State(i);
            }
            while (faReader.hasNextLine()) {
                String data = faReader.nextLine();
                String temp[] = data.split(" ");
                if(!data.contains("start") && !data.contains("final")){ //add letters to alphabet
                    this.dfa.addToAlphabet(temp[1]);
                }
                if(data.startsWith("start")){
                    String[] initialStates = data.substring(6).split(" ");
                    for(int i=0; i<initialStates.length; i++){
                        dfa.setInitialState(this.dfa.partitionToDFA()[Integer.parseInt(initialStates[i])]);
                    }
                }
                else if(data.startsWith("final")){
                    String[] finalStates = data.substring(6).split(" ");
                    for(int i=0; i<finalStates.length; i++){
                        dfa.addFinalState(this.dfa.partitionToDFA()[Integer.parseInt(finalStates[i])]);
                        this.dfa.getNonFinalStates().remove(this.dfa.partitionToDFA()[Integer.parseInt(finalStates[i])]);
                    }
                }
                else{
                    int from = Integer.parseInt(temp[0]);
                    int to = Integer.parseInt(temp[2]);
                    this.dfa.partitionToDFA()[to].addTransitionFrom(temp[1], this.dfa.partitionToDFA()[from]);
                    this.dfa.partitionToDFA()[from].addTransitionTo(temp[1], this.dfa.partitionToDFA()[to]);
                    this.dfa.addNonFinalState(this.dfa.partitionToDFA()[from]);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static String selectFile(){
        String pathToFile = "";
        Scanner scanner = new Scanner(System.in);
        while(pathToFile.isEmpty()){
            System.out.println("enter valid absolute path to file  ");
            pathToFile = scanner.nextLine();
            if (!isValidPath(pathToFile)){
                System.out.println("invalid path to file ");
                pathToFile = "";
            }
        }
        return pathToFile;
    }

    public static boolean isValidPath(String pathToFile){
        File tempFile = new File( pathToFile );
        boolean exists = tempFile.exists();
        boolean isTextFile = (pathToFile.substring(pathToFile.length()-4,pathToFile.length()).equals(".txt"));
        return (exists && isTextFile);
    }


    public static boolean containsUnreachableState(DFA dfa){
        return dfa.containsUnreachableState();
    }

    public DFA getDfa(){
        return this.dfa;
    }
}
