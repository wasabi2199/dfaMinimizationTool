import algorithms.*;
import fa.*;
import helpers.Helper;

import java.io.File;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        menu();
    }

    public static void menu(){
        while(true){
            System.out.println("\nWould you like to: \n1)generate new dfa(s) \n2)read existing ");
            int option = 0;
            Scanner scanner = new Scanner(System.in);
            while (option > 2 || option < 1){
                option = scanner.nextInt();
                if(option>2 || option<1){
                    System.out.println("invalid input");
                    System.out.println("\nWould you like to: \n1)generate new dfa(s) \n2)read existing ");
                }
            }
            if(option == 1){
                menu1();
            }
            if(option == 2){
                menu2();
            }
            System.out.println("\nWould you like to continue? \n[Y/y] - yes \n[N/n] - no ");
            char c = 'e';
            c = scanner.next().charAt(0);
            while(!(c == 'n' || c == 'N' || c == 'y' ||c == 'Y')){
                System.out.println("invalid input");
                System.out.println("\nWould you like to continue? \n[Y/y] - yes \n[N/n] - no  ");
                c = scanner.next().charAt(0);
            }
            if(c == 'n' || c == 'N'){
                break;
            }
        }
    }

    public static void menu2(){
        DfaReader dfaReader = new DfaReader();
        dfaReader.readDFA();
        DFA dfa = dfaReader.getDfa();
        int option = option();
        minimization(option, dfa);
    }

    public static void menu1(){
        Scanner scanner = new Scanner(System.in);
        String pathToFile ="";
        System.out.println("enter the file directory");
        while(pathToFile.isEmpty()){
            pathToFile = scanner.nextLine();
            boolean b = checkDirectory(pathToFile);
            if( b != true){
                b = createDirectory(pathToFile);
                if( b != true){
                    pathToFile = "";
                }
            }
        }
        System.out.println("enter the number of files to generate(how many automata you want to generate): ");
        int i = Integer.valueOf(scanner.nextLine());
        System.out.println("enter the number of states: ");
        int n = Integer.valueOf(scanner.nextLine());
        System.out.println("enter the alphabet size: ");
        int size = Integer.valueOf(scanner.nextLine());
        Set<String> alphabet = newAlphabet(size);
        DFA[] dfas = new DFA[i];
        for(int j=0; j<i; j++){
            DfaGenerator dfaGenerator = new DfaGenerator(pathToFile, n, alphabet, i);
            dfas[j] = dfaGenerator.getDfa();
        }
        int option;
        for(int j=0; j<dfas.length; j++){
            System.out.println(" .....Minimizing "+(j+1)+". automaton..... ");
            option = option();
            minimization(option, dfas[j]);
        }
    }


    public static boolean checkDirectory(String path){
        boolean exists = false;
        File directory = new File(path);
        if(directory.exists()){
            exists = true;
        }
        return exists;
    }

    public static boolean  createDirectory(String path){
        File file = new File(path);
        boolean bool = file.mkdirs();
        if(bool){
            System.out.println("Directory created successfully");
            return true;
        }
        System.out.println("Sorry couldn't create specified directory");
        return false;
    }

    public static boolean useAnotherAlgorithm(){
        char c = 'e';
        Scanner scanner = new Scanner(System.in);
        System.out.println("Would you like to try another algorithm for this dfa? \n[Y/y] - yes \n[N/n] - no  ");
        c = scanner.next().charAt(0);
        while(!(c == 'n' || c == 'N' || c == 'y' ||c == 'Y')){
            System.out.println("invalid input");
            System.out.println("Would you like to try another algorithm for this dfa? \n[Y/y] - yes \n[N/n] - no  ");
            c = scanner.next().charAt(0);
        }
        if(c == 'n' || c == 'N'){
            return false;
        }
        return true;
    }

    public static void minimization(int option, DFA dfa){
        DFA copy = dfa.copy();
        System.out.println("original DFA");
        dfa.printDFA();
        switch (option){
            case 1:
                Brzozowski brzozowski = new Brzozowski(copy);
                brzozowski.minimization();
                brzozowski.toDFA().printDFA();
                break;
            case 2:
                Hopcroft hopcroft = new Hopcroft(copy);
                hopcroft.minimization();
                hopcroft.toDFA().printDFA();
                break;
            case 3:
                Moore moore = new Moore(copy);
                moore.minimization();
                moore.toDFA().printDFA();
                break;
            case 4:
                Watson watson = new Watson(copy);
                watson.minimization();
                watson.toDFA().printDFA();
                break;
        }
        if(useAnotherAlgorithm()){
            option = option();
            minimization(option, dfa);
        }
    }

    public static int option(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("choose the algorithm:\n1)Brzozowski\n2)Hopcroft\n3)Moore\n4)Watson");
        String line = scanner.nextLine();
        while (!algorithmOptionValidation(line)){
            System.out.println("invalid input");
            System.out.println("choose the algorithm:\n1)Brzozowski\n2)Hopcroft\n3)Moore\n4)Watson");
            line = scanner.nextLine();
        }
        return Integer.parseInt(line);
    }

    public static boolean algorithmOptionValidation(String line){
        if(Helper.isNumeric(line) && Integer.parseInt(line)<=4 && Integer.parseInt(line)>0 ){
            return true;
        }
        return false;
    }

    public static Set<String> newAlphabet(int size){
        Set<String> a = new HashSet<String>();
        char c;
        for(int i=0; i< size; i++){
            c =(char) (i + 'a');
            a.add(String.valueOf(c));
        }
        return a;
    }

}
