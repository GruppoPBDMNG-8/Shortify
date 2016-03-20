package entity;

import utilities.IpLocationServices;
import com.maxmind.geoip2.exception.GeoIp2Exception;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Entity which models a click from a user to a short url. Stores user ip, location, browser and date/time of the click.
 * Created by Giuseppe Perniola on 08/03/2016.
 */
public class Click {

    private String ip;
    private String location;
    private String userAgent;
    private String date;

    /**
     * Constructor
     * @param new_ip
     * @param new_userAgent
     */
    public Click(String new_ip, String new_userAgent){
        this.ip = new_ip;
        setLocation(new_ip);
        setDate();
        setUserAgent(new_userAgent);
    }

    /**
     * Given an ip, finds the country associated to it and sets the location field in the object.
     * @param ip
     */
    public void setLocation(String ip){
        String loc = "Unknown";
        IpLocationServices ipLoc = new IpLocationServices();
        try {
            loc = ipLoc.getCountry(ip);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeoIp2Exception e) {
            //e.printStackTrace();
        }
        this.location = loc;
    }

    /**
     * Given a userAgent string, finds which browser is associated to it and sets the userAgent field in the object.
     * @param new_userAgent
     */
    public void setUserAgent(String new_userAgent){
        String userAgent = "Unknown";
        if (new_userAgent.contains("Chrome")) { userAgent = "Chrome";}
        else if (new_userAgent.contains("Safari")) { userAgent = "Safari";}
            else if (new_userAgent.contains("Firefox")) { userAgent = "Firefox";}
                else if (new_userAgent.contains("Opera")) { userAgent = "Opera";}
                     else if (new_userAgent.contains("MSIE") ||
                                new_userAgent.contains("Trident/7.0")) { userAgent = "Internet Explorer";}
        this.userAgent = userAgent;
    }

    /**
     * Calculates the current date and time and sets the date field in the object.
     */
    public void setDate(){
        LocalDateTime nowTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
        this.date = nowTime.format(formatter);
    }


    public String getIp(){
        return this.ip;
    }
    public String getLocation(){
        return this.location;
    }
    public String getUserAgent(){
        return this.userAgent;
    }
    public String getDate(){
        return date.toString();
    }

    public String toString(){
        return "(ip:" + this.getIp() + ", date:" + this.getDate() + ", location:" + this.getLocation() + ", userAgent:" + getUserAgent()+")";
    }

}