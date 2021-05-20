import java.io.*;
import java.util.Scanner;
import java.util.Vector;
public class Substitution {
    private static Vector<String> check (Vector<String> list, String password){
        if(password.contains("!")){
            if (list.contains(password.replace('!', 'i'))) {

            } else {
                list.add(password.replace('!', 'i'));
            }
        }
        if(password.contains("1")){
            if (list.contains(password.replace('1', 'i'))) {

            } else {
                list.add(password.replace('1', 'i'));
            }
            if (list.contains(password.replace('1', 'l'))) {

            } else {
                list.add(password.replace('1', 'l'));
            }
        }
        if(password.contains("7")){
            if (list.contains(password.replace('7', 't'))) {

            } else {
                list.add(password.replace('7', 't'));
            }
        }
        if(password.contains("3")){
            if (list.contains(password.replace('3', 'e'))) {

            } else {
                list.add(password.replace('3', 'e'));
            }
        }
        if(password.contains("0")){
            if (list.contains(password.replace('0', 'o'))) {

            } else {
                list.add(password.replace('0', 'o'));
            }
        }
        if(password.contains("$")) {
            if (list.contains(password.replace('$', 's'))) {

            } else {
                list.add(password.replace('$', 's'));
            }
        }
        if(password.contains("5")){
            if (list.contains(password.replace('5', 's'))) {

            } else {
                list.add(password.replace('5', 's'));
            }
        }
        return list;
    }

    private static Vector<String> replaceCharacter(String name){
        Vector<String> list = new Vector<String>();

        //check initial name to get initial variation
        list.add(name);
        list = check(list,name);

        //loop through the list to get variations of the variations adding more to the list as it goes
        int i = 0;
        while(i<list.size()){
            list = check(list,list.get(i));
            i++;
        }
        list.remove(0);
        return list;
    }

    private static Vector<String> getOriginals(String path){
        Vector<String> originals = new Vector<>(0);
        try {
            File file = new File (path);
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()){
                originals.add(sc.nextLine());
            }
        } catch(Exception e) {
            System.out.println("file not found!");
        }
        return originals;
    }

    public static Boolean containsSub (String name){
        Vector<String> originals = getOriginals("Whitelists/NameWhitelist.txt");

        //replace relevant characters in name
        Vector<String> modified = replaceCharacter(name);

        //check against each original
        for (String original : originals) {
            if (modified.contains(original)) {
                return true;
            }
        }
        return false;
    }
}
