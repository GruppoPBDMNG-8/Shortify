package Logic;

import DAO.RedisDAO;
import Exceptions.*;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by Giuseppe on 15/03/2016.
 */
public class Populator {

    private HashMap<String,String> URLs = new HashMap<String,String>();

    public void Populator(int nURLs, int nClicks){
        if (!RedisDAO.isPopulated()){
            insertCustomURLs();
            insertRandomURLs(nURLs);
            insertClicks(nClicks);
            RedisDAO.setPopulated();
        }
    }

    private String generateIp(){
        Random rnd = new Random();
        Integer n1 = rnd.nextInt(254);
        Integer n2 = rnd.nextInt(254);
        Integer n3 = rnd.nextInt(254);
        Integer n4 = rnd.nextInt(254);
        String ip = n1.toString() + "." + n2.toString() + "." + n3.toString() + "." + n4.toString();
        return ip;
    }

    private String generateUserAgent(){
        Random rnd = new Random();
        String browsers[] = new String[4];
        browsers[0] = "Chrome";
        browsers[1] = "Firefox";
        browsers[2] = "Safari";
        browsers[3] = "Opera";
        browsers[4] = "MSIE";

        return browsers[rnd.nextInt(5)];
    }

    private void insertCustomURLs(){

        URLs.put("http://www.google.it","example");
        URLs.put("http://www.facebook.com",null);
        URLs.put("http://www.redis.io",null);
        URLs.put("https://www.youtube.com/watch?v=dQw4w9WgXcQ",null);
        URLs.put("http://www.reddit.com",null);
        URLs.put("http://www.uniba.it/ricerca/dipartimenti/informatica",null);
        for(String key : URLs.keySet()){
            try {
                String shortURL = Services.setShortURL(key,URLs.get(key),generateIp(),generateUserAgent());
                URLs.put(key,shortURL);
            } catch (CustomUrlUnavailableException e) {
                e.printStackTrace();
            } catch (MaxAttemptsException e) {
                e.printStackTrace();
            } catch (BlankUrlException e) {
                e.printStackTrace();
            }
        }


    }

    private void insertRandomURLs(int n){
        for (int i = 0; i < n; i++){
            String longURL = generateRandomURL();
            try {
                String shortURL = Services.setShortURL(longURL,null,generateIp(),generateUserAgent());
                URLs.put(longURL,shortURL);
            } catch (CustomUrlUnavailableException e) {
                e.printStackTrace();
            } catch (MaxAttemptsException e) {
                e.printStackTrace();
            } catch (BlankUrlException e) {
                e.printStackTrace();
            }
        }
    }

    private String generateRandomURL(){
        char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        Integer len = 7 + random.nextInt(50);
        for (int i = 0; i < len; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String output = "http://" + sb.toString();
        return output;
    }

    private void insertClicks(int n){
        Random rnd = new Random();
        Object[] shortURLs = URLs.values().toArray();
        for (int i = 0; i < n; i++){
            String shortURL = shortURLs[rnd.nextInt(shortURLs.length)].toString();
            try {
                Services.redirectURL(shortURL,generateIp(),generateUserAgent());
            } catch (UrlNotPresentException e) {
                e.printStackTrace();
            } catch (EmptyClicksException e) {
                e.printStackTrace();
            }
        }
    }
}



