package entity;

import logic.IpLocationServices;
import com.maxmind.geoip2.exception.GeoIp2Exception;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Giuseppe on 08/03/2016.
 */
public class Click {

    private String ip;
    private String location;
    private String userAgent;
    private String date;

    public Click(String new_ip, String new_userAgent){
        this.ip = new_ip;
        setLocation(new_ip);
        setDate();
        setUserAgent(new_userAgent);
    }

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

    private void setDate(){
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