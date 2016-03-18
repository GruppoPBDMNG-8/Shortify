package utilities;

import exceptions.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Random;

import com.google.common.net.InetAddresses;
import logic.Services;

/**
 * Created by Giuseppe on 15/03/2016.
 */
public class Populator {

    private HashMap<String,String> URLs = new HashMap<String,String>();

    public Populator(int nURLs, int nClicks){
        System.out.println("Populating db...");
        long startTime = System.currentTimeMillis();
        if (!Services.DBisPopulated()){
            insertCustomURLs();
            insertRandomURLs(nURLs);
            insertClicks(nClicks);
            Services.setDBisPopulated();
            System.out.println("...db populated with " + nURLs + "URLs and " + nClicks + " clicks" );
            //System.out.println(URLs.toString());
        }
        else System.out.println("...db already populated");
        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("time taken:" + totalTime);
    }

    private String generateIp(){
        Random rnd = new Random();
        String ipString = InetAddresses.fromInteger(rnd.nextInt()).getHostAddress();
        return ipString;
    }

    private String generateUserAgent(){
        Random rnd = new Random();
        String browsers[] = new String[5];
        browsers[0] = "Chrome";
        browsers[1] = "Firefox";
        browsers[2] = "Safari";
        browsers[3] = "Opera";
        browsers[4] = "MSIE";

        return browsers[rnd.nextInt(5)];
    }

    private void insertCustomURLs(){

        URLs.put("http://www.google.it","example");
        URLs.put("http://www.facebook.com","face");
        URLs.put("http://www.redis.io","redis");
        URLs.put("https://www.youtube.com/watch?v=dQw4w9WgXcQ","rick");
        URLs.put("http://www.reddit.com","reddit");
        URLs.put("http://www.uniba.it/ricerca/dipartimenti/informatica","dib");
        for(String key : URLs.keySet()){
            try {
                String shortURL = Services.setShortURL(key,URLs.get(key),generateIp(),generateUserAgent());
                JsonObject jsonRequest = new Gson().fromJson(shortURL,JsonObject.class);
                URLs.put(key,jsonRequest.get("shortURL").getAsString());
            } catch (CustomUrlUnavailableException e) {
                e.printStackTrace();
            } catch (MaxAttemptsException e) {
                e.printStackTrace();
            } catch (BlankUrlException e) {
                e.printStackTrace();
            } catch (BadWordException e) {
                e.printStackTrace();
            }
        }


    }

    private void insertRandomURLs(int n){
        for (int i = 0; i < n; i++){
            String longURL = generateRandomURL();
            try {
                String shortURL = Services.setShortURL(longURL,"",generateIp(),generateUserAgent());
                JsonObject jsonRequest = new Gson().fromJson(shortURL,JsonObject.class);
                URLs.put(longURL,jsonRequest.get("shortURL").getAsString());
            } catch (CustomUrlUnavailableException e) {
                e.printStackTrace();
            } catch (MaxAttemptsException e) {
                e.printStackTrace();
            } catch (BlankUrlException e) {
                e.printStackTrace();
            } catch (BadWordException e) {
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



