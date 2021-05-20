import java.io.File;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Task1 {
    private static String[] splitEmail(String email){
        String[] split = new String[3];
        String[] name = email.split("@");
        split[0] = name[0];


        String[] address = name[1].split("\\.");

        split[1] = address[0];
        split[2] = address[1];
        return split;
    }

    private static Boolean containsKeyword(String name){
        //get keywords
        Vector<String> keywords = new Vector<>(0);
        try {
            File file = new File ("Whitelists/KeywordWhitelist.txt");
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()){
                keywords.add(sc.nextLine());
            }
        } catch(Exception e) {
            System.out.println("file not found!");
        }

        for (String keyword : keywords) {
            if (name.contains(keyword)) {
                System.out.println(keyword);
                return true;
            }
        }
        return false;
    }

    private static int checkName(String name){
        if(containsKeyword(name)){
            System.out.println("Keyword detected in email address");
            return 2;
        }
        return 0;
    }

    private static int checkAddress(String address){
        if(Substitution.containsSub(address)){
            System.out.println("Substitution detected in domain of email address");
            return 3;
        }
        return 0;
    }

    //if the domain is edu or gov then subtract 1 from score
    private static int checkDomain(String domain){
        if(domain.equalsIgnoreCase("gov")){
            return -1;
        }
        if(domain.equalsIgnoreCase("edu")){
            return -1;
        }
        return 0;
    }

    private static int checkEmail(String email){
        //split the email into its components
        String[] broken = splitEmail(email);
        String name = broken[0];
        String address = broken[1];
        String domain = broken[2];

        //check each component and return score
        int score = checkName(name) + checkAddress(address) + checkDomain(domain);
        return Math.max(score, 0);
    }

    private static int checkWords(String body){
        int score = 0;
        //get keywords
        Vector<String> keywords = new Vector<>(0);
        try {
            File file = new File ("Whitelists/BodyWhitelist.txt");
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()){
                keywords.add(sc.nextLine());
            }
        } catch(Exception e) {
            System.out.println("file not found!");
        }

        Boolean keywordUsed = false;

        System.out.println();

        for (String keyword : keywords) {
            if (body.toLowerCase().contains(keyword.toLowerCase())) {
                System.out.println(keyword);
                keywordUsed = true;
                score++;
            }
        }
        if(keywordUsed){
            System.out.println("Keyword in body detected");
        }
        return Math.min(score, 3);
    }

    private static Boolean startsWithNumber(String url){
        if(Character.isDigit(url.charAt(0))){
            return true;
        }
        return false;
    }


    private static int URL(String body){
        int score = 0;
        Vector<String> urls = new Vector<String>();
        Vector<String> keywords = new Vector<>();
        Boolean number = false;
        Boolean keywordUsed = false;

        //identify URLS
        //this regex was taken from this site and slightly modified: https://stackoverflow.com/questions/5713558/detect-and-extract-url-from-a-string
        Pattern pattern = Pattern.compile("https://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|][a-zA-Z0-9._%+-]+", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(body);
        if(matcher.find()){
            String URL = body.substring(matcher.start()+8,matcher.end());
            urls.add(URL);
        } else {
            System.out.println("No URLs found");
            return 0;
        }
        while(matcher.find(matcher.end())){
            String URL = body.substring(matcher.start()+8,matcher.end());
            urls.add(URL);
        }

        try {
            File file = new File ("Whitelists/UrlWhitelist.txt");
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()){
                keywords.add(sc.nextLine());
            }
        } catch(Exception e) {
            System.out.println("file not found!");
        }


        //for each url
        for(String url: urls){
            //check if url starts with number
            if(startsWithNumber(url)){
                number = true;
                score++;
            }

            //check url for keywords
            for(String keyword : keywords){
                if(url.contains(keyword)){
                    keywordUsed = true;
                    score++;
                }
            }
        }

        if(number){
            System.out.println("URL beginning with number detected");
        }
        if(keywordUsed){
            System.out.println("URL containing keyword detected");
        }

        return Math.min(score,2);
    }


    private static int checkBody(String body){
        int score = URL(body);
        score += checkWords(body);
        return score;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Email");
        String testEmail = scanner.nextLine();

        System.out.println("Enter Body");
        String testBody = scanner.nextLine();
        System.out.println();


//        String testEmail = "paypa1admin@email.com";
//        String testBody = "you have been entered into a raffle to win $100. This is urgent, you must act fast go to https://1egex101.com/ and https://test.com/ and https://foo.com/";tes

        int email = checkEmail(testEmail);
        int body = checkBody(testBody);
        int phishingScore = email + body;
        if(phishingScore > 0){
            System.out.println("This emails phishing score is " + phishingScore + " for the reasons  given above");
        } else {
            System.out.println("Nothing suspicious detected");
            System.out.println("Phishing score is " + phishingScore);
        }
    }
}
