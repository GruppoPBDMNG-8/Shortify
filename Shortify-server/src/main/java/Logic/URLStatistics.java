package logic;

import dao.RedisDAO;
import entity.Click;
import exceptions.EmptyClicksException;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Giuseppe on 09/03/2016.
 */
public class URLStatistics {

    private String longURL;

    private String URLCreationDate;
    private String LastClickDate;
    private String LastClickLocation;

    private int clicksSize;
    private HashMap<String,Integer> UserAgentsClicks = new HashMap<String,Integer>();
    private HashMap<String,Integer> UserLocationClicks = new HashMap<String,Integer>();


    public URLStatistics(String shortURL, String longURL) throws EmptyClicksException {
        this.longURL = longURL;
        List<Click> clickList = loadClicks(shortURL);
        System.out.println(clickList.toString());
        if (clickList.isEmpty()) throw new EmptyClicksException("Internal server error, please try again later");
        else calculate(clickList);

    }

    private List<Click> loadClicks (String shortURL){
        List<Click> clicksList = new ArrayList<>();
        Gson gson = new Gson();
        List<String> jsonClicks = RedisDAO.getAllClicks(shortURL);

        for ( String stringClick : jsonClicks){
            clicksList.add(gson.fromJson(stringClick,Click.class));
        }
        return clicksList;
    }

    private void calculate(List<Click> clicksList){
        clicksSize = clicksList.size() - 1;
        URLCreationDate = clicksList.get(0).getDate();
        LastClickDate = clicksList.get(clicksList.size()-1).getDate();
        LastClickLocation = clicksList.get(clicksList.size()-1).getLocation();

        clicksList.remove(0);
        for(Click click : clicksList){
            Integer i = UserAgentsClicks.getOrDefault(click.getUserAgent(),0);
            Integer j =  UserLocationClicks.getOrDefault(click.getLocation(),0);
            UserAgentsClicks.put(click.getUserAgent(), i+1);
            UserLocationClicks.put(click.getLocation(), j+1);
        }
    }
}
