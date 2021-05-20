import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;

public class Task2 {

    private static Boolean isParameterized(String query){
        Vector<String> split = splitQuery(query);
        for(String input : split){
            if(input.contains("WHERE") && input.contains("<input from user>")){
                return false;
            }
        }
        return true;
    }

    private static Vector<String> splitQuery(String query){
        Vector<String> list = new Vector<>();
        String[] temp = query.split("\n");
        list.addAll(Arrays.asList(temp));
        return list;
    }

    private static int checkUserInput(String input){
        int score = 0;


        if(input.contains("and")){
            System.out.println("and detected in input. Possible SQL Injection");
            score +=2;
        }

        if(input.contains("'") ){
            System.out.println("Single quote detected in input. Possible SQL Injection");
            score +=2;
        }

        //check for Tautology if both if statements trigger then a score of 10 is passed
        if(input.contains(" or ")){
            System.out.println("OR detected in user input. Possible Tautology attack");
            score += 3;
        }
        if(input.lastIndexOf("=") != -1){
            int index = input.lastIndexOf("=");
            score+= 2;
            if(input.charAt(index-2) == input.charAt(index+2)){
                System.out.println("Tautology Detected. Tautology attack almost guaranteed");
                score+=5;
            } else {
                //this can also include blind attacks
                System.out.println("= detected but no tautology. Could be a Blind attack");
            }
        }

        //check for union select
        if(input.contains("union") && input.contains("select")){
            System.out.println("Union Select detected. Union Select attack likely");
            score += 5;
        }

        //check for characters that comment out code
        if(input.contains("--") || input.contains("#") || input.contains("/*")){
            System.out.println("Input contains comment characters");
            score += 3;
        }

        //check for piggy-back attacks
        if(input.contains(";")){
            System.out.println("Semicolon detected. Piggy-Back attack likely");
            score += 5;
        }

        //check for time based attacks
        if(input.contains("sleep") || input.contains("wait") || input.contains("stop")){
            System.out.println("Timing keyword detected. Possible timing attack");
        }

        //check for alternate encoding
        if(input.contains("char(") || input.contains("ascii(") || input.contains("hex(") || input.contains("hexadecimal(")){
            System.out.println("Alternate encoding detected. Alternate encoding attack likely");
            score += 4;
        }

        return Math.min(score,10);
    }

    private static void testQuery(String query, String userInput){
        int score = 0;

        if(!isParameterized(query)){
            System.out.println("This statement is not parameterized. This is the simplest way to prevent SQLI");
            score += 1;
        }

        score += checkUserInput(userInput);
        score = Math.min(score, 10);
        System.out.println("Vulnerability Score: " + score);
    }

    public static void main(String[] args) {
        String query1 =
                "SELECT ItemDescription, ItemPrice\n" +
                "FROM Items\n" +
                "WHERE ItemName = <input from user>\n";

        String query2 =
                "SELECT Accounts\n" +
                "FROM Users\n" +
                "WHERE Username=<input from user> AND Password=<input from user>\n";

        Scanner scanner = new Scanner(System.in);

        System.out.println("Which Query do you want to use (query1/query2)");
        System.out.println("Or you can enter another query");
        String input = scanner.nextLine();

        String userInput = "";

        if(input.equalsIgnoreCase("query1")){
            System.out.println("Enter User Input");
            userInput = scanner.nextLine();
            userInput = userInput.toLowerCase();
            testQuery(query1, userInput);
        } else if(input.equalsIgnoreCase("query2")){
            System.out.println("Enter First Input");
            String input1 = scanner.nextLine();
            System.out.println("Enter Second Input");
            String input2 = scanner.nextLine();
            userInput = input1 + input2;
            userInput = userInput.toLowerCase();
            testQuery(query2, userInput);
        } else {
            System.out.println("Enter User Input (both in one string)");
            userInput = scanner.nextLine();
            userInput = userInput.toLowerCase();
            testQuery(input, userInput);
        }
    }
}
