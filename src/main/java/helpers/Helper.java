package helpers;

import java.util.Queue;
import java.util.Scanner;

public class Helper {

    public static void nextStep(Queue<String> output){
        if(!output.isEmpty()){
            System.out.println(output.poll());
        }
    }

    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public static int pressToContinue(Queue<String> output){
        try{
            Scanner scanner = new Scanner(System.in);
            String string = scanner.nextLine();
            if(isNumeric(string)){
                if(Integer.parseInt(string) <= 0 ){
                    System.out.println("No step has been done, waiting for valid input");
                }
                else if(Integer.parseInt(string) > output.size()){
                    System.out.println("There are not that many steps left ");
                }
                else{
                    return Integer.parseInt(string);
                }
            }
            else if(string.trim().isEmpty()){
                return 1;
            }
            else {
                System.out.println("No step has been done, waiting for valid input");
            }
        }
        catch(Exception e){
        }
        return 0;
    }
}
