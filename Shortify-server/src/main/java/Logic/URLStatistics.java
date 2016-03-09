package Logic;

import DAO.RedisDAO;
import Entity.Click;
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
    private HashMap<String, Integer> UserAgentsClicks;
    private HashMap<String,Integer> UserLocationClicks;


    public URLStatistics(String shortURL, String longURL){
        this.longURL = longURL;
        List<Click> clickList = loadClicks(shortURL);
        calculate(clickList);

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
        LastClickDate = clicksList.get(clicksList.size()).getDate();
        LastClickLocation = clicksList.get(clicksList.size()).getLocation();

        clicksList.remove(0);
        for(Click click : clicksList){
            UserAgentsClicks.put(click.getUserAgent(), UserAgentsClicks.getOrDefault(click.getUserAgent(),1)+1);
            UserLocationClicks.put(click.getUserAgent(), UserLocationClicks.getOrDefault(click.getUserAgent(),1)+1);
        }
    }
}
