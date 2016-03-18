package entity;

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


    public URLStatistics(String URL, List<String> stringClicks){
        this.longURL = URL;
        List<Click> clicksList = new ArrayList<>();
        Gson gson = new Gson();
        for ( String stringClick : stringClicks){
            clicksList.add(gson.fromJson(stringClick,Click.class));
        }
        calculate(clicksList);
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

    public String getLastClickLocation() {return LastClickLocation;}
    public String getLastClickDate() {return LastClickDate;}
    public String getURLCreationDate() {return URLCreationDate;}
    public String getLongURL() {return longURL;}
    public int getClicksSize() {return clicksSize;}
}
