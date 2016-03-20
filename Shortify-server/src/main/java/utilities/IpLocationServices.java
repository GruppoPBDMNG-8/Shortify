package utilities;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.record.Country;

/**
 * Contains functions to find the location of a given ip address
 * Created by Giuseppe Perniola on 08/03/2016.
 */
public class IpLocationServices {

    // A File object pointing to your GeoIP2 or GeoLite2 database
    private File database;
    private static final String dbPath = "/Shortify-server/src/main/java/utilities/files/GeoLite2-Country.mmdb";
    private DatabaseReader reader;

    /**
     * Constructor. Creates the DatabaseReader object.
     */
    public IpLocationServices() {
        database = new File(System.getProperty("user.dir").replace("/target", "") + dbPath);
        try {
            reader = new DatabaseReader.Builder(database).build();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Given an ip, finds which country is associated to it.
     * @param ip
     * @return the IsoCode of the country found from the ip.
     * @throws IOException
     * @throws GeoIp2Exception
     */
    public String getCountry(String ip) throws IOException, GeoIp2Exception {

        InetAddress ipAddress = InetAddress.getByName(ip);
        CountryResponse response = reader.country(ipAddress);
        Country country = response.getCountry();

        return country.getIsoCode();
    }
}
