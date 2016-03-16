package logic;

import java.io.*;
import java.util.ArrayList;


/**
 * Created by Giuseppe on 16/03/2016.
 */
public class BadWordsFilter {

    private static final String filePath = "/Shortify-server/src/main/java/logic/badwords_eng.txt";
    private static final ArrayList<String> badwords = new ArrayList<String>();
    static {

        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(System.getProperty("user.dir").replace("/target", "") + filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String str;

        try {
            while((str = in.readLine()) != null){
                badwords.add(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        System.out.println("Loading badwords list");
    }

    public static void print(){
        System.out.println(badwords.toString());
    }

    public static boolean isBadword(String word){
        for (String badword : badwords){
            if (word.contains(badword))
                return true;
        }
        return false;
    }
}
